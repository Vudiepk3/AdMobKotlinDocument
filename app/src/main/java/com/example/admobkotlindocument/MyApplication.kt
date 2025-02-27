package com.example.admobkotlindocument

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback

// MyApplication kế thừa từ Application để quản lý quảng cáo mở ứng dụng (App Open Ad)
class MyApplication : Application() {
    // Biến lưu trữ đối tượng quảng cáo mở ứng dụng
    private var appOpenAd: AppOpenAd? = null

    // Biến kiểm soát trạng thái hiển thị quảng cáo (tránh hiển thị nhiều lần)
    private var isAdShowing = false

    override fun onCreate() {
        super.onCreate()

        // Khởi tạo Mobile Ads SDK của Google
        MobileAds.initialize(this)

        // Gọi hàm để tải quảng cáo mở ứng dụng
        loadAppOpenAd()
    }

    /**
     * Phương thức để tải quảng cáo mở ứng dụng
     */
    private fun loadAppOpenAd() {
        // Tạo một yêu cầu quảng cáo (AdRequest)
        val adRequest = AdRequest.Builder().build()

        // Load quảng cáo với ID test của Google
        AppOpenAd.load(
            this, "ca-app-pub-3940256099942544/9257395921", adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    // Khi quảng cáo tải thành công, lưu vào biến appOpenAd
                    appOpenAd = ad
                    Log.d("AdMob", "App Open Ad Loaded") // Ghi log để kiểm tra
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    // Khi tải quảng cáo thất bại, ghi log lỗi
                    Log.e("AdMob", "App Open Ad Failed: ${error.message}")
                }
            })
    }

    /**
     * Phương thức hiển thị quảng cáo mở ứng dụng
     * @param activity: Activity hiện tại dùng để hiển thị quảng cáo
     */
    fun showAppOpenAd(activity: BaseActivity) {
        // Kiểm tra xem quảng cáo đã tải chưa và có đang hiển thị không
        if (appOpenAd != null && !isAdShowing) {
            // Đánh dấu trạng thái đang hiển thị quảng cáo
            isAdShowing = true

            // Hiển thị quảng cáo
            appOpenAd?.show(activity)

            // Gán callback để xử lý sự kiện khi quảng cáo đóng hoặc lỗi
            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Khi người dùng đóng quảng cáo, đặt lại trạng thái và tải quảng cáo mới
                    isAdShowing = false
                    loadAppOpenAd()
                }

                override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                    // Nếu quảng cáo không thể hiển thị, đặt lại trạng thái để thử lại sau
                    isAdShowing = false
                }
            }
        }
    }
}
