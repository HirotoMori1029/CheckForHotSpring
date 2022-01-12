package com.hirogram.android.checkforhotspring

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val APP_ID = "22656e56813a23e149407c8c15c8e731"
    }

    private lateinit var cAdapter: CustomAdapter
    private var blgList = mutableListOf<Belonging>()
    private var falseList = mutableListOf<Belonging>()
    private var fNameList = arrayListOf<String>()
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
        blgList = generateItemList()
        cAdapter = CustomAdapter(blgList)

        //アダプターに持たせたリスナを定義
        cAdapter.listener = object : CustomAdapter.Listener{
            override fun onCheckClick(position: Int) {
                blgList[position].check = !(blgList[position].check)
                //adapterの更新
                cAdapter.bList = blgList
                cAdapter.notifyDataSetChanged()
                //Paperの更新
                Paper.book().
                write(blgList[position].name, Belonging(blgList[position].name, blgList[position].check))
                //結果をログに出力
                Log.d("onCheckClick", "${blgList[position].name} is changed to {${blgList[position].check}}")
            }
        }

        //touchでの処理するための記述
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = cAdapter

        //StartButtonを押下されたときの処理(暫定)
        val btnStart = findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener {
            falseList.clear()
            blgList.forEach {
                if (!it.check) {
                    falseList.add(it)
                }
            }
            //全てがチェックされていれば・・・
            if (falseList.isEmpty()) {
                //todo コンプリートのアクションを予定
                Toast.makeText(this, "All complete!!", Toast.LENGTH_LONG).show()
                blgList.forEach {
                    it.check = false
                }
                cAdapter.bList = blgList
                cAdapter.notifyDataSetChanged()
                Paper.book().allKeys.forEach {
                    Paper.book().write(it, Belonging(it, false))
                }
                //場所を登録していれば
                val rPlace = getSharedPreferences(
                    getString(R.string.preferences_file_key),Context.MODE_PRIVATE
                ).getString(
                    getString(R.string.place_key), null
                    )
                rPlace?.let {
                    val urlFull = "$WEATHERINFO_URL&q=$it&APPID=$APP_ID"
                    receiveWeatherInfo(urlFull)
                }

                //未チェックがあれば・・・
            } else {
                fNameList.clear()
                falseList.forEach {
                    fNameList.add(it.name)
                }
                val args = Bundle()
                args.putStringArrayList("fNameList", fNameList)
                val dialogFragment = RemainItemDialogFragment()
                dialogFragment.arguments = args
                dialogFragment.show(supportFragmentManager, "RemainItemDialogFragment")
                Toast.makeText(this, "$fNameList", Toast.LENGTH_LONG).show()
            }
        }

        //＋ボタンを押下されたときの処理
        val addButton = findViewById<ImageButton>(R.id.btn_plus)
        addButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        blgList = generateItemList()
        cAdapter.bList = blgList
        cAdapter.notifyDataSetChanged()

    }

    private fun generateItemList() :MutableList<Belonging> {
        val itemList = mutableListOf<Belonging>()
        Paper.book().allKeys.forEach {
            itemList.add(Paper.book().read(it))
        }
        return itemList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true

        when(item.itemId) {
            //全てのアイテムを削除する処理
            R.id.allDelete -> {
                blgList.clear()
                cAdapter.bList = blgList
                Paper.book().destroy()
                cAdapter.notifyDataSetChanged()
            }
            //アドレスを登録する処理
            R.id.registerPlace -> {
                val rIntent = Intent(this@MainActivity, RegisterPlaceActivity::class.java)
                startActivity(rIntent)
            }
            else ->
                returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
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
                adapter.notifyItemRemoved(position)
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
                    R.drawable.ic_baseline_delete_32
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

    @UiThread
    private fun receiveWeatherInfo(urlFull: String) {
        lifecycleScope.launch { 
            val result = weatherInfoBackGroundRunner(urlFull)
            weatherInfoPostRunner(result)
        }
    }

    @WorkerThread
    private suspend fun weatherInfoBackGroundRunner(url: String) :String {
        return withContext(Dispatchers.IO) {
            var result = ""
            val url = URL(url)
            val con = url.openConnection() as? HttpURLConnection
            con?.let {
                try {
                    it.connectTimeout = 1000
                    it.readTimeout = 1000
                    it.requestMethod = "GET"
                    it.connect()
                    val stream = it.inputStream
                    result = is2String(stream)
                    stream.close()
                }
                catch (ex: SocketTimeoutException) {
                    Log.w("CheckForHotSpring", "SocketTimeout")
                }
                it.disconnect()
            }
            result
        }
    }

    private fun is2String(stream: InputStream) :String{
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream, "UTF8"))
        //todo ここの処理の意味を調べる
        var line = reader.readLine()
        while (line != null) {
            sb.append(line)
            line = readLine()
        }
        reader.close()
        return sb.toString()
    }
    
    @UiThread
    private fun weatherInfoPostRunner(result: String) {
        val infoList = arrayListOf<String>()
        val rootJSON = JSONObject(result)
        var str = rootJSON.getString("name")
        infoList.add(str)
        str = rootJSON.getJSONArray("weather")
            .getJSONObject(0).getString("description")
        infoList.add(str)
        Toast.makeText(this, "$infoList", Toast.LENGTH_LONG).show()
    }
}