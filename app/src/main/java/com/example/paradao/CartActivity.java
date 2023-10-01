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
 * @fileName	: CartActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 장바구니 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class CartActivity extends AppCompatActivity {

    ListView listItem;

    private Vector<String> vcStoreName = new Vector<>();     //검색된 매장명 데이터를 담는 벡터
    private Vector<String> vcProductName = new Vector<>();   //검색된 제품명 데이터를 담는 벡터
    private Vector<Integer> vcCartid = new Vector<>();       //검색된 카트아이디 데이터를 담는 벡터
    ArrayList<ListProductData> list = new ArrayList<>();     //리스트에 담을 데이터

    String id;  //사용자 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectData();

        CartActivity.CustomList adapter = new CartActivity.CustomList(CartActivity.this);
        listItem = (ListView) findViewById(R.id.list_alam);
        listItem.setAdapter(adapter);
        listItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    delete(position);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    //체크된 아이템 삭제 함수
    private void delete(int position) {
        Log.d("CartActivity", "cartid = " + vcCartid.get(position));

        Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("DELETE FROM cart WHERE id = '" + id + "' AND cartid = " + vcCartid.get(position));
        db.close();
        dbHelper.close();

        Toast.makeText(getApplicationContext(), "해당 제품이 삭제 되었습니다.", Toast.LENGTH_LONG).show();

        list.clear();
        selectData();
    }

    //장바구니 데이터 조회 함수
    private void selectData() {
        Log.d("CartActivity", "데이터베이스 조회 시작");
        try {
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT storename, productname, productstock, productimg, cartid FROM cart, product, store WHERE cart.productid = product.productid AND cart.storeid = store.storeid AND id = '" + id + "'", null);
            while(c.moveToNext()) {
                Log.d("CartActivity", c.getString(1));
                ListProductData listdata = new ListProductData(); //리스트에 담을 정보 데이터 객체 생성

                listdata.setStoreName(c.getString(0));    //매장명 Value 값을 listdata 객체에 set
                listdata.setProductName(c.getString(1));    //제품명 Value 값을 listdata 객체에 set
                listdata.setQuantity(c.getString(2));  //재고량 Value 값을 listdata 객체에 set
                listdata.setImage(String.valueOf(getResources().getIdentifier(c.getString(3), "drawable", getPackageName()))); //제품 이미지 경로 값을 listdata 객체에 set

                vcStoreName.add(c.getString(0));        //검색된 매장명 저장
                vcProductName.add(c.getString(1));      //검색된 제품명 저장
                vcCartid.add(c.getInt(4));            //검색된 카트아이디 저장

                list.add(listdata); //list에 listdata 객체를 add
            }
            CartActivity.CustomList adapter = new CartActivity.CustomList(CartActivity.this);
            listItem = (ListView) findViewById(R.id.list_alam);
            listItem.setAdapter(adapter);
            listItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        delete(position);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "서버 접속에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Log.d("CartActivity", "데이터베이스 조회 완료");
    }

    //리스트 커스텀 함수
    public class CustomList extends ArrayAdapter<ListProductData> {
        private final Activity context;
        public CustomList(Activity context) {
            super(context, R.layout.item_product, list);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.item_product, null, true);

            ImageView image = (ImageView) rowView.findViewById(R.id.image);
            TextView productName = (TextView) rowView.findViewById(R.id.productName);
            TextView storeName = (TextView) rowView.findViewById(R.id.storeName);
            TextView quantity = (TextView) rowView.findViewById(R.id.quantity);

            ListProductData listData = list.get(position);

            image.setImageResource(Integer.parseInt(listData.getImage()));
            productName.setText(listData.getProductName());
            storeName.setText(listData.getStoreName());
            quantity.setText("남은 재고량 : " + listData.getQuantity());

            return rowView;
        }
    }

}
