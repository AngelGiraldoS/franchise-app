package co.com.crm.constructora.franchiseapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "franchises_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    private String id;
    private String name;
    private List<Product> products;

}