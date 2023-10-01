package com.example.paradao;

/**
 * @packageName	: com.example.paradao
 * @fileName	: ListProductData.java
 * @author		: TaeJeong Park
 * @date		: 2022.12.11
 * @description	: 입고알림 아이템 데이터를 담는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2022.12.11		TaeJeong Park		최초 생성
 * 2022.12.11		TaeJeong Park		기능 구현 완료
 */
public class ListProductData {

    private String image;
    private String productName;
    private String storeName;
    private String quantity;

    public void setImage(String image) {
        this.image = image;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getQuantity() {
        return quantity;
    }

}
