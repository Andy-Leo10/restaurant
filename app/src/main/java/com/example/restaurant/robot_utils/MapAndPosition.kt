package com.example.restaurant.robot_utils

import android.util.Log
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.CommandListener
import com.ainirobot.coreservice.client.Definition
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object MapAndPosition {
    fun getPlaceList(reqId: Int) {
        RobotApi.getInstance().getPlaceList(reqId, object : CommandListener() {
            override fun onResult(result: Int, message: String?) {
                try {
                    if (message != null) {
                        val jsonArray = JSONArray(message)
                        for (i in 0 until jsonArray.length()) {
                            val json = jsonArray.getJSONObject(i)
                            val x = json.getDouble("x")      // x coordinate
                            val y = json.getDouble("y")      // y coordinate
                            val theta = json.getDouble("theta") // z coordinate
                            val name = json.getString("name") // position name
                            Log.i("MapAndPosition", "Place: name=$name, x=$x, y=$y, theta=$theta")
                        }
                    } else {
                        Log.e("MapAndPosition", "Received null message in getPlaceList")
                    }
                } catch (e: JSONException) {
                    Log.e("MapAndPosition", "JSON parsing error: ${e.message}", e)
                } catch (e: NullPointerException) {
                    Log.e("MapAndPosition", "Null pointer error: ${e.message}", e)
                }
            }
        })
    }

    fun getMapName(reqId: Int) {
        RobotApi.getInstance().getMapName(reqId, object : CommandListener() {
            override fun onResult(result: Int, message: String?) {
                if (!message.isNullOrEmpty()) {
                    val mapName = message
                    Log.i("MapAndPosition", "Map name: $mapName")
                } else {
                    Log.e("MapAndPosition", "Map name is null or empty")
                }
            }
        })
    }

    fun getPosition(reqId: Int) {
        RobotApi.getInstance().getPosition(reqId, object : CommandListener() {
            override fun onResult(result: Int, message: String?) {
                try {
                    if (message != null) {
                        val json = JSONObject(message)
                        val x = json.getDouble(Definition.JSON_NAVI_POSITION_X)
                        val y = json.getDouble(Definition.JSON_NAVI_POSITION_Y)
                        val z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA)
                        Log.i("MapAndPosition", "Position: x=$x, y=$y, theta=$z")
                    } else {
                        Log.e("MapAndPosition", "Received null message in getPosition")
                    }
                } catch (e: JSONException) {
                    Log.e("MapAndPosition", "JSON parsing error: ${e.message}", e)
                } catch (e: NullPointerException) {
                    Log.e("MapAndPosition", "Null pointer error: ${e.message}", e)
                }
            }
        })
    }
}