package tn.esprit.devopsSpring_5DS3_G1.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devopsSpring_5DS3_G1.entities.Supplier;
import tn.esprit.devopsSpring_5DS3_G1.services.Iservices.ISupplierService;
import java.util.List;


@RestController
@AllArgsConstructor
public class SupplierController {

	ISupplierService supplierService;

	@GetMapping("/supplier")
	public List<Supplier> getSuppliers() {
		return supplierService.retrieveAllSuppliers();
	}

	@GetMapping("/supplier/{supplierId}")
	public Supplier retrieveSupplier(@PathVariable Long supplierId) {
		return supplierService.retrieveSupplier(supplierId);
	}

	@PostMapping("/supplier")
	public Supplier addSupplier(@RequestBody Supplier supplier) {
		return supplierService.addSupplier(supplier);
	}

	@DeleteMapping("/supplier/{supplierId}")
	public void removeFournisseur(@PathVariable Long supplierId) {
		supplierService.deleteSupplier(supplierId);
	}

	@PutMapping("/supplier")
	public Supplier modifyFournisseur(@RequestBody Supplier supplier) {
		return supplierService.updateSupplier(supplier);
	}

}
