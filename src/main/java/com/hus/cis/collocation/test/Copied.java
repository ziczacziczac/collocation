package com.hus.cis.collocation.test;

import com.hus.cis.collocation.io.ReadFile;

import java.io.*;
import java.util.ArrayList;

public class Copied {
    public static void main(String[] args) throws IOException {
        ReadFile rf = new ReadFile();
        rf.open("C:\\Users\\ADMIN\\Desktop\\COLLOCATION\\data_filter.csv");
        ArrayList<String> lines = rf.read();
        rf.close();
        String source = "C:\\Users\\ADMIN\\Desktop\\COLLOCATION\\nontokenized-split-raw\\";
        String dest = "C:\\Users\\ADMIN\\Desktop\\COLLOCATION\\nontokenized-split\\";
        for(int i = 1; i < lines.size(); i++) {
            String[] elems = lines.get(i).split("\\+");
            String fileName = elems[0].replaceAll("\"", "");
            fileName = fileName.replaceAll("\\.csv", "");
            String[] fileNameSplit = fileName.split("\\\\");
            fileName = fileNameSplit[fileNameSplit.length - 1];

            System.out.println(fileName);
            String fileSource = source + fileName;
            String fileDest = dest + fileName;

            copyFileUsingStream(new File(fileSource), new File(fileDest));
        }
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
