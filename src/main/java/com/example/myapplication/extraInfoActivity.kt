package com.example.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class extraInfoActivity : AppCompatActivity() {

    var extraView : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_info)

        extraView = findViewById<LinearLayout>(R.id.extraView)
        val intent : Intent = getIntent()
        val text : String? = intent.getStringExtra("text")
        val extraText : String? = intent.getStringExtra("extraText")
        val carImage : String? = intent.getStringExtra("carImage")
        val url = imageUrl + carImage

        // Method to save an bitmap to a file
        fun bitmapToFile(bitmap:Bitmap): Uri {
            // Get the context wrapper
            val wrapper = ContextWrapper(applicationContext)

            // Initialize a new file instance to save bitmap object
            var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
            file = File(file,"${UUID.randomUUID()}.jpg")

            try{
                // Compress the bitmap and save in jpg format
                val stream:OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
                stream.flush()
                stream.close()
            }catch (e:IOException){
                e.printStackTrace()
            }

            // Return the saved bitmap uri
            return Uri.parse(file.absolutePath)
        }

        createViewDynamically(text!!, extraText!!)

        class BaseAsyncTask : AsyncTask<String?, Void, String?>() {
            override fun doInBackground(str: Array<String?>): String? {
                val url = URL(str[0])
                val connection: HttpURLConnection = url
                    .openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()

                val bitmap : Bitmap = BitmapFactory.decodeStream(input)
                val uri = bitmapToFile(bitmap)

                return uri.toString()
            }

            override fun onPostExecute(response: String?) {
                createViewDynamically(response!!)
            }
        }
        BaseAsyncTask().execute(url)
    }

    fun createViewDynamically(text : String, extraText : String = "") {
        if (extraText != "") {
            val dynamicTW = TextView(this)
            dynamicTW.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dynamicTW.setText(text + "\n" + extraText)

            val size: Float = 20f
            dynamicTW.textSize = size
            dynamicTW.setBackgroundColor(Color.rgb(40, 24, 177))
            dynamicTW.setTextColor(Color.rgb(255, 199, 0))
            dynamicTW.setPadding(5, 5, 20, 5)
            extraView!!.addView(dynamicTW)
        } else {
            val dynamicIW = ImageView(this)
            dynamicIW.setImageURI(text.toUri())
            extraView!!.addView(dynamicIW)
        }
    }
}
