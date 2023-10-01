package com.example.paradao;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

/**
 * @packageName	: com.example.paradao
 * @fileName	: StoreDetailActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 매장상세정보 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class StoreDetailActivity extends AppCompatActivity {

    private String id;  //사용자 아이디
    private int storeid;  //제품 아이디
    private String storeAddr;  //매장 주소
    private String storeName;  //매장명

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storedetail);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신
        storeid = intent.getExtras().getInt("storeid");      //매장 아이디 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectData();

        ImageButton btnMap = (ImageButton) findViewById(R.id.btn_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("storeaddr", storeAddr);                //로그인한 사용자 회원번호 전송
                intent.putExtra("storename", storeName);           //매장 아이디 전송
                startActivity(intent);  //지도 화면으로 이동
            }
        });

        TextView tellNum = (TextView) findViewById(R.id.tf_storeTellNum);
        tellNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tellNum.getText())));   //매장 전화번호 자동 입력된 상태로 통화 앱 실행
                return false;
            }
        });

    }

    private void selectData() {

        ImageView img_store = (ImageView) findViewById(R.id.img_store);
        TextView tf_resultStoreName = (TextView) findViewById(R.id.tf_resultStoreName);
        TextView tf_stime = (TextView) findViewById(R.id.tf_stime);
        TextView tf_storeTellNum = (TextView) findViewById(R.id.tf_storeTellNum);
        TextView tf_storeAddress = (TextView) findViewById(R.id.tf_storeAddress);

        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT storename, storeaddr, storetel, storestarttime, storeendtime, storeimg FROM store WHERE storeid = " + storeid, null);

            if (c.moveToNext()) {
                tf_resultStoreName.setText(c.getString(0));
                tf_storeAddress.setText(c.getString(1));
                tf_storeTellNum.setText(c.getString(2));
                tf_stime.setText(c.getString(3) + " ~ " + c.getString(4));
                img_store.setImageResource(getResources().getIdentifier(c.getString(5),"drawable",getPackageName()));
                storeAddr = c.getString(1);
                storeName = c.getString(0);
            }

            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "서버 접속에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Log.d("StoreDetailActivity", "데이터베이스 조회 완료");

    }
}