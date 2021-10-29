package com.mrtwon.framex_premium.FragmentSubscription

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.google.android.material.card.MaterialCardView
import com.mrtwon.framex_premium.ActivityWebView.ActivityWebView
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.Helper.SwipeListener
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.MyApplication
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.WorkManager.Work
import com.mrtwon.framex_premium.room.Notification
import com.mrtwon.framex_premium.room.Subscription
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import kotlinx.android.synthetic.main.one_element_notification.view.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

class FragmentSubscription:  Fragment(){
    val vm: SubscriptionVM by lazy { ViewModelProvider(this).get(SubscriptionVM::class.java) }
    val listNotification = arrayListOf<Notification>()
    val listSubscription = arrayListOf<Subscription>()
    lateinit var rv_notification: RecyclerView
    lateinit var rv_subscription: RecyclerView
    lateinit var helper_notify: TextView
    lateinit var helper_subscript: TextView
    lateinit var state_workmanager_online: TextView
    lateinit var state_workmanager_offline: TextView
    lateinit var layout_notification: LinearLayout
    private val NOTIFICATION_FORMAT = "%s сезон %s серия уже доступна"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_subscription = view.findViewById(R.id.rv_subscription)
        helper_notify = view.findViewById(R.id.helper_notification)
        helper_subscript = view.findViewById(R.id.helper_subscription)
        state_workmanager_online = view.findViewById(R.id.state_online)
        state_workmanager_offline = view.findViewById(R.id.state_offline)
        layout_notification = view.findViewById(R.id.layout_notification)
        rv_subscription.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rv_subscription.adapter = SubscriptionAdapter(listSubscription)
        view.test_send.setOnClickListener{
            val instance = WorkManager.getInstance(MyApplication.getInstance.applicationContext)

            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val myRequest = OneTimeWorkRequest.Builder(Work::class.java)
                .setConstraints(constraint)
                .addTag("test-subscription")
                .build()
            instance.enqueue(myRequest)

        }
        observerNotification()
        observerSubscription()
        helper_notify.visibility = View.GONE
        super.onViewCreated(view, savedInstanceState)
    }


    private fun addView(list: List<Notification>){
        for(one_element in list){
            val mView = layoutInflater.inflate(R.layout.test_one_element_notification, layout_notification, false)
            mView.ru_title.text = "\uD83C\uDFAC ${HelperFunction.cutNotificationTitle(one_element.ru_title)}"
            mView.season_and_episode.text = String.format(NOTIFICATION_FORMAT, one_element.season, one_element.series)
            mView.setOnTouchListener(SwipeListener(right = {
                mView.alpha = 0.7f
                ObjectAnimator.ofFloat(mView, "translationX", widthDisplayPx()).apply {
                    duration = 300
                    start()
                }.addListener(
                    onEnd = {
                        vm.removeNotification(one_element)
                        layout_notification.removeView(mView)
                    }
                )
            }))
            mView.findViewById<ImageView>(R.id.play).setOnClickListener{
                log("click")
                (requireActivity() as MainActivity).navController.navigate(R.id.fragmentAboutSerial, Bundle().apply {
                    putInt("id", one_element.content_id)
                })
            }
            layout_notification.addView(mView)
        }
    }

    override fun onStart() {
        showStatusWorkManager()
        super.onStart()
    }

    private fun observerSubscription(){
        vm.subscriptionListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                helper_subscript.visibility = View.VISIBLE
                rv_subscription.visibility = View.GONE
            } else {
                helper_subscript.visibility = View.GONE
                rv_subscription.visibility = View.VISIBLE
                listSubscription.clear()
                listSubscription.addAll(it)
                listSubscription.reverse()
                rv_subscription.adapter?.notifyDataSetChanged()
            }
        }
    }
    private fun observerNotification(){
        vm.notificationListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                log("notification action")
                layout_notification.visibility = View.GONE
                helper_notify.visibility = View.VISIBLE
            } else {
                log("notification action with data, size = ${it.size}")
                layout_notification.visibility = View.VISIBLE
                helper_notify.visibility = View.GONE
                layout_notification.removeAllViews()
                addView(it.reversed())
            }
        }

    }



    private fun showStatusWorkManager(){
        val instance = WorkManager.getInstance(requireContext())
        val workInfo = instance.getWorkInfosByTag("subscription")
        val state = workInfo.get()
        if(state.isNotEmpty() && state[state.lastIndex].state == WorkInfo.State.ENQUEUED){
            state_workmanager_online.visibility = View.VISIBLE
            state_workmanager_offline.visibility = View.GONE
        }else{
            state_workmanager_offline.visibility = View.VISIBLE
            state_workmanager_online.visibility = View.GONE
        }
    }


    inner class SubscriptionViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val delete: ImageButton = itemView.findViewById(R.id.btn_delete_subscription)
        fun bind(subscription: Subscription){
            Picasso.get().load(subscription.poster).into(poster)
            delete.setOnClickListener{
                vm.removeSubscription(subscription)
            }
            poster.setOnClickListener{
                val bundle = Bundle().apply { putInt("id", subscription.content_id) }
                (requireActivity() as MainActivity).navController.navigate(R.id.fragmentAboutSerial, bundle)
            }
        }
    }
    inner class SubscriptionAdapter(private val list: List<Subscription>): RecyclerView.Adapter<SubscriptionViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
            return SubscriptionViewHolder(layoutInflater.inflate(R.layout.one_element_subscription, parent, false))
        }

        override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
            holder.bind(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    private fun widthDisplayPx(): Float{
        return Resources.getSystem().displayMetrics.widthPixels.toFloat()
    }

    private fun log(s: String){
        Log.i("self-subscription", s)
    }
}