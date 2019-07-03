package com.example.testnotifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


        Log.v(TAG, "=======================================")
        val extras = intent.extras
        if (extras != null) {
            val poop = extras.get(Intent.EXTRA_KEY_EVENT)
            // get data via the key
            val value1 = extras.getString(Intent.EXTRA_TEXT)
            if (value1 != null) {
                Log.v(MainActivity.TAG, value1)
            }

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



        bt_Page1.setOnClickListener {
            loadPage2()
        }


    }

    private fun loadPage2() {
        val i = Intent(this@MainActivity, Page2Activity::class.java)
        startActivity(i)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
