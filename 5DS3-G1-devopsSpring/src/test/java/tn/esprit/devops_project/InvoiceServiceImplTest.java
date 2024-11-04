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
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.services.iservices.IInvoiceService;
import tn.esprit.devops_project.services.iservices.ISupplierService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ActiveProfiles("test")
class InvoiceServiceImplTest {

    @Autowired
    IInvoiceService invoiceService;

    @Autowired
    ISupplierService supplierService;

    private List<Supplier> suppliers = new ArrayList<>();
    private Invoice invoice1;
    private Invoice invoice2;
    private Invoice invoice3;

    @Test
    void testApplyDiscountToSupplierInvoices() {
        // 1. Create supplier1 with CONVENTIONNE category
        Supplier supplier1 = new Supplier();
        supplier1.setLabel("supplier1");
        supplier1.setSupplierCategory(SupplierCategory.CONVENTIONNE);
        supplierService.addSupplier(supplier1);
        suppliers.add(supplier1);

        // 2. Create supplier2 with ORDINAIRE category
        Supplier supplier2 = new Supplier();
        supplier2.setLabel("supplier2");
        supplier2.setSupplierCategory(SupplierCategory.ORDINAIRE);
        supplierService.addSupplier(supplier2);
        suppliers.add(supplier2);

        // 3. Create invoices and assign them to suppliers
        invoice1 = new Invoice();
        invoice1.setAmountInvoice(1000);
        invoice1.setDateCreationInvoice(new Date());
        invoice1.setArchived(Boolean.FALSE);
        invoice1.setSupplier(supplier1);
        invoiceService.addInvoice(invoice1);

        invoice2 = new Invoice();
        invoice2.setAmountInvoice(2000);
        invoice2.setDateCreationInvoice(new Date());
        invoice2.setArchived(Boolean.FALSE);
        invoice2.setSupplier(supplier1);
        invoiceService.addInvoice(invoice2);

        invoice3 = new Invoice();
        invoice3.setAmountInvoice(1500);
        invoice3.setDateCreationInvoice(new Date());
        invoice3.setArchived(Boolean.FALSE);
        invoice3.setSupplier(supplier2);
        invoiceService.addInvoice(invoice3);

        // 4. Iterate through suppliers and apply discount only if the supplier is CONVENTIONNE
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierCategory() == SupplierCategory.CONVENTIONNE) {
                invoiceService.applyDiscountToSupplierInvoices(
                        supplier.getIdSupplier(), 10, new Date(0), new Date()
                );

                // 5. Fetch updated invoices for the CONVENTIONNE supplier
                List<Invoice> updatedInvoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());

                double totalAmountDiscount = 0;
                double totalAmountInvoice = 0;

                // 6. Calculate the total discount and total invoice amount
                for (Invoice invoice : updatedInvoices) {
                    totalAmountDiscount += invoice.getAmountDiscount();
                    totalAmountInvoice += invoice.getAmountInvoice();
                }

                // 7. Assertions for the CONVENTIONNE supplier
                assertEquals(300, totalAmountDiscount, 0.01,
                        "Total discount for supplier's invoices should be 300");
                assertEquals(2700, totalAmountInvoice, 0.01,
                        "Total amount for supplier's invoices should be 2700");
            } else {
                // 8. Verify that no discount was applied to ORDINAIRE supplier
                List<Invoice> ordinaryInvoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());
                for (Invoice invoice : ordinaryInvoices) {
                    assertEquals(0, invoice.getAmountDiscount(),
                            "No discount should be applied to supplier's invoices");
                }
            }
        }

        // 9. Clean up: Delete invoices and suppliers
        invoiceService.deleteInvoice(invoice1.getIdInvoice());
        invoiceService.deleteInvoice(invoice2.getIdInvoice());
        invoiceService.deleteInvoice(invoice3.getIdInvoice());
        supplierService.deleteSupplier(supplier1.getIdSupplier());
        supplierService.deleteSupplier(supplier2.getIdSupplier());
    }
}
