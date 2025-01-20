package co.com.crm.constructora.franchiseapp.infraestructure.adapters.persistence;

import co.com.crm.constructora.franchiseapp.domain.model.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveCrudRepository<Franchise, String> {
    Mono<Franchise> findByName(String name);
}
