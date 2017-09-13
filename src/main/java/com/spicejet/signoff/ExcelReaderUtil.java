package com.spicejet.signoff;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelReaderUtil {

    private Object getCellValue(Cell cell) {
        if (cell.getCellTypeEnum() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = cell.getDateCellValue();
                return df.format(date);
            }
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        }
        if (cell.getCellTypeEnum() == CellType.FORMULA) {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        }
        return null;
    }

    public Map<String, Book> readBooksFromExcelFile(String excelFilePath) throws IOException {
        LinkedHashMap<String, Book> mapBooks = new LinkedHashMap<>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        for (Row nextRow : firstSheet) {
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Book aBook = new Book();
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                int columnIndex = nextCell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        aBook.setNoFirst((String) getCellValue(nextCell));
                        break;
                    case 1:
                        aBook.setTypeFirst((String) getCellValue(nextCell));
                        break;
                    case 2:
                        aBook.setRevisionFirst((String) getCellValue(nextCell));
                        break;
                    case 3:
                        aBook.setByFirst((String) getCellValue(nextCell));
                        break;
                    case 4:
                        aBook.setNoSecond((String) getCellValue(nextCell));
                        break;
                    case 5:
                        aBook.setTypeSecond((String) getCellValue(nextCell));
                        break;
                    case 6:
                        aBook.setRevisionSecond((String) getCellValue(nextCell));
                        break;
                    case 7:
                        aBook.setBySecond((String) getCellValue(nextCell));
                        break;
                    default:
                        // do nothing
                }
            }
            mapBooks.put(aBook.getNoFirst(), aBook);
        }
        workbook.close();
        inputStream.close();
        return mapBooks;
    }

    public List<Book> processBooks(Map<String, Book> mapBooks) {
        List<Book> listBooks = new ArrayList<>();
        for (Map.Entry<String, Book> book : mapBooks.entrySet()) {
            Book aBook = book.getValue();
            if (aBook.getNoSecond() == null || aBook.getNoSecond().length() == 0) {
                setSecondHalfValues(listBooks, aBook);
                continue;
            }
            Book bBook = mapBooks.get(aBook.getNoSecond());
            if (bBook != null) {
                Book cBook = Book.getCopiedBookInstance(aBook);
                cBook.setTypeSecond(bBook.getTypeFirst());
                cBook.setRevisionSecond(bBook.getRevisionFirst());
                cBook.setBySecond(bBook.getByFirst());
                listBooks.add(cBook);
            }
            setSecondHalfValues(listBooks, aBook);
        }
        return listBooks;
    }

    public void writeBooksFromExcelFile(String inputExcelFilePath, String outputExcelFilePath, List<Book> listBooks)
            throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(getSheetName(inputExcelFilePath));
            int rowNum = 0;
            System.out.println("Processing : " + inputExcelFilePath
                    .split(Constants.SEPERATOR)[inputExcelFilePath.split(Constants.SEPERATOR).length - 1]);
            for (Book book : listBooks) {
                if (rowNum == 0) {
                    rowNum++;
                    createMainHeader(sheet, workbook, book);
                    continue;
                }
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(book.getNoFirst());
                cell = row.createCell(colNum++);
                cell.setCellValue(book.getTypeFirst());
                cell = row.createCell(colNum++);
                cell.setCellValue(book.getRevisionFirst());
                cell = row.createCell(colNum++);
                cell.setCellValue(book.getByFirst());
                cell = row.createCell(colNum++);
                cell.setCellValue(book.getNoSecond());
                cell = row.createCell(colNum++);
                cell.setCellValue(book.getTypeSecond());
                cell = row.createCell(colNum++);
                cell.setCellValue(book.getRevisionSecond());
                cell = row.createCell(colNum);
                cell.setCellValue(book.getBySecond());
            }
            FileOutputStream outputStream = new FileOutputStream(outputExcelFilePath);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Processed : " + inputExcelFilePath
                .split(Constants.SEPERATOR)[inputExcelFilePath.split(Constants.SEPERATOR).length - 1]);
    }

    private void setSecondHalfValues(List<Book> listBooks, Book aBook) {
        aBook.setNoSecond(aBook.getNoFirst());
        aBook.setTypeSecond(aBook.getTypeFirst());
        aBook.setRevisionSecond(aBook.getRevisionFirst());
        aBook.setBySecond(aBook.getByFirst());
        listBooks.add(aBook);
    }

    private String getSheetName(String excelFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        workbook.close();
        return firstSheet.getSheetName();
    }

    private void createMainHeader(XSSFSheet sheet, XSSFWorkbook workbook, Book book) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue(book.getNoFirst());
        header.createCell(1).setCellValue(book.getTypeFirst());
        header.createCell(2).setCellValue(book.getRevisionFirst());
        header.createCell(3).setCellValue(book.getByFirst());
        header.createCell(4).setCellValue(book.getNoSecond());
        header.createCell(5).setCellValue(book.getTypeSecond());
        header.createCell(6).setCellValue(book.getRevisionSecond());
        header.createCell(7).setCellValue(book.getBySecond());
        setHeaderStyle(workbook, header, sheet, IndexedColors.GREY_40_PERCENT.getIndex());
    }

    private void setHeaderStyle(XSSFWorkbook workbook, Row header, XSSFSheet sheet, short backgroundColor) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(backgroundColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.DARK_RED.getIndex());
        for (int i = 0; i < header.getLastCellNum(); i++) {
            if (header.getCell(i) != null) {
                header.getCell(i).setCellStyle(style);
                sheet.autoSizeColumn(i);
            }
        }
    }

}
