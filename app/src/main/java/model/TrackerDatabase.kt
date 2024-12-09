package model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

// Defines the Room database for tracker entities, with a version number for migrations
@Database(entities = [Goal::class, Milestone::class, ActivityEntry::class], version = 13) // Increment the version when making schema changes
abstract class TrackerDatabase : RoomDatabase() {
    // Abstract function to access DAOs, which provides methods for database operations
    abstract fun goalDAO(): GoalDAO
    abstract fun activityEntryDAO(): ActivityEntryDAO
    abstract fun milestoneDAO(): MilestoneDAO

    companion object {
        @Volatile
        private var INSTANCE: TrackerDatabase? = null // Singleton instance to prevent multiple database instances

        // Returns the singleton instance of TrackerDatabase, creating it if it doesn’t exist
        fun getDatabase(context: Context): TrackerDatabase {
            // Checks if INSTANCE is already created; if not, it synchronizes to ensure only one instance is created
            return INSTANCE ?: synchronized(this) {
                // Builds the Room database with the specified configuration
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackerDatabase::class.java,
                    "tracker_db" // Sets the name of the database file
                ).build()
                INSTANCE = instance // Sets the INSTANCE to the newly created database
                instance // Returns the database instance
            }
        }
    }
}

