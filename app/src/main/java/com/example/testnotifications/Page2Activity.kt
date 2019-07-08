package com.example.testnotifications

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_page2.*
import kotlinx.android.synthetic.main.content_page2.*


class Page2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page2)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        bt_Page2.setOnClickListener {
            val i = Intent(this@Page2Activity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

}
