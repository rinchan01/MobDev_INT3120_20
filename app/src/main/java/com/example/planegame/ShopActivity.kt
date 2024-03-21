package com.example.planegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView

class ShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val skinShop: GridView = findViewById(R.id.skinList)

        val skin1 = Skin(R.drawable.ship1, "20")
        val skin2 = Skin(R.drawable.ship2, "40")
        val skin3 = Skin(R.drawable.ship3, "60")
        val skin4 = Skin(R.drawable.ship4, "80")
        val skin5 = Skin(R.drawable.ship5, "100")
        val skin6 = Skin(R.drawable.ship6, "120")

        val skinList = listOf(skin1, skin2, skin3, skin4, skin5, skin6)

        val skinAdapter = SkinAdapter(this, skinList)
        skinShop.adapter = skinAdapter

        val currentSkin = findViewById<ImageView>(R.id.currentSkin)
        skinShop.setOnItemClickListener { _, _, pos, _ ->
            currentSkin.setImageResource(skinList[pos].skinImg)
        }

    }
}