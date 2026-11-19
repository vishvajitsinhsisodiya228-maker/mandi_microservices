package com.mandi.produce;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory produce store for "Hari Bhari Mandi". No database or JPA.
 */
@Service
public class ProduceService {

    private final List<Produce> produceList = new ArrayList<>();
    private int nextId = 7;

    public ProduceService() {
        produceList.add(new Produce(1, "Tomato", 28.0, 450));
        produceList.add(new Produce(2, "Potato", 22.0, 500));
        produceList.add(new Produce(3, "Onion", 30.0, 380));
        produceList.add(new Produce(4, "Cauliflower", 45.0, 220));
        produceList.add(new Produce(5, "Green Peas", 90.0, 120));
        produceList.add(new Produce(6, "Spinach", 18.0, 160));
    }

    public synchronized List<Produce> getAll() {
        return new ArrayList<>(produceList);
    }

    public synchronized Produce getById(int id) {
        return produceList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produce with id " + id + " not found"));
    }

    public synchronized Produce create(Produce produce) {
        produce.setId(nextId++);
        produceList.add(produce);
        return produce;
    }

    public synchronized Produce update(int id, Produce updated) {
        Produce existing = getById(id);
        existing.setName(updated.getName());
        existing.setPricePerKg(updated.getPricePerKg());
        existing.setStockKg(updated.getStockKg());
        return existing;
    }

    public synchronized void delete(int id) {
        Produce existing = getById(id);
        produceList.remove(existing);
    }

    /** Sum of stockKg over all vegetables (used by the health check). */
    public synchronized int getTotalStockKg() {
        return produceList.stream().mapToInt(Produce::getStockKg).sum();
    }
}
