package co.com.crm.constructora.franchiseapp.infraestructure.adapters.rest;


import co.com.crm.constructora.franchiseapp.domain.model.Branch;
import co.com.crm.constructora.franchiseapp.domain.model.Franchise;
import co.com.crm.constructora.franchiseapp.domain.model.Product;
import co.com.crm.constructora.franchiseapp.domain.model.ProductWithBranch;
import co.com.crm.constructora.franchiseapp.domain.ports.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/franchises")
@AllArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;


    @Operation(summary = "Create a new franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Franchise created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Franchise.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input") })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> createFranchise(@RequestBody Franchise franchise) {
        return franchiseService.createFranchise(franchise);
    }

    @Operation(summary = "Get a franchise by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the franchise",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Franchise.class)) }),
            @ApiResponse(responseCode = "404", description = "Franchise not found") })
    @GetMapping("/{id}")
    public Mono<Franchise> getFranchiseById(@PathVariable String id) {
        return franchiseService.getFranchiseById(id);
    }

    @Operation(summary = "Get all franchises")
    @ApiResponse(responseCode = "200", description = "List of franchises",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Franchise.class)) })
    @GetMapping
    public Flux<Franchise> getAllFranchises() {
        return franchiseService.getAllFranchises();
    }


    @Operation(summary = "Delete a franchise by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Franchise deleted"),
            @ApiResponse(responseCode = "404", description = "Franchise not found") })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteFranchise(@PathVariable String id) {
        return franchiseService.deleteFranchise(id);
    }

    @Operation(summary = "Add a branch to a franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch added to franchise",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Franchise.class)) }),
            @ApiResponse(responseCode = "404", description = "Franchise not found") })
    @PostMapping("/{franchiseId}/branches")
    public Mono<Franchise> addBranch(@PathVariable String franchiseId, @RequestBody Branch branch) {
        return franchiseService.addBranch(franchiseId, branch);
    }

    @Operation(summary = "Get all branches of a franchise")
    @ApiResponse(responseCode = "200", description = "List of branches",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Branch.class)) })
    @GetMapping("/{franchiseId}/branches")
    public Flux<Branch> getBranchesByFranchiseId(@PathVariable String franchiseId) {
        return franchiseService.getBranchesByFranchiseId(franchiseId);
    }
    @GetMapping("/{franchiseId}/products/max-stock")
    @Operation(summary = "Get the product with the maximum stock for each branch of a franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products with maximum stock per branch",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductWithBranch.class))}),
            @ApiResponse(responseCode = "404", description = "Franchise not found")
    })
    public Mono<List<ProductWithBranch>> getMaxStockProductsByFranchise(@PathVariable String franchiseId) {
        return franchiseService.getMaxStockProductsByFranchise(franchiseId);
    }

    @PutMapping("/{franchiseId}/name")
    @Operation(summary = "Update the name of a franchise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Franchise name updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Franchise.class))}),
            @ApiResponse(responseCode = "404", description = "Franchise not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<Franchise> updateFranchiseName(@PathVariable String franchiseId, @RequestParam String newName) {
        return franchiseService.updateFranchiseName(franchiseId, newName);
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/name")
    @Operation(summary = "Update the name of a branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch name updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Branch.class))}),
            @ApiResponse(responseCode = "404", description = "Franchise or branch not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<Branch> updateBranchName(@PathVariable String franchiseId, @PathVariable String branchId, @RequestParam String newName) {
        return franchiseService.updateBranchName(franchiseId, branchId, newName);
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/name")
    @Operation(summary = "Update the name of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product name updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Franchise, branch or product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<Product> updateProductName(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestParam String newName) {
        return franchiseService.updateProductName(franchiseId, branchId, productId, newName);
    }

    @DeleteMapping("/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product from a branch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted from branch"),
            @ApiResponse(responseCode = "404", description = "Branch or product not found")
    })
    public Mono<Void> deleteProductFromBranch(@PathVariable String franchiseId, @PathVariable String branchId, @PathVariable String productId) {
        return franchiseService.deleteProductFromBranch(franchiseId, branchId, productId);
    }

    @PutMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    @Operation(summary = "Modify the stock of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product stock modified",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Franchise, branch or product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<Product> modifyProductStock(
            @PathVariable String franchiseId,
            @PathVariable String branchId,
            @PathVariable String productId,
            @RequestParam int newStock) {
        return franchiseService.modifyProductStock(franchiseId, branchId, productId, newStock);
    }


}