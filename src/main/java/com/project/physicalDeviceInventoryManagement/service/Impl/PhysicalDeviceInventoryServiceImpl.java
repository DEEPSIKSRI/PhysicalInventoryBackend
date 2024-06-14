package com.project.physicalDeviceInventoryManagement.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.physicalDeviceInventoryManagement.dto.InventoryDTO;
import com.project.physicalDeviceInventoryManagement.dto.ListIntegerDTO;
import com.project.physicalDeviceInventoryManagement.dto.ParticularInventoryDTO;
import com.project.physicalDeviceInventoryManagement.dto.ResponseDTO;
import com.project.physicalDeviceInventoryManagement.entity.PhysicalDeviceInventory;
import com.project.physicalDeviceInventoryManagement.excel.ExcelData;
import com.project.physicalDeviceInventoryManagement.repository.PhysicalDeviceInventoryRepository;
import com.project.physicalDeviceInventoryManagement.service.PhysicalDeviceInventoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PhysicalDeviceInventoryServiceImpl implements PhysicalDeviceInventoryInterface {

    private final PhysicalDeviceInventoryRepository physicalDeviceInventoryRepository;
    private final ExcelData excelData;
    private final ObjectMapper objectMapper = new ObjectMapper ();

    @Autowired
    public PhysicalDeviceInventoryServiceImpl ( PhysicalDeviceInventoryRepository physicalDeviceInventoryRepository , ExcelData excelData ) {
        this.physicalDeviceInventoryRepository = physicalDeviceInventoryRepository;
        this.excelData = excelData;
    }

    @Override
    public ResponseEntity < ResponseDTO > createInventory ( InventoryDTO inventoryDTO ) throws JsonProcessingException {
//        PhysicalDeviceInventory imeiExist = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryByImei ( inventoryDTO.getImei ( ) );
//        if ( imeiExist != null ) {
//            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "IMEI is Already Exist!!" , "" ) );
//        }
        PhysicalDeviceInventory physicalDeviceInventory = new PhysicalDeviceInventory ( );
        physicalDeviceInventory.setImei ( inventoryDTO.getImei ( ) );
        physicalDeviceInventory.setDeviceType ( inventoryDTO.getDeviceType ( ).toLowerCase ( ) );
        physicalDeviceInventory.setModel ( inventoryDTO.getModel ( ).toLowerCase ( ) );
        physicalDeviceInventory.setPreviousHolder ( inventoryDTO.getPreviousHolder ( ) );
        physicalDeviceInventory.setCurrentHolder ( inventoryDTO.getCurrentHolder ( ) );
        physicalDeviceInventory.setEntryCreatedDate ( inventoryDTO.getEntryCreatedDate ( ) );
        String simTypeJson = objectMapper.writeValueAsString(inventoryDTO.getSimType());
        physicalDeviceInventory.setSimType(simTypeJson);
//        physicalDeviceInventory.setSimType ( inventoryDTO.getSimType ( ) );
        physicalDeviceInventory.setRemarks ( inventoryDTO.getRemarks ( ) );
        physicalDeviceInventory.setBusinessNeeded ( inventoryDTO.getBusinessNeeded ( ) );
        physicalDeviceInventoryRepository.save ( physicalDeviceInventory );
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Inventory Created Successfully!!" , "" ) );
    }

@Override
public ResponseEntity<ResponseDTO> listOfInventory() {
    List<PhysicalDeviceInventory> physicalDeviceInventoryList = physicalDeviceInventoryRepository.findAll();
//    List<PhysicalDeviceInventory> physicalDeviceInventories = new ArrayList<>();

    if (physicalDeviceInventoryList.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(false, HttpStatus.BAD_REQUEST, "No Inventory Found", ""));
    }

//    for (PhysicalDeviceInventory physicalDeviceInventory : physicalDeviceInventoryList) {
//        String simType = physicalDeviceInventory.getSimType().toString ( );
//        if (simType.startsWith("[") && simType.endsWith("]")) {
//            try {
//                List<String> simTypeList = objectMapper.readValue(simType, new TypeReference<> () {});
//                System.out.println (simTypeList +"-------------------" );
//                String cleanedString = String.join(",", simTypeList);
//                physicalDeviceInventory.setSimType(cleanedString);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        physicalDeviceInventories.add(physicalDeviceInventory);
//    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, HttpStatus.OK, "List of Inventory Retrieved Successfully!!", physicalDeviceInventoryList));
}



    @Override
    public ResponseEntity < ResponseDTO > particularInventorById ( Integer id ) throws JsonProcessingException {
        PhysicalDeviceInventory physicalDeviceInventory = getParticularInventory ( id );
        if ( physicalDeviceInventory == null ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "Inventory Not Found" , "" ) );
        }
        ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO ( );
        particularInventoryDTO.setImei ( physicalDeviceInventory.getImei ( ) );
        particularInventoryDTO.setDeviceType ( physicalDeviceInventory.getDeviceType ( ) );
        particularInventoryDTO.setModel ( physicalDeviceInventory.getModel ( ) );
