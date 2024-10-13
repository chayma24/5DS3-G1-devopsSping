package tn.esprit.devopsSpring_5DS3_G1.services.Iservices;

import tn.esprit.devopsSpring_5DS3_G1.entities.Product;
import tn.esprit.devopsSpring_5DS3_G1.entities.ProductCategory;

import java.util.List;

public interface IProductService {

    Product addProduct(Product product, Long idStock);
    Product retrieveProduct(Long id);
    List<Product> retreiveAllProduct();
    List<Product> retrieveProductByCategory(ProductCategory category);
    void deleteProduct(Long id);
    List<Product> retreiveProductStock(Long id);


}
