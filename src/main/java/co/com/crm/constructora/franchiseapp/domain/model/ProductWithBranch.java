package co.com.crm.constructora.franchiseapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithBranch {
    private String branchName;
    private Product product;
}