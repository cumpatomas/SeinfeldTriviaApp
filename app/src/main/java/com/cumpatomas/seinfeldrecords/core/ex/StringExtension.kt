package com.cumpatomas.seinfeldrecords.core.ex

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


fun CharSequence.addSpaces(): String {
    var output = ""
    try {
        output = this[0].toString()

        for (i in 1..this.lastIndex) {
            if (this[i].toString().matches("[A-Z]".toRegex()))
            {
                output += " ${this[i]}"

            } else output += this[i]
        }
    } catch (e: Exception) {
        output = this.toString()
    }

        return output
    }

fun TextView.typeWrite(lifecycleOwner: LifecycleOwner, text: String, intervalMs: Long) {
    this@typeWrite.text = ""
    lifecycleOwner.lifecycleScope.launch {
        repeat(text.length) {
            delay(intervalMs)
            this@typeWrite.text = text.take(it + 1)
        }
    }
}
