package com.anirudh.websocket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.anirudh.websocket.databinding.ActivityMainBinding
import com.google.gson.Gson

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import javax.net.ssl.SSLSocketFactory
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(){

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.connect.setOnClickListener {
            val room_no = binding.roomno.text
            val user_id = binding.userid.text
            val intent = Intent(this,ChatActivity::class.java)
            intent.apply {
                putExtra("room_no",room_no)
                putExtra("user_id",user_id)
            }
            startActivity(intent)
        }
    }
}