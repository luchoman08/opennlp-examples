package com.truora.comprehend;

import com.opencsv.CSVWriter;
import java.io.InputStream;
import java.io.IOException;

import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.util.Span;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static String join(String[] in, String sep) {
        String result = "";
        for (String val : in) {
            result += val + sep;
        }

        return result;
    }

    public static String join(ArrayList<String> in, String sep) {
        String result = "";
        for (String val : in) {
            result += val + sep;
        }

        return result;
    }

    public static List<String> readFile() throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("./raw_phrases.txt"));

        List<String> lines = new LinkedList<>(); // create a new list
        String line = reader.readLine(); // read a line at a time
        while (line != null) {
            lines.add(line); // add the line to your list
            line = reader.readLine();
        }

        return lines;
    }

    public static ArrayList<String> fetchTextFromSpans(String[] raw, Span[] spans) {
        ArrayList<String> result = new ArrayList<>();

        for (Span span : spans) {
            String[] parts = Arrays.copyOfRange(raw, span.getStart(), span.getEnd());
            result.add(App.join(parts, " ").trim());
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        List<String> texts = App.readFile();

        Tokenizer tokenizer = WhitespaceTokenizer.INSTANCE;

        TokenNameFinderModel model;
        try (InputStream modelIn = new java.io.FileInputStream("./es-ner-person.bin")) {
            model = new TokenNameFinderModel(modelIn);
        }

        NameFinderME nameFinder = new NameFinderME(model);

        CSVWriter writer = new CSVWriter(new FileWriter("./output.csv"), '|', CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        writer.writeNext(new String[]{"text", "names"});
        
        for (String text : texts) {
            String[] tokens = tokenizer.tokenize(text);
            Span nameSpans[] = nameFinder.find(tokens);
            nameFinder.clearAdaptiveData();

            ArrayList<String> names = App.fetchTextFromSpans(tokens, nameSpans);

            writer.writeNext(new String[]{text, App.join(names, " ").trim()});
        }

    }
}
