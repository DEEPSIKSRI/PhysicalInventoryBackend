package com.project.physicalDeviceInventoryManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.physicalDeviceInventoryManagement.api.PhysicalDeviceInventoryApi;
import com.project.physicalDeviceInventoryManagement.dto.InventoryDTO;
import com.project.physicalDeviceInventoryManagement.dto.ListIntegerDTO;
import com.project.physicalDeviceInventoryManagement.dto.ResponseDTO;
import com.project.physicalDeviceInventoryManagement.service.PhysicalDeviceInventoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhysicalDeviceInventoryController implements PhysicalDeviceInventoryApi {

    private final PhysicalDeviceInventoryInterface physicalDeviceInventoryInterface;


    @Override
    public ResponseEntity < ResponseDTO > createInventory ( InventoryDTO inventoryDTO ) throws JsonProcessingException {
        return physicalDeviceInventoryInterface.createInventory ( inventoryDTO );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfInventory ( ) {
        return physicalDeviceInventoryInterface.listOfInventory ( );
    }

    @Override
    public ResponseEntity < ResponseDTO > particularInventory ( Integer id ) throws JsonProcessingException {
        return physicalDeviceInventoryInterface.particularInventorById ( id );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfDeviceType ( ) {
        return physicalDeviceInventoryInterface.listOfDeviceType ( );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfModel ( ) {
        return physicalDeviceInventoryInterface.listOfModel ( );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfSim ( ) {
        return physicalDeviceInventoryInterface.listOfSim ( );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfImei ( ) {
        return physicalDeviceInventoryInterface.listOfImei ( );
    }

    @Override
    public ResponseEntity < ResponseDTO > searchByImei ( String imei ) {
        return physicalDeviceInventoryInterface.searchByImei ( imei );
    }

    @Override
    public ResponseEntity < ResponseDTO > deleteInventory ( Integer id ) {
        return physicalDeviceInventoryInterface.deleteInventory ( id );
    }

    @Override
    public ResponseEntity < ResponseDTO > deleteListOfInventories ( ListIntegerDTO inventoryDTOList ) {
        return physicalDeviceInventoryInterface.deleteListOfInventory(inventoryDTOList);
    }

    @Override
    public ResponseEntity < ResponseDTO > editInventory ( Integer id , InventoryDTO inventoryDTO ) throws JsonProcessingException {
        return physicalDeviceInventoryInterface.editInventory ( id , inventoryDTO );
    }

    @Override
    public ResponseEntity < ResponseDTO > getByDevice ( String device ) {
        return physicalDeviceInventoryInterface.getParticularDevice ( device );
    }

    @Override
    public ResponseEntity < ResponseDTO > getByModel ( String model ) {
        return physicalDeviceInventoryInterface.getParticularModel ( model );
    }

    @Override
    public ResponseEntity < ResponseDTO > getBySim ( String sim ) {
        return physicalDeviceInventoryInterface.getParticularSim ( sim );
    }

    @Override
    public ResponseEntity < ResponseDTO > filterByInventories ( String device , String model , String sim ) {
        return physicalDeviceInventoryInterface.filterRecords ( device , model , sim );
    }

    @Override
    public ResponseEntity < ResponseDTO > deleteAll ( ) {
        return physicalDeviceInventoryInterface.deleteAll ( );
    }

    @Override
    public ResponseEntity < ResponseDTO > saveCode ( MultipartFile file ) throws IOException {
        return physicalDeviceInventoryInterface.dataFromExcel ( file );
    }


}