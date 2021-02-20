package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.Adapter.TaskListAdapter;
import com.example.todolist.Model.TaskListModel;
import com.example.todolist.Utilities.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private DataBaseHandler db;

    private RecyclerView tasksRecyclerView;
    private TaskListAdapter taskAdapter;
    private FloatingActionButton button;

    private List<TaskListModel> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DataBaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tastRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskListAdapter(db, MainActivity.this);
        tasksRecyclerView.setAdapter(taskAdapter);

        button = findViewById(R.id.actionButton);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouch(taskAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        taskAdapter.setTask(taskList);

        button.setOnClickListener((View v) -> {
            AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }

}