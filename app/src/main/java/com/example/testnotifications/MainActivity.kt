package com.example.testnotifications

import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    private var MY_TOKEN: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get token
        retrieveMyToken()

        // TOKEN BUTTON
        logTokenButton.setOnClickListener {

            if(MY_TOKEN == "")
                retrieveMyToken()

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, MY_TOKEN)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }

        // CATCH THE NOTIFICATION THAT OPENED THE APP
        Log.v(TAG, "=======================================")
        val extras = intent.extras
        if (extras != null) {
            val poop = extras.get(Intent.EXTRA_KEY_EVENT)
            // get data via the key
            val value1 = extras.getString(Intent.EXTRA_TEXT)
            if (value1 != null) {
                Log.v(TAG, value1)
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

        // Set language
        /*var currentLanguage = getLanguageFromApi()
        if(currentLanguage != "English"){
            rg_langues.check(R.id.rb_french)
        }*/

        // LANGUAGE RADIO-BUTTONS
        rg_langues.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)

                // Tell API that the language has changed
                // TODO

                // Subscribe to the appropriate Topic
                subscribeToTopic(getTopicName(rg_langues.checkedRadioButtonId), getOldTopicName(rg_langues.checkedRadioButtonId))

                // Make me a toast!
                Toast.makeText(applicationContext," On checked change : ${radio.text}",
                    Toast.LENGTH_SHORT).show()
            })
    }

    private fun retrieveMyToken(){
        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                MY_TOKEN = task.result?.token
            })
        // [END retrieve_current_token]
    }

    private fun subscribeToTopic(newTopic:String, oldTopic:String) {
        val myUrl = "http://192.168.6.120:8080/notification/subscribeToTopic/"
        val result: String
        val getRequest = HttpPostRequest()
        result = getRequest.execute(myUrl, "deviceToken=$MY_TOKEN&newTopic=$newTopic&oldTopic=$oldTopic").get()
        Log.d(TAG, "HTTP request= $result")
    }

    private fun getTopicName(radioButtonId: Int):String{
        var result = ""
        when(radioButtonId){
            rb_English.id -> result = TOPIC_ENGLISH
            rb_french.id -> result = TOPIC_FRENCH
        }
        return result
    }

    private fun getOldTopicName(radioButtonId: Int):String{
        var result = ""
        when(radioButtonId){
            rb_English.id -> result = TOPIC_FRENCH
            rb_french.id -> result = TOPIC_ENGLISH
        }
        return result
    }

    private fun loadPage2() {
        val i = Intent(this@MainActivity, Page2Activity::class.java)
        startActivity(i)
        finish()
    }

    private fun getLanguageFromApi():String{
        return ""
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
        result = getRequest.execute(myUrl, "recipientId=$MY_ID").get()
        Log.d(TAG, "HTTP request= $result")
        Toast.makeText(baseContext, result, Toast.LENGTH_SHORT).show()
    }

    private fun changeLanguage(){
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
        private const val MY_ID = "3fb9c8d8-c703-4bd5-97bd-a0a117414f7b"
        private const val REDMI_TOKEN = "cC6htAMkqAc:APA91bFcTW_fN1d8WATnrAGMyjBcEyO8owMsd802JDRV0WBq0pJmFsTVAysr7A4nk4lSdwir2qcaCXtpoJjH1cQkgPS4FxniK47GOWGyqY3MiTnHCqB4S1ws1Ve5u2312iL6fkbvWVhO"
        private const val TOPIC_ENGLISH = "TopicEnglish"
        private const val TOPIC_FRENCH = "TopicFrench"
    }
}
