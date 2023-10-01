package com.example.paradao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @packageName	: com.example.paradao
 * @fileName	: MainActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.11.16
 * @description	: 홈 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.11.16		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    private int[] images = new int[3];                  //이미지 슬라이더에 담을 이미지를 저장하는 배열

    //DB 생성
    SQLiteDatabase db;

    //사용자 정보
    String id;
    String nickname;

    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Create_DB helper = new Create_DB(this, "paradao", null, 1); //데이터베이스 생성
        db = helper.getWritableDatabase();

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신
        nickname = intent.getExtras().getString("nickname");  //사용자 닉네임 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //배너 이미지 슬라이더 세팅
        setBanner();

        sliderViewPager = findViewById(R.id.sliderViewPager);   //슬라이더 뷰페이저 생성
        layoutIndicator = findViewById(R.id.layoutIndicators);  //인디케이터 리니어레이아웃 생성

        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));

        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        setupIndicators(3);

        ImageButton btn_gs = (ImageButton) findViewById(R.id.btn_gs);
        btn_gs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 1);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btn_cu = (ImageButton) findViewById(R.id.btn_cu);
        btn_cu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 2);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btn_seven = (ImageButton) findViewById(R.id.btn_seven);
        btn_seven.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 3);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btn_homplus = (ImageButton) findViewById(R.id.btn_homplus);
        btn_homplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 4);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btn_emart = (ImageButton) findViewById(R.id.btn_emart);
        btn_emart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 5);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btn_nike = (ImageButton) findViewById(R.id.btn_nike);
        btn_nike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 6);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btn_onnuri = (ImageButton) findViewById(R.id.btn_onnuri);
        btn_onnuri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);    //StoreListActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("brandid", 7);       //선택된 브랜드 아이디 전송
                startActivity(intent);  //매장리스트 화면으로 이동
            }
        });

        ImageButton btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                searchStart();  //조회 시작
            }
        });

        EditText tfSearch = (EditText) findViewById(R.id.tf_search);
        tfSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("MainActivity", String.valueOf(i) + ", " + keyEvent);
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && cnt % 2 == 0) searchStart();  //조회 시작
                cnt++;
                return false;
            }
        });

    }

    private void searchStart() {

        EditText searchStr = (EditText) findViewById(R.id.tf_search);       //검색어 텍스트필드
        Log.d("MainActivity", searchStr.getText().toString());

        Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);    //ProductListActivity 인텐트 생성
        intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
        intent.putExtra("searchValue", searchStr.getText().toString()); //검색할 값 송신
        intent.putExtra("flag", "search"); //진입 화면 경로 구분 값 송신

        searchStr.setText("");  //검색 텍스트필드 초기화

        startActivity(intent);  //통합검색결과리스트 화면으로 이동

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.popup, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        Intent intent;

        switch(item.getItemId())
        {
            case R.id.btn_goalam:
                intent = new Intent(getApplicationContext(), AlamActivity.class);    //AlamActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                startActivity(intent);  //입고알림 화면으로 이동

                break;
            case R.id.btn_goreservation:
                intent = new Intent(getApplicationContext(), ReservationActivity.class);    //ReservationActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                startActivity(intent);  //예약확인 화면으로 이동

                break;
            case R.id.btn_gocart:
                intent = new Intent(getApplicationContext(), CartActivity.class);    //CartActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                startActivity(intent);  //장바구니 화면으로 이동

                break;
            case R.id.btn_gomy:
                intent = new Intent(getApplicationContext(), MyActivity.class);    //CartActivity 인텐트 생성
                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("nickname", nickname);    //로그인한 사용자 닉네임 전송
                startActivity(intent);  //마이페이지 화면으로 이동

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //배너 이미지 세팅 함수
    private void setBanner() {
        images[0] = getResources().getIdentifier("banner1", "drawable", getPackageName());
        images[1] = getResources().getIdentifier("banner3", "drawable", getPackageName());
        images[2] = getResources().getIdentifier("banner2", "drawable", getPackageName());
    }

    //인디케이터 세팅 함수
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    //현재 선택된 인디케이터 세팅 함수
    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

}