package com.konex.extra

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.konex.R
import com.konex.model.Message

class ChatAdapter(val messageList: MutableList<Message>,val  context: Context) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.adapter_message_one, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message: String = messageList[position].message
        val isReceived: Boolean = messageList[position].isReceived
        if (isReceived) {
            holder.messageReceive.visibility = View.VISIBLE
            holder.messageSend.visibility = View.GONE
            holder.messageReceive.text = message
        } else {
            holder.messageSend.visibility = View.VISIBLE
            holder.messageReceive.visibility = View.GONE
            holder.messageSend.text = message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageSend: MaterialTextView
        var messageReceive: MaterialTextView

        init {
            messageSend = itemView.findViewById(R.id.message_send)
            messageReceive = itemView.findViewById(R.id.message_receive)
        }
    }
}
