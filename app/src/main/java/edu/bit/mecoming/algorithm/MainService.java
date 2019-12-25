package edu.bit.mecoming.algorithm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.PriorityQueue;

import edu.bit.mecoming.managers.AlarmManager;
import edu.bit.mecoming.managers.MapManager;
import edu.bit.mecoming.managers.XmlFileManager;
import edu.bit.mecoming.ui.main.TodoListFragment;

public class MainService extends Service {


    static private PriorityQueue<TodoEvent> todoQueue;
    public static MapManager mapManager = new MapManager();                 //地图及路程时长计算管理实例化
    public static AlarmManager alarmManager = new AlarmManager();           //提醒管理器实例化
    private static XmlFileManager xmlFileManager = new XmlFileManager();    //文件读写管理器实例化

    
    private Thread thread;//主线程
    /**
     * @ brief:  主线程Runnable
     * @ author: 严霜
     */
    private Runnable mainRunnable = new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainTask();
            }
        }
    };

    /**
     * @ brief:  获取TODO优先列表
     * @ author: 严霜
     */
   public static PriorityQueue<TodoEvent> getTodoQueue (){
       if (todoQueue == null){
           return null;
       }
       PriorityQueue<TodoEvent> queue = new PriorityQueue<TodoEvent>(todoQueue);
       return queue;
    }

    /**
     * @ brief:  程序主程序，每秒执行一次，检测事件地点与用户所在地的距离，进行提醒、UI更新、文件读写等操作
     * @ author: 严霜
     */
    private void mainTask(){

        TodoEvent nextTodo;
        if (todoQueue.size() > 0){
            nextTodo = todoQueue.peek();
        } else {
            return;
        }
        long curTime = System.currentTimeMillis();
        long walkTime = 1000*60*mapManager.getWalkTime(nextTodo.getLatLng(), this);

        if (curTime+walkTime >= nextTodo.getTime().getTime())//当前时间加行走时间大于事项时间，开始提醒
        {
            alarmManager.startAlarm(nextTodo, context);
            todoQueue.poll();
            TodoListFragment.sInstance.todoLvRefreshHandler.sendMessage(new Message());

            PriorityQueue<TodoEvent> queue = getTodoQueue();
            TodoEvent[] e = new TodoEvent[queue.size()];
            while (!queue.isEmpty()) e[queue.size()-1] = queue.poll();
            xmlFileManager.writeToFile(e, context);
        }
    }

    /**
     * @ brief:  添加TODO事件
     * @ author: 严霜
     */
    public static void addTodoEvent(TodoEvent todoEvent) {
        todoQueue.add(todoEvent);
        xmlFileManager.clearFile();

        PriorityQueue<TodoEvent> queue = getTodoQueue();
        TodoEvent[] e = new TodoEvent[queue.size()];
        while (!queue.isEmpty()) e[queue.size()-1] = queue.poll();
        xmlFileManager.writeToFile(e, context);
    }


    /**
     * @ brief:  删除TODO事件
     * @ author: 严霜
     */
    public static void deleteTodoEvent(int index){
        PriorityQueue<TodoEvent> queue = getTodoQueue();

        if (queue != null) {
            queue.clear();
        }
        int i = 0;
        while (!todoQueue.isEmpty()) {
            if(i == index) todoQueue.poll();
            else queue.add(todoQueue.poll());
            i++;
        }
        todoQueue = new PriorityQueue<TodoEvent>(queue);

        TodoEvent[] e = new TodoEvent[queue.size()];
        while (!queue.isEmpty()) e[queue.size()-1] = queue.poll();
        xmlFileManager.writeToFile(e, context);


    }

    int mStartMode;
    IBinder mBinder;
    boolean mAllowRebind;
    public static Context context;
    /** 当服务被创建时调用. */
    @Override
    public void onCreate() {


    }

    /** 调用startService()启动服务时回调 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        todoQueue = new PriorityQueue<TodoEvent>(100, TodoEvent.comparator);
        context = this;
        thread = new Thread(mainRunnable);
        thread.start();
        todoQueue.addAll(xmlFileManager.readFromFile());
        TodoListFragment.sInstance.todoLvRefreshHandler.sendMessage(new Message());

        return START_STICKY;
    }

    /** 通过bindService()绑定到服务的客户端 */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** 通过unbindService()解除所有客户端绑定时调用 */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** 通过bindService()将客户端绑定到服务时调用*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** 服务不再有用且将要被销毁时调用 */
    @Override
    public void onDestroy() {
        thread.stop();

        PriorityQueue<TodoEvent> queue = getTodoQueue();
        TodoEvent[] e = new TodoEvent[queue.size()];
        while (!queue.isEmpty()) e[queue.size()-1] = queue.poll();
        xmlFileManager.writeToFile(e, context);
    }

}