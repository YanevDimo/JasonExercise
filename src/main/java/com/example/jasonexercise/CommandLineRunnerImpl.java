package com.example.jasonexercise;

import com.example.jasonexercise.model.dto.ProductNameAndPriceDTO;
import com.example.jasonexercise.model.dto.UserSoldDTO;
import com.example.jasonexercise.service.CategoryService;
import com.example.jasonexercise.service.ProductService;
import com.example.jasonexercise.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    public static final String OUTPUT_PATH = "src/main/resources/files/out/";
    public static final String PRODUCT_IN_RANGE_FILE_NAME = "products-in-range.json";
    public static final String USERS_AND_SOLD_PRODUCTS = "users_and_sold_products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));


    }

    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Enter exercise");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
        }
    }

    private void soldProducts() throws IOException {
     List<UserSoldDTO>userSoldDtos = userService.findAllUsersWithMoreThanOneSoldProducts();

     String content = gson.toJson(userSoldDtos);

     writeToFile(OUTPUT_PATH+USERS_AND_SOLD_PRODUCTS,content);
    }

    private void productsInRange() throws IOException {

        List<ProductNameAndPriceDTO> producDTO = productService
                .findAllProductsInRangeOrderByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(producDTO);

        writeToFile(OUTPUT_PATH + PRODUCT_IN_RANGE_FILE_NAME, content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {

        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProduct();
    }
}
