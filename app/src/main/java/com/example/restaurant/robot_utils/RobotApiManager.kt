package com.example.restaurant.robot_utils

import android.util.Log
import android.os.RemoteException
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import android.content.Context

import com.ainirobot.coreservice.client.ApiListener
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.StatusListener
import com.ainirobot.coreservice.client.Definition
// import android.os.Handler
// import android.os.Looper
// Handler(Looper.getMainLooper()).postDelayed({
//     RobotApiManager.testRotationCommands()
// }, 2000)

// robot_utils
import com.example.restaurant.robot_utils.BasicMotion
// import com.example.restaurant.robot_utils.MapAndPosition
// import com.example.restaurant.robot_utils.Navigation
// import com.ainirobot.coreservice.client.listener.CommandListener
// import org.json.JSONArray
// import org.json.JSONException
// import org.json.JSONObject
// import com.ainirobot.coreservice.client.listener.ActionListener

object RobotApiManager {
    public fun connectToServer(context: Context, lifecycleOwner: LifecycleOwner) {
        // Attempt to connect to the server and handle the connection status
        try {
            RobotApi.getInstance().connectServer(context, object : ApiListener {
                override fun handleApiDisabled() {
                    // Handle API disabled scenario
                    Log.e("RobotApiManager", "API is disabled")
                }
    
                override fun handleApiConnected() {
                    // Server is connected, set the callback for receiving requests
                    Log.i("RobotApiManager", "API connected successfully")
                    registerStatusListeners(lifecycleOwner)

                    // Start the first rotation
                    BasicMotion.rotateRobotAsync("right", reqId = 1, speed = 10.0F, angle = 30F) 
                    BasicMotion.rotateRobotAsync("left", reqId = 1, speed = 10.0F, angle = 30F) { success ->
                        Log.i("RobotApiManager", "Second rotation completed: $success")
                    }
                    // getPlaceList(reqId = 1)
                    // getMapName(reqId = 1)
                    // getPosition(reqId = 1)
                    // startNavigation(
                    //     reqId = 1,
                    //     destName = "cocina",
                    //     coordinateDeviation = 0.5,
                    //     time = 10000L
                    // )
                }
    
                override fun handleApiDisconnected() {
                    // Handle server disconnection
                    Log.e("RobotApiManager", "API disconnected")
                }
            })
        } catch (e: Exception) {
            // Log the exception to help debug the issue
            Log.e("RobotApiManager", "Failed to connect to server: ${e.message}", e)
        }
    }    

    public fun registerStatusListeners(lifecycleOwner: LifecycleOwner) {
        // Register listeners for various status updates from the robot
        try {
            val statusListener = object : StatusListener() {
                override fun onStatusUpdate(type: String, data: String) {
                    try {
                        when (type) {
                            Definition.STATUS_POSE -> Log.i("StatusListener", "Robot Pose: $data")
                            Definition.STATUS_POSE_ESTIMATE -> Log.i("StatusListener", "Positioning Status: $data") // not loging data!
                            Definition.STATUS_BATTERY -> Log.i("StatusListener", "Battery Info: $data")
                            else -> Log.w("StatusListener", "Unknown status type: $type")
                        }
                    } catch (e: RemoteException) {
                        Log.e("StatusListener", "Error handling status update: ${e.message}", e)
                    }
                }
            }

            // Register listeners for the desired status types
            RobotApi.getInstance().registerStatusListener(Definition.STATUS_POSE, statusListener)
            RobotApi.getInstance().registerStatusListener(Definition.STATUS_POSE_ESTIMATE, statusListener)
            RobotApi.getInstance().registerStatusListener(Definition.STATUS_BATTERY, statusListener)

            // Unregister the listener when no longer needed (e.g., in onDestroy)
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    RobotApi.getInstance().unregisterStatusListener(statusListener)
                    Log.i("RobotApiManager", "StatusListener unregistered")
                    super.onDestroy(owner)
                }
            })
        } catch (e: Exception) {
            Log.e("RobotApiManager", "Failed to register status listeners: ${e.message}", e)
        }
    }

// fun getPlaceList(reqId: Int) {
//     RobotApi.getInstance().getPlaceList(reqId, object : CommandListener() {
//         override fun onResult(result: Int, message: String?) {
//             try {
//                 if (message != null) {
//                     val jsonArray = JSONArray(message)
//                     for (i in 0 until jsonArray.length()) {
//                         val json = jsonArray.getJSONObject(i)
//                         val x = json.getDouble("x")      // x coordinate
//                         val y = json.getDouble("y")      // y coordinate
//                         val theta = json.getDouble("theta") // z coordinate
//                         val name = json.getString("name") // position name
//                         Log.i("TestAndres", "Place: name=$name, x=$x, y=$y, theta=$theta")
//                     }
//                 } else {
//                     Log.e("TestAndres", "Received null message in getPlaceList")
//                 }
//             } catch (e: JSONException) {
//                 Log.e("TestAndres", "JSON parsing error: ${e.message}", e)
//             } catch (e: NullPointerException) {
//                 Log.e("TestAndres", "Null pointer error: ${e.message}", e)
//             }
//         }
//     })
// }

