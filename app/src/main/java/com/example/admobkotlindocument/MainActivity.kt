package com.example.admobkotlindocument

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.admobkotlindocument.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAppOpen.setOnClickListener {
            (application as MyApplication).showAppOpenAd(this)
        }

        binding.btnBanner.setOnClickListener {
            if (isInternetAvailable()) {
                setUpBanner()
            } else {
                Toast.makeText(this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnInterstitial.setOnClickListener {
            if (isInternetAvailable()) {
                showInterstitialAd()
            } else {
                Toast.makeText(this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNative.setOnClickListener {
            if (isInternetAvailable()) {
                loadNativeAd()
            } else {
                Toast.makeText(this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show()
            }
        }

        // Kiểm tra Internet và tải quảng cáo khi mở app
        if (isInternetAvailable()) {
            loadInterstitialAd()
        } else {
            Toast.makeText(this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show()
        }
    }

    // 🛠 Kiểm tra kết nối Internet
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun setUpBanner() {
        val adRequest = AdRequest.Builder().build()
        binding.bannerAdView.loadAd(adRequest)
        binding.bannerAdView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Toast.makeText(this@MainActivity, "Banner Ad Failed: ${adError.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded() {
                Toast.makeText(this@MainActivity, "Banner Ad Loaded", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Phuong thuc load interstitial ad
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                Log.d("AdMob", "Interstitial Ad Loaded")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
                Toast.makeText(this@MainActivity, "Interstitial Ad lỗi: ${adError.message}", Toast.LENGTH_LONG).show()
                Log.d("AdMob", "onAdFailedToLoad: ${adError.message}")
            }
        })
    }

    private fun showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd?.show(this)
        } else {
            Toast.makeText(this, "Quảng cáo chưa sẵn sàng!", Toast.LENGTH_SHORT).show()
            loadInterstitialAd()
        }
    }
    // Phuong thuc load native ad
    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                val adView = layoutInflater.inflate(R.layout.native_ad_layout, null) as NativeAdView
                populateNativeAdView(nativeAd, adView)
                binding.nativeAdContainer.removeAllViews()
                binding.nativeAdContainer.addView(adView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdMob", "Native Ad Failed: ${error.message}")
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.findViewById<TextView>(R.id.ad_headline).text = nativeAd.headline
        adView.findViewById<TextView>(R.id.ad_body).text = nativeAd.body ?: ""
        adView.findViewById<Button>(R.id.ad_call_to_action).apply {
            text = nativeAd.callToAction
            visibility = if (nativeAd.callToAction != null) View.VISIBLE else View.GONE
        }
        adView.setNativeAd(nativeAd)
    }
}
