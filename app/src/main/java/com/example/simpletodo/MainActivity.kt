package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.AbsListView
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. remove item from list
                listOfTasks.removeAt(position)
                //2. Notify the adapter something has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }


//        // user clicks Add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // code executed when button clicked
//            Log.i("Caren", "User clicked on button")
//        }

//        listOfTasks.add("Do laundry")
//        listOfTasks.add("Go for a walk")
        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up button and input field so tasks can be added
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // user clicks Add button
        findViewById<Button>(R.id.button).setOnClickListener {
            //grab input text from id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //Add string to listOfTasks
            listOfTasks.add(userInputtedTask)

            // notify the data adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //clear input field
            inputTextField.setText("")

            saveItems()
        }
    }

    //save the data that the user has inputted
    //save data by writing and reading from a file

    //get the file we need
    fun getDataFile() : File {
        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //load items by reading every line in the file
    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //save items by writing to the file
    fun saveItems() {
        try{
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}