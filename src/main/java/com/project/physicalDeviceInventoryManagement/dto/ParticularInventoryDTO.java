package com.project.physicalDeviceInventoryManagement.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ParticularInventoryDTO {
    private String imei;

    private String deviceType;

    private String model;

    private List <String> simType;

    private String currentHolder;

    private String previousHolder;

    private Timestamp entryCreatedDate;

    private Timestamp lastUpdatedDate;

    private String remarks;

    private String businessNeeded;

}