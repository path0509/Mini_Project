package shop.model;

import java.time.LocalDate;

public class Product {
    private String pName;	//제품 이름 (ex: 헨리넥 티셔츠)
    private String pCode;	//제품 코드
    private String category; //제품 카테고리
    private int price;	//제품 가격
    private int pStock;	// 재고
    private LocalDate postDate; //제품 등록일 (등록된 날짜별로 보고싶은 경우를 생각해서)

    public Product(String pCode, String pName, String category, int price, int pStock) {
        this.pName = pName;
        this.pCode = pCode;
        this.category = category;
        this.price = price;
        this.pStock = pStock;
        
        postDate = LocalDate.now();
    }

    public String getPCode() { return pCode; }
    public String getPName() { return pName; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public int getStock() { return pStock; }

    public boolean isSoldOut() {
        return pStock <= 0;
    }

    public void decreaseStock() {
        if (pStock > 0) {
            pStock--;
        }
    }

    @Override
    public String toString() {
        return pCode + "_" + category + "_" + price + "원_재고:" + (isSoldOut() ? "품절" : pStock + "개");
    }
}