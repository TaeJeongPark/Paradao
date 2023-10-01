package com.example.paradao;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * @packageName	: com.example.paradao
 * @fileName	: MapActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 카카오 맵 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class MapActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private String storeAddr;   //매장 주소
    private String storeName;   //매장명

    private double lat = 0;                         //위도 데이터를 담은 실수형 변수
    private double lon = 0;                         //경도 데이터를 담은 실수형 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        storeAddr = intent.getExtras().getString("storeaddr");              //매장 주소 데이터 수신
        storeName = intent.getExtras().getString("storename");              //매장명 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        geocoderStart(storeAddr);

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
        mapView.setZoomLevel(1, true);

        MapPOIItem marker = new MapPOIItem();
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(lat, lon);
        marker.setItemName(storeName);
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        mapViewContainer.addView(mapView);

    }

    //주소 데이터를 위도와 경도로 변환
    public void geocoderStart(String addressIn) {

        final Geocoder geocoder = new Geocoder(this.getBaseContext());
        List<Address> list = null;

        try {
            list = geocoder.getFromLocationName(addressIn, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MapActivity","예외발생 : 주소 변환에 실패했습니다.");
        }

        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(getBaseContext(), "해당되는 주소가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Address addr = list.get(0);
                lat = addr.getLatitude();
                lon = addr.getLongitude();
            }
        }
    }
}
