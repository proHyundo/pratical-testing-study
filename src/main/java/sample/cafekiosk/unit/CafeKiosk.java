package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(Beverage beverage, int count) {
        if (count < 1) throw new IllegalArgumentException("음료는 1잔 이상이어야 합니다.");
        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }
        return totalPrice;
    }

    public Order createOrder(){
        LocalDateTime now = LocalDateTime.now();
        if (now.toLocalTime().isBefore(SHOP_OPEN_TIME) || now.toLocalTime().isAfter(SHOP_CLOSE_TIME))
            throw new IllegalArgumentException("주문 가능 시간이 아닙니다.");
        return new Order(now, beverages);
    }

    /**
     * 테스트 하기 어려운 부분은 분리하기.
     * 1. 관측할 때 마다 변경되는 값 E.g.) 현재 시간
     * 2. 외부세계에 영향을 주는 값 E.g.) 로그, 메시지발송, DB저장
     * */
    public Order createOrder(LocalDateTime now){
        if (now.toLocalTime().isBefore(SHOP_OPEN_TIME) || now.toLocalTime().isAfter(SHOP_CLOSE_TIME))
            throw new IllegalArgumentException("주문 가능 시간이 아닙니다.");
        return new Order(now, beverages);
    }


}
