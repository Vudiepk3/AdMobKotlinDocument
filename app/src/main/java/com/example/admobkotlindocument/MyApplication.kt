package com.example.admobkotlindocument

import com.dino.sample.AppOpenUtils
import com.dino.sample.adjust.AdjustUtils
import com.dino.sample.application.AdsApplication

class Application : AdsApplication() {
    override fun onCreateApplication() {
        AdjustUtils.initAdjust(this,"",false)
//        AdmobUtils.initAdmob(this, 10000, isDebug = true, isEnableAds = true)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN){
            AppOpenUtils.getInstance().timeToBackground = System.currentTimeMillis()
        }
    }
}