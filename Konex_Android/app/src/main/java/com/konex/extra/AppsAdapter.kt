package com.konex.extra

import android.content.Context
import android.content.Intent
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.util.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.konex.R
import com.konex.activity.BugReportingWindow
import com.konex.model.AppList
import java.util.*
import kotlin.collections.ArrayList

class AppsAdapter(val context: Context, val appList: MutableList<AppList>) : RecyclerView.Adapter<AppsAdapter.ViewHolder>(),Filterable{

    private var appFullList:Collection<AppList> = ArrayList(appList)
    val show = SparseBooleanArray()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.installed_app_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AppsAdapter.ViewHolder, position: Int) {
        if (!show.get(position, false)){
            holder.btn_layout.visibility = View.GONE
        } else {
            holder.btn_layout.visibility = View.VISIBLE
            println("else true entered at position : ${position}")
        }
        holder.bindItems(appList[position],context)
    }

    override fun getItemCount(): Int {
        return appList.size
    }
    override fun getFilter(): Filter {
        return  filter
    }
    private val filter:Filter = object:Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = arrayListOf<AppList>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(appFullList)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }

                for (item in appFullList) {
                    if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                    else if(item.name.toUpperCase(Locale.ROOT).contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            appList.clear()
            appList.addAll(results?.values as Collection<AppList>)
            notifyDataSetChanged()
        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val AppName = itemView.findViewById<TextView>(R.id.list_app_name)
        val packageName = itemView.findViewById<TextView>(R.id.app_package)
        val packageIcon = itemView.findViewById<ImageView>(R.id.app_icon)
        val btn_layout = itemView.findViewById<LinearLayout>(R.id.ll_btn)
        val openAppBtn = itemView.findViewById<MaterialButton>(R.id.open_app_btn)
        val reportBugBtn = itemView.findViewById<MaterialButton>(R.id.bug_report_btn)
        val mainLayout = itemView.findViewById<LinearLayout>(R.id.main_layout)
        val openReport = context as? SearchBarInterface
        fun bindItems(appItem: AppList, context: Context) {
            AppName.text = appItem.name
            packageName.text = appItem.packages
            packageIcon.setImageDrawable(appItem.icon)
            mainLayout.setOnClickListener {
                println("before change : ${show}")
                for (i in 0 until show.size) {
                    println("in loop : ${show.keyAt(i)}")
                    if (show.get(show.keyAt(i))) {
                        show.put(show.keyAt(i), false)
                        println("in loop in codition at index: $i , ${show.keyAt(i)}")
                    }
                }
                if (!show.get(adapterPosition)){
                    btn_layout.visibility = View.VISIBLE
                    show.put(adapterPosition, true)
                }else{
                    btn_layout.visibility = View.GONE
                    show.put(adapterPosition, false)
                }
                notifyDataSetChanged()
                println("after change : ${show}")
            }
            openAppBtn.setOnClickListener {
                val intent = context.packageManager.getLaunchIntentForPackage(appItem.packages)
                if(intent != null){
                    context.startActivity(intent)
                }
                show.put(adapterPosition, false)
                notifyDataSetChanged()
            }
            reportBugBtn.setOnClickListener {
                openReport!!.bugReportWindow(true)
                context.startActivity(Intent(context,BugReportingWindow::class.java).putExtra("appName",appItem.name))
                show.put(adapterPosition, false)
                notifyDataSetChanged()
            }
        }
    }


}