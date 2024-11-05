package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.dto.SupplierDTO;
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

	// Helper method to convert Supplier entity to DTO
	private SupplierDTO toDTO(Supplier supplier) {
		SupplierDTO dto = new SupplierDTO();
		dto.setCode(supplier.getCode());
		dto.setLabel(supplier.getLabel());
		dto.setSupplierCategory(supplier.getSupplierCategory().name());
		dto.setInvoiceIds(supplier.getInvoices()
				.stream()
				.map(Invoice::getIdInvoice)
				.collect(Collectors.toList()));
		return dto;
	}

	// Helper method to convert DTO to Supplier entity
	private Supplier toEntity(SupplierDTO dto) {
		Supplier supplier = new Supplier();
		supplier.setCode(dto.getCode());
		supplier.setLabel(dto.getLabel());
		supplier.setSupplierCategory(SupplierCategory.valueOf(dto.getSupplierCategory()));
		return supplier;
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
	public void removeSupplier(@PathVariable Long supplierId) {
		supplierService.deleteSupplier(supplierId);
	}

	@PutMapping("/supplier")
	public SupplierDTO modifySupplier(@RequestBody SupplierDTO supplierDTO) {
		Supplier supplier = toEntity(supplierDTO);
		Supplier updatedSupplier = supplierService.updateSupplier(supplier);
		return toDTO(updatedSupplier);
	}
}