//        String simTypeJson = objectMapper.writeValueAsString(physicalDeviceInventory.getSimType());
//        particularInventoryDTO.setSimType(physicalDeviceInventory.getSimType ());

//        List<String> stringList = new ArrayList<> ();
//        stringList.add ( physicalDeviceInventory.getSimType ( ) );
//        particularInventoryDTO.setSimType( stringList );
        List<String> simTypeList = objectMapper.readValue(physicalDeviceInventory.getSimType(), new TypeReference<List<String>>(){});
        particularInventoryDTO.setSimType ( simTypeList );
        System.out.println (simTypeList );
        particularInventoryDTO.setPreviousHolder ( physicalDeviceInventory.getPreviousHolder ( ) );
        particularInventoryDTO.setCurrentHolder ( physicalDeviceInventory.getCurrentHolder ( ) );
        particularInventoryDTO.setEntryCreatedDate ( physicalDeviceInventory.getEntryCreatedDate ( ) );
        particularInventoryDTO.setLastUpdatedDate ( physicalDeviceInventory.getLastUpdatedDate ( ) );
        particularInventoryDTO.setRemarks ( physicalDeviceInventory.getRemarks ( ) );
        particularInventoryDTO.setBusinessNeeded ( physicalDeviceInventory.getBusinessNeeded ( ) );
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Retrieved Particular Inventory!!" , particularInventoryDTO ) );

    }

    private PhysicalDeviceInventory getParticularInventory ( Integer id ) {
        Optional < PhysicalDeviceInventory > physicalDeviceInventory = physicalDeviceInventoryRepository.findById ( id );
        return physicalDeviceInventory.orElse ( null );

    }


    @Override
    public ResponseEntity < ResponseDTO > listOfDeviceType ( ) {
        List < String > stringList = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryByDeviceType ( );
        if ( stringList.isEmpty ( ) ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Device" , "" ) );
        }
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "List of Device Type Retrieved Successfully!!" , stringList ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfModel ( ) {
        List < String > stringList = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryByModel ( );
        if ( stringList.isEmpty ( ) ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Model" , "" ) );
        }
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "List of Models Type Retrieved Successfully!!" , stringList ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > listOfSim ( ) {
        List < String > rawStrings = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryBySimType ( );
        Set < String > formattedStrings = new HashSet <> ( );
        for(String rawString : rawStrings ) {
            formattedStrings.add ( rawString);
        }

//        for ( String rawString : rawStrings ) {
//            if ( rawString.startsWith ( "[" ) && rawString.endsWith ( "]" )) {
//                String cleanedString = rawString.substring ( 0, rawString.length ( )  );
////                System.out.println (cleanedString +"c" );
//                String[] splitCommas = cleanedString.split ( ",\\s*" );
////                System.out.println ( Arrays.toString ( splitCommas ) +"s" );
//                formattedStrings.addAll ( Arrays.asList ( splitCommas ) );
//            } else {
//                formattedStrings.add ( rawString );
//            }
//        }
//        System.out.println (formattedStrings +"fs" );
        return ResponseEntity.status ( HttpStatus.OK )
                .body ( new ResponseDTO ( true , HttpStatus.OK , "Sim types retrieved successfully" , formattedStrings ) );
    }


    @Override
    public ResponseEntity < ResponseDTO > listOfImei ( ) {
        List < String > stringList = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryByImei ( );
        if ( stringList.isEmpty ( ) ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Imei" , "" ) );
        }
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "List of Imei Type Retrieved Successfully!!" , stringList ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > searchByImei ( String imei ) {
       List<PhysicalDeviceInventory> physicalDeviceInventory = physicalDeviceInventoryRepository.findPhysicalDeviceInventoriesByImei (imei  );

        if ( physicalDeviceInventory == null ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory for this Imei" , "" ) );
        } else
            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Inventory Based on Imei Retrieved Successfully!!" , physicalDeviceInventory ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > deleteInventory ( Integer id ) {
        PhysicalDeviceInventory physicalDeviceInventory = getParticularInventory ( id );
        if ( physicalDeviceInventory == null ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory!!" , "" ) );
        }
        physicalDeviceInventoryRepository.deleteById ( id );
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Deleted Inventory Successfully!!" , "" ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > editInventory ( Integer id , InventoryDTO inventoryDTO ) throws JsonProcessingException {
        PhysicalDeviceInventory updatedInventories = getParticularInventory ( id );
        if ( updatedInventories == null ) {
            return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory!!" , "" ) );
        }
        updatedInventories.setImei ( inventoryDTO.getImei ( ) );
        updatedInventories.setDeviceType ( inventoryDTO.getDeviceType ( ) );
        updatedInventories.setModel ( inventoryDTO.getModel ( ) );
        String simTypeJson = objectMapper.writeValueAsString(inventoryDTO.getSimType());
        updatedInventories.setSimType(simTypeJson);
        updatedInventories.setPreviousHolder ( inventoryDTO.getPreviousHolder ( ) );
        updatedInventories.setCurrentHolder ( inventoryDTO.getCurrentHolder ( ) );
        updatedInventories.setEntryCreatedDate ( inventoryDTO.getEntryCreatedDate ( ) );
        updatedInventories.setLastUpdatedDate ( Timestamp.valueOf ( LocalDateTime.now ( ) ) );
        updatedInventories.setRemarks ( inventoryDTO.getRemarks ( ) );
        updatedInventories.setBusinessNeeded ( inventoryDTO.getBusinessNeeded ( ) );
        physicalDeviceInventoryRepository.save ( updatedInventories );

        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Updated Inventories Successfully!!" , "" ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > getParticularDevice ( String device ) {
        List < PhysicalDeviceInventory > physicalDeviceInventory = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryByDeviceType ( device );
        if ( physicalDeviceInventory.isEmpty ( ) ) {
            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory for this Device Type!!" , "" ) );
        }
        List < ParticularInventoryDTO > particularInventoryDTOS = physicalDeviceInventory.stream ( )
                .map ( physicalDeviceInventory1 -> {
                    ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO ( );
                    particularInventoryDTO.setImei ( physicalDeviceInventory1.getImei ( ) );
                    particularInventoryDTO.setDeviceType ( physicalDeviceInventory1.getDeviceType ( ) );
                    particularInventoryDTO.setModel ( physicalDeviceInventory1.getModel ( ) );
                    List<String> simTypeList = null;
                    try {
                        simTypeList = objectMapper.readValue(physicalDeviceInventory1.getSimType(), new TypeReference < List <String> > (){});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                    particularInventoryDTO.setSimType ( simTypeList );
//                    particularInventoryDTO.setSimType ( physicalDeviceInventory1.getSimType ( ) );
                    particularInventoryDTO.setPreviousHolder ( physicalDeviceInventory1.getPreviousHolder ( ) );
                    particularInventoryDTO.setCurrentHolder ( physicalDeviceInventory1.getCurrentHolder ( ) );
                    particularInventoryDTO.setEntryCreatedDate ( physicalDeviceInventory1.getEntryCreatedDate ( ) );
                    particularInventoryDTO.setLastUpdatedDate ( physicalDeviceInventory1.getLastUpdatedDate ( ) );
                    particularInventoryDTO.setRemarks ( physicalDeviceInventory1.getRemarks ( ) );
                    particularInventoryDTO.setBusinessNeeded ( physicalDeviceInventory1.getBusinessNeeded ( ) );
                    return particularInventoryDTO;
                } ).toList ( );

        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Retrieved Inventory Based on Device!!" , particularInventoryDTOS ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > getParticularModel ( String model ) {
        List < PhysicalDeviceInventory > physicalDeviceInventory = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryByModel ( model );
        if ( physicalDeviceInventory.isEmpty ( ) ) {
            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory for this Model Type!!" , "" ) );
        }
        List < ParticularInventoryDTO > particularInventoryDTOS = physicalDeviceInventory.stream ( )
                .map ( physicalDeviceInventory1 -> {
                    ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO ( );
                    particularInventoryDTO.setImei ( physicalDeviceInventory1.getImei ( ) );
                    particularInventoryDTO.setDeviceType ( physicalDeviceInventory1.getDeviceType ( ) );
                    particularInventoryDTO.setModel ( physicalDeviceInventory1.getModel ( ) );
                    List<String> simTypeList = null;
                    try {
                        simTypeList = objectMapper.readValue(physicalDeviceInventory1.getSimType(), new TypeReference < List <String> > (){});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                    particularInventoryDTO.setSimType ( simTypeList );

//                    particularInventoryDTO.setSimType ( physicalDeviceInventory1.getSimType ( ) );
                    particularInventoryDTO.setPreviousHolder ( physicalDeviceInventory1.getPreviousHolder ( ) );
                    particularInventoryDTO.setCurrentHolder ( physicalDeviceInventory1.getCurrentHolder ( ) );
                    particularInventoryDTO.setEntryCreatedDate ( physicalDeviceInventory1.getEntryCreatedDate ( ) );
                    particularInventoryDTO.setLastUpdatedDate ( physicalDeviceInventory1.getLastUpdatedDate ( ) );
                    particularInventoryDTO.setRemarks ( physicalDeviceInventory1.getRemarks ( ) );
                    particularInventoryDTO.setBusinessNeeded ( physicalDeviceInventory1.getBusinessNeeded ( ) );
                    return particularInventoryDTO;
                } ).toList ( );

        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Retrieved Inventory Based on Model!!" , particularInventoryDTOS ) );
    }

    @Override
    public ResponseEntity < ResponseDTO > getParticularSim ( String sim ) {
        List < PhysicalDeviceInventory > physicalDeviceInventory = physicalDeviceInventoryRepository.findPhysicalDeviceInventoryBySimType ( sim );
        if ( physicalDeviceInventory.isEmpty ( ) ) {
            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory for this SIM Type!!" , "" ) );
        }
        List < ParticularInventoryDTO > particularInventoryDTOS = physicalDeviceInventory.stream ( )
                .map ( physicalDeviceInventory1 -> {
                    ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO ( );
                    particularInventoryDTO.setImei ( physicalDeviceInventory1.getImei ( ) );
                    particularInventoryDTO.setDeviceType ( physicalDeviceInventory1.getDeviceType ( ) );
                    particularInventoryDTO.setModel ( physicalDeviceInventory1.getModel ( ) );
//                    particularInventoryDTO.setSimType ( physicalDeviceInventory1.getSimType ( ) );
                    List<String> simTypeList = null;
                    try {
                        simTypeList = objectMapper.readValue(physicalDeviceInventory1.getSimType(), new TypeReference < List <String> > (){});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException ( e );
                    }
                    particularInventoryDTO.setSimType ( simTypeList );
                    particularInventoryDTO.setPreviousHolder ( physicalDeviceInventory1.getPreviousHolder ( ) );
                    particularInventoryDTO.setCurrentHolder ( physicalDeviceInventory1.getCurrentHolder ( ) );
                    particularInventoryDTO.setEntryCreatedDate ( physicalDeviceInventory1.getEntryCreatedDate ( ) );
                    particularInventoryDTO.setLastUpdatedDate ( physicalDeviceInventory1.getLastUpdatedDate ( ) );
                    particularInventoryDTO.setRemarks ( physicalDeviceInventory1.getRemarks ( ) );
                    particularInventoryDTO.setBusinessNeeded ( physicalDeviceInventory1.getBusinessNeeded ( ) );
                    return particularInventoryDTO;
                } ).toList ( );

        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Retrieved Inventory Based on SIM!!" , particularInventoryDTOS ) );
    }

//    @Override
//    public ResponseEntity < ResponseDTO > filterRecords ( String device , String model , String sim ) {
//
//        String deviceNull = (device == null || device.isEmpty ( )) ? null : device;
//        String modelNull = (model == null || model.isEmpty ( )) ? null : model;
//        String simNull = (sim == null || sim.isEmpty ( )) ? null : sim;
//        List < PhysicalDeviceInventory > physicalDeviceInventories = physicalDeviceInventoryRepository.findAll ( );
//        if ( deviceNull == null && modelNull == null && simNull == null ) {
//            List < ParticularInventoryDTO > particularInventoryDTOS = physicalDeviceInventories.stream ( )
//                    .map ( physicalDeviceInventory1 -> {
//                        ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO ( );
//                        particularInventoryDTO.setImei ( physicalDeviceInventory1.getImei ( ) );
//                        particularInventoryDTO.setDeviceType ( physicalDeviceInventory1.getDeviceType ( ) );
//                        particularInventoryDTO.setModel ( physicalDeviceInventory1.getModel ( ) );
////                        particularInventoryDTO.setSimType ( physicalDeviceInventory1.getSimType ( ) );
//                        List<String> simTypeList = null;
//                        try {
//                            simTypeList = objectMapper.readValue(physicalDeviceInventory1.getSimType(), new TypeReference < List <String> > (){});
//                        } catch (JsonProcessingException e) {
//                            throw new RuntimeException ( e );
//                        }
//                        particularInventoryDTO.setSimType ( simTypeList );
//                        particularInventoryDTO.setPreviousHolder ( physicalDeviceInventory1.getPreviousHolder ( ) );
//                        particularInventoryDTO.setCurrentHolder ( physicalDeviceInventory1.getCurrentHolder ( ) );
//                        particularInventoryDTO.setEntryCreatedDate ( physicalDeviceInventory1.getEntryCreatedDate ( ) );
//                        particularInventoryDTO.setLastUpdatedDate ( physicalDeviceInventory1.getLastUpdatedDate ( ) );
//                        particularInventoryDTO.setRemarks ( physicalDeviceInventory1.getRemarks ( ) );
//                        particularInventoryDTO.setBusinessNeeded ( physicalDeviceInventory1.getBusinessNeeded ( ) );
//                        return particularInventoryDTO;
//                    } ).toList ( );
//
//            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Retrieved Inventory!!" , particularInventoryDTOS ) );
//
//        }
//            List < PhysicalDeviceInventory > physicalDeviceInventoryList = physicalDeviceInventoryRepository.findPhysicalDeviceInventoriesByDeviceTypeAndModelAndSimType ( deviceNull , modelNull , s );
//
//        System.out.println (physicalDeviceInventoryList +"deepu"+simNull );
//        if ( physicalDeviceInventoryList.isEmpty ( ) ) {
//            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( false , HttpStatus.BAD_REQUEST , "There is No Inventory!!" , "" ) );
//        }
//        List < ParticularInventoryDTO > particularInventoryDTOS = physicalDeviceInventoryList.stream ( )
//                .map ( physicalDeviceInventory1 -> {
//                    ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO ( );
//                    particularInventoryDTO.setImei ( physicalDeviceInventory1.getImei ( ) );
//                    particularInventoryDTO.setDeviceType ( physicalDeviceInventory1.getDeviceType ( ) );
//                    particularInventoryDTO.setModel ( physicalDeviceInventory1.getModel ( ) );
//                    List<String> simTypeList = null;
//                    try {
//                        simTypeList = objectMapper.readValue(physicalDeviceInventory1.getSimType(), new TypeReference < List <String> > (){});
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException ( e );
//                    }
//                    particularInventoryDTO.setSimType ( simTypeList );
////                    particularInventoryDTO.setSimType ( physicalDeviceInventory1.getSimType ( ) );
//                    particularInventoryDTO.setPreviousHolder ( physicalDeviceInventory1.getPreviousHolder ( ) );
//                    particularInventoryDTO.setCurrentHolder ( physicalDeviceInventory1.getCurrentHolder ( ) );
//                    particularInventoryDTO.setEntryCreatedDate ( physicalDeviceInventory1.getEntryCreatedDate ( ) );
//                    particularInventoryDTO.setLastUpdatedDate ( physicalDeviceInventory1.getLastUpdatedDate ( ) );
//                    particularInventoryDTO.setRemarks ( physicalDeviceInventory1.getRemarks ( ) );
//                    particularInventoryDTO.setBusinessNeeded ( physicalDeviceInventory1.getBusinessNeeded ( ) );
//                    return particularInventoryDTO;
//                } ).toList ( );
//
//        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Retrieved Inventory!!" , particularInventoryDTOS ) );
//    }

    @Override
    public ResponseEntity<ResponseDTO> filterRecords(String device, String model, String sim) {
        String deviceNull = (device == null || device.isEmpty()) ? null : device;
        String modelNull = (model == null || model.isEmpty()) ? null : model;
        List<String> simList = (sim == null || sim.isEmpty()) ? null : Arrays.asList(sim.split(","));

        List<PhysicalDeviceInventory> physicalDeviceInventories = physicalDeviceInventoryRepository.findAll();
        List<PhysicalDeviceInventory> filteredInventories = new ArrayList<>();

        for (PhysicalDeviceInventory inventory : physicalDeviceInventories) {
            List<String> simTypeList = null;
            try {
                simTypeList = objectMapper.readValue(inventory.getSimType(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            boolean matchesDevice = (deviceNull == null || deviceNull.equals(inventory.getDeviceType()));
            boolean matchesModel = (modelNull == null || modelNull.equals(inventory.getModel()));
            boolean matchesSim = (simList == null || !Collections.disjoint(simList, simTypeList));

            if (matchesDevice && matchesModel && matchesSim) {
                filteredInventories.add(inventory);
            }
        }

        if (filteredInventories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(false, HttpStatus.BAD_REQUEST, "There is No Inventory!!", ""));
        }

        List<ParticularInventoryDTO> particularInventoryDTOS = filteredInventories.stream()
                .map(physicalDeviceInventory -> {
                    ParticularInventoryDTO particularInventoryDTO = new ParticularInventoryDTO();
                    particularInventoryDTO.setImei(physicalDeviceInventory.getImei());
                    particularInventoryDTO.setDeviceType(physicalDeviceInventory.getDeviceType());
                    particularInventoryDTO.setModel(physicalDeviceInventory.getModel());

                    List<String> simTypeList;
                    try {
                        simTypeList = objectMapper.readValue(physicalDeviceInventory.getSimType(), new TypeReference<List<String>>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    particularInventoryDTO.setSimType(simTypeList);

                    particularInventoryDTO.setPreviousHolder(physicalDeviceInventory.getPreviousHolder());
                    particularInventoryDTO.setCurrentHolder(physicalDeviceInventory.getCurrentHolder());
                    particularInventoryDTO.setEntryCreatedDate(physicalDeviceInventory.getEntryCreatedDate());
                    particularInventoryDTO.setLastUpdatedDate(physicalDeviceInventory.getLastUpdatedDate());
                    particularInventoryDTO.setRemarks(physicalDeviceInventory.getRemarks());
                    particularInventoryDTO.setBusinessNeeded(physicalDeviceInventory.getBusinessNeeded());

                    return particularInventoryDTO;
                }).toList();


        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, HttpStatus.OK, "Retrieved Inventory!!", particularInventoryDTOS));
    }



    @Override
    public ResponseEntity < ResponseDTO > deleteAll ( ) {
        physicalDeviceInventoryRepository.deleteAll ( );
        return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Deleted All Records!!" , "" ) );


    }

    @Override
    public ResponseEntity < ResponseDTO > dataFromExcel ( MultipartFile file ) throws IOException {
        if ( excelData.isValidExcelFile ( file ) ) {
            List < PhysicalDeviceInventory > physicalDeviceInventoryList = excelData.getData ( file.getInputStream ( ) );
            if ( physicalDeviceInventoryList.isEmpty ( ) ) {
                return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( Boolean.FALSE , HttpStatus.BAD_REQUEST , "IMEI is Already Exist.." , null ) );

            }
            physicalDeviceInventoryRepository.saveAll ( physicalDeviceInventoryList );
            return ResponseEntity.status ( HttpStatus.OK ).body ( new ResponseDTO ( true , HttpStatus.OK , "Excel Data Added Successfully !" , null ) );

        }
        return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).body ( new ResponseDTO ( Boolean.FALSE , HttpStatus.BAD_REQUEST , "Failed to Add Data!!" , null ) );

    }

    @Override
    public ResponseEntity<ResponseDTO> deleteListOfInventory(ListIntegerDTO inventoryDTOList) {
        if (inventoryDTOList == null || inventoryDTOList.getIntegerList () == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false, HttpStatus.BAD_REQUEST, "Inventory list must not be null", null));
        }

        try {
            physicalDeviceInventoryRepository.deleteAllById(inventoryDTOList.getIntegerList ());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(true, HttpStatus.OK, "Deleted Inventories Successfully!!", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false, HttpStatus.BAD_REQUEST, "Failed to Delete Inventory!!", null));
        }
    }
}