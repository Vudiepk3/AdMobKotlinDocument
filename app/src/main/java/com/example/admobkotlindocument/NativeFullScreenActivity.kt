package com.example.admobkotlindocument

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admobkotlindocument.databinding.ActivityNativeFullScreenBinding

class NativeFullScreenActivity : AppCompatActivity() {
    val binding by lazy { ActivityNativeFullScreenBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AdmobManager.showAdsNativeFullScreen(
            this,
            AdmobManager.nativeHolderFull,
            binding.bannerContainer)
    }
}