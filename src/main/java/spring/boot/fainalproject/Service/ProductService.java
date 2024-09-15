package spring.boot.fainalproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.boot.fainalproject.API.ApiException;
import spring.boot.fainalproject.Model.Product;
import spring.boot.fainalproject.Model.Supplier;
import spring.boot.fainalproject.Repository.ProductRepository;
import spring.boot.fainalproject.Repository.SupplierRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get a specific product by ID
    public Product getProductById(Integer id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new ApiException("Product not found");
        }
        return product;
    }

    // Add a new product by Supplier
    public void addProduct(Integer supplierId, Product product) {
        Supplier supplier = supplierRepository.findSupplierById(supplierId);
        if (supplier == null) {
            throw new ApiException("Supplier not found");
        }
        product.setSupplier(supplier); // Set supplier to the product
        productRepository.save(product);
    }

    // Update an existing product
    public void updateProduct(Integer id, Product updatedProduct, Integer supplierId) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new ApiException("Product not found");
        }

        Supplier supplier = supplierRepository.findSupplierById(supplierId);
        if (supplier == null) {
            throw new ApiException("Supplier not found");
        }

        product.setProductName(updatedProduct.getProductName());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());
        product.setDescription(updatedProduct.getDescription());
        product.setImgURL(updatedProduct.getImgURL());
        product.setSupplier(supplier); // Update the supplier of the product

        productRepository.save(product);
    }

    // Delete a product
    public void deleteProduct(Integer id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new ApiException("Product not found");
        }
        productRepository.delete(product);
    }
}