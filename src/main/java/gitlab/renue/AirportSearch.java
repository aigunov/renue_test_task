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
import java.util.List;
import java.util.concurrent.*;

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

        // Поиск по запросу (многопоточно)
        var results = new ArrayList<SearchResult>();
        ExecutorService executorService = Executors.newFixedThreadPool(4); // Пул из 4 потоков

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String request;
            List<Future<SearchResult>> futures = new ArrayList<>();

            while ((request = reader.readLine()) != null) {
                String finalRequest = request;
                futures.add(executorService.submit(() -> {
                    long searchStart = System.nanoTime();
                    var result = compressedTrie.search(finalRequest);
                    long searchTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - searchStart);
                    return new SearchResult(finalRequest, result, searchTime);
                }));
            }

            for (Future<SearchResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Ошибка получения результата: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла запросов: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }

        // Формирование JSON-отчета
        var formatter = new ResponseFormatter(initTime, results);
        var visitor = new JsonReportVisitor(outputFilePath, initTime);
        formatter.format(visitor);
        System.out.println("Поиск завершен.");
    }
}