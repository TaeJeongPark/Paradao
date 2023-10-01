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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @packageName	: com.example.paradao
 * @fileName	: ProductDetailActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 제품상세정보 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class ProductDetailActivity extends AppCompatActivity {

    String id;  //사용자 아이디
    int productid;  //제품 아이디
    int storeid;    //지점 아이디
    int brandid;    //브랜드 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신
        productid = intent.getExtras().getInt("productid");    //제품 아이디 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectData();

        ImageButton btnStoreInfo = (ImageButton) findViewById(R.id.btn_storeInfo);
        btnStoreInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreDetailActivity.class);
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("storeid", storeid);       //매장 아이디 전송
                startActivity(intent);  //매장상세정보 화면으로 이동
            }
        });

        ImageButton btnAddCart = (ImageButton) findViewById(R.id.btn_addCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertCart();
            }
        });

        ImageButton btnAddAalam = (ImageButton) findViewById(R.id.btn_addAlam);
        btnAddAalam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertAlam();
            }
        });

        ImageButton btnAddReservation = (ImageButton) findViewById(R.id.btn_addReservation);
        btnAddReservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertReservation();
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

    //제품 예약
    private void insertReservation() {
        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor c = db.rawQuery("SELECT COUNT(*) FROM reservation WHERE id = " + id, null);
            if(c.moveToNext()){
                if(c.getInt(0) !=0 ) {
                    Toast.makeText(getApplicationContext(), "이미 예약된 제품이 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String startTime = dateFormat.format(date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.HOUR, +2);
                    String endTime = dateFormat.format(cal.getTime());
                    Log.d("ProductDetailActivity", "startTime : " + startTime + ", endTime : " + endTime);
                    db.execSQL("INSERT INTO reservation (id, productid, storeid, brandid, starttime, endtime) VAlUES('" + id + "', " + productid + ", " + storeid + ", " + brandid + ", '" + startTime + "', '" + endTime + "')");
                    db.close();
                    dbHelper.close();
                    Toast.makeText(getApplicationContext(), "제품 예약을 완료했습니다.", Toast.LENGTH_LONG).show();
                    Log.d("ProductDetailActivity", "제품 예약 완료");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //입고알림에 제품 추가
    private void insertAlam() {
        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("INSERT INTO notice (id, productid, storeid, brandid) VAlUES('"+id+"', "+productid+", "+storeid+", "+brandid+")");
            db.close();
            dbHelper.close();
            Toast.makeText(getApplicationContext(), "입고알림 리스트에 제품을 추가했습니다.", Toast.LENGTH_LONG).show();
            Log.d("ProductDetailActivity", "입고알림에 제품 추가 완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //카트에 제품 추가
    private void insertCart() {
        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.execSQL("INSERT INTO cart (id, productid, storeid, brandid) VAlUES('"+id+"', "+productid+", "+storeid+", "+brandid+")");
            db.close();
            dbHelper.close();
            Toast.makeText(getApplicationContext(), "장바구니 리스트에 제품을 추가했습니다.", Toast.LENGTH_LONG).show();
            Log.d("ProductDetailActivity", "카트에 제품 추가 완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //제품 상세정보 조회
    private void selectData() {

        ImageView img_product = (ImageView) findViewById(R.id.img_product);
        TextView tf_resultProductName = (TextView) findViewById(R.id.tf_resultProductName);
        TextView tf_resultProductPrice = (TextView) findViewById(R.id.tf_resultProductPrice);
        TextView tf_resultProductQuantity = (TextView) findViewById(R.id.tf_resultProductQuantity);
        TextView tf_resultProductEvent = (TextView) findViewById(R.id.tf_resultProductEvent);
        TextView tf_supply = (TextView) findViewById(R.id.tf_supply);
        TextView tf_storeName = (TextView) findViewById(R.id.tf_storeName);
        TextView tf_storeAddress = (TextView) findViewById(R.id.tf_storeAddress);
        TextView tf_storeTellNum = (TextView) findViewById(R.id.tf_storeTellNum);

        DecimalFormat formatter = new DecimalFormat("###,###"); //천단위 구분기호 생성 포맷

        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT productname, productprice, productstock, productevent, productsupply, storename, storeaddr, storetel, product.storeid, productimg, product.brandid FROM product, store WHERE product.storeid = store.storeid AND productid = " + productid, null);

            if (c.moveToNext()) {
                tf_resultProductName.setText(c.getString(0));
                tf_resultProductPrice.setText(formatter.format(c.getInt(1)) + " 원");
                tf_resultProductQuantity.setText(formatter.format(c.getInt(2)) + " 개");
                tf_resultProductEvent.setText(c.getString(3));
                tf_supply.setText(c.getString(4));
                tf_storeName.setText(c.getString(5));
                tf_storeAddress.setText(c.getString(6));
                tf_storeTellNum.setText(c.getString(7));
                storeid = c.getInt(8);
                img_product.setImageResource(getResources().getIdentifier(c.getString(9),"drawable",getPackageName()));
                brandid = c.getInt(10);
            }

            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "서버 접속에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Log.d("ProductDetailActivity", "데이터베이스 조회 완료");

    }

}
