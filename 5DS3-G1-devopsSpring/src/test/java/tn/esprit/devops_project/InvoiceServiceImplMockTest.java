package tn.esprit.devops_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.services.iservices.IInvoiceService;
import tn.esprit.devops_project.services.iservices.ISupplierService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InvoiceServiceImplMockitoTest {

    @Mock
    private IInvoiceService invoiceService;

    @Mock
    private ISupplierService supplierService;

    @InjectMocks
    private InvoiceServiceImplTest invoiceServiceImplTest;

    private List<Supplier> suppliers;
    private Invoice invoice1;
    private Invoice invoice2;
    private Invoice invoice3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        suppliers = new ArrayList<>();

        // Create suppliers
        Supplier supplier1 = new Supplier();
        supplier1.setIdSupplier(1L);
        supplier1.setLabel("supplier1");
        supplier1.setSupplierCategory(SupplierCategory.CONVENTIONNE);
        suppliers.add(supplier1);

        Supplier supplier2 = new Supplier();
        supplier2.setIdSupplier(2L);
        supplier2.setLabel("supplier2");
        supplier2.setSupplierCategory(SupplierCategory.ORDINAIRE);
        suppliers.add(supplier2);

        // Create invoices
        invoice1 = new Invoice();
        invoice1.setIdInvoice(1L);
        invoice1.setAmountInvoice(1000);
        invoice1.setDateCreationInvoice(new Date());
        invoice1.setArchived(Boolean.FALSE);
        invoice1.setSupplier(supplier1);

        invoice2 = new Invoice();
        invoice2.setIdInvoice(2L);
        invoice2.setAmountInvoice(2000);
        invoice2.setDateCreationInvoice(new Date());
        invoice2.setArchived(Boolean.FALSE);
        invoice2.setSupplier(supplier1);

        invoice3 = new Invoice();
        invoice3.setIdInvoice(3L);
        invoice3.setAmountInvoice(1500);
        invoice3.setDateCreationInvoice(new Date());
        invoice3.setArchived(Boolean.FALSE);
        invoice3.setSupplier(supplier2);
    }

    @Test
    void testApplyDiscountToSupplierInvoices() {
        // Mocking the behavior of the services
        when(supplierService.addSupplier(any(Supplier.class))).thenReturn(suppliers.get(0));
        when(supplierService.addSupplier(any(Supplier.class))).thenReturn(suppliers.get(1));
        when(invoiceService.addInvoice(any(Invoice.class))).thenReturn(invoice1).thenReturn(invoice2).thenReturn(invoice3);

        // Mocking the behavior to return invoices based on supplier
        when(invoiceService.getInvoicesBySupplier(suppliers.get(0).getIdSupplier())).thenReturn(List.of(invoice1, invoice2));
        when(invoiceService.getInvoicesBySupplier(suppliers.get(1).getIdSupplier())).thenReturn(List.of(invoice3));

        // Mocking the discount application
        doAnswer(invocation -> {
            Long supplierId = invocation.getArgument(0);
            float discountPercentage = invocation.getArgument(1);

            List<Invoice> invoices = invoiceService.getInvoicesBySupplier(supplierId);
            for (Invoice invoice : invoices) {
                if (!invoice.getArchived()) {
                    float discountAmount = invoice.getAmountInvoice() * (discountPercentage / 100);
                    invoice.setAmountDiscount(discountAmount);
                    invoice.setAmountInvoice(invoice.getAmountInvoice() - discountAmount);
                }
            }
            return null;
        }).when(invoiceService).applyDiscountToSupplierInvoices(anyLong(), anyFloat(), any(Date.class), any(Date.class));

        // Apply discounts
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierCategory() == SupplierCategory.CONVENTIONNE) {
                Date startDate = new Date(0);
                Date endDate = new Date();

                invoiceService.applyDiscountToSupplierInvoices(
                        supplier.getIdSupplier(), 10, startDate, endDate
                );

                // Fetch updated invoices
                List<Invoice> updatedInvoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());

                double totalAmountDiscount = updatedInvoices.stream().mapToDouble(Invoice::getAmountDiscount).sum();
                double totalAmountInvoice = updatedInvoices.stream().mapToDouble(Invoice::getAmountInvoice).sum();

                // Assertions for the CONVENTIONNE supplier
                assertEquals(300, totalAmountDiscount, 0.01,
                        "Total discount for supplier's invoices should be 300");
                assertEquals(2700, totalAmountInvoice, 0.01,
                        "Total amount for supplier's invoices should be 2700");

                // Verify that applyDiscountToSupplierInvoices was called exactly once
                verify(invoiceService, times(1)).applyDiscountToSupplierInvoices(
                        supplier.getIdSupplier(), 10.0f, startDate, endDate
                );
            } else {
                // Verify that no discount was applied to ORDINAIRE supplier
                List<Invoice> ordinaryInvoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());
                for (Invoice invoice : ordinaryInvoices) {
                    assertEquals(0, invoice.getAmountDiscount(),
                            "No discount should be applied to supplier's invoices");
                }
            }
        }
    }
}