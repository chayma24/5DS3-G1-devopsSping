package tn.esprit.devops_project;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.services.iservices.IInvoiceService;
import tn.esprit.devops_project.services.iservices.ISupplierService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest  // No need to specify properties here
@ActiveProfiles("test")  // Ensure 'test' profile is active
class InvoiceServiceImplTest {

    @Autowired
    IInvoiceService invoiceService;

    @Autowired
    ISupplierService supplierService;

    private Supplier supplier;
    private Invoice invoice1;
    private Invoice invoice2;

    @Test
    void testApplyDiscountToSupplierInvoices() {
        // 1. Create supplier
        supplier = new Supplier();
        supplier.setLabel("Test Supplier");
        supplierService.addSupplier(supplier);
        System.out.println("Created supplier with ID: " + supplier.getIdSupplier());

        // 2. Create Invoice 1
        invoice1 = new Invoice();
        invoice1.setAmountInvoice(1000);
        invoice1.setDateCreationInvoice(new Date());
        invoice1.setArchived(Boolean.FALSE);
        invoice1.setSupplier(supplier);
        invoiceService.addInvoice(invoice1);

        // 3. Create Invoice 2
        invoice2 = new Invoice();
        invoice2.setAmountInvoice(2000);
        invoice2.setDateCreationInvoice(new Date());
        invoice2.setArchived(Boolean.FALSE);
        invoice2.setSupplier(supplier);
        invoiceService.addInvoice(invoice2);

        // 4. Apply 10% discount to both invoices
        invoiceService.applyDiscountToSupplierInvoices(
                supplier.getIdSupplier(), 10, new Date(0), new Date()
        );

        // 5. Fetch the updated invoices
        List<Invoice> updatedInvoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());

        double totalAmountDiscount = 0;
        double totalAmountInvoice = 0;

        // 6. Calculate the total discount and total invoice amount
        for (Invoice invoice : updatedInvoices) {
            totalAmountDiscount += invoice.getAmountDiscount();
            totalAmountInvoice += invoice.getAmountInvoice();
        }

        // 7. Assertions to verify the discount and total invoice amount
        assertEquals(300, totalAmountDiscount, 0.01,
                "Total discount for both invoices should be 300");
        assertEquals(2700, totalAmountInvoice, 0.01,
                "Total amount for both invoices should be 2700");

        // 8. Clean up: Delete invoices and supplier
        invoiceService.deleteInvoice(invoice1.getIdInvoice());
        invoiceService.deleteInvoice(invoice2.getIdInvoice());
        supplierService.deleteSupplier(supplier.getIdSupplier());
    }
}