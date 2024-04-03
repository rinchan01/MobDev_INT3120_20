package com.example.planegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planegame.ShopActivity.Skin

class SkinShopViewModel: ViewModel() {
    private var skins = MutableLiveData<MutableList<Skin>>()
    val skinsLiveData: MutableLiveData<MutableList<Skin>> get() = skins
    private val coins = MutableLiveData(0)
    val coinsLiveData: MutableLiveData<Int> get() = coins

    init {
        val coinImgId = R.drawable.coin
        skins.value = mutableListOf(
            Skin(R.drawable.ship1, "20", coinImgId),
            Skin(R.drawable.ship2, "40", coinImgId),
            Skin(R.drawable.ship3, "60", coinImgId),
            Skin(R.drawable.ship4, "80", coinImgId),
            Skin(R.drawable.ship5, "100", coinImgId),
            Skin(R.drawable.ship6, "120", coinImgId)
        )
    }

    fun setCoins(coins: Int) {
        this.coins.value = coins
    }

    fun updateCoins(price: Int) {
        coins.value = coins.value?.minus(price)
    }

    fun updateSkin(idx: Int) {
        val skin = skins.value!![idx]
        val updatedSkins = skins.value!!
        updatedSkins[idx] = skin.copy(purchased = true)
        skins.value = updatedSkins
    }
}