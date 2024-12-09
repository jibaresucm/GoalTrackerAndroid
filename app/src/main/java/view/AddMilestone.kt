package view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.goaltracker.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddMilestone : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.milestone_add)

        val desc = findViewById<EditText>(R.id.MilestoneAddDesc)
        val addBtn = findViewById<Button>(R.id.addMilestone)

        addBtn.setOnClickListener {
            if(desc.text.toString() == ""){
                Toast.makeText(this, "You need to take fill in the description!!!!", Toast.LENGTH_SHORT)
            }

            else{
                val int = Intent()
                int.putExtra("DESCRIPTION", desc.text.toString())
                int.putExtra("DATE", dateToString(Calendar.getInstance().time))
                setResult(RESULT_OK, int)
                finish()

            }
        }
    }

    fun dateToString(date: Date): String
    {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format(date)
    }
}