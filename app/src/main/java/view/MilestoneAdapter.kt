package view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.R
import model.ActivityEntry
import model.Milestone

class MilestoneAdapter(private var milestoneList: MutableList<Milestone>): RecyclerView.Adapter<MilestoneAdapter.MilestoneViewHolder>() {

    class MilestoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var date = itemView.findViewById<TextView>(R.id.milestoneDate)
        var desc = itemView .findViewById<TextView>(R.id.milestoneDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.milestone_view, parent, false);
        return MilestoneViewHolder(view);
    }

    override fun getItemCount(): Int {
        return milestoneList.size;
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.date.text = milestoneList[position].date
        holder.desc.text = milestoneList[position].description
    }

    fun updateMilestones(list: MutableList<Milestone>){
        milestoneList = list;
        notifyDataSetChanged();
    }

    fun insertMilestone(milestone: Milestone){
        milestoneList.add(0, milestone)
        notifyItemInserted(0)
    }
}