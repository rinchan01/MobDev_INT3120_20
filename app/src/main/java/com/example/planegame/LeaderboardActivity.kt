package com.example.planegame

import com.example.planegame.LeaderboardFragment.LeaderBoardPlayer
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.planegame.database.PlayerDatabase
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class LeaderboardActivity : AppCompatActivity() {
    private val playerDao by lazy {
        PlayerDatabase.getDatabase(this).playerDao
    }
    private lateinit var viewPager: ViewPager2
    private lateinit var myPagerAdapter: MyPagerAdapter
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        viewPager = findViewById(R.id.vpLeaderboard)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        myPagerAdapter = MyPagerAdapter(supportFragmentManager, lifecycle)

        lifecycleScope.launch(Dispatchers.IO) {
            playerDao.getLeaderboard().collectLatest { players ->
                val leaderboardPlayers: List<LeaderBoardPlayer> = players.map {
                    val file = File(filesDir, it.avatarPath)
                    val bytes = file.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    LeaderBoardPlayer(it.name, it.highScore, bmp)
                }
                val localPlayers = listOf<LeaderBoardPlayer>()
                withContext(Dispatchers.Main) {
                    myPagerAdapter.addFragment(LeaderboardFragment(leaderboardPlayers))
                    myPagerAdapter.addFragment(LeaderboardFragment(localPlayers))

                    viewPager.adapter = myPagerAdapter

                    tabLayout = findViewById(R.id.tlLeaderboard)
                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                        when (position) {
                            0 -> tab.text = "Global"
                            1 -> tab.text = "Local"
                        }
                    }.attach()
                }
            }
        }


        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.IO) {
            playerDao.getLeaderboard().collectLatest { players ->
                var defaultLocationString = "0.0, 0.0"
                for(player in players) {
                    if(player.name == getSharedPreferences("my_prefs", MODE_PRIVATE).getString("username", "")) {
                        defaultLocationString = player.location
                        break
                    }
                }
                val localPlayers: List<LeaderBoardPlayer> = players
                    .filter { distanceBetweenLocationStrings(it.location, defaultLocationString) < 10000}
                    .map {
                    val file = File(filesDir, it.avatarPath)
                    val bytes = file.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    LeaderBoardPlayer(it.name, it.highScore, bmp)
                }
                withContext(Dispatchers.Main) {
                    myPagerAdapter.updateFragment(LeaderboardFragment(localPlayers), 1)
                    viewPager.adapter?.notifyItemChanged(1)
                }
            }
        }
    }

    private class MyPagerAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {

        val fragmentList = arrayListOf<Fragment>()

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        fun updateFragment(fragment: Fragment, position: Int) {
            fragmentList[position] = fragment
        }
        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }
    }

    private fun distanceBetweenLocationStrings(string1: String, string2: String): Float {
        val parts = string1.split(",")
        val lat1 = parts[0].trim().toDouble()
        val lon1 = parts[1].trim().toDouble()

        val parts2 = string2.split(",")
        val lat2 = parts2[0].trim().toDouble()
        val lon2 = parts2[1].trim().toDouble()

        var result = FloatArray(3)

        Location.distanceBetween(lat1, lon2, lat2, lon2, result)

        return result[0]
    }
}

