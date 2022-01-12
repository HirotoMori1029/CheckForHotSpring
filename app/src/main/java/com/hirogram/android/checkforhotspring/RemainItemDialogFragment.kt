package com.hirogram.android.checkforhotspring

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment

class RemainItemDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val fNameList = arguments?.getStringArrayList("fNameList") ?: arrayListOf()

        val dialog = activity?.let {
            val adapter = ArrayAdapter(it, android.R.layout.simple_list_item_1, fNameList)
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.dialog_title)
            builder.setAdapter(adapter, null)
            builder.setPositiveButton(R.string.dialog_btn_ok, null)
            builder.create()
        }

        return dialog ?: throw IllegalStateException("Activity is null")
    }
}