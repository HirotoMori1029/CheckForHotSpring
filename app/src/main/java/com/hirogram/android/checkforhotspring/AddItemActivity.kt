package com.hirogram.android.checkforhotspring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.paperdb.Paper

class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val etText = findViewById<EditText>(R.id.et_text)
        val btnSave = findViewById<Button>(R.id.btn_save_text)

        btnSave.setOnClickListener {
            val etStr = etText.text.toString()
            if (etStr == "") {
                Toast.makeText(this, "input text", Toast.LENGTH_LONG).show()
            } else {
                val blg = Belonging(etStr, false)
                Paper.book().write(etStr, blg)
                Toast.makeText(this, Paper.book().read<Belonging>(etStr).name + "has been saved", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}