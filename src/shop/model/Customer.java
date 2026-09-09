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
    //날짜는 Date클래스를 쓰거나 숫자형식으로 타임스탬프를 저장하는게 좋음.
    //date.toString() 로 저장을 하게되면 지역이나 언어별 로케일이 들어갈 수 있게되기도하고, 날짜비교 할일이 있으면 매우 귀찮아짐.
    //DB같은 외부연동을 한다면 타임스탬프로 관리하는게 제일 좋고 일반적임. 
    
    private int cPoint;
    private String status; // 일반 유저에게 보이지않는 관리자 분기 나누는 칸

    // 상태관리는 enum으로 하는게 베스트.
    
    private List<Payment> myPayments;  // Map을 사용해보는건 어떨까?

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
