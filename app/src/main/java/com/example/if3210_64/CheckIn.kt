package com.example.if3210_64

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.security.Permission

class CheckIn : AppCompatActivity(), SensorEventListener {
    private lateinit var codeScanner: CodeScanner
    private lateinit var sensorManager: SensorManager
    private var text: TextView? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_in)

        // location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),0)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Thermometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        text = findViewById<TextView>(R.id.temp_button)

        // QR Scanner
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            post(it.text)
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        loadAmbientTemperature()
        codeScanner.startPreview()
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.values.isNotEmpty()) {
            text?.text = sensorEvent.values[0].toString();
        }
    }

    private fun loadAmbientTemperature() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
            Toast.makeText(this, "No Ambient Temperature Sensor !", Toast.LENGTH_LONG).show()
        }
    }

    private fun post(qr: String) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),0)
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location->
                if (location != null) {
                    var response = makeRequest(qr, location.latitude, location.longitude)
                    Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
                    if (response.success) {
                        val builder: AlertDialog.Builder? = this.let {
                            AlertDialog.Builder(it)
                        }

                        builder?.setMessage("Berhasil")

                        builder?.create()
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    }
                }
                else {
                        // can not find location
                    Toast.makeText(this, location, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun makeRequest(qr: String, lat: Double, long: Double): QrCodeResponse {
        val url = "https://perludilindungi.herokuapp.com/check-in/"
        var data = Data("", "")
        var response = QrCodeResponse(
            false,
            0,
            "Error fetch",
            data
        )
        val msg = JSONObject()
        msg.put("qrCode", qr)
        msg.put("latitude", lat)
        msg.put("longitude", long)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            msg,
            {
                data = Data(
                    it.getJSONObject("data").getString("userStatus"),
                    it.getJSONObject("data").getString("reason")
                )
                response = QrCodeResponse(
                    it.getBoolean("success"),
                    it.getInt("code"),
                    it.getString("message"),
                    data
                )
            },
            {
                response = QrCodeResponse(
                    false,
                    0,
                    "Err",
                    data
                )
            }
        )
        return response
    }


}

