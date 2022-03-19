package com.example.jasonexercise.service;

import com.example.jasonexercise.model.dto.ProductNameAndPriceDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProduct() throws IOException;

    List<ProductNameAndPriceDTO> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper);
}
