package tn.esprit.devops_project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InvoiceServiceImplMockito {
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private OperatorRepository operatorRepository;
    @InjectMocks
    private InvoiceServiceImpl invoiceServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        configureMocks();
    }

    private void configureMocks() {
        Invoice invoice1 = new Invoice();
        invoice1.setIdInvoice(1L);
        invoice1.setAmountInvoice(1000f);
        invoice1.setArchived(false);

        Invoice invoice2 = new Invoice();
        invoice2.setIdInvoice(2L);
        invoice2.setAmountInvoice(500f);
        invoice2.setArchived(false);

        Operator operator1 = new Operator();
        operator1.setIdOperateur(1L);
        operator1.setInvoices(new HashSet<>(Arrays.asList(invoice1)));

        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice1));
        when(invoiceRepository.findById(2L)).thenReturn(Optional.of(invoice2));
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator1));
    }

    @Test
    @DisplayName("Récupérer toutes les factures")
    public void testRetrieveAllInvoices() {
        List<Invoice> invoices = invoiceServiceImpl.retrieveAllInvoices();
        assertEquals(2, invoices.size(), "On doit récupérer 2 factures.");
    }

    @Test
    @DisplayName("Annuler une facture en l'archivant")
    public void testCancelInvoice() {
        invoiceServiceImpl.cancelInvoice(1L);
        Invoice invoice = invoiceRepository.findById(1L).orElse(null);
        assertNotNull(invoice);
        assertTrue(invoice.getArchived());
    }

    @Test
    @DisplayName("Annuler une facture (Facture non trouvée)")
    public void testCancelInvoice_NotFound() {
        when(invoiceRepository.findById(999L)).thenReturn(Optional.empty());
           Exception exception = assertThrows(NullPointerException.class, () -> {
            invoiceServiceImpl.cancelInvoice(999L);
        });
        assertEquals("Invoice not found", exception.getMessage());
    }

    @Test
    @DisplayName("Assigner un opérateur à une facture")
    public void testAssignOperatorToInvoice() {
        invoiceServiceImpl.assignOperatorToInvoice(1L, 1L);
        Operator operator = operatorRepository.findById(1L).orElse(null);
        assertNotNull(operator);
        assertTrue(operator.getInvoices().stream().anyMatch(invoice -> invoice.getIdInvoice().equals(1L)));
    }

    @Test
    @DisplayName("Calculer la taxe d'une facture")
    public void testCalculateInvoiceTax() {
        float tax = invoiceServiceImpl.calculateInvoiceTax(1L);
        assertEquals(130.0, tax);
    }

    @Test
    @DisplayName("Calculer la taxe (Montant invalide)")
    public void testCalculateInvoiceTax_InvalidAmount() {
        Invoice invoice = invoiceRepository.findById(2L).orElse(null);
        assertNotNull(invoice);
        invoice.setAmountInvoice(-500f);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceServiceImpl.calculateInvoiceTax(2L);
        });
        assertEquals("Invalid invoice amount", exception.getMessage());
    }
}
