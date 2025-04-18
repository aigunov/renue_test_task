package gitlab.renue;

import gitlab.renue.logic.CompressedTrie;
import gitlab.renue.logic.CsvParser;
import gitlab.renue.report.JsonReportVisitor;
import gitlab.renue.report.ResponseFormatter;
import gitlab.renue.report.SearchResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AirportSearch {
    private static String dataFilePath = null;
    private static int indexedColumnId = 0;
    private static String inputFilePath = null;
    private static String outputFilePath = null;

    public static void main(String[] args) {
        //Парсинг аргументы строки вызова
        for (int i = 0; i < args.length; i++) {
            if ("--data".equals(args[i])) {
                dataFilePath = args[++i];
            } else if ("--indexed-column-id".equals(args[i])) {
                indexedColumnId = Integer.parseInt(args[++i]);
            } else if ("--input-file".equals(args[i])) {
                inputFilePath = args[++i];
            } else if ("--output-file".equals(args[i])) {
                outputFilePath = args[++i];
            }
        }
        if (dataFilePath == null || indexedColumnId <= 0 || indexedColumnId > 14 ||
                inputFilePath == null || outputFilePath == null) {
            System.out.println("Неверные входные данные");
            return;
        }

        search();
    }

    private static void search() {
        // Формирование дерева
        CompressedTrie compressedTrie = new CompressedTrie();
        CsvParser parser = new CsvParser();

        long startTime = System.currentTimeMillis();
        parser.parseCsv(dataFilePath, compressedTrie, indexedColumnId);
        long initTime = System.currentTimeMillis() - startTime;

        // Поиск по запросу
        var results = new ArrayList<SearchResult>();
        try (var reader = new BufferedReader(new FileReader(inputFilePath))) {
            String request;
            while ((request = reader.readLine()) != null) {
                long searchStart = System.currentTimeMillis();
                var result = compressedTrie.search(request);
                long searchTime = System.currentTimeMillis() - searchStart;
                results.add(new SearchResult(request, result, searchTime));
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла запросов: " + e.getMessage());
            return;
        }

        // Формирование JSON-отчета
        var formatter = new ResponseFormatter(initTime, results);
        var visitor = new JsonReportVisitor(outputFilePath, initTime);
        formatter.format(visitor);
        System.out.println("Поиск завершен.");
    }
}