// fun getMapName(reqId: Int) {
//     RobotApi.getInstance().getMapName(reqId, object : CommandListener() {
//         override fun onResult(result: Int, message: String?) {
//             if (!message.isNullOrEmpty()) {
//                 // "message" means map name
//                 val mapName = message
//                 Log.i("TestAndres", "Map name: $mapName")
//             } else {
//                 Log.e("TestAndres", "Map name is null or empty")
//             }
//         }
//     })
// }

// fun getPosition(reqId: Int) {
//     RobotApi.getInstance().getPosition(reqId, object : CommandListener() {
//         override fun onResult(result: Int, message: String?) {
//             try {
//                 if (message != null) {
//                     val json = JSONObject(message)
//                     // x coordinate
//                     val x = json.getDouble(Definition.JSON_NAVI_POSITION_X)
//                     // y coordinate
//                     val y = json.getDouble(Definition.JSON_NAVI_POSITION_Y)
//                     // z coordinate (theta)
//                     val z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA)
//                     Log.i("TestAndres", "Position: x=$x, y=$y, theta=$z")
//                 } else {
//                     Log.e("TestAndres", "Received null message in getPosition")
//                 }
//             } catch (e: JSONException) {
//                 Log.e("TestAndres", "JSON parsing error: ${e.message}", e)
//             } catch (e: NullPointerException) {
//                 Log.e("TestAndres", "Null pointer error: ${e.message}", e)
//             }
//         }
//     })
// }

// fun startNavigation(
//     reqId: Int,
//     destName: String,
//     coordinateDeviation: Double,
//     time: Long
// ) {
//     val navigationListener = object : ActionListener() {
//         override fun onResult(status: Int, response: String?) {
//             when (status) {
//                 Definition.RESULT_OK -> {
//                     if (response == "true") {
//                         Log.i("TestAndres", "Navigation successful")
//                     } else {
//                         Log.e("TestAndres", "Navigation failed")
//                     }
//                 }
//             }
//         }

//         override fun onError(errorCode: Int, errorString: String?) {
//             when (errorCode) {
//                 Definition.ERROR_NOT_ESTIMATE -> Log.e("TestAndres", "Not currently located")
//                 Definition.ERROR_IN_DESTINATION -> Log.e("TestAndres", "Already in destination range")
//                 Definition.ERROR_DESTINATION_NOT_EXIST -> Log.e("TestAndres", "Destination does not exist")
//                 Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE -> Log.e("TestAndres", "Cannot reach destination (obstacle/timeout)")
//                 Definition.ACTION_RESPONSE_ALREADY_RUN -> Log.e("TestAndres", "API already running, stop before calling again")
//                 Definition.ACTION_RESPONSE_REQUEST_RES_ERROR -> Log.e("TestAndres", "Chassis control API already running, stop first")
//                 Definition.ERROR_MULTI_ROBOT_WAITING_TIMEOUT -> Log.e("TestAndres", "Multi-robot waiting timeout")
//                 Definition.ERROR_NAVIGATION_FAILED -> Log.e("TestAndres", "Navigation failed (other problem)")
//                 else -> Log.e("TestAndres", "Navigation error: $errorCode $errorString")
//             }
//         }

//         override fun onStatusUpdate(status: Int, data: String?, extraData: String?) {
//             when (status) {
//                 Definition.STATUS_NAVI_AVOID -> Log.i("TestAndres", "Route blocked by obstacles")
//                 Definition.STATUS_NAVI_AVOID_END -> Log.i("TestAndres", "Obstacle disappeared")
//                 Definition.STATUS_START_NAVIGATION -> Log.i("TestAndres", "Start navigation")
//                 Definition.STATUS_START_CRUISE -> Log.i("TestAndres", "Start cruise")
//                 Definition.STATUS_NAVI_OUT_MAP -> Log.i("TestAndres", "Run out of map")
//                 Definition.STATUS_NAVI_MULTI_ROBOT_WAITING -> Log.i("TestAndres", "Waiting for other robots")
//                 Definition.STATUS_NAVI_MULTI_ROBOT_WAITING_END -> Log.i("TestAndres", "Waiting ended")
//                 Definition.STATUS_NAVI_GO_STRAIGHT -> Log.i("TestAndres", "Go straight")
//                 Definition.STATUS_NAVI_TURN_LEFT -> Log.i("TestAndres", "Turn left")
//                 Definition.STATUS_NAVI_TURN_RIGHT -> Log.i("TestAndres", "Turn right")
//                 else -> Log.i("TestAndres", "Status update: $status $data $extraData")
//             }
//         }
//     }

//     RobotApi.getInstance().startNavigation(
//         reqId,
//         destName,
//         coordinateDeviation,
//         time,
//         navigationListener
//     )
// }

}