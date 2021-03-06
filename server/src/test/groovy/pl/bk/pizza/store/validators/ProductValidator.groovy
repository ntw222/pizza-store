package pl.bk.pizza.store.validators

import pl.bk.common.dto.product.input.NewKebabDTO
import pl.bk.common.dto.product.input.NewPizzaToppingDTO
import pl.bk.pizza.store.helpers.CommonSpecification
import spock.lang.Unroll

import static java.math.BigDecimal.ONE
import static pl.bk.pizza.store.domain.exception.ErrorCode.*

class ProductValidator extends CommonSpecification
{

    //TODO Consider validation in mappers instead of domain objects ??
    /*@Unroll
    def "Pizza should not have #errorMessage"()
    {
        when:
        def error = createProductWithError(pizza)

        then:
        error.errorCode == errorMessage

        where:
        pizza                                        | errorMessage
        new NewPizzaDTO(ONE, null, DoughDTO.THICK)   | EMPTY_PIZZA_SIZE
        new NewPizzaDTO(ONE, PizzaSizeDTO.BIG, null) | EMPTY_DOUGH
    }*/

    def "PizzaTopping should not have empty name"()
    {
        when:
        def error = createProductWithError(pizzaTopping)

        then:
        error.errorCode == errorMessage

        where:
        pizzaTopping                    | errorMessage
        new NewPizzaToppingDTO(ONE, "") | EMPTY_PIZZA_TOPPING_NAME
    }

    @Unroll
    def "Kebab should not have #errorMessage"()
    {
        when:
        def error = createProductWithError(kebab)

        then:
        error.errorCode == errorMessage

        where:
        kebab                            | errorMessage
        new NewKebabDTO(ONE, "desc", "") | EMPTY_KEBAB_NAME
        new NewKebabDTO(ONE, "", "desc") | EMPTY_KEBAB_DESCRIPTION
    }
}
