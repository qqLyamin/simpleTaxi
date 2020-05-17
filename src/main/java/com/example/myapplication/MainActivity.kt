package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import utils.rideInfo
import java.net.HttpURLConnection
import java.net.URL

fun getResponseFromURL(url: URL?) : String {
    var urlConnection = url!!.openConnection() as HttpURLConnection
    try {
        val data = urlConnection.inputStream.bufferedReader().readText()
        return data
    } finally {
        urlConnection.disconnect()
    }
}

val BASE_URL = "https://www.roxiemobile.ru/careers/test/"
val mainUrl = URL("${BASE_URL}orders.json")
val imageUrl = "${BASE_URL}images/"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var mainList : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainList = findViewById<LinearLayout>(R.id.mainList)

        var rides = mutableListOf<rideInfo>()

        fun openExtraInfoActivity(text : String, extraText : String, carImage : String) {
            val intent = Intent(this, extraInfoActivity :: class.java)
            intent.putExtra("text", text)
            intent.putExtra("extraText", extraText)
            intent.putExtra("carImage", carImage)
            startActivity(intent)
        }

        fun createButtonDynamically(text : String, extraText : String, carImage : String) {
            val dynamicButton = Button(this, null, R.style.ButtonHOLO)
            dynamicButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dynamicButton.text = text

            val size : Float = 20f
            dynamicButton.textSize = size
            dynamicButton.setOnClickListener {
                openExtraInfoActivity(text, extraText, carImage)
            }
            dynamicButton.setBackgroundColor(Color.rgb(40, 24, 177))
            dynamicButton.setTextColor(Color.rgb(255, 199, 0))
            dynamicButton.setPadding(5,5,20,5)

            mainList!!.addView(dynamicButton)
        }

        class BaseAsyncTask : AsyncTask<URL, Void, String?>() {
            override fun doInBackground(urls: Array<URL?>): String? {
                val response = getResponseFromURL(urls[0])
                return response;
            }

            override fun onPostExecute(response: String?) {
                val jsonArray : JSONArray = JSONArray(response)                                                                     // ALL ELEMENTS OF JSON ARRAY
                for (n in 0 until jsonArray.length()) {
                    var rideInfo : rideInfo = rideInfo()

                    val jsonInfo : JSONObject = jsonArray.getJSONObject(n)                                                          // CURRENT ELEMENT

                    val jsonStartAddress: JSONObject = jsonInfo.getJSONObject("startAddress")                                // CURRENT ELEMENT START ADDRESS OBJECT
                    val jsonEndAddress: JSONObject = jsonInfo.getJSONObject("endAddress")                                    // CURRENT ELEMENT END ADDRESS OBJECT
                    val jsonPrice: JSONObject = jsonInfo.getJSONObject("price")                                              // CURRENT ELEMENT PRICE OBJECT
                    val jsonVehicle: JSONObject = jsonInfo.getJSONObject("vehicle")                                          // CURRENT ELEMENT VEHICLE OBJECT

                    /* OBJECT INIT BY JSON */
                    rideInfo.setRideId(jsonInfo.getInt("id"))
                    rideInfo.setStartAddress(jsonStartAddress.getString("city"), jsonStartAddress.getString("address"))
                    rideInfo.setEndAddress(jsonEndAddress.getString("city"), jsonEndAddress.getString("address"))
                    rideInfo.setPriceAmountAndCurrency(jsonPrice.getInt("amount"), jsonPrice.getString("currency"))
                    rideInfo.setRideOrderTime(jsonInfo.getString("orderTime"))
                    rideInfo.setRideVehicleInfo(jsonVehicle.getString("regNumber"), jsonVehicle.getString("modelName"),
                                                jsonVehicle.getString("photo"), jsonVehicle.getString("driverName"))

                    /* PUSHING THIS CURRENT RIDE TO ALL RIDES CONTAINER */
                    rides.add(rideInfo)
                }

                rides.sortByDescending { it.orderTime }

                for (numberOfRide in 0 until rides.size) {
                    var text : String = ""
                    text += "Адрес начала поездки : " + rides[numberOfRide].startAddressCity + "   "
                    text += rides[numberOfRide].startAddressAddress
                    text += "\n"
                    text += "Адрес окончания поездки : " + rides[numberOfRide].endAddressCity + "   "
                    text += rides[numberOfRide].endAddressAddress
                    text += "\n"
                    text += "Дата поездки : " + rides[numberOfRide].orderTime.dropLast(15)
                    text += "\n"
                    text += "Сумма к оплате : " + (rides[numberOfRide].priceRideAmount / 100 ).toString() + " " + rides[numberOfRide].priceRideCurrency + " " +
                                               (if((rides[numberOfRide].priceRideAmount % 100 ) == 0) "00" else (rides[numberOfRide].priceRideAmount % 100 )).toString() + " КОП"

                    var extraText : String = ""
                    extraText +=  "Марка Автомобиля : " + rides[numberOfRide].modelName + "\n" +
                                  "ФИО водителя : " + rides[numberOfRide].driverName + "\n" +
                                  "Регистрационный номер : " + rides[numberOfRide].regNumber  + "\n" +
                                  "Время начала поездки : " + rides[numberOfRide].orderTime.dropWhile { it != 'T' }


                    var carImage : String = rides[numberOfRide].photo
                    createButtonDynamically(text, extraText, carImage)
                }
            }
        }

        BaseAsyncTask().execute(mainUrl)
    }
}
