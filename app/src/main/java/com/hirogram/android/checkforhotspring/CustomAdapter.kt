package com.hirogram.android.checkforhotspring

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(var bList: MutableList<Belonging>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    var listener: Listener? = null

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val tvID: TextView = view.findViewById(R.id.tvID)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

    }

    override fun onCreateViewHolder (viewGroup: ViewGroup, viewType:Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val belonging = bList[position]
        holder.tvID.apply {
            text = this.context.getString(R.string.item_number, position + 1)
        }
        holder.tvName.text = belonging.name
        holder.checkBox.isChecked = belonging.check
        holder.checkBox.setOnClickListener {
            listener?.onCheckClick(position)
        }

        Log.d("CustomAdapter", "onBindViewHolder has been called")
    }

    override fun getItemCount() = bList.size

    interface Listener {
        fun onCheckClick(position: Int)
    }

}