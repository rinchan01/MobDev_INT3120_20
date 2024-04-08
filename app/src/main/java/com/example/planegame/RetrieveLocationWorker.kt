package com.example.planegame

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.planegame.database.Player
import com.example.planegame.database.PlayerDatabase
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RetrieveLocationWorker(
    private val appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        var locationString: String
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(appContext)
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@withContext Result.failure()
        }

        val locationResult = suspendCoroutine { continuation ->
            fusedLocationProviderClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location ->
                    if (location == null) {
                        Toast.makeText(appContext, "Cannot get location.", Toast.LENGTH_SHORT).show()
                        continuation.resumeWithException(RuntimeException("Cannot get location"))
                    } else {
                        continuation.resume(location)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }


        val lat = locationResult.latitude
        val lon = locationResult.longitude
        locationString = "$lat, $lon"
        val username = PreferenceHelper(appContext).getUsername()
        val newPlayer = Player(username, 0, 0, "6", "defaultAvatar.png", locationString)

        val playerDao = PlayerDatabase.getDatabase(appContext).playerDao
        Log.d("NewPlayer", newPlayer.toString())
        playerDao.getPlayer(username) ?: playerDao.insertPlayer(newPlayer)

        Result.success(
            workDataOf(
                "LOCATION_STRING" to lat.toString() + "," + lon.toString()
            )
        )
    }
}

class SetWeatherBackgroundWorker(
    private val appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val response = try {
            Log.d("Worker", inputData.getString("LOCATION_STRING")?:"none")
            RetrofitInstance.weatherAPI.getCurrentWeather(q = inputData.getString("LOCATION_STRING")?:"none")
        } catch (e: Exception) {
            return@withContext Result.failure()
        }

        if(response.isSuccessful && response.body() != null) {
            return@withContext Result.success(
                workDataOf(
                    "WEATHER_STRING" to response.body()!!.current.condition.text,
                    "WEATHER_CODE" to response.body()!!.current.condition.code
                )
            )
        }

        return@withContext Result.failure()
    }
}