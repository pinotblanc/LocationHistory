package noncom.pino.locationhistory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationLog::class], version = 1, exportSchema = false)
abstract class LocationHistoryDB: RoomDatabase() {

        abstract fun dao(): LocationHistoryDAO

        companion object {

                @Volatile
                private var INSTANCE: LocationHistoryDB? = null

                fun getDatabase(context: Context): LocationHistoryDB {

                        // check whether DB is initialized
                        if (INSTANCE == null) {
                                synchronized(this) {
                                        INSTANCE = Room.databaseBuilder(context.applicationContext, LocationHistoryDB::class.java, "locations.db").build()
                                }
                        }
                        return INSTANCE as LocationHistoryDB
                }
        }
}