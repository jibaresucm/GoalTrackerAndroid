package main


import android.app.Activity
import model.Goal
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import view.AddGoalActivity
import view.GoalActivity
import view.GoalAdapter
import controller.Controller

class MainActivity : AppCompatActivity() {
    private lateinit var goalList: MutableList<Goal>
    private lateinit var controller: Controller
    private lateinit var goalRecyclerView: RecyclerView
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var activityObj: MainActivity
    private val addGoalLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode == Activity.RESULT_OK){
            val extras = result.data?.extras;

            val goal = Goal(title = extras?.getString("TITLE").toString(), startedDate = extras?.getString("STARTDATE").toString(),
                details = extras?.getString("DETAILS").toString(), description = extras?.getString("DESCRIPTION").toString(),
                deadLine = extras?.getString("DEADLINE").toString())

            lifecycleScope.launch {
                val id = controller.insertGoal(goal)
                goal.id = id
                goalAdapter.insertGoal(goal);
            }
        }
    }
    private val goalLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val extras = result.data?.extras;

            when(extras?.getString("ACTION")){
                "UPDATE" ->{
                    val goal = Goal(title = extras?.getString("TITLE").toString(), startedDate = extras?.getString("STARTDATE").toString(),
                        details = extras?.getString("DETAILS").toString(), description = extras?.getString("DESCRIPTION").toString(),
                        deadLine = extras?.getString("DEADLINE").toString(), id = extras.getLong("ID"));

                    val pos = extras.getInt("INDEX")

                    goalList[pos] = goal;
                    goalAdapter.notifyItemChanged(pos);


                }

                "DELETE" ->{
                    val position = extras.getInt("INDEX")
                    goalList.removeAt(position);
                    goalAdapter.notifyDataSetChanged();

                    val id = extras.getLong("ID")

                    lifecycleScope.launch {
                        controller.deleteGoal(id);
                    }
                }
                else ->{

                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityObj = this;
        controller = Controller.getInstance(this)!!;

        goalRecyclerView = findViewById(R.id.goalRecyclerView)
        goalRecyclerView.layoutManager = LinearLayoutManager(this);

        goalAdapter = GoalAdapter(mutableListOf<Goal>(), activityObj)

        goalRecyclerView.adapter = goalAdapter

        val btn = findViewById<FloatingActionButton>(R.id.addGoal);

        /*lifecycleScope.launch {
            controller.deleteAllGoals()
        }*/

        lifecycleScope.launch {
            goalList =  controller.getGoalList();
            goalAdapter.updateGoals(goalList);
        }



        btn.setOnClickListener {
            val intent = Intent(this, AddGoalActivity::class.java);
            addGoalLauncher.launch(intent);
        }
    }

    fun openGoal(position:Int){
        val currgoal = goalList[position]
        val intent = Intent(this, GoalActivity::class.java).apply {
            putExtra("TITLE", currgoal.title)
            putExtra("DESCRIPTION", currgoal.description)
            putExtra("DETAILS", currgoal.details)
            putExtra("DEADLINE", currgoal.deadLine)
            putExtra("STARTDATE", currgoal.startedDate)
            putExtra("ID", currgoal.id)
            putExtra("INDEX", position)
        }
        goalLauncher.launch(intent);
    }
}