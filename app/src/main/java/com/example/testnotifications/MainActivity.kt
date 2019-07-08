package com.example.testnotifications

import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TOKEN BUTTON
        logTokenButton.setOnClickListener {
            // Get token
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
            // [END retrieve_current_token]
        }

        // CATCH THE NOTIFICATION THAT OPENED THE APP
        Log.v(TAG, "=======================================")
        val extras = intent.extras
        if (extras != null) {
            val poop = extras.get(Intent.EXTRA_KEY_EVENT)
            // get data via the key
            val value1 = extras.getString(Intent.EXTRA_TEXT)
            if (value1 != null) {
                Log.v(MainActivity.TAG, value1)
            }

            // List all data
            if (extras != null) {
                for (key in extras.keySet()) {
                    val value = extras.get(key)
                    Log.d("Activity onResume", "Key: $key Value: $value")
                }
            }

            // THIS WORKS!
            if(extras.get("OpenPage") != null){
                loadPage2()
            }
        }
        Log.v(TAG, "=======================================")


        // PAGE2 BUTTON
        bt_Page1.setOnClickListener {
            loadPage2()
        }

        // LANGUAGE RADIO-BUTTONS



    }

    private fun loadPage2() {
        val i = Intent(this@MainActivity, Page2Activity::class.java)
        startActivity(i)
        finish()
    }


    // Send notification to itself
    fun sendNotificationToMe(v: View){
        //Some url endpoint that you may have
        val myUrl = "http://192.168.6.120:8080/notification/send/user/"
        //String to place our result in
        val result: String
        //Instantiate new instance of our class
        val getRequest = HttpPostRequest()
        //Perform the doInBackground method, passing in our url
        result = getRequest.execute(myUrl, "recipientId=3fb9c8d8-c703-4bd5-97bd-a0a117414f7b").get()
        Log.d(TAG, "HTTP request= $result")
        Toast.makeText(baseContext, result, Toast.LENGTH_SHORT).show()
    }

    // Send Notification to Topic
    fun sendNotificationToTopic(v:View){

    }

    private fun getDataFromApi(){

        // Create URL
        val githubEndpoint = URL("http://192.168.6.120:8080/notification/testGet/")
        // Create connection
        val myConnection = githubEndpoint.openConnection() as HttpsURLConnection
        myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1")

        if (myConnection.getResponseCode() == 200) {
            // Success
            val responseBody = myConnection.inputStream
            val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
            val jsonReader = JsonReader(responseBodyReader)
            jsonReader.beginObject() // Start processing the JSON object

            Log.e(TAG, "--------------[ GET ]------------")
            while (jsonReader.hasNext()) { // Loop through all keys
                val key = jsonReader.nextName() // Fetch the next key
                if (key == "organization_url") { // Check if desired key
                    // Fetch the value as a String
                    val value = jsonReader.nextString()

                    // Do something with the value
                    Log.e(TAG, value)

                    break // Break out of the loop
                } else {
                    jsonReader.skipValue() // Skip values of other keys
                }
            }
            Log.e(TAG, "--------------[ GET ]------------")
            jsonReader.close()
            myConnection.disconnect()
        } else {
            // Error handling code goes here
            Log.e(TAG, "Get request failed!")
        }
    }

    // GET data from API
    fun testGetFromApi(v: View){
        //Some url endpoint that you may have
        val myUrl = "http://192.168.6.120:8080/notification/testGet/"
        //String to place our result in
        val result: String
        //Instantiate new instance of our class
        val getRequest = HttpGetRequest()
        //Perform the doInBackground method, passing in our url
        result = getRequest.execute(myUrl).get()
        Log.d(TAG, "HTTP request= $result")
        Toast.makeText(baseContext, result, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val TAG = "MainActivity"
        private const val REDMI_TOKEN = "cC6htAMkqAc:APA91bFcTW_fN1d8WATnrAGMyjBcEyO8owMsd802JDRV0WBq0pJmFsTVAysr7A4nk4lSdwir2qcaCXtpoJjH1cQkgPS4FxniK47GOWGyqY3MiTnHCqB4S1ws1Ve5u2312iL6fkbvWVhO"
    }
}
