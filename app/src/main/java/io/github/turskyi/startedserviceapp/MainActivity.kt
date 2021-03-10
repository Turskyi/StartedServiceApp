package io.github.turskyi.startedserviceapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View?) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DelayedMessageService::class.java)
        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE, resources.getString(R.string.response))
        DelayedMessageService().enqueueWork(this, intent)
    }
}