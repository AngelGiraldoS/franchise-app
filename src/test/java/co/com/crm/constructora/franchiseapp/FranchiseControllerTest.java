package co.com.crm.constructora.franchiseapp;


import co.com.crm.constructora.franchiseapp.domain.model.Branch;
import co.com.crm.constructora.franchiseapp.domain.model.Franchise;
import co.com.crm.constructora.franchiseapp.domain.model.Product;
import co.com.crm.constructora.franchiseapp.domain.model.ProductWithBranch;
import co.com.crm.constructora.franchiseapp.domain.ports.FranchiseService;
import co.com.crm.constructora.franchiseapp.infraestructure.adapters.rest.FranchiseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseControllerTest {

    @Mock
    private FranchiseService franchiseService;

    @InjectMocks
    private FranchiseController franchiseController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(franchiseController).build();
    }

    @Test
    void createFranchise_Success() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");

        when(franchiseService.createFranchise(any(Franchise.class))).thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(franchise)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Franchise.class)
                .isEqualTo(franchise);
    }

    @Test
    void getFranchiseById_Success() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");

        when(franchiseService.getFranchiseById("1")).thenReturn(Mono.just(franchise));

        webTestClient.get()
                .uri("/franchises/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franchise.class)
                .isEqualTo(franchise);
    }

    @Test
    void getAllFranchises_Success() {
        Franchise franchise1 = new Franchise();
        franchise1.setId("1");
        franchise1.setName("Franchise 1");

        Franchise franchise2 = new Franchise();
        franchise2.setId("2");
        franchise2.setName("Franchise 2");

        when(franchiseService.getAllFranchises()).thenReturn(Flux.just(franchise1, franchise2));

        webTestClient.get()
                .uri("/franchises")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Franchise.class)
                .hasSize(2)
                .contains(franchise1, franchise2);
    }

    @Test
    void deleteFranchise_Success() {
        when(franchiseService.deleteFranchise("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/franchises/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void addBranch_Success() {
        Branch branch = new Branch();
        branch.setId("b1");
        branch.setName("Branch 1");

        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");
        franchise.setBranches(new ArrayList<>());

        Franchise updatedFranchise = new Franchise();
        updatedFranchise.setId("1");
        updatedFranchise.setName("Franchise 1");
        updatedFranchise.setBranches(Collections.singletonList(branch));


        when(franchiseService.addBranch(anyString(), any(Branch.class))).thenReturn(Mono.just(updatedFranchise));

        webTestClient.post()
                .uri("/franchises/1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branch)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franchise.class)
                .isEqualTo(updatedFranchise);
    }

    @Test
    void addBranch_FranchiseNotFound() {
        Branch branch = new Branch();
        branch.setId("b1");
        branch.setName("Branch 1");

        when(franchiseService.addBranch(anyString(), any(Branch.class))).thenReturn(Mono.error(new Exception("Franchise not found")));

        webTestClient.post()
                .uri("/franchises/1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branch)
                .exchange()
                .expectStatus().is5xxServerError();
    }


    @Test
    void getBranchesByFranchiseId_Success() {
        Branch branch1 = new Branch();
        branch1.setId("b1");
        branch1.setName("Branch 1");

        Branch branch2 = new Branch();
        branch2.setId("b2");
        branch2.setName("Branch 2");

        when(franchiseService.getBranchesByFranchiseId("1")).thenReturn(Flux.just(branch1, branch2));

        webTestClient.get()
                .uri("/franchises/1/branches")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Branch.class)
                .hasSize(2)
                .contains(branch1, branch2);
    }

    @Test
    void getMaxStockProductsByFranchise_Success() {
        Branch branch1 = new Branch();
        branch1.setId("b1");
        branch1.setName("Branch 1");
        Product product1 = new Product();
        product1.setId("p1");
        product1.setName("Product 1");
        product1.setStock(10);
        branch1.setProducts(Collections.singletonList(product1));

        Branch branch2 = new Branch();
        branch2.setId("b2");
        branch2.setName("Branch 2");
        Product product2 = new Product();
        product2.setId("p2");
        product2.setName("Product 2");
        product2.setStock(20);
        branch2.setProducts(Collections.singletonList(product2));

        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");
        franchise.setBranches(Arrays.asList(branch1, branch2));

        ProductWithBranch pwb1 = new ProductWithBranch("Branch 1", product1);
        ProductWithBranch pwb2 = new ProductWithBranch("Branch 2", product2);


        when(franchiseService.getMaxStockProductsByFranchise("1")).thenReturn(Mono.just(List.of(pwb1, pwb2)));

        webTestClient.get()
                .uri("/franchises/1/products/max-stock")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductWithBranch.class)
                .hasSize(2)
                .contains(pwb1, pwb2);
    }
    @Test
    void getMaxStockProductsByFranchise_FranchiseNotFound() {
        when(franchiseService.getMaxStockProductsByFranchise("1")).thenReturn(Mono.error(new Exception("Franchise not found")));

        webTestClient.get()
                .uri("/franchises/1/products/max-stock")
                .exchange()
                .expectStatus().is5xxServerError();
    }


    @Test
    void updateFranchiseName_Success() {
        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");

        Franchise updatedFranchise = new Franchise();
        updatedFranchise.setId("1");
        updatedFranchise.setName("New Franchise Name");

        when(franchiseService.updateFranchiseName("1", "New Franchise Name")).thenReturn(Mono.just(updatedFranchise));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/name")
                        .queryParam("newName", "New Franchise Name")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Franchise.class)
                .isEqualTo(updatedFranchise);
    }

    @Test
    void updateFranchiseName_FranchiseNotFound() {
        when(franchiseService.updateFranchiseName("1", "New Franchise Name")).thenReturn(Mono.error(new Exception("Franchise not found")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/name")
                        .queryParam("newName", "New Franchise Name")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void updateBranchName_Success() {
        Branch branch = new Branch();
        branch.setId("b1");
        branch.setName("Branch 1");

        Branch updatedBranch = new Branch();
        updatedBranch.setId("b1");
        updatedBranch.setName("New Branch Name");

        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");
        franchise.setBranches(Collections.singletonList(branch));

        when(franchiseService.updateBranchName("1", "b1", "New Branch Name")).thenReturn(Mono.just(updatedBranch));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/name")
                        .queryParam("newName", "New Branch Name")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Branch.class)
                .isEqualTo(updatedBranch);
    }

    @Test
    void updateBranchName_FranchiseNotFound() {
        when(franchiseService.updateBranchName("1", "b1", "New Branch Name")).thenReturn(Mono.error(new Exception("Franchise not found")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/name")
                        .queryParam("newName", "New Branch Name")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void updateBranchName_BranchNotFound() {
        when(franchiseService.updateBranchName("1", "b1", "New Branch Name")).thenReturn(Mono.error(new Exception("Branch not found in this franchise")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/name")
                        .queryParam("newName", "New Branch Name")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }
    @Test
    void updateProductName_Success() {
        Product product = new Product();
        product.setId("p1");
        product.setName("Product 1");

        Product updatedProduct = new Product();
        updatedProduct.setId("p1");
        updatedProduct.setName("New Product Name");

        Branch branch = new Branch();
        branch.setId("b1");
        branch.setName("Branch 1");
        branch.setProducts(Collections.singletonList(product));

        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");
        franchise.setBranches(Collections.singletonList(branch));

        when(franchiseService.updateProductName("1", "b1", "p1", "New Product Name")).thenReturn(Mono.just(updatedProduct));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/name")
                        .queryParam("newName", "New Product Name")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(updatedProduct);
    }

    @Test
    void updateProductName_FranchiseNotFound() {
        when(franchiseService.updateProductName("1", "b1", "p1", "New Product Name")).thenReturn(Mono.error(new Exception("Franchise not found")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/name")
                        .queryParam("newName", "New Product Name")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void updateProductName_BranchNotFound() {
        when(franchiseService.updateProductName("1", "b1", "p1", "New Product Name")).thenReturn(Mono.error(new Exception("Branch not found in this franchise")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/name")
                        .queryParam("newName", "New Product Name")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void updateProductName_ProductNotFound() {
        when(franchiseService.updateProductName("1", "b1", "p1", "New Product Name")).thenReturn(Mono.error(new Exception("Product not found in this branch")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/name")
                        .queryParam("newName", "New Product Name")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }


    @Test
    void modifyProductStock_Success() {
        Product product = new Product();
        product.setId("p1");
        product.setName("Product 1");
        product.setStock(10);

        Product updatedProduct = new Product();
        updatedProduct.setId("p1");
        updatedProduct.setName("Product 1");
        updatedProduct.setStock(20);

        Branch branch = new Branch();
        branch.setId("b1");
        branch.setName("Branch 1");
        branch.setProducts(Collections.singletonList(product));

        Franchise franchise = new Franchise();
        franchise.setId("1");
        franchise.setName("Franchise 1");
        franchise.setBranches(Collections.singletonList(branch));

        when(franchiseService.modifyProductStock("1", "b1", "p1", 20)).thenReturn(Mono.just(updatedProduct));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/stock")
                        .queryParam("newStock", 20)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .isEqualTo(updatedProduct);
    }

    @Test
    void modifyProductStock_FranchiseNotFound() {
        when(franchiseService.modifyProductStock("1", "b1", "p1", 20)).thenReturn(Mono.error(new Exception("Franchise not found")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/stock")
                        .queryParam("newStock", 20)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void modifyProductStock_BranchNotFound() {
        when(franchiseService.modifyProductStock("1", "b1", "p1", 20)).thenReturn(Mono.error(new Exception("Branch not found in this franchise")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/stock")
                        .queryParam("newStock", 20)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void modifyProductStock_ProductNotFound() {
        when(franchiseService.modifyProductStock("1", "b1", "p1", 20)).thenReturn(Mono.error(new Exception("Product not found in this branch")));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/franchises/1/branches/b1/products/p1/stock")
                        .queryParam("newStock", 20)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }
}