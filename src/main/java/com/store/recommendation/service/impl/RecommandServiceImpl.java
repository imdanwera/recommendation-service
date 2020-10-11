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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommandServiceImpl implements RecommandService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ProductDTO> getProductAccessories(String type) {

        // Get Instance through load balancer
        ServiceInstance serviceInstance = loadBalancerClient.choose("INVENTORY-SERVICE");

        // Get Instance through discovery client
        /*List<ServiceInstance> instances = discoveryClient.getInstances("INVENTORY-SERVICE");
        ServiceInstance serviceInstance = instances.get(0);*/

        String baseURL = serviceInstance.getUri().toString();
        String contextPath = String.format("/api/inventories/{type}/accessories", type);

        Map<String,String> urlParam = new HashMap<>();
        urlParam.put("type", type);

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseURL+contextPath).buildAndExpand(urlParam);

        ResponseEntity<List<ProductDTO>> productList =
                restTemplate.exchange(builder.toUriString(), HttpMethod.GET, getHeader(), new ParameterizedTypeReference<List<ProductDTO>>() {});

        productList.getBody().stream().forEach(productDTO -> System.out.println(productDTO.getProductName()));
        /*try {
            Thread.sleep(6500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //throw new RuntimeException();
        return productList.getBody();
    }

    private HttpEntity<?> getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }
}
