package shop.model.pay;

public class EasyPayment extends Payment {
    public EasyPayment(String provider) {
        super(provider + " 페이");
    }
    @Override
    public boolean processPay(int amount) {
        System.out.println(amount + "원을 " + payType + "으로 결제요청 합니다.");
        return true;
    }
}