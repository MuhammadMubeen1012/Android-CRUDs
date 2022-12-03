package com.example.crudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapp.adapter.TaskListAdapter
import com.example.crudapp.database.DatabaseHelper
import com.example.crudapp.model.taskListModel

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var addButton: Button
    var adapter: TaskListAdapter ?=null
    var dbHandler: DatabaseHelper ?=null
    var taskList: List<taskListModel> = ArrayList<taskListModel>()
    var linearLayoutManager: LinearLayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.tasksRV)
        addButton = findViewById(R.id.addTaskBTN)

        dbHandler = DatabaseHelper(this)
        fetchData()

        //add data as we have no data in it
        addButton.setOnClickListener{
            val intent: Intent = Intent(applicationContext,AddTask::class.java)
            startActivity(intent)
        }
    }

    private fun fetchData(){
        taskList = dbHandler!!.getTasks()
        adapter = TaskListAdapter(taskList,applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        adapter?.notifyDataSetChanged()
    }
}