package com.krushit.model;

public class BrandModel {
    private int brandModelId;
    private int serviceId;
    private String brandName;
    private String model;
    private int minYear;

    public BrandModel() {}

    public BrandModel(int brandModelId, int serviceId, String brandName, String model, int minYear) {
        this.brandModelId = brandModelId;
        this.serviceId = serviceId;
        this.brandName = brandName;
        this.model = model;
        this.minYear = minYear;
    }

    public int getBrandModelId() {
        return brandModelId;
    }

    public void setBrandModelId(int brandModelId) {
        this.brandModelId = brandModelId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    @Override
    public String toString() {
        return "BrandModel [brandModelId=" + brandModelId + ", serviceId=" + serviceId + 
               ", brandName=" + brandName + ", model=" + model + ", minYear=" + minYear + "]";
    }
}
