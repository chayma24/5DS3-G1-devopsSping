package tn.esprit.devopsSpring_5DS3_G1.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devopsSpring_5DS3_G1.entities.Invoice;
import tn.esprit.devopsSpring_5DS3_G1.entities.Operator;
import tn.esprit.devopsSpring_5DS3_G1.entities.Supplier;
import tn.esprit.devopsSpring_5DS3_G1.repositories.InvoiceDetailRepository;
import tn.esprit.devopsSpring_5DS3_G1.repositories.InvoiceRepository;
import tn.esprit.devopsSpring_5DS3_G1.repositories.OperatorRepository;
import tn.esprit.devopsSpring_5DS3_G1.repositories.SupplierRepository;
import tn.esprit.devopsSpring_5DS3_G1.services.Iservices.IInvoiceService;

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
		//invoiceRepository.updateInvoice(invoiceId);
	}

	@Override
	public Invoice retrieveInvoice(Long invoiceId) {

		return invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException("Invoice not found"));
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

	// ---
	@Override
	public List<Invoice> getInvoicesByStatus(boolean isArchived) {
		return invoiceRepository.findByArchived(isArchived);
	}

	@Override
	public float calculateInvoiceTax(Long invoiceId) {
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new NullPointerException("Invoice not found"));
		if (invoice.getAmountInvoice() <= 0) {
			throw new IllegalArgumentException("Invalid invoice amount");
		}
		float taxRate = 0.13f;
		return invoice.getAmountInvoice() * taxRate;
	}

	@Override
	public long countInvoicesBySupplier(Long supplierId) {
		Supplier supplier = supplierRepository.findById(supplierId)
				.orElseThrow(() -> new NullPointerException("Supplier not found"));
		return supplier.getInvoices().size();
	}

	@Override
	public List<Invoice> findInvoicesByAmountGreaterThan(float amount) {
		return invoiceRepository.findByAmountInvoiceGreaterThan(amount);
	}
}