package com.example.admobkotlindocument

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.admobkotlindocument.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var rewardedInterstitialAd: RewardedAd? = null


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
        loadRewardedAd()// Tải quảng cáo khi ứng dụng khởi động
        binding.btnRewardedAds.setOnClickListener {
            if (isInternetAvailable()) {
                showRewardedAd()
            } else {
                Toast.makeText(this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show()
            }
        }
//        loadRewardedInterstitialAd() // Tải quảng cáo khi ứng dụng khởi động
//
//        binding.btnRewardedAds.setOnClickListener {
//            showRewardedInterstitialAd()
//        }

        if (isInternetAvailable()) {
            loadInterstitialAd()
        } else {
            Toast.makeText(this, "Không có kết nối Internet!", Toast.LENGTH_SHORT).show()
        }
    }

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
                Toast.makeText(this@MainActivity, "Không thể tải quảng cáo banner: ${adError.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded() {
                Toast.makeText(this@MainActivity, "Quảng cáo banner đã tải thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }
// Chi load quang cao 1 lan
//    private fun loadInterstitialAd() {
//        val adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdLoaded(ad: InterstitialAd) {
//                interstitialAd = ad
//                Log.d("AdMob", "Quảng cáo xen kẽ đã tải thành công!")
//            }
//
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                interstitialAd = null
//                Toast.makeText(this@MainActivity, "Không thể tải quảng cáo xen kẽ: ${adError.message}", Toast.LENGTH_LONG).show()
//                Log.d("AdMob", "onAdFailedToLoad: ${adError.message}")
//            }
//        })
//    }

    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                Log.d("AdMob", "Quảng cáo xen kẽ đã tải thành công!")

                // Khi quảng cáo đóng, tải lại quảng cáo mới
                interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("AdMob", "Quảng cáo xen kẽ đã đóng, tải lại quảng cáo mới.")
                        interstitialAd = null
                        loadInterstitialAd() // Tải lại quảng cáo ngay khi nó đóng
                        val intent = Intent(this@MainActivity, MainActivity2::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
                Toast.makeText(this@MainActivity, "Không thể tải quảng cáo xen kẽ: ${adError.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    // Hiển thị quảng cáo xen kẽ
    private fun showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd?.show(this)
        } else {
            Toast.makeText(this, "Quảng cáo xen kẽ chưa sẵn sàng!", Toast.LENGTH_SHORT).show()
            loadInterstitialAd() // Tải lại nếu chưa có quảng cáo
        }
    }

    // Sử dụng AdLoader để tải quảng cáo gốc
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
                    Log.e("AdMob", "Không thể tải quảng cáo gốc: ${error.message}")
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        binding.nativeAdContainer.setBackgroundResource(R.drawable.border_ad_container);
        // Gán tiêu đề quảng cáo (BẮT BUỘC)
        adView.findViewById<TextView>(R.id.ad_headline)?.apply {
            text = nativeAd.headline
            adView.headlineView = this
        }

        // Gán nội dung mô tả (có thể null)
        adView.findViewById<TextView>(R.id.ad_body)?.apply {
            text = nativeAd.body ?: ""
            visibility = if (nativeAd.body != null) View.VISIBLE else View.GONE
            adView.bodyView = this
        }

        // Gán tên nhà quảng cáo (có thể null)
        adView.findViewById<TextView>(R.id.ad_advertiser)?.apply {
            text = nativeAd.advertiser ?: ""
            visibility = if (nativeAd.advertiser != null) View.VISIBLE else View.GONE
            adView.advertiserView = this
        }

        // Gán nút CTA (Call to Action) (có thể null)
        adView.findViewById<Button>(R.id.ad_call_to_action)?.apply {
            text = nativeAd.callToAction ?: ""
            visibility = if (nativeAd.callToAction != null) View.VISIBLE else View.GONE
            adView.callToActionView = this
        }

        // Gán icon nhà quảng cáo (có thể null)
        nativeAd.icon?.let { icon ->
            adView.findViewById<ImageView>(R.id.ad_icon)?.apply {
                setImageDrawable(icon.drawable)
                visibility = View.VISIBLE
            }
        } ?: run {
            adView.findViewById<ImageView>(R.id.ad_icon)?.visibility = View.GONE
        }
        adView.iconView = adView.findViewById(R.id.ad_icon)

        // Gán MediaView (video hoặc hình ảnh)
        adView.findViewById<MediaView>(R.id.ad_media)?.let { mediaView ->
            adView.mediaView = mediaView
        }

        // Gán AdChoices Overlay (nếu có)
        adView.findViewById<FrameLayout>(R.id.ad_choices_container)?.let { adChoicesContainer ->
            val adChoicesView = AdChoicesView(adView.context)
            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adChoicesView)
        }

        // Gán badge "Ad" (cố định)
        adView.findViewById<TextView>(R.id.ad_badge)?.apply {
            visibility = View.VISIBLE
        }

        // Đánh dấu đây là một Native Ad
        adView.setNativeAd(nativeAd)
    }


    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this@MainActivity,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    rewardedAd = null
                    Toast.makeText(this@MainActivity, "Rewarded ad load failed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    this@MainActivity.rewardedAd = rewardedAd
                    binding.btnRewardedAds.setEnabled(true)
                    Toast.makeText(
                        this@MainActivity,
                        "Rewarded ad loaded successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd!!.show(
                this@MainActivity
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Ad shown successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun loadRewardedInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5354046379", // ID test của Google
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedInterstitialAd = null
                    Log.e("AdMob", "Không thể tải quảng cáo có thưởng: ${adError.message}")
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedInterstitialAd = ad
                    Log.d("AdMob", "Quảng cáo có thưởng đã tải thành công!")
                }
            }
        )
    }
    private fun showRewardedInterstitialAd() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedInterstitialAd = null
                    loadRewardedInterstitialAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedInterstitialAd = null
                    loadRewardedInterstitialAd()
                }
            }
            rewardedInterstitialAd?.show(this) { rewardItem ->
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Toast.makeText(this, "Bạn nhận được $rewardAmount $rewardType!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Quảng cáo có thưởng chưa sẵn sàng!", Toast.LENGTH_SHORT).show()
            loadRewardedInterstitialAd()
        }
    }
    // Khi quay lại ứng dụng, hiển thị quảng cáo mở
//    override fun onRestart() {
//        super.onRestart()
//        (application as MyApplication).showAppOpenAd(this)
//    }

}
