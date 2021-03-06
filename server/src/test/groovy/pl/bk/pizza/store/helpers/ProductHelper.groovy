package pl.bk.pizza.store.helpers

import org.springframework.test.web.reactive.server.WebTestClient
import pl.bk.common.dto.order.OrderDTO
import pl.bk.common.dto.product.input.NewProductDTO
import pl.bk.common.dto.product.input.NewProductPriceDTO
import pl.bk.common.dto.product.output.KebabDTO
import pl.bk.common.dto.product.output.PizzaDTO
import pl.bk.common.dto.product.output.PizzaToppingDTO
import pl.bk.common.dto.product.output.ProductDTO
import pl.bk.pizza.store.infrastructure.error.ErrorMessage
import reactor.core.publisher.Mono

import static org.springframework.http.HttpMethod.GET

trait ProductHelper
{
    abstract WebTestClient getClient()

    ProductDTO createProduct(NewProductDTO product)
    {
        client
            .post()
            .uri("/admin/products")
            .body(Mono.just(product), NewProductDTO.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProductDTO)
            .returnResult()
            .responseBody
    }

    ProductDTO createProductWithAuthorizationToken(NewProductDTO product, String token)
    {
        client
            .post()
            .uri("/admin/products")
            .header("Authorization", "Bearer $token")
            .body(Mono.just(product), NewProductDTO.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProductDTO)
            .returnResult()
            .responseBody
    }

    ErrorMessage createProductWithError(NewProductDTO product)
    {
        client
            .post()
            .uri("/admin/products")
            .body(Mono.just(product), NewProductDTO.class)
            .exchange()
            .expectBody(ErrorMessage)
            .returnResult()
            .responseBody
    }

    WebTestClient.ResponseSpec createProductWithSecurityError(NewProductDTO product)
    {
        client
            .post()
            .uri("/admin/products")
            .body(Mono.just(product), NewProductDTO.class)
            .exchange()
    }

    ProductDTO getProduct(String id)
    {
        client
            .method(GET)
            .uri("/products/$id")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ProductDTO)
            .returnResult()
            .responseBody
    }

    List<ProductDTO> getAllProducts()
    {
        client
            .method(GET)
            .uri("/products")
            .exchange()
            .expectBodyList(ProductDTO)
            .returnResult()
            .responseBody
    }

    List<ProductDTO> getAllAvailableProducts()
    {
        client
            .method(GET)
            .uri("/products/available")
            .exchange()
            .expectBodyList(ProductDTO)
            .returnResult()
            .responseBody
    }

    OrderDTO addProductToOrder(String orderId, String productId)
    {
        client
            .put()
            .uri("/orders/$orderId/$productId")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(OrderDTO)
            .returnResult()
            .responseBody
    }

    ProductDTO makeProductNonAvailable(String id)
    {
        client
            .put()
            .uri("/products/$id/non-available")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ProductDTO)
            .returnResult()
            .responseBody
    }

    ProductDTO changeProductPrice(String id, NewProductPriceDTO productPriceDTO)
    {
        client
            .put()
            .uri("/products/$id/changePrice")
            .body(Mono.just(productPriceDTO), NewProductPriceDTO)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ProductDTO)
            .returnResult()
            .responseBody
    }

    List<PizzaDTO> getPizzas()
    {
        client
            .method(GET)
            .uri("/products/pizzas")
            .exchange()
            .expectBodyList(PizzaDTO)
            .returnResult()
            .responseBody
    }

    List<KebabDTO> getKebabs()
    {
        client
            .method(GET)
            .uri("/products/kebabs")
            .exchange()
            .expectBodyList(KebabDTO)
            .returnResult()
            .responseBody
    }

    List<PizzaToppingDTO> getPizzaToppings()
    {
        client
            .method(GET)
            .uri("/products/pizzaToppings")
            .exchange()
            .expectBodyList(PizzaToppingDTO)
            .returnResult()
            .responseBody
    }
}
