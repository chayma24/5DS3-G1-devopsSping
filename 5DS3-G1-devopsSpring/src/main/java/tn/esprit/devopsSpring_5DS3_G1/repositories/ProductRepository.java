package tn.esprit.devopsSpring_5DS3_G1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devopsSpring_5DS3_G1.entities.Product;
import tn.esprit.devopsSpring_5DS3_G1.entities.ProductCategory;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);
    List<Product> findByStockIdStock(Long idStock);
}
