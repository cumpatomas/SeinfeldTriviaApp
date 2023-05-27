package com.cumpatomas.seinfeldrecords.core.ex

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
