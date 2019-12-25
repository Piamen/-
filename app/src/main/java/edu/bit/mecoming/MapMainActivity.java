package edu.bit.mecoming;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SuggestionResultObject;
import com.tencent.lbssearch.object.result.WalkingResultObject;
import com.tencent.map.tools.net.http.HttpResponseListener;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.bit.mecoming.managers.MapManager;

/* 选择地点活动 */
public class MapMainActivity extends FragmentActivity {

    private Button btn_stlmp;
    private Button btn_trfmp;
    private TencentMap tencentMap;
    private EditText et_dest;
    private ListView mlist;
    private Button btn_srch;

    public static MapMainActivity sInstance;
    public static TencentSearch tencentSearch;
    public static List<WalkingResultObject.Route> routesWalk;
    public static List<SuggestionResultObject.SuggestionData> suggestionDataList;
    public static SuggestionResultObject.SuggestionData selectedData;
    public static MapManager mapManager = new MapManager();
    public static Marker marker;

    final HttpResponseListener lsnerSuggest = new HttpResponseListener() {
        @Override
        public void onSuccess(int i, Object o) {
            SuggestionResultObject sgRes = ((SuggestionResultObject) o);
            MapMainActivity.suggestionDataList = sgRes.data;
            try {

                SimpleAdapter adapter;
                List<HashMap<String, Object>> list;
                HashMap<String, Object> map;
                list = new ArrayList<HashMap<String, Object>>();
                int i0 = 1;
                for (SuggestionResultObject.SuggestionData x: suggestionDataList)
                {

                    map = new HashMap<String, Object>();
                    map.put("index", (i0++) + " | " + x.title);

                    map.put("title", x.address);
                    list.add(map);


                    Log.i("MapMainActivity + onS", x.address);
                }
                String[] from = {"index", "title"};
                int[] to = {android.R.id.text1, android.R.id.text2};

                adapter = new SimpleAdapter(MapMainActivity.sInstance, list, android.R.layout.simple_list_item_2, from, to);
                mlist = findViewById(R.id.mlist);
                mlist.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("MapMainActivity + onS", e.toString());
            }

        }

        @Override
        public void onFailure(int i, String s, Throwable throwable) {
            Log.e("MapMainActivity", s);

        }
    };

    public void getSuggestion(String address, String region, Context context) {

        TencentSearch search = new TencentSearch(this);
        SuggestionParam param = new SuggestionParam(address,region);
        search.suggestion(param, lsnerSuggest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_main);
        sInstance = this;
        // 地图控件定义
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment =
                (SupportMapFragment) fm.findFragmentById(R.id.map_frag);
        tencentMap = mapFragment.getMap();
        tencentMap.getUiSettings().setZoomControlsEnabled(false);
        tencentMap.setMyLocationEnabled(true);

        // 地图类型设置按钮单击事件
        btn_stlmp = findViewById(R.id.btn_stlmp);
        btn_stlmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tencentMap.getMapType() == TencentMap.MAP_TYPE_SATELLITE) {
                    tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
                } else {
                    tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
                }
            }
        });

        // 交通热力图设置按钮单击事件
        btn_trfmp = findViewById(R.id.btn_trfmp);
        btn_trfmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tencentMap.isTrafficEnabled() == false) {
                    tencentMap.setTrafficEnabled(true);
                } else {
                    tencentMap.setTrafficEnabled(false);
                }

            }
        });

        // 输入地址查询事件
        et_dest = findViewById(R.id.et_dest);
        et_dest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String addr = et_dest.getText().toString();
                getSuggestion(addr, "北京", sInstance);  // this context is written wrong probably
                Log.w("onTextChanged: ", addr);
            }
        });

        // 搜索建议栏点击事件 点击搜索栏ListView对应编号的控件后将对应搜索建议的值存给静态地址结果变量
        // 并且将控件内容（搜索建议）投影到地址栏中，同时在MapFragment中添加一个Marker标记当前选择的地点。
        try {

            mlist = findViewById(R.id.mlist);
            mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {

                    MapMainActivity.selectedData = MapMainActivity.suggestionDataList.get(position);
                    et_dest.setText(selectedData.title);
                    if(MapMainActivity.marker!=null)marker.remove();

                    MapMainActivity.marker = tencentMap.addMarker(new MarkerOptions().
                            position(selectedData.latLng).
                            title(selectedData.city).
                            snippet("DestinationMarker"));

                    double l1,l2;
                    l1=selectedData.latLng.latitude;
                    l2=selectedData.latLng.longitude;
                    CameraUpdate cameraSigma =
                            CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                    new LatLng(l1,l2), //新的中心点坐标
                                    15,  //新的缩放级别
                                    45f, //俯仰角 0~45° (垂直地图时为0)
                                    45f)); //偏航角 0~360° (正北方为0)
                    //移动地图
                    tencentMap.animateCamera(cameraSigma);
                } catch (Exception e) {

                    Log.e("MapMainActivity", e.toString());
                }
                }

            });
        } catch (Exception e)
        {
            Log.e("MapMainActivity", e.toString());
        }

        // 搜索确认键 点击CONFIRM按钮（id=btn_srch）移除MARKER，退出MapActivity并且将数据传递给新建任务的模块
        btn_srch = findViewById(R.id.btn_srch);
        btn_srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.sInstance.setAddress(selectedData.latLng, selectedData.title);

                finish();
            }
        });

    }

}