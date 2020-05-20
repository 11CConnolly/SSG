package cc14g17.jparser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListParser {

    ListParser() {
    }

    // Main method for testing purposes
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
        String file_path = "src\\main\\resources\\lists\\StringSQLIAttacks.csv";
        File file = new File(file_path);

        // Parse payloads as list of lists
        List<List<String>> payloads = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                payloads.add(Arrays.asList(vals));
            }
        } catch (IOException e) {
            System.out.println("Error when reading " + file_path);
            e.printStackTrace();
        }

        // Flatten payload to List and return
        return payloads.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<String> getPathTraversalPayloads() {
        String file_path = "src\\main\\resources\\lists\\StringPathAttacks.csv";
        File file = new File(file_path);

        // Parse payloads as list of lists
        List<List<String>> payloads = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                payloads.add(Arrays.asList(vals));
            }
        } catch (IOException e) {
            System.out.println("Error when reading " + file_path);
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
        String file_path = "src\\main\\resources\\lists\\IntegerAttacks.csv";
        File file = new File(file_path);

        // Parse payloads as list of lists
        List<List<String>> payloads = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                payloads.add(Arrays.asList(vals));
            }
        } catch (IOException e) {
            System.out.println("Error when reading " + file_path);
            e.printStackTrace();
        }

        // Flatten payload to List after parsing the Integer value and returning
        List<Integer> list = new ArrayList<>();
        for (List<String> payload : payloads) {
            for (String s : payload) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }
}