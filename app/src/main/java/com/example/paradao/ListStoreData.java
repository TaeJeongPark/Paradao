package com.example.paradao;

/**
 * @packageName	: com.example.paradao
 * @fileName	: ListStoreData.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.14
 * @description	: 매장 아이템 데이터를 담는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.14		TaeJeong Park		최초 생성
 * 2022.12.14		TaeJeong Park		기능 구현 완료
 */
public class ListStoreData {

    private String image;
    private String storeName;
    private String storeStartTime;
    private String storeEndTime;

    public void setImage(String image) {
        this.image = image;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreStartTime(String quantity) {
        this.storeStartTime = quantity;
    }

    public void setStoreEndTime(String quantity) { this.storeStartTime = quantity; }

    public String getImage() {
        return image;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreStartTime() {
        return storeStartTime;
    }

    public String getStoreEndTime() { return storeStartTime; }

}
