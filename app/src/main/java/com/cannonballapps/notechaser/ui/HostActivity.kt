package com.cannonballapps.notechaser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityHostBinding>(this, R.layout.activity_host)
    }

}
