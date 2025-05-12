package com.example.restaurant.robot_utils

import android.util.Log
import kotlinx.coroutines.*

import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.CommandListener

public fun rotateRobotAsync(direction: String, reqId: Int, speed: Float, angle: Float? = null, onComplete: (Boolean) -> Unit = {}) {
    val rotateListener = object : CommandListener() {
        override fun onResult(result: Int, message: String) {
            if ("succeed".equals(message, ignoreCase = true)) {
                Log.i("BasicMotion", "Rotation $direction succeeded")
                onComplete(true)
            } else {
                Log.e("BasicMotion", "Rotation $direction failed: $message")
                onComplete(false)
            }
        }
    }

    try {
        when (direction) {
            "right" -> {
                if (angle != null) {
                    RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener)
                } else {
                    RobotApi.getInstance().turnLeft(reqId, speed, rotateListener)
                }
            }
            "left" -> {
                if (angle != null) {
                    RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener)
                } else {
                    RobotApi.getInstance().turnRight(reqId, speed, rotateListener)
                }
            }
            else -> {
                Log.e("BasicMotion", "Invalid direction: $direction")
                onComplete(false)
            }
        }
    } catch (e: Exception) {
        Log.e("BasicMotion", "Failed to execute rotation: ${e.message}", e)
        onComplete(false)
    }
}

public suspend fun rotateRobotSync(direction: String, reqId: Int, speed: Float, angle: Float? = null): Boolean {
    return withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            val rotateListener = object : CommandListener() {
                override fun onResult(result: Int, message: String) {
                    if ("succeed".equals(message, ignoreCase = true)) {
                        Log.i("BasicMotion", "Rotation $direction succeeded")
                        continuation.resume(true) {}
                    } else {
                        Log.e("BasicMotion", "Rotation $direction failed: $message")
                        continuation.resume(false) {}
                    }
                }
            }

            try {
                when (direction) {
                    "left" -> {
                        if (angle != null) {
                            RobotApi.getInstance().turnLeft(reqId, speed, angle, rotateListener)
                        } else {
                            RobotApi.getInstance().turnLeft(reqId, speed, rotateListener)
                        }
                    }
                    "right" -> {
                        if (angle != null) {
                            RobotApi.getInstance().turnRight(reqId, speed, angle, rotateListener)
                        } else {
                            RobotApi.getInstance().turnRight(reqId, speed, rotateListener)
                        }
                    }
                    else -> {
                        Log.e("BasicMotion", "Invalid direction: $direction")
                        continuation.resume(false) {}
                    }
                }
            } catch (e: Exception) {
                Log.e("BasicMotion", "Failed to execute rotation: ${e.message}", e)
                continuation.resume(false) {}
            }
        }
    }
}