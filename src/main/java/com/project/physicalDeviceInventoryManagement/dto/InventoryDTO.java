package com.project.physicalDeviceInventoryManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class InventoryDTO {
    @NotBlank(message = "IMEI is required")
    @Size(min = 15, max = 15, message = "IMEI must be 15 characters long")
    private String imei;

    @NotBlank(message = "Device type is required")
    private String deviceType;

    @NotBlank(message = "Model is required")
    private String model;

//    @NotBlank(message = "SIM type is required")
    private List<String> simType;

    @NotBlank(message = "Current holder is required")
    private String currentHolder;

    private String previousHolder;

    @NotNull(message = "Entry created date is required")
    private Timestamp entryCreatedDate;

    private Timestamp lastUpdatedDate;

    private String remarks;

    private String businessNeeded;
}