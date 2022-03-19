package com.example.jasonexercise.service;

import com.example.jasonexercise.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomRandomCategories();
}
