package com.project.physicalDeviceInventoryManagement.repository;

import com.project.physicalDeviceInventoryManagement.entity.PhysicalDeviceInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalDeviceInventoryRepository extends JpaRepository< PhysicalDeviceInventory,Integer> {

    @Query("SELECT DISTINCT P .deviceType FROM PhysicalDeviceInventory AS P")
    List<String> findPhysicalDeviceInventoryByDeviceType();

    @Query("SELECT DISTINCT P .model FROM PhysicalDeviceInventory AS P")
    List<String> findPhysicalDeviceInventoryByModel();

    @Query("SELECT DISTINCT simType FROM PhysicalDeviceInventory")
    List<String> findPhysicalDeviceInventoryBySimType();

    @Query("SELECT P .imei FROM PhysicalDeviceInventory AS P")
    List<String> findPhysicalDeviceInventoryByImei();

    List<PhysicalDeviceInventory> findPhysicalDeviceInventoriesByImei(String imei);

    PhysicalDeviceInventory findPhysicalDeviceInventoryByImei( String imei );

    List<PhysicalDeviceInventory> findPhysicalDeviceInventoryByDeviceType(String deviceType );

    List<PhysicalDeviceInventory> findPhysicalDeviceInventoryByModel(String model );

    List<PhysicalDeviceInventory> findPhysicalDeviceInventoryBySimType(String simType );

    @Query("SELECT p FROM PhysicalDeviceInventory p " +
            "WHERE (:deviceType IS NULL OR p.deviceType = :deviceType) " +
            "AND (:model IS NULL OR p.model = :model) " +
            "AND (:simType IS NULL OR p.simType = :simType)")
    List<PhysicalDeviceInventory> findPhysicalDeviceInventoriesByDeviceTypeAndModelAndSimType(
            @Param("deviceType") String deviceType,
            @Param("model") String model,
            @Param("simType") String simType);

    boolean existsByImei ( String imei );

    void deleteAllById(Iterable<? extends Integer> ids);





}