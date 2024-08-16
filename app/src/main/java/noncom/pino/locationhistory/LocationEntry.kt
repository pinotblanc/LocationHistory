package noncom.pino.locationhistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntry(

    @PrimaryKey
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)