package tn.esprit.devopsSpring_5DS3_G1.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devopsSpring_5DS3_G1.entities.Stock;
import tn.esprit.devopsSpring_5DS3_G1.repositories.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceImplTest {

    @InjectMocks
    private StockServiceImpl stockService;

    @Mock
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddStock() {
        Stock stock = new Stock();
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock result = stockService.addStock(stock);

        assertEquals(stock, result);
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void testRetrieveStock() {
        Long id = 1L;
        Stock stock = new Stock();
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));

        Stock result = stockService.retrieveStock(id);

        assertEquals(stock, result);
        verify(stockRepository, times(1)).findById(id);
    }

    @Test
    void testRetrieveStockNotFound() {
        Long id = 1L;
        when(stockRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> stockService.retrieveStock(id));
        assertEquals("Stock not found", exception.getMessage());

        verify(stockRepository, times(1)).findById(id);
    }

    @Test
    void testRetrieveAllStock() {
        List<Stock> stockList = new ArrayList<>();
        when(stockRepository.findAll()).thenReturn(stockList);

        List<Stock> result = stockService.retrieveAllStock();

        assertEquals(stockList, result);
        verify(stockRepository, times(1)).findAll();
    }
}
