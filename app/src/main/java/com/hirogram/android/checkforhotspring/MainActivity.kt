package com.hirogram.android.checkforhotspring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //データ作成
        val towel = Belonging(1, "towel", false)
        val money = Belonging(2, "money", false)
        val spareChanges = Belonging(3, "spare changes", false)
        val bList = mutableListOf(towel, money, spareChanges)

        //RecyclerViewの取得
        val recyclerView = findViewById<RecyclerView>(R.id.lv)

        //layoutManager
        val layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        recyclerView.layoutManager = layoutManager

        //adapterの設定
        mAdapter = CustomAdapter(bList)
        recyclerView.adapter = mAdapter
    }
}