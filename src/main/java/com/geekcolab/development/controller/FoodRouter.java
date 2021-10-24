package com.geekcolab.development.controller;

import com.geekcolab.development.handler.FoodHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * FoodRouter is routing class to configure all your food routes and simple pass
 * handler to handle incoming request for corresponding end points.
 *
 */

@Configuration
public class FoodRouter {

    @Bean
    public RouterFunction<ServerResponse> route(FoodHandler foodHandler) {
        return RouterFunctions.route(
                GET("/food").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), foodHandler::getAllFoods)
                .andRoute(GET("/food/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        foodHandler::getFood)
                .andRoute(GET("/food/title/{title}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        foodHandler::getFoodsByTitle)
                .andRoute(GET("/food/restaurant/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        foodHandler::getFoodsByRestaurantId)
                .andRoute(GET("/food/restaurant/name/{name}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        foodHandler::getFoodByRestaurantName)
                .andRoute(POST("/food").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        foodHandler::addNewFood)
                .andRoute(PUT("/food/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), foodHandler::updateFood)
                .andRoute(DELETE("/food/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), foodHandler::deleteAnFood);
    }
}
