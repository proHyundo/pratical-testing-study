package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        // 특정 일자 결제완료 상태의 주문들 가져온다.
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );
        // 총 매출 합계 계산
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();
        // 메일 전송
        boolean result = mailService.sendMail(
                "no-reply@cafekiosk.com",
                email,
                String.format("[주문 매출 통계] 날짜 : %s", orderDate),
                String.format("총 매출: %s", totalAmount)
        );

        if (!result) throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        return true;
    }
}
