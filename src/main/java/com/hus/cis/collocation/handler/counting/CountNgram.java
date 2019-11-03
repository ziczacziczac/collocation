package com.hus.cis.collocation.handler.counting;

import com.hus.cis.collocation.SuperData;
import com.hus.cis.collocation.data.Document;
import com.hus.cis.collocation.handler.validator.StopWordValidator;
import com.hus.cis.collocation.handler.validator.WordValidator;
import com.hus.cis.collocation.io.DirectoryContents;
import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.io.ReadFile;
import com.hus.cis.collocation.io.WriteFile;
import com.hus.cis.collocation.utils.NgramUtils;
import vn.pipeline.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CountNgram {
    private String outputUnigram;
    private String outputBigram;
    private String outputTrigram;
    private String output4gram;
    private static CountUnigram one;
    private static CountBigram bigram;
    private static CountTriGram trigram;
    private static Count4gram fourgram;
    private static StopWordValidator stopWordsValidator;
    private static WordValidator check;
    private boolean isTokenized;
    private long numberOfSyllables = 0;
    private ArrayList<String> listpath;
    private String target = DirectorySavedResult.getDirectoryToSaveResult();
    static boolean isLoadFile = false;
    private ReadFile rf;

    public CountNgram() {
        check = new WordValidator();
        stopWordsValidator = new StopWordValidator();
    }

    public void setFileInput(String fileInput) {
        if (!isLoadFile) {
            listpath = DirectoryContents.getFileTxt(fileInput);
        }

    }

    public void resetLoadFile() {
        isLoadFile = false;
    }

    public void setType(boolean isTokenized) {
        target = DirectorySavedResult.getDirectoryToSaveResult();
        this.isTokenized = isTokenized;
        if (isTokenized) {
            target += "tokenized/";
        } else {
            target += "nontokenized/";
        }
        outputUnigram = target + "unigram.txt";
        outputBigram = target + "bigram.txt";
        outputTrigram = target + "trigram.txt";
        output4gram = target + "fourgram.txt";
    }

    public void countUnigram() throws IOException {

        one = new CountUnigram();

        for (String apath : listpath) {
            rf = new ReadFile();
            rf.open(apath);
            ArrayList<String> allLines = rf.read();
            rf.close();
            Document doc = new Document(apath, allLines);
            for (String aline : allLines) {
                List<Word> words = getListWordFromLine(aline);
                for (Word word : words) {
                    numberOfSyllables++;
                    if (check.checkWord(word.getForm())
                            && word.getPosTag().matches("np|nc|n|nu|nb|ny|e|a")
                            && !stopWordsValidator.isStopWord(word.getForm())) {
                        one.add(word.getForm(), doc);
                    }
                }
            }
        }
        System.out.println("Number of SyllablesValidator " + numberOfSyllables);
    }

    private void countBiGram(CountUnigram loadUnigram) throws IOException {
        bigram = new CountBigram();
        Hashtable<String, SuperData> unigram = loadUnigram.getOneCount();
        for (String apath : listpath) {
            rf = new ReadFile();
            rf.open(apath);
            ArrayList<String> allLines = rf.read();
            rf.close();
            Document doc = new Document(apath, allLines);
            for (String aline : allLines) {
                List<Word> words = getListWordFromLine(aline);
                int length = words.size();
                int i = 0;
                while (i + 1 < length) {
                    if (unigram.get(words.get(i).getForm()) != null
                            && unigram.get(words.get(i + 1).getForm()) != null
                            && words.get(i).getPosTag().matches("np|nc|n|nu|nb|ny")
                            && words.get(i + 1).getPosTag().matches("np|nc|n|nu|nb|ny|e|a")
                    ) {
                        bigram.add(words.get(i).getForm(), words.get(i+1).getForm(), doc);
                    }
                    i++;
                }
            }
        }
    }

    public void countTrigram(CountUnigram loadUnigram) throws IOException {
        trigram = new CountTriGram();
        Hashtable<String, SuperData> unigram = loadUnigram.getOneCount();
        for (String apath : listpath) {
            rf = new ReadFile();
            rf.open(apath);
            ArrayList<String> allLines = rf.read();
            rf.close();
            Document doc = new Document(apath, allLines);
            for (String aline : allLines) {
                List<Word> words = getListWordFromLine(aline);
                int length = words.size();
                int i = 0;

                while (i + 2 < length) {
                    Word firstWord = words.get(i);
                    Word secondWord = words.get(i + 1);
                    Word thirdWord = words.get(i + 2);
                    if (unigram.get(firstWord.getForm()) != null && unigram.get(secondWord.getForm()) != null
                            && unigram.get(thirdWord.getForm()) != null)
                    {
                        trigram.add(firstWord.getForm(), secondWord.getForm(), thirdWord.getForm(), doc);
                    }
                    i++;
                }
            }
        }
    }

    public static List<Word> getListWordFromLine(String aline){
        String[] elems = aline.split(" ");
        List<Word> words = new ArrayList<>();
        for(String elem: elems){
            String[] part = elem.split("\\|");
            if(part.length == 2) {
                Word word = new Word(-1, part[0], part[1]);
                words.add(word);
            }

        }
        return words;
    }
    public void count4gramV3_1(CountUnigram loadTriAsUnigram, CountUnigram loadUnigram) throws IOException {
        fourgram = new Count4gram();
        Hashtable<String, SuperData> hasTriAsUni = loadTriAsUnigram.getOneCount();
        Hashtable<String, SuperData> hasUni = loadUnigram.getOneCount();
        for (String apath : listpath) {
            rf = new ReadFile();
            rf.open(apath);
            ArrayList<String> allLines = rf.read();
            rf.close();
            Document doc = new Document(apath, allLines);
            for (String aline : allLines) {
                List<Word> words = getListWordFromLine(aline);
                int i = 0;
                while (i + 3 < words.size()) {
                    String tri = words.get(i).getForm() + " " + words.get(i + 1).getForm() + " " + words.get(i + 2).getForm();
                    String uni = words.get(i + 3).getForm();
                    if (hasTriAsUni.get(tri) != null
                            && hasUni.get(uni) != null
                    ) {
                        fourgram.add(tri, uni, doc);
                    }
                    i++;
                }
            }
        }
    }

    public void count4gramV2_2(CountUnigram loadBiAsUnigram) {
        fourgram = new Count4gram();
        Hashtable<String, SuperData> hashBiAsUni = loadBiAsUnigram.getOneCount();
        for (String apath : listpath) {
            rf = new ReadFile();
            rf.open(apath);
            ArrayList<String> allLines = rf.read();
            rf.close();
            Document doc = new Document(apath, allLines);
            for (String aline : allLines) {
                String[] tokens = aline.split(" ");
                int i = 0;
                while (i + 3 < tokens.length) {
                    String bi1 = tokens[i + 0] + " " + tokens[i + 1];
                    String bi2 = tokens[i + 2] + " " + tokens[i + 3];
                    i++;
                    if (hashBiAsUni.get(bi1) != null && hashBiAsUni.get(bi2) != null) {
                        fourgram.add(bi1, bi2, doc);
                    }
                }
            }
        }
    }

    public int[] getCount() {
        int[] counts = new int[3];
        counts[0] = one.getN();
        counts[1] = bigram.getN();
        return counts;
    }

    public void processUniBigram() throws IOException {
        countUnigram();
        WriteFile wf = new WriteFile();
        wf.open(outputUnigram);
        one.summary();
        wf.writeHashUnigram(one);
        wf.close();
        System.out.println("Unigram: complete!");

        NgramUtils load = new NgramUtils();
        load.setType(isTokenized);
        CountUnigram loadUnigram = load.loadUnigram();
        countBiGram(loadUnigram);
        bigram.summary();
        wf.open(outputBigram);
        wf.writeHashBigram(bigram);
        wf.close();
        System.out.println("Bigram: complete!");
    }

    public void processTrigram(CountUnigram loadUnigram) throws IOException {
        countTrigram(loadUnigram);
        trigram.sumary();
        WriteFile wf = new WriteFile();
        wf.open(outputTrigram);
        wf.writeHashTrigram(trigram);
        wf.close();
        System.out.println("Trigram: complete!");
    }

    public void process4gramV3_1(CountUnigram loadTriAsUnigram, CountUnigram loadUnigram) throws IOException {
        count4gramV3_1(loadTriAsUnigram, loadUnigram);
        WriteFile wf = new WriteFile();
        wf.open(output4gram);
        fourgram.sumary();
        wf.writeHash4gram(fourgram);
        wf.close();
        System.out.println("Fourgram: complete!");
    }

    public ArrayList<ArrayList<String>> getAllLine(ArrayList<String> listPath) {
        ArrayList<ArrayList<String>> contentFile = new ArrayList<>();
        ReadFile rf = new ReadFile();
        for (String apath : listPath) {
            rf.open(apath);
            ArrayList<String> allLinesInFile = rf.read();
            contentFile.add(allLinesInFile);
        }
        return contentFile;
    }

    public static void main(String[] args) {
        String aline = "đã|R phê_duyệt|V thuốc|N lynparza|V của|E công_ty|N dược_phẩm|N astrazeneca|N plc|N là|V loại|Nc thuốc|N đầu_tiên|A dành|V cho|E những|L bệnh_nhân|N bị|V";
        String bline = "";
        System.out.println(getListWordFromLine(bline).size());
    }
}
