package com.geekcolab.development.repository;

import com.geekcolab.development.model.Food;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The FoodRepository is a simple interface which extend JpaRepository to provide all default methods to your
 * entity/document repository.

 */

@Repository
public interface FoodRepository extends ReactiveMongoRepository<Food, String> {

    Mono<Food> findById(String foodId);
    Flux<Food> findByRestaurantID(String s);
    Flux<Food> findByRestaurantName(String s);
    Flux<Food> findAllByTitleContaining(String s);
}
