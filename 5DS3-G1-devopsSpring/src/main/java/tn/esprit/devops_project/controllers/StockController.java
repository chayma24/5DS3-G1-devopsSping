package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.services.iservices.IStockService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class StockController {

    private final IStockService stockService;

    // Inner DTO class for Stock data transfer
    public static class StockDTO {
         String title;
         Set<Long> productIds; // Only expose Product IDs, not the full Product entity
    }

    // Helper method to convert Stock entity to DTO
    private StockDTO toDTO(Stock stock) {
        StockDTO dto = new StockDTO();
        dto.title = stock.getTitle();
        dto.productIds = stock.getProducts()
                .stream()
                .map(Product::getIdProduct)
                .collect(Collectors.toSet());
        return dto;
    }

    // Helper method to convert DTO to Stock entity
    private Stock toEntity(StockDTO dto) {
        Stock stock = new Stock();
        stock.setTitle(dto.title);
        return stock; // Products are managed elsewhere
    }

    @PostMapping("/stock")
    public StockDTO addStock(@RequestBody StockDTO stockDTO) {
        Stock stock = toEntity(stockDTO);
        Stock savedStock = stockService.addStock(stock);
        return toDTO(savedStock);
    }

    @GetMapping("/stock/{id}")
    public StockDTO retrieveStock(@PathVariable Long id) {
        Stock stock = stockService.retrieveStock(id);
        return toDTO(stock);
    }

    @GetMapping("/stock")
    public List<StockDTO> retrieveAllStock() {
        return stockService.retrieveAllStock()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}