package com.example.loginpage

import android.Manifest
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val TARGET_MAC_ADDRESS = "00:00:13:01:12:65"
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var bluetoothSocket: BluetoothSocket? = null

    private lateinit var etCustID: EditText
    private lateinit var etCustPass: EditText
    private lateinit var btnOpen: Button
    private lateinit var btnClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCustID = findViewById(R.id.custID)
        etCustPass = findViewById(R.id.custPass)
        btnOpen = findViewById(R.id.btnOpen)
        btnClose = findViewById(R.id.btnClose)

        autoConnect()

        btnOpen.setOnClickListener {
            sendData("1")
        }

        btnClose.setOnClickListener {
            sendData("0")
        }
    }

    private fun autoConnect() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Toast.makeText(this, "Bluetooth is OFF!", Toast.LENGTH_LONG).show()
            return
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
            Toast.makeText(this, "Give me the Permission", Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show()

        Thread {
            try {
                val device = bluetoothAdapter.getRemoteDevice(TARGET_MAC_ADDRESS)
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID)
                bluetoothSocket?.connect()

                runOnUiThread {
                    Toast.makeText(this, "Connected successfully!", Toast.LENGTH_SHORT).show()

                    enableTheUI()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Connection failed: " + e.message, Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun enableTheUI() {
        etCustID.isEnabled = true
        etCustPass.isEnabled = true
        btnOpen.isEnabled = true
        btnClose.isEnabled = true
    }

    private fun sendData(message: String) {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.outputStream.write(message.toByteArray())
                Toast.makeText(this, "Sent: $message", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this, "Sending failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}