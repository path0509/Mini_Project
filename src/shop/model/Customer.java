package shop.model;


import java.util.ArrayList;
/*
 * 전반적인 고객에 관한 모델들을 정의하기 위한 클래스
 *
 */
import java.util.List;

import shop.model.pay.Payment;

public class Customer {
    private String id, password, name, phoneNum;
    private String signDate;
    private int cPoint;
    private String status; // 일반 유저에게 보이지않는 관리자 분기 나누는 칸
    private List<Payment> myPayments;

    public Customer() {

    }

    public Customer(String id, String password, String name, String phoneNum, String sjgnDate) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.signDate = signDate;
        this.cPoint = 10000;
        this.status = "신규유저";
        this.myPayments = new ArrayList<>();
    }

    public String getId() {return id; }
    public String getPw() { return password; }
    public String getName() { return name; }
    public int getPoint() { return cPoint; }
    public List<Payment> getMyPayments() { return myPayments; }


    public void addPayment(Payment pay) {
        myPayments.add(pay);
    }

    public void addPoint(int amount) {
        this.cPoint += amount;
    }

    public boolean usePoint(int amount) {
        if (this.cPoint >= amount) {
            this.cPoint -= amount;
            return true;
        }
        return false;
    }



}