package com.spicejet.signoff;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Launcher {

    public static void main(String[] args) {
        ExcelReaderUtil reader = new ExcelReaderUtil();
        List<Book> listBooks;
        Map<String, Book> mapBooks;
        try {

            File directory = new File(Constants.INPUT_DIR);
            File[] fList = directory.listFiles();
            if ((fList != null) && (fList.length > 0)) {
                for (File file : fList) {
                    if (file.getName().endsWith(".xlsx")) {
                        String inputExcelFilePath = Constants.INPUT_DIR + Constants.SEPERATOR + file.getName();
                        String outputExcelFilePath = Constants.OUTPUT_DIR + Constants.SEPERATOR + file.getName();
                        mapBooks = reader.readBooksFromExcelFile(inputExcelFilePath);
                        listBooks = reader.processBooks(mapBooks);
                        reader.writeBooksFromExcelFile(inputExcelFilePath, outputExcelFilePath, listBooks);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
