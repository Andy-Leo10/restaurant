package com.example.restaurant.robot_utils

import android.util.Log
import kotlinx.coroutines.*
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.CommandListener

object BasicMotion {
    suspend fun rotateRobotSync(direction: String, reqId: Int, speed: Float, angle: Float? = null): Boolean {
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

    fun rotateRobotAsync(
        direction: String,
        reqId: Int,
        speed: Float,
        angle: Float? = null,
        onComplete: (Boolean) -> Unit = {}
    ) {
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
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            Log.e("BasicMotion", "Failed to execute rotation: ${e.message}", e)
            onComplete(false)
        }
    }
}

/* EXAMPLES

- rotateRobotAsync
BasicMotion.rotateRobotAsync("right", reqId = 1, speed = 10.0F, angle = 30F)
BasicMotion.rotateRobotAsync("left", reqId = 1, speed = 10.0F, angle = 30F) { success ->
    Log.i("RobotApiManager", "Rotation completed: $success")
}

- rotateRobotSync
// Start a coroutine to perform multiple rotations in sequence
CoroutineScope(Dispatchers.Main).launch {
    val directions = listOf("right", "left", "right", "left", "right", "left", "right", "left", "right", "left")
    for ((index, direction) in directions.withIndex()) {
        val success = BasicMotion.rotateRobotSync(direction, reqId = index + 1, speed = 10.0F, angle = 15F)
        if (!success) {
            Log.e("RobotApiManager", "Rotation $direction failed at step ${index + 1}")
            break
        }
    }
}

*/