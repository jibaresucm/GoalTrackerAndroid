package model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MilestoneDAO {
    @Insert
    suspend fun insertMilestone(milestone: Milestone)

    @Query("SELECT * FROM milestones WHERE goalId = :goal_id" )
    suspend fun getMilestonesFromGoal(goal_id :Long): MutableList<Milestone>

    @Query("DELETE FROM milestones WHERE goalId = :goal_id" )
    suspend fun deleteMilestonesFrom(goal_id :Long)
}