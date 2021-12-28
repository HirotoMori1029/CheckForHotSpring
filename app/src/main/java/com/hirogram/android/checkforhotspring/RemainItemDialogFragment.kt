package com.hirogram.android.checkforhotspring

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class RemainItemDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.dialog_title)
            builder.setMessage(R.string.dialog_msg)
            builder.setPositiveButton(R.string.dialog_btn_ok, DialogButtonClickListener())
            builder.setNegativeButton(R.string.dialog_btn_no, DialogButtonClickListener())
            builder.create()
        }

        return dialog ?: throw IllegalStateException("Activity is null")
    }

    private inner class DialogButtonClickListener : DialogInterface.OnClickListener {
        override fun onClick(p0: DialogInterface?, which: Int) {
            var msg = ""
            when(which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    msg = "Positive"
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    msg = "Negative"
                }
            }
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
        }
    }
}