package noncom.pino.locationhistory

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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

        val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        val request = LocationRequest.Builder(5*1000L).build()
        val locationCallback = object: LocationCallback() {

            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.locations.lastOrNull()?.let { location ->

                    Toast.makeText(
                        this@MainActivity,
                        "${location.latitude} : ${location.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        locationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

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