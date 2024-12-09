package view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.goaltracker.R
import utilities.BitmapConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddActivityEntry: AppCompatActivity(), LocationListener {
    private lateinit var imgView: ImageView;
    private var imageBitMap: Bitmap? = null
    private lateinit var title: EditText;
    private lateinit var locationText: TextView;
    private var imageSet = false;
    private var locationSet = false;
    private var entrySaved = false;
    private lateinit var locationManager: LocationManager
    private val cameraLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
            imageBitMap = data?.extras?.get("data") as Bitmap?
            imgView.setImageBitmap(imageBitMap);
            imageSet = true;
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        permission->
        val locationPermission = permission;

        if(locationPermission){
            updateLocation()
        }
        else{
            Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //init location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager;
        requestLocationUpdates();

        //init views
        title = findViewById(R.id.addActivityEntryTitle)
        imgView = findViewById(R.id.imageView)
        locationText = findViewById(R.id.addEntryLocation)
        val cameraBtn = findViewById<Button>(R.id.cameraButton)
        val addBtn = findViewById<Button>(R.id.addActivityBtn)
        val locationBtn = findViewById<Button>(R.id.locationButton)


        cameraBtn.setOnClickListener {
            cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        locationBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION // Fine location permission
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ){
                updateLocation()
            }
            else{
                requestLocationUpdates()
            }

        }

        addBtn.setOnClickListener {
            if(title.text.toString() == ""){
                Toast.makeText(this, "You need to take fill in the title!!!!", Toast.LENGTH_SHORT).show()
            }
            else if(!imageSet){
                Toast.makeText(this, "You need to take a picture!!!", Toast.LENGTH_SHORT).show()
            }
            else{
                entrySaved = true;
                val int = Intent()
                int.putExtra("IMAGE",
                    imageBitMap?.let { it1 -> BitmapConverter.converterBitmapToString(it1) })
                int.putExtra("DESCRIPTION", title.text.toString())
                if(locationSet){
                    int.putExtra("LOCATION", locationText.text.toString())
                } else int.putExtra("LOCATION", "")
                    int.putExtra("DATE", dateToString(Calendar.getInstance().time))

                setResult(RESULT_OK, int)
                finish()
            }
        }
        restoreInstanceState(savedInstanceState);
        
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        if(entrySaved) return;

        if(imageSet){
            outState.putString("IMAGE", imageBitMap?.let { it1 -> BitmapConverter.converterBitmapToString(it1) })
        }
        if(locationSet){
            outState.putString("LOCATION", locationText.text.toString())
        }
        outState.putString("DESCRIPTION",  title.text.toString())
        Log.d("Saving state", "Saving...")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroying activity", "Activity destroyed")
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) return;
        Log.d("Restoring state", "savedInstanceState is not null proceeding to restore")

        title.text.append(savedInstanceState.getString("DESCRIPTION"));

        if(savedInstanceState.containsKey("IMAGE")){
            imageSet = true;
            imageBitMap = BitmapConverter.converterStringToBitmap(savedInstanceState.getString("IMAGE").toString());
            imgView.setImageBitmap(imageBitMap);
        }

        if(savedInstanceState.containsKey("LOCATION")){
            locationSet = true;
            locationText.text = savedInstanceState.getString("LOCATION")
        }
    }

    fun requestLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION // Fine location permission
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, // Use GPS for location updates
                3000, // Minimum time interval between updates (1 second)
                30f, // Minimum distance interval between updates (1 meter)
                this // Pass the current activity as the LocationListener
            )
        }
        else {
            Toast.makeText(this, "Location permissions are required", Toast.LENGTH_SHORT).show()
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        val p0 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(p0 == null) return

        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(p0.latitude, p0.longitude, 1);
        if(address == null) return;
        val locationString = address[0].subLocality + ", " + address[0].adminArea + ", "+ address[0].countryName

        locationSet = true;
        locationText.text = locationString;
    }

    override fun onLocationChanged(p0: Location) {
        Log.d("Address", "location changed");
    }

    fun dateToString(date: Date): String
    {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return simpleDateFormat.format(date)
    }

}