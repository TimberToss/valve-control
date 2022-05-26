package com.example.valvecontrol.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess.appearance
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess.deviceName
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess.readAppearance
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess.readDeviceName
import com.beepiz.bluetooth.gattcoroutines.ExperimentalBleGattCoroutinesCoroutinesApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import com.example.valvecontrol.data.model.ValveSetting
import com.example.valvecontrol.data.provider.ValveAppDataStorePrefKeys.DATA_STORE_NAME
import com.example.valvecontrol.ui.Constants.PERMISSIONS_API_29
import com.example.valvecontrol.ui.Constants.PERMISSIONS_API_31
import com.example.valvecontrol.ui.Constants.PERMISSIONS_OLD_API
import com.example.valvecontrol.ui.Constants.SETTINGS_TABLE_NAME_FIELD
import com.example.valvecontrol.ui.Constants.SETTINGS_TABLE_SEGMENT1_FIELD
import com.example.valvecontrol.ui.Constants.SETTINGS_TABLE_SEGMENT2_FIELD
import com.example.valvecontrol.ui.Constants.SETTINGS_TABLE_SEGMENT3_FIELD
import com.example.valvecontrol.ui.Constants.SETTINGS_TABLE_SEGMENT4_FIELD
import com.example.valvecontrol.ui.Constants.USERS_TABLE_EMAIL_FIELD
import com.example.valvecontrol.ui.main.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import splitties.bitflags.hasFlag
import splitties.systemservices.bluetoothManager
import java.util.*

const val MY_TAG = "MY_TAG"

fun isApi(apiNumber: Int) = Build.VERSION.SDK_INT >= apiNumber

fun resolveNeededPermissions() = when {
    isApi(Build.VERSION_CODES.S) -> PERMISSIONS_API_31
    isApi(Build.VERSION_CODES.Q) -> PERMISSIONS_API_29
    else -> PERMISSIONS_OLD_API
}

fun <T> mutableSharedFlow() =
    MutableSharedFlow<T>(extraBufferCapacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)

inline var <reified T> MutableSharedFlow<T>.value
    get() = replayCache.firstOrNull()
    set(value) {
        tryEmit(value ?: throw IllegalStateException("Try to emit null"))
    }

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

inline fun <reified T> Flow<T>.observe(
    scope: CoroutineScope,
    noinline action: suspend (T) -> Unit
) = onEach { action(it) }.launchIn(scope)

inline fun <reified T> CoroutineScope.observe(
    sharedFlow: SharedFlow<T>,
    crossinline action: suspend (T) -> Unit
) = sharedFlow.observe(this) { action(it) }

fun FirebaseUser.createUserForDocument() = hashMapOf(
    USERS_TABLE_EMAIL_FIELD to email
)

fun ValveSetting.toFirestoreSetting() = hashMapOf(
    SETTINGS_TABLE_NAME_FIELD to name,
    SETTINGS_TABLE_SEGMENT1_FIELD to segment1,
    SETTINGS_TABLE_SEGMENT2_FIELD to segment2,
    SETTINGS_TABLE_SEGMENT3_FIELD to segment3,
    SETTINGS_TABLE_SEGMENT4_FIELD to segment4
)

fun Map<String, Any>.toValveSetting() = ValveSetting(
    name = this[SETTINGS_TABLE_NAME_FIELD] as String,
    segment1 = (this[SETTINGS_TABLE_SEGMENT1_FIELD] as Long).toInt(),
    segment2 = (this[SETTINGS_TABLE_SEGMENT2_FIELD] as Long).toInt(),
    segment3 = (this[SETTINGS_TABLE_SEGMENT3_FIELD] as Long).toInt(),
    segment4 = (this[SETTINGS_TABLE_SEGMENT4_FIELD] as Long).toInt()
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Context.checkAllPermissions(multiplePermissionsState: MultiplePermissionsState) {
    if (multiplePermissionsState.allPermissionsGranted) {
        // If all permissions are granted, then show screen with the feature enabled
        Log.d(MY_TAG, "All permission is granted")
        Toast.makeText(
            this,
            "All permissions Granted! Thank you!",
            Toast.LENGTH_SHORT
        ).show()
    } else {
        val text = getTextToShowGivenPermissions(
            multiplePermissionsState.revokedPermissions,
            multiplePermissionsState.shouldShowRationale
        )
        Log.d(MY_TAG, "Not all permission is granted $text")
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }
            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }
            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}

private val mySecondPhone = "D0:28:BA:18:4B:6C"
val defaultDeviceMacAddress = mySecondPhone

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
    } catch (e: Exception) {
        Log.e(tag, "logGattServices: ${e.message}", e)
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
        val properties = BluetoothGattCharacteristic.PROPERTY_WRITE
        val instanceId = 35
        val permissions = BluetoothGattCharacteristic.PERMISSION_WRITE
        val characteristic = BluetoothGattCharacteristic(uuid, properties, permissions).also {
            it.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            it.setValue(7_777_777, BluetoothGattCharacteristic.FORMAT_SINT32, 0)
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
        Log.e(MY_TAG, "Connection timed out after $connectionTimeoutInMillis milliseconds!")
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

private val BluetoothGattService.typeString: String
    get() = when (type) {
        BluetoothGattService.SERVICE_TYPE_PRIMARY -> "PRIMARY"
        BluetoothGattService.SERVICE_TYPE_SECONDARY -> "SECONDARY"
        else -> "UNKNOWN"
    }

private val BluetoothGattCharacteristic.writeTypeString: String
    get() = when (writeType) {
        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT -> "DEFAULT"
        BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE -> "NO_RESPONSE"
        BluetoothGattCharacteristic.WRITE_TYPE_SIGNED -> "SIGNED"
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
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_READ)) append("READ, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_WRITE)) append("WRITE, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) append("WRITE_NO_RESPONSE, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE)) append("SIGNED_WRITE, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_INDICATE)) append("INDICATE, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_NOTIFY)) append("NOTIFY, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_BROADCAST)) append("BROADCAST, ")
    if (properties.hasFlag(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS)) append("EXTENDED_PROPS, ")
}.toString()