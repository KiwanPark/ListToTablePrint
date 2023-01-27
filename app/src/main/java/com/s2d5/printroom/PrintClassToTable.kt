package com.s2d5.printroom

import android.util.Log
import java.lang.Integer.max
import kotlin.reflect.KCallable

class PrintClassToTable() {
    private val propertyMap: LinkedHashMap<String, Int> = linkedMapOf()
    fun printProperty(members: Collection<KCallable<*>>, arrayList: ArrayList<Card>) {
        for (m in members) {
            if (!m.toString().contains("(")) {
                //Log.e(TAG, m.name)
                propertyMap[m.name] = m.name.length
            }
        }

        for (c in arrayList) {
            Log.e("card", c.toString())
            for (entry in propertyMap.entries) {
                val value = c.toString().split(entry.key)[1].replace("=", "").split(",")[0].replace(")", "")
                propertyMap[entry.key] = max(propertyMap[entry.key] ?: 3, value.length+3)
            }
        }

        var headerTopBoxResult = "┌"
        var headerValuesResult = "│"
        var headerMidBoxResult = "├"
        var footerBotBoxResult = "└"


        for (entry in propertyMap.entries) {
            headerValuesResult += padFormat(entry.key, entry.value) +"│"
            for(i in 0 until entry.value) {
                headerTopBoxResult += "─"
                headerMidBoxResult += "─"
                footerBotBoxResult += "─"
            }
            headerTopBoxResult += "┬"
            headerMidBoxResult += "┼"
            footerBotBoxResult += "┴"
            Log.e(entry.key, entry.value.toString())
        }

        var valuesResult = ""
        for(c in arrayList) {
            for (entry in propertyMap.entries) {
                val value = c.toString().split(entry.key)[1].replace("=", "").split(",")[0].replace(")", "")
                valuesResult += "│" + padFormat(value, entry.value)
            }
            valuesResult += "|"+System.lineSeparator()
        }

        headerTopBoxResult = headerTopBoxResult.substring(0, headerTopBoxResult.length-1)
        headerTopBoxResult += "┐"
        headerMidBoxResult = headerMidBoxResult.substring(0, headerMidBoxResult.length-1)
        headerMidBoxResult += "│"
        footerBotBoxResult = footerBotBoxResult.substring(0, footerBotBoxResult.length-1)
        footerBotBoxResult += "┘"
        valuesResult = valuesResult.substring(0, valuesResult.length-1)

        Log.e("Table", """
            
            #$headerTopBoxResult
            #$headerValuesResult
            #$headerMidBoxResult
            #$valuesResult
            #$footerBotBoxResult
        """.trimMargin("#"))
    }

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