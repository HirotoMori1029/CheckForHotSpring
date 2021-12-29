package com.hirogram.android.checkforhotspring

import android.util.Log
import io.paperdb.Paper

class ListGenerator {
    fun generateItemList() :MutableList<Belonging> {
        val itemList = mutableListOf<Belonging>()
        Paper.book().allKeys.forEach {
            itemList.add(Paper.book().read(it))
            Log.d("ListGenerator", "ItemList was generated")
        }
        return itemList
    }

    fun generateNgList(blgList: MutableList<Belonging>) :ArrayList<String>{
        val ngNameList = arrayListOf<String>()
        blgList.forEach {
            if (!it.check) {
                ngNameList.add(it.name)
            }
        }
        Log.d("ListGenerator", "ngList was generated")
        return ngNameList
    }
}