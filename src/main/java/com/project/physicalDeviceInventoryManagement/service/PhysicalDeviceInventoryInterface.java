package com.project.physicalDeviceInventoryManagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.physicalDeviceInventoryManagement.dto.InventoryDTO;
import com.project.physicalDeviceInventoryManagement.dto.ListIntegerDTO;
import com.project.physicalDeviceInventoryManagement.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PhysicalDeviceInventoryInterface {
    ResponseEntity< ResponseDTO> createInventory ( InventoryDTO inventoryDTO ) throws JsonProcessingException;

    ResponseEntity< ResponseDTO> listOfInventory ( );

    ResponseEntity< ResponseDTO> particularInventorById ( Integer id ) throws JsonProcessingException;

    ResponseEntity< ResponseDTO> listOfDeviceType (  );

    ResponseEntity< ResponseDTO> listOfModel ( );

    ResponseEntity< ResponseDTO> listOfSim ( );

    ResponseEntity< ResponseDTO> listOfImei ( );

    ResponseEntity< ResponseDTO> searchByImei ( String imei );

    ResponseEntity< ResponseDTO> deleteInventory ( Integer id );

    ResponseEntity< ResponseDTO> editInventory ( Integer id , InventoryDTO inventoryDTO ) throws JsonProcessingException;

    ResponseEntity< ResponseDTO> getParticularDevice ( String device );

    ResponseEntity< ResponseDTO> getParticularModel ( String model );

    ResponseEntity< ResponseDTO> getParticularSim ( String sim );

    ResponseEntity< ResponseDTO> filterRecords ( String device , String model , String sim );

    ResponseEntity< ResponseDTO> deleteAll ( );

    ResponseEntity< ResponseDTO> dataFromExcel ( MultipartFile file ) throws IOException;

    ResponseEntity< ResponseDTO> deleteListOfInventory ( ListIntegerDTO inventoryDTOList );
}