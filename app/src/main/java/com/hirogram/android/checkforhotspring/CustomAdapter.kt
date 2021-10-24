package com.hirogram.android.checkforhotspring

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter {

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.tvID)
        val name: TextView = view.findViewById(R.id.tvName)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

    }
}