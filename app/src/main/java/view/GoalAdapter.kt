package view

import model.Goal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.R
import main.MainActivity

class GoalAdapter(private var goals: MutableList<Goal>, private val activityObj: MainActivity) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    inner class GoalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var view = itemView;
        var title = itemView.findViewById<TextView>(R.id.goalTitle)
        var desc = itemView.findViewById<TextView>(R.id.goalDesc)
        var date = itemView.findViewById<TextView>(R.id.goalDeadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_view, parent, false)
        return GoalViewHolder(view);
    }

    override fun getItemCount(): Int {
        return goals.size;
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.title.text = goals[position].title;
        holder.desc.text = goals[position].description;
        holder.date.text = goals[position].deadLine
        holder.view.setOnClickListener{
            activityObj.openGoal(position)
        }
    }

    fun updateGoals(list: MutableList<Goal>){
        goals = list;
        notifyDataSetChanged();
    }

    fun insertGoal(goal: Goal){
        goals.add(goal);
        notifyItemInserted(goals.size - 1)
    }
}