package noncom.pino.locationhistory.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "logs")
data class LocationLog(

    @PrimaryKey
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)