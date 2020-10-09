package com.eric.zvonilka.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.eric.zvonilka.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    fun click(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        super.finish()
    }
}