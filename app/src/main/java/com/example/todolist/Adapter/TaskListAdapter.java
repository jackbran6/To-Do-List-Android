package com.example.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.TaskListModel;
import com.example.todolist.R;
import com.example.todolist.Utilities.DataBaseHandler;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<TaskListModel> taskList;
    private MainActivity activity;
    private DataBaseHandler db;

    public TaskListAdapter(DataBaseHandler db, MainActivity activity){
        this.activity = activity;
        this.db = db;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        db.openDatabase();

        final TaskListModel item = taskList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    @Override
    public int getItemCount(){
        return taskList.size();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        TaskListModel item = taskList.get(position);
        db.deleteTask(item.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTask(List<TaskListModel> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void editItem(int position){
        TaskListModel item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super((view));
            task = view.findViewById(R.id.taskCheckBox);
        }
    }
}
