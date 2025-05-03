package com.example.a51task

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    
        val db = AppDatabase.getDatabase(this)
        val taskDao = db.taskDao()
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val layout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
    
        // 协程作用域
        val mainScope = kotlinx.coroutines.MainScope()
        var taskList: List<Task> = emptyList()
        lateinit var adapter: TaskAdapter
    
        fun refreshTasks() {
            mainScope.launch {
                taskList = taskDao.getAllTasks()
                adapter = TaskAdapter(taskList,
                    onEdit = { task -> showEditTaskDialog(task, taskDao, ::refreshTasks) },
                    onDelete = { task -> mainScope.launch { taskDao.deleteTask(task); refreshTasks() } }
                )
                recyclerView.adapter = adapter
            }
        }
        refreshTasks()
    
        // 添加任务按钮
        val fab = com.google.android.material.floatingactionbutton.FloatingActionButton(this)
        fab.setImageResource(android.R.drawable.ic_input_add)
        val params = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT,
            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        params.setMargins(0,0,32,32)
        fab.layoutParams = params
        layout.addView(fab)
    
        fab.setOnClickListener {
            showAddTaskDialog(taskDao, ::refreshTasks)
        }
        val btnPublish = findViewById<android.widget.Button>(R.id.btn_publish)
        btnPublish.setOnClickListener {
            val intent = android.content.Intent(this, PublishTaskActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun showAddTaskDialog(taskDao: TaskDao, refresh: () -> Unit) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("添加任务")
        val view = layoutInflater.inflate(R.layout.item_task, null)
        val etTitle = view.findViewById<android.widget.TextView>(R.id.tv_task_title)
        val etDesc = view.findViewById<android.widget.TextView>(R.id.tv_task_desc)
        view.findViewById<android.widget.LinearLayout>(R.id.btn_edit).visibility = android.view.View.GONE
        view.findViewById<android.widget.LinearLayout>(R.id.btn_delete).visibility = android.view.View.GONE
        builder.setView(view)
        builder.setPositiveButton("保存") { _, _ ->
            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()
            if (title.isNotBlank()) {
                kotlinx.coroutines.MainScope().launch {
                    taskDao.insertTask(Task(title = title, description = desc, timestamp = System.currentTimeMillis()))
                    refresh()
                }
            }
        }
        builder.setNegativeButton("取消", null)
        builder.show()
    }
    
    private fun showEditTaskDialog(task: Task, taskDao: TaskDao, refresh: () -> Unit) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("编辑任务")
        val view = layoutInflater.inflate(R.layout.item_task, null)
        val etTitle = view.findViewById<android.widget.TextView>(R.id.tv_task_title)
        val etDesc = view.findViewById<android.widget.TextView>(R.id.tv_task_desc)
        etTitle.text = task.title
        etDesc.text = task.description
        view.findViewById<android.widget.LinearLayout>(R.id.btn_edit).visibility = android.view.View.GONE
        view.findViewById<android.widget.LinearLayout>(R.id.btn_delete).visibility = android.view.View.GONE
        builder.setView(view)
        builder.setPositiveButton("保存") { _, _ ->
            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()
            if (title.isNotBlank()) {
                kotlinx.coroutines.MainScope().launch {
                    taskDao.updateTask(task.copy(title = title, description = desc))
                    refresh()
                }
            }
        }
        builder.setNegativeButton("取消", null)
        builder.show()
    }
}
