package com.datn.motchill.admin.common.utils;

import com.datn.motchill.admin.presentation.enums.AdminIsActiveEnum;
import com.datn.motchill.admin.presentation.enums.AdminIsDisplayEnum;
import com.datn.motchill.admin.presentation.enums.AdminStatusEnum;
import com.datn.motchill.shared.annotation.ExcelColumn;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author: tienlm_llq LAC LONG QUAN
 * Since: 20/03/2025 06:04 pm
 * Description:
 */
public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    //style cho header excel
    private static CellStyle styleForHeader(SXSSFSheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        //font.setFontHeight((short)14);
        //text color
        //font.setColor(IndexedColors.BRIGHT_GREEN.getIndex());

        //cell style
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillBackgroundColor(IndexedColors.SEA_GREEN.getIndex());
        //cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    //style for dòng
    private static CellStyle styleForRow(SXSSFSheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    //style cho kiểu dữ liệu date
    private static CellStyle getDateStyle(SXSSFSheet sheet) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        CreationHelper creationHelper = sheet.getWorkbook().getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
        return cellStyle;
    }

    //style cho kiểu dữ liệu datetime
    private static CellStyle getDateTimeStyle(SXSSFSheet sheet) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        CreationHelper creationHelper = sheet.getWorkbook().getCreationHelper();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
        return cellStyle;
    }

    //đầu trang excel
    private static int writeHeader(SXSSFSheet sheet, int rowIndex, List<String> columnHeader, String paramName) {
        CellStyle cellStyle = styleForHeader(sheet);
        SXSSFRow row = sheet.createRow(rowIndex);
        Cell cell;
        cell = row.createCell(1);
        cell.setCellValue("Motchill");
        rowIndex++;
        //---------------
        row = sheet.createRow(rowIndex);
        // Lấy thời gian hiện tại
        LocalDate currentDate = LocalDate.now();
        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        cell = row.createCell(3);
        cell.setCellStyle(styleForRow(sheet));
        cell.setCellValue("Ngày tạo: " + formattedDate);

        rowIndex += 1;
        row = sheet.createRow(rowIndex);
        row.setHeightInPoints((short) 36);

        cell = row.createCell(1);

        CellStyle paramNameCellStyle = sheet.getWorkbook().createCellStyle();

        Font paramNameCellFont = sheet.getWorkbook().createFont();
        paramNameCellFont.setBold(true);
        paramNameCellFont.setFontName("Times New Roman");
        paramNameCellFont.setFontHeightInPoints((short) 14);
        paramNameCellStyle.setFont(paramNameCellFont);
        paramNameCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        cell.setCellValue(paramName);
        cell.setCellStyle(paramNameCellStyle);

        rowIndex += 2;
        //---------------
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        for (int i = 0; i < columnHeader.size(); i++) {
            cell = row.createCell(i + 1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnHeader.get(i));
        }
        return rowIndex + 1;
    }


    //lấy ra tên field của đối tượng và value annotation @ExcelColumn
    private static <T> void extractFiled(Class<T> clazz, List<String> anFiled, List<String> nameFiled) {
       // Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
//                if (!field.canAccess(object)) {
                    field.setAccessible(true);  // Cân nhắc giữ lại dòng này nếu không còn cách nào khác
//                }
                nameFiled.add(field.getName());
                anFiled.add(annotation.value());
            }
        }
    }

    private static void extractFieldsFromClass(Class<?> clazz, List<String> anFiled, List<String> nameFiled){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                field.setAccessible(true);  // Ensure the field is accessible
                nameFiled.add(field.getName());
                anFiled.add(annotation.value());
            }
        }
    }


    //ghi một dòng excel
    private static <T> void writeValue(List<String> data, Row row, T clazz, int index, SXSSFSheet sheet) throws NoSuchFieldException, IllegalAccessException {
        Cell cell;
        Field field;
        cell = row.createCell(0);
        cell.setCellValue(index);
        for (int i = 0; i < data.size(); i++) {
            cell = row.createCell(i + 1);
            field = clazz.getClass().getDeclaredField(data.get(i));
//            if (!field.canAccess(clazz)) {
                field.setAccessible(true);  // Cân nhắc giữ lại dòng này nếu không còn cách nào khác
//            }
            //Object test = field.get(clazz);
            setRowValue(cell, field.get(clazz), sheet);
        }
    }

    //Chuyển dữ liệu từ java -> excel
    private static void setRowValue(Cell cell, Object value, SXSSFSheet sheet) {
    	if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellStyle(getDateStyle(sheet));
            cell.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellStyle(getDateTimeStyle(sheet));
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            cell.setCellStyle(getDateStyle(sheet));
            cell.setCellValue(((LocalDate) value).atStartOfDay());
        } else if (value instanceof AdminIsActiveEnum) {
            cell.setCellValue(((AdminIsActiveEnum) value).getValue());
        } else if (value instanceof AdminStatusEnum) {
            cell.setCellValue(((AdminStatusEnum) value).getValue());
        } else if (value instanceof AdminIsDisplayEnum) {
            cell.setCellValue(((AdminIsDisplayEnum) value).getValue());
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }

    //tự động điều chỉnh kích cỡ
    private static void autosizeColumn(SXSSFSheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    //viết excel
    public static <T> Workbook getWorkBox(Class<T> clazz, List<T> data, String paramName) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Sheet1");
        int rowIndex = 0;
        //list tên cột excel
        List<String> alias = new ArrayList<>();
        //list tên cột excel
        List<String> column = new ArrayList<>();
        extractFiled(clazz, alias, column);
        rowIndex = writeHeader(sheet, rowIndex, alias, paramName);
        //đánh stt bản ghi
        int index = 1;
        try {
            for (T item : data) {
                Row row = sheet.createRow(rowIndex);
                writeValue(column, row, item, index, sheet);
                rowIndex++;
                index++;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("error when export to excel");
        }
        return workbook;
    }

    /**
     *
     * @param workbook
     * @param rowIndex dòng bắt đầu ghi data
     * @param data
     * @param clazz
     * @return
     * @param <T>
     */
    public static<T> SXSSFWorkbook writeWordBook(SXSSFWorkbook workbook,int rowIndex,List<T> data,Class<T> clazz){
        SXSSFSheet sheet = (SXSSFSheet) workbook.getSheetAt(0);
        //lấy ra các trường của class
        Field[] fields = clazz.getDeclaredFields();
        //danh sách tên các trường của class
        List<String> listColumn = new ArrayList<>();
        for (Field field: fields){
            listColumn.add(field.getName());
        }
        //ghi từ dòng index
        int index = 1;
        try {
            for (T item : data) {
                Row row = sheet.createRow(rowIndex);
                writeValue(listColumn, row, item, index, sheet);
                rowIndex++;
                index++;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("error when export to excel");
        }
        return workbook;
    }

    public static<T> void exportExcel(HttpServletResponse response, String path, String fileName, List<T> listData, int rowIndex, Class<T> clazz) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename= " + fileName + ".xlsx";
        InputStream fileExcelInpS;
        try{
            fileExcelInpS = new ClassPathResource(path).getInputStream();
        }catch (IOException e){
            log.error(e.getMessage());
            throw new IOException("Template file not found");
        }
        XSSFWorkbook workbook = new XSSFWorkbook(fileExcelInpS);
        //tạo file excel dùng để xuất dữ liệu lớn
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook);
        writeWordBook(sxssfWorkbook, rowIndex, listData, clazz);
        ServletOutputStream outputStream = response.getOutputStream();
        sxssfWorkbook.write(outputStream);
        response.setHeader(headerKey, headerValue);
        workbook.close();
        outputStream.close();
    }


    //hàm export
    public static <T> void export(HttpServletResponse response, Class<T> clazz, List<T> data, String fileName) throws IOException {
        export(response, clazz, data, fileName, fileName);
    }

    public static <T> void export(HttpServletResponse response, Class<T> clazz, List<T> data, String fileName, String paramName) throws IOException {
        Workbook workbook = getWorkBox(clazz, data, paramName);
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename= " + fileName + ".xlsx";
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        response.setHeader(headerKey, headerValue);
        workbook.close();
        outputStream.close();
    }





    /**
     * @param excelFile Multipath file excel
     * @param handler   Hàm xử lý ánh xạ dữ liệu
     * @param index     vị trí cột header, không tính những dòng không có giá trị, bắt đầu từ 1
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> importExcel(MultipartFile excelFile, Function<Map<String, Cell>, T> handler, int index, Class<T> clazz) throws IOException, IllegalArgumentException {
        //kiểm tra xem có phải đuôi file là .xlsx hay không
        if (!checkFile(excelFile)) {
            throw new IllegalArgumentException("The excel file is not in the correct format");
        }
        //Lấy ra các giá trị được đánh dấu với @ExcelColumn
        List<String> fieldName = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
//                if (!field.canAccess(clazz)) {
                    field.setAccessible(true);  // Cân nhắc giữ lại dòng này nếu không còn cách nào khác
//                }
                fieldName.add(annotation.value());

            }
        }
        //Nếu không đánh dấu cột nào thì hủy
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("The excel file is not in the correct format");
        }
        //Danh sách chứa kết quả đối tượng sau cùng
        List<T> result = new ArrayList<>();
        //Map chứa tên cột excel và vị trí của nó
        Map<String, Integer> columnIndices = new HashMap<>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        int rowColumn = 1;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            // Nếu rowColumn < index thì tăng rowColumn và bỏ qua phần còn lại
            if (rowColumn < index) {
                rowColumn++;
                continue;  // Giữ lại một lệnh continue
            }

            // Nếu là cột header thì thêm vào map
            if (rowColumn == index) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                int columnIndex = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    columnIndices.put(cell.getStringCellValue(), columnIndex++);
                }
                // Chỉ lấy những cột mà nằm trong danh sách value @ExcelColumn
                columnIndices = columnIndices.entrySet().stream()
                        .filter(entry -> fieldName.contains(entry.getKey()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));
                rowColumn++;
            } else {
                // Kiểm tra xem map có bị rỗng hay không
                if (columnIndices.isEmpty()) {
                    throw new IllegalArgumentException("The excel file is not in the correct format");
                }

                Iterator<Cell> cellIterator = nextRow.cellIterator();
                // Map chứa tên cột và Cell tương ứng
                Map<String, Cell> cellMap = new HashMap<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Map<String, Integer> finalColumnIndices = columnIndices;
                    columnIndices.keySet().stream()
                            .filter(key -> finalColumnIndices.get(key).equals(cell.getColumnIndex()))
                            .findFirst().ifPresent(columnName -> cellMap.put(columnName, cell));
                }

                // Nếu cellMap không rỗng thì mới xử lý tiếp
                if (!cellMap.isEmpty()) {
                    T value = handler.apply(cellMap);
                    if (value != null) {
                        result.add(value);
                    }
                }
            }
        }

        workbook.close();
        return result;
    }

    public static <T> List<T> importExcelOption(MultipartFile excelFile, Function<Map<String, Cell>, T> handler, int index, Class<T> clazz) throws IOException, IllegalArgumentException {
        //kiểm tra xem có phải đuôi file là .xlsx hay không
        if (!checkFile(excelFile)) {
            throw new IllegalArgumentException("The excel file is not in the correct format");
        }
        //Lấy ra các giá trị được đánh dấu với @ExcelColumn
        List<String> fieldName = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
//                if (!field.canAccess(clazz)) {
                field.setAccessible(true);  // Cân nhắc giữ lại dòng này nếu không còn cách nào khác
//                }
                fieldName.add(annotation.value());

            }
        }
        //Nếu không đánh dấu cột nào thì hủy
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("The excel file is not in the correct format");
        }
        //Danh sách chứa kết quả đối tượng sau cùng
        List<T> result = new ArrayList<>();
        //Map chứa tên cột excel và vị trí của nó
        Map<String, Integer> columnIndices = new HashMap<>();
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        int rowColumn = 1;
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();

            // Nếu rowColumn < index thì tăng rowColumn và bỏ qua phần còn lại
            if (rowColumn < index) {
                rowColumn++;
                continue;  // Giữ lại một lệnh continue
            }
            // Nếu là cột header thì thêm vào map
            if (rowColumn == index) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                int columnIndex = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    columnIndices.put(cell.getStringCellValue(), columnIndex++);
                }
                // Chỉ lấy những cột mà nằm trong danh sách value @ExcelColumn
                columnIndices = columnIndices.entrySet().stream()
                        .filter(entry -> fieldName.contains(entry.getKey()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));
                rowColumn++;

            }
            else if (rowColumn == index+1){
                rowColumn++;
            }
            else {
                // Kiểm tra xem map có bị rỗng hay không
                if (columnIndices.isEmpty()) {
                    throw new IllegalArgumentException("The excel file is not in the correct format");
                }

                Iterator<Cell> cellIterator = nextRow.cellIterator();
                // Map chứa tên cột và Cell tương ứng
                Map<String, Cell> cellMap = new HashMap<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Map<String, Integer> finalColumnIndices = columnIndices;
                    columnIndices.keySet().stream()
                            .filter(key -> finalColumnIndices.get(key).equals(cell.getColumnIndex()))
                            .findFirst().ifPresent(columnName -> cellMap.put(columnName, cell));
                }

                // Nếu cellMap không rỗng thì mới xử lý tiếp
                if (!cellMap.isEmpty()) {
                    T value = handler.apply(cellMap);
                    if (value != null) {
                        result.add(value);
                    }
                }
            }
        }

        workbook.close();
        return result;
    }

    private static boolean checkFile(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx");
    }

    public static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue();
                } else {
                    cellValue = cell.getNumericCellValue();
                }
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BLANK:
            case ERROR:
            default:
                break;
        }

        return cellValue;
    }

    public static String getCellValueFormatted(Cell cell) {
        DataFormatter formatter = new DataFormatter(Locale.US);
        return formatter.formatCellValue(cell);
    }


    public static String getCellValueFormatedForBigNumberValue(Cell cell) {

        DataFormatter formatter = new DataFormatter(Locale.US);

        // Lấy style của cell
        CellStyle style = cell.getCellStyle();

        // Lấy formatIndex (mã định dạng số của cell)
        int formatIndex = style.getDataFormat();

        // Lấy formatString (chuỗi định dạng số của cell)
        String formatString = style.getDataFormatString();

        // Nếu formatString là null, dùng định dạng mặc định
        if (formatString == null) {
            formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
        }

        // Định dạng giá trị của cell
        String formattedValue = formatter.formatRawCellContents(
                BigDecimal.valueOf(cell.getNumericCellValue()).doubleValue(), formatIndex, formatString
        );


        return formattedValue.replaceAll("[^\\d.]", "");
    }

    public static void exportTemplate(HttpServletResponse response, String fileName, String path) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename= " + fileName + ".xlsx";
        InputStream fileExcel = new ClassPathResource(path).getInputStream();
        Workbook workbook = WorkbookFactory.create(fileExcel);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        response.setHeader(headerKey, headerValue);
        workbook.close();
        outputStream.close();
    }

    public static void writeHeaderData(String[] headerRowValues, SXSSFSheet sheet, int rowIndex) {
        CellStyle cellStyle = styleForHeader(sheet);
        Cell cell;
        Row headerRow;
        headerRow = sheet.createRow(rowIndex);
        cell = headerRow.createCell(1);
        cell.setCellValue("TABLE DATA");
        cell.setCellStyle(cellStyle);

        //Tạo tiêu đề cho cột
        headerRow = sheet.createRow(rowIndex + 2);
        for (int i = 0; i < headerRowValues.length; i++) {
            cell = headerRow.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headerRowValues[i]);
        }
    }

    public static <T> List<String> getAlias(Class<T> clazz) {
        List<String> alias = new ArrayList<>();
        List<String> column = new ArrayList<>();
        extractFiled(clazz, alias, column);
        return alias;
    }

    /**
     *
     * @param response
     * @param fileName
     * @param path
     * @param lists
     * @param headers danh sách các cột trong sheet để nhập dữ liệu
     * @param ignoreFields
     */
    public static void exportData(HttpServletResponse response, String fileName, String path, Map<String, List<String>> lists, List<String> headers, List<String> ignoreFields) {
        SXSSFWorkbook workbook = null;
        ServletOutputStream outputStream = null;
        try {
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename= " + fileName + ".xlsx";
            workbook = ExcelUtils.getWorkBook(path);
            outputStream = response.getOutputStream();

            XSSFSheet tempSheet;
            SXSSFSheet sheet;
            SXSSFSheet sheet1 = workbook.getSheetAt(0);

            //Tạo sheet để lưu trữ data
            if (workbook.getNumberOfSheets() > 1) {
                sheet = workbook.getSheetAt(1);
            } else {
                sheet = workbook.createSheet("Data");
            }
            int rowIndex = 0;
            //Lưu tên trường cho cột
            List<String> headersDataSheet = new ArrayList<>();
            headers.forEach(header -> {
                if(lists.containsKey(header)) {
                    headersDataSheet.add(header);
                }
            });
            ExcelUtils.writeHeaderData(headersDataSheet.toArray(new String[0]), sheet, rowIndex);
            rowIndex += 3; // trừ dòng TABLE DATA, dòng trống và header
            //Lưu dữ liệu vào sheet 2
            Optional<Integer> maxOptional = lists.keySet().stream()
                    .map(key -> lists.get(key).size())
                    .max(Integer::compareTo);

            int max = maxOptional.orElse(0);  // Trả về 0 nếu không có giá trị max
            for (int i = 0; i < max; i++) {
                Row row = sheet.getRow(i + rowIndex);
                if (row == null) {
                    row = sheet.createRow(i + rowIndex);
                }

                for (Map.Entry<String, List<String>> entry : lists.entrySet()) {
                    String key = entry.getKey();
                    List<String> values = entry.getValue();

                    if (i < values.size()) {
                        row.createCell(headersDataSheet.indexOf(key)).setCellValue(values.get(i));
                    }
                }
            }


            //Tạo workbook để lưu tạm thời
            XSSFWorkbook tempWorkbook = new XSSFWorkbook();
            tempSheet = tempWorkbook.createSheet("Temp");

            // Tạo droplist
            CellRangeAddressList addressList;
            String formula;
            for (String key : headersDataSheet) {
                if(!(!ObjectUtils.isEmpty(ignoreFields) && ignoreFields.contains(key))) {
                    int colInDataSheet = headersDataSheet.indexOf(key);
                    int col = headers.indexOf(key);
                    String colName = getColumnIndex(colInDataSheet);
                    addressList = new CellRangeAddressList(5, 5, col + 1, col + 1);
                    formula = "Data!$" +colName+ "$"+(rowIndex + 1)+":$"+colName+"$" + (rowIndex + lists.get(key).size());
                    // Đẩy dữ liệu vào sheet 1 với ô nhớ cố định
                    ExcelUtils.getDataValidation(addressList, formula, tempSheet, sheet1);
                }
            }

            response.setHeader(headerKey, headerValue);
            response.setContentType("application/octet-stream");

            workbook.write(outputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if(workbook != null) {
                    workbook.close();
                }
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }


    public static String getColumnIndex(String colName, List<String> headers) {
        int col = headers.indexOf(colName);
        return getColumnIndex(col);
    }
    public static String getColumnIndex(int col) {
        StringBuilder colIdx = new StringBuilder("");
        while (col >= 0) {
            if(col < 26) { // đổi sang ký tự
                colIdx.append((char)('A' + col));
            }
            col -= 26;
        }
        return colIdx.toString();
    }


    public static void getDataValidation(CellRangeAddressList cellRangeAddressList, String formula, XSSFSheet tempSheet, SXSSFSheet sheet) {
        //Xử lý lấy dữ liệu từ tempSheet
        DataValidationHelper validationHelper = new XSSFDataValidationHelper(tempSheet);
        DataValidationConstraint dataValidationConstraint = validationHelper.createFormulaListConstraint(formula);

        //Xử lý trả ra dữ liệu và add vào sheet 1
        DataValidation dataValidation = validationHelper.createValidation(dataValidationConstraint, cellRangeAddressList);
        dataValidation.setSuppressDropDownArrow(true);
        //Ngăn chặn nhập dữ liệu không hợp lệ
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);

        dataValidation.createErrorBox("Title", "Message");
        dataValidation.setShowErrorBox(true);

        dataValidation.createPromptBox("List", "Please select a value from the list.");
        dataValidation.setShowPromptBox(true);

        tempSheet.addValidationData(dataValidation);
        for (DataValidation dv : tempSheet.getDataValidations()) {
            sheet.addValidationData(dv);
        }
    }


    public static SXSSFWorkbook getWorkBook(String path) throws IOException {
        // Sử dụng ClassPathResource để lấy file từ classpath
        ClassPathResource resource = new ClassPathResource(path);

        // Mở InputStream từ resource
        try (InputStream fileInputStream = resource.getInputStream()) {
            // Tạo XSSFWorkbook từ InputStream
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

            // Tạo và trả về SXSSFWorkbook từ XSSFWorkbook
            return new SXSSFWorkbook(workbook);
        }
    }

}
