package ru.sfedu.lereena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class CSVReader {

    public static String[][] CSVToArray(String path, String delim) {
        URL url = null;
        try {
            url = new URL(path);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File groupsIDs = new File(url.getFile());

        String[][] res = new String[28][27];

        BufferedReader bufRdr = null;
        try {
            bufRdr = new BufferedReader(new FileReader(groupsIDs));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        int row = 0;
        int col = 0;

        //read each line of text file
        try {
            while ((line = bufRdr.readLine()) != null && row < 28) {
                StringTokenizer st = new StringTokenizer(line, ",");
                while (st.hasMoreTokens()) {
                    //get next token and store it in the array
                    res[row][col] = st.nextToken();
                    col++;
                }
                col = 0;
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        String[][] test = CSVToArray("http://mihaxxx.16mb.com/vkgroupsids.csv", ";");
        for (int i = 0; i < 28; i++)
            for (int j = 0; j < 27; j++) {
                System.out.print(test[i][j] + " ");
            }
    }
}
