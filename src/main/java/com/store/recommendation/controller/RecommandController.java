package com.store.recommendation.controller;

import com.store.recommendation.model.ProductDTO;
import com.store.recommendation.service.RecommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommand")
public class RecommandController {

    @Autowired
    RecommandService recommandService;

    @GetMapping("/accessories/{accessoryType}")
    public ResponseEntity<List<ProductDTO>> getProductAccessories(@PathVariable("accessoryType") String accessoryType) {
        List<ProductDTO> accessoryList = recommandService.getProductAccessories(accessoryType);
        return new ResponseEntity<List<ProductDTO>>(accessoryList, HttpStatus.OK);
    }


}
