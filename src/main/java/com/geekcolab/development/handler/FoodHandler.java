package com.geekcolab.development.handler;

import com.geekcolab.development.model.Food;
import com.geekcolab.development.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * FoodHandler is class to handle all request/response for Food CRUD operations
 * Router will call corresponding handler to handle incoming requests.
 * for example /v1/food POST will call createFood handler.
 *
 */

@Component
public class FoodHandler {

    @Autowired
    private FoodRepository foodRepository;

    public static Mono<String> currentUserId() {
        return jwt().map(jwt -> jwt.getClaimAsString("sub"));
    }

    public static Mono<String> currentUserName() {
        return jwt().map(jwt -> jwt.getClaimAsString("name"));
    }

    public static Mono<Jwt> jwt() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .cast(Jwt.class);
    }

    /**
     * Get All food method will be called by router to route incoming request at /v1/food GET
     * and will return all foods available in DB.
     * @param request  //incoming server request
     * @return response  //outgoing server response.
     */

    public Mono<ServerResponse> getAllFoods(ServerRequest request) {
        Flux<Food> foods = foodRepository.findAll();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(foods, Food.class);
    }

    public Mono<ServerResponse> getFoodsByTitle(ServerRequest request) {
        String title = request.pathVariable("title");
        Flux<Food> foods = foodRepository.findAllByTitleContaining(title);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(foods, Food.class);
    }

    public Mono<ServerResponse> getFoodsByRestaurantId(ServerRequest request) {
        String id = request.pathVariable("id");
        Flux<Food> foods = foodRepository.findByRestaurantID(id);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(foods, Food.class);
    }

    public Mono<ServerResponse> getFoodByRestaurantName(ServerRequest request) {
        Mono<? extends Principal> jwt = request.principal();
        String name = request.pathVariable("name");
        Flux<Food> foods = foodRepository.findByRestaurantName(name);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(foods, Food.class);
    }

    /**
     * Get an food method will be called by router to route incoming request at /v1/food/{id} GET
     * and will return an food based on food ID
     * @param request  //incoming server request
     * @return response  //outgoing server response.
     */

    public Mono<ServerResponse> getFood(ServerRequest request) {
        String foodId = request.pathVariable("id");
        return foodRepository.findById(foodId)
                .flatMap(food -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(food), Food.class)).switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Add a new food method will be called by router to route incoming request at /v1/food POST
     * and will return created foods.
     * @param request  //incoming server request
     * @return response  //outgoing server response.
     */

    public Mono<ServerResponse> addNewFood(ServerRequest request) {
        Mono<Food> foodMono = request.bodyToMono(Food.class);
        Mono<Food> newFood = foodMono.flatMap(foodRepository::save);
        return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(newFood, Food.class);
    }

    /**
     * Update an food by food ID and updated body coming in body.
     * will be called by router to route incoming request at /v1/food/{id} PUT
     * and will return updated food record.
     * @param request  //incoming server request
     * @return response  //outgoing server response.
     */

    public Mono<ServerResponse> updateFood(ServerRequest request) {
        String foodId = request.pathVariable("id");
        Mono<Food> foodMono = request.bodyToMono(Food.class);
        return foodRepository.findById(foodId)
                .flatMap(food -> foodMono.flatMap(food1 -> {
                    food.setTitle(food1.getTitle());
                    food.setImage(food1.getImage());
                    food.setDescription(food1.getDescription());
                    Mono<Food> updatedFood = foodRepository.save(food);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(updatedFood, Food.class);
                }));
    }

    /**
     * delete a food based on food ID.
     * will be called by router to route incoming request at /v1/food/{id} DELETE
     * and will return all foods available in DB.
     * @param request  //incoming server request
     * @return response  //outgoing server response.
     */

    public Mono<ServerResponse> deleteAnFood(ServerRequest request) {
        String foodId = request.pathVariable("id");
        return foodRepository.findById(foodId)
                .flatMap(food -> foodRepository
                        .delete(food)
                        .then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
