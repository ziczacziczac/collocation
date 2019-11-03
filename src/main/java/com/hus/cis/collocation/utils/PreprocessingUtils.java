package com.hus.cis.collocation.utils;

import com.hus.cis.collocation.io.DirectoryContents;
import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.io.ReadFile;
import com.hus.cis.collocation.io.WriteFile;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;

public class PreprocessingUtils {
    private ArrayList<String> specialCharacters = new ArrayList<>();
    private ReadFile rf;
    private WriteFile wf;
    private String[] markOld = {"òa", "óa", "ỏa", "õa", "ọa",
            "òe", "óe", "ỏe", "õe", "ọe",
            "ùy", "úy", "ủy", "ũy", "ụy"};
    private String[] markNew = {"oà", "oá", "oả", "oã", "oạ",
            "oè", "oé", "oẻ", "oẽ", "oẹ",
            "uỳ", "uý", "uỷ", "uỹ", "uỵ"};

    public void readSpecialCharacter() { // doc file chua cac dau Tieng Viet,

        rf = new ReadFile();
        String specialCharacterFile = "NLP_DATA/charAndSyllables/Character.txt";
        rf.open(specialCharacterFile);
        specialCharacters = rf.read();
    }

    private String remark(String line) {
        for (int i = 0; i < markNew.length; i++) {
            line = line.replaceAll(markOld[i], markNew[i]);
        }
        return line;
    }

    public PreprocessingUtils() {
        readSpecialCharacter();
        //System.out.println(dau.size());
    }

    // tra ve 1 ArrayList cac cau trong 1 dong
    private ArrayList<String> splitLine(String line) { // line dau vao can
        // duoc fix voi
        // truong hop line
        // == null;
        line = remark(line);
        ArrayList<String> listString = new ArrayList<>();
        line = line.trim();
        line = line.replaceAll("//s+", " "); // dinh dang lai 1 dong
        String element = "";
        String s[] = line.split(" "); // tach
//		for (String string : s) {
//			if (!dau.containsKey(string)) { // neu ko phai la dau thi
//													// them vao element
//				element += string + " ";
//			} else { // gap 1 stopWord
//				if (element.length() != 0) {
//					listString.add(element); // them vao array
//				}
//				element = "";
//			}
//		}

        ArrayList<String> z = new ArrayList<>();
        z.add(line);
        for (String specialCharacter : specialCharacters) {
            ArrayList<String> partToRemoves = new ArrayList<>();
            ArrayList<String> partToAdds = new ArrayList<>();
            for (String part : z) {
                String[] partSplited = part.split(specialCharacter);
                if (partSplited.length > 1
                    || partSplited.length == 1 && !partSplited[0].equals(part)
                ) {
                    partToRemoves.add(part);
                    for (String newPart : partSplited) {
                        if(newPart.length() > 0)
                            partToAdds.add(newPart);
                    }
                }
            }
            z.removeAll(partToRemoves);
            z.addAll(partToAdds);
        }


//		if (element.length() != 0)
//			listString.add(element);
//		if (listString.size() == 0) { // them khi dong chi chua 1 thanh phan
//			listString.add(element);
//		}
        return z;
    }

    public void print(ArrayList<String> list) {
        System.out.println("Co " + list.size() + " thanh phan: ");
        for (String string : list) {
            System.out.println(string);
        }
    }

    public void splitLineInFile(String fileNameInput, String fileNameOutPut) {
        rf = new ReadFile();
        rf.open(fileNameInput);
        ArrayList<String> lines = rf.read();
        rf.close();

        wf = new WriteFile();
        wf.open(fileNameOutPut);
        for (String string : lines) {
            string = string.trim();
            if (string.equals(" "))
                string = "";
            if (string.trim().length() != 0 && string.trim().length() != 1) {
                ArrayList<String> lineChild = splitLine(string.trim());

                wf.writeLines(lineChild);
            }
        }
        wf.close();
    }

    private String target = DirectorySavedResult.getDirectoryToSaveResult();
    private int isTokenize;

    public void setTypeCorpus(int isTokenize) {
        this.isTokenize = isTokenize;
    }

    public void setOuput() {
        if (isTokenize == 0) {
            target += "tokenized-split";
        } else {
            target += "nontokenized-split";
        }
    }

    public void split(ArrayList<String> pathname) {
        setOuput();
        Scheduler scheduler = Schedulers.fromExecutor(Executors.newScheduledThreadPool(2));
        Flux.fromIterable(pathname)
                .doOnNext(path -> {
                    splitLineInFile(path, target + "/" + path.replace('\\', '_').replace(':', '_'));
                    System.out.println(Thread.currentThread().getId());
                })
                .subscribeOn(scheduler).blockLast();
    }

    public void process(String fileInput) {

        ArrayList<String> listPath = DirectoryContents.getFileTxt(fileInput);
        split(listPath);
        System.out.println("Split ....... Ok");
    }

    public static void main(String[] args) {
        PreprocessingUtils preprocessingUtils = new PreprocessingUtils();
        System.out.println(preprocessingUtils.splitLine("hay được biết với tên “ gen jolie ”").size());
//
//		String t = "asdj:";
//		String regrex = "\\.\\.";
//		System.out.println(t.split(regrex).length);
    }
}