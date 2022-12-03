package com.example.crudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.crudapp.database.DatabaseHelper
import com.example.crudapp.model.taskListModel

class AddTask : AppCompatActivity() {

    lateinit var addTaskBTN : Button
    lateinit var deleteTaskBTN : Button
    lateinit var nameET: EditText
    lateinit var detailET: EditText
    var dbHelper: DatabaseHelper ?= null
    var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        addTaskBTN = findViewById(R.id.addButton)
        deleteTaskBTN = findViewById(R.id.deleteButton)
        nameET = findViewById(R.id.taskNameET)
        detailET = findViewById(R.id.taskDetailET)

        dbHelper = DatabaseHelper(this)

        if(intent!=null && intent.getStringExtra("Mode") == "E"){
            //do open edit mode
            isEditMode = true
            addTaskBTN.text = "Update"
            deleteTaskBTN.visibility = View.VISIBLE

            val task: taskListModel = dbHelper!!.getTaskByID(intent.getIntExtra("ID" , 0))
            nameET.setText(task.name)
            detailET.setText(task.details)

        } else{
            isEditMode = false
            addTaskBTN.text = "Save"
            deleteTaskBTN.visibility = View.GONE
        }

        addTaskBTN.setOnClickListener{
            var success: Boolean = false
            val tasks: taskListModel = taskListModel()

            if(isEditMode){
                //do update

                tasks.ID = intent.getIntExtra("ID",0)
                tasks.name = nameET.text.toString()
                tasks.details = detailET.text.toString()

                success = dbHelper?.updateTask(tasks) as Boolean
            } else{
                //do insert

                tasks.name = nameET.text.toString()
                tasks.details = detailET.text.toString()

                success = dbHelper?.addTask(tasks) as Boolean
            }

            if(success){
                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went wrong" , Toast.LENGTH_LONG).show()
            }
        }

        deleteTaskBTN.setOnClickListener{
            val dialog = AlertDialog.Builder(this).setTitle("Info")
                .setMessage("Are you want to delete it?")
                .setPositiveButton("Yes" , {dialog , i ->
                    val success = dbHelper?.deleteTask(intent.getIntExtra("ID" , 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                })
                .setNegativeButton("No" , {dialog , i->
                    dialog.dismiss()
                })
            dialog.show()
        }
    }
}