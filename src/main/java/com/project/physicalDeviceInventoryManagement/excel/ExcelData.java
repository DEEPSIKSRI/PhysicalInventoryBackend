package com.project.physicalDeviceInventoryManagement.excel;

import com.project.physicalDeviceInventoryManagement.entity.PhysicalDeviceInventory;
import com.project.physicalDeviceInventoryManagement.repository.PhysicalDeviceInventoryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ExcelData {

    private final PhysicalDeviceInventoryRepository physicalDeviceInventoryRepository;

    @Autowired
    public ExcelData(PhysicalDeviceInventoryRepository physicalDeviceInventoryRepository) {
        this.physicalDeviceInventoryRepository = physicalDeviceInventoryRepository;
    }

    public boolean isValidExcelFile(MultipartFile file) {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContentType());
    }

    public List<PhysicalDeviceInventory> getData(InputStream inputStream) throws IOException {
        List<PhysicalDeviceInventory> physicalDeviceInventoriesList = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("RealDevice_list");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

        int rowIndex = 0;
        for (Row row : sheet) {
            if (rowIndex == 0) {
                rowIndex++;
                continue;
            }
            Iterator<Cell> cellIterator = row.iterator();
            int cellIndex = 0;
            PhysicalDeviceInventory physicalDeviceInventory = new PhysicalDeviceInventory();
            boolean isRowEmpty = true;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cellIndex) {
                    case 0 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setImei(String.valueOf((long) cell.getNumericCellValue()));
                        } else {
                            physicalDeviceInventory.setImei(cell.getStringCellValue());
                        }
                        if (physicalDeviceInventory.getImei() != null && !physicalDeviceInventory.getImei().isEmpty()) {
                            isRowEmpty = false;
                        }
                    }
                    case 1 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setDeviceType(String.valueOf((long) cell.getNumericCellValue()));
                        } else {
                            physicalDeviceInventory.setDeviceType(cell.getStringCellValue());
                        }
                    }
                    case 2 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setModel(String.valueOf((long) cell.getNumericCellValue()));
                        } else {
                            physicalDeviceInventory.setModel(cell.getStringCellValue());
                        }
                    }
                    case 3 -> {
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            String sim = cell.getStringCellValue ();
//                            List<String> simList = Arrays.asList ( sim.split ( (",") ));
                            String formattedSim = "[\"" + sim + "\"]";
                            physicalDeviceInventory.setSimType ( formattedSim);
                            System.out.println (physicalDeviceInventory.getSimType () +"sim" );
                            } else {
                            physicalDeviceInventory.setSimType(cell.getStringCellValue());
                        }
                    }
                    case 4 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setCurrentHolder(String.valueOf((long) cell.getNumericCellValue()));
                        } else {
                            physicalDeviceInventory.setCurrentHolder(cell.getStringCellValue());
                        }
                    }
                    case 5 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setPreviousHolder(String.valueOf((long) cell.getNumericCellValue()));
                        } else {
                            physicalDeviceInventory.setPreviousHolder(cell.getStringCellValue());
                        }
                    }
                    case 6 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setEntryCreatedDate(new Timestamp(cell.getDateCellValue().getTime()));
                        } else {
                            String dateString = cell.getStringCellValue();
                            if (datePattern.matcher(dateString).matches()) {
                                try {
                                    physicalDeviceInventory.setEntryCreatedDate(new Timestamp(dateFormat.parse(dateString).getTime()));
                                } catch (ParseException e) {
                                    physicalDeviceInventory.setEntryCreatedDate(null);
                                }
                            } else {
                                physicalDeviceInventory.setEntryCreatedDate(null);
                            }
                        }
                    }
                    case 7 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setLastUpdatedDate(new Timestamp(cell.getDateCellValue().getTime()));
                        } else {
                            String dateString = cell.getStringCellValue();
                            if (datePattern.matcher(dateString).matches()) {
                                try {
                                    physicalDeviceInventory.setLastUpdatedDate(new Timestamp(dateFormat.parse(dateString).getTime()));
                                } catch (ParseException e) {
                                    physicalDeviceInventory.setLastUpdatedDate(null);
                                }
                            } else {
                                physicalDeviceInventory.setLastUpdatedDate(null);
                            }
                        }
                    }
                    case 8 -> {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            physicalDeviceInventory.setRemarks(String.valueOf((long) cell.getNumericCellValue()));
                        } else {
                            physicalDeviceInventory.setRemarks(cell.getStringCellValue());
                        }
                    }
                    default -> {
                    }
                }
                cellIndex++;
            }
            if (!isRowEmpty && !physicalDeviceInventoryRepository.existsByImei(physicalDeviceInventory.getImei())) {
                physicalDeviceInventoriesList.add(physicalDeviceInventory);
            }
        }
        workbook.close();
        return physicalDeviceInventoriesList;
    }
}