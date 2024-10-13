package tn.esprit.devopsSpring_5DS3_G1.services.Iservices;

import tn.esprit.devopsSpring_5DS3_G1.entities.Stock;

import java.util.List;

public interface IStockService {

    Stock addStock(Stock stock);
    Stock retrieveStock(Long id);
    List<Stock> retrieveAllStock();

}
