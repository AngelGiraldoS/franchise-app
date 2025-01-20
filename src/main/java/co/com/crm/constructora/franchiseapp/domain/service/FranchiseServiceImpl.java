package co.com.crm.constructora.franchiseapp.domain.service;

import co.com.crm.constructora.franchiseapp.domain.model.Branch;
import co.com.crm.constructora.franchiseapp.domain.model.Franchise;
import co.com.crm.constructora.franchiseapp.domain.model.Product;
import co.com.crm.constructora.franchiseapp.domain.model.ProductWithBranch;
import co.com.crm.constructora.franchiseapp.domain.ports.FranchiseService;
import co.com.crm.constructora.franchiseapp.infraestructure.adapters.persistence.FranchiseRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;


    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseRepository.save(franchise);
    }

    @Override
    public Mono<Franchise> getFranchiseById(String id) {
        return franchiseRepository.findById(id);
    }

    @Override
    public Flux<Franchise> getAllFranchises() {
        return franchiseRepository.findAll();
    }


    @Override
    public Mono<Void> deleteFranchise(String id) {
        return franchiseRepository.deleteById(id);
    }


    @Override
    public Mono<Franchise> addBranch(String franchiseId, Branch branch) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    List<Branch> branches = Optional.ofNullable(franchise.getBranches())
                            .orElseGet(ArrayList::new);
                    branches.add(branch);
                    franchise.setBranches(branches);
                    return franchiseRepository.save(franchise);
                })
                .switchIfEmpty(Mono.error(new Exception("Franchise not found")));
    }

    @Override
    public Flux<Branch> getBranchesByFranchiseId(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()));
    }

    @Override
    public Mono<List<ProductWithBranch>> getMaxStockProductsByFranchise(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> Flux.fromIterable(franchise.getBranches())
                        .flatMap(branch -> {
                            if (branch.getProducts() == null || branch.getProducts().isEmpty()) {
                                return Mono.empty();
                            }

                            return Mono.just(branch.getProducts().stream()
                                            .max(Comparator.comparingInt(Product::getStock))
                                            .orElse(null))
                                    .map(product -> new ProductWithBranch(branch.getName(), product));
                        })
                        .collectList()
                )
                .switchIfEmpty(Mono.error(new Exception("Franchise not found")));
    }

    @Override
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    franchise.setName(newName);
                    return franchiseRepository.save(franchise);
                })
                .switchIfEmpty(Mono.error(new Exception("Franchise not found")));
    }

    @Override
    public Mono<Branch> updateBranchName(String franchiseId, String branchId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Optional<Branch> optionalBranch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst();

                    if (optionalBranch.isPresent()) {
                        Branch branch = optionalBranch.get();
                        branch.setName(newName);
                        return franchiseRepository.save(franchise)
                                .thenReturn(branch);
                    } else {
                        return Mono.error(new Exception("Branch not found in this franchise"));
                    }
                })
                .switchIfEmpty(Mono.error(new Exception("Franchise not found")));
    }

    @Override
    public Mono<Product> updateProductName(String franchiseId, String branchId, String productId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    Optional<Branch> optionalBranch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst();

                    if (optionalBranch.isPresent()) {
                        Branch branch = optionalBranch.get();
                        Optional<Product> optionalProduct = branch.getProducts().stream()
                                .filter(p -> p.getId().equals(productId))
                                .findFirst();

                        if (optionalProduct.isPresent()) {
                            Product product = optionalProduct.get();
                            product.setName(newName);
                            return franchiseRepository.save(franchise)
                                    .thenReturn(product);
                        } else {
                            return Mono.error(new Exception("Product not found in this branch"));
                        }
                    } else {
                        return Mono.error(new Exception("Branch not found in this franchise"));
                    }
                })
                .switchIfEmpty(Mono.error(new Exception("Franchise not found")));
    }

    @Override
    public Mono<Void> deleteProductFromBranch(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {

                    Optional<Branch> optionalBranch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst();

                    if (optionalBranch.isPresent()) {
                        Branch branch = optionalBranch.get();

                        boolean removed = branch.getProducts().removeIf(p -> p.getId().equals(productId));
                        if (!removed) {
                            return Mono.error(new Exception("Product not found in this branch"));
                        }

                        return franchiseRepository.save(franchise)
                                .then();
                    } else {
                        return Mono.error(new Exception("Branch not found in this franchise"));
                    }
                });


    }
    @Override
    public Mono<Product> modifyProductStock(String franchiseId, String branchId, String productId, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .flatMap(franchise -> {
                    // Buscar la rama dentro de la franquicia
                    Optional<Branch> optionalBranch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst();

                    if (optionalBranch.isPresent()) {
                        Branch branch = optionalBranch.get();

                        // Buscar el producto dentro de la rama
                        Optional<Product> optionalProduct = branch.getProducts().stream()
                                .filter(p -> p.getId().equals(productId))
                                .findFirst();

                        if (optionalProduct.isPresent()) {
                            Product product = optionalProduct.get();
                            product.setStock(newStock);

                            return franchiseRepository.save(franchise)
                                    .thenReturn(product);
                        } else {
                            return Mono.error(new Exception("Product not found in this branch"));
                        }
                    } else {
                        return Mono.error(new Exception("Branch not found in this franchise"));
                    }
                })
                .switchIfEmpty(Mono.error(new Exception("Franchise not found")));
    }

}