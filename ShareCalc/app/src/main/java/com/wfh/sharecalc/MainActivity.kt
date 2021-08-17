package com.wfh.sharecalc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    private var lastNumeric:Boolean= false
    private var lastDecimal:Boolean= false
    private var toShare:String = ""

    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                Toast.makeText(this@MainActivity, "Ad Loaded",Toast.LENGTH_SHORT).show()
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                super.onAdFailedToLoad(adError)
                mAdView.loadAd(adRequest)
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }
        }

    }

    fun onShare(view: View){
        val shareBody = toShare
        //Log.i("info",toShare)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Calculation")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share Calculation Via :"))
    }

    fun onDigit(view: View){
        txtInput.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View){
        txtInput.text = ""
        lastNumeric = false
        lastDecimal = false
    }

    fun onDecimal(view: View){
        if(lastNumeric && !lastDecimal){
            txtInput.append(".")
            lastNumeric = false
            lastDecimal = true
        }
    }

    private fun removeDotZeroAfter(result:String) : String{
        var value = result
        if(result.contains(".0"))
            value = result.substring(0,result.length-2)

        return value
    }

    fun onEqual(view: View){
        if(lastNumeric){
            toShare = txtInput.text.toString()
            var txtval = txtInput.text.toString()
            var prefix = ""
            try{
                if(txtval.startsWith("-")){
                    prefix = "-"
                    txtval = txtval.substring(1)
                }

                if(txtval.contains("-")){
                    val splitVals = txtval.split("-")
                    var one = splitVals[0]
                    var two = splitVals[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    txtInput.text = removeDotZeroAfter((one.toDouble() - two.toDouble()).toString())
                    toShare = toShare + " = " + txtInput.text
                }else if(txtval.contains("+")){
                    val splitVals = txtval.split("+")
                    var one = splitVals[0]
                    var two = splitVals[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    txtInput.text = removeDotZeroAfter((one.toDouble() + two.toDouble()).toString())
                    toShare = toShare + " = " + txtInput.text
                }else if(txtval.contains("x")){
                    val splitVals = txtval.split("x")
                    var one = splitVals[0]
                    var two = splitVals[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    txtInput.text = removeDotZeroAfter((one.toDouble() * two.toDouble()).toString())
                    toShare = toShare + " = " + txtInput.text
                }else if(txtval.contains("/")){
                    val splitVals = txtval.split("/")
                    var one = splitVals[0]
                    var two = splitVals[1]
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }
                    txtInput.text = removeDotZeroAfter((one.toDouble()/two.toDouble()).toString())
                    toShare = toShare + " = " + txtInput.text
                }


            }catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
    }



    fun onOperator(view: View){
        if (lastNumeric && !isOperatorAdded(txtInput.text.toString())) {
            txtInput.append((view as Button).text)
            lastNumeric = false // Update the flag
            lastDecimal = false    // Reset the DOT flag
        }
    }

    private fun isOperatorAdded(value: String) : Boolean{
        return if (value.startsWith("-")){
            false
        }else{
            value.contains("/") || value.contains("x") || value.contains("+") || value.contains("-")
        }
    }



}