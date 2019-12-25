package edu.bit.mecoming;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.HashMap;
import java.util.PriorityQueue;

import edu.bit.mecoming.algorithm.MainService;
import edu.bit.mecoming.algorithm.TodoEvent;
import edu.bit.mecoming.managers.MapManager;

import static edu.bit.mecoming.algorithm.MainService.context;
import static edu.bit.mecoming.algorithm.MainService.getTodoQueue;

/* 删除事项活动 */
public class DeleteActivity extends AppCompatActivity {

    HashMap<String, Object> map;

    public static DeleteActivity sInstance;
    public static int delIndex;

    public static TodoEvent todoEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);

        sInstance = this;


        MapManager mapManager = new MapManager();
        PriorityQueue<TodoEvent> queue = MainService.getTodoQueue();
        int i = delIndex;
        while (i-- >= 0) {
            todoEvent = queue.poll();
        }
        TextView text1= (TextView) findViewById(R.id.textViewdelete);
        text1.setText(todoEvent.getName());   // 设置字符
        TextView text2= (TextView) findViewById(R.id.textViewdelete2);
        text2.setText(todoEvent.getTimeString());   // 设置字符
        TextView text3= (TextView) findViewById(R.id.textViewdelete3);
        text3.setText(todoEvent.getAddress() + "\n正在计算路程");   // 设置字符

        mapManager.getWalkTime_IT(todoEvent.getLatLng(), context);

    }

    @SuppressLint("HandlerLeak")
    public Handler timeCalcHandler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            TextView text3= (TextView) findViewById(R.id.textViewdelete3);
            text3.setText(data.getString("msg"));   // 设置字符
        }
    };

    public void GoBack(View v){     // 按钮对应的 onclick 响应

        finish();
    }
    public void Delete(View v){     // 按钮对应的 onclick 响应
        //此处调用删除函数，删除ToDoEvent中排序后的第Alarm.view个
        MainService.deleteTodoEvent(delIndex);

        finish();
    }
}
