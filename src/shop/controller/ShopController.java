package shop.controller;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import shop.model.Customer;
import shop.model.Product;
import shop.model.pay.Payment;

public class ShopController {
    private List <Customer> customers = new ArrayList<>();
    private List <Product> products = new ArrayList<>();
    private Customer loggedInUser = null;
    private List <Product> cart = new ArrayList<>();
    private final String FILE_PATH = "products.txt";

    public ShopController() {
    	loadProductsFromCSV();
    }

//    public void startShopping() {
//
//    }
    public boolean login(String id, String password) {
        for (Customer c : customers) {
            if (c.getId().equals(id) && c.getPw().equals(password)) {
                loggedInUser = c; return true;
            }
        }
        return false;
    }

    public void signUp(String id, String password, String name, String phoneNum) {
        String signDate = LocalDate.now().toString();
        customers.add(new Customer(id, password, name, phoneNum, signDate));
        
    }

    public void logout() {
        this.loggedInUser = null; 
        this.cart.clear();
    }

    public Customer getLoggedInUser() {
        return loggedInUser;

        // 로그인된 사용자가 없을때 null을 반환하는게 아니라 별도 exception으로 관리하면 좋음.
    }

    public void loadProductsFromCSV() {
        products.clear();
        File file = new File(FILE_PATH);
        
        
        if (!file.exists()) {
            System.out.println("[경고] 파일을 찾을 수 없습니다!");
            System.out.println("자바가 찾고 있는 위치: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
             
                String[] data = line.split(",");
                if (data.length >= 5) {
                	
                	products.add(new Product(
                		    data[0].trim(), // 상품코드
                		    data[1].trim(), // 상품명
                		    data[2].trim(), // 카테고리
                		    Integer.parseInt(data[3].trim()), // 가격
                		    Integer.parseInt(data[4].trim())  // 재고
                		));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            System.out.println("[에러] 상품 정보를 불러오는데 실패했습니다.");
        }
        System.out.println("현재 로딩된 상품 개수: " + products.size() + "개");
    }

    public void saveProductsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Product p : products) {
                bw.write(p.getPCode() + "," + p.getPName() + "," + p.getCategory() + "," + p.getPrice() + "," + p.getStock());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("상품 정보를 파일에 저장하는데 실패했습니다.");
        }
    }

    public List<Product> getProducts() { return products; }
    public List<Product> getCart() { return cart; }

    public boolean addCart(String pCode) {
        String cleanInput = pCode.replaceAll("[^a-zA-Z0-9]", ""); 
        
        for (Product p : products) {
            
            if (p.getPCode().equalsIgnoreCase(cleanInput)) {
                if (p.isSoldOut()) {
                    System.out.println("[안내] 해당 상품은 품절되었습니다.");
                    return false;
                }
                cart.add(p); 
                return true;
            }
        }
        
        System.out.println("[에러] '" + cleanInput + "'은(는) 존재하지 않는 상품 코드입니다.");
        return false;
    }

    public void removeCart(String pCode) {
        cart.removeIf(p -> p.getPCode().equals(pCode));
    }

    public void processCheckout(Payment payment, int usePoint) {
        int total = cart.stream().mapToInt(Product::getPrice).sum();
        int discountAmount = (int)(total * 0.1); 
        int finalAmount = total - discountAmount - usePoint;
        if(finalAmount < 0) finalAmount = 0;

        // 1. 다형성 활용: 결제 처리
        if (payment.processPay(finalAmount)) {
            
            // 2. 재고 검사 (결제 전에 미리 재고가 다 있는지 확인하는 게 안전해!)
            for (Product item : cart) {
                for (Product p : products) {
                    if (p.getPCode().equals(item.getPCode())) {
                        if (p.getStock() <= 0) {
                            System.out.println("[알림] " + p.getPName() + " 재고가 부족합니다.");
                            return; // 결제 중단!

                            //여기서 return을 하면, 저 아래의 "결제에 실패했습니다." 메시지는 출력되지 않을것임.
                            //try catch를 활용하면 어떨까?
                            //여기선 throw를 하고 저 아래에서 catch로 받아 실패메시지를 띄우면 좋을거 같음.
                            //추후에 추가될 결제실패 요소에도 대응하기 쉬워짐.
                        }
                    }
                }
            }

            // 3. 로직 수행
            loggedInUser.usePoint(usePoint);
            loggedInUser.addPoint((int)(finalAmount * 0.01)); 

            for (Product item : cart) {
                for (Product p : products) {
                    if (p.getPCode().equals(item.getPCode())) { 
                        p.decreaseStock(); 
                        break; 
                    }
                }
            }
            
            saveProductsToCSV(); 
            cart.clear(); 
            System.out.println("결제가 성공적으로 완료되었습니다!");
        } else {
            System.out.println("결제에 실패했습니다.");
        }
    }

}
