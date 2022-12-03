package com.example.crudapp.adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapp.AddTask
import com.example.crudapp.R
import com.example.crudapp.model.taskListModel

class TaskListAdapter(taskList: List<taskListModel> , internal var context: Context)
    : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    //start binding view and recycler view
    internal var taskList: List<taskListModel> = ArrayList()
    //bind it with the object task list
    init {
        this.taskList = taskList
    }

    inner class TaskViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name : TextView = view.findViewById(R.id.tasknameTV)
        var detail : TextView = view.findViewById(R.id.taskDetailTV)
        var editButton : Button = view.findViewById(R.id.editTaskBTN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        //method to bind view
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_tasks,parent,false)
        return TaskViewHolder(view)
        //view is bind now
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
       //binding the objects with our data
        val tasks = taskList[position]
        holder.name.text = tasks.name
        holder.detail.text = tasks.details

        holder.editButton.setOnClickListener{
            val intent = Intent(context,AddTask::class.java)
            intent.putExtra("Mode" , "E")
            intent.putExtra("ID" , tasks.ID)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}