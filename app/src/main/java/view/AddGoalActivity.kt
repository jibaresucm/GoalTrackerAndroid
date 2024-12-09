package view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.set
import com.example.goaltracker.R
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddGoalActivity : AppCompatActivity() {
    private lateinit var title:EditText
    private lateinit var desc:EditText
    private lateinit var det:EditText
    private lateinit var dead:CalendarView
    private var year = -1
    private var day = -1
    private var month = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goal_add)


        title = findViewById<EditText>(R.id.addGoalTitleEdit)
        desc = findViewById<EditText>(R.id.addGoalDescEdit)
        det = findViewById<EditText>(R.id.addGoalDetEdit)
        dead = findViewById<CalendarView>(R.id.addGoalDeadline)

        dead.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            day = dayOfMonth
            this.month = month + 1
            this.year = year
            Log.d("New Date", "" + day + "/" + month + "/" + year)
        }

        handleAction();

        val addBtn = findViewById<Button>(R.id.addGoalBtn)
        addBtn.setOnClickListener {
            handleAdd();
        }


    }

    private fun handleAdd(){
        if(title.text.toString() == "" || det.text.toString() == ""|| desc.text.toString() == ""){
            Toast.makeText(this, "You need to fill all the options!!!", Toast.LENGTH_SHORT).show()
        }
        else{
            val int = Intent()

            int.putExtra("TITLE", title.text.toString())
            int.putExtra("DETAILS", det.text.toString())
            int.putExtra("DESCRIPTION", desc.text.toString())
            if(day == -1) int.putExtra("DEADLINE", dateToString(Date(dead.date)))
            else  int.putExtra("DEADLINE", "$day/$month/$year")

            int.putExtra("STARTDATE", dateToString(Calendar.getInstance().time))

            setResult(RESULT_OK, int)
            finish()

        }
    }

    private fun handleAction(){
        if(intent.hasExtra("ACTION")){
            when(intent.getStringExtra("ACTION")){
                "UPDATE" -> {

                    desc.setText(intent.getStringExtra("DESCRIPTION").toString())
                    det.setText(intent.getStringExtra("DETAILS").toString())
                    title.setText(intent.getStringExtra("TITLE").toString())

                    val trimmedDate = intent.getStringExtra("DEADLINE").toString().split('/')
                    var calendar = Calendar.getInstance()

                    calendar.set(Calendar.DAY_OF_MONTH, trimmedDate[0].toInt())
                    calendar.set(Calendar.MONTH, trimmedDate[1].toInt() -1)
                    calendar.set(Calendar.YEAR, trimmedDate[2].toInt())

                    val miliSecs = calendar.timeInMillis

                    dead.date = miliSecs



                }

            }
        }
    }

    fun dateToString(date: Date): String
    {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format(date)
    }



}