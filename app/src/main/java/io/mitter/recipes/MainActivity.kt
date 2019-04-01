package io.mitter.recipes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import io.mitter.android.Mitter
import io.mitter.android.error.model.base.ApiError
import io.mitter.data.domain.entity.EntityProfile
import io.mitter.models.mardle.messaging.Message
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private val channelId = "KNOqT-6GwKj-oVZG3-gws36"
    private val messageList: MutableList<Message> = mutableListOf()
    private lateinit var chatRecyclerViewAdapter: ChatRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mitter = (application as App).mitter
        val users = mitter.Users()
        val messaging = mitter.Messaging()
        EventBus.getDefault().register(this)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        chatRecyclerView?.layoutManager = linearLayoutManager

        messaging.getMessagesInChannel(
            channelId = channelId,
            onValueAvailableCallback = object : Mitter.OnValueAvailableCallback<List<Message>> {
                override fun onError(apiError: ApiError) {

                }

                override fun onValueAvailable(value: List<Message>) {
                    val messages = value.reversed()

                    messageList.addAll(messages)
                    chatRecyclerViewAdapter = ChatRecyclerViewAdapter(
                        messageList = messageList,
                        currentUserId = mitter.getUserId()
                    )

                    chatRecyclerView?.adapter = chatRecyclerViewAdapter
                }
            }
        )

        sendButton?.setOnClickListener {
            messaging.sendTextMessage(
                channelId = channelId,
                message = messageEditText.text.toString()
            )
            messageEditText.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewMessage(message: Message) {
        messageList.add(message)
        chatRecyclerViewAdapter.notifyItemInserted(messageList.size - 1)
        chatRecyclerView.scrollToPosition(messageList.size - 1)
    }
}
