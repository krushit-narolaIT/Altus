package com.krushit.dto;

import java.util.List;

public class BrandModelResponseDTO {
    private String brandName;
    private List<String> models;

    public BrandModelResponseDTO(String brandName, List<String> models) {
        this.brandName = brandName;
        this.models = models;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }
}
