package com.example.paradao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @packageName	: com.example.paradao
 * @fileName	: SplashActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.11.16
 * @description	: 스플래시 스크린 동작을 위한 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.11.16		TaeJeong Park		최초 생성
 * 2022.11.16		TaeJeong Park		기능 구현 완료
 */
public class SplashActivity extends Activity {

    protected void onCreate(Bundle savedlnstanceState) {

        super.onCreate(savedlnstanceState);

        try {
            Thread.sleep(2000); //2초간 스플래시 화면 노출
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, LoginActivity.class);   //로그인 액티비티를 인텐트 변수에 대입

        startActivity(intent);  //로그인 화면으로 이동

        finish();   //현재 액티비티 종료

    }
    
}
