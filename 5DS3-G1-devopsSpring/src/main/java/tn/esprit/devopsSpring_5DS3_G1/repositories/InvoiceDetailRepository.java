package tn.esprit.devopsSpring_5DS3_G1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devopsSpring_5DS3_G1.entities.InvoiceDetail;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

}
