package tn.esprit.devops_project.services.Iservices;

import tn.esprit.devops_project.entities.Invoice;

import java.util.Date;
import java.util.List;

public interface IInvoiceService {
	List<Invoice> retrieveAllInvoices();

	List<Invoice> getInvoicesBySupplier(Long idSupplier);

	void cancelInvoice(Long id);

	Invoice retrieveInvoice(Long id);

	Invoice addInvoice(Invoice invoice);
	
	void assignOperatorToInvoice(Long idOperator, Long idInvoice);

	float getTotalAmountInvoiceBetweenDates(Date startDate, Date endDate);

	void applyDiscountToSupplierInvoices(Long supplierId, float discountPercentage, Date startDate, Date endDate);

	void deleteInvoice(Long InvoiceId);

	}
