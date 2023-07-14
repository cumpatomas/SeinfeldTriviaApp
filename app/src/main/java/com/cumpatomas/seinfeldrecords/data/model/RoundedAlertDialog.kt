package com.cumpatomas.seinfeldrecords.data.model

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.cumpatomas.seinfeldrecords.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RoundedAlertDialog : DialogFragment() {

    //...

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity(), R.style.RoundedCornersDialog)
            .setTitle("Test")
            .setMessage("Message")
            .setPositiveButton("OK", null)
            .create()
    }

}