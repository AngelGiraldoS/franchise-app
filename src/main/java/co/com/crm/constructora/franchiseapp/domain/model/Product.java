package co.com.crm.constructora.franchiseapp.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "franchises_collection")
public class Product {
    private String id;
    private String name;
    private int stock;

}