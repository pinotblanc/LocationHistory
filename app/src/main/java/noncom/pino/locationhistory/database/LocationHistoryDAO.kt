package noncom.pino.locationhistory.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(entry: LocationLog)

    @Query("SELECT * FROM logs ORDER BY timestamp DESC LIMIT 1")
    fun getLastLocation(): LocationLog

    @Query("SELECT * FROM logs ORDER BY timestamp DESC")
    fun getLocationsNewestFirst(): Flow<List<LocationLog>>
}