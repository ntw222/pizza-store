package pl.bk.pizza.store.domain.order;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.bk.pizza.store.domain.product.BaseProductInfo;

import java.math.BigDecimal;
import java.util.List;

import static pl.bk.pizza.store.domain.order.OrderStatus.*;
import static pl.bk.pizza.store.domain.service.NowProvider.now;
import static pl.bk.pizza.store.domain.validator.order.OrderValidator.realizationShouldBeStarted;

@Getter
@Document(collection = "order")
public class Order
{
    @Id
    private final String id;
    private final List<BaseProductInfo> products;
    private String userEmail;
    private OrderStatus orderStatus;
    private Long orderDateTime;
    private BigDecimal totalPrice;
    private final DeliveryInfo deliveryInfo;
    
    Order(String id, String userEmail, List<BaseProductInfo> products, OrderStatus orderStatus, BigDecimal totalPrice, DeliveryInfo deliveryInfo)
    {
        this.id = id;
        this.userEmail = userEmail;
        this.products = products;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.deliveryInfo = deliveryInfo;
    }
    
    public Order addProduct(BaseProductInfo product)
    {
        products.add(product);
        return this;
    }
    
    public Order setToRealization()
    {
        orderStatus = TO_REALIZATION;
        orderDateTime = now().toEpochSecond();
        totalPrice = calculateTotalPrice();
        return this;
    }
    
    private BigDecimal calculateTotalPrice()
    {
        return products
            .stream()
            .map(BaseProductInfo::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public Order setToDelivered()
    {
        realizationShouldBeStarted(orderStatus);
        orderStatus = DELIVERED;
        return this;
    }
}
