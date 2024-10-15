package tn.esprit.devops_project;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.services.Iservices.IInvoiceService;
import tn.esprit.devops_project.services.Iservices.ISupplierService;

import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class InvoiceServiceImplTest {

    @Autowired
    IInvoiceService invoiceService;

    @Autowired
    ISupplierService supplierService;

    private Supplier supplier;
    private Invoice invoice1;
    private Invoice invoice2;

    @Test
    void testApplyDiscountToSupplierInvoices() {

        // Create supplier
        supplier = new Supplier();
        supplier.setLabel("Test Supplier");
        supplierService.addSupplier(supplier);
        System.out.println(supplier.getIdSupplier());

        // Create invoice 1
        invoice1 = new Invoice();
        invoice1.setAmountInvoice(1000);
        invoice1.setDateCreationInvoice(new Date());
        invoice1.setArchived(Boolean.FALSE);
        invoice1.setSupplier(supplier);
        invoiceService.addInvoice(invoice1);

        // Create invoice 2
        invoice2 = new Invoice();
        invoice2.setAmountInvoice(2000);
        invoice2.setDateCreationInvoice(new Date());
        invoice2.setArchived(Boolean.FALSE);
        invoice2.setSupplier(supplier);
        invoiceService.addInvoice(invoice2);

        // Apply 10% discount
        invoiceService.applyDiscountToSupplierInvoices(supplier.getIdSupplier(), 10, new Date(0), new Date());

        // Fetch updated invoices from the repository
        List<Invoice> updatedInvoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());

        // Variable to store total discount
        double totalAmountDiscount = 0;
        double totalAmountInvoice = 0;

        // Check the amountDiscount and amountInvoice for each invoice
        for (Invoice invoice : updatedInvoices) {
            // Calculate total discount
            totalAmountDiscount += invoice.getAmountDiscount();
            totalAmountInvoice += invoice.getAmountInvoice();

        }

        // Assert the total discount amount
        assertEquals(300, totalAmountDiscount, 0.01, "Total discount for both invoices should be 300");
        assertEquals(2700, totalAmountInvoice, 0.01, "Total amount for both invoices should be 1700");
        // Clean up
        invoiceService.deleteInvoice(invoice1.getIdInvoice());
        invoiceService.deleteInvoice(invoice2.getIdInvoice());
        supplierService.deleteSupplier(supplier.getIdSupplier());
    }

}
