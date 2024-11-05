package tn.esprit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devopsSpring_5DS3_G1.entities.Operator;
import tn.esprit.devopsSpring_5DS3_G1.repositories.OperatorRepository;
import tn.esprit.devopsSpring_5DS3_G1.services.OperatorServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OperatorServiceImplTest {
    @Mock
    private OperatorRepository operatorRepository;

    @InjectMocks
    private OperatorServiceImpl operatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllOperators() {
        List<Operator> operators = new ArrayList<>();
        operators.add(new Operator());
        when(operatorRepository.findAll()).thenReturn(operators);

        List<Operator> result = operatorService.retrieveAllOperators();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(operatorRepository, times(1)).findAll();
    }

    @Test
    void testAddOperator() {
        Operator operator = new Operator();
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);

        Operator result = operatorService.addOperator(operator);

        assertNotNull(result);
        verify(operatorRepository, times(1)).save(operator);
    }

    @Test
    void testDeleteOperator() {
        doNothing().when(operatorRepository).deleteById(anyLong());

        operatorService.deleteOperator(1L);

        verify(operatorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateOperator() {
        Operator operator = new Operator();
        when(operatorRepository.save(any(Operator.class))).thenReturn(operator);

        Operator result = operatorService.updateOperator(operator);

        assertNotNull(result);
        verify(operatorRepository, times(1)).save(operator);
    }

    @Test
    void testRetrieveOperator() {
        Operator operator = new Operator();
        when(operatorRepository.findById(anyLong())).thenReturn(Optional.of(operator));

        Operator result = operatorService.retrieveOperator(1L);

        assertNotNull(result);
        verify(operatorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveOperatorNotFound() {
        when(operatorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NullPointerException.class, () -> {
            operatorService.retrieveOperator(1L);
        });

        assertEquals("Operator not found", exception.getMessage());
        verify(operatorRepository, times(1)).findById(1L);
    }
}
