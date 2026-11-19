package com.mandi.order.client;

import com.mandi.order.RecommendationResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Used when recommendation-service is down: returns the vegetable name
 * with an empty suggestions list.
 */
@Component
public class RecommendationClientFallback implements RecommendationClient {

    @Override
    public RecommendationResponse getRecommendations(String produceName) {
        return new RecommendationResponse(produceName, Collections.emptyList());
    }
}
