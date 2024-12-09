package controller

import android.content.Context
import model.ActivityEntry
import model.Goal
import model.Milestone
import model.TrackerDatabase

class Controller {

    companion object {
        private var instance: Controller? = null;
        private lateinit var database: TrackerDatabase;

        fun getInstance(context: Context): Controller? {
            if(instance == null){
                instance = Controller();
                database = TrackerDatabase.getDatabase(context);
            }
            return instance;
        }
    }

    suspend fun getGoalList(): MutableList<Goal> {
            return database.goalDAO().getAllGoals();
    }

    suspend fun insertGoal(goal: Goal): Long{
       return database.goalDAO().insertGoal(goal)
    }

    suspend fun deleteGoal(id :Long){
        database.goalDAO().deleteGoal(id);
        database.activityEntryDAO().deleteEntriesFromGoal(id)
        database.milestoneDAO().deleteMilestonesFrom(id)
    }

    suspend fun updateGoal(goal: Goal){
        database.goalDAO().updateGoal(goal)
    }

    /*suspend fun deleteAllGoals(){
        database.goalDAO().deleteAllGoals()
    }*/

    suspend fun insertActivityEntry(activityEntry: ActivityEntry): Long{

        return database.activityEntryDAO().insertActivityEntry(activityEntry)
    }

    suspend fun getActivityEntryList(id :Long): MutableList<ActivityEntry> {
        return database.activityEntryDAO().getActivityEntriesFromGoal(id)
    }

    suspend fun getMilestonesList(id: Long) : MutableList<Milestone>{
        return database.milestoneDAO().getMilestonesFromGoal(id);
    }

    suspend fun insertMilestone(milestone: Milestone) {
        database.milestoneDAO().insertMilestone(milestone);
    }
}