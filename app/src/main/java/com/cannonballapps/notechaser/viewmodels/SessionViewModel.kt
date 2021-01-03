package com.cannonballapps.notechaser.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.cannonballapps.notechaser.prefsstore.PrefsStore


class SessionViewModel @ViewModelInject constructor(
        private val prefsStore: PrefsStore
) : ViewModel() {


}