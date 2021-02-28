package com.octopus.test.baiduMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.octopus.test.R;
import com.octopus.test.utils.FlyLog;
import com.octopus.test.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图选点demo
 */
public class SelectMapActivity extends AppCompatActivity implements
        BaiduMap.OnMapStatusChangeListener,
        PoiItemAdapter.MyOnItemClickListener,
        OnGetGeoCoderResultListener,
        LocationListener,
        GpsStatus.Listener {

    private static final int sDefaultRGCRadius = 500;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng mCenter;
    private Handler mHandler;
    private RecyclerView mRecyclerView;
    private PoiItemAdapter mPoiItemAdapter;
    private GeoCoder mGeoCoder = null;
    private boolean mStatusChangeByItemClick = false;
    private TextView textinfo;
    private double location[] = {22.546250932689176,113.93630403238355};
    private LocationManager locationManager;
    public static List<String> list_provider = null;
    private boolean isGpsEnabled = false;
    private boolean isFirstSetPoint = true;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_choose_place_main);
        textinfo = findViewById(R.id.textinfo);
        init();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, "delete_aiding_data", null);
        //判断是否开启GPS定位功能
        isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        if (isGpsEnabled) {
            list_provider = locationManager.getProviders(true);
            for (String provider : list_provider) {
                locationManager.requestLocationUpdates(provider, 1000, 0, this);
            }
            locationManager.addGpsStatusListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mMapView) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mMapView) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        locationManager.removeUpdates(this);

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (null != mGeoCoder) {
            mGeoCoder.destroy();
        }

        if (null != mMapView) {
            mMapView.onDestroy();
        }
    }

    private void init() {
        initRecyclerView();

        mHandler = new Handler(this.getMainLooper());

        initMap();
    }

    private void initMap() {
        mMapView = findViewById(R.id.mapview);
        if (null == mMapView) {
            return;
        }
        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }
        // 设置初始中心点为国人通信大厦
        double temp[] = GpsTools.WGS84ToGCJ02(
                Double.valueOf((String) SPUtil.get(this,"lon","113.9408379075131")),
                Double.valueOf((String) SPUtil.get(this,"lat","22.542954645599487")));
        mCenter = new LatLng(temp[1], temp[0]);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(mCenter, 16);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                createCenterMarker();
                reverseRequest(mCenter);
            }
        });
    }

    /**
     * 创建地图中心点marker
     */
    private void createCenterMarker() {
        Projection projection = mBaiduMap.getProjection();
        if (null == projection) {
            return;
        }

        Point point = projection.toScreenLocation(mCenter);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_binding_point);
        if (null == bitmapDescriptor) {
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(mCenter)
                .icon(bitmapDescriptor)
                .flat(false)
                .fixedScreenPosition(point);
        mBaiduMap.addOverlay(markerOptions);
        bitmapDescriptor.recycle();
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.poi_list);
        if (null == mRecyclerView) {
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 逆地理编码请求
     *
     * @param latLng
     */
    private void reverseRequest(LatLng latLng) {
        if (null == latLng) {
            return;
        }

        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption().location(latLng)
                .newVersion(1) // 建议请求新版数据
                .radius(sDefaultRGCRadius);

        if (null == mGeoCoder) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mGeoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null == reverseGeoCodeResult) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUI(reverseGeoCodeResult);
            }
        });
    }

    /**
     * 更新UI
     *
     * @param reverseGeoCodeResult
     */
    private void updateUI(ReverseGeoCodeResult reverseGeoCodeResult) {
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();

        PoiInfo curAddressPoiInfo = new PoiInfo();
        curAddressPoiInfo.address = reverseGeoCodeResult.getAddress();
        curAddressPoiInfo.location = reverseGeoCodeResult.getLocation();

        if (null == poiInfos) {
            poiInfos = new ArrayList<>(2);
        }

        poiInfos.add(0, curAddressPoiInfo);

        if (null == mPoiItemAdapter) {

            mPoiItemAdapter = new PoiItemAdapter(poiInfos);
            mRecyclerView.setAdapter(mPoiItemAdapter);
            mPoiItemAdapter.setOnItemClickListener(this);
        } else {
            mPoiItemAdapter.updateData(poiInfos);
        }
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        LatLng newCenter = mapStatus.target;
        location = GpsTools.GCJ02ToWGS84(mCenter.longitude, mCenter.latitude);
        textinfo.setText("WGS84:" + location[0] + "," + location[1]);
        FlyLog.e("GCJ02:" + newCenter.longitude + "," + newCenter.latitude);
        FlyLog.e("WGS84:" + location[0] + "," + location[1]);
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLng newCenter = mapStatus.target;

        // 如果是点击poi item导致的地图状态更新，则不用做后面的逆地理请求，
        if (mStatusChangeByItemClick) {
            if (!Utils.isLatlngEqual(mCenter, newCenter)) {
                mCenter = newCenter;
            }
            mStatusChangeByItemClick = false;
            return;
        }

        if (!Utils.isLatlngEqual(mCenter, newCenter)) {
            mCenter = newCenter;
            reverseRequest(mCenter);
        }

    }

    @Override
    public void onItemClick(int position, PoiInfo poiInfo) {
        if (null == poiInfo || null == poiInfo.getLocation()) {
            return;
        }

        mStatusChangeByItemClick = true;
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(poiInfo.getLocation());
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    public void saveLocation(View view) {
        SPUtil.set(this,"lon",String.valueOf(location[0]));
        SPUtil.set(this,"lat",String.valueOf(location[1]));
        Bundle gpsInfo = Utils.getGpsInfo(location[0], location[1]);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.sendExtraCommand(LocationManager.GPS_PROVIDER, "simulate_gps_info", gpsInfo);
        lm.sendExtraCommand(LocationManager.NETWORK_PROVIDER, "simulate_gps_info", gpsInfo);
        lm.sendExtraCommand(LocationManager.PASSIVE_PROVIDER, "simulate_gps_info", gpsInfo);
    }

    @Override
    public void onGpsStatusChanged(int i) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
