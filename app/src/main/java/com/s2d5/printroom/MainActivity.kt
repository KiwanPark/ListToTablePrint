package com.s2d5.printroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.s2d5.printroom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val arrayList:ArrayList<Card> = arrayListOf(
            Card("A", "S", 1),
            Card("1", "name", 1),
            Card("suit", "Sas, f23wfd", 1),
            Card("B", "S", 112391299),
            Card("cdw", "score", 1),
        )

        PrintClassToTable.printProperty(Card::class.members, arrayList)
    }
}