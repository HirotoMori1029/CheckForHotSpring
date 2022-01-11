package com.hirogram.android.checkforhotspring

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_place)

        val sp = getPreferences(Context.MODE_PRIVATE)
        val key = getString(R.string.place_key)
        val text = sp.getString(key, null) ?: "Nothing"
        val tvPlace = findViewById<TextView>(R.id.tvPlace)
        tvPlace.text = getString(R.string.current_place_desc, text)

        val plist: MutableList<String> = mutableListOf("Sasebo", "Fukuoka", "Kitakyushu")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, plist)
        val lvPlace = findViewById<ListView>(R.id.lvPlace)

        //タップされたときの処理
        lvPlace.setOnItemClickListener {
            parent, _, position, _ ->
            val place = parent.getItemAtPosition(position) as String
            tvPlace.text = getString(R.string.current_place_desc, place)
            sp.edit().putString(key, place).apply()
        }
        lvPlace.adapter = adapter

    }
}