package com.example.jasonexercise.service.impl;

import com.example.jasonexercise.model.dto.ProductNameAndPriceDTO;
import com.example.jasonexercise.model.dto.ProductSeedDto;
import com.example.jasonexercise.model.entity.Product;
import com.example.jasonexercise.repository.ProductRepository;
import com.example.jasonexercise.service.CategoryService;
import com.example.jasonexercise.service.ProductService;
import com.example.jasonexercise.service.UserService;
import com.example.jasonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.jasonexercise.constants.GlobalConstant.RESOURCE_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    public static final String PRODUCT_FILE_NAME = "products.json";

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private Gson gson;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public void seedProduct() throws IOException {

        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCE_FILE_PATH + PRODUCT_FILE_NAME));

        ProductSeedDto[] productSeedDtos = gson
                .fromJson(fileContent,ProductSeedDto[].class);

        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto,Product.class);
                    product.setSeller(userService.findByRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(900L)) > 0){
                        product.setBuyer((userService.findByRandomUser()));
                    }
                    product.setCategories(categoryService.findRandomRandomCategories());

                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDTO> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPriceDesc(lower,upper)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDTO productNameAndPriceDTO = modelMapper
                            .map(product,ProductNameAndPriceDTO.class);

                    productNameAndPriceDTO.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));

                    return productNameAndPriceDTO;
                })
                .collect(Collectors.toList());
    }
}
