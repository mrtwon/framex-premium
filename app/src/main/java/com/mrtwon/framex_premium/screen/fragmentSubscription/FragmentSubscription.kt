package com.mrtwon.framex_premium.screen.fragmentSubscription

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.Helper.SwipeListener
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.app.MyApplication
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.workManager.Work
import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_subscription.view.*
import kotlinx.android.synthetic.main.one_element_notification.view.*
import javax.inject.Inject

class FragmentSubscription:  Fragment(){
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var viewModelFactory: SubscriptionVM.Factory
    private val vm: SubscriptionVM by lazy { ViewModelProvider(this, viewModelFactory).get(SubscriptionVM::class.java) }
    private val listNotification = arrayListOf<Notification>()
    private val listSubscription = arrayListOf<Subscription>()
    private lateinit var rvNotification: RecyclerView
    private lateinit var rvSubscription: RecyclerView
    private lateinit var helperNotify: TextView
    private lateinit var helperSubscript: TextView
    private lateinit var stateWorkManagerOnline: TextView
    private lateinit var stateWorkManagerOffline: TextView
    private lateinit var layoutNotification: LinearLayout
    private val notificationFormat = "%s сезон %s серия уже доступна"


    override fun onAttach(context: Context) {
        context
            .appComponent
            .createSubscriptionComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvSubscription = view.findViewById(R.id.rv_subscription)
        helperNotify = view.findViewById(R.id.helper_notification)
        helperSubscript = view.findViewById(R.id.helper_subscription)
        stateWorkManagerOnline = view.findViewById(R.id.state_online)
        stateWorkManagerOffline = view.findViewById(R.id.state_offline)
        layoutNotification = view.findViewById(R.id.layout_notification)
        rvSubscription.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvSubscription.adapter = SubscriptionAdapter(listSubscription)
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
        helperNotify.visibility = View.GONE
        super.onViewCreated(view, savedInstanceState)
    }


    private fun addView(list: List<Notification>){
        for(one_element in list){
            val mView = layoutInflater.inflate(R.layout.test_one_element_notification, layoutNotification, false)
            mView.ru_title.text = "\uD83C\uDFAC ${HelperFunction.cutNotificationTitle(one_element.ruTitle ?: "Нет данных")}"
            mView.season_and_episode.text = String.format(notificationFormat, one_element.season, one_element.episode)
            mView.setOnTouchListener(SwipeListener(right = {
                mView.alpha = 0.7f
                ObjectAnimator.ofFloat(mView, "translationX", widthDisplayPx()).apply {
                    duration = 300
                    start()
                }.addListener(
                    onEnd = {
                        vm.removeNotification(one_element.id)
                        layoutNotification.removeView(mView)
                    }
                )
            }))
            mView.findViewById<ImageView>(R.id.play).setOnClickListener{
                log("click")
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.fragmentAboutSerial, Bundle().apply {
                    putInt("id", one_element.idContent)
                })
            }
            layoutNotification.addView(mView)
        }
    }

    override fun onStart() {
        showStatusWorkManager()
        super.onStart()
    }


    private fun observerSubscription(){
        vm.subscriptionLiveData.observe(viewLifecycleOwner){ liveData ->
            liveData.setLifecycleOwner(viewLifecycleOwner)
            liveData.observe(object: SubscriptionInterface.Observer<List<Subscription>>{
                override fun onChange(t: List<Subscription>) {
                    if(t.isEmpty()){
                        helperSubscript.visibility = View.VISIBLE
                        rvSubscription.visibility = View.GONE
                    }else{
                        listSubscription.clear()
                        listSubscription.addAll(t.reversed())
                        rvSubscription.adapter?.notifyDataSetChanged()
                        helperSubscript.visibility = View.GONE
                        rvSubscription.visibility = View.VISIBLE
                    }
                }

            })
        }
    }
    private fun observerNotification(){
        vm.notificationLiveData.observe(viewLifecycleOwner){ liveData ->
            liveData.setLifecycleOwner(viewLifecycleOwner)
            liveData.observe(object: SubscriptionInterface.Observer<List<Notification>>{
                override fun onChange(t: List<Notification>) {
                    if (t.isEmpty()) {
                        layoutNotification.visibility = View.GONE
                        helperNotify.visibility = View.VISIBLE
                    } else {
                        listNotification.clear()
                        listNotification.addAll(t)
                        addView(t.reversed())
                        layoutNotification.visibility = View.VISIBLE
                        helperNotify.visibility = View.GONE
                    }
                }

            })
        }
       /* vm.notificationListLiveData.observe(viewLifecycleOwner) {
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
        }*/

    }



    private fun showStatusWorkManager(){
        val instance = WorkManager.getInstance(requireContext())
        val workInfo = instance.getWorkInfosByTag("subscription")
        val state = workInfo.get()
        if(state.isNotEmpty() && state[state.lastIndex].state == WorkInfo.State.ENQUEUED){
            stateWorkManagerOnline.visibility = View.VISIBLE
            stateWorkManagerOffline.visibility = View.GONE
        }else{
            stateWorkManagerOffline.visibility = View.VISIBLE
            stateWorkManagerOnline.visibility = View.GONE
        }
    }


    inner class SubscriptionViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView){
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val delete: ImageButton = itemView.findViewById(R.id.btn_delete_subscription)
        fun bind(subscription: Subscription){
            picasso.load(subscription.posterPreview).into(poster)
            delete.setOnClickListener{
                vm.removeSubscription(subscription.contentId)
            }
            poster.setOnClickListener{
                val bundle = Bundle().apply { putInt("id", subscription.contentId) }
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.fragmentAboutSerial, bundle)
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