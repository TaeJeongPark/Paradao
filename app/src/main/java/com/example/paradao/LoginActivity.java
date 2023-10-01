package com.example.paradao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @packageName	: com.example.paradao
 * @fileName	: LoginActivity.java
 * @author		: TaeJeong Park
 * @date		: 2022.11.16
 * @description	: 로그인 화면
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.11.16		TaeJeong Park		최초 생성
 * 2022.11.17		TaeJeong Park		기능 구현 완료
 */
public class LoginActivity extends AppCompatActivity {

    //DB 생성
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Create_DB helper = new Create_DB(this, "paradao", null, 1); //데이터베이스 생성
        db = helper.getWritableDatabase();

        Log.d("KeyHash", getKeyHash());

        ImageButton kakao_login_button = (ImageButton)findViewById(R.id.kakao_login_button);
        kakao_login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                    login();
                }
                else{
                    accountLogin();
                }
            }
        });
    }

    private void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo(1);
            }
            return null;
        });
    }

    private void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo(0);
            }
            return null;
        });
    }

    private void getUserInfo(int flag){
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: "+ user.getId() +
                            "\n이름: "+ user.getKakaoAccount().getProfile().getNickname());
                }
                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user1);

                String id = String.valueOf(user.getId());
                String nickname = user.getKakaoAccount().getProfile().getNickname();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);    //MainActivity 인텐트 생성

                intent.putExtra("id", id);                //로그인한 사용자 회원번호 전송
                intent.putExtra("nickname", nickname);        //로그인한 사용자 이름 전송

                //회원가입 유저인 경우 회원 정보 DB에 insert
                if(flag == 0) db.execSQL("INSERT INTO member (id, nickname) VAlUES(id, nickname)");

                startActivity(intent);  //홈 화면으로 이동
            }
            return null;
        });
    }

    // 키해시 얻기
    private String getKeyHash(){
        try{
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if(packageInfo == null) return null;
            for(Signature signature: packageInfo.signatures){
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                }catch (NoSuchAlgorithmException e){
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
                }
            }
        }catch(PackageManager.NameNotFoundException e){
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }

}
