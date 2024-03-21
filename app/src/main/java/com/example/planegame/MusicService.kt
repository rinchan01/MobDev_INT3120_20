package com.example.planegame

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService :Service(){
    lateinit var mediaPlayer: MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}