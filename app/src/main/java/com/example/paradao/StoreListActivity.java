package com.example.paradao;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @packageName	: com.example.paradao
 * @fileName	: StoreListActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 매장리스트 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class StoreListActivity extends AppCompatActivity {

    ListView listItem;

    private Vector<Integer> vcStoreid = new Vector<>();     //검색된 지점 아이디 데이터를 담는 벡터
    ArrayList<ListStoreData> list = new ArrayList<>();   //리스트에 담을 데이터

    String uid;  //사용자 아이디
    int brandid; //브랜드 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storelist);

        Intent intent = getIntent();
        uid = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신
        brandid = intent.getExtras().getInt("brandid");    //브랜드 아이디 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectData();

        StoreListActivity.CustomList adapter = new StoreListActivity.CustomList(StoreListActivity.this);
        listItem = (ListView) findViewById(R.id.list_store);
        listItem.setAdapter(adapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("id", uid);                //로그인한 사용자 회원번호 전송
                intent.putExtra("storeid", vcStoreid.get(+position));       //선택된 지점 아이디 전송
                intent.putExtra("flag", "select"); //진입 화면 경로 구분 값 송신
                startActivity(intent);  //제품리스트 화면으로 이동
            }
        });

    }

    //매장리스트 데이터 조회 함수
    private void selectData() {
        Log.d("StoreListActivity", "데이터베이스 조회 시작");
        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT storename, storestarttime, storeendtime, storeimg, storeid FROM store WHERE brandid = " + brandid, null);
            while(c.moveToNext()) {
                Log.d("StoreListActivity", c.getString(1));
                ListStoreData listdata = new ListStoreData(); //리스트에 담을 정보 데이터 객체 생성

                listdata.setStoreName(c.getString(0));    //지점명 Value 값을 listdata 객체에 set
                listdata.setStoreStartTime(c.getString(1));    //운영 시작 시간 Value 값을 listdata 객체에 set
                listdata.setStoreEndTime(c.getString(2));  //운영 종료 시간 Value 값을 listdata 객체에 set
                listdata.setImage(String.valueOf(getResources().getIdentifier(c.getString(3), "drawable", getPackageName()))); //제품 이미지 경로 값을 listdata 객체에 set

                vcStoreid.add(c.getInt(4));            //검색된 지점아이디 저장

                list.add(listdata); //list에 listdata 객체를 add
            }
            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "서버 접속에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Log.d("StoreListActivity", "데이터베이스 조회 완료");
    }

    //리스트 커스텀 함수
    public class CustomList extends ArrayAdapter<ListStoreData> {
        private final Activity context;
        public CustomList(Activity context) {
            super(context, R.layout.item_store, list);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.item_store, null, true);

            ImageView image = (ImageView) rowView.findViewById(R.id.image);
            TextView storeName = (TextView) rowView.findViewById(R.id.storeName);
            TextView time = (TextView) rowView.findViewById(R.id.time);

            ListStoreData listData = list.get(position);

            image.setImageResource(Integer.parseInt(listData.getImage()));
            storeName.setText(listData.getStoreName());
            time.setText(listData.getStoreStartTime() + " ~ " + listData.getStoreEndTime());

            return rowView;
        }
    }

}
