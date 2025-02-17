package tn.esprit.devopsSpring_5DS3_G1.services.Iservices;

import tn.esprit.devopsSpring_5DS3_G1.entities.Supplier;

import java.util.List;

public interface ISupplierService {

	List<Supplier> retrieveAllSuppliers();

	Supplier addSupplier(Supplier supplier);

	void deleteSupplier(Long id);

	Supplier updateSupplier(Supplier supplier);

	Supplier retrieveSupplier(Long id);

}
