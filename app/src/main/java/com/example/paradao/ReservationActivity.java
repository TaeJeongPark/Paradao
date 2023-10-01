package com.example.paradao;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

/**
 * @packageName	: com.example.paradao
 * @fileName	: ReservationActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 예약확인 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class ReservationActivity extends AppCompatActivity {

    String id;  //사용자 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resulvation);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");    //사용자 아이디 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectData();

        ImageButton btnCancel = (ImageButton) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteData();
            }
        });

    }

    //예약 정보 삭제 함수
    private void deleteData() {

        TextView tf_resultProductName = (TextView) findViewById(R.id.tf_resultProductName);
        TextView tf_resultProductPrice = (TextView) findViewById(R.id.tf_resultProductPrice);
        TextView tf_resulvation = (TextView) findViewById(R.id.tf_resulvation);
        TextView tf_brandname = (TextView) findViewById(R.id.tf_brandname);
        TextView tf_storename = (TextView) findViewById(R.id.tf_storename);
        TextView tf_resulvstarttime = (TextView) findViewById(R.id.tf_resulvstarttime);
        TextView tf_resulvendtime = (TextView) findViewById(R.id.tf_resulvendtime);

        Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("DELETE FROM reservation WHERE id = '" + id + "'");
        db.close();
        dbHelper.close();

        Toast.makeText(getApplicationContext(), "예약이 취소 되었습니다.", Toast.LENGTH_LONG).show();

        tf_resultProductName.setText("에약된 제품이 없습니다.");
        tf_resultProductPrice.setText("");
        tf_resulvation.setText("");
        tf_brandname.setText("");
        tf_storename.setText("");
        tf_resulvstarttime.setText("");
        tf_resulvendtime.setText("");

    }

    //예약 정보 조회 함수
    private void selectData() {

        TextView tf_resultProductName = (TextView) findViewById(R.id.tf_resultProductName);
        TextView tf_resultProductPrice = (TextView) findViewById(R.id.tf_resultProductPrice);
        TextView tf_resulvation = (TextView) findViewById(R.id.tf_resulvation);
        TextView tf_brandname = (TextView) findViewById(R.id.tf_brandname);
        TextView tf_storename = (TextView) findViewById(R.id.tf_storename);
        TextView tf_resulvstarttime = (TextView) findViewById(R.id.tf_resulvstarttime);
        TextView tf_resulvendtime = (TextView) findViewById(R.id.tf_resulvendtime);

        DecimalFormat formatter = new DecimalFormat("###,###"); //천단위 구분기호 생성 포맷

        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT productname, productprice, brandname, storename, starttime, endtime FROM reservation, product, store, brand WHERE reservation.productid = product.productid AND reservation.storeid = store.storeid AND reservation.brandid = brand.brandid AND id = '" + id + "'", null);

            if (c.moveToNext()) {
                tf_resultProductName.setText(c.getString(0));
                tf_resultProductPrice.setText(formatter.format(c.getInt(1)) + " 원");
                tf_resulvation.setText("1 개");
                tf_brandname.setText(c.getString(2));
                tf_storename.setText(c.getString(3));
                tf_resulvstarttime.setText(c.getString(4));
                tf_resulvendtime.setText(c.getString(5));
            } else {
                tf_resultProductName.setText("에약된 제품이 없습니다.");
                tf_resultProductPrice.setText("");
                tf_resulvation.setText("");
                tf_brandname.setText("");
                tf_storename.setText("");
                tf_resulvstarttime.setText("");
                tf_resulvendtime.setText("");
            }

            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "서버 접속에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Log.d("ReservationActivity", "데이터베이스 조회 완료");
    }

}
