package com.store.recommendation.service.impl;


import com.store.recommendation.model.ProductDTO;
import com.store.recommendation.service.RecommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class RecommandServiceImpl implements RecommandService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<ProductDTO> getProductAccessories(String accessoryType) {

        // Get Instance through load balancer
        //ServiceInstance serviceInstance = loadBalancerClient.choose("INVENTORY-SERVICE");

        List<ServiceInstance> instances = discoveryClient.getInstances("INVENTORY-SERVICE");
        ServiceInstance serviceInstance = instances.get(0);

        String baseURL = serviceInstance.getUri().toString();
        String contextPath = "/api/inventory/accessory";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseURL+contextPath).queryParam("accstype", accessoryType);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<ProductDTO>> productList =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, getHeader(), new ParameterizedTypeReference<List<ProductDTO>>() {});

        productList.getBody().stream().forEach(productDTO -> System.out.println(productDTO.getProductName()));

        return productList.getBody();
    }

    private HttpEntity<?> getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
