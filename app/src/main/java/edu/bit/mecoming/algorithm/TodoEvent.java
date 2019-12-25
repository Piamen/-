package edu.bit.mecoming.algorithm;

import android.annotation.SuppressLint;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TodoEvent {
    public enum TodoState{              //待办事项状态
        TODO_STATE_NEXT,
        TODO_STATE_FARAWAY,
        TODO_STATE_DONE
    }
    private String name;        //待办事项名称
    private Time time;          //待办事项时间（时间戳形式，故包括日期）
    private TodoState todoState;//待办事项状态
    private LatLng latLng;      //待办事项坐标
    private String address;     //待办事项地点名称

    /**
     * @ brief:  TodoEvent构造函数
     * @ author: 严霜
     */
    public TodoEvent(String name, Time time, LatLng latLng, String address)
    {
        this.name = name;
        this.time = time;
        this.todoState = TodoState.TODO_STATE_FARAWAY;
        this.latLng = latLng;
        this.address = address;
    }

    public TodoEvent()
    {

    }

    /**
     * @ brief:  TodoEvent比较器，用于优先队列排序
     * @ author: 严霜
     */
    static public Comparator<TodoEvent> comparator = new Comparator<TodoEvent>() {
        @Override
        public int compare(TodoEvent todoEvent, TodoEvent t1) {
            if(todoEvent.time.getTime() - t1.time.getTime() > 0)
                return 1;
            else if(todoEvent.time.getTime() - t1.time.getTime() < 0)
                return -1;
            else return 0;
        }
    };
    
    public static Comparator<TodoEvent> getComparator() {
        return comparator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "TodoEvent{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", todoState=" + todoState +
                ", latLng=" + latLng +
                ", address='" + address + '\'' +
                '}';
    }

    public static void setComparator(Comparator<TodoEvent> comparator) {
        TodoEvent.comparator = comparator;
    }

    public String getName()
    {
        return name;
    }

    public Time getTime() {
        return time;
    }

    private String timeStamp2Date(long milliseconds) {
        String format = "yyyy年MM月dd日 HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(milliseconds));
    }

    /**
     * @ brief:  TodoEvent获取时间（字符串形式）
     * @ author: 严霜
     */
    public String getTimeString(){
        return timeStamp2Date(this.getTime().getTime());
    }

    public TodoState getTodoState() {
        return todoState;
    }

    public void setTodoState(TodoState todoState) {
        this.todoState = todoState;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getAddress() {
        return address;
    }
}
