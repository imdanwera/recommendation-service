package com.store.recommendation.service;

import com.store.recommendation.model.ProductDTO;

import java.util.List;

public interface RecommandService {

    List<ProductDTO> getProductAccessories(String accessoryType);
}
