package com.eric.zvonilka.activities

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.eric.zvonilka.R
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        supportActionBar?.hide()
        val animDrawable = root_launch_layout.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(10)
        animDrawable.setExitFadeDuration(500)
        animDrawable.start()
        Handler().postDelayed({
            animDrawable.stop()
            launch()
            super.finish()
        },2500)
    }
    fun launch(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}