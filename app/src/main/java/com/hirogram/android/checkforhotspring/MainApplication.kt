package com.hirogram.android.checkforhotspring

import android.app.Application
import io.paperdb.Paper

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //Paper初期化
        Paper.init(this)
    }

    //PaperSave
    fun saveItem(name: String) {
        val blg = Belonging(name, false)
        Paper.book().write(blg.name, blg)
    }

    //PaperRead
    fun readItem(key: String): Belonging {
        return Paper.book().read(key)
    }
}