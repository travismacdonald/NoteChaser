package com.example.notechaser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.notechaser.R
import com.example.notechaser.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityHostBinding>(this, R.layout.activity_host)
    }

}