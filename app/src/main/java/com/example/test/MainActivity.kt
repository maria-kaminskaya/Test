package com.example.test

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Long.toHexString
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    lateinit var android_id: String

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

        imprint.setOnClickListener {
            getDevisesImprint(this)
        }
    }


    @SuppressLint("HardwareIds")
    fun getDevisesImprint(context: Context): String {
        //Android Secure ID
        android_id = Secure.getString(contentResolver, Secure.ANDROID_ID);
        Log.d("MainActivity", "getDevisesImprint ${android_id}")

        //Android gsf ID
        lateinit var hexString: String
        val sUri: Uri = Uri.parse("content://com.google.android.gsf.gservices")
        try {
            val query: Cursor = context.contentResolver
                .query(sUri, null, null, arrayOf("android_id"), null)
                ?: return "Not found"
            if (!query.moveToFirst() || query.columnCount < 2) {
                query.close()
                return "Not found"
            }
            hexString = toHexString(query.getString(1).toLong())
            query.close()
            hexString.toUpperCase(Locale.ROOT).trim { it <= ' ' }
        } catch (e: SecurityException) {
            e.printStackTrace()

        } catch (e2: Exception) {
            e2.printStackTrace()

        }

        Log.d("MainActivity", "getGSFID  ${hexString}")

        //Build.Fingerprint
        val build_fingerprint = Build.FINGERPRINT
        Log.d("MainActivity", "Build.Fingerprint  ${build_fingerprint}")

        Toast.makeText(this@MainActivity, "Devises imprint ${android_id} ${hexString} ${build_fingerprint}", Toast.LENGTH_SHORT).show()

        return hexString+android_id+build_fingerprint

        }
    }

