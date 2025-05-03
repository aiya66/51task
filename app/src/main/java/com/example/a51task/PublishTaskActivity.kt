package com.example.a51task

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a51task.R
import kotlinx.coroutines.launch

class PublishTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_task)

        val btnFinish = findViewById<Button>(R.id.btn_edit)
        val btnDelete = findViewById<Button>(R.id.btn_delete)
        btnDelete.visibility = android.view.View.GONE
        btnFinish.text = "完成"
        btnFinish.setOnClickListener {
            val input = findViewById<android.widget.EditText>(R.id.et_task_input).text.toString()
            val title = "任务"
            val desc = input
            val db = AppDatabase.getDatabase(this)
            val taskDao = db.taskDao()
            val timestamp = System.currentTimeMillis()
            val mainScope = kotlinx.coroutines.MainScope()
            mainScope.launch {
                val task = Task(title = title, description = desc, timestamp = timestamp)
                taskDao.insertTask(task)
                val intent = Intent(this@PublishTaskActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
        }
        // 可选：初始化输入框为空
        findViewById<android.widget.EditText>(R.id.et_task_input).setText("")
        findViewById<TextView>(R.id.tv_task_title).text = ""
        findViewById<TextView>(R.id.tv_task_desc).text = ""
    }
}