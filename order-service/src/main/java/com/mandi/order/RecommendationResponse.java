package com.mandi.order;

import java.util.List;

/**
 * Reply of the FastAPI service. Field names must match the JSON exactly:
 * {"produce": "tomato", "suggestions": ["onion", "green chilli", "coriander"]}
 */
public class RecommendationResponse {

    private String produce;
    private List<String> suggestions;

    public RecommendationResponse() {
    }

    public RecommendationResponse(String produce, List<String> suggestions) {
        this.produce = produce;
        this.suggestions = suggestions;
    }

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
