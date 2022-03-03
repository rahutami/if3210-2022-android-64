package com.example.if3210_64

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_check_in.view.*

class DialogCheckIn(success: Boolean, reason: String) : DialogFragment() {

    val success = success
    val reason = reason

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout to use as dialog or embedded fragment
        val view = inflater.inflate(R.layout.dialog_check_in, container, false)
        if (success) {
            view.title_text.text = "Berhasil"
            view.subtitle_text.text = ""
            view.imageView.setImageResource(R.mipmap.green)
        } else {
            view.title_text.text = "Gagal"
            view.subtitle_text.text = reason
            view.imageView.setImageResource(R.mipmap.red)
        }
        return view
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}