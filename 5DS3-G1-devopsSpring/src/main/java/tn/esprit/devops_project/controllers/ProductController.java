package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.services.iservices.IProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final IProductService productService;

    // Inner DTO class for Product data transfer
    public static class ProductDTO {
         String title;
         float price;
         int quantity;
         String category;
         Long stockId;  // Only exposing the stock ID, not the whole Stock object
    }

    // Helper method to convert Product entity to DTO
    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.title = product.getTitle();
        dto.price = product.getPrice();
        dto.quantity = product.getQuantity();
        dto.category = product.getCategory().name();
        dto.stockId = product.getStock() != null ? product.getStock().getIdStock() : null;
        return dto;
    }

    // Helper method to convert DTO to Product entity
    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setTitle(dto.title);
        product.setPrice(dto.price);
        product.setQuantity(dto.quantity);
        product.setCategory(ProductCategory.valueOf(dto.category));
        return product;
    }

    @PostMapping("/product/{idStock}")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO, @PathVariable Long idStock) {
        Product product = toEntity(productDTO);
        Product savedProduct = productService.addProduct(product, idStock);
        return toDTO(savedProduct);
    }

    @GetMapping("/product/{id}")
    public ProductDTO retrieveProduct(@PathVariable Long id) {
        Product product = productService.retrieveProduct(id);
        return toDTO(product);
    }

    @GetMapping("/product")
    public List<ProductDTO> retreiveAllProduct() {
        return productService.retreiveAllProduct()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/product/stock/{id}")
    public List<ProductDTO> retreiveProductStock(@PathVariable Long id) {
        return productService.retreiveProductStock(id)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/productCategory/{category}")
    public List<ProductDTO> retrieveProductByCategory(@PathVariable ProductCategory category) {
        return productService.retrieveProductByCategory(category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}