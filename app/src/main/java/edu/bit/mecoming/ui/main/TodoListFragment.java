package edu.bit.mecoming.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import edu.bit.mecoming.AddActivity;
import edu.bit.mecoming.DeleteActivity;
import edu.bit.mecoming.MainActivity;
import edu.bit.mecoming.R;
import edu.bit.mecoming.algorithm.MainService;
import edu.bit.mecoming.algorithm.TodoEvent;
import edu.bit.mecoming.ui.main.adapter.LvAdapter;

import static edu.bit.mecoming.algorithm.MainService.getTodoQueue;

/**
 * 主界面的Fragment
 */
public class TodoListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView todoListView;
    private List<TodoEvent> todoEventList = new ArrayList<>();

    public static TodoListFragment sInstance;
    /* author: hcl */
    /* 根据传入的todoQueue，刷新fragment中的listview */
    public void refreshTodoLv(PriorityQueue<TodoEvent> todoQueue)
    {
        todoEventList.clear();
        while (todoQueue.size() > 0)
        {
            todoEventList.add(todoQueue.poll());
        }
        LvAdapter myAdapter = new LvAdapter(this.getContext(),R.layout.list_item, todoEventList);

        todoListView.setAdapter(myAdapter);

    }

    /* author: hcl */
    /* 用于给MainService提供刷新接口，一旦接收到消息，立马刷新 */
    @SuppressLint("HandlerLeak")
    public Handler todoLvRefreshHandler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshTodoLv(getTodoQueue());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        PriorityQueue<TodoEvent> queue = MainService.getTodoQueue();
        if(queue != null) refreshTodoLv(queue);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);

    }

    private View.OnClickListener onAddBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent();
            intent.setClass(getActivity (), AddActivity.class);
            startActivity(intent);


        }
    };

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todolist, container, false);

        todoListView = root.findViewById(R.id.todoLv); //hcl: 绑定todolistview
        sInstance = this;//hcl:用于对外提供访问此类的非静态接口的instance

        /* author: hcl */
        /* 添加按钮的初始化 */
        Button addBtn;
        addBtn = root.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(onAddBtnClicked);

        ListView listView = (ListView) root.findViewById(R.id.todoLv);
        //listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                DeleteActivity.delIndex = position;
                Intent intent = new Intent();
                intent.setClass(MainActivity.Instance, DeleteActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }


}