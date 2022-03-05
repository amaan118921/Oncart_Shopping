package com.example.oncart.application

import android.app.Application
import com.example.oncart.helper.HelpRepo
import com.example.oncart.room.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var repo: HelpRepo
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        repo.initSharedPreferences()
    }
}