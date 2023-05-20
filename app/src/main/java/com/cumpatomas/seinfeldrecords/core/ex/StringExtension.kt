package com.cumpatomas.seinfeldrecords.core.ex


fun CharSequence.addSpaces(): String {
        var output = this[0].toString()

        for (i in 1..this.lastIndex) {
            if (this[i].toString().matches("[A-Z]".toRegex()))
            {
                output += " ${this[i]}"

            } else output += this[i]
        }
        return output
    }
