package spring.boot.fainalproject.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.fainalproject.Model.Product;
import spring.boot.fainalproject.Service.ProductService;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Get all products
    @GetMapping
    public ResponseEntity getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAllProducts());
    }

    // Get a product by ID
    @GetMapping("/getProductById/{id}")
    public ResponseEntity getProductById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(productService.getProductById(id));
    }

    // Add a new product by a Supplier
    @PostMapping
    public ResponseEntity addProduct(@RequestParam Integer supplierId, @Valid @RequestBody Product product) {
        productService.addProduct(supplierId, product);
        return ResponseEntity.status(201).body("Product added successfully");
    }

    // Update an existing product by Supplier
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id,
                                        @RequestParam Integer supplierId,
                                        @Valid @RequestBody Product productDetails) {
        productService.updateProduct(id, productDetails, supplierId);
        return ResponseEntity.status(200).body("Product updated successfully");
    }

    // Delete a product
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(200).body("Product deleted successfully");
    }
}