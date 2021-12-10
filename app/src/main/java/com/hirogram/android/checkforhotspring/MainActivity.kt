package com.hirogram.android.checkforhotspring

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    private var cAdapter: CustomAdapter? = null
    private var bList = mutableListOf<Belonging>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //RecyclerViewの取得
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
        }
        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        bList = generateItemList()
        cAdapter = CustomAdapter(bList)
        recyclerView.adapter = cAdapter

        //StartButtonを押下されたときの処理(暫定)
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            Toast.makeText(this, "${Paper.book().allKeys}", Toast.LENGTH_LONG).show()
        }

        //＋ボタンを押下されたときの処理
        val addButton = findViewById<ImageButton>(R.id.btn_plus)
        addButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        val btnDelete = findViewById<Button>(R.id.btn_delete)
        btnDelete.setOnClickListener {
            //todo あとでチェックしていないアイテムを削除するように変更する
            Paper.book().destroy()
            cAdapter?.bList?.clear()
            cAdapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()

        cAdapter?.bList = generateItemList()
        cAdapter?.notifyDataSetChanged()
        Log.d("onResume", "onResume has been executed")

    }

    private fun generateItemList() :MutableList<Belonging> {
        val itemList = mutableListOf<Belonging>()
        Paper.book().allKeys.forEach {
            itemList.add(Paper.book().read(it))
        }
        return itemList
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

}