package com.hus.cis.collocation.test;

import com.hus.cis.collocation.SuperData;
import com.hus.cis.collocation.data.Candidate;
import com.hus.cis.collocation.handler.counting.Count4gram;
import com.hus.cis.collocation.handler.counting.CountBigram;
import com.hus.cis.collocation.handler.counting.CountTriGram;
import com.hus.cis.collocation.handler.counting.CountUnigram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Do Quang Dat k59 HUS_CIS Create a BufferWriter for write result
 */
public class WriteFile {
    private BufferedWriter bw;

    public WriteFile() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Open a bufferwriter to write output
     *
     * @param fileName : path of file output
     */
    public void open(String fileName) {
        try {
            bw = new BufferedWriter(new FileWriter(new File(fileName)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLines(ArrayList<String> lines) throws IOException {
        int i = 0;
        for (String aline : lines) {
            System.out.println(i ++);
            bw.write(aline + "\n");

        }

    }

    public void writeCandidates(ArrayList<Candidate> cands, long totalFrequency) {
        String result = totalFrequency + "\n";
        for (int i = 0; i < cands.size(); i++) {
            result += cands.get(i).toString() + "\n";
        }
        try {
            bw.write(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void writeHashUnigram(CountUnigram unigram) {
        Hashtable<String, SuperData> oneCount = unigram.getOneCount();
        int n = unigram.getN();
        Enumeration<String> e = oneCount.keys();
        long sum = unigram.getSum();
        try {
            bw.write(n + "\n" + (sum / n) + "\n");
            while (e.hasMoreElements()) {
                String temp = e.nextElement();
                SuperData sd = oneCount.get(temp);
                bw.write(temp + " " + sd.toString() + "\n");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void writeHashBigram(CountBigram bigram) {

        Hashtable<String, Hashtable<String, SuperData>> bigramInHash = bigram.getBigram();
        Enumeration<String> e = bigramInHash.keys();
        long sum = bigram.getSum();
        int n = bigram.getN();
        try {
            bw.write(n + "\n" + (sum / n) + "\n");
            while (e.hasMoreElements()) {
                String temp = e.nextElement();
                Enumeration<String> e2 = bigramInHash.get(temp).keys();
                while (e2.hasMoreElements()) {
                    String tempValue = e2.nextElement();
                    SuperData sd = bigramInHash.get(temp).get(tempValue);
                    bw.write(temp + " " + tempValue + " " + sd.toString() + "\n");
                }
                if (bigramInHash.get(temp).isEmpty()) {
                    bigramInHash.remove(temp);
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void writeHashTrigram(CountTriGram trigram) {
        Hashtable<String, Hashtable<String, Hashtable<String, SuperData>>> hasTrigram = trigram.getTrigramCount();
        int n = trigram.getNumberTrigrams();
        long sum = trigram.getSum();
        try {
            bw.write(n + "\n" + sum / n + "\n");
            Enumeration<String> firstKeys = hasTrigram.keys();
            while (firstKeys.hasMoreElements()) {
                String firstWord = firstKeys.nextElement();
                Enumeration<String> secondKeys = hasTrigram.get(firstWord).keys();
                while (secondKeys.hasMoreElements()) {
                    String secondWord = secondKeys.nextElement();
                    Enumeration<String> thirdKeys = hasTrigram.get(firstWord).get(secondWord).keys();
                    while (thirdKeys.hasMoreElements()) {
                        String thirdWord = thirdKeys.nextElement();
                        SuperData sd = hasTrigram.get(firstWord).get(secondWord).get(thirdWord);
                        int count = sd.getNumberOccurrence();
                        bw.write(firstWord + " " + secondWord + " " + thirdWord + " " + sd.toString() + "\n");
                        sum += count;
                    }
                }
                if (hasTrigram.get(firstWord).isEmpty()) {
                    hasTrigram.remove(firstWord);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeHash4gram(Count4gram fourgram) {
        Hashtable<String, Hashtable<String, SuperData>> hashFourgram = fourgram.get4gram();
        Enumeration<String> e = hashFourgram.keys();
        int n = fourgram.getN();
        long sum = fourgram.getSum();
        try {
            bw.write(n + "\n" + sum / n + "\n");
            while (e.hasMoreElements()) {
                String temp = e.nextElement();
                Enumeration<String> e2 = hashFourgram.get(temp).keys();
                while (e2.hasMoreElements()) {
                    String tempValue = e2.nextElement();
                    SuperData sd = hashFourgram.get(temp).get(tempValue);
                    bw.write(temp + " " + tempValue + " " + sd.toString() + "\n");
                }
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    public void write(String string) {
        try {
            bw.write(string);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isNull() {
        return bw == null;
    }

}
