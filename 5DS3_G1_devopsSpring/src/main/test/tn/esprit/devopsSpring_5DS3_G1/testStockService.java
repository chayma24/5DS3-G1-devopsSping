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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
//Taaaaaaz

class StockServiceImplTest {

    @InjectMocks
    private StockServiceImpl stockService;

    @Mock
    private StockRepository stockRepository;

    private Stock stock;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a test Stock object
        stock = new Stock();
        stock.setId(1L);
        stock.setName("Test Stock");
        stock.setQuantity(100);
    }

    @Test
    void addStock_ShouldReturnSavedStock() {
        // Given: Mock the save method to return the stock object
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        // When: Call the addStock method
        Stock result = stockService.addStock(stock);

        // Then: Verify that the stock was saved and returned correctly
        assertNotNull(result);
        assertEquals(stock.getId(), result.getId());
        assertEquals(stock.getName(), result.getName());
        assertEquals(stock.getQuantity(), result.getQuantity());

        // Verify interactions with the repository
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void retrieveStock_ShouldReturnStock_WhenStockExists() {
        // Given: Mock the findById method to return the stock wrapped in an Optional
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // When: Call the retrieveStock method
        Stock result = stockService.retrieveStock(1L);

        // Then: Verify that the correct stock is returned
        assertNotNull(result);
        assertEquals(stock.getId(), result.getId());
        assertEquals(stock.getName(), result.getName());

        // Verify interactions with the repository
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    void retrieveStock_ShouldThrowException_WhenStockDoesNotExist() {
        // Given: Mock the findById method to return an empty Optional
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then: Call the retrieveStock method and expect an exception
        assertThrows(NullPointerException.class, () -> {
            stockService.retrieveStock(1L);
        });

        // Verify interactions with the repository
        verify(stockRepository, times(1)).findById(1L);
    }

    @Test
    void retrieveAllStock_ShouldReturnListOfStocks() {
        // Given: Mock the findAll method to return a list of stocks
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock));

        // When: Call the retrieveAllStock method
        List<Stock> result = stockService.retrieveAllStock();

        // Then: Verify that the correct list of stocks is returned
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stock.getId(), result.get(0).getId());

        // Verify interactions with the repository
        verify(stockRepository, times(1)).findAll();
    }
}
