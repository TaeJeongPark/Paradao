package com.example.paradao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * @packageName	: com.example.paradao
 * @fileName	: MyActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 마이페이지 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class MyActivity extends AppCompatActivity {

    String id;  //사용자 아이디
    String nickname;    //사용자 닉네임

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");              //사용자 아이디 데이터 수신
        nickname = intent.getExtras().getString("nickname");  //사용자 닉네임 데이터 수신

        //타이틀 바 로고 설정
        getSupportActionBar().setIcon(R.drawable.paladao_logo2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView tf_nickname = (TextView) findViewById(R.id.tf_nickname);
        tf_nickname.setText(nickname + " 님 안녕하세요.");

        ImageButton btnLogout = (ImageButton) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });

        ImageButton btnExit = (ImageButton) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exit();
            }
        });

    }

    //로그아웃
    private void logout() {
        String TAG = "logout()";
        /*UserApiClient.getInstance().logout(error -> {
            if (error != null) {
                Log.e(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
            }else {
                Log.e(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);    //LoginActivity 인텐트 생성
                startActivity(intent);  //로그인 화면으로 이동
                finish();
            }
            return null;
        });*/

        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);    //LoginActivity 인텐트 생성
                startActivity(intent);  //로그인 화면으로 이동
                finish();

                return null;
            }
        });
    }

    //회원탈퇴
    private void exit() {
        String TAG = "logout()";
        /*UserApiClient.getInstance().unlink(error -> {
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error);
            } else {
                Log.e(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);    //LoginActivity 인텐트 생성
                startActivity(intent);  //로그인 화면으로 이동
                finish();
            }
            return null;
        });*/

        UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);    //LoginActivity 인텐트 생성
                startActivity(intent);  //로그인 화면으로 이동
                finish();

                return null;
            }
        });
    }
}
