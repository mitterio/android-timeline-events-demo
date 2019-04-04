package io.mitter.recipes

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import android.util.Log
import io.mitter.android.Mitter
import io.mitter.android.domain.model.LoggingLevel
import io.mitter.android.domain.model.MitterConfig
import io.mitter.android.domain.model.UserAuth
import io.mitter.models.mardle.messaging.*
import org.greenrobot.eventbus.EventBus

class App : Application() {
    lateinit var mitter: Mitter

    private val appLifecycleListener: AppLifecycleListener by lazy { AppLifecycleListener() }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleListener)

        val mitterConfig = MitterConfig(
            applicationId = "Izr37-Vm7TS-U8cIu-sVtqj",
            loggingLevel = LoggingLevel.FULL
        )

        val userAuth = UserAuth(
            userId = "XQQ8U-7JuFc-tPLON-AdGMl",
            userAuthToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtaXR0ZXItaW8iLCJ1c2VyVG9rZW5JZCI6InBsVGNVSm5YVktSZXlkV3QiLCJ1c2VydG9rZW4iOiJydWYyY20xczY2aTJwYW9laWpzczJrbjFvIiwiYXBwbGljYXRpb25JZCI6Ikl6cjM3LVZtN1RTLVU4Y0l1LXNWdHFqIiwidXNlcklkIjoiWFFROFUtN0p1RmMtdFBMT04tQWRHTWwifQ.N2WW4tbWkr8lGaP26wb7PIrkR6NlvNvVFZCAnEgxvHBtYDJILyAd7v2VSqZLTXrKi9O8HcZOliA64oe41j2cQQ"
        )

        val userAuth2 = UserAuth(
            userId = "VMCDv-czm5Z-nMDOJ-jJ67Y",
            userAuthToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtaXR0ZXItaW8iLCJ1c2VyVG9rZW5JZCI6IjJGUlFCeGd3aUx3cHR5U0giLCJ1c2VydG9rZW4iOiJzZ21ldTdmNDNhMHRuMW9zamltNjR2a3RpMyIsImFwcGxpY2F0aW9uSWQiOiJJenIzNy1WbTdUUy1VOGNJdS1zVnRxaiIsInVzZXJJZCI6IlZNQ0R2LWN6bTVaLW5NRE9KLWpKNjdZIn0.oE47AiUkqxVaEeINp2H2SyAgOkFKgZ5Z8zYyzlTEsQuwSBdsj0E6sJK7fqq0ecCpgM6jKx_d-a05EbgixxUtyA"
        )

        mitter = Mitter(
            context = this,
            mitterConfig = mitterConfig,
            userAuth = userAuth2
        )

        mitter.registerOnPushMessageReceivedListener(object : Mitter.OnPushMessageReceivedCallback {
            override fun onChannelStreamData(channelId: String, streamId: String, streamData: ContextFreeMessage) {

            }

            override fun onNewChannel(channel: Channel) {

            }

            override fun onNewChannelTimelineEvent(channelId: String, timelineEvent: TimelineEvent) {

            }

            override fun onNewMessage(channelId: String, message: Message) {
                EventBus.getDefault().post(message)

                if (message.senderId.domainId() != mitter.getUserId()) {
                    mitter.Messaging().addDeliveredTimelineEvent(
                        channelId = channelId,
                        messageIds = listOf(message.messageId)
                    )
                }
            }

            override fun onNewMessageTimelineEvent(messageId: String, timelineEvent: TimelineEvent) {
                EventBus.getDefault().post(
                    TimelineEventAdded(messageId, timelineEvent)
                )
            }

            override fun onParticipationChangedEvent(
                channelId: String,
                participantId: String,
                newStatus: ParticipationStatus,
                oldStatus: ParticipationStatus?
            ) {

            }

            override fun onTypingIndication(channelId: String, senderId: String) {
                if (mitter.getUserId() != senderId) {
                    EventBus.getDefault().post(TypingIndicator())
                }
            }
        })
    }
}
