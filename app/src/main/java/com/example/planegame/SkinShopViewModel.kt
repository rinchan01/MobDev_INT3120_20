package com.example.planegame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.planegame.ShopActivity.Skin

class SkinShopViewModel: ViewModel() {
    private val _skinList = MutableLiveData<MutableList<Skin>>()
    val skinList: LiveData<MutableList<Skin>> = _skinList

    private val _coins = MutableLiveData<Int>()
    val coins: LiveData<Int> = _coins

    init {
        val coinImgId = R.drawable.coin
        _skinList.value = mutableListOf(
            Skin(R.drawable.ship1, "20", coinImgId),
            Skin(R.drawable.ship2, "40", coinImgId),
            Skin(R.drawable.ship3, "60", coinImgId),
            Skin(R.drawable.ship4, "80", coinImgId),
            Skin(R.drawable.ship5, "100", coinImgId),
            Skin(R.drawable.ship6, "120", coinImgId)
        )
    }

    fun setCoins(coins: Int) {
        _coins.value = coins
    }

    fun updateSkin(idx: Int) {
        val skin = _skinList.value!![idx]
        val updatedSkins = _skinList.value!!
        updatedSkins[idx] = skin.copy(purchased = true)
        _skinList.value = updatedSkins
    }

    fun updateCoins(price: Int) {
        _coins.value = _coins.value?.minus(price)
    }

    fun getSkinList(): MutableList<Skin> {
        return _skinList.value ?: mutableListOf()
    }

    fun getCoins(): Int {
        return _coins.value ?: 0
    }
}