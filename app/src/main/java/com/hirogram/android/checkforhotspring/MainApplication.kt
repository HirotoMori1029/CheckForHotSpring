package com.hirogram.android.checkforhotspring

import android.app.Application
import io.paperdb.Paper

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //Paper初期化
        Paper.init(this)
    }

}