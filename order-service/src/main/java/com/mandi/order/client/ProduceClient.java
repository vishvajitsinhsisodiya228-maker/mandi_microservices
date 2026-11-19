package com.mandi.order.client;

import com.mandi.order.ProduceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for produce-service. The name is looked up in Eureka,
 * so no host or port is written here.
 */
@FeignClient(name = "produce-service")
public interface ProduceClient {

    @GetMapping("/api/produce/{id}")
    ProduceDto getProduce(@PathVariable("id") int id);
}
