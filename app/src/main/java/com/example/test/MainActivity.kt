package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    companion object {
        const val AD_UNIT_ID = "ca-app-pub-2579532894113456/9413517755"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        button = findViewById(R.id.ads)

        MobileAds.initialize(this,"ca-app-pub-2579532894113456~9413517755")

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                .build()
        )

        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = Companion.AD_UNIT_ID
            adListener = (object : AdListener() {
                override fun onAdLoaded() {
                    Toast.makeText(this@MainActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(this@MainActivity,
                        "onAdFailedToLoad() with error code: $errorCode",
                        Toast.LENGTH_SHORT).show()
                }
            })
        }
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        ads.setOnClickListener {
            if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        } }
    }


}