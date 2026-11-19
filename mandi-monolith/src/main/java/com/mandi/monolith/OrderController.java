package com.mandi.monolith;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ProduceService produceService;
    private final List<Order> orders = new ArrayList<>();   // in-memory order store
    private int nextOrderId = 1;

    public OrderController(ProduceService produceService) {
        this.produceService = produceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Order placeOrder(@RequestBody OrderRequest request) {
        if (request.getQuantityKg() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "quantityKg must be greater than 0");
        }

        // reduces stock; throws HTTP 400 if quantityKg is more than the stock
        Produce produce = produceService.reduceStock(request.getProduceId(), request.getQuantityKg());

        double total = produce.getPricePerKg() * request.getQuantityKg();
        Order order = new Order(nextOrderId++, produce.getId(), produce.getName(),
                request.getQuantityKg(), produce.getPricePerKg(), total);
        orders.add(order);
        return order;
    }

    @GetMapping
    public List<Order> getOrders() {
        return orders;
    }
}
