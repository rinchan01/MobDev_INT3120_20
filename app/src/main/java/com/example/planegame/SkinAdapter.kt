package com.example.planegame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class SkinAdapter (context: Context, private val skinList: List<Skin>) : ArrayAdapter<Skin>(context, 0, skinList){
    private class ViewHolder {
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
        holder.textView.text = currentItem.price
        holder.coinImg.setImageResource(R.drawable.coin)
        return itemView!!
    }
}