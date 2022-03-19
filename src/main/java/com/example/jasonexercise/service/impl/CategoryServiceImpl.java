package com.example.jasonexercise.service.impl;

import com.example.jasonexercise.model.dto.CategorySeedDTO;
import com.example.jasonexercise.model.entity.Category;
import com.example.jasonexercise.repository.CategoryRepository;
import com.example.jasonexercise.service.CategoryService;
import com.example.jasonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.jasonexercise.constants.GlobalConstant.RESOURCE_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {

    public static final String CATEGORIES_FILE_NAME = "categories.json";

    private final Gson gson;
    private final CategoryRepository categoryRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(Gson gson, CategoryRepository categoryRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.gson = gson;
        this.categoryRepository = categoryRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME));
        System.out.println();

        CategorySeedDTO[] categorySeedDtos = gson.fromJson(fileContent, CategorySeedDTO[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDTO ->  modelMapper.map(categorySeedDTO, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> findRandomRandomCategories() {
        Set<Category>categorySet = new HashSet<>();
        int categoryCount = ThreadLocalRandom.current().nextInt(1,3);

        long totalCategoriesCount = categoryRepository.count();

        for (int i = 0; i < categoryCount; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1,totalCategoriesCount + 1);

            categorySet.add(categoryRepository.findById(randomId).orElse(null));
        }
        return categorySet;
    }
}
