package com.example.valvecontrol.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.BluetoothGattCharacteristic.*
import android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY
import android.bluetooth.BluetoothGattService.SERVICE_TYPE_SECONDARY
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.beepiz.bluetooth.gattcoroutines.ExperimentalBleGattCoroutinesCoroutinesApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import splitties.bitflags.hasFlag
import splitties.systemservices.bluetoothManager
import java.util.*


private const val GATT_MAX_MTU_SIZE = 517

class MainActivity : ComponentActivity() {

    private val permissionsOldApi = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissionsApi29 = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.S)
    private val permissionsApi31 = listOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            map.entries.forEach {
                if (it.value) {
                    Log.d(MY_TAG, "${it.key} permission is granted")
                    Toast.makeText(
                        this,
                        "isGranted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(MY_TAG, "${it.key} permission is NOT granted")
                    Toast.makeText(
                        this,
                        "not granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = when {
            isApi(Build.VERSION_CODES.S) -> permissionsApi31
            isApi(Build.VERSION_CODES.Q) -> permissionsApi29
            else -> permissionsOldApi
        }
        checkPermissions(permissions)
        checkBLE()
//        lifecycleScope.launch {
//            logNameAndAppearance()
//        }
//        lifecycleScope.launch {
//            deviceFor(mySecondPhone).logGattServices()
////            delay(3_000)
////            deviceFor(mySecondPhone).writeCharacteristic()
////            delay(10_000)
////            deviceFor(mySecondPhone).logGattServices()
//        }
//        scanLeDevice()

//        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        val mBluetoothAdapter = bluetoothManager.adapter
//        startScan(mBluetoothAdapter)
        setContent {
            ValveApp()
        }
    }

    private val bluetoothLeScanner = bluetoothManager.adapter.bluetoothLeScanner
    private var scanning = false
    private var isScanning = false
    private val handler = Handler()
    private var mHandler = Handler()
    private var mScanResults = mutableMapOf<String, BluetoothDevice>()
    private var mScanCallback = BtleScanCallback(mScanResults)

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10_000

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                Log.d(MY_TAG, "stop scan")
                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            Log.d(MY_TAG, "start scan")
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    //    private val leDeviceListAdapter = LeDeviceListAdapter()
    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(MY_TAG, "scan result $callbackType address: ${result.device}")
            super.onScanResult(callbackType, result)
//            leDeviceListAdapter.addDevice(result.device)
//            leDeviceListAdapter.notifyDataSetChanged()
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d(MY_TAG, "scan fail errorCode $errorCode")
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScan(mBluetoothAdapter: BluetoothAdapter) {
        if (!hasPermissions(mBluetoothAdapter) || isScanning) {
            return
        }
        val filters: List<ScanFilter> = ArrayList()
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()

        Log.d(MY_TAG, "startScan")
        mBluetoothAdapter.bluetoothLeScanner.startScan(filters, settings, mScanCallback)
        isScanning = true
        mHandler.postDelayed({ stopScan(mBluetoothAdapter) }, SCAN_PERIOD)
    }

    @SuppressLint("MissingPermission")
    private fun stopScan(mBluetoothAdapter: BluetoothAdapter) {
        Log.d(MY_TAG, "stopScan")
        if (isScanning && mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.bluetoothLeScanner.stopScan(mScanCallback)
            scanComplete()
        }
        isScanning = false
    }

    @SuppressLint("MissingPermission")
    private fun scanComplete() {
        Log.d(MY_TAG, "scanComplete")
        if (mScanResults.isEmpty()) {
            return
        }
        lifecycleScope.launch {
            for (entry in mScanResults.entries) {
                Log.d(
                    MY_TAG,
                    "Found device: ${entry.key} ${entry.value.name} ${entry.value.uuids} ${entry.value.type}"
                )
                logNameAndAppearance(entry.key)
//                delay(3_000L)
            }
        }
    }


    private class BtleScanCallback(val mScanResults: MutableMap<String, BluetoothDevice>) :
        ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(MY_TAG, "BLE Scan Failed with code $errorCode")
        }

        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceAddress = device.address
//            Log.d(MY_TAG, "BLE ScanResult $device ${result.scanRecord?.deviceName}")
            mScanResults[deviceAddress] = device
        }
    }

    private fun hasPermissions(mBluetoothAdapter: BluetoothAdapter): Boolean {
        if (!mBluetoothAdapter.isEnabled) {
            Log.d(MY_TAG, "1")

            requestBluetoothEnable()
            return false
        } /*else if (!hasLocationPermissions()) {
            Log.d(MY_TAG, "2")
            requestLocationPermission()
            return false
        }*/
        return true
    }

    private fun requestBluetoothEnable() {
//        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//        Log.d(MY_TAG, "Requested user enables Bluetooth. Try starting the scan again.")
    }

    private fun hasLocationPermissions(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
//        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
    }

    private fun checkPermission(permissionName: String) = ContextCompat.checkSelfPermission(
        this,
        permissionName
    ) == PackageManager.PERMISSION_GRANTED.also {
        Log.d(MY_TAG, "$permissionName PERMISSION_GRANTED")
    }

    private fun checkPermissions(permissions: List<String>) {
        permissions.filter {
            checkPermission(it).not()
        }.also {
            Log.d(MY_TAG, "permissions ask")
            requestPermissionLauncher.launch(it.toTypedArray())
        }
//        when {
//
////            shouldShowRequestPermissionRationale(permissionName) -> {
////                // In an educational UI, explain to the user why your app requires this
////                // permission for a specific feature to behave as expected. In this UI,
////                // include a "cancel" or "no thanks" button that allows the user to
////                // continue using your app without granting the permission.
////                Log.d(MY_TAG, "$permissionName shouldShowRequestPermissionRationale")
////            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                Log.d(MY_TAG, "permissions ask")
//                requestPermissionLauncher.launch(permissions.toTypedArray())
//            }
//        }
    }

    private fun checkBLE() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d(MY_TAG, "Support BLUETOOTH_LE")
        } else {
            Log.d(MY_TAG, "NOT Support BLUETOOTH_LE")
        }
    }

    private val myEddystoneUrlBeaconMacAddress = "F2:D6:43:93:70:7A"
    private val mySecondPhone = "D0:28:BA:18:4B:6C"
    private val defaultDeviceMacAddress = mySecondPhone

    @SuppressLint("MissingPermission")
    suspend fun logNameAndAppearance(deviceMacAddress: String = defaultDeviceMacAddress) {
        deviceFor(deviceMacAddress).useBasic { device, services ->
            services.forEach { Log.d(MY_TAG, "Service found with UUID: ${it.uuid}") }
            with(GenericAccess) {
                device.readAppearance()
                Log.d(MY_TAG, "Device appearance: ${device.appearance}")
                device.readDeviceName()
                Log.d(MY_TAG, "Device name: ${device.deviceName}")
            }
        }
    }

    fun deviceFor(macAddress: String): BluetoothDevice =
        bluetoothManager.adapter.getRemoteDevice(macAddress)

    @OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun BluetoothDevice.logGattServices(tag: String = "BleGattCoroutines") {
        val deviceConnection = GattConnection(bluetoothDevice = this@logGattServices)
        try {
            deviceConnection.connect() // Suspends until connection is established
            val gattServices = deviceConnection.discoverServices() // Suspends until completed
            gattServices.forEach {
                it.characteristics.forEach {
                    try {
                        deviceConnection.readCharacteristic(it) // Suspends until characteristic is read
                    } catch (e: Exception) {
                        Log.e(tag, "Couldn't read characteristic with uuid: ${it.uuid}", e)
                    }
                }
                Log.d(tag, it.print(printCharacteristics = true))
            }
        } finally {
            deviceConnection.close() // Close when no longer used. Also triggers disconnect by default.
        }
    }

    @OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun BluetoothDevice.writeCharacteristic(tag: String = "BleGattCoroutinesWrite") {
        val deviceConnection = GattConnection(bluetoothDevice = this@writeCharacteristic)
        try {
            deviceConnection.connect() // Suspends until connection is established
//            deviceConnection.requestMtu(GATT_MAX_MTU_SIZE)
            val uuid = UUID.randomUUID()
            val properties = PROPERTY_WRITE
            val instanceId = 35
            val permissions = PERMISSION_WRITE
            val characteristic = BluetoothGattCharacteristic(uuid, properties, permissions).also {
                it.writeType = WRITE_TYPE_DEFAULT
                it.setValue(7_777_777, FORMAT_SINT32, 0)
                it.service
            }
            try {
                Log.e(tag, "start write characteristic with uuid: $uuid")
                deviceConnection.writeCharacteristic(characteristic) // Suspends until characteristic is read
                Log.e(tag, "end write characteristic with uuid: $uuid")
            } catch (e: Exception) {
                Log.e(tag, "Couldn't write characteristic with uuid: $uuid", e)
            }
        } finally {
            deviceConnection.close() // Close when no longer used. Also triggers disconnect by default.
        }
    }

    /**
     * Connects to the device, discovers services, executes [block] and finally closes the connection.
     */
    @SuppressLint("MissingPermission")
    @OptIn(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    suspend inline fun BluetoothDevice.useBasic(
        connectionTimeoutInMillis: Long = 5000L,
        block: (GattConnection, List<BluetoothGattService>) -> Unit
    ) {
        val deviceConnection = GattConnection(this)
        try {
            deviceConnection.logConnectionChanges()
            withTimeout(connectionTimeoutInMillis) {
                deviceConnection.connect()
            }
            Log.i(MY_TAG, "Connected!")
            val services = deviceConnection.discoverServices()
            Log.i(MY_TAG, "Services discovered!")
            block(deviceConnection, services)
        } catch (e: TimeoutCancellationException) {
            Log.e(
                MY_TAG,
                "Connection timed out after $connectionTimeoutInMillis milliseconds!".also {
                    Toast.makeText(
                        this@MainActivity,
                        "$it",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(MY_TAG, "$e")
        } finally {
            deviceConnection.close()
            Log.i(MY_TAG, "Closed!")
        }
    }

    @ObsoleteCoroutinesApi
    fun GattConnection.logConnectionChanges() {
        GlobalScope.launch(Dispatchers.Main) {
            stateChangeChannel.consumeEach {
                Log.i(MY_TAG, "connection state changed: $it")
            }
        }
    }

    fun BluetoothGattService.print(printCharacteristics: Boolean = true): String {
        return if (printCharacteristics) printWithCharacteristics() else printWithoutCharacteristics()
    }

    fun BluetoothGattService.printWithoutCharacteristics(): String = """UUID: $uuid
instance ID: $instanceId
type: $typeString
characteristics count: ${characteristics.count()}
included services count: ${includedServices?.count()}
"""

    fun BluetoothGattService.printWithCharacteristics(): String = """UUID: $uuid
instance ID: $instanceId
type: $typeString
characteristics: {
${characteristics.joinToString { it.print() }.prependIndent()}
}
included services count: ${includedServices?.count()}
"""
}

private val BluetoothGattService.typeString: String
    get() = when (type) {
        SERVICE_TYPE_PRIMARY -> "PRIMARY"
        SERVICE_TYPE_SECONDARY -> "SECONDARY"
        else -> "UNKNOWN"
    }

private val BluetoothGattCharacteristic.writeTypeString: String
    get() = when (writeType) {
        WRITE_TYPE_DEFAULT -> "DEFAULT"
        WRITE_TYPE_NO_RESPONSE -> "NO_RESPONSE"
        WRITE_TYPE_SIGNED -> "SIGNED"
        else -> "UNKNOWN"
    }

private val BluetoothGattCharacteristic.propertiesString: String
    get() = propertiesString(properties)

private val BluetoothGattCharacteristic.permissionsString: String
    get() {
        return "$permissions"
        // return permissionsString(permissions)
    }

fun BluetoothGattCharacteristic.print(): String = """UUID: $uuid
instance ID: $instanceId
permissions: $permissionsString
writeType: $writeTypeString
properties: $propertiesString
value: $value
stringValue: ${getStringValue(0)}
"""

private fun propertiesString(properties: Int): String = StringBuilder().apply {
    if (properties.hasFlag(PROPERTY_READ)) append("READ, ")
    if (properties.hasFlag(PROPERTY_WRITE)) append("WRITE, ")
    if (properties.hasFlag(PROPERTY_WRITE_NO_RESPONSE)) append("WRITE_NO_RESPONSE, ")
    if (properties.hasFlag(PROPERTY_SIGNED_WRITE)) append("SIGNED_WRITE, ")
    if (properties.hasFlag(PROPERTY_INDICATE)) append("INDICATE, ")
    if (properties.hasFlag(PROPERTY_NOTIFY)) append("NOTIFY, ")
    if (properties.hasFlag(PROPERTY_BROADCAST)) append("BROADCAST, ")
    if (properties.hasFlag(PROPERTY_EXTENDED_PROPS)) append("EXTENDED_PROPS, ")
}.toString()