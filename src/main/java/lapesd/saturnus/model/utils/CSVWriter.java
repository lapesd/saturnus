package lapesd.saturnus.model.utils;

import java.io.FileWriter;

public class CSVWriter extends com.opencsv.CSVWriter {

    public CSVWriter(FileWriter writer, String head) {
        super(writer);
        writeNext(head.split(" "));
    }
}
