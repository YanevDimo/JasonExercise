package com.example.jasonexercise.model.dto;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Set;

public class UserSoldDTO {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Set<ProductWithBuyerDTO> soldProducts;

    public UserSoldDTO(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ProductWithBuyerDTO> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<ProductWithBuyerDTO> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
