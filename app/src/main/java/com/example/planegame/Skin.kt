package com.example.planegame

data class Skin(val skinImg: Int, val price: String, val coinImg: Int) {
    constructor(skinImg: Int, price: String) : this(skinImg, price, R.drawable.coin)
}
