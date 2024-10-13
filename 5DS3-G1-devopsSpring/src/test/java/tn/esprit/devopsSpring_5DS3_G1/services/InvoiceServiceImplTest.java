package tn.esprit.devopsSpring_5DS3_G1.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devopsSpring_5DS3_G1.entities.Invoice;
import tn.esprit.devopsSpring_5DS3_G1.entities.Operator;
import tn.esprit.devopsSpring_5DS3_G1.repositories.InvoiceRepository;
import tn.esprit.devopsSpring_5DS3_G1.repositories.OperatorRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InvoiceServiceImplTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    private InvoiceServiceImpl invoiceServiceImpl;

    @BeforeEach
    public void setUp() {
        invoiceServiceImpl = new InvoiceServiceImpl(invoiceRepository, operatorRepository, null, null);
        setupData();
    }

    private void setupData() {
        Invoice invoice1 = new Invoice();
        invoice1.setIdInvoice(1L);
        invoice1.setAmountInvoice(1000f);
        invoice1.setArchived(false);
        invoiceRepository.save(invoice1);

        Invoice invoice2 = new Invoice();
        invoice2.setIdInvoice(2L);
        invoice2.setAmountInvoice(500f);
        invoice2.setArchived(false);
        invoiceRepository.save(invoice2);

        Operator operator1 = new Operator();
        operator1.setIdOperateur(1L);
        operator1.setInvoices(new HashSet<>());
        operatorRepository.save(operator1);
    }

    // Test: Retrieve all invoices
    @Test
    @DisplayName("Test: Retrieve all invoices")
    public void testRetrieveAllInvoices() {

        List<Invoice> invoices = invoiceServiceImpl.retrieveAllInvoices();
        assertEquals(2, invoices.size());
    }

    // Test: Cancel an invoice
    @Test
    @DisplayName("Cancel an invoice by archiving it")
    public void testCancelInvoice() {

        invoiceServiceImpl.cancelInvoice(1L);
        Invoice invoice = invoiceRepository.findById(1L).orElse(null);
        assertNotNull(invoice);
        assertTrue(invoice.getArchived());
    }

    // Test: Cancel an invoice (Invoice not found)
    @Test
    @DisplayName("Cancel an invoice - Invoice not found")
    public void testCancelInvoice_NotFound() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            invoiceServiceImpl.cancelInvoice(999L);
        });
        assertEquals("Invoice not found", exception.getMessage());
    }

    // Test: Assign operator to an invoice
    @Test
    @DisplayName("Assign operator to an invoice")
    @Transactional
    public void testAssignOperatorToInvoice() {

        invoiceServiceImpl.assignOperatorToInvoice(1L, 1L);
        Operator operator = operatorRepository.findById(1L).orElse(null);
        assertNotNull(operator);
        assertTrue(operator.getInvoices().stream().anyMatch(invoice -> invoice.getIdInvoice() == 1L));
    }

    // Test: Calculate invoice tax
    @Test
    @DisplayName("Calculate tax for an invoice")
    public void testCalculateInvoiceTax() {

        float tax = invoiceServiceImpl.calculateInvoiceTax(1L);
        assertEquals(130.0, tax);
    }

    // Test: Calculate invoice tax (Invalid amount)
    @Test
    @DisplayName("Calculate tax - Invalid invoice amount")
    public void testCalculateInvoiceTax_InvalidAmount() {

        Invoice invoice = invoiceRepository.findById(2L).orElse(null);
        assertNotNull(invoice);
        invoice.setAmountInvoice(-500f);
        invoiceRepository.save(invoice);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceServiceImpl.calculateInvoiceTax(2L);
        });
        assertEquals("Invalid invoice amount", exception.getMessage());
    }

}
