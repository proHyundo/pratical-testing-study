package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 -> 동시성 고민
     * 키워드 : optimistic lock, pessimistic lock 등이 있다.
     * */

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();      // 주문생성요청에서 상품번호들을 조회
        List<Product> requestedProducts = findProductsBy(productNumbers);   // 상품번호들로 상품들을 조회(상품번호 중복가능)

        deductStockQuantities(requestedProducts);

        // 2. 주문 생성
        Order order = Order.create(requestedProducts, registeredDateTime);
        // 3. 주문 DB 저장
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        // 1. 상품 조회
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        // 1-1. 조회된 상품 리스트로 key: 상품번호, value: 상품 객체인 Map 생성
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(product -> product.getProductNumber(), product -> product));
        // 1-2. 주문생성요청에 담긴 상품번호로 map 에서 상품 객체를 조회하여 리스트로 생성
        List<Product> duplicateProducts = productNumbers.stream()
                .map(productNum -> productMap.get(productNum))
                .collect(Collectors.toList());
        return duplicateProducts;
    }

    private void deductStockQuantities(List<Product> requestedProducts) {
        // 요청된 상품들에서 재고 체크가 필요한(BAKERY, BOTTLE) 상품 번호들 추출.(상품번호 중복가능)
        List<String> needStockQuantityCheckProductNumbers = extractProductNumbersFrom(requestedProducts);
        // 재고체크가 필요한 상품번호들로 재고들을 조회(중복없음) 및 Map<상품번호, 재고>로 변환
        Map<String, Stock> stockMap = createStockMap(needStockQuantityCheckProductNumbers);
        // 상품별 재고 counting. Map<상품번호, 요청수량> 으로 변환
        Map<String, Long> stockCountMap = createCountingMap(needStockQuantityCheckProductNumbers);

        // 재고 차감 시도
        for (String productNumber : new HashSet<>(needStockQuantityCheckProductNumbers)) {
            Stock stock = stockMap.get(productNumber);
            int quantity = stockCountMap.get(productNumber).intValue();
            // 재고가 없으면 예외 발생
            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            // 재고 차감
            stock.deductQuantity(quantity);
        }
    }

    private List<String> extractProductNumbersFrom(List<Product> requestedProducts) {
        return requestedProducts.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(product -> product.getProductNumber())
                .collect(Collectors.toList());
    }

    private Map<String, Stock> createStockMap(List<String> needStockQuantityCheckProductNumbers) {
        List<Stock> stocksToCheck = stockRepository.findAllByProductNumberIn(needStockQuantityCheckProductNumbers);
        return stocksToCheck.stream()
                .collect(Collectors.toMap(stock -> stock.getProductNumber(), stock -> stock));
    }

    private Map<String, Long> createCountingMap(List<String> needStockQuantityCheckProductNumbers) {
        return needStockQuantityCheckProductNumbers.stream()
                .collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));
    }
}
