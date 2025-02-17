package tn.esprit.devopsSpring_5DS3_G1.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devopsSpring_5DS3_G1.entities.Supplier;
import tn.esprit.devopsSpring_5DS3_G1.repositories.SupplierRepository;
import tn.esprit.devopsSpring_5DS3_G1.services.Iservices.ISupplierService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SupplierServiceImpl implements ISupplierService {

	SupplierRepository supplierRepository;

	@Override
	public List<Supplier> retrieveAllSuppliers() {
		return supplierRepository.findAll();
	}


	@Override
	public Supplier addSupplier(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public Supplier updateSupplier(Supplier supplier) {
		return  supplierRepository.save(supplier);
	}

	@Override
	public void deleteSupplier(Long SupplierId) {
		supplierRepository.deleteById(SupplierId);

	}

	@Override
	public Supplier retrieveSupplier(Long supplierId) {

		return supplierRepository.findById(supplierId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + supplierId));
	}


}