package shop.model.pay;


public abstract class Payment {
    protected String payType; // 결제 수단 명칭

    // type같이 값의 유형이 고정된 것들은 enum 형식으로 관리해야함. String은 오타의 위험이 있음.

    public Payment(String payType) {
        this.payType = payType;
    }

    public String getPayType() { return payType; }

    // 자식 클래스들이 각자의 결제 방식을 구현하도록 강제함
    public abstract boolean processPay(int amount);
}
