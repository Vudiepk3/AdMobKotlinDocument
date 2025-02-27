package com.example.admobkotlindocument

import android.os.Bundle
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.dino.sample.AdmobUtils
import com.dino.sample.AppOpenUtils
import com.dino.sample.ApplovinUtils
import com.dino.sample.callback_applovin.NativeCallback
import com.dino.sample.utils.Utils
import com.example.admobkotlindocument.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdmobUtils.initAdmob(this, 10000, isDebug = true, isEnableAds = true)
        AppOpenUtils.getInstance()
            .init(application, getString(com.dino.ads.R.string.test_ads_admob_app_open_new))
        AppOpenUtils.getInstance()
            .disableAppResumeWithActivity(SplashActivity::class.java)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ApplovinUtils.isNetworkConnected(this)) {
            ApplovinUtils.initApplovin(application,
                "Hd8NW44NTx4ndvT7Pw2PIQR_omwB0DB00BKnHGXorX1hCETptrgiRyRCtDcZqbhU9Wi_l4R0Icd5N5SkKJFGIy",
                isDebug = true,
                isEnableAds = true,
                callback = object : ApplovinUtils.Initialization {
                    override fun onInitSuccessful() {
                        ApplovinUtils.loadNative(this@SplashActivity,
                            ApplovinManager.nativeHolder, object :
                                NativeCallback {
                                override fun onNativeAdLoaded(
                                    nativeAd: MaxAd?,
                                    nativeAdView: MaxNativeAdView?
                                ) {
                                    Toast.makeText(
                                        this@SplashActivity,
                                        "Loaded",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onAdFail(error: String) {
                                    Toast.makeText(
                                        this@SplashActivity,
                                        "Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onAdRevenuePaid(ad: MaxAd) {

                                }
                            })
                        Utils.getInstance()
                            .replaceActivity(this@SplashActivity, MainActivity::class.java)
                    }
                })
        } else {
            Utils.getInstance()
                .replaceActivity(this@SplashActivity, MainActivity::class.java)
        }
    }
}