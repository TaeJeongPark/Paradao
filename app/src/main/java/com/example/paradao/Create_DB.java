package com.example.paradao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @packageName	: com.example.paradao
 * @fileName	: Create_DB.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.13
 * @description	: 데이터베이스 접근 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.13		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class Create_DB extends SQLiteOpenHelper {

    private static SQLiteDatabase db;
    private static String DB_PATH = "";
    private static String DB_NAME = "paradao.db";
    private Context mContext;

    public Create_DB(Context context, String paradao, Object o, int i) {
        super(context, DB_NAME, null, 1);

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        dataBaseCheck();
    }

    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d("Create_DB","Database is copied.");
        }
    }

    //db를 assets에서 복사해온다.
    private void dbCopy() {

        try {
            File folder = new File(DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String out_filename = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer,0,mLength);
            }
            outputStream.flush();;
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Create_DB","IOException 발생");
        }
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
        super.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("Create_DB","onOpen() : DB Opening!");
    }

    @Override
    public void onCreate(SQLiteDatabase dbIn) {

        db = dbIn;

        Log.d("Create_DB", "데이터베이스 생성 시작");

        //브랜드 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS brand (brandid integer PRIMARY KEY AUTOINCREMENT, brandname text, brandlogoimg text)");

        //지점 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS store (storeid integer PRIMARY KEY AUTOINCREMENT, brandid integer, storename text, storestarttime text, storeendtime text, storetel text, storeaddr text, storeimg text, FOREIGN KEY(brandid) REFERENCES brand(brandid))");

        //제품 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS product (productid integer PRIMARY KEY AUTOINCREMENT, storeid integer, brandid integer, productname text, productprice integer, productstock integer, productevent text, productsupply text, productimg text, FOREIGN KEY(storeid) REFERENCES store(storeid), FOREIGN KEY(brandid) REFERENCES brand(brandid))");

        //회원 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS member (id text PRIMARY KEY, nickname text)");

        //장바구니 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS cart (cartid integer PRIMARY KEY AUTOINCREMENT, id text, productid integer, storeid integer, brandid integer, FOREIGN KEY(id) REFERENCES member(id), FOREIGN KEY(productid) REFERENCES product(productid), FOREIGN KEY(storeid) REFERENCES store(storeid), FOREIGN KEY(brandid) REFERENCES brand(brandid))");

        //예약 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS reservation (reservationid integer PRIMARY KEY AUTOINCREMENT, id text, productid integer, storeid integer, brandid integer, starttime datetime, endtime datetime, FOREIGN KEY(id) REFERENCES member(id), FOREIGN KEY(productid) REFERENCES product(productid), FOREIGN KEY(storeid) REFERENCES store(storeid), FOREIGN KEY(brandid) REFERENCES brand(brandid))");

        //입고알림 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS notice (noticeid integer PRIMARY KEY AUTOINCREMENT, id text, productid integer, storeid integer, brandid integer, FOREIGN KEY(id) REFERENCES member(id), FOREIGN KEY(productid) REFERENCES product(productid), FOREIGN KEY(storeid) REFERENCES store(storeid), FOREIGN KEY(brandid) REFERENCES brand(brandid))");


        //브랜드 테이블 insert
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(1, 'GS25', 'gs')");
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(2, 'CU', 'cu')");
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(3, '7ELEVEN', 'seven')");
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(4, 'Homeplus', 'homepluse')");
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(5, 'emart', 'emart')");
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(6, 'NIKE', 'nike')");
        db.execSQL("INSERT INTO brand (brandid, brandname, brandlogoimg) VAlUES(7, '온누리약국', 'onnuri')");


        //지점 테이블 insert
        //GS25
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(1, 1, 'GS25 인하대후문점', '07:00', '24:00', '032-868-1336', '인천 미추홀구 인하로 51 대원빌라', 'gs_store_inhabackgate')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(2, 1, 'GS25 용현사거리점', '00:00', '24:00', '032-884-8036', '인천 미추홀구 인주대로 129 (용현동 459-53)', 'gs_store_yonghyeonintersection')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(3, 1, 'GS25 인천문화점', '00:00', '24:00', '032-874-6763', '인천 미추홀구 재넘이길 6', 'gs_store_incheonculture')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(4, 1, 'GS25 송도커낼그린점', '06:00', '24:00', '032-884-8036', '인천광역시 연수구 아트센터대로 87 402동 163호', 'gs_store_songdocnwk')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(5, 1, 'GS25 송도더샵점', '00:00', '24:00', '032-834-2439', '인천광역시 연수구 아트센터대로97번길 75', 'gs_store_songdotheshop')");

        //CU
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(6, 2, 'CU 인하대학교점', '07:00', '24:00', '032-876-0574', '인천 미추홀구 인하로 77 (용현동)', 'cu_store_inha')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(7, 2, 'CU 용현정석2호점', '06:00', '24:00', '032-862-5734', '인천 미추홀구 경인남길30번길 104', 'cu_store_yonghyeonjeongseok')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(8, 2, 'CU 학익드림점', '00:00', '24:00', '070-4383-5813', '인천 미추홀구 학익소로 5', 'cu_store_hakikdream')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(9, 2, 'CU 송도자이점', '00:00', '24:00', '032-834-8552', '인천광역시 연수구 컨벤시아대로130번길 58', 'cu_store_songdozai')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(10, 2, 'CU 송도에이스점', '00:00', '24:00', '032-831-8093', '인천광역시 연수구 컨벤시아대로42번길 27 송도에이스프라자Ⅱ', 'cu_store_songdoace')");

        //7ELEVEN
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(11, 3, '세븐일레븐 인하드림점', '00:00', '24:00', '032-213-5412', '인천 미추홀구 용정공원로 83번길 59', 'seven_store_inhadream')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(12, 3, '세븐일레븐 인하대역점', '00:00', '24:00', '032-887-0848', '인천 미추홀구 독배로 311', 'seven_store_inhasubway')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(13, 3, '세븐일레븐 인하대문화점', '00:00', '24:00', '032-152-1226', '인천 미추홀구 경인남길30번길 66', 'seven_store_inhamunhwa')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(14, 3, '세븐일레븐 송도포스코점', '00:00', '24:00', '032-851-7731', '인천광역시 연수구 컨벤시아대로42번길 77', 'seven_store_songdoposco')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(15, 3, '세븐일레븐 송도아데니움', '00:00', '24:00', '070-8192-8888', '인천광역시 연수구 신송로 164', 'seven_store_songdoadenium')");

        //homepluse
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(16, 4, '홈플러스 송도점', '10:00', '24:00', '1577-3355', '인천 연수구 송도동 168-3', 'homepluse_store_songdo')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(17, 4, '홈플러스 인하점', '10:00', '24:00', '1577-3355', '인천 남구 용현1.4동 소성로 6', 'homepluse_store_inha')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(18, 4, '홈플러스 인천숭의점', '10:00', '24:00', '1577-3355', '인천 남구 석정로 51', 'homepluse_store_soong')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(19, 4, '홈플러스 구월점', '10:00', '24:00', '1577-3355', '인천 남동구 구월1동 1130', 'homepluse_store_guworl')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(20, 4, '홈플러스 인천논현점', '10:00', '24:00', '1577-3355', '인천 남동구 청능대로 596', 'homepluse_store_nonhyen')");

        //emart
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(21, 5, '이마트 연수점', '10:00', '24:00', '032-820-1234', '인천 연수구 동춘동 926-9', 'emart_store_yeonsu')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(22, 5, '이마트 인천마트점', '10:00', '24:00', '032-430-1234', '인천 남구 관교동 15번지', 'emart_store_incheon')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(23, 5, '이마트 동인천점', '10:00', '24:00', '032-451-1234', '인천 중구 인중로 134', 'emart_store_dongincheon')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(24, 5, '이마트 부천점', '10:00', '24:00', '032-610-5123', '경기도 부천시 부천로 1', 'emart_store_bucheon')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(25, 5, '이마트 계양점', '10:00', '24:00', '032-717-1234', '인천 계양구 봉오대로 785', 'emart_store_geyang')");

        //nike
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(26, 6, '나이키 인천구월점', '10:30', '22:00', '032-435-2094', '인천 남동구 성말로 9', 'nike_store_guworl')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(27, 6, '나이키 신포점', '10:30', '22:00', '032-773-2094', '인천 중구 신생동 2-24', 'nike_store_shinpo')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(28, 6, '나이키 롯데인천점', '10:30', '21:00', '032-450-2648', '인천 남동구 구월동 1455', 'nike_store_lotteincheon')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(29, 6, '나이키 세이브존상동점', '10:30', '21:00', '032-321-1979', '경기도 부천시 원미구 상3동 535-5', 'nike_store_savezonesangdong')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(30, 6, '나이키 홈플러스부천상동점', '10:30', '22:00', '032-650-8072', '경기도 부천시 원미구 상2동 540-1', 'nike_store_bucheonsangdong')");

        //onnuri
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(31, 7, '건강온누리약국', '08:00', '20:00', '032-815-2660', '인천 연수구 연수동 598', 'onnuri_store_rjsrkd')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(32, 7, '자연온누리약국', '08:00', '19:00', '032-831-7545', '인천 연수구 옥련동 304-12', 'onnuri_store_wkdus')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(33, 7, '둥지온누리약국', '09:00', '20:00', '032-818-5549', '인천 연수구 청학동 함박뫼로 26', 'onnuri_store_endwl')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(34, 7, '온누리동아약국', '08:00', '21:00', '032-812-7582', '인천 연수구 동춘2동 933-1', 'onnuri_store_ehddk')");
        db.execSQL("INSERT INTO store (storeid, brandid, storename, storestarttime, storeendtime, storetel, storeaddr, storeimg) VAlUES(35, 7, '온누리현대약국', '07:00', '21:00', '032-831-4822', '인천 연수구 옥련동 462-136', 'onnuri_store_guseo')");


        //제품 테이블 insert
        //GS25
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포테이토버거', 3400, 3, '풀)청양참치마요삼각김밥 증정', '불가능', 'gs_photatobuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '매점왕)치즈벅', 2500, 6, '10% 할인', '불가능', 'gs_cheesebuck')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '처갓집)양념치킨버거', 3900, 4, '2+1', '불가능', 'gs_seasonedchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '스팸에그버거', 3400, 5, '1+1', '불가능', 'gs_spameggburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '코리안좀비버거', 3300, 3, '10% 할인', '불가능', 'gs_koreanzombieburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '빅)네슈빌핫치킨버거', 3400, 3, '2+1', '불가능', 'gs_nashvillehotchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '빅)통다리살치킨버거', 3900, 1, '풀)청양참치마요삼각김밥 증정', '불가능', 'gs_wholelegschickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '삼립)어니언스테이크치즈버거', 2700, 3, '1+1', '불가능', 'gs_onionsteakcheeseburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '삼립)NEW핫N스모키더블버거', 2300, 6, '행사없음', '불가능', 'gs_hotsmokeydoubleburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, 'NEW)데리버거', 1900, 0, '10% 할인', '불가능', 'gs_pickupburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, 'NEW빅&더블버거', 3300, 4, '2+1', '불가능', 'gs_bigdoubleburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포테이토베이컨샌드위치', 2500, 3, '1+1', '불가능', 'gs_potatobaconsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '크랜베리치킨샌드위치', 2700, 0, '행사없음', '불가능', 'gs_cranberrychickensandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '초코랑아몬드랑샌드위치1', 2500, 3, '행사없음', '불가능', 'gs_chocolateandalmondsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '민트랑초코랑샌드위치', 3400, 4, '2+1', '1+1', 'gs_mintandchocolatesandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, 'K-B.E.L.T샌드위치', 2500, 2, '1+1', '불가능', 'gs_kbeltsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, 'B.E.L.T샌드위치', 2800, 1, '행사없음', '불가능', 'gs_beltsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '실속)삼겹왕김밥', 2700, 4, '1+1', '불가능', 'gs_samgyeopsalkinggimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '실속)정통왕김밥', 2000, 1, '10% 할인', '불가능', 'gs_authentickinggimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '풀)청양참치마요삼각김밥', 1500, 6, '2+1', '불가능', 'gs_cheongyangtuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '풀)베이컨참치마요삼각김밥', 1500, 0, '1+1', '불가능', 'gs_bacontunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '풀)스팸김치볶음밥삼각김밥', 1600, 1, '2+1', '불가능', 'gs_spamkimchifriedrice')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 돌아온 로켓단 초코롤 85g', 1500, 0, '행사없음', '불가능', 'spc_chocolateroll')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 0, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 0, '행사없음', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 디그다 딸기카스타드빵 90g', 1500, 1, '행사없음', '불가능', 'spc_strawberrycustard')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 꼬부기의 달콤파삭 꼬부기빵 75g', 1500, 1, '행사없음', '불가능', 'spc_squidbread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 푸린의 폭신폭신 딸기크림빵 100g', 1500, 0, '10% 할인', '불가능', 'spc_strawberrycreambread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '포켓몬빵 피카피카 촉촉 치즈케익 80g', 1500, 1, '행사없음', '불가능', 'spc_cheesecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '크라운제과 죠리퐁 165g', 1500, 13, '행사없음', '불가능', 'snack_jorypong')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오리온 닥터유 다이제 초코 225g', 1600, 17, '10% 할인', '불가능', 'snack_daizechocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '서울식품공업 뻥이요 골드 60g', 680, 27, '행사없음', '불가능', 'snack_thatsbullshit')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '해태제과 홈런볼 초코 46g', 950, 12, '행사없음', '불가능', 'snack_homerunballchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오리온 오징어 땅콩 98g', 1200, 16, '10% 할인', '불가능', 'snack_squidpeanuts')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '농심 포스틱 270g', 3500, 1, '행사없음', '불가능', 'snack_postick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '농심 새우깡 400g', 3100, 14, '행사없음', '불가능', 'snack_shrimpcrackers')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '롯데제과 빈츠 24개입 204g', 3500, 8, '행사없음', '불가능', 'snack_binz')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오리온 꼬북칩 스윗바닐라맛 136g', 2100, 1, '10% 할인', '불가능', 'snack_kkobukchipsweetvanilla')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '크라운제과 뽀로로와 친구들 플레인 65g', 800, 13, '행사없음', '불가능', 'snack_pororo')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오리온 태양의 맛! 돌아온 썬 오리지널 135g', 1800, 1, '행사없음', '불가능', 'snack_sun')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '크라운제과 참쌀 설병 270g', 3000, 3, '행사없음', '불가능', 'gs_photatobuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오리온 눈을 감자 페퍼솔트맛 56g', 900, 35, '10% 할인', '불가능', 'snack_closeyoureyes')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '농심 바나나킥 75g', 1200, 12, '행사없음', '불가능', 'snack_bananakick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '허쉬 초콜릿 칩 모찌 쿠키 12개입 240g', 3500, 33, '행사없음', '불가능', 'snack_hersheyschocolatechip')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오뚜기 뿌셔뿌셔 바베큐맛 90g', 500, 32, '10% 할인', '불가능', 'snack_sprinkle')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '광일제과 꾀돌이 45g', 300, 21, '행사없음', '불가능', 'snack_prankster')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '해태제과 버터링 딥초코 155g', 4200, 7, '행사없음', '불가능', 'snack_butteringdeepchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '크라운제과 버터와플 11개입 316g', 3400, 3, '행사없음', '불가능', 'snack_butterwaffles')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(1, 1, '오리온 초코송이 144g', 1200, 3, '10% 할인', '불가능', 'snack_chocolatebunch')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '실속)정통왕김밥', 2000, 4, '행사없음', '불가능', 'gs_authentickinggimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '풀)청양참치마요삼각김밥', 1500, 2, '2+1', '불가능', 'gs_cheongyangtuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '풀)베이컨참치마요삼각김밥', 1500, 1, '1+1', '불가능', 'gs_bacontunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '풀)스팸김치볶음밥삼각김밥', 1600, 3, '2+1', '불가능', 'gs_spamkimchifriedrice')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 돌아온 로켓단 초코롤 85g', 1500, 2, '10% 할인', '불가능', 'spc_chocolateroll')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 3, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 1, '10% 할인', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 디그다 딸기카스타드빵 90g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycustard')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 꼬부기의 달콤파삭 꼬부기빵 75g', 1500, 0, '행사없음', '불가능', 'spc_squidbread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 푸린의 폭신폭신 딸기크림빵 100g', 1500, 3, '행사없음', '불가능', 'spc_strawberrycreambread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '포켓몬빵 피카피카 촉촉 치즈케익 80g', 1500, 2, '행사없음', '불가능', 'spc_cheesecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(2, 1, '크라운제과 죠리퐁 165g', 1500, 5, '행사없음', '불가능', 'snack_jorypong')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '포켓몬빵 피카피카 촉촉 치즈케익 80g', 1500, 6, '행사없음', '불가능', 'spc_cheesecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '크라운제과 죠리퐁 165g', 1500, 4, '행사없음', '불가능', 'snack_jorypong')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '오리온 닥터유 다이제 초코 225g', 1600, 3, '행사없음', '불가능', 'snack_daizechocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '서울식품공업 뻥이요 골드 60g', 680, 6, '행사없음', '불가능', 'snack_thatsbullshit')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '해태제과 홈런볼 초코 46g', 950, 8, '10% 할인', '불가능', 'snack_homerunballchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '오리온 오징어 땅콩 98g', 1200, 6, '행사없음', '불가능', 'snack_squidpeanuts')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '농심 포스틱 270g', 3500, 4, '행사없음', '불가능', 'snack_postick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '농심 새우깡 400g', 3100, 7, '10% 할인', '불가능', 'snack_shrimpcrackers')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '롯데제과 빈츠 24개입 204g', 3500, 42, '행사없음', '불가능', 'snack_binz')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '오리온 꼬북칩 스윗바닐라맛 136g', 2100, 4, '행사없음', '불가능', 'snack_kkobukchipsweetvanilla')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '크라운제과 뽀로로와 친구들 플레인 65g', 800, 26, '행사없음', '불가능', 'snack_pororo')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '오리온 태양의 맛! 돌아온 썬 오리지널 135g', 1800, 34, '행사없음', '불가능', 'snack_sun')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '크라운제과 참쌀 설병 270g', 3000, 9, '행사없음', '불가능', 'gs_photatobuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(3, 1, '오리온 눈을 감자 페퍼솔트맛 56g', 900, 12, '행사없음', '불가능', 'snack_closeyoureyes')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '코리안좀비버거', 3300, 8, '행사없음', '불가능', 'gs_koreanzombieburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '빅)네슈빌핫치킨버거', 3400, 4, '2+1', '불가능', 'gs_nashvillehotchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '빅)통다리살치킨버거', 3900, 6, '풀)청양참치마요삼각김밥 증정', '불가능', 'gs_wholelegschickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '삼립)어니언스테이크치즈버거', 2700, 7, '1+1', '불가능', 'gs_onionsteakcheeseburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '삼립)NEW핫N스모키더블버거', 2300, 2, '10% 할인', '불가능', 'gs_hotsmokeydoubleburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, 'NEW)데리버거', 1900, 3, '행사없음', '불가능', 'gs_pickupburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, 'NEW빅&더블버거', 3300, 5, '2+1', '불가능', 'gs_bigdoubleburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '포테이토베이컨샌드위치', 2500, 3, '1+1', '불가능', 'gs_potatobaconsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '크랜베리치킨샌드위치', 2700, 2, '10% 할인', '불가능', 'gs_cranberrychickensandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '초코랑아몬드랑샌드위치1', 2500, 5, '행사없음', '불가능', 'gs_chocolateandalmondsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '민트랑초코랑샌드위치', 3400, 7, '2+1', '1+1', 'gs_mintandchocolatesandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, 'K-B.E.L.T샌드위치', 2500, 14, '1+1', '불가능', 'gs_kbeltsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, 'B.E.L.T샌드위치', 2800, 12, '행사없음', '불가능', 'gs_beltsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '실속)삼겹왕김밥', 2700, 1, '1+1', '불가능', 'gs_samgyeopsalkinggimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '실속)정통왕김밥', 2000, 12, '행사없음', '불가능', 'gs_authentickinggimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(4, 1, '풀)청양참치마요삼각김밥', 1500, 4, '2+1', '불가능', 'gs_cheongyangtuna')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '농심 포스틱 270g', 3500, 4, '행사없음', '불가능', 'snack_postick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '농심 새우깡 400g', 3100, 3, '행사없음', '불가능', 'snack_shrimpcrackers')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '롯데제과 빈츠 24개입 204g', 3500, 12, '행사없음', '불가능', 'snack_binz')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '오리온 꼬북칩 스윗바닐라맛 136g', 2100, 16, '행사없음', '불가능', 'snack_kkobukchipsweetvanilla')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '크라운제과 뽀로로와 친구들 플레인 65g', 800, 18, '행사없음', '불가능', 'snack_pororo')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '오리온 태양의 맛! 돌아온 썬 오리지널 135g', 1800, 3, '행사없음', '불가능', 'snack_sun')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '크라운제과 참쌀 설병 270g', 3000, 13, '행사없음', '불가능', 'gs_photatobuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '오리온 눈을 감자 페퍼솔트맛 56g', 900, 14, '행사없음', '불가능', 'snack_closeyoureyes')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '농심 바나나킥 75g', 1200, 12, '10% 할인', '불가능', 'snack_bananakick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '허쉬 초콜릿 칩 모찌 쿠키 12개입 240g', 3500, 17, '행사없음', '불가능', 'snack_hersheyschocolatechip')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '오뚜기 뿌셔뿌셔 바베큐맛 90g', 500, 4, '행사없음', '불가능', 'snack_sprinkle')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '광일제과 꾀돌이 45g', 300, 6, '행사없음', '불가능', 'snack_prankster')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '해태제과 버터링 딥초코 155g', 4200, 13, '행사없음', '불가능', 'snack_butteringdeepchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '크라운제과 버터와플 11개입 316g', 3400, 17, '행사없음', '불가능', 'snack_butterwaffles')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(5, 1, '오리온 초코송이 144g', 1200, 18, '행사없음', '불가능', 'snack_chocolatebunch')");

        //CU
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '햄)빅통모짜비프버거', 3500, 6, '행사없음', '불가능', 'cu_bigtongmozzabeefburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '햄)매콤판타스틱치킨버거', 3700, 3, '에비앙 350ml 증정', '불가능', 'cu_spicyfantasticchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '햄)백종원트리플불고기버거', 3400, 3, '행사없음', '불가능', 'cu_baekjongwontriplebulgogi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '햄)통새우치즈버거', 2800, 3, '행사없음', '불가능', 'cu_wholeshrimpcheeseburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '샌)백종원콘치즈참치샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_baekjongwoncorncheesetuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '샌)백종원콘치즈감자샌드위치', 2500, 3, '행사없음', '불가능', 'cu_baekjongwoncorncheesepotato')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '샌)점보4단칠리크랩샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_jumbo4tierchilicrab')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '김)확실한참치김밥(신)', 2800, 3, '10% 할인', '불가능', 'cu_suretunakimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '김)확실한제육김밥(신)', 2800, 3, '행사없음', '불가능', 'cu_surefiredkimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '김)확실한불고기김밥(신)', 2800, 3, '행사없음', '불가능', 'cu_surebulgogigimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 돌아온 로켓단 초코롤 85g', 1500, 2, '10% 할인', '불가능', 'spc_chocolateroll')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 3, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 1, '10% 할인', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 디그다 딸기카스타드빵 90g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycustard')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 꼬부기의 달콤파삭 꼬부기빵 75g', 1500, 0, '행사없음', '불가능', 'spc_squidbread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 푸린의 폭신폭신 딸기크림빵 100g', 1500, 3, '행사없음', '불가능', 'spc_strawberrycreambread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(6, 2, '포켓몬빵 피카피카 촉촉 치즈케익 80g', 1500, 2, '행사없음', '불가능', 'spc_cheesecake')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '허쉬 초콜릿 칩 모찌 쿠키 12개입 240g', 3500, 17, '행사없음', '불가능', 'snack_hersheyschocolatechip')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '오뚜기 뿌셔뿌셔 바베큐맛 90g', 500, 4, '행사없음', '불가능', 'snack_sprinkle')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '광일제과 꾀돌이 45g', 300, 6, '행사없음', '불가능', 'snack_prankster')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '해태제과 버터링 딥초코 155g', 4200, 13, '행사없음', '불가능', 'snack_butteringdeepchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '크라운제과 버터와플 11개입 316g', 3400, 17, '행사없음', '불가능', 'snack_butterwaffles')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '오리온 초코송이 144g', 1200, 18, '행사없음', '불가능', 'snack_chocolatebunch')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '햄)매콤판타스틱치킨버거', 3700, 3, '에비앙 350ml 증정', '불가능', 'cu_spicyfantasticchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '햄)백종원트리플불고기버거', 3400, 3, '행사없음', '불가능', 'cu_baekjongwontriplebulgogi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '햄)통새우치즈버거', 2800, 3, '행사없음', '불가능', 'cu_wholeshrimpcheeseburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '샌)백종원콘치즈참치샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_baekjongwoncorncheesetuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 3, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 1, '10% 할인', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '포켓몬빵 디그다 딸기카스타드빵 90g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycustard')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(7, 2, '포켓몬빵 꼬부기의 달콤파삭 꼬부기빵 75g', 1500, 0, '행사없음', '불가능', 'spc_squidbread')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '햄)매콤판타스틱치킨버거', 3700, 3, '에비앙 350ml 증정', '불가능', 'cu_spicyfantasticchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '햄)백종원트리플불고기버거', 3400, 3, '행사없음', '불가능', 'cu_baekjongwontriplebulgogi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '햄)통새우치즈버거', 2800, 3, '행사없음', '불가능', 'cu_wholeshrimpcheeseburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '샌)백종원콘치즈참치샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_baekjongwoncorncheesetuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '샌)백종원콘치즈감자샌드위치', 2500, 3, '행사없음', '불가능', 'cu_baekjongwoncorncheesepotato')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '샌)점보4단칠리크랩샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_jumbo4tierchilicrab')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '크라운제과 참쌀 설병 270g', 3000, 13, '행사없음', '불가능', 'gs_photatobuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '오리온 눈을 감자 페퍼솔트맛 56g', 900, 14, '행사없음', '불가능', 'snack_closeyoureyes')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '농심 바나나킥 75g', 1200, 12, '10% 할인', '불가능', 'snack_bananakick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '허쉬 초콜릿 칩 모찌 쿠키 12개입 240g', 3500, 17, '행사없음', '불가능', 'snack_hersheyschocolatechip')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '오뚜기 뿌셔뿌셔 바베큐맛 90g', 500, 4, '행사없음', '불가능', 'snack_sprinkle')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '광일제과 꾀돌이 45g', 300, 6, '행사없음', '불가능', 'snack_prankster')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '해태제과 버터링 딥초코 155g', 4200, 13, '행사없음', '불가능', 'snack_butteringdeepchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '크라운제과 버터와플 11개입 316g', 3400, 17, '행사없음', '불가능', 'snack_butterwaffles')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(8, 2, '오리온 초코송이 144g', 1200, 18, '행사없음', '불가능', 'snack_chocolatebunch')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '샌)백종원콘치즈참치샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_baekjongwoncorncheesetuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '샌)백종원콘치즈감자샌드위치', 2500, 3, '행사없음', '불가능', 'cu_baekjongwoncorncheesepotato')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '샌)점보4단칠리크랩샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_jumbo4tierchilicrab')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '김)확실한참치김밥(신)', 2800, 3, '10% 할인', '불가능', 'cu_suretunakimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '김)확실한제육김밥(신)', 2800, 3, '행사없음', '불가능', 'cu_surefiredkimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '김)확실한불고기김밥(신)', 2800, 3, '행사없음', '불가능', 'cu_surebulgogigimbap')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '포켓몬빵 돌아온 로켓단 초코롤 85g', 1500, 2, '10% 할인', '불가능', 'spc_chocolateroll')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 3, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '샌)백종원콘치즈감자샌드위치', 2500, 3, '행사없음', '불가능', 'cu_baekjongwoncorncheesepotato')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '샌)점보4단칠리크랩샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_jumbo4tierchilicrab')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '크라운제과 참쌀 설병 270g', 3000, 13, '행사없음', '불가능', 'gs_photatobuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '오리온 눈을 감자 페퍼솔트맛 56g', 900, 14, '행사없음', '불가능', 'snack_closeyoureyes')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(9, 2, '농심 바나나킥 75g', 1200, 12, '10% 할인', '불가능', 'snack_bananakick')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '크라운제과 버터와플 11개입 316g', 3400, 17, '행사없음', '불가능', 'snack_butterwaffles')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '오리온 초코송이 144g', 1200, 18, '행사없음', '불가능', 'snack_chocolatebunch')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '햄)매콤판타스틱치킨버거', 3700, 3, '에비앙 350ml 증정', '불가능', 'cu_spicyfantasticchickenburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '햄)백종원트리플불고기버거', 3400, 3, '행사없음', '불가능', 'cu_baekjongwontriplebulgogi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '햄)통새우치즈버거', 2800, 3, '행사없음', '불가능', 'cu_wholeshrimpcheeseburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '샌)백종원콘치즈참치샌드위치', 2500, 3, '에비앙 350ml 증정', '불가능', 'cu_baekjongwoncorncheesetuna')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 3, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 1, '10% 할인', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '농심 바나나킥 75g', 1200, 12, '10% 할인', '불가능', 'snack_bananakick')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '허쉬 초콜릿 칩 모찌 쿠키 12개입 240g', 3500, 17, '행사없음', '불가능', 'snack_hersheyschocolatechip')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '오뚜기 뿌셔뿌셔 바베큐맛 90g', 500, 4, '행사없음', '불가능', 'snack_sprinkle')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(10, 2, '광일제과 꾀돌이 45g', 300, 6, '행사없음', '불가능', 'snack_prankster')");

        //세븐일레븐
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '그린)NEW더커진더블빅불고기버거', 2600, 12, '행사없음', '불가능', 'seven_bigbulgogibuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)와규함박스테이크버거2.0', 2800, 13, '맑은샘물 500ml 증정', '불가능', 'seven_steakbuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '그린)NEW숯불갈비맛버거', 2000, 2, '행사없음', '불가능', 'seven_galbibuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '그린)NEW더블디럭스버거', 2200, 16, '맑은샘물 500ml 증정', '불가능', 'seven_doubledeluxeburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)NEW더블치즈불고기버거', 2100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_doublecheesebulgogiburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)NEW케이준치킨&햄샌드위치', 2200, 5, '행사없음', '불가능', 'seven_cajunchickenhamsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)NEW햄참치샐러드샌드위치', 2200, 7, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunasaladsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)참치인가샌드위치', 2200, 4, '행사없음', '불가능', 'seven_tunasandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '그린)햄참치마요&참치김치삼각김밥', 1900, 6, '맑은샘물 500ml 증정', '불가능', 'sven_hamtunamayotunakimchi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)청양참치마요네즈삼각김밥', 1200, 8, '행사없음', '불가능', 'seven_cheongyangtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '롯데)NEW직화돼지갈비삼각김밥', 1100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_directfiredporkribs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '그린)NEW햄참치마요네즈삼각김밥', 1200, 4, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '푸르밀)검은콩우유300ml', 1650, 1, '행사없음', '불가능', 'seven_blacksoybeanmilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '푸르밀)가나초코우유300ml', 1650, 0, '맑은샘물 500ml 증정', '불가능', 'seven_ganachocomilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '덴마크)민트초코우유310ml', 1650, 0, '행사없음', '불가능', 'seven_mintchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '덴마크)딸기딸기우유300ml', 1650, 5, '행사없음', '불가능', 'seven_strawberrystrawberrymilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '덴마크)초코초코우유300ml', 1650, 5, '행사없음', '불가능', 'seven_chocolatechocolatemilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '덴마크)바나바나우유300ml', 1650, 5, '행사없음', '불가능', 'seven_bananabananamilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 돌아온 로켓단 초코롤 85g', 1500, 0, '10% 할인', '불가능', 'spc_chocolateroll')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 0, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 0, '10% 할인', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 디그다 딸기카스타드빵 90g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycustard')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 꼬부기의 달콤파삭 꼬부기빵 75g', 1500, 0, '행사없음', '불가능', 'spc_squidbread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 푸린의 폭신폭신 딸기크림빵 100g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycreambread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(11, 3, '포켓몬빵 피카피카 촉촉 치즈케익 80g', 1500, 0, '행사없음', '불가능', 'spc_cheesecake')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '그린)햄참치마요&참치김치삼각김밥', 1900, 6, '맑은샘물 500ml 증정', '불가능', 'sven_hamtunamayotunakimchi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)청양참치마요네즈삼각김밥', 1200, 8, '행사없음', '불가능', 'seven_cheongyangtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)NEW직화돼지갈비삼각김밥', 1100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_directfiredporkribs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '그린)NEW햄참치마요네즈삼각김밥', 1200, 4, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '푸르밀)검은콩우유300ml', 1650, 1, '행사없음', '불가능', 'seven_blacksoybeanmilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '푸르밀)가나초코우유300ml', 1650, 0, '맑은샘물 500ml 증정', '불가능', 'seven_ganachocomilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '덴마크)민트초코우유310ml', 1650, 0, '행사없음', '불가능', 'seven_mintchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '덴마크)딸기딸기우유300ml', 1650, 5, '행사없음', '불가능', 'seven_strawberrystrawberrymilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '덴마크)초코초코우유300ml', 1650, 5, '행사없음', '불가능', 'seven_chocolatechocolatemilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '덴마크)바나바나우유300ml', 1650, 5, '행사없음', '불가능', 'seven_bananabananamilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '그린)NEW더커진더블빅불고기버거', 2600, 12, '행사없음', '불가능', 'seven_bigbulgogibuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)와규함박스테이크버거2.0', 2800, 13, '맑은샘물 500ml 증정', '불가능', 'seven_steakbuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '그린)NEW숯불갈비맛버거', 2000, 2, '행사없음', '불가능', 'seven_galbibuger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '그린)NEW더블디럭스버거', 2200, 16, '맑은샘물 500ml 증정', '불가능', 'seven_doubledeluxeburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)NEW더블치즈불고기버거', 2100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_doublecheesebulgogiburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)NEW케이준치킨&햄샌드위치', 2200, 5, '행사없음', '불가능', 'seven_cajunchickenhamsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)NEW햄참치샐러드샌드위치', 2200, 7, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunasaladsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(12, 3, '롯데)참치인가샌드위치', 2200, 4, '행사없음', '불가능', 'seven_tunasandwich')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '덴마크)초코초코우유300ml', 1650, 5, '행사없음', '불가능', 'seven_chocolatechocolatemilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '덴마크)바나바나우유300ml', 1650, 5, '행사없음', '불가능', 'seven_bananabananamilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 돌아온 로켓단 초코롤 85g', 1500, 0, '10% 할인', '불가능', 'spc_chocolateroll')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 돌아온 고오스 초코케익 80g', 1500, 0, '행사없음', '불가능', 'spc_chocolatecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 파이리의 화르륵 핫소스팡 90g', 1500, 0, '10% 할인', '불가능', 'spc_hotsaucepan')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 디그다 딸기카스타드빵 90g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycustard')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 꼬부기의 달콤파삭 꼬부기빵 75g', 1500, 0, '행사없음', '불가능', 'spc_squidbread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 푸린의 폭신폭신 딸기크림빵 100g', 1500, 0, '행사없음', '불가능', 'spc_strawberrycreambread')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '포켓몬빵 피카피카 촉촉 치즈케익 80g', 1500, 0, '행사없음', '불가능', 'spc_cheesecake')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '그린)NEW더블디럭스버거', 2200, 16, '맑은샘물 500ml 증정', '불가능', 'seven_doubledeluxeburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '롯데)NEW더블치즈불고기버거', 2100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_doublecheesebulgogiburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '롯데)NEW케이준치킨&햄샌드위치', 2200, 5, '행사없음', '불가능', 'seven_cajunchickenhamsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '롯데)NEW햄참치샐러드샌드위치', 2200, 7, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunasaladsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(13, 3, '롯데)참치인가샌드위치', 2200, 4, '행사없음', '불가능', 'seven_tunasandwich')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '롯데)참치인가샌드위치', 2200, 4, '행사없음', '불가능', 'seven_tunasandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '그린)햄참치마요&참치김치삼각김밥', 1900, 6, '맑은샘물 500ml 증정', '불가능', 'sven_hamtunamayotunakimchi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '롯데)청양참치마요네즈삼각김밥', 1200, 8, '행사없음', '불가능', 'seven_cheongyangtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '롯데)NEW직화돼지갈비삼각김밥', 1100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_directfiredporkribs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '그린)NEW햄참치마요네즈삼각김밥', 1200, 4, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '푸르밀)검은콩우유300ml', 1650, 1, '행사없음', '불가능', 'seven_blacksoybeanmilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '푸르밀)가나초코우유300ml', 1650, 0, '맑은샘물 500ml 증정', '불가능', 'seven_ganachocomilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '덴마크)민트초코우유310ml', 1650, 0, '행사없음', '불가능', 'seven_mintchocolate')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '덴마크)딸기딸기우유300ml', 1650, 5, '행사없음', '불가능', 'seven_strawberrystrawberrymilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '덴마크)초코초코우유300ml', 1650, 5, '행사없음', '불가능', 'seven_chocolatechocolatemilk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(14, 3, '덴마크)바나바나우유300ml', 1650, 5, '행사없음', '불가능', 'seven_bananabananamilk')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '그린)NEW더블디럭스버거', 2200, 16, '맑은샘물 500ml 증정', '불가능', 'seven_doubledeluxeburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '롯데)NEW더블치즈불고기버거', 2100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_doublecheesebulgogiburger')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '롯데)NEW케이준치킨&햄샌드위치', 2200, 5, '행사없음', '불가능', 'seven_cajunchickenhamsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '롯데)NEW햄참치샐러드샌드위치', 2200, 7, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunasaladsandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '롯데)참치인가샌드위치', 2200, 4, '행사없음', '불가능', 'seven_tunasandwich')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '그린)햄참치마요&참치김치삼각김밥', 1900, 6, '맑은샘물 500ml 증정', '불가능', 'sven_hamtunamayotunakimchi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '롯데)청양참치마요네즈삼각김밥', 1200, 8, '행사없음', '불가능', 'seven_cheongyangtunamayonnaise')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '롯데)NEW직화돼지갈비삼각김밥', 1100, 2, '맑은샘물 500ml 증정', '불가능', 'seven_directfiredporkribs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(15, 3, '그린)NEW햄참치마요네즈삼각김밥', 1200, 4, '맑은샘물 500ml 증정', '불가능', 'seven_hamtunamayonnaise')");

        //홈플러스
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '올 겨울 처음 수확 한 딸기 500G(팩)', 8990, 23, '행사없음', '불가능', 'ha')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '하리보 메리크리스마스 980G', 13990, 53, '행사없음', '불가능', 'hb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '냉동 소막창 구이 300G', 9990, 43, '행사없음', '불가능', 'hc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '네오플램 블로썸 IH 양수냄비 20CM', 22900, 26, '행사없음', '불가능', 'hd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '바비큐 버라이어티팩 1.14KG', 1990, 7, '행사없음', '불가능', 'he')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '믿고먹는 안심농협 쌀 20KG', 3400, 43, '행사없음', '불가능', 'hf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '제주 타이벡 밀감 3KG(박스)', 9990, 53, '행사없음', '불가능', 'hg')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '[몽블랑제]촉촉 마들렌 (4입)', 4540, 11, '행사없음', '불가능', 'hh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '바지락살 400G(팩)', 6930, 23, '행사없음', '불가능', 'hi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '한봉지가득 손질홍합(진주담치/국산) 특', 5400, 15, '행사없음', '불가능', 'hj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '곰표 누룽지 오트밀 750G', 9990, 23, '행사없음', '불가능', 'hk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '국산볶음 땅콩 400G (봉)', 9990, 15, '행사없음', '불가능', 'hl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '[몽블랑제]베리메리 화이트 크리스마스 케이크', 13230, 21, '행사없음', '불가능', 'hm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '슈퍼푸드 블루베리(칠레) 310G(팩)', 6990, 15, '행사없음', '불가능', 'hn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '팔도진미 강원도 춘천식 닭갈비 800G', 11990, 23, '행사없음', '불가능', 'ho')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, 'New홈플식탁 생연어 새우 초밥 (16입)', 13990, 16, '행사없음', '불가능', 'hp')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '감자 3KG(박스)', 7990, 23, '행사없음', '불가능', 'hq')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '창녕 깐마늘 1kg(봉)', 9990, 62, '행사없음', '불가능', 'hr')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '서울 체다 치즈 900G', 9900, 3, '행사없음', '불가능', 'hs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(16, 4, '유한양행 암앤해머 클린버스트 세탁세제 2.21L', 18900, 16, '행사없음', '불가능', 'ht')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '바비큐 버라이어티팩 1.14KG', 1990, 7, '행사없음', '불가능', 'he')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '믿고먹는 안심농협 쌀 20KG', 3400, 43, '행사없음', '불가능', 'hf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '제주 타이벡 밀감 3KG(박스)', 9990, 53, '행사없음', '불가능', 'hg')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '[몽블랑제]촉촉 마들렌 (4입)', 4540, 11, '행사없음', '불가능', 'hh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '바지락살 400G(팩)', 6930, 23, '행사없음', '불가능', 'hi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '한봉지가득 손질홍합(진주담치/국산) 특', 5400, 15, '행사없음', '불가능', 'hj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '곰표 누룽지 오트밀 750G', 9990, 23, '행사없음', '불가능', 'hk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '국산볶음 땅콩 400G (봉)', 9990, 15, '행사없음', '불가능', 'hl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '[몽블랑제]베리메리 화이트 크리스마스 케이크', 13230, 21, '행사없음', '불가능', 'hm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '슈퍼푸드 블루베리(칠레) 310G(팩)', 6990, 15, '행사없음', '불가능', 'hn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '팔도진미 강원도 춘천식 닭갈비 800G', 11990, 23, '행사없음', '불가능', 'ho')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, 'New홈플식탁 생연어 새우 초밥 (16입)', 13990, 16, '행사없음', '불가능', 'hp')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '감자 3KG(박스)', 7990, 23, '행사없음', '불가능', 'hq')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '창녕 깐마늘 1kg(봉)', 9990, 62, '행사없음', '불가능', 'hr')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '서울 체다 치즈 900G', 9900, 3, '행사없음', '불가능', 'hs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '유한양행 암앤해머 클린버스트 세탁세제 2.21L', 18900, 16, '행사없음', '불가능', 'ht')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '올 겨울 처음 수확 한 딸기 500G(팩)', 8990, 23, '행사없음', '불가능', 'ha')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '하리보 메리크리스마스 980G', 13990, 53, '행사없음', '불가능', 'hb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '냉동 소막창 구이 300G', 9990, 43, '행사없음', '불가능', 'hc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(17, 4, '네오플램 블로썸 IH 양수냄비 20CM', 22900, 26, '행사없음', '불가능', 'hd')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '바지락살 400G(팩)', 6930, 23, '행사없음', '불가능', 'hi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '한봉지가득 손질홍합(진주담치/국산) 특', 5400, 15, '행사없음', '불가능', 'hj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '곰표 누룽지 오트밀 750G', 9990, 23, '행사없음', '불가능', 'hk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '국산볶음 땅콩 400G (봉)', 9990, 15, '행사없음', '불가능', 'hl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '[몽블랑제]베리메리 화이트 크리스마스 케이크', 13230, 21, '행사없음', '불가능', 'hm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '슈퍼푸드 블루베리(칠레) 310G(팩)', 6990, 15, '행사없음', '불가능', 'hn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '팔도진미 강원도 춘천식 닭갈비 800G', 11990, 23, '행사없음', '불가능', 'ho')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, 'New홈플식탁 생연어 새우 초밥 (16입)', 13990, 16, '행사없음', '불가능', 'hp')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '감자 3KG(박스)', 7990, 23, '행사없음', '불가능', 'hq')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '창녕 깐마늘 1kg(봉)', 9990, 62, '행사없음', '불가능', 'hr')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '서울 체다 치즈 900G', 9900, 3, '행사없음', '불가능', 'hs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '유한양행 암앤해머 클린버스트 세탁세제 2.21L', 18900, 16, '행사없음', '불가능', 'ht')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '올 겨울 처음 수확 한 딸기 500G(팩)', 8990, 23, '행사없음', '불가능', 'ha')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '하리보 메리크리스마스 980G', 13990, 53, '행사없음', '불가능', 'hb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '냉동 소막창 구이 300G', 9990, 43, '행사없음', '불가능', 'hc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '네오플램 블로썸 IH 양수냄비 20CM', 22900, 26, '행사없음', '불가능', 'hd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '바비큐 버라이어티팩 1.14KG', 1990, 7, '행사없음', '불가능', 'he')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '믿고먹는 안심농협 쌀 20KG', 3400, 43, '행사없음', '불가능', 'hf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '제주 타이벡 밀감 3KG(박스)', 9990, 53, '행사없음', '불가능', 'hg')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(18, 4, '[몽블랑제]촉촉 마들렌 (4입)', 4540, 11, '행사없음', '불가능', 'hh')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '팔도진미 강원도 춘천식 닭갈비 800G', 11990, 23, '행사없음', '불가능', 'ho')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, 'New홈플식탁 생연어 새우 초밥 (16입)', 13990, 16, '행사없음', '불가능', 'hp')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '감자 3KG(박스)', 7990, 23, '행사없음', '불가능', 'hq')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '창녕 깐마늘 1kg(봉)', 9990, 62, '행사없음', '불가능', 'hr')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '서울 체다 치즈 900G', 9900, 3, '행사없음', '불가능', 'hs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '유한양행 암앤해머 클린버스트 세탁세제 2.21L', 18900, 16, '행사없음', '불가능', 'ht')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '올 겨울 처음 수확 한 딸기 500G(팩)', 8990, 23, '행사없음', '불가능', 'ha')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '하리보 메리크리스마스 980G', 13990, 53, '행사없음', '불가능', 'hb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '냉동 소막창 구이 300G', 9990, 43, '행사없음', '불가능', 'hc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '네오플램 블로썸 IH 양수냄비 20CM', 22900, 26, '행사없음', '불가능', 'hd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '바비큐 버라이어티팩 1.14KG', 1990, 7, '행사없음', '불가능', 'he')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '믿고먹는 안심농협 쌀 20KG', 3400, 43, '행사없음', '불가능', 'hf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '제주 타이벡 밀감 3KG(박스)', 9990, 53, '행사없음', '불가능', 'hg')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '[몽블랑제]촉촉 마들렌 (4입)', 4540, 11, '행사없음', '불가능', 'hh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '바지락살 400G(팩)', 6930, 23, '행사없음', '불가능', 'hi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '한봉지가득 손질홍합(진주담치/국산) 특', 5400, 15, '행사없음', '불가능', 'hj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '곰표 누룽지 오트밀 750G', 9990, 23, '행사없음', '불가능', 'hk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '국산볶음 땅콩 400G (봉)', 9990, 15, '행사없음', '불가능', 'hl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '[몽블랑제]베리메리 화이트 크리스마스 케이크', 13230, 21, '행사없음', '불가능', 'hm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(19, 4, '슈퍼푸드 블루베리(칠레) 310G(팩)', 6990, 15, '행사없음', '불가능', 'hn')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '하리보 메리크리스마스 980G', 13990, 53, '행사없음', '불가능', 'hb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '냉동 소막창 구이 300G', 9990, 43, '행사없음', '불가능', 'hc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '네오플램 블로썸 IH 양수냄비 20CM', 22900, 26, '행사없음', '불가능', 'hd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '바비큐 버라이어티팩 1.14KG', 1990, 7, '행사없음', '불가능', 'he')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '믿고먹는 안심농협 쌀 20KG', 3400, 43, '행사없음', '불가능', 'hf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '제주 타이벡 밀감 3KG(박스)', 9990, 53, '행사없음', '불가능', 'hg')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '[몽블랑제]촉촉 마들렌 (4입)', 4540, 11, '행사없음', '불가능', 'hh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '바지락살 400G(팩)', 6930, 23, '행사없음', '불가능', 'hi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '한봉지가득 손질홍합(진주담치/국산) 특', 5400, 15, '행사없음', '불가능', 'hj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '곰표 누룽지 오트밀 750G', 9990, 23, '행사없음', '불가능', 'hk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '국산볶음 땅콩 400G (봉)', 9990, 15, '행사없음', '불가능', 'hl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '[몽블랑제]베리메리 화이트 크리스마스 케이크', 13230, 21, '행사없음', '불가능', 'hm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '슈퍼푸드 블루베리(칠레) 310G(팩)', 6990, 15, '행사없음', '불가능', 'hn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '팔도진미 강원도 춘천식 닭갈비 800G', 11990, 23, '행사없음', '불가능', 'ho')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, 'New홈플식탁 생연어 새우 초밥 (16입)', 13990, 16, '행사없음', '불가능', 'hp')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '감자 3KG(박스)', 7990, 23, '행사없음', '불가능', 'hq')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '창녕 깐마늘 1kg(봉)', 9990, 62, '행사없음', '불가능', 'hr')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '서울 체다 치즈 900G', 9900, 3, '행사없음', '불가능', 'hs')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '유한양행 암앤해머 클린버스트 세탁세제 2.21L', 18900, 16, '행사없음', '불가능', 'ht')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(20, 4, '올 겨울 처음 수확 한 딸기 500G(팩)', 8990, 23, '행사없음', '불가능', 'ha')");

        //이마트
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '서울우유 아침에주스 제주풋귤750ML', 2980, 53, '행사없음', '불가능', 'a')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '켈로그 레드베리 에너지바 4입(25g*4)', 3980, 42, '행사없음', '불가능', 'b')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '자연퐁 스팀워시 주방세제 750mL(레몬)', 7500, 31, '1+1', '불가능', 'c')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '한라봉 2kg 박스', 18680, 0, '행사없음', '불가능', 'd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '해태 고향만두 1800g', 12980, 7, '행사없음', '불가능', 'e')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '제주삼다수 그린 2L 6병', 5880, 83, '행사없음', '불가능', 'f')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '에스티로더 더블웨어 파운데이션 세트', 65408, 23, '엔비 립 밤 정품 증정', '불가능', 'g')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '입생로랑(YSL) 메쉬 핑크 쿠션', 84645, 2, '미니 핑크 쿠션 증정', '불가능', 'h')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '[농심] 올리브 짜파게티 (140gx5입)', 4380, 21, '행사없음', '불가능', 'i')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '꼼마꼼마 뉴 올데이 비말마스크 KF-AD 대형 검정색 50매', 10990, 63, '행사없음', '불가능', 'j')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '제주몬트락 돈육 선물세트', 66900, 32, '행사없음', '불가능', 'k')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '남 벨벳 파자마 세트', 37737, 43, '행사없음', '불가능', 'l')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '오븐 에어프라이어 J50N904003800', 107400, 23, '행사없음', '불가능', 'm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(21, 5, '센티드 더플라워 디퓨저 120ml_프리지아', 8940, 62, '40% 할인', '불가능', 'n')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '[농심] 올리브 짜파게티 (140gx5입)', 4380, 21, '행사없음', '불가능', 'i')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '꼼마꼼마 뉴 올데이 비말마스크 KF-AD 대형 검정색 50매', 10990, 63, '행사없음', '불가능', 'j')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '제주몬트락 돈육 선물세트', 66900, 25, '행사없음', '불가능', 'k')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '남 벨벳 파자마 세트', 37737, 34, '행사없음', '불가능', 'l')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '오븐 에어프라이어 J50N904003800', 107400, 13, '행사없음', '불가능', 'm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '센티드 더플라워 디퓨저 120ml_프리지아', 8940, 32, '40% 할인', '불가능', 'n')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '서울우유 아침에주스 제주풋귤750ML', 2980, 12, '행사없음', '불가능', 'a')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '켈로그 레드베리 에너지바 4입(25g*4)', 3980, 15, '행사없음', '불가능', 'b')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '자연퐁 스팀워시 주방세제 750mL(레몬)', 7500, 21, '1+1', '불가능', 'c')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '한라봉 2kg 박스', 18680, 2, '행사없음', '불가능', 'd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '해태 고향만두 1800g', 12980, 32, '행사없음', '불가능', 'e')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '제주삼다수 그린 2L 6병', 5880, 25, '행사없음', '불가능', 'f')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '에스티로더 더블웨어 파운데이션 세트', 65408, 24, '엔비 립 밤 정품 증정', '불가능', 'g')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(22, 5, '입생로랑(YSL) 메쉬 핑크 쿠션', 84645, 25, '미니 핑크 쿠션 증정', '불가능', 'h')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '센티드 더플라워 디퓨저 120ml_프리지아', 8940, 4, '40% 할인', '불가능', 'n')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '서울우유 아침에주스 제주풋귤750ML', 2980, 26, '행사없음', '불가능', 'a')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '켈로그 레드베리 에너지바 4입(25g*4)', 3980, 23, '행사없음', '불가능', 'b')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '자연퐁 스팀워시 주방세제 750mL(레몬)', 7500, 16, '1+1', '불가능', 'c')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '한라봉 2kg 박스', 18680, 4, '행사없음', '불가능', 'd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '해태 고향만두 1800g', 12980, 13, '행사없음', '불가능', 'e')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '[농심] 올리브 짜파게티 (140gx5입)', 4380, 66, '행사없음', '불가능', 'i')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '꼼마꼼마 뉴 올데이 비말마스크 KF-AD 대형 검정색 50매', 10990, 41, '행사없음', '불가능', 'j')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '제주몬트락 돈육 선물세트', 66900, 34, '행사없음', '불가능', 'k')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '남 벨벳 파자마 세트', 37737, 14, '행사없음', '불가능', 'l')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '오븐 에어프라이어 J50N904003800', 107400, 4, '행사없음', '불가능', 'm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '제주삼다수 그린 2L 6병', 5880, 25, '행사없음', '불가능', 'f')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '에스티로더 더블웨어 파운데이션 세트', 65408, 23, '엔비 립 밤 정품 증정', '불가능', 'g')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(23, 5, '입생로랑(YSL) 메쉬 핑크 쿠션', 84645, 6, '미니 핑크 쿠션 증정', '불가능', 'h')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '해태 고향만두 1800g', 12980, 21, '행사없음', '불가능', 'e')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '[농심] 올리브 짜파게티 (140gx5입)', 4380, 4, '행사없음', '불가능', 'i')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '꼼마꼼마 뉴 올데이 비말마스크 KF-AD 대형 검정색 50매', 10990, 6, '행사없음', '불가능', 'j')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '제주몬트락 돈육 선물세트', 66900, 31, '행사없음', '불가능', 'k')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '남 벨벳 파자마 세트', 37737, 46, '행사없음', '불가능', 'l')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '오븐 에어프라이어 J50N904003800', 107400, 7, '행사없음', '불가능', 'm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '제주삼다수 그린 2L 6병', 5880, 31, '행사없음', '불가능', 'f')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '에스티로더 더블웨어 파운데이션 세트', 65408, 44, '엔비 립 밤 정품 증정', '불가능', 'g')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '입생로랑(YSL) 메쉬 핑크 쿠션', 84645, 8, '미니 핑크 쿠션 증정', '불가능', 'h')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '센티드 더플라워 디퓨저 120ml_프리지아', 8940, 5, '40% 할인', '불가능', 'n')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '서울우유 아침에주스 제주풋귤750ML', 2980, 2, '행사없음', '불가능', 'a')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '켈로그 레드베리 에너지바 4입(25g*4)', 3980, 7, '행사없음', '불가능', 'b')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '자연퐁 스팀워시 주방세제 750mL(레몬)', 7500, 8, '1+1', '불가능', 'c')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(24, 5, '한라봉 2kg 박스', 18680, 62, '행사없음', '불가능', 'd')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '오븐 에어프라이어 J50N904003800', 107400, 17, '행사없음', '불가능', 'm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '제주삼다수 그린 2L 6병', 5880, 12, '행사없음', '불가능', 'f')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '에스티로더 더블웨어 파운데이션 세트', 65408, 1, '엔비 립 밤 정품 증정', '불가능', 'g')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '입생로랑(YSL) 메쉬 핑크 쿠션', 84645, 43, '미니 핑크 쿠션 증정', '불가능', 'h')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '센티드 더플라워 디퓨저 120ml_프리지아', 8940, 23, '40% 할인', '불가능', 'n')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '서울우유 아침에주스 제주풋귤750ML', 2980, 6, '행사없음', '불가능', 'a')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '켈로그 레드베리 에너지바 4입(25g*4)', 3980, 34, '행사없음', '불가능', 'b')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '자연퐁 스팀워시 주방세제 750mL(레몬)', 7500, 67, '1+1', '불가능', 'c')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '한라봉 2kg 박스', 18680, 43, '행사없음', '불가능', 'd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '해태 고향만두 1800g', 12980, 11, '행사없음', '불가능', 'e')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '[농심] 올리브 짜파게티 (140gx5입)', 4380, 34, '행사없음', '불가능', 'i')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '꼼마꼼마 뉴 올데이 비말마스크 KF-AD 대형 검정색 50매', 10990, 4, '행사없음', '불가능', 'j')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '제주몬트락 돈육 선물세트', 66900, 13, '행사없음', '불가능', 'k')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(25, 5, '남 벨벳 파자마 세트', 37737, 33, '행사없음', '불가능', 'l')");

        //나이키
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 스포츠웨어 클럽 팬츠 기모', 38640, 5, '행사없음', '가능', 'na')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 여성운동화 W TC 7900 (DD9682-100)', 129300, 4, '행사없음', '가능', 'nb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 에어맥스 90 NIKE DH8010-100', 88960, 5, '행사없음', '가능', 'nc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 에어 줌 페가수스 39 DH4072-002', 88960, 8, '행사없음', '가능', 'nd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 버로우 로우 2 (GS) NIKE BQ5448-007', 47200, 3, '행사없음', '가능', 'ne')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-500', 44160, 6, '행사없음', '가능', 'nf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 줌 머큐리얼 베이퍼 15 프로 TF', 163930, 1, '행사없음', '가능', 'ng')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 버로우 방한 패딩 슬리퍼', 69750, 3, '행사없음', '가능', 'nh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 레볼루션 6 넥스트 네이처 NIKE DC3729-500', 44160, 6, '행사없음', '가능', 'ni')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 에어 줌 페가수스 39 DH4071-100', 88960, 3, '행사없음', '가능', 'nj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-001', 44160, 9, '행사없음', '가능', 'nk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 에어 윈플로 9 쉴드 DM1106_001', 85140, 1, '행사없음', '가능', 'nl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 와플 데뷰 운동화', 57855, 6, '행사없음', '가능', 'nm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 조던 맥스 아우라 4 NIKE DN3687-100', 95360, 3, '행사없음', '가능', 'nn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 디파이 올 데이 NIKE DJ1196-103', 50560, 2, '행사없음', '가능', 'no')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(26, 6, '나이키 에어 줌 페가수스 39 DH4072-100', 88960, 6, '행사없음', '가능', 'np')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 버로우 로우 2 (GS) NIKE BQ5448-007', 47200, 3, '행사없음', '가능', 'ne')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-500', 44160, 6, '행사없음', '가능', 'nf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 줌 머큐리얼 베이퍼 15 프로 TF', 163930, 1, '행사없음', '가능', 'ng')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 버로우 방한 패딩 슬리퍼', 69750, 3, '행사없음', '가능', 'nh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 레볼루션 6 넥스트 네이처 NIKE DC3729-500', 44160, 6, '행사없음', '가능', 'ni')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 에어 줌 페가수스 39 DH4071-100', 88960, 3, '행사없음', '가능', 'nj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-001', 44160, 9, '행사없음', '가능', 'nk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 에어 윈플로 9 쉴드 DM1106_001', 85140, 1, '행사없음', '가능', 'nl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 와플 데뷰 운동화', 57855, 6, '행사없음', '가능', 'nm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 조던 맥스 아우라 4 NIKE DN3687-100', 95360, 3, '행사없음', '가능', 'nn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 디파이 올 데이 NIKE DJ1196-103', 50560, 2, '행사없음', '가능', 'no')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 에어 줌 페가수스 39 DH4072-100', 88960, 6, '행사없음', '가능', 'np')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 스포츠웨어 클럽 팬츠 기모', 38640, 5, '행사없음', '가능', 'na')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 여성운동화 W TC 7900 (DD9682-100)', 129300, 4, '행사없음', '가능', 'nb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 에어맥스 90 NIKE DH8010-100', 88960, 5, '행사없음', '가능', 'nc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(27, 6, '나이키 에어 줌 페가수스 39 DH4072-002', 88960, 8, '행사없음', '가능', 'nd')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 버로우 방한 패딩 슬리퍼', 69750, 3, '행사없음', '가능', 'nh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 레볼루션 6 넥스트 네이처 NIKE DC3729-500', 44160, 6, '행사없음', '가능', 'ni')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 에어 줌 페가수스 39 DH4071-100', 88960, 3, '행사없음', '가능', 'nj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-001', 44160, 9, '행사없음', '가능', 'nk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 에어 윈플로 9 쉴드 DM1106_001', 85140, 1, '행사없음', '가능', 'nl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 와플 데뷰 운동화', 57855, 6, '행사없음', '가능', 'nm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 조던 맥스 아우라 4 NIKE DN3687-100', 95360, 3, '행사없음', '가능', 'nn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 디파이 올 데이 NIKE DJ1196-103', 50560, 2, '행사없음', '가능', 'no')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 에어 줌 페가수스 39 DH4072-100', 88960, 6, '행사없음', '가능', 'np')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 스포츠웨어 클럽 팬츠 기모', 38640, 5, '행사없음', '가능', 'na')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 여성운동화 W TC 7900 (DD9682-100)', 129300, 4, '행사없음', '가능', 'nb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 에어맥스 90 NIKE DH8010-100', 88960, 5, '행사없음', '가능', 'nc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 에어 줌 페가수스 39 DH4072-002', 88960, 8, '행사없음', '가능', 'nd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 버로우 로우 2 (GS) NIKE BQ5448-007', 47200, 3, '행사없음', '가능', 'ne')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-500', 44160, 6, '행사없음', '가능', 'nf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(28, 6, '나이키 줌 머큐리얼 베이퍼 15 프로 TF', 163930, 1, '행사없음', '가능', 'ng')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-001', 44160, 9, '행사없음', '가능', 'nk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 에어 윈플로 9 쉴드 DM1106_001', 85140, 1, '행사없음', '가능', 'nl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 와플 데뷰 운동화', 57855, 6, '행사없음', '가능', 'nm')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 조던 맥스 아우라 4 NIKE DN3687-100', 95360, 3, '행사없음', '가능', 'nn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 디파이 올 데이 NIKE DJ1196-103', 50560, 2, '행사없음', '가능', 'no')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 에어 줌 페가수스 39 DH4072-100', 88960, 6, '행사없음', '가능', 'np')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 스포츠웨어 클럽 팬츠 기모', 38640, 5, '행사없음', '가능', 'na')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 여성운동화 W TC 7900 (DD9682-100)', 129300, 4, '행사없음', '가능', 'nb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 에어맥스 90 NIKE DH8010-100', 88960, 5, '행사없음', '가능', 'nc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 에어 줌 페가수스 39 DH4072-002', 88960, 8, '행사없음', '가능', 'nd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 버로우 로우 2 (GS) NIKE BQ5448-007', 47200, 3, '행사없음', '가능', 'ne')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-500', 44160, 6, '행사없음', '가능', 'nf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 줌 머큐리얼 베이퍼 15 프로 TF', 163930, 1, '행사없음', '가능', 'ng')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 버로우 방한 패딩 슬리퍼', 69750, 3, '행사없음', '가능', 'nh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 레볼루션 6 넥스트 네이처 NIKE DC3729-500', 44160, 6, '행사없음', '가능', 'ni')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(29, 6, '나이키 에어 줌 페가수스 39 DH4071-100', 88960, 3, '행사없음', '가능', 'nj')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 조던 맥스 아우라 4 NIKE DN3687-100', 95360, 3, '행사없음', '가능', 'nn')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 디파이 올 데이 NIKE DJ1196-103', 50560, 2, '행사없음', '가능', 'no')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 에어 줌 페가수스 39 DH4072-100', 88960, 6, '행사없음', '가능', 'np')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 스포츠웨어 클럽 팬츠 기모', 38640, 5, '행사없음', '가능', 'na')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 여성운동화 W TC 7900 (DD9682-100)', 129300, 4, '행사없음', '가능', 'nb')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 에어맥스 90 NIKE DH8010-100', 88960, 5, '행사없음', '가능', 'nc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 에어 줌 페가수스 39 DH4072-002', 88960, 8, '행사없음', '가능', 'nd')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 버로우 로우 2 (GS) NIKE BQ5448-007', 47200, 3, '행사없음', '가능', 'ne')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-500', 44160, 6, '행사없음', '가능', 'nf')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 줌 머큐리얼 베이퍼 15 프로 TF', 163930, 1, '행사없음', '가능', 'ng')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 버로우 방한 패딩 슬리퍼', 69750, 3, '행사없음', '가능', 'nh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 레볼루션 6 넥스트 네이처 NIKE DC3729-500', 44160, 6, '행사없음', '가능', 'ni')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 에어 줌 페가수스 39 DH4071-100', 88960, 3, '행사없음', '가능', 'nj')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 레볼루션 6 넥스트 네이처 DC3729-001', 44160, 9, '행사없음', '가능', 'nk')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 에어 윈플로 9 쉴드 DM1106_001', 85140, 1, '행사없음', '가능', 'nl')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(30, 6, '나이키 와플 데뷰 운동화', 57855, 6, '행사없음', '가능', 'nm')");


        //온누리
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '타이레놀 500mg', 2500, 6, '행사없음', '불가능', 'oa')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '어린이 타이레놀 160mg', 2600, 3, '행사없음', '불가능', 'ob')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '애드빌', 3100, 8, '행사없음', '불가능', 'oc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '탁센 400 이부프로펜', 2700, 5, '행사없음', '불가능', 'od')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '어린이 부루펜 시럽', 2300, 3, '행사없음', '불가능', 'oe')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '스피딕400', 2300, 7, '행사없음', '불가능', 'of')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '스웨클로액', 2200, 5, '행사없음', '불가능', 'og')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '푸로스판 시럽', 3100, 9, '행사없음', '불가능', 'oh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '아이톡씨엠씨점안액', 2500, 6, '행사없음', '불가능', 'oi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(31, 7, '하이마린연질캡슐', 2600, 2, '행사없음', '불가능', 'oj')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '타이레놀 500mg', 2500, 3, '행사없음', '불가능', 'oa')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '어린이 타이레놀 160mg', 2600, 2, '행사없음', '불가능', 'ob')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '애드빌', 3100, 5, '행사없음', '불가능', 'oc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '탁센 400 이부프로펜', 2700, 2, '행사없음', '불가능', 'od')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '어린이 부루펜 시럽', 2300, 6, '행사없음', '불가능', 'oe')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '스피딕400', 2300, 4, '행사없음', '불가능', 'of')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '스웨클로액', 2200, 5, '행사없음', '불가능', 'og')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '푸로스판 시럽', 3100, 3, '행사없음', '불가능', 'oh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '아이톡씨엠씨점안액', 2500, 1, '행사없음', '불가능', 'oi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(32, 7, '하이마린연질캡슐', 2600, 3, '행사없음', '불가능', 'oj')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '타이레놀 500mg', 2500, 5, '행사없음', '불가능', 'oa')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '어린이 타이레놀 160mg', 2600, 2, '행사없음', '불가능', 'ob')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '애드빌', 3100, 3, '행사없음', '불가능', 'oc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '탁센 400 이부프로펜', 2700, 6, '행사없음', '불가능', 'od')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '어린이 부루펜 시럽', 2300, 4, '행사없음', '불가능', 'oe')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '스피딕400', 2300, 8, '행사없음', '불가능', 'of')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '스웨클로액', 2200, 4, '행사없음', '불가능', 'og')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '푸로스판 시럽', 3100, 5, '행사없음', '불가능', 'oh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '아이톡씨엠씨점안액', 2500, 2, '행사없음', '불가능', 'oi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(33, 7, '하이마린연질캡슐', 2600, 4, '행사없음', '불가능', 'oj')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '타이레놀 500mg', 2500, 7, '행사없음', '불가능', 'oa')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '어린이 타이레놀 160mg', 2600, 4, '행사없음', '불가능', 'ob')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '애드빌', 3100, 2, '행사없음', '불가능', 'oc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '탁센 400 이부프로펜', 2700, 4, '행사없음', '불가능', 'od')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '어린이 부루펜 시럽', 2300, 5, '행사없음', '불가능', 'oe')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '스피딕400', 2300, 4, '행사없음', '불가능', 'of')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '스웨클로액', 2200, 7, '행사없음', '불가능', 'og')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '푸로스판 시럽', 3100, 5, '행사없음', '불가능', 'oh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '아이톡씨엠씨점안액', 2500, 3, '행사없음', '불가능', 'oi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(34, 7, '하이마린연질캡슐', 2600, 5, '행사없음', '불가능', 'oj')");

        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '타이레놀 500mg', 2500, 7, '행사없음', '불가능', 'oa')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '어린이 타이레놀 160mg', 2600, 5, '행사없음', '불가능', 'ob')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '애드빌', 3100, 4, '행사없음', '불가능', 'oc')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '탁센 400 이부프로펜', 2700, 3, '행사없음', '불가능', 'od')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '어린이 부루펜 시럽', 2300, 2, '행사없음', '불가능', 'oe')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '스피딕400', 2300, 6, '행사없음', '불가능', 'of')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '스웨클로액', 2200, 4, '행사없음', '불가능', 'og')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '푸로스판 시럽', 3100, 2, '행사없음', '불가능', 'oh')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '아이톡씨엠씨점안액', 2500, 1, '행사없음', '불가능', 'oi')");
        db.execSQL("INSERT INTO product (storeid, brandid, productname, productprice, productstock, productevent, productsupply, productimg) VAlUES(35, 7, '하이마린연질캡슐', 2600, 3, '행사없음', '불가능', 'oj')");


        //장바구니 테이블 insert
        db.execSQL("INSERT INTO cart (id, productid, storeid, brandid) VAlUES('1111', 1, 1, 1)");
        db.execSQL("INSERT INTO cart (id, productid, storeid, brandid) VAlUES('1111', 2, 1, 1)");
        db.execSQL("INSERT INTO cart (id, productid, storeid, brandid) VAlUES('1111', 3, 1, 1)");


        //예약 테이블 insert
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String startTime = dateFormat.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, +2);
        String endTime = dateFormat.format(cal.getTime());
        db.execSQL("INSERT INTO reservation (id, productid, storeid, brandid, starttime, endtime) VAlUES('1111', 1, 1, 1, '2022-12-14 10:22:23', '2022-12-14 12:22:23')");


        //입고알림 테이블 insert
        db.execSQL("INSERT INTO notice (id, productid, storeid, brandid) VAlUES('1111', 1, 1, 1)");
        db.execSQL("INSERT INTO notice (id, productid, storeid, brandid) VAlUES('1111', 2, 1, 1)");
        db.execSQL("INSERT INTO notice (id, productid, storeid, brandid) VAlUES('1111', 3, 1, 1)");

        Log.d("Create_DB", "데이터베이스 생성 완료");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor select(String sql) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(String sql) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
