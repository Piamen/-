package edu.bit.mecoming.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.bit.mecoming.R;
import edu.bit.mecoming.algorithm.TodoEvent;

import static android.view.LayoutInflater.from;

public class LvAdapter extends ArrayAdapter {   //ListView的Adapter

    private int resource;
    public LvAdapter(Context context, int resource, List<TodoEvent> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TodoEvent todoEvent = (TodoEvent)getItem(position);
        @SuppressLint({"ViewHolder", "InflateParams"})
        View view = from(getContext()).inflate(resource, null);

        TextView name = (TextView)view.findViewById(R.id.text1);
        TextView news = (TextView)view.findViewById(R.id.text3);

        if (todoEvent != null) {
            name.setText(todoEvent.getName() + " | " +  todoEvent.getAddress());
            news.setText(todoEvent.getTimeString());
        }


        return view;
    }

}
