package com.example.valvecontrol.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.ui.*
import com.example.valvecontrol.ui.Constants.CHARACTERISTIC_SEGMENT1_UUID
import com.example.valvecontrol.ui.Constants.CHARACTERISTIC_SEGMENT2_UUID
import com.example.valvecontrol.ui.Constants.CHARACTERISTIC_SEGMENT3_UUID
import com.example.valvecontrol.ui.Constants.CHARACTERISTIC_SEGMENT4_UUID
import com.example.valvecontrol.ui.Constants.SERVICE_UUID
import com.example.valvecontrol.ui.Constants.USERS_TABLE_NAME
import com.example.valvecontrol.ui.Constants.USERS_TABLE_SETTINGS_COLLECTION
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.Event
import com.example.valvecontrol.ui.main.viewmodel.IMainViewModel.PresenterEvent
import com.example.valvecontrol.ui.main.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import splitties.systemservices.bluetoothManager
import java.io.UnsupportedEncodingException
import java.util.*


class MainActivity : ComponentActivity() {

    companion object {
        private const val SCAN_PERIOD: Long = 10_000
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var gatt: BluetoothGatt
    private lateinit var bluetoothLeAdvertiser: BluetoothLeAdvertiser

    private var isScan = false
    private var segmentInitialized = false

    private var scanning = false
    private val handler = Handler()
    private val bluetoothLeScanner = bluetoothManager.adapter.bluetoothLeScanner

    private var isScanning = false
    private var connected = false
    private var mHandler = Handler()
    private var mScanResults = mutableMapOf<String, BluetoothDevice>()
    private var mScanCallback = BtleScanCallback(mScanResults)

    private val viewModel: IMainViewModel by inject<MainViewModel>()

    inner class GattClientCallback : BluetoothGattCallback() {
        @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.d(MY_TAG, "onConnectionStateChange GATT_FAILURE")
                disconnectGattServer()
                return
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.d(MY_TAG, "onConnectionStateChange != BluetoothGatt.GATT_SUCCESS")
                disconnectGattServer()
                return
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(MY_TAG, "onConnectionStateChange STATE_CONNECTED")
                gatt.discoverServices()
                connected = true
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(MY_TAG, "onConnectionStateChange STATE_DISCONNECTED")
                disconnectGattServer()
            }
        }

        @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.d(MY_TAG, "onServicesDiscovered != BluetoothGatt.GATT_SUCCESS")
                return
            }
            Log.d(MY_TAG, "onServicesDiscovered == BluetoothGatt.GATT_SUCCESS")
            val service = gatt!!.getService(SERVICE_UUID)
            val characteristicSegment1 = service.getCharacteristic(CHARACTERISTIC_SEGMENT1_UUID)
            val characteristicSegment2 = service.getCharacteristic(CHARACTERISTIC_SEGMENT2_UUID)
            val characteristicSegment3 = service.getCharacteristic(CHARACTERISTIC_SEGMENT3_UUID)
            val characteristicSegment4 = service.getCharacteristic(CHARACTERISTIC_SEGMENT4_UUID)
            characteristicSegment1.writeType = WRITE_TYPE_DEFAULT
            characteristicSegment2.writeType = WRITE_TYPE_DEFAULT
            characteristicSegment3.writeType = WRITE_TYPE_DEFAULT
            characteristicSegment4.writeType = WRITE_TYPE_DEFAULT
            segmentInitialized = gatt.setCharacteristicNotification(characteristicSegment1, true)
                    && gatt.setCharacteristicNotification(characteristicSegment2, true)
                    && gatt.setCharacteristicNotification(characteristicSegment3, true)
                    && gatt.setCharacteristicNotification(characteristicSegment4, true)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(MY_TAG, "onCharacteristicWrite value ${characteristic?.value} status $status")
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            Log.d(MY_TAG, "onCharacteristicRead value ${characteristic?.value} status $status")
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            val messageBytes = characteristic!!.value
            var messageString: String? = null
            try {
                messageString = String(messageBytes, Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                Log.e(MY_TAG, "Unable to convert message bytes to string")
            }
            Log.d(MY_TAG, "Received message: $messageString")
        }
    }

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

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(MY_TAG, "onCreate")
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.firestore
//        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeAdvertiser = bluetoothManager.adapter.bluetoothLeAdvertiser
        observeViewModel(viewModel)
//        val permissions = resolveNeededPermissions()
//        checkPermissions(permissions)
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
        viewModel.sendEvent(Event.GetValveSettings)
        startScanning()
