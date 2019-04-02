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
            userAuthToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtaXR0ZXItaW8iLCJ1c2VyVG9rZW5JZCI6IlJqS2h3OU9KRmJtR2xpSlgiLCJ1c2VydG9rZW4iOiI4ZnM3dXFqcGw1cWFrNXJ2cGM2OTU1cGxwOCIsImFwcGxpY2F0aW9uSWQiOiJJenIzNy1WbTdUUy1VOGNJdS1zVnRxaiIsInVzZXJJZCI6IlhRUThVLTdKdUZjLXRQTE9OLUFkR01sIn0.3I3Bq9LzQTUTYwYeQozH1Vid_EYZQeZC5I50GZ2MRRWImANSGXympbf_vE-3mBxwDQOb-w7hdPe4ae-pYDejFQ"
        )

        val userAuth2 = UserAuth(
            userId = "VMCDv-czm5Z-nMDOJ-jJ67Y",
            userAuthToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtaXR0ZXItaW8iLCJ1c2VyVG9rZW5JZCI6ImFiSGxDY0NUMVFoakIzRGwiLCJ1c2VydG9rZW4iOiI3Z3RxNmFvY2QxN2NjOWcyOWZxcG9mNG8wbCIsImFwcGxpY2F0aW9uSWQiOiJJenIzNy1WbTdUUy1VOGNJdS1zVnRxaiIsInVzZXJJZCI6IlZNQ0R2LWN6bTVaLW5NRE9KLWpKNjdZIn0.yH45RsM3cGpM6Gx81RFmGp3RFdjH3QGbXvMjYNxaRUOxcXO5-HXt0oAIpBfSZWWJUDQaTecdtFAy8m0LN1E7Bg"
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
