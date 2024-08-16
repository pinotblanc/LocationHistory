package noncom.pino.locationhistory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import noncom.pino.locationhistory.databinding.ActivityMainBinding


class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // ======= window composition ==============
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_map, R.id.navigation_timeline
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // =============================================
    }

    override fun onResume() {
        super.onResume()

        startTracking()
    }

    private fun startTracking() {

        // first, check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1002
            )
        }

        val locationManager = this@MainActivity.getSystemService(LOCATION_SERVICE) as LocationManager
        val locationListener = LocationListener { location ->
            Toast.makeText(
                this@MainActivity,
                "Current Location - ${location.latitude}:${location.longitude}",
                Toast.LENGTH_LONG
            ).show()
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 0f, locationListener)
            Toast.makeText(this@MainActivity, "started requests", Toast.LENGTH_LONG).show()
        }
        catch (e: SecurityException) {

            Toast.makeText(this@MainActivity, "exception: no permission!", Toast.LENGTH_SHORT).show()
        }

//
//        // check if app has background location permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 1001)
//        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (grantResults.isEmpty() || grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
//
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//            // TODO: what to do if permissions are denied
//        }
//    }
}