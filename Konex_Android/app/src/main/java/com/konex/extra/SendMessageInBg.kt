package com.konex.extra

import android.os.AsyncTask
import android.util.Log
import com.google.cloud.dialogflow.v2.*

class SendMessageInBg(
    private val botReply: BotReply,
    private val session: SessionName,
    private val sessionsClient: SessionsClient,
    private val queryInput: QueryInput
) : AsyncTask<Void?, Void?, DetectIntentResponse?>() {
    private val TAG = "async"


    override fun onPostExecute(response: DetectIntentResponse?) {
        //handle return response here
        botReply.callback(response)
    }

    override fun doInBackground(vararg params: Void?): DetectIntentResponse? {
        try {
            val detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build()
            return sessionsClient.detectIntent(detectIntentRequest)
        } catch (e: Exception) {
            Log.d(TAG, "doInBackground: " + e.message)
            e.printStackTrace()
        }
        return null
    }
}