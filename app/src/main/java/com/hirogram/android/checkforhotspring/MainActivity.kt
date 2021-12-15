package com.hirogram.android.checkforhotspring

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    private lateinit var cAdapter: CustomAdapter
    private var bList = mutableListOf<Belonging>()
    private val swipeToDismissTouchHelper by lazy {
        getSwipeToDismissTouchHelper(cAdapter)
    }

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

        //touchでの処理するための記述
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView)
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
            cAdapter.bList.clear()
            cAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()

        cAdapter.bList = generateItemList()
        cAdapter.notifyDataSetChanged()
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

    private fun getSwipeToDismissTouchHelper(adapter: CustomAdapter) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //positionを取得する
                val position = viewHolder.absoluteAdapterPosition
                val key = adapter.bList[position].name
                adapter.bList.removeAt(position)
                adapter.notifyDataSetChanged()
                //Paperの処理
                Paper.book().delete(key)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                //itemの変数を定義
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)
                val deleteIcon = AppCompatResources.getDrawable(
                    this@MainActivity,
                    //todo iconは後で作成する
                    R.drawable.ic_launcher_foreground
                )

                val iconMarginVertical =
                    (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2

                deleteIcon.setBounds(
                    itemView.left + iconMarginVertical,
                    itemView.top + iconMarginVertical,
                    itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMarginVertical
                )
                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.right + dX.toInt(),
                    itemView.bottom
                )

                background.draw(c)
                deleteIcon.draw(c)
            }
        }
        )

}