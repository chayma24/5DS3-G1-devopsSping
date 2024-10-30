package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.services.iservices.ISupplierService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class SupplierController {

	private final ISupplierService supplierService;

	// Inner DTO class to encapsulate Supplier data
	public static class SupplierDTO {
		 String code;
		 String label;
		 String supplierCategory; // Representing the enum as a String
		 List<Long> invoiceIds;   // Only exposing invoice IDs, not the full Invoice objects
	}

	// Helper method to convert Supplier entity to DTO
	private SupplierDTO toDTO(Supplier supplier) {
		SupplierDTO dto = new SupplierDTO();
		dto.code = supplier.getCode();
		dto.label = supplier.getLabel();
		dto.supplierCategory = supplier.getSupplierCategory().name();
		dto.invoiceIds = supplier.getInvoices()
				.stream()
				.map(Invoice::getIdInvoice)  // Method reference used here
				.collect(Collectors.toList());
		return dto;
	}

	// Helper method to convert DTO to Supplier entity
	private Supplier toEntity(SupplierDTO dto) {
		Supplier supplier = new Supplier();
		supplier.setCode(dto.code);
		supplier.setLabel(dto.label);
		supplier.setSupplierCategory(SupplierCategory.valueOf(dto.supplierCategory));
		return supplier;  // Invoices are managed separately and not set here
	}

	@GetMapping("/supplier")
	public List<SupplierDTO> getSuppliers() {
		return supplierService.retrieveAllSuppliers()
				.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/supplier/{supplierId}")
	public SupplierDTO retrieveSupplier(@PathVariable Long supplierId) {
		Supplier supplier = supplierService.retrieveSupplier(supplierId);
		return toDTO(supplier);
	}

	@PostMapping("/supplier")
	public SupplierDTO addSupplier(@RequestBody SupplierDTO supplierDTO) {
		Supplier supplier = toEntity(supplierDTO);
		Supplier savedSupplier = supplierService.addSupplier(supplier);
		return toDTO(savedSupplier);
	}

	@DeleteMapping("/supplier/{supplierId}")
	public void removeFournisseur(@PathVariable Long supplierId) {
		supplierService.deleteSupplier(supplierId);
	}

	@PutMapping("/supplier")
	public SupplierDTO modifyFournisseur(@RequestBody SupplierDTO supplierDTO) {
		Supplier supplier = toEntity(supplierDTO);
		Supplier updatedSupplier = supplierService.updateSupplier(supplier);
		return toDTO(updatedSupplier);
	}
}