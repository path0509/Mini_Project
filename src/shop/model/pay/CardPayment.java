package shop.model.pay;

public class CardPayment extends Payment {
    public CardPayment(String company, String owner) {
        super(company + " 카드(" + owner + ")");
    }
    @Override
    public boolean processPay(int amount) {
        System.out.println(amount + "원을 " + payType + "으로 승인요청 합니다.");
        return true;
    }
}