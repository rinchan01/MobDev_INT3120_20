package com.example.planegame

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class LeaderboardFragment(private val playerList: List<LeaderBoardPlayer>) : Fragment() {
    private lateinit var listView: ListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.lvLeaderboard)
        val adapter = LeaderBoardAdapter(requireContext(), playerList)
        listView.adapter = adapter
    }

    private class LeaderBoardAdapter(context: Context, private val players: List<LeaderBoardPlayer>): ArrayAdapter<LeaderBoardPlayer>(context, 0, players) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            val holder: ViewHolder

            if(convertView == null) {
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.leaderboard_item, parent, false)

                holder = ViewHolder()
                holder.ivAvatar = itemView.findViewById(R.id.ivAvatar)
                holder.tvPlayerName = itemView.findViewById(R.id.tvPlayerName)
                holder.tvCoins = itemView.findViewById(R.id.tvCoins)

                itemView.tag = holder
            } else {
                holder = itemView?.tag as ViewHolder
            }

            val currentItem = players[position]
            holder.tvPlayerName.text = currentItem.name
            holder.tvCoins.text = currentItem.coins.toString()
            holder.ivAvatar.setImageBitmap(currentItem.avatar)
            return itemView!!
        }

        class ViewHolder {
            lateinit var ivAvatar: ImageView
            lateinit var tvPlayerName: TextView
            lateinit var tvCoins: TextView
        }
    }

    class LeaderBoardPlayer(val name: String, val coins: Int, val avatar: Bitmap)
}