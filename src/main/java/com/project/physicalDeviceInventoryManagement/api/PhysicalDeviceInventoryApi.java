package com.project.physicalDeviceInventoryManagement.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.physicalDeviceInventoryManagement.dto.InventoryDTO;
import com.project.physicalDeviceInventoryManagement.dto.ListIntegerDTO;
import com.project.physicalDeviceInventoryManagement.dto.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping("/inventoryManagement")
@CrossOrigin
public interface PhysicalDeviceInventoryApi {
    @PostMapping("/createInventory")
    ResponseEntity < ResponseDTO > createInventory ( @Valid @RequestBody InventoryDTO inventoryDTO ) throws JsonProcessingException;

    @GetMapping("/listOfInventory")
    ResponseEntity<ResponseDTO> listOfInventory();

    @GetMapping("/particularInventory/{id}")
    ResponseEntity<ResponseDTO> particularInventory ( @PathVariable Integer id ) throws JsonProcessingException;

    @GetMapping("/listOfDeviceType")
    ResponseEntity<ResponseDTO> listOfDeviceType ();

    @GetMapping("/listOfModel")
    ResponseEntity<ResponseDTO> listOfModel ();

    @GetMapping("/listOfSim")
    ResponseEntity<ResponseDTO> listOfSim();

    @GetMapping("/listOfImei")
    ResponseEntity<ResponseDTO> listOfImei ();

    @GetMapping("/searchByImei/{imei}")
    ResponseEntity<ResponseDTO> searchByImei (  @PathVariable String imei );

    @DeleteMapping("/deleteInventory/{id}")
    ResponseEntity<ResponseDTO> deleteInventory ( @PathVariable Integer id );


    @DeleteMapping("/deleteListOfInventories")
    ResponseEntity<ResponseDTO> deleteListOfInventories (@RequestBody  ListIntegerDTO  inventoryDTOList );

    @PutMapping("/editInventory/{id}")
    ResponseEntity<ResponseDTO> editInventory ( @Valid @PathVariable Integer id, @Valid @RequestBody InventoryDTO inventoryDTO ) throws JsonProcessingException;

    @GetMapping("/getByDevice/{device}")
    ResponseEntity<ResponseDTO> getByDevice ( @PathVariable String device );

    @GetMapping("/getByModel/{model}")
    ResponseEntity<ResponseDTO> getByModel( @PathVariable String model );

    @GetMapping("/getBySim/{sim}")
    ResponseEntity<ResponseDTO> getBySim ( @PathVariable String sim );

    @GetMapping("/filterByInventories")
    ResponseEntity<ResponseDTO> filterByInventories(
            @RequestParam(required = false) String device,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String sim) ;

    @DeleteMapping("/deleteAll")
    ResponseEntity<ResponseDTO> deleteAll();


    @PostMapping("/dataFromExcel")
    ResponseEntity<ResponseDTO> saveCode(@RequestParam(value="file", required = false) MultipartFile file) throws IOException;
}