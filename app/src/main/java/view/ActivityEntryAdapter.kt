package view

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goaltracker.R
import model.ActivityEntry
import utilities.BitmapConverter

class ActivityEntryAdapter(private var activityEntriesList: MutableList<ActivityEntry>) : RecyclerView.Adapter<ActivityEntryAdapter.ActivityEntryViewHolder>() {

    class ActivityEntryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.activityEntryImg)
        val desc = itemView.findViewById<TextView>(R.id.activityEntryDesc)
        val locationdate = itemView.findViewById<TextView>(R.id.activityEntryDateLocation)
        var view = itemView;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityEntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_view, parent, false)
        return ActivityEntryViewHolder(view);
    }

    override fun getItemCount(): Int {
        return activityEntriesList.size;
    }

    override fun onBindViewHolder(holder: ActivityEntryViewHolder, position: Int) {
        val bitmap: Bitmap = BitmapConverter.converterStringToBitmap(activityEntriesList[position].image)!!
        val resized = Bitmap.createScaledBitmap(bitmap, bitmap?.width?.times(2) ?: 260, bitmap?.height?.times(2) ?: 464, false);
        holder.image.setImageBitmap(resized)
        holder.desc.text = activityEntriesList[position].description
        holder.locationdate.text = activityEntriesList[position].date + " " + activityEntriesList[position].location;

    }

    fun updateActivityEntries(list: MutableList<ActivityEntry>){
        activityEntriesList = list;
        notifyDataSetChanged();
    }

    fun insertActivityEntry(entry: ActivityEntry){
        activityEntriesList.add(0, entry)
        notifyItemInserted(0)
    }
}
