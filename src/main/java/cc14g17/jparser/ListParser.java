package cc14g17.jparser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to Parse Payload Lists from under the src/main/resources/lists and provide lists as
 * test data for generation
 */
public class ListParser {

    private List<String> SQLPayloads;
    private List<String> pathPayloads;
    private List<Integer> integerPayloads;

    ListParser() {
        parseAllPayloads();
    }

    /**
     * Parse all payloads and assign to local variable
     */
    private void parseAllPayloads() {
        SQLPayloads = parseSQLPayloads();
        pathPayloads = parsePathPayloads();
        integerPayloads = parseIntegerPayloads();
    }

    /**
     * Parses a list of common SQL Injection attacks from StringSQLIAttacks.csv to use as test data
     *
     * @return List of Strings - SQL Injection Payload Strings
     */
    private List<String> parseSQLPayloads() {
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
        List<String> list = new ArrayList<>();
        for (List<String> payload : payloads) {
            list.addAll(payload);
        }
        return list;
    }

    /**
     * Parses a list of common Path Traversal attacks from StringPathAttacks.csv to use as test data
     *
     * @return List of Strings - Path Traversal Payload Strings
     */
    private List<String> parsePathPayloads() {
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

        // Flatten payloads from List of Lists to List and return
        List<String> list = new ArrayList<>();
        for (List<String> payload : payloads) {
            list.addAll(payload);
        }
        return list;
    }

    /**
     * Parses a list of common integers used for attack from IntegerAttacks.csv to use as test data
     *
     * @return List of Integers - Integers for Integer Overflow or Attack
     */
    private List<Integer> parseIntegerPayloads() {
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

    /**
     * Simple getter for flat list of SQL Injection strings
     *
     * @return List of Strings for SQL Injection
     */
    public List<String> getSQLPayloads () {
        return SQLPayloads;
    }

    /**
     * Simple getter for flat list of Path Traversal Strings
     *
     * @return List of Strings for Path Traversal Attack
     */
    public List<String> getPathTraversalPayloads() {
        return pathPayloads;
    }

    /**
     * Simple getter for flat list of Integers for an Integer attack
     *
     * @return List of Integers for integer attack
     */
    public List<Integer> getIntegerPayloads() {
        return integerPayloads;
    }
}