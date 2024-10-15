package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.IInvoiceService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

	final InvoiceRepository invoiceRepository;
	final OperatorRepository operatorRepository;
	final InvoiceDetailRepository invoiceDetailRepository;
	final SupplierRepository supplierRepository;
	
	@Override
	public List<Invoice> retrieveAllInvoices() {
		return invoiceRepository.findAll();
	}
	@Override
	public void cancelInvoice(Long invoiceId) {
		// method 01
		Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException("Invoice not found"));
		invoice.setArchived(true);
		invoiceRepository.save(invoice);
		//method 02 (Avec JPQL)
		invoiceRepository.updateInvoice(invoiceId);
	}

	@Override
	public Invoice retrieveInvoice(Long invoiceId) {

		return invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException("Invoice not found"));
	}

	@Override
	public Invoice addInvoice(Invoice invoice) {

		return invoiceRepository.save(invoice);
	}

	@Override
	public List<Invoice> getInvoicesBySupplier(Long idSupplier) {
		Supplier supplier = supplierRepository.findById(idSupplier).orElseThrow(() -> new NullPointerException("Supplier not found"));
		return (List<Invoice>) supplier.getInvoices();
	}

	@Override
	public void assignOperatorToInvoice(Long idOperator, Long idInvoice) {
		Invoice invoice = invoiceRepository.findById(idInvoice).orElseThrow(() -> new NullPointerException("Invoice not found"));
		Operator operator = operatorRepository.findById(idOperator).orElseThrow(() -> new NullPointerException("Operator not found"));
		operator.getInvoices().add(invoice);
		operatorRepository.save(operator);
	}

	@Override
	public float getTotalAmountInvoiceBetweenDates(Date startDate, Date endDate) {
		return invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate);
	}

	@Override
	public void deleteInvoice(Long InvoiceId) {
		invoiceRepository.deleteById(InvoiceId);

	}

	@Override
	public void applyDiscountToSupplierInvoices(Long supplierId, float discountPercentage, Date startDate, Date endDate) {
		// Step 1: Retrieve the supplier
		Supplier supplier = supplierRepository.findById(supplierId)
				.orElseThrow(() -> new NullPointerException("Supplier not found"));

		// Step 2: Retrieve the invoices within the date range
		List<Invoice> invoices = invoiceRepository.findInvoicesBySupplierAndDateRange(supplier, startDate, endDate);

		// Step 3: Apply the discount
		for (Invoice invoice : invoices) {
			if (!invoice.getArchived()) {
				// Calculate the discount amount
				float discountAmount = invoice.getAmountInvoice() * (discountPercentage / 100);
				// Apply the discount
				invoice.setAmountDiscount(discountAmount);
				invoice.setAmountInvoice(invoice.getAmountInvoice() - discountAmount);
				// Update the last modification date
				invoice.setDateLastModificationInvoice(new Date());
				// Save the updated invoice
				invoiceRepository.save(invoice);
			}
		}
	}



}