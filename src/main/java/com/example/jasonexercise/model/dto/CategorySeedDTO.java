package com.example.jasonexercise.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class CategorySeedDTO {

    @Expose
    private String name;

    public CategorySeedDTO() {

    }

    @Size(min = 3, max = 15)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
