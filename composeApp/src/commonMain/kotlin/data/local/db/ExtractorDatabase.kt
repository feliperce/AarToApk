package data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.local.dao.ExtractorDao
import data.local.entity.KeystoreEntity
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        KeystoreEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class ExtractorDatabase : RoomDatabase(), DB {
    abstract fun extractorDao(): ExtractorDao

    override fun clearAllTables() {
        super.clearAllTables()
    }

    companion object {
        const val DATABASE_NAME = "extractor.db"
    }
}

interface DB {
    fun clearAllTables(): Unit {}
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<ExtractorDatabase>
): ExtractorDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
