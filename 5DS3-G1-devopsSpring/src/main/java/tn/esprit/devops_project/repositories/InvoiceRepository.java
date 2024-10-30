package tn.esprit.devops_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;

import java.util.Date;
import java.util.List;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	
	@Query("SELECT i FROM Invoice i where i.supplier=:supplier and i.archived=false")
	public List<Invoice> retrieveInvoicesBySupplier(@Param("supplier") Supplier supplier);

	
	@Query("SELECT sum(i.amountInvoice) FROM Invoice i where  i.dateCreationInvoice between :startDate"
			+ " and :endDate and i.archived=false")
	float getTotalAmountInvoiceBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Modifying
	@Query("update Invoice i set i.archived=true where i.idInvoice=?1")
	void updateInvoice(Long id);

	@Query("SELECT i FROM Invoice i WHERE i.supplier = :supplier " +
			"AND i.dateCreationInvoice >= :startDate " +
			"AND i.dateCreationInvoice <= :endDate")
	List<Invoice> findInvoicesBySupplierAndDateRange(@Param("supplier") Supplier supplier,
													 @Param("startDate") Date startDate,
													 @Param("endDate") Date endDate);
	
}
