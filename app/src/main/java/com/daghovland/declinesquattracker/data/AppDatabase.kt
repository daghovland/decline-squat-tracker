package com.daghovland.declinesquattracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// @Database ties entity classes to the DB file and declares the version.
// Bump `version` + add a Migration whenever you change the schema.
// exportSchema = false skips writing schema JSON to disk (fine for a personal app).
@Database(entities = [SquatSet::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun squatSetDao(): SquatSetDao

    companion object {
        @Volatile  // ensures all threads see the same instance immediately
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                // Double-checked locking — the standard singleton pattern on Android
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,  // use applicationContext to avoid leaking Activity
                    AppDatabase::class.java,
                    "squat_tracker.db"
                ).build().also { INSTANCE = it }
            }
    }
}
