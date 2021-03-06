package pl.bk.pizza.store.domain.discount.bonus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.bk.pizza.store.domain.discount.Discount;
import pl.bk.pizza.store.domain.order.Order;
import pl.bk.pizza.store.domain.product.BaseProductInfo;

import java.util.List;

import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.of;

@AllArgsConstructor
@Getter
public class ExtraProductDiscount implements Discount
{
    private final Integer numberOfBoughtProducts;
    private final String productId;
    private final Integer extraProducts;
    
    @Override
    public Order apply(Order order)
    {
        return of(order).map(o -> o.getProducts()
                                   .stream()
                                   .filter(p -> extraProducts > 0)
                                   .filter(p -> p.getId().equals(productId))
                                   .count() > numberOfBoughtProducts ? addExtraProducts(o) : o)
                        .findFirst()
                        .get();
    }
    
    private Order addExtraProducts(Order order)
    {
        return of(order)
            .flatMap(o -> range(0, extraProducts)
                .mapToObj(x -> o.addProduct(getProductById(o.getProducts()))))
            .findFirst()
            .get();
    }
    
    private BaseProductInfo getProductById(List<BaseProductInfo> products)
    {
        return products
            .stream()
            .filter(p -> p.getId().equals(productId))
            .findFirst()
            .get();
    }
}
