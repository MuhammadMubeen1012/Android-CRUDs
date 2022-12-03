package com.example.crudapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.crudapp.model.taskListModel

//factory arguement - dont want to handle override members
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null , DB_VERSION) {
    //init variable while creating object of this class
    companion object{
        private val DB_NAME = "Task"
        private val DB_VERSION = 1
        private val TABLE_NAME = "taskList"
        private val ID = "id"
        private val TASK_NAME = "taskName"
        private val TASK_DETAILS = "taskDetails"
    }

    //EXECUTE ON OBJECT CREATION
    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT)"
        p0?.execSQL(CREATE_TABLE)
    }

    //execute on database upgrades in versisons
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    //Now create modal class which hold data, helped in performing CRUD operation

    //Now add functions for crud operation

    @SuppressLint("Range")
    fun getTasks(): List<taskListModel>{
        //list to store data
        val tasksList = ArrayList<taskListModel>()
        //db
        val db = writableDatabase
        //query
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        //curser
        val cursor = db.rawQuery(selectQuery,null)

        if(cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    val tasks = taskListModel()
                    tasks.ID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
                    tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
                    tasksList.add(tasks)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return tasksList
    }

    fun addTask(tasks: taskListModel): Boolean {
        val db = this.writableDatabase
        //we have to take the values
        val values = ContentValues()

        //put values
        values.put(TASK_NAME,tasks.name)
        values.put(TASK_DETAILS,tasks.details)

        //insert into table
        val added: Long = db.insert(TABLE_NAME,null,values)

        db.close()

        return (Integer.parseInt("$added")!=-1)


    }

    @SuppressLint("Range")
    fun getTaskByID(id: Int): taskListModel{
        val tasks = taskListModel()
        val db = writableDatabase
        //select query
        val query = "SELECT * from $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(query,null)

        cursor?.moveToFirst()
        tasks.ID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
        tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
        cursor.close()
        return tasks
    }

    fun deleteTask(id:Int): Boolean{
        val db = this.writableDatabase
        val deleted: Long = db.delete(TABLE_NAME,"$ID = \"=?\"", arrayOf(id.toString())).toLong()
        db.close()
        return (Integer.parseInt("$deleted") != -1 )
    }

    fun updateTask(task: taskListModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(TASK_NAME,task.name)
        values.put(TASK_DETAILS,task.details)

        val updated: Long = db.update(TABLE_NAME,values,"$ID = \"=?\"", arrayOf(task.toString())).toLong()
        db.close()

        return (Integer.parseInt("$updated") != -1 )

    }
}