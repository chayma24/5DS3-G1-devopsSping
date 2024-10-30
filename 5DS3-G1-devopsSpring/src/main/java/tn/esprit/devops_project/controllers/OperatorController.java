package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.services.iservices.IOperatorService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class OperatorController {

	private final IOperatorService operatorService;

	// Inner DTO class to encapsulate Operator data
	public static class OperatorDTO {
		 String fname;
		 String lname;
		 Set<Long> invoiceIds; // Only expose invoice IDs, not the entire Invoice entity
	}

	// Helper method to convert Operator entity to DTO
	private OperatorDTO toDTO(Operator operator) {
		OperatorDTO dto = new OperatorDTO();
		dto.fname = operator.getFname();
		dto.lname = operator.getLname();
		dto.invoiceIds = operator.getInvoices()
				.stream()
				.map(Invoice::getIdInvoice)
				.collect(Collectors.toSet());
		return dto;
	}

	// Helper method to convert DTO to Operator entity
	private Operator toEntity(OperatorDTO dto) {
		Operator operator = new Operator();
		operator.setFname(dto.fname);
		operator.setLname(dto.lname);
		return operator; // Password and invoices can be managed elsewhere
	}

	@GetMapping("/operator")
	public List<OperatorDTO> getOperators() {
		return operatorService.retrieveAllOperators()
				.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/operator/{operatorId}")
	public OperatorDTO retrieveOperator(@PathVariable Long operatorId) {
		Operator operator = operatorService.retrieveOperator(operatorId);
		return toDTO(operator);
	}

	@PostMapping("/operator")
	public OperatorDTO addOperator(@RequestBody OperatorDTO operatorDTO) {
		Operator operator = toEntity(operatorDTO);
		Operator savedOperator = operatorService.addOperator(operator);
		return toDTO(savedOperator);
	}

	@DeleteMapping("/operator/{operatorId}")
	public void removeOperator(@PathVariable Long operatorId) {
		operatorService.deleteOperator(operatorId);
	}

	@PutMapping("/operator")
	public OperatorDTO modifyOperator(@RequestBody OperatorDTO operatorDTO) {
		Operator operator = toEntity(operatorDTO);
		Operator updatedOperator = operatorService.updateOperator(operator);
		return toDTO(updatedOperator);
	}
}