package com.hirogram.android.checkforhotspring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Paper.init(this)


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

        val addButton = findViewById<ImageButton>(R.id.btn_plus)
        addButton.setOnClickListener {
            goFragment(AddItemFragment())
        }
    }

    private fun goFragment(fragment: Fragment) {
        val frgManager = supportFragmentManager
        val frgTransaction = frgManager.beginTransaction()
        frgTransaction.replace(R.id.main_layout, fragment)
        frgTransaction.commit()
    }


}