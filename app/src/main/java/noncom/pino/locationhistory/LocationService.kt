package noncom.pino.locationhistory

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter
import java.io.IOException


class LocationService: Service() {

    private lateinit var locationManager: LocationManager
    private val locationListener = object: LocationListener {

        override fun onLocationChanged(location: Location) { saveLocationToFile(location) }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onCreate() {

        println("created LocationService")

        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10f, locationListener)
        } else { println("error, locationService running without location permissions") }
    }

    private fun saveLocationToFile(location: Location) {

        val locationData = hashMapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "timestamp" to System.currentTimeMillis()
        )

        val json = Gson().toJson(locationData)
        val file = File(getExternalFilesDir(null), "location_data.json")
        try {
            FileWriter(file, true).apply {
                append(json)
                append("\n")
                flush()
                close()
            }
        } catch (e: IOException) { e.printStackTrace() }

        println("saved gps data! :)")
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }
}