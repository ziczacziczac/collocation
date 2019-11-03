package com.hus.cis.collocation.test;

import com.hus.cis.collocation.io.DirectoryContents;
import com.hus.cis.collocation.io.ReadFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JointDictionary {

    public static void main(String[] args) {
        List<String> listFile = DirectoryContents.getFileTxt("C:\\Users\\ADMIN\\Documents\\collocation\\file_ngram");
//        List<String> part1 = listFile.subList(0, listFile.size() /3);
//        List<String> part2 = listFile.subList(listFile.size() / 3 + 1, listFile.size() * 2 /3);
//        List<String> part3 = listFile.subList(listFile.size() * 2 /3 + 1, listFile.size()) ;
        ConcurrentHashMap<String, Type> dictionary = buildDictionary("NLP_DATA\\tdyh.txt", "NLP_DATA\\tdvn_without_tdyh.txt");
//        System.out.println(part1.size());
//        System.out.println(part2.size());
//        System.out.println(part3.size());
        ArrayList<String> resultLines = new ArrayList<>();
        resultLines.add("file_path,tdvn,tdyh,unknown");
        AtomicInteger count = new AtomicInteger();
        Executors.newFixedThreadPool(4).submit(() -> {
            listFile.parallelStream()
                    .forEach(filePath -> {
//                        System.out.println(count.getAndIncrement());

                        HashMap<String, Integer> ngram = new HashMap<>();
                        ReadFile readFile = new ReadFile();
                        readFile.open(filePath);
                        ArrayList<String> lines = readFile.read();
                        for(int i = 1; i < lines.size(); i++) {
                            String[] elems = lines.get(i).split(",");
                            ngram.put(elems[0], Integer.parseInt(elems[1]));
                        }

                        AtomicInteger numberOfTdvnWords = new AtomicInteger();
                        AtomicInteger numberOfTdyhWords = new AtomicInteger();
                        AtomicInteger numberOfUnknownWords = new AtomicInteger();
                        ngram.forEach((key1, value) -> {
                            Type type = dictionary.get(key1);
                            if(type == null) {
                                numberOfUnknownWords.getAndIncrement();
                            } else if(type.equals(Type.MEDICAL_DIC)){
                                numberOfTdyhWords.getAndIncrement();
                            } else {
                                numberOfTdvnWords.getAndIncrement();
                            }
                        });
                        String resultLine = filePath + ",,,," + numberOfTdvnWords.get() + ",,,," + numberOfTdyhWords.get()
                                + ",,,," + numberOfUnknownWords.get();
                        resultLines.add(resultLine);
                        System.out.println(resultLines.size());
                    });
            System.out.println("Write result for part 1");
            WriteFile wf = new WriteFile();
            wf.open("C:\\Users\\ADMIN\\Desktop\\COLLOCATION\\output_count.csv");
            try {
                wf.writeLines(resultLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
            wf.close();
            System.exit(0);
        });





    }

//    private static void separate_dictionary(){
//        List<String> tdvn = buildDictionary("NLP_DATA\\tdvn.txt");
//        List<String> tdyh = buildDictionary("NLP_DATA\\tdyh.txt");
//
//        ArrayList<String> tdvn_without_tdyh = new ArrayList<>();
//        for(String word : tdvn) {
//            if(!tdyh.contains(word)) {
//                tdvn_without_tdyh.add(word);
//            } else {
//                System.out.println(word);
//            }
//        }
//        System.out.println(tdvn_without_tdyh.size());
//        System.out.println(tdvn.size());
//
//        WriteFile wf = new WriteFile();
//        wf.open("NLP_DATA\\tdvn_without_tdyh.txt");
//        wf.writeLines(tdvn_without_tdyh);
//        wf.close();
//    }

    private static ConcurrentHashMap<String, Type> buildDictionary(String medicalPath, String vietnameseDic){
        ReadFile rf = new ReadFile();
        rf.open(medicalPath);
        ArrayList<String> listMedicalDictionay = rf.read();
        rf.close();

        rf.open(vietnameseDic);
        ArrayList<String> listVietNameseDictionay = rf.read();
        rf.close();

        ConcurrentHashMap<String, Type> dict = new ConcurrentHashMap<String, Type>();
        for(String medical: listMedicalDictionay) {
            dict.put(medical, Type.MEDICAL_DIC);
        }

        for(String vietnamese: listVietNameseDictionay) {
            dict.put(vietnamese, Type.VIETNAMESE_DIC);
        }
        return dict;
    }

    enum Type {
        MEDICAL_DIC, VIETNAMESE_DIC
    }
}
