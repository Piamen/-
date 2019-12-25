package edu.bit.mecoming;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import edu.bit.mecoming.algorithm.MainService;
import edu.bit.mecoming.algorithm.TodoEvent;

/* 主活动 */
public class MainActivity extends FragmentActivity {
    public static TencentLocation tmpLocation;

    private LocationManager locationManager;
    private String provider;
    public static MainActivity Instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(permissions, 0);
            }
        }
        
        /* 开启主服务 */
        startService(new Intent(getBaseContext(), MainService.class));

        Instance = this;

        /* 开始进行定位 */
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(10);
        TencentLocationListener listener = tencentLocationListener;
        TencentLocationManager locationManager = TencentLocationManager.getInstance(this);
        int error = locationManager.requestLocationUpdates(request, listener);

    }

    TencentLocationListener tencentLocationListener = new TencentLocationListener() {
        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            MainActivity.tmpLocation = tencentLocation;
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    };
    

}