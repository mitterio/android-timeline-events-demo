package io.mitter.recipes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.mitter.models.mardle.messaging.Message
import kotlinx.android.synthetic.main.item_message_other.view.*
import kotlinx.android.synthetic.main.item_message_self.view.*

class ChatRecyclerViewAdapter(
    private val messageList: List<Message>,
    private val currentUserId: String,
    private val otherUserProfilePhoto: String
) : RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {
    private val MESSAGE_SELF_VIEW = 0
    private val MESSAGE_OTHER_VIEW = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = if (viewType == MESSAGE_SELF_VIEW) R.layout.item_message_self else R.layout.item_message_other
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMessage(messageList[position])
    }

    override fun getItemViewType(position: Int) = if (messageList[position].senderId.domainId() == currentUserId)
        MESSAGE_SELF_VIEW else MESSAGE_OTHER_VIEW

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindMessage(message: Message) {
            with(message) {
                if (senderId.domainId() == currentUserId) {
                    itemView?.selfMessageText?.text = textPayload
                } else {
                    itemView?.otherMessageText?.text = textPayload
                    Glide.with(itemView.context)
                        .load(otherUserProfilePhoto)
                        .circleCrop()
                        .into(itemView.senderImage)
                }
            }
        }
    }
}