//        connectDevice(deviceFor(defaultDeviceMacAddress))
        setContent {
            Log.d(MY_TAG, "setContent")
            AppContent()
        }
    }

    @Composable
    private fun AppContent() {
        val userToken by viewModel.firebaseUserToken.collectAsState()
        Log.d(MY_TAG, "userToken $userToken")
        if (userToken != null) {
            ValveApp()
        } else {
            AuthValveApp()
        }
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private fun connectDevice(device: BluetoothDevice) {
        Log.d(MY_TAG, "connectDevice ${device.address}")
        val gattClientCallback = GattClientCallback()
        gatt = device.connectGatt(this, false, gattClientCallback)
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    fun disconnectGattServer() {
        isScan = false
        gatt.disconnect()
        gatt.close()
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    private fun startScanning() {
        mScanResults = HashMap()
        mScanCallback = BtleScanCallback(mScanResults)

        val filters = mutableListOf<ScanFilter>()
        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(SERVICE_UUID))
            .build()
        filters.add(scanFilter)
        val settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
        bluetoothLeScanner.startScan(filters, settings, mScanCallback)

        mHandler = Handler()
        mHandler.postDelayed(::stopScan, SCAN_PERIOD)

        isScan = true
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    private fun stopScan() {
        if (isScan && bluetoothManager.adapter.isEnabled) {
            bluetoothLeScanner.stopScan(mScanCallback)
            scanComplete()
        }
        isScan = false
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private fun applyValveSetting(valveSetting: ValveSetting) {
        Log.d(MY_TAG, "sendMessage connected $connected segmentInitialized $segmentInitialized")
        if (!connected || !segmentInitialized) {
            return
        }
        val service = gatt.getService(SERVICE_UUID)
//        val message = "Hello"
        valveSetting.toCharacteristics().forEach { (uuid, value) ->
            service.writeCharacteristic(uuid, value.toString())
        }
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    private fun BluetoothGattService.writeCharacteristic(uuid: UUID, value: String) {
        val characteristic = getCharacteristic(uuid)
        var messageBytes = ByteArray(0)
        try {
            messageBytes = value.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            Log.e(MY_TAG, "Failed to convert string value $value uuid $uuid to byte array", e)
        }
        characteristic.value = messageBytes
        val success = gatt.writeCharacteristic(characteristic)
        Log.d(MY_TAG, "writeCharacteristic uuid $uuid success $success")
    }

    private fun observeViewModel(viewModel: IMainViewModel) = lifecycleScope.run {
        observe(viewModel.presenterEvent, ::handlePresenterEvent)
    }

    @SuppressLint("MissingPermission")
    private fun handlePresenterEvent(event: PresenterEvent) {
        when (event) {
            is PresenterEvent.SignUp -> handleSignUp(event)
            is PresenterEvent.Login -> handleLogin(event)
            is PresenterEvent.AddValveSetting -> handleAddValveSetting(event.valveSetting)
            is PresenterEvent.GetValveSettings -> handleGetSettings()
            is PresenterEvent.StartScan -> startScan()
            is PresenterEvent.ConnectDevice -> connectDevice(event.device)
            is PresenterEvent.ApplyValveSetting -> applyValveSetting(event.valveSetting)
        }
    }

    private fun handleSignUp(event: PresenterEvent.SignUp) {
        Log.d(MY_TAG, "MainActivity handleSignUp")
        auth.createUserWithEmailAndPassword(event.email, event.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MY_TAG, "createUserWithEmail:success ${auth.currentUser?.email}")
                    Toast.makeText(
                        this, "Create user success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    auth.currentUser?.let {
                        createFirestoreDocument(it)
                        viewModel.sendEvent(Event.SetUser(it))
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MY_TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Create user failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun handleLogin(event: PresenterEvent.Login) {
        Log.d(MY_TAG, "MainActivity handleLogin")
        auth.signInWithEmailAndPassword(event.email, event.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MY_TAG, "signInWithEmail:success ${auth.currentUser?.uid}")
                    Toast.makeText(
                        this, "Authentication success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    auth.currentUser?.let { viewModel.sendEvent(Event.SetUser(it)) }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MY_TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun createFirestoreDocument(firebaseUser: FirebaseUser) {
        database.collection(USERS_TABLE_NAME)
            .document(firebaseUser.uid)
            .set(firebaseUser.createUserForDocument())
            .addOnSuccessListener { Log.d(MY_TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e ->
                Log.w(MY_TAG, "Error adding document", e)
            }
    }

    private fun handleAddValveSetting(setting: ValveSetting) {
        val user = Firebase.auth.currentUser ?: return
        database.collection(USERS_TABLE_NAME)
            .document(user.uid)
            .collection(USERS_TABLE_SETTINGS_COLLECTION)
            .add(setting.toFirestoreSetting())
            .addOnSuccessListener { documentReference ->
                Log.d(MY_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(MY_TAG, "Error adding document", e)
            }
    }

    private fun handleGetSettings() {
        Log.d(MY_TAG, "handleGetSettings user ${Firebase.auth.currentUser}")
        val user = Firebase.auth.currentUser ?: return
        Log.d(MY_TAG, "handleGetSettings")
        database.collection(USERS_TABLE_NAME)
            .document(user.uid)
            .collection(USERS_TABLE_SETTINGS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                try {
                    val settings = result.documents.mapNotNull { it.data?.toValveSetting() }
                    viewModel.sendEvent(Event.UpdateValveSettings(settings))
                } catch (e: Exception) {
                    Log.e(MY_TAG, "Error when transform firestore data to ValveSetting", e)
                }
                for (document in result) {
                    Log.d(MY_TAG, "${document.id} => ${document.data}")
                    document.data
                }
            }
            .addOnFailureListener { exception ->
                Log.w(MY_TAG, "Error getting documents.", exception)
            }
    }

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

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d(MY_TAG, "scan result $callbackType address: ${result.device}")
            super.onScanResult(callbackType, result)
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d(MY_TAG, "scan fail errorCode $errorCode")
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        val mBluetoothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
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
            viewModel.sendEvent(Event.SetBluetoothDevices(mScanResults.values.toList()))
            for (entry in mScanResults.entries) {
                Log.d(
                    MY_TAG,
                    "Found device: ${entry.key} ${entry.value.name} ${entry.value.uuids} ${entry.value.type}"
                )
//                logNameAndAppearance(entry.key)
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
            Log.e(MY_TAG, "addScanResult ${result.device.address}")
            val device = result.device
            val deviceAddress = device.address
//            Log.d(MY_TAG, "BLE ScanResult $device ${result.scanRecord?.deviceName}")
            mScanResults[deviceAddress] = device

//            stopScan();
//            BluetoothDevice bluetoothDevice = scanResult.getDevice();
//            connectDevice(bluetoothDevice);
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
}