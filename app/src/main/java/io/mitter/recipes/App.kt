package io.mitter.recipes

import android.app.Application
import io.mitter.android.Mitter
import io.mitter.android.domain.model.MitterConfig
import io.mitter.android.domain.model.UserAuth
import io.mitter.models.mardle.messaging.*
import org.greenrobot.eventbus.EventBus

class App : Application() {
    lateinit var mitter: Mitter

    override fun onCreate() {
        super.onCreate()

        val mitterConfig = MitterConfig(
            applicationId = "Izr37-Vm7TS-U8cIu-sVtqj"
        )

        val userAuth = UserAuth(
            userId = "XQQ8U-7JuFc-tPLON-AdGMl",
            userAuthToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtaXR0ZXItaW8iLCJ1c2VyVG9rZW5JZCI6IkhYZnpWWVlQMHhwM1dqREgiLCJ1c2VydG9rZW4iOiJqNWltZWRpc2VvMTVlMGZqN250Mms1bTZsbSIsImFwcGxpY2F0aW9uSWQiOiJJenIzNy1WbTdUUy1VOGNJdS1zVnRxaiIsInVzZXJJZCI6IlhRUThVLTdKdUZjLXRQTE9OLUFkR01sIn0.W20UkfW622V9qcwn4YRIuPA9tNC2HjLM7PoTghjwTVn3SO-nwcxrPLuk-QGUiXhk4DSTt2PEqayg5R0bGyEdUg"
        )

        val userAuth2 = UserAuth(
            userId = "VMCDv-czm5Z-nMDOJ-jJ67Y",
            userAuthToken = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtaXR0ZXItaW8iLCJ1c2VyVG9rZW5JZCI6IkZLYXZnVUd1VmxMU01wTUMiLCJ1c2VydG9rZW4iOiI3OW9hbm1uNTBsb2RxdGVpYW04MTdiaGJzNCIsImFwcGxpY2F0aW9uSWQiOiJJenIzNy1WbTdUUy1VOGNJdS1zVnRxaiIsInVzZXJJZCI6IlZNQ0R2LWN6bTVaLW5NRE9KLWpKNjdZIn0.YW1oirrZIccioV5h5s7AnPAq6TrkflyKzGWk3r2BR7pJQuvYZijaaBb6RhDAXKEkQs9HXYxlvoYKZ5x1n8rcNQ"
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
            }

            override fun onNewMessageTimelineEvent(messageId: String, timelineEvent: TimelineEvent) {

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
