package edu.bit.mecoming.managers;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.object.param.RoutePlanningParam;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.WalkingResultObject;

import com.tencent.map.tools.net.http.HttpResponseListener;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import edu.bit.mecoming.DeleteActivity;
import edu.bit.mecoming.MainActivity;
import edu.bit.mecoming.MapMainActivity;

import static edu.bit.mecoming.DeleteActivity.todoEvent;

/**
 * @ class:  MapManager
 * @ brief:  计算路程时间，包括回调和轮询两种方式
 * @ author: 危昊成
 */
public class MapManager {
    final HttpResponseListener lsnerRoute = new HttpResponseListener() {
        @Override
        public void onSuccess(int i, Object o) {
            List<WalkingResultObject.Route> routes = ((WalkingResultObject) o).result.routes;
            MapMainActivity.routesWalk = routes;
        }

        @Override
        public void onFailure(int i, String s, Throwable throwable) {

        }
    };
    public long getWalkTime(LatLng latLng, Context context) {

        final TencentSearch searchInst = new TencentSearch(context);

        if (MainActivity.tmpLocation == null) return 0;

        LatLng from = new LatLng(MainActivity.tmpLocation.getLatitude(), MainActivity.tmpLocation.getLongitude());
        LatLng to = latLng;
        RoutePlanningParam param = new WalkingParam(from, to);
        searchInst.getRoutePlan(param, lsnerRoute);
        if(MapMainActivity.routesWalk == null) return 0;
        WalkingResultObject.Route topRoute = MapMainActivity.routesWalk.get(0);
        return (long) topRoute.duration;
    }

    final HttpResponseListener lsnerRouteHandle = new HttpResponseListener() {
        @Override
        public void onSuccess(int i, Object o) {
            List<WalkingResultObject.Route> routes = ((WalkingResultObject) o).result.routes;
            MapMainActivity.routesWalk = routes;


            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("msg", todoEvent.getAddress() + "\n距离这里" +
                    routes.get(0).duration + "分钟路程");
            message.setData(data);
            DeleteActivity.sInstance.timeCalcHandler.sendMessage(message);

        }

        @Override
        public void onFailure(int i, String s, Throwable throwable) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("msgerr", s + "，无法计算路程");
            Log.e("MapManager", s);
            message.setData(data);
            DeleteActivity.sInstance.timeCalcHandler.sendMessage(message);


        }
    };
    public void getWalkTime_IT(LatLng latLng, Context context) {

        final TencentSearch searchInst = new TencentSearch(context);

        if (MainActivity.tmpLocation == null) {
            Log.e("MapManager", "meidingwei");
            Toast.makeText(context,"未开启定位",Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng from = new LatLng(MainActivity.tmpLocation.getLatitude(), MainActivity.tmpLocation.getLongitude());
        LatLng to = latLng;

        RoutePlanningParam param = new WalkingParam(from, to);
        searchInst.getRoutePlan(param, lsnerRouteHandle);
    }

}
