package co.com.crm.constructora.franchiseapp.domain.ports;


import co.com.crm.constructora.franchiseapp.domain.model.Branch;
import co.com.crm.constructora.franchiseapp.domain.model.Franchise;
import co.com.crm.constructora.franchiseapp.domain.model.Product;
import co.com.crm.constructora.franchiseapp.domain.model.ProductWithBranch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FranchiseService {
    Mono<Franchise> createFranchise(Franchise franchise);
    Mono<Franchise> getFranchiseById(String id);
    Flux<Franchise> getAllFranchises();
    Mono<Void> deleteFranchise(String id);
    Mono<Franchise> addBranch(String franchiseId, Branch branch);
    Flux<Branch> getBranchesByFranchiseId(String franchiseId);
    Mono<List<ProductWithBranch>> getMaxStockProductsByFranchise(String franchiseId);
    Mono<Franchise> updateFranchiseName(String franchiseId, String newName);
    Mono<Branch> updateBranchName(String franchiseId, String branchId, String newName);
    Mono<Product> updateProductName(String franchiseId, String branchId, String productId, String newName);
    Mono<Void> deleteProductFromBranch(String franchiseId, String branchId, String productId);
    Mono<Product> modifyProductStock(String franchiseId, String branchId, String productId, int newStock);

}