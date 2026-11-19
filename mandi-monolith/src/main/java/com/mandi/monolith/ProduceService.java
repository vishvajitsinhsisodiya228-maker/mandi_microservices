package com.mandi.monolith;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the in-memory produce list for "Hari Bhari Mandi".
 * No database / JPA is used - data lives only while the app runs.
 */
@Service
public class ProduceService {

    private final List<Produce> produceList = new ArrayList<>();

    public ProduceService() {
        produceList.add(new Produce(1, "Tomato", 28.0, 450));
        produceList.add(new Produce(2, "Potato", 22.0, 500));
        produceList.add(new Produce(3, "Onion", 30.0, 380));
        produceList.add(new Produce(4, "Cauliflower", 45.0, 220));
        produceList.add(new Produce(5, "Green Peas", 90.0, 120));
        produceList.add(new Produce(6, "Spinach", 18.0, 160));
    }

    public List<Produce> getAllProduce() {
        return produceList;
    }

    public Produce getById(int id) {
        return produceList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produce with id " + id + " not found"));
    }

    /**
     * Reduces the stock of a vegetable by the given quantity.
     * Throws HTTP 400 if the quantity is more than the available stock.
     */
    public synchronized Produce reduceStock(int produceId, int quantityKg) {
        Produce produce = getById(produceId);
        if (quantityKg > produce.getStockKg()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only " + produce.getStockKg() + " kg of " + produce.getName()
                            + " in stock, but " + quantityKg + " kg requested");
        }
        produce.setStockKg(produce.getStockKg() - quantityKg);
        return produce;
    }
}
