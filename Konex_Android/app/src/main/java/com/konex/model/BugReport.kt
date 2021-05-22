package com.konex.model

import android.net.Uri

data class BugReport(
    val bugTitle:String,
    val bugDes:String,
    val deviceName:String,
    val screenSize:String,
    val appName:String,
    val status:String)