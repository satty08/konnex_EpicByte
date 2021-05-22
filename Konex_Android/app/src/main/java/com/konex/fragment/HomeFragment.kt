package com.konex.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.konex.R
import com.konex.activity.AdminActivity
import com.konex.activity.MainActivity
import com.konex.extra.AppsAdapter
import com.konex.extra.HideSearchBar
import com.konex.extra.SearchBarInterface
import com.konex.model.AppList
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(),HideSearchBar {
    private var installedApps: MutableList<AppList>? = null
    private var installedAppAdapter: AppsAdapter? = null
    internal var searchBarInterface: SearchBarInterface?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val homeView =  inflater.inflate(R.layout.fragment_home, container, false)
        (context as MainActivity).searchBar(this)
       // (activity as MainActivity?)!!.delegate.setSupportActionBar(home_toolbar)
        searchBarInterface = activity as? SearchBarInterface
        installedApps = ArrayList()
        installedApps = getInstalledApps()
        sortArrayList()
        homeView.rv_home.layoutManager = LinearLayoutManager(
            homeView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        installedAppAdapter = AppsAdapter(homeView.context, installedApps!!)
        homeView.rv_home.adapter = installedAppAdapter
        homeView.search_btn.setOnClickListener {
           openSearchBar()
        }
        homeView.back_arrow.setOnClickListener {
            closeSearchBar()
        }
        homeView.admin_btn.setOnClickListener {
//            val Getintent = Intent(Intent.ACTION_VIEW, Uri.parse("https://konex-d1db0.web.app"))
//            startActivity(Getintent)
            startActivity(Intent(context,AdminActivity::class.java))
            searchBarInterface!!.bugReportWindow(true)
        }

        val mTitleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length>0){
                    installedAppAdapter!!.filter.filter(s)
                }else{
                    homeView.rv_home.adapter = installedAppAdapter
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        homeView.search.addTextChangedListener(mTitleTextWatcher)
        return homeView
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        val searchItem = menu.findItem(R.id.menu_search)
//        val searchView = searchItem.actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                installedAppAdapter!!.filter.filter(newText)
//                return true
//            }
//
//        })
//
//        return super.onCreateOptionsMenu(menu, inflater)
//    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun getInstalledApps(): ArrayList<AppList> {
        val apps = ArrayList<AppList>()

        val packs = activity!!.packageManager.getInstalledPackages(0)
        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (i in packs.indices) {
            val p = packs[i]
            if (!isSystemPackage(p)) {
                val appName = p.applicationInfo.loadLabel(activity!!.packageManager).toString()
                val icon = p.applicationInfo.loadIcon(activity!!.packageManager)
                val packages = p.applicationInfo.packageName
                apps.add(AppList(appName, icon, packages))
            }

        }
        return apps
    }
    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
    private fun sortArrayList() {
        Collections.sort(installedApps,
            object : Comparator<AppList?> {
                override fun compare(o1: AppList?, o2: AppList?): Int {
                    return (o1!!.name).compareTo(o2!!.name);
                }
            })

    }
    fun openSearchBar(){
        back_arrow.visibility = View.VISIBLE
        search.visibility = View.VISIBLE
        search.requestFocus()
        val imm: InputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        imm.toggleSoftInputFromWindow(
            homefrag_layout.applicationWindowToken,
            InputMethodManager.SHOW_FORCED,
            0
        )
        search_btn.visibility = View.GONE
        title_home.visibility = View.GONE
        searchBarInterface!!.isSearchFocusable(true)
    }
    fun closeSearchBar(){
        val view: View? = activity!!.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        back_arrow.visibility = View.GONE
        search.visibility = View.GONE
        search_btn.visibility = View.VISIBLE
        title_home.visibility = View.VISIBLE
        search.setText("")
        searchBarInterface!!.isSearchFocusable(false)
    }

    override fun hideSearchBar() {
        closeSearchBar()
    }
}