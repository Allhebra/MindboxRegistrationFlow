package com.example.mindboxregistrationflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindboxregistrationflow.state.PushRegistrationStateHolder
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val pushRegistrationStateHolder: PushRegistrationStateHolder by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pushRegistrationStateHolder.proceed()
    }
}
