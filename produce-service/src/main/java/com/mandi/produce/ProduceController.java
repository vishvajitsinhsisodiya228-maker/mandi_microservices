package com.mandi.produce;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/produce")
public class ProduceController {

    private final ProduceService produceService;

    public ProduceController(ProduceService produceService) {
        this.produceService = produceService;
    }

    @GetMapping
    public List<Produce> getAll() {
        return produceService.getAll();
    }

    @GetMapping("/{id}")
    public Produce getById(@PathVariable int id) {
        return produceService.getById(id);   // 404 if unknown
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produce create(@Valid @RequestBody Produce produce) {
        return produceService.create(produce);   // 400 if validation fails
    }

    @PutMapping("/{id}")
    public Produce update(@PathVariable int id, @Valid @RequestBody Produce produce) {
        return produceService.update(id, produce);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        produceService.delete(id);
    }
}
