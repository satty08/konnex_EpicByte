package com.konex.extra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.konex.R
import com.konex.model.AppList
import com.konex.model.BugListItem

class BugListAdapter(val context: Context, val bugList: MutableList<BugListItem>) : RecyclerView.Adapter<BugListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BugListAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.bug_list_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BugListAdapter.ViewHolder, position: Int) {
        holder.bindItems(bugList[position],context)
    }

    override fun getItemCount(): Int {
        return  bugList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName = itemView.findViewById<MaterialTextView>(R.id.app_name)
        val bugTitle = itemView.findViewById<MaterialTextView>(R.id.bug_title_text)
        val bugDescription = itemView.findViewById<MaterialTextView>(R.id.bug_des_text)
        val modelName = itemView.findViewById<MaterialTextView>(R.id.model_name_text)
        val screenSize = itemView.findViewById<MaterialTextView>(R.id.screen_size_text)
        val status = itemView.findViewById<MaterialTextView>(R.id.status_text)
        fun bindItems(bugItem: BugListItem, context: Context) {
            println("item added:${bugItem.toString()}")
            appName.text = bugItem.app
            bugTitle.text = bugItem.title
            bugDescription.text = bugItem.des
            modelName.text = bugItem.model
            screenSize.text = bugItem.screen
            status.text = bugItem.status

        }
    }
}