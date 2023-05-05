package cart.controller.product;

import cart.service.product.ProductService;
import cart.service.product.dto.ProductRequest;
import cart.service.product.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ProductRequest productRequest) {
        Long productId = productService.create(productRequest);
        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductWebResponse> update(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        ProductResponse updated = productService.update(productRequest, id);
        ProductWebResponse updateResponse = new ProductWebResponse(updated.getId(), updated.getName(), updated.getImageUrl(), updated.getPrice());
        return ResponseEntity.ok(updateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
