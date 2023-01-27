package com.s2d5.printroom

import android.util.Log

class Util {

    private fun padLeft(s: String, n: Int): String {
        return "%${n}s".format(s)
    }

    private fun padRight(s: String, n: Int): String {
        return "%-${n}s".format(s)
    }

    fun padFormat(s: String, n: Int): String {
        var transformString = s

        if (s.length > n) {
            transformString = s.substring(s.length - (n))
        }

        val leftPad = (n + transformString.length) / 2
        Log.e("padForamt", "$n | (${leftPad}) $s(${s.length}) (${n})")

        return padRight(padLeft(transformString, leftPad), n)
    }
}