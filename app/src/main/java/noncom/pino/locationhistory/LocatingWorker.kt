package noncom.pino.locationhistory

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener


class LocatingWorker(private val context: Context, private val workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {

    private lateinit var lastLocation: Location

    // fetch location and write to disk
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {

        // instantiating components needed to fetch current location
        val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object: LocationCallback() {}
        val listener = OnCompleteListener<Location> { task ->

            if (task.isSuccessful && task.result != null) {

                lastLocation = task.result
                locationClient.removeLocationUpdates(locationCallback)

                // TODO: remove line
                Log.d("Location", "${lastLocation.latitude} : ${lastLocation.longitude}")

                // TODO: write entry to DB
                val entry = LocationEntry(System.currentTimeMillis(), lastLocation.latitude, lastLocation.longitude)
            }
            else { Log.d("Location", "failed to fetch location") }
        }
        try { locationClient.lastLocation.addOnCompleteListener(listener) }
        catch (e: SecurityException) { Log.e("Location", "lost location permission") }

        return Result.success()
    }
}