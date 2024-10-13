package tn.esprit.devopsSpring_5DS3_G1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devopsSpring_5DS3_G1.entities.Stock;


public interface StockRepository extends JpaRepository<Stock, Long> {}

