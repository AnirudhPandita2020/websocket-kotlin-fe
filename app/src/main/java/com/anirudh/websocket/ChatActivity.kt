package com.anirudh.websocket


import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebBackForwardList
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.rotationMatrix
import androidx.core.widget.addTextChangedListener

import androidx.recyclerview.widget.LinearLayoutManager
import com.anirudh.websocket.databinding.ActivityChatBinding
import com.anirudh.websocket.dataclass.Message

import com.anirudh.websocket.dataclass.MessageList

import com.anirudh.websocket.websocket.MessageListener
import com.anirudh.websocket.websocket.WebSocketManager
import com.google.gson.Gson
import kotlin.concurrent.thread


class ChatActivity : AppCompatActivity() ,MessageListener{
    private var checkformessage = false
    private val list = MessageList()
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding :ActivityChatBinding
    private lateinit var bundle: Bundle
    private lateinit var room_no :String
    private lateinit var user_id:String
    private var url  = "wss://websocket-fastapi-test.herokuapp.com/ws/"
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bundle = intent.extras!!
        room_no = bundle.get("room_no").toString()
        user_id = bundle.get("user_id").toString()
        url+= "$room_no/$user_id"
        WebSocketManager.init(url,this)
        connection()
        binding.messageList.layoutManager = LinearLayoutManager(this)
        binding.sendbtn.setOnClickListener {
            val message = Message(binding.messagebox.text.toString(),room_no,user_id)
            val req = Gson().toJson(message)
            WebSocketManager.sendMessage(req)
            binding.messagebox.setText("")

        }
        binding.messagebox.setOnClickListener {
            binding.messagebox.focusable = View.FOCUSABLE
        }
        binding.messagebox.addTextChangedListener {
            val is_empty = it.isNullOrEmpty()

            if (WebSocketManager.isConnect() && !is_empty) {
                val message = Gson().toJson(Message("Typing", room_no, user_id))
                WebSocketManager.sendMessage(message)
            }
            else{
                val message = Gson().toJson(Message("websocket",room_no,user_id))
                WebSocketManager.sendMessage(message)

            }
        }


    }
    private fun connection(){
        thread {
            kotlin.run {
                WebSocketManager.connect()
            }
        }
    }

    override fun onConnectSuccess() {
       Log.i("TAG","")
    }

    override fun onConnectFailed() {
        Log.i("TAG","")
    }

    override fun onClose() {
        Log.i("TAG","")
    }

    override fun onMessage(text: String?) {
       Log.i("TAG",text as String)
        val message = Gson().fromJson(text,Message::class.java)
        runOnUiThread {
            if(message.message != "Typing" && message.message != "websocket") {
                setTitle("websocket")
                list.add(message)
                binding.messageList.adapter = MessageAdapter(list)
                binding.messageList.scrollToPosition(list.lastIndex)
            }
            else if (message.user_id !=  user_id && message.message == "Typing"){
                setTitle("${message.user_id} is typing...")
            }
            else if(message.message == "websocket"){
                setTitle("websocket")
            }

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
    }
}