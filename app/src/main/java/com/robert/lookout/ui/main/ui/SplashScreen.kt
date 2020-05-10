package com.robert.planYourDay.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.robert.lookout.R
import com.robert.lookout.ui.main.viewModel.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            val intent=Intent(this@SplashScreen,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
