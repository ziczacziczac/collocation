package com.hus.cis.collocation.test;

import com.hus.cis.collocation.io.DirectoryContents;
import com.hus.cis.collocation.io.ReadFile;
import vn.pipeline.Annotation;
import vn.pipeline.Sentence;
import vn.pipeline.VnCoreNLP;
import vn.pipeline.Word;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class VNTokenizer {
    public static void main(String[] args) throws IOException {

        // "wseg", "pos", "ner", and "parse" refer to as word segmentation, POS tagging, NER and dependency parsing, respectively.
        String[] annotators = {"wseg", "pos"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);
        String source = "C:\\Users\\ADMIN\\Desktop\\COLLOCATION\\nontokenized-split\\";
        String dest = "C:\\Users\\ADMIN\\Desktop\\COLLOCATION\\tokenized-split\\";
        ReadFile readFile = new ReadFile();
        WriteFile writeFile = new WriteFile();

        List<String> files = DirectoryContents.getFileTxt(source);

        for (String filePath : files) {
            System.out.println(filePath);
            readFile.open(filePath);
            ArrayList<String> lines = readFile.read();
            readFile.close();
            writeFile.open(dest + readFile.getFileName());
            lines.forEach(line -> {
                Annotation annotation = new Annotation(line);
                try {
                    pipeline.annotate(annotation);
                    StringBuilder l = new StringBuilder();
                    for (Word word : annotation.getWords()) {
                        l.append(word.getForm()).append("|").append(word.getPosTag()).append(" ");
                    }
                    writeFile.write(l.toString().trim() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(line);
                }
            });
            writeFile.close();
        }

    }
}
