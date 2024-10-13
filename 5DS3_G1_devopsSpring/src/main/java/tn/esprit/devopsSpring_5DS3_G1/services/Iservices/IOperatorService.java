package tn.esprit.devopsSpring_5DS3_G1.services.Iservices;

import tn.esprit.devopsSpring_5DS3_G1.entities.Operator;

import java.util.List;


public interface IOperatorService {

	List<Operator> retrieveAllOperators();

	Operator addOperator(Operator operator);

	void deleteOperator(Long id);

	Operator updateOperator(Operator operator);

	Operator retrieveOperator(Long id);

}
