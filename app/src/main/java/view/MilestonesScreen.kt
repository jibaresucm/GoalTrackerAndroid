package view
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import controller.Controller
import kotlinx.coroutines.launch
import model.Milestone
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

class MilestonesScreen: AppCompatActivity() {
    private lateinit var milestoneRecyclerView: RecyclerView
    private lateinit var milestoneAdapter: MilestoneAdapter
    private lateinit var milestoneList: MutableList<Milestone>
    private var goalId: Long = -1
    private lateinit var controller: Controller
    private var addMilestone: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val extras = result.data?.extras;
            val milestone = Milestone(
                date = extras?.getString("DATE").toString(),
                description = extras?.getString("DESCRIPTION").toString(),
                goalId = goalId
            )
            Log.d("goalId", goalId.toString());
            Log.d("date", extras?.getString("DATE").toString())
            milestoneAdapter.insertMilestone(milestone)

            lifecycleScope.launch {
                controller.insertMilestone(milestone);
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.milestone_screen)

        goalId = intent.getLongExtra("GOALID", -1);

        milestoneRecyclerView = findViewById(R.id.milestone_recycler_view)
        milestoneRecyclerView.layoutManager = LinearLayoutManager(this);

        milestoneAdapter = MilestoneAdapter(mutableListOf())

        milestoneRecyclerView.adapter = milestoneAdapter

        controller = Controller.getInstance(this)!!;

        lifecycleScope.launch{
            milestoneList = controller.getMilestonesList(goalId)
            milestoneAdapter.updateMilestones(milestoneList);
        }

        val addBtn = findViewById<FloatingActionButton>(R.id.addMilestoneBtn)

        addBtn.setOnClickListener {
            val int = Intent(this, AddMilestone::class.java)
            addMilestone.launch(int)
        }
    }
}