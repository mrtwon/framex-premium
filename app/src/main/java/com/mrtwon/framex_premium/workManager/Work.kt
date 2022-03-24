package com.mrtwon.framex_premium.workManager


import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.screen.mainActivity.MainActivity
import com.mrtwon.framex_premium.notificationAction.createNotification
import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.entity.OneEpisode
import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.usecase.*
import kotlinx.coroutines.*
import javax.inject.Inject

class Work(private val context: Context, param: WorkerParameters): Worker(context, param) {

    @Inject
    lateinit var addNotificationContent: AddNotificationContent
    @Inject
    lateinit var getSubscriptionList: GetSubscriptionList
    @Inject
    lateinit var updateSubscription: UpdateSubscriptionContent
    @Inject
    lateinit var getAllEpisodeOfSerial: GetAllEpisodeOfSerial

    private val notificationChannel = "New Content"
    private val titleNotification = "Новая серия"
    private val textNotificationFormat = "%s %s сезон %s серия уже доступна"
    private var pendingIntent: PendingIntent
    private val scope = CoroutineScope(Job() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> {} })

    init {
        context
            .appComponent
            .createWorkManagerComponent()
            .inject(this)

        val intent = Intent(context, MainActivity::class.java).putExtra("redirect", "FragmentSubscription")
        pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun doWork(): Result {
        stepStart()
        return Result.success()
    }
        // не принимай на свой счёт, у меня такое бывает и я не знаю причины)
    private fun stepStart() {
            getSubscriptionList(Unit, scope){
                it.fold(
                    {},
                    { subscriptions ->
                        for(subscription in subscriptions){
                            Log.i("self-work-manager","subscription ${subscription.ruTitle}")
                            getAllEpisodeOfSerial(
                                params = GetAllEpisodeOfSerial.Param(
                                    id = subscription.contentId
                                ),
                                scope = scope
                            ){ either ->
                                either.fold(
                                    {},
                                    { episodeOfSerial ->
                                        Log.i("self-work-manager", "from ${subscription.episodeCount-1} to ${episodeOfSerial.list.size-1} all ${episodeOfSerial.list.size}")
                                        if(episodeOfSerial.allEpisodeCount > subscription.episodeCount){
                                            stepTwo(infoBySerial = subscription, list = episodeOfSerial.list.subList(subscription.episodeCount, episodeOfSerial.list.size))
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }
    }

    private fun stepTwo(infoBySerial: Subscription, list: List<OneEpisode>){
        updateSubscription(
            params = UpdateSubscriptionContent.Param(
                subscription = infoBySerial.apply {
                    this.episodeCount = list.size
                }
            ),
            scope = scope
        )
        list.forEach {  oneEpisode ->
            stepThree(
                Notification(
                idContent = infoBySerial.contentId,
                season = oneEpisode.season,
                episode = oneEpisode.episode,
                ruTitle = infoBySerial.ruTitle
            ))
        }
    }


    private fun stepThree(notification: Notification){
                val text = String.format(
                    textNotificationFormat,
                    notification.ruTitle,
                    notification.season,
                    notification.episode
                )
                createNotification(text, titleNotification, notificationChannel, pendingIntent, context)
            addNotificationContent(
                params = AddNotificationContent.Param(
                    notification = notification
                ),
                scope = scope
            )
        }


    private fun log(s: String) {
        Log.i("self-worker", s)
    }

}