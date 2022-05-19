package com.example.greendao.upgrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.greendao.upgrade.databinding.ActivityMainBinding
import com.example.greendao.upgrade.beans.User
import com.example.greendao.upgrade.dao.DbHelper

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        with(viewBinding) {
            addBtn.setOnClickListener {
                doAdd()
            }
            getBtn.setOnClickListener {
                doGet()
            }
        }
    }

    private fun doAdd() {
        val user = User().apply {
            name = "wangpan1"
            age = 18
            email = "p.wang0813@gmail.com"
        }
        DbHelper.get().addUser(user)
        Log.e("tag", "doAdd: $user")
    }

    private fun doGet() {
        val users = DbHelper.get().users
        Log.e("tag", "doGet: $users")
    }

}