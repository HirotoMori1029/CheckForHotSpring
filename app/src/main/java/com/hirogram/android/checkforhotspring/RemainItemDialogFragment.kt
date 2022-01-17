package com.hirogram.android.checkforhotspring

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment

class RemainItemDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val argsName = arguments?.getString("ARGS_NAME")
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(activity)
        //argsNameで分岐
        when (argsName) {
            //残りのアイテムを表示する処理
            "remainItemDisplay" -> {
                val fNameList = arguments?.getStringArrayList("fNameList") ?: arrayListOf()
                dialog = activity?.let {
                    val adapter = ArrayAdapter(it, android.R.layout.simple_list_item_1, fNameList)
                    builder.setTitle(R.string.rem_dialog_title)
                    builder.setAdapter(adapter, null)
                    builder.setPositiveButton(R.string.rem_dialog_btn_ok, null)
                    builder.create()
                }
            }
            //天気情報を表示する処理
            "weatherMessageDisplay" -> {
                var msg = ""
                arguments?.let {
                    val cityName = it.getString("name", null)
                    val main = it.getString("main", null)
                    val desc = it.getString("description", null)
                    msg += "In $cityName, It is $desc today"
                    if (main == "Rain") {
                        msg += getString(R.string.msg_rain)
                    } else if (main == "Snow") {
                        msg += getString(R.string.msg_snow)
                }

                }
                dialog = activity?.let {
                    builder.setTitle(R.string.msg_dialog_title)
                    builder.setMessage(msg)
                    builder.setPositiveButton(R.string.msg_dialog_btn_ok, null)
                    builder.create()
                }
            }
        }
        return dialog ?: throw IllegalStateException("Activity is null")
    }
}