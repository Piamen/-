package edu.bit.mecoming.managers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import edu.bit.mecoming.algorithm.TodoEvent;


/**
 * @ class:  XmlFileManager
 * @ brief:  文件读写管理
 * @ author: 严霜
 */
public class XmlFileManager {
    private static String createDir(String dirPath){
        //因为文件夹可能有多层，比如:  a/b/c/ff.txt  需要先创建a文件夹，然后b文件夹然后...
        try{
            File file=new File(dirPath);
            if(file.getParentFile().exists()){
                file.mkdir();
                return file.getAbsolutePath();
            }
            else {
                createDir(file.getParentFile().getAbsolutePath());
                file.mkdir();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return dirPath;
    }


    public void writeToFile(TodoEvent[] events, Context context) {
        // 拿到xml序列化器
        XmlSerializer xs = Xml.newSerializer();

        // 包装外部存储路径
        File file = new File(Environment.getExternalStorageDirectory()+"/Android/data/edu.bit.MeComing",
                "todoEvents.xml");
        if(!file.getParentFile().exists()) createDir(file.getParentFile().getAbsolutePath());


        FileOutputStream fos = null;
        try {
            // 用输出流输出info.xml
            fos = new FileOutputStream(file);
            // 指定用utf-8编码生成文件
            xs.setOutput(fos, "UTF_8");
            // 生成xml表头，两个参数表示表头属性
            xs.startDocument("UTF-8", true);
            // 生成根节点
            /*
             * 其实拼接的过程有点像写html文件， 无非就是一对标签，一对标签的写而已。
             * 从代码可以看出，xs.startTag(),表示标签开始，endTag()表示标签结束。
             * 第二个参数表示节点名称。
             */
            xs.startTag(null, "todo_list");
            for(TodoEvent e: events) {
                xs.startTag(null, "todo_event");
                xs.startTag(null, "name");
                xs.text(e.getName());
                xs.endTag(null, "name");

                xs.startTag(null, "time");
                xs.text(new String(String.valueOf(e.getTime().getTime())));
                xs.endTag(null, "time");

                xs.startTag(null, "lat");
                xs.text(new String(String.valueOf(e.getLatLng().latitude)));
                xs.endTag(null, "lat");

                xs.startTag(null, "lng");
                xs.text(new String(String.valueOf(e.getLatLng().longitude)));
                xs.endTag(null, "lng");

                xs.startTag(null, "address");
                xs.text(new String(e.getAddress()));
                xs.endTag(null, "address");
                xs.endTag(null, "todo_event");
            }
            xs.endTag(null, "todo_list");



            // 表示文档生成结束
            xs.endDocument();

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

    public static List<TodoEvent> getTodoEvents(InputStream inStream)
            throws Exception {
        TodoEvent todoEvent = null;
        List<TodoEvent> todoEvents = null;
        todoEvents = new ArrayList<TodoEvent>();
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(inStream, "UTF-8");
        int event = pullParser.getEventType();// 觸發第一個事件
        LatLng latLng = new LatLng();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    todoEvents = new ArrayList<TodoEvent>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("todo_event".equals(pullParser.getName())) {
//                        int id = new Integer(pullParser.getAttributeValue(0));
                        todoEvent = new TodoEvent();
                        latLng = new LatLng();
//                        todoEvent.setTodoState(id);
                    }
                    if (todoEvent != null) {
                        if ("name".equals(pullParser.getName())) {
                            todoEvent.setName(pullParser.nextText());
                        }
                        if ("time".equals(pullParser.getName())) {
                            Time t = new Time(Long.parseLong(pullParser.nextText()));
                            todoEvent.setTime(t);
                        }
                        if ("lat".equals(pullParser.getName())) {
                            double x = Double.parseDouble(pullParser.nextText());
                            latLng.setLatitude(x);
                        }
                        if ("lng".equals(pullParser.getName())) {
                            double x = Double.parseDouble(pullParser.nextText());
                            latLng.setLongitude(x);

                            todoEvent.setLatLng(latLng);

                        }
                        if ("address".equals(pullParser.getName())) {
                            todoEvent.setAddress(pullParser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("todo_event".equals(pullParser.getName())) {
                        todoEvents.add(todoEvent);
                        todoEvent = null;
                    }
                    break;
            }
            event = pullParser.next();
        }

        return todoEvents;
    }

    public List<TodoEvent> readFromFile()
    {

        // 包装外部存储路径
        File file = new File(Environment.getExternalStorageDirectory()+"/Android/data/edu.bit.MeComing",
                "todoEvents.xml");

        if (!file.exists()) return new ArrayList<TodoEvent>();
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<TodoEvent> todoEventList = null;
        try {
            todoEventList = this.getTodoEvents(inStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(todoEventList.size() > 0)
        for (TodoEvent todoEvent : todoEventList) {

        }
        return todoEventList;
    }
    public void clearFile()
    {

    }
}
