package shop.model.pay;


public abstract class Payment {
    protected String payType; // 결제 수단 명칭

    public Payment(String payType) {
        this.payType = payType;
    }

    public String getPayType() { return payType; }

    // 자식 클래스들이 각자의 결제 방식을 구현하도록 강제함
    public abstract boolean processPay(int amount);
}