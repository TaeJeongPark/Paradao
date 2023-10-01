package com.example.paradao;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;

/**
 * @packageName	: com.example.paradao
 * @fileName	: kakaoApplication.java
 * @author		: TaeJeong Park
 * @date		: 2022.11.16
 * @description	: 카카오 SDK 초기화
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.11.16		TaeJeong Park		최초 생성
 * 2022.11.16		TaeJeong Park		기능 구현 완료
 */
public class kakaoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //카카오 SDK 초기화
        KakaoSdk.init(this, "86baa88d6fc3ef3de4743830f490509a");
    }

}
