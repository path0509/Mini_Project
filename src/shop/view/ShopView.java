package shop.view;

import java.util.List;
import java.util.Scanner;

import shop.controller.ShopController;
import shop.model.Customer;
import shop.model.Product;
import shop.model.pay.CardPayment;
import shop.model.pay.CashPayment;
import shop.model.pay.EasyPayment;
import shop.model.pay.Payment;

public class ShopView {
    Scanner sc = new Scanner(System.in);
    private ShopController controller = new ShopController();

    public void start() {
        boolean isRunning = true; // 수정 포인트 1: 무한 루프 제어용 변수

        while(isRunning) {
            System.out.println("===== 쇼핑몰 로그인/회원가입 =====");
            System.out.println("[1] 로그인 하기");
            System.out.println("[2] 회원가입 하기");
            System.out.println("[3] 서비스 종료하기");
            System.out.println("");
            System.out.print("이용할 메뉴를 입력해주세요: ");

            try {
                // 수정 포인트 2: nextInt() 혼용으로 인한 버그와 문자 입력 예외를 방지하기 위해 통일
                int menu = Integer.parseInt(sc.nextLine());

                switch(menu){
                    case 1:
                        loginView();
                        break;
                    case 2:
                        signUpView();
                        break;
                    case 3:
                        System.out.println("서비스를 종료합니다.");
                        isRunning = false; // 루프를 탈출하도록 false로 변경
                        break;
                    default:
                        System.out.println("지원하지 않는 기능입니다. 다시 입력해주세요.");
                        break;
                }
            } catch (NumberFormatException e) {
                // 수정 포인트 3: 문자를 입력했을 때 프로그램이 뻗지 않도록 방어
                System.out.println("잘못된 입력입니다. 숫자만 입력해주세요!");
            }
        }

        // 수정 포인트 4: 무한 루프가 완전히 끝난 프로그램 종료 시점에만 스캐너 닫기
        sc.close();
    }

    private void signUpView() {
        System.out.println("\n--- 회원가입 ---");
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("PW: "); String pw = sc.nextLine();
        System.out.print("이름: "); String name = sc.nextLine();
        System.out.print("전화번호: "); String phone = sc.nextLine();
        controller.signUp(id, pw, name, phone);
        System.out.println("가입이 완료되었습니다!");
    }

    private void loginView() {
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("PW: "); String pw = sc.nextLine();
        if (controller.login(id, pw)) {
            System.out.println("로그인 성공! " + controller.getLoggedInUser().getName() + "님 환영합니다.");
            loggedInMenu(); // 로그인 성공 시 메인 메뉴로 진입
        } else {
            System.out.println("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    private void loggedInMenu() {
        while (controller.getLoggedInUser() != null) {
            System.out.println("\n=== 이용 메뉴 ===");
            System.out.println("1. 상품조회/관리  2. 결제수단 관리  3. 장바구니/결제  4. 로그아웃");
            System.out.print("선택: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": productMenuView(); break;
                case "2": managePaymentView(); break;
                case "3": checkoutView(); break;
                case "4": controller.logout(); break;
                case "5": managePaymentView(); break;
                default: System.out.println("잘못된 입력입니다. 1~4 사이의 숫자를 입력해주세요.");
            }
        }
    }

    private void productMenuView() {
        List<Product> list = controller.getProducts();
        System.out.println("--- 상품 목록 ---");

        // 상품은 최대 10개까지만 노출
        int count = 0;
        for (Product p : list) {
            if (count >= 10) break;
            System.out.println("[" + p.getPName() + "] " + p.toString());
            count++;
        }

        System.out.println("\n1. 장바구니에 담기  2. 뒤로가기");
        System.out.print("선택: ");
        String sel = sc.nextLine();

        try {
            if (sel.equals("1")) {
                System.out.print("담을 상품 코드를 입력하세요: ");
                String code = sc.nextLine();
                if(controller.addCart(code)) System.out.println("장바구니에 추가되었습니다.");
            }
        } catch (NumberFormatException e) {
            System.out.println("가격이나 재고에는 숫자만 입력해야 합니다!");
        }
    }
    
//    private void myPaymentView() {
//    	System.out.println("\n--- 결제 수단 관리 ---");
//        System.out.println("1. 조회  2. 등록  3. 삭제");
//        System.out.print("선택: ");
//        String sel = sc.nextLine();
//        
//        if (sel == "1") {
//        	System.out.println(Payment);
//        }
//        
//    }
    
    private void managePaymentView() {
        System.out.println("\n--- 결제 수단 등록 ---");
        System.out.println("1. 현금(계좌)  2. 카드  3. 페이");
        System.out.print("선택: ");
        String sel = sc.nextLine();

        Customer user = controller.getLoggedInUser();
        Payment newPay = null;

        switch (sel) {
            case "1":
                System.out.print("입금은행: "); String bank = sc.nextLine();
                System.out.print("입금자명: "); String name = sc.nextLine();
                newPay = new CashPayment(bank, name);
                break;
            case "2":
                System.out.print("카드사: "); String company = sc.nextLine();
                System.out.print("소유주 명: "); String owner = sc.nextLine();
                newPay = new CardPayment(company, owner);
                break;
            case "3":
                System.out.print("페이 종류(네이버/카카오/삼성/애플 중 택1): ");
                String provider = sc.nextLine();
                newPay = new EasyPayment(provider);
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }

        if (newPay != null) {
            user.addPayment(newPay);
            System.out.println("결제 수단이 등록되었습니다.");
        }
    }

    private void checkoutView() {
        List<Product> cart = controller.getCart();
        if (cart.isEmpty()) { System.out.println("장바구니가 비어있습니다."); return; }

        System.out.println("\n--- 장바구니 목록 ---");
        for (Product p : cart) {
            System.out.println("[" + p.getPCode() + "] " + p.getPName() + " - " + p.getPrice() + "원");
        }

        System.out.print("\n장바구니에서 뺄 상품 코드를 입력하세요 (없으면 엔터): ");
        String removeCode = sc.nextLine();
        if (!removeCode.isEmpty()) {
            controller.removeCart(removeCode);
            System.out.println("상품이 장바구니에서 제외되었습니다.");
        }

        if (controller.getCart().isEmpty()) return;

        Customer user = controller.getLoggedInUser();
        List<Payment> pays = user.getMyPayments();

        if (pays.isEmpty()) {
            System.out.println("등록된 결제 수단이 없습니다. 결제 수단 관리에서 먼저 등록해주세요.");
            return;
        }

        try {
            System.out.println("\n--- 결제 진행 ---");
            System.out.println("현재 보유 포인트: " + user.getPoint());
            System.out.print("사용할 포인트 금액 (사용 안함: 0): ");
            int usePoint = Integer.parseInt(sc.nextLine());

            if(usePoint > user.getPoint()) {
                System.out.println("포인트가 부족합니다. 0원으로 처리합니다.");
                usePoint = 0;
            }

            System.out.println("결제 수단을 선택하세요:");
            for (int i = 0; i < pays.size(); i++) {
                System.out.println((i + 1) + ". " + pays.get(i).getPayType());
            }
            System.out.print("선택: ");
            int payIdx = Integer.parseInt(sc.nextLine()) - 1;

            if (payIdx >= 0 && payIdx < pays.size()) {
                controller.processCheckout(pays.get(payIdx), usePoint);
                System.out.println("주문 및 결제가 완료되었습니다. 메인으로 돌아갑니다.");
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자를 입력해야 합니다. 결제를 취소하고 메뉴로 돌아갑니다.");
        }
    }
}