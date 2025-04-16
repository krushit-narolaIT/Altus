package com.krushit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brand_models")
public class BrandModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_model_id")
    private int brandModelId;

    @Column(name = "brand_name", nullable = false, length = 20)
    private String brandName;

    @Column(name = "model", nullable = false, length = 20)
    private String model;

    @Column(name = "min_year")
    private int minYear;

    /*    @Column(name = "service_id")
    private int serviceId;*/

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private VehicleService vehicleService;

    public BrandModel() {}

    public BrandModel(int brandModelId, VehicleService vehicleService, String brandName, String model, int minYear) {
        this.brandModelId = brandModelId;
        this.vehicleService = vehicleService;
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

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
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
        return "BrandModel [brandModelId=" + brandModelId + ", vehicleService=" + vehicleService +
                ", brandName=" + brandName + ", model=" + model + ", minYear=" + minYear + "]";
    }
}
