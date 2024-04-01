package com.example.planegame

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.planegame.database.PlayerDatabase
import com.example.planegame.databinding.ActivityShopBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopActivity : ComponentActivity() {
    private lateinit var binding: ActivityShopBinding
    private val viewModel: SkinShopViewModel by viewModels()
    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }
    private lateinit var purchaseDialog: MaterialAlertDialogBuilder
    private lateinit var adapter: SkinAdapter
    private var idx = 6
    private val PREF_NAME = "SettingsPrefs"
    private val PICK_SKIN_KEY = "pickSkin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = SkinAdapter(this, viewModel.skinsLiveData.value!!)
        binding.skinList.adapter = adapter
        val playerName = PreferenceHelper(this@ShopActivity).getUsername()

        lifecycleScope.launch(Dispatchers.Main) {
            val player = playerDao.getPlayer(playerName)
            viewModel.setCoins(player!!.coins)
            player.skins.split(" ").forEach {
                viewModel.updateSkin(it.toInt() - 1)
            }
        }

        viewModel.coinsLiveData.observe(this) { coins ->
            binding.currentcoins.text = coins.toString()
        }

        viewModel.skinsLiveData.observe(this) { skins ->
            adapter.notifyDataSetChanged()
        }

        purchaseDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Purchase Skin")
            .setMessage("Do you want to purchase this skin?")
            .setPositiveButton("Yes") { _, _ ->
                if(viewModel.coinsLiveData.value!! >= viewModel.skinsLiveData.value!![idx].price.toInt()) {
                    viewModel.updateCoins(viewModel.skinsLiveData.value!![idx].price.toInt())
                    lifecycleScope.launch(Dispatchers.IO) {
                        val player = playerDao.getPlayer(playerName)
                        val newPlayer = player!!.copy(coins = viewModel.coinsLiveData.value!!, skins = player.skins + " ${idx + 1}")
                        playerDao.upsertPlayer(newPlayer)
                    }
                    adapter.updateData(idx)
                } else {
                    Toast.makeText(applicationContext, "Not enough coins", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No") { _, _ -> }

        val currentSkinIdx = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(PICK_SKIN_KEY, 5)
        binding.currentSkin.setImageResource(viewModel.skinsLiveData.value!![currentSkinIdx].skinImg)

        binding.skinList.setOnItemClickListener { _, _, position, _ ->
            idx = position
            if(viewModel.skinsLiveData.value!![idx].purchased) {
                binding.currentSkin.setImageResource(viewModel.skinsLiveData.value!![idx].skinImg)
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putInt(PICK_SKIN_KEY, idx).apply()
                skinIdx = idx
            }
            else purchaseDialog.show()
        }

    }

    private class SkinAdapter (context: Context, private var skinList: MutableList<Skin>) : ArrayAdapter<Skin>(context, 0, skinList){
        class ViewHolder {
            lateinit var imageView: ImageView
            lateinit var textView: TextView
            lateinit var coinImg: ImageView
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            var itemView = convertView
            val holder: ViewHolder

            if(convertView == null) {
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.skin_block, parent, false)

                holder = ViewHolder()
                holder.imageView = itemView.findViewById(R.id.imageView)
                holder.textView = itemView.findViewById(R.id.price)
                holder.coinImg = itemView.findViewById(R.id.coin)

                itemView.tag = holder

            } else {
                holder = itemView?.tag as ViewHolder
            }

            val currentItem = skinList[position]
            holder.imageView.setImageResource(currentItem.skinImg)
            if(currentItem.purchased) {

                holder.imageView.foreground = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    holder.imageView.foregroundTintBlendMode = null
                }
                holder.textView.text = "Purchased"
                holder.coinImg.setImageResource(0)
            } else {
                holder.textView.text = currentItem.price
                holder.coinImg.setImageResource(currentItem.coinImg)
            }
            return itemView!!
        }

        fun updateData(idx: Int) {
            skinList[idx] = skinList[idx].copy(purchased = true)
            notifyDataSetChanged()
        }

    }

    companion object {
        var skinIdx = 5
    }

    data class Skin(val skinImg: Int, val price: String, val coinImg: Int, val purchased: Boolean = false)
}