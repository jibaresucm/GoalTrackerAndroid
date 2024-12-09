package view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView.OnDateChangeListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import controller.Controller
import kotlinx.coroutines.launch
import model.ActivityEntry
import model.Goal


class GoalActivity: AppCompatActivity() {
    private lateinit var activityRecyclerView: RecyclerView
    private lateinit var activityEntryAdapter: ActivityEntryAdapter
    private lateinit var entryList: MutableList<ActivityEntry>
    private var goalId: Long = -1
    private var pos: Int = -1
    private var goalStart: String = "XX/XX/XXXX"
    private lateinit var controller: Controller
    private var resultIntent = Intent();
    private var addActivityEntryLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val extras = result.data?.extras
            val entry = ActivityEntry(goalId = goalId, description = extras?.getString("DESCRIPTION").toString(),
                date = extras?.getString("DATE").toString(), image = extras?.getString("IMAGE").toString(), location = extras?.getString("LOCATION").toString())

            lifecycleScope.launch{
                entry.id = controller.insertActivityEntry(entry);
                activityEntryAdapter.insertActivityEntry(entry)
            }

        }
    }
    private var editGoalLauncher :ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val extra = result.data?.extras;

            val goalTitle = extra?.getString("TITLE").toString()
            val goalDesc = extra?.getString("DESCRIPTION").toString()
            val goalDet = extra?.getString("DETAILS").toString()
            val goalDead = extra?.getString("DEADLINE").toString()
            //goalStart
            //goalId

            val goal = Goal(id = goalId, startedDate = goalStart, deadLine = goalDead, details = goalDet, title = goalTitle, description = goalDesc)
            lifecycleScope.launch {
                controller.updateGoal(goal);
            }

            resultIntent.putExtra("ACTION", "UPDATE")
            resultIntent.putExtra("TITLE", goalTitle)
            resultIntent.putExtra("DESCRIPTION", goalDesc)
            resultIntent.putExtra("DETAILS", goalDet)
            resultIntent.putExtra("DEADLINE", goalDead)
            resultIntent.putExtra("STARTDATE", goalStart)
            resultIntent.putExtra("ID", goalId)
            resultIntent.putExtra("INDEX", pos)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goal_screen)


        //Controller Init
        controller = Controller.getInstance(this)!!;

        //Info parse and Info set
        val goalTitle = intent.getStringExtra("TITLE")
        val goalDesc = intent.getStringExtra("DESCRIPTION")
        val goalDet = intent.getStringExtra("DETAILS")
        val goalDead = intent.getStringExtra("DEADLINE")
        goalStart = intent.getStringExtra("STARTDATE").toString()
        goalId = intent.getLongExtra("ID", -1)
        pos = intent.getIntExtra("INDEX", -1)
        activityRecyclerView = findViewById(R.id.activityEntryRecyclerView)

        val title = findViewById<TextView>(R.id.goal_title)
        val desc = findViewById<TextView>(R.id.goal_desc)
        val det = findViewById<TextView>(R.id.goal_details)
        val dead = findViewById<TextView>(R.id.goal_deadline)
        val start = findViewById<TextView>(R.id.goal_started)





        title.text = goalTitle;
        desc.text = goalDesc;
        det.text = goalDet;
        dead.text = "Deadline: " + goalDead;
        start.text = "Started: " + goalStart;

        //Delete dialog conf
        val deleteDialog = Dialog(this);
        deleteDialog.setContentView(R.layout.delete_goal_dialog)

        val deleteDialogBtn = deleteDialog.findViewById<Button>(R.id.deleteDeleteGoalDialog)
        deleteDialogBtn.setOnClickListener {
            resultIntent.putExtra("ACTION", "DELETE")
            resultIntent.putExtra("ID", goalId)
            resultIntent.putExtra("INDEX", pos)
            setResult(RESULT_OK, resultIntent);
            finish()
        }

        val cancelDialogBtn = deleteDialog.findViewById<Button>(R.id.cancelDeleteGoalDialog)
        cancelDialogBtn.setOnClickListener {
            deleteDialog.dismiss()
        }


        //Buttons functionality
        val addEntryBtn = findViewById<FloatingActionButton>(R.id.addEntry)
        addEntryBtn.setOnClickListener {
            val int = Intent(this, AddActivityEntry::class.java)
            addActivityEntryLauncher.launch(int)
        }

        val milestoneBtn = findViewById<Button>(R.id.milestones_button)
        milestoneBtn.setOnClickListener {
            val int = Intent(this, MilestonesScreen::class.java)
            int.putExtra("GOALID", goalId)
            startActivity(int)
        }

        val editBtn = findViewById<ImageButton>(R.id.editGoal)
        editBtn.setOnClickListener{
            val int = Intent(this, AddGoalActivity::class.java)
            int.putExtra("ACTION", "UPDATE")
            int.putExtra("TITLE", goalTitle)
            int.putExtra("DESCRIPTION", goalDesc)
            int.putExtra("DETAILS", goalDet)
            int.putExtra("DEADLINE", goalDead)
            editGoalLauncher.launch(int);
        }

        val deleteBtn = findViewById<ImageButton>(R.id.deleteGoal)
        deleteBtn.setOnClickListener{
            deleteDialog.show();
        }

        //RecyclerView Conf
        activityRecyclerView.layoutManager = GridLayoutManager(this, 2)
        activityEntryAdapter = ActivityEntryAdapter(mutableListOf<ActivityEntry>())

        activityRecyclerView.adapter = activityEntryAdapter

        //Database get
        lifecycleScope.launch {
            entryList  = controller.getActivityEntryList(goalId);
            activityEntryAdapter.updateActivityEntries(entryList)
        }
    }

}