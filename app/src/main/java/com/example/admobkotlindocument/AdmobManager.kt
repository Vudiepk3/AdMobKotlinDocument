package com.example.admobkotlindocument


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.dino.sample.AdNativeSize
import com.dino.sample.AdmobUtils
import com.dino.sample.utils.Utils
import com.dino.sample.utils.admod.InterHolderAdmob
import com.dino.sample.utils.admod.NativeHolderAdmob

import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

object AdmobManager {
    var nativeHolder =
        NativeHolderAdmob("ca-app-pub-3940256099942544/2247696110")
    var nativeHolderFull =
       NativeHolderAdmob("ca-app-pub-3940256099942544/7342230711")
    var interholder = InterHolderAdmob("ca-app-pub-3940256099942544/1033173712")

    fun loadInter(context: Context, interHolder: InterHolderAdmob) {
        AdmobUtils.loadInterstitial(context, interHolder,
            object :
                AdmobUtils.LoadInterCallback {
                override fun onInterLoaded(interstitialAd: InterstitialAd?, isLoading: Boolean) {
                    interholder.inter = interstitialAd
                    interHolder.check = isLoading
                    Utils.getInstance().showMessenger(context, "onAdLoaded")
                }

                override fun onInterFailed(error: String) {
                    Utils.getInstance().showMessenger(context, "onAdFail")
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {

                }
            }
        )
    }


    fun showInter(
        context: Context,
        interHolder: InterHolderAdmob,
        adListener: AdListener,
        enableLoadingDialog: Boolean
    ) {
        AdmobUtils.showInterstitial(
            context as Activity, interHolder, 10000, object :
                AdmobUtils.InterCallback {
                override fun onInterLoaded() {
                Utils.getInstance().showMessenger(context, "onAdLoaded")
                }

                override fun onStartAction() {
                    adListener.onAdClosed()
                }

                override fun onInterFailed(error: String) {
                    interHolder.inter = null
                    loadInter(context, interHolder)
                    adListener.onFailed()
                    Utils.getInstance().showMessenger(context, "onAdFail")
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {

                }

                override fun onDismissedInter() {
                    interHolder.inter = null
                    loadInter(context, interHolder)
//                    adListener.onAdClosed()
                    Utils.getInstance().showMessenger(context, "onEventClickAdClosed")
                }

                override fun onInterShowed() {
                    Utils.getInstance().showMessenger(context, "onAdShowed")
                }
            }, enableLoadingDialog
        )
    }

    fun loadAdsNativeNew(context: Context, holder: NativeHolderAdmob) {
        AdmobUtils.loadNative(
            context,
            holder,
            object : AdmobUtils.NativeCallback {
                override fun onNativeReady(ad: NativeAd?) {
                }

                override fun onNativeLoaded() {
                }

                override fun onNativeFailed(error: String) {
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {

                }

                override fun onNativeClicked() {
                }
            })
    }

    fun showNative(activity: Activity, viewGroup: ViewGroup, holder: NativeHolderAdmob) {
        if (!AdmobUtils.isNetworkConnected(activity)) {
            viewGroup.visibility = View.GONE
            return
        }
        AdmobUtils.showNative(
            activity,
            holder,
            viewGroup,
            R.layout.ad_unified_medium,
            AdNativeSize.MEDIUM,
            object : AdmobUtils.NativeCallbackSimple {
                override fun onNativeLoaded() {
                   Utils.getInstance().showMessenger(activity, "onNativeShow")
                }

                override fun onNativeFailed(error: String) {
                    Utils.getInstance().showMessenger(activity, "onAdsFailed")
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {

                }
            })
    }

    fun showAdsNativeFullScreen(
        activity: Activity,
        nativeHolder: NativeHolderAdmob,
        viewGroup: ViewGroup
    ) {
        AdmobUtils.showNativeFullScreen(activity, nativeHolder, viewGroup,
            R.layout.ad_native_fullscreen, object :
                AdmobUtils.NativeCallbackSimple {
                override fun onNativeLoaded() {
                    Log.d("==full==", "NativeLoaded: ")
                }

                override fun onNativeFailed(error: String) {
                    Log.d("==full==", "NativeFailed: $error")
                }

                override fun onPaid(adValue: AdValue?, adUnitAds: String?) {

                }

            })
    }


    interface AdListener {
        fun onAdClosed()
        fun onFailed()
    }
}