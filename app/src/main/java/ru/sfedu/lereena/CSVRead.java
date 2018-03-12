package ru.sfedu.lereena;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CSVRead {

    private void initializeURLsArr() {

        String[][] urls = new String[28][27];

        CSVReader reader = null;
        try {
            reader = new CSVReader(
                    new InputStreamReader(new FileInputStream("/res/raw/vkgroupsids.txt"), "UTF-8"),
                    ',', '\'', 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] line;
        try {
            for (int i = 0; i < 28; i++) {
                while ((line = reader.readNext()) != null) {
                    urls[i] = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[][] test;
        File file = new File("C:/Users/Lekker/Documents/GitHub/NewsFeedSFEDU/app/src/main/res/raw/vkgroupsids.txt");
        //String[][] dd = CSVToArray("C:/Users/Lekker/Documents/GitHub/NewsFeedSFEDU/app/src/main/res/raw/vkgroupsids.txt");
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 27; j++) {
         //       System.out.print(dd[i][j] + " ");
            }
            System.out.println();
        }
    }
}
