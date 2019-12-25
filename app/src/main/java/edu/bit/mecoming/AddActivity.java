package edu.bit.mecoming;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import edu.bit.mecoming.algorithm.MainService;
import edu.bit.mecoming.algorithm.TodoEvent;
import edu.bit.mecoming.ui.main.TodoListFragment;
/* 添加事项活动 */
public class AddActivity extends AppCompatActivity {
    int mYear, mMonth, mDay;
    Button btn;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;
    static public AddActivity sInstance;
    private LatLng latLng;
    private String address;
    public static int year=0;
    public static int mounth=0;
    public static int day=0;
    public static int hour=0;
    public static int minute =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        sInstance = this;
        clock1display();
    }


    public void showHourPicker() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);

                    AddActivity.hour=hourOfDay;
                    AddActivity.minute =minute;

                    display();

                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();


    }



    public void clock1display() {
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);


        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {

        dateDisplay.setText(new StringBuffer().append(AddActivity.year).append("年")
                .append(AddActivity.mounth).append("月")
                .append(AddActivity.day).append("日 ")
                .append(AddActivity.hour).append(":")
                .append(AddActivity.minute).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            AddActivity.year=year;
            AddActivity.mounth=  monthOfYear+1;
            AddActivity.day=dayOfMonth;
            showHourPicker();

        }
    };
    public Long StringToTimestamp(String time){


        Long times = Long.valueOf(0);
        try {
            times =  ((Timestamp.valueOf(time).getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(times==0){
            System.out.println("String转10位时间戳失败");
        }
        return times;

    }

    public void BackToHomePage(View v){     // 按钮对应的 onclick 响应

        String s = AddActivity.year+"-"+ AddActivity.mounth+"-"+ AddActivity.day +
                " "+ AddActivity.hour+":"+ AddActivity.minute+":00";
        Time time = new Time(Timestamp.valueOf(s).getTime());
//        time.setYear(AddActivity.year);
//        time.setMonth(AddActivity.mounth);
//        time.setDate(AddActivity.day);



        EditText name= (EditText) findViewById(R.id.editText);  //还是根据ID找到对象，并进行接下来的操作
        String string1;
        string1=name.getText().toString();

        TodoEvent todoEvent = new TodoEvent(string1,  time, latLng, address);

        MainService.addTodoEvent(todoEvent);
        finish();

    }

    public void GetTime(View v){     // 按钮对应的 onclick 响应
        showDialog(DATE_DIALOG);

    }

    public void GetName(View v){     // 按钮对应的 onclick 响应

        EditText name= (EditText) findViewById(R.id.editText);  //还是根据ID找到对象，并进行接下来的操作

    }

    public void setAddress(LatLng latlng, String address){
        this.address = address;
        this.latLng = latlng;
        TextView textView = findViewById(R.id.position);
        textView.setText(address);

    }

    public void GetTargetLocation(View v){     // 按钮对应的 onclick 响应

        Intent intent = new Intent();
        intent.setClass(this, MapMainActivity.class);
        startActivity(intent);

    }

}







