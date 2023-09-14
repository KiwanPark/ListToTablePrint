package com.s2d5.printroom

import android.util.Log
import java.lang.Exception
import java.lang.Integer.max
import kotlin.reflect.KCallable

private val String.koreanLength: Int
    get() {
        var length = 0
        val koreanRegex = Regex("[ㄱ-힣]")
        for (letter in this) {
            if (koreanRegex.containsMatchIn(letter.toString())) {
                length++
            }
        }
        return length
    }

class PrintClassToTable {


    companion object {
        private val propertyMap: LinkedHashMap<String, Int> = linkedMapOf()

        @JvmStatic
        // 커스텀 데이터와 클래스의 속성을 받아서 시각화된 문자열을 반환하는 함수
        fun <T> printProperty(members: Collection<KCallable<*>>, arrayList: ArrayList<T>): String {
            // 클래스의 속성을 반복하면서 각 속성의 이름과 길이를 저장
            for (m in members) {
                if (!m.toString().contains("(")) {
                    propertyMap[m.name] = m.name.length
                }
            }

            // 커스텀 데이터를 반복하면서 각 속성의 값을 추출하고 문자열 시각화에 필요한 길이 계산
            for (c in arrayList) {
                for (entry in propertyMap.entries) {
                    try {
                        // 리플렉션을 사용하여 속성 값을 추출하고 길이 계산
                        val field = c!!::class.java.getDeclaredField(entry.key)
                        field.isAccessible = true
                        val propertyValue = field.get(c)!!.toString()
                        val koreanRegex = Regex("[ㄱ-힣]")
                        var length = propertyValue.length
                        for (letter in propertyValue) {
                            if (koreanRegex.containsMatchIn(letter.toString())) {
                                length++
                            }
                        }
                        if (length % 2 != 0) {
                            length++
                        }

                        propertyMap[entry.key] = max(propertyMap[entry.key] ?: 3, length + 4)
                    } catch (e: Exception) {
                        // 필드 액세스 중 예외 처리 (필드가 없거나 액세스 권한이 없는 경우)
                        Log.e("error", e.message.toString())
                    }
                }
            }

            // 테이블 헤더 및 상자 문자열 초기화
            var headerTopBoxResult = "┌"
            var headerValuesResult = "│"
            var headerMidBoxResult = "├"
            var footerBotBoxResult = "└"

            // 각 속성의 이름과 길이를 기반으로 테이블 헤더 문자열 생성
            for (entry in propertyMap.entries) {
                headerValuesResult += padFormat(entry.key, entry.value) + "│"
                for (i in 0 until entry.value / 2) {
                    headerTopBoxResult += "─"
                    headerMidBoxResult += "─"
                    footerBotBoxResult += "─"
                }
                headerTopBoxResult += "┬"
                headerMidBoxResult += "┼"
                footerBotBoxResult += "┴"
            }

            // 데이터 값 문자열 초기화
            var valuesResult = ""
            for (c in arrayList) {
                for (entry in propertyMap.entries) {
                    try {
                        // 리플렉션을 사용하여 속성 값을 추출하고 문자열 시각화에 추가
                        val field = c!!::class.java.getDeclaredField(entry.key)
                        field.isAccessible = true
                        val propertyValue = field.get(c)!!.toString()
                        valuesResult += "│" + padFormat(propertyValue, entry.value - propertyValue.koreanLength)
                    } catch (e: Exception) {
                        // 필드 액세스 중 예외 처리 (필드가 없거나 액세스 권한이 없는 경우)
                        Log.e("error", e.message.toString())
                    }
                }
                valuesResult += "|" + System.lineSeparator()
            }

            // 테이블 상자 문자열 마무리
            headerTopBoxResult = headerTopBoxResult.substring(0, headerTopBoxResult.length - 1)
            headerTopBoxResult += "┐"
            headerMidBoxResult = headerMidBoxResult.substring(0, headerMidBoxResult.length - 1)
            headerMidBoxResult += "│"
            footerBotBoxResult = footerBotBoxResult.substring(0, footerBotBoxResult.length - 1)
            footerBotBoxResult += "┘"
            valuesResult = valuesResult.substring(0, valuesResult.length - 1)

            // 생성된 테이블 문자열 출력 및 반환
            Log.e("Table", headerTopBoxResult)
            Log.e("Table", headerValuesResult)
            Log.e("Table", headerMidBoxResult)

            for (valueStr in valuesResult.split(System.lineSeparator())) {
                Log.e("Table", valueStr)
            }
            Log.e("Table", footerBotBoxResult)

            return """
            #$headerTopBoxResult
            #$headerValuesResult
            #$headerMidBoxResult
            #$valuesResult
            #$footerBotBoxResult
        """.trimMargin("#")
        }

        // 문자열을 왼쪽으로 패딩하는 함수
        private fun padLeft(s: String, n: Int): String {
            return "%${n}s".format(s)
        }

        // 문자열을 오른쪽으로 패딩하는 함수
        private fun padRight(s: String, n: Int): String {
            return "%-${n}s".format(s)
        }

        // 문자열을 지정된 길이에 맞춰 가운데로 패딩하는 함수
        private fun padFormat(s: String, n: Int): String {
            var transformString = s

            if (s.length > n) {
                transformString = s.substring(s.length - (n))
            }

            val leftPad = (n + transformString.length) / 2

            return padRight(padLeft(transformString, leftPad), n)
        }
    }
}
