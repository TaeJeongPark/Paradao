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
import java.util.Locale;
import java.util.Vector;

/**
 * @packageName	: com.example.paradao
 * @fileName	: ProductListActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 제품리스트 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class ProductListActivity extends AppCompatActivity {

    ListView listItem;

    private Vector<String> vcStoreName = new Vector<>();     //검색된 매장명 데이터를 담는 벡터
    private Vector<String> vcProductName = new Vector<>();   //검색된 제품명 데이터를 담는 벡터
    private Vector<Integer> vcProductid = new Vector<>();     //검색된 알림아이디 데이터를 담는 벡터
    ArrayList<ListProductData> list = new ArrayList<>();     //리스트에 담을 데이터

    String uid;  //사용자 아이디
    int storeid;  //지점 아이디
    String flag;    //진입 화면 경로 구분 값
    String searchValue; //검색할 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        Intent intent = getIntent();
        uid = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신
        storeid = intent.getExtras().getInt("storeid");    //지점 아이디 데이터 수신
        flag = intent.getExtras().getString("flag");    //진입 화면 경로 구분 값 수신
        searchValue = intent.getExtras().getString("searchValue");    //검색할 데이터 값 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        selectData();

        ProductListActivity.CustomList adapter = new ProductListActivity.CustomList(ProductListActivity.this);
        listItem = (ListView) findViewById(R.id.list_alam);
        listItem.setAdapter(adapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("id", uid);                //로그인한 사용자 회원번호 전송
                intent.putExtra("productid", vcProductid.get(+position));       //선택된 제품 아이디 전송
                startActivity(intent);  //제품상세정보 화면으로 이동
            }
        });

    }

    //제품리스트 데이터 조회 함수
    private void selectData() {
        Log.d("ProductListActivity", "데이터베이스 조회 시작");
        try {
            String searchSQL = "";
            Create_DB dbHelper = new Create_DB(this, "paradao", null, 1);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if(flag.equals("search")) {
                searchValue = searchValue.toUpperCase(Locale.ROOT);
                Log.d("ProductListActivity", "searchValue : " + searchValue);
                searchSQL = "WHERE product.storeid = store.storeid AND (productname like '%" + searchValue + "%' OR storename like '%" + searchValue + "%')";
            } else {
                searchSQL = "WHERE product.storeid = store.storeid AND product.storeid = " + storeid;
            }
            Cursor c = db.rawQuery("SELECT storename, productname, productstock, productimg, productid FROM product, store " + searchSQL, null);
            while(c.moveToNext()) {
                Log.d("ProductListActivity", c.getString(1));
                ListProductData listdata = new ListProductData(); //리스트에 담을 정보 데이터 객체 생성

                listdata.setStoreName(c.getString(0));    //매장명 Value 값을 listdata 객체에 set
                listdata.setProductName(c.getString(1));    //제품명 Value 값을 listdata 객체에 set
                listdata.setQuantity(c.getString(2));  //재고량 Value 값을 listdata 객체에 set
                listdata.setImage(String.valueOf(getResources().getIdentifier(c.getString(3), "drawable", getPackageName()))); //제품 이미지 경로 값을 listdata 객체에 set

                vcStoreName.add(c.getString(0));        //검색된 매장명 저장
                vcProductName.add(c.getString(1));      //검색된 제품명 저장
                vcProductid.add(c.getInt(4));            //검색된 제품 아이디 저장

                list.add(listdata); //list에 listdata 객체를 add
            }
            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "서버 접속에 실패했습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Log.d("ProductListActivity", "데이터베이스 조회 완료");
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
