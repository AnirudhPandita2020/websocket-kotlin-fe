package com.anirudh.websocket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anirudh.websocket.databinding.ActivityChatBinding
import com.anirudh.websocket.databinding.MessageViewBinding
import com.anirudh.websocket.dataclass.MessageList

class MessageAdapter(private val messageList:MessageList):RecyclerView.Adapter<MessageAdapter.viewholder>() {
    class viewholder(val binding: MessageViewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(MessageViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.binding.title.text = messageList[position].user_id
        holder.binding.message.text = messageList[position].message
    }

    override fun getItemCount() = messageList.size
}