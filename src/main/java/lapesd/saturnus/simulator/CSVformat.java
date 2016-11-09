package lapesd.saturnus.simulator;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVformat {

    private static BufferedWriter file;

    public static void openFileToWrite(String fileName, String head) {
        try {
            file = new BufferedWriter(new FileWriter(fileName));
            file.write(head);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLine(String line) {
        try {
            file.write("\n" + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
