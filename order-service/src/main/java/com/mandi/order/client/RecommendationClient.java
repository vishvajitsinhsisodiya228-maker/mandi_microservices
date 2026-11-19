package com.mandi.order.client;

import com.mandi.order.RecommendationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for the Python FastAPI recommendation-service.
 * Eureka does not know this service, so the url is given directly.
 */
@FeignClient(name = "recommendation-service",
        url = "http://localhost:8000",
        fallback = RecommendationClientFallback.class)
public interface RecommendationClient {

    @GetMapping("/recommendations/{produceName}")
    RecommendationResponse getRecommendations(@PathVariable("produceName") String produceName);
}
