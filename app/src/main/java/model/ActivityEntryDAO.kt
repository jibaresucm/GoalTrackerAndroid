package model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ActivityEntryDAO {
    @Insert
    suspend fun insertActivityEntry(activityEntry: ActivityEntry): Long

    @Query("SELECT * FROM activities WHERE goalId = :id ORDER BY id DESC")
    suspend fun getActivityEntriesFromGoal(id :Long): MutableList<ActivityEntry>

    @Query("DELETE FROM activities WHERE goalId = :idGoal")
    suspend fun deleteEntriesFromGoal(idGoal: Long)
}