package com.mandi.order;

import com.mandi.order.client.ProduceClient;
import com.mandi.order.client.RecommendationClient;
import feign.FeignException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ProduceClient produceClient;
    private final RecommendationClient recommendationClient;
    private final DiscoveryClient discoveryClient;
    private final List<Order> orders = new ArrayList<>();   // in-memory order store
    private int nextOrderId = 1;

    public OrderController(ProduceClient produceClient,
                           RecommendationClient recommendationClient,
                           DiscoveryClient discoveryClient) {
        this.produceClient = produceClient;
        this.recommendationClient = recommendationClient;
        this.discoveryClient = discoveryClient;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Order placeOrder(@RequestBody OrderRequest request) {
        if (request.getQuantityKg() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "quantityKg must be greater than 0");
        }

        ProduceDto produce = fetchProduce(request.getProduceId());

        double total = produce.getPricePerKg() * request.getQuantityKg();
        Order order = new Order(nextOrderId++, produce.getId(), produce.getName(),
                request.getQuantityKg(), total);
        orders.add(order);
        return order;
    }

    @GetMapping
    public synchronized List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    /** Suggestions from the FastAPI service for the vegetable of a stored order. */
    @GetMapping("/{id}/suggestions")
    public RecommendationResponse getSuggestions(@PathVariable int id) {
        Order order = findOrder(id);
        return recommendationClient.getRecommendations(order.getProduceName());
    }

    /** Every running produce-service instance known to Eureka (own small maps). */
    @GetMapping("/instances/produce")
    public List<Map<String, Object>> getProduceInstances() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (ServiceInstance instance : discoveryClient.getInstances("produce-service")) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("host", instance.getHost());
            info.put("port", instance.getPort());
            info.put("uri", instance.getUri().toString());
            result.add(info);
        }
        return result;
    }

    private synchronized Order findOrder(int id) {
        return orders.stream()
                .filter(o -> o.getOrderId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order with id " + id + " not found"));
    }

    private ProduceDto fetchProduce(int produceId) {
        try {
            return produceClient.getProduce(produceId);
        } catch (FeignException.NotFound e) {
            // produce-service replied 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Produce with id " + produceId + " not found in produce-service");
        } catch (FeignException e) {
            // no instance registered in Eureka, or produce-service cannot be reached
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "produce-service is not running or has no instance registered in Eureka. Please try again later.");
        }
    }
}
