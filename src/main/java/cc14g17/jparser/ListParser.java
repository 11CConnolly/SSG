package cc14g17.jparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListParser {

    private final String FILE_PATH_BASE = "C:\\Users\\callu\\jparser\\src\\main\\resources\\lists\\";

    ListParser() {
    }

    public static void main(String[] args) {
        ListParser listParser = new ListParser();
        System.out.println("[LOG] ---Get SQL Payloads---");
        listParser.getSQLPayloads().forEach(System.out::println);
        System.out.println("[LOG] ---Get Path Payloads---");
        listParser.getPathTraversalPayloads().forEach(System.out::println);
        System.out.println("[LOG] ---Get Integer Payloads---");
        listParser.getIntegerPayloads().forEach(System.out::println);
    }

    public List<String> getSQLPayloads () {
        String file_path = FILE_PATH_BASE + "StringSQLIAttacks.csv";
        File file = new File(file_path);

        // Parse payloads as list of lists
        List<List<String>> payloads = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                payloads.add(Arrays.asList(vals));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Flatten payload to List and return
        return payloads.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<String> getPathTraversalPayloads() {
        String file_path = FILE_PATH_BASE + "StringPathAttacks.csv";
        File file = new File(file_path);

        // Parse payloads as list of lists
        List<List<String>> payloads = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                payloads.add(Arrays.asList(vals));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Flatten payload to List and return
        List<String> list = new ArrayList<>();
        for (List<String> payload : payloads) {
            list.addAll(payload);
        }
        return list;
    }

    public List<Integer> getIntegerPayloads() {
        String file_path = FILE_PATH_BASE + "IntegerAttacks.csv";
        File file = new File(file_path);

        // Parse payloads as list of lists
        List<List<String>> payloads = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                payloads.add(Arrays.asList(vals));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Flatten payload to List and return
        List<Integer> list = new ArrayList<>();
        for (List<String> payload : payloads) {
            for (String s : payload) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }
}