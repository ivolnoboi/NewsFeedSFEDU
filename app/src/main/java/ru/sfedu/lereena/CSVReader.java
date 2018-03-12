package ru.sfedu.lereena;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    InputStream inputStream;

    public CSVReader(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List read(String delim) {
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(delim);
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }

    public static void main(String[] args) {
        /*String[][] test = CSVToArray("http://mihaxxx.16mb.com/vkgroupsids.csv", ";");
        for (int i = 0; i < 28; i++)
            for (int j = 0; j < 27; j++) {
                System.out.print(test[i][j] + " ");
            }*/
    }
}
