package com.konex.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.common.collect.Lists
import com.konex.R
import com.konex.activity.MainActivity
import com.konex.extra.BotReply
import com.konex.extra.ChatAdapter
import com.konex.extra.SendMessageInBg
import com.konex.model.Message
import kotlinx.android.synthetic.main.fragment_chat_bot.*
import kotlinx.android.synthetic.main.fragment_chat_bot.view.*
import java.util.*
import kotlin.collections.ArrayList


class ChatBotFragment : Fragment(),BotReply {
    var chatAdapter: ChatAdapter? = null
    lateinit var messageList: MutableList<Message>

    private val TAG = "chatBotFragment"
    //dialogFlow
    private var sessionsClient: SessionsClient? = null
    private var sessionName: SessionName? = null
    private val uuid = UUID.randomUUID().toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val chatBotView =  inflater.inflate(R.layout.fragment_chat_bot, container, false)
        (activity as MainActivity?)!!.delegate.setSupportActionBar(chatBotView.chat_bot_toolbar)
        messageList = ArrayList()
        chatBotView.btnSend.setOnClickListener(View.OnClickListener {
            val message: String = chatBotView.editMessage.text.toString()
            if (message.isNotEmpty()) {

                messageList.add(Message(message, false))
                editMessage.setText("")
                sendMessageToBot(message)
                Objects.requireNonNull(chatBotView.chatView.adapter).notifyDataSetChanged()
                Objects.requireNonNull(chatBotView.chatView.layoutManager)!!.scrollToPosition(
                    messageList.size - 1
                )
            } else {
                Toast.makeText(context, "Please enter text!", Toast.LENGTH_SHORT).show()
            }
        })
        chatBotView.chatView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        chatAdapter = ChatAdapter(messageList, chatBotView.context)
        chatBotView.chatView!!.adapter = chatAdapter
        setUpBot()
        return chatBotView
    }
    private fun setUpBot() {
        try {
            val stream = this.resources.openRawResource(R.raw.credential)
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"))
            val projectId = (credentials as ServiceAccountCredentials).projectId
            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings = settingsBuilder.setCredentialsProvider(
                FixedCredentialsProvider.create(credentials)
            ).build()
            sessionsClient = SessionsClient.create(sessionsSettings)
            sessionName = SessionName.of(projectId, uuid)
            Log.d(TAG, "projectId : $projectId")
        } catch (e: Exception) {
            Log.d(TAG, "setUpBot: " + e.message)
        }
    }

    private fun sendMessageToBot(message: String) {
        val input = QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build()
        SendMessageInBg(this, sessionName!!, sessionsClient!!, input).execute()
    }

    override fun callback(returnResponse: DetectIntentResponse?) {
        if (returnResponse != null) {
            val botReply = returnResponse.queryResult.fulfillmentText
            if (!botReply.isEmpty()) {
                messageList.add(Message(botReply, true))
                chatAdapter!!.notifyDataSetChanged()
                Objects.requireNonNull(chatView.layoutManager)!!
                    .scrollToPosition(messageList.size - 1)
            } else {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "failed to connect!", Toast.LENGTH_SHORT).show()
        }
    }


}