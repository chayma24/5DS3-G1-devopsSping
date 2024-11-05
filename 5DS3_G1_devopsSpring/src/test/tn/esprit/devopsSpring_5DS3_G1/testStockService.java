package tn.esprit.devopsSpring_5DS3_G1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devopsSpring_5DS3_G1.entities.Stock;
import tn.esprit.devopsSpring_5DS3_G1.repositories.StockRepository;
//Saleeeem
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
//Taaaaaaz

@SpringBootTest
@ExtendWith(SpringExtension.class)
class StockServiceImplTest {

    @Autowired
    private StockServiceImpl stockService;

    @Autowired
    private StockRepository stockRepository;

    private Stock stock;

    @BeforeEach
    void setUp() {
        // Initialize the stock object
        stock = new Stock();
        stock.setTitle("Test Stock");
        stock.setProducts(new HashSet<>());  // Initially no products

        // Clear the repository before each test to ensure isolated tests
        stockRepository.deleteAll();
    }

    @Test
    void addStock_ShouldReturnSavedStock() {
        // When: Call the addStock method (which internally calls stockRepository.save())
        Stock result = stockService.addStock(stock);

        // Then: Verify that the stock was saved and returned correctly
        assertNotNull(result);
        assertEquals(stock.getTitle(), result.getTitle());
        assertTrue(result.getProducts().isEmpty());  // No products associated yet

        // Verify that the stock is now stored in the repository
        Optional<Stock> savedStock = stockRepository.findById(result.getIdStock());
        assertTrue(savedStock.isPresent());
        assertEquals(stock.getTitle(), savedStock.get().getTitle());
    }

    @Test
    void retrieveStock_ShouldReturnStock_WhenStockExists() {
        // Given: Save the stock first
        Stock savedStock = stockRepository.save(stock);

        // When: Call the retrieveStock method
        Stock result = stockService.retrieveStock(savedStock.getIdStock());

        // Then: Verify that the correct stock is returned
        assertNotNull(result);
        assertEquals(savedStock.getIdStock(), result.getIdStock());
        assertEquals(savedStock.getTitle(), result.getTitle());
    }

    @Test
    void retrieveStock_ShouldThrowException_WhenStockDoesNotExist() {
        // When & Then: Call the retrieveStock method and expect an exception
        assertThrows(NullPointerException.class, () -> {
            stockService.retrieveStock(999L); // Non-existing stock ID
        });
    }

    @Test
    void retrieveAllStock_ShouldReturnListOfStocks() {
        // Given: Save a couple of stocks in the repository
        stockRepository.save(stock);
        Stock anotherStock = new Stock();
        anotherStock.setTitle("Another Stock");
        anotherStock.setProducts(new HashSet<>());
        stockRepository.save(anotherStock);

        // When: Call the retrieveAllStock method
        List<Stock> result = stockService.retrieveAllStock();

        // Then: Verify that the correct list of stocks is returned
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(s -> s.getTitle().equals("Test Stock")));
        assertTrue(result.stream().anyMatch(s -> s.getTitle().equals("Another Stock")));
    }
}