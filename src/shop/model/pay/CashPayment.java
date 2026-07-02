package shop.model.pay;

public class CashPayment extends Payment {
    public CashPayment(String bank, String name) {
        super(bank + " 계좌이체(" + name + ")");
    }
    @Override
    public boolean processPay(int amount) {
        System.out.println(amount + "원을 " + payType + "으로 결제 진행합니다.");
        return true;
    }
}