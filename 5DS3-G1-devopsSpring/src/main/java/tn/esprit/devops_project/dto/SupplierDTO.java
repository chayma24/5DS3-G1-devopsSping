package tn.esprit.devops_project.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SupplierDTO {
    private String code;
    private String label;
    private String supplierCategory;
    private List<Long> invoiceIds;
}