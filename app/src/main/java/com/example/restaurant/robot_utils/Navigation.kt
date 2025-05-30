package com.example.restaurant.robot_utils

import android.util.Log
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.ActionListener
import com.ainirobot.coreservice.client.Definition

object Navigation {
    fun startNavigation(
        reqId: Int,
        destName: String,
        coordinateDeviation: Double,
        time: Long
    ) {
        val navigationListener = object : ActionListener() {
            override fun onResult(status: Int, response: String?) {
                when (status) {
                    Definition.RESULT_OK -> {
                        if (response == "true") {
                            Log.i("Navigation", "Navigation successful")
                        } else {
                            Log.e("Navigation", "Navigation failed")
                        }
                    }
                }
            }

            override fun onError(errorCode: Int, errorString: String?) {
                when (errorCode) {
                    Definition.ERROR_NOT_ESTIMATE -> Log.e("Navigation", "Not currently located")
                    Definition.ERROR_IN_DESTINATION -> Log.e("Navigation", "Already in destination range")
                    Definition.ERROR_DESTINATION_NOT_EXIST -> Log.e("Navigation", "Destination does not exist")
                    Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE -> Log.e("Navigation", "Cannot reach destination (obstacle/timeout)")
                    Definition.ACTION_RESPONSE_ALREADY_RUN -> Log.e("Navigation", "API already running, stop before calling again")
                    Definition.ACTION_RESPONSE_REQUEST_RES_ERROR -> Log.e("Navigation", "Chassis control API already running, stop first")
                    Definition.ERROR_MULTI_ROBOT_WAITING_TIMEOUT -> Log.e("Navigation", "Multi-robot waiting timeout")
                    Definition.ERROR_NAVIGATION_FAILED -> Log.e("Navigation", "Navigation failed (other problem)")
                    else -> Log.e("Navigation", "Navigation error: $errorCode $errorString")
                }
            }

            override fun onStatusUpdate(status: Int, data: String?, extraData: String?) {
                when (status) {
                    Definition.STATUS_NAVI_AVOID -> Log.i("Navigation", "Route blocked by obstacles")
                    Definition.STATUS_NAVI_AVOID_END -> Log.i("Navigation", "Obstacle disappeared")
                    Definition.STATUS_START_NAVIGATION -> Log.i("Navigation", "Start navigation")
                    Definition.STATUS_START_CRUISE -> Log.i("Navigation", "Start cruise")
                    Definition.STATUS_NAVI_OUT_MAP -> Log.i("Navigation", "Run out of map")
                    Definition.STATUS_NAVI_MULTI_ROBOT_WAITING -> Log.i("Navigation", "Waiting for other robots")
                    Definition.STATUS_NAVI_MULTI_ROBOT_WAITING_END -> Log.i("Navigation", "Waiting ended")
                    Definition.STATUS_NAVI_GO_STRAIGHT -> Log.i("Navigation", "Go straight")
                    Definition.STATUS_NAVI_TURN_LEFT -> Log.i("Navigation", "Turn left")
                    Definition.STATUS_NAVI_TURN_RIGHT -> Log.i("Navigation", "Turn right")
                    else -> Log.i("Navigation", "Status update: $status $data $extraData")
                }
            }
        }

        RobotApi.getInstance().startNavigation(
            reqId,
            destName,
            coordinateDeviation,
            time,
            navigationListener
        )
    }
}