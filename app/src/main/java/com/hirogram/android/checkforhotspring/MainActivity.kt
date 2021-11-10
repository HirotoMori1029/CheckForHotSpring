package com.hirogram.android.checkforhotspring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //データ作成
        val towel = Belonging(1, "towel", false)
        val money = Belonging(2, "money", false)
        val spareChanges = Belonging(3, "spare changes", false)
        val bList = arrayListOf(towel, money, spareChanges)


        //RecyclerViewの取得
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
        }

        //layoutManager
        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        //adapterの設定
        recyclerView.adapter = CustomAdapter(bList)

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            Toast.makeText(this, "$bList", Toast.LENGTH_LONG).show()
        }
    }


}