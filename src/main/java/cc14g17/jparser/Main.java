package cc14g17.jparser;

import java.io.IOException;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern FILE_EXT_IS_JAVA = Pattern.compile(".*\\.java$");

    private static final String[] FILE_TEST_PATHS = {
            "src/main/java/cc14g17/SECdefects/CWE20_Improper_Input_Validation.java",
            "src/main/java/cc14g17/SECdefects/CWE22_Path_Traversal.java",
            "src/main/java/cc14g17/SECdefects/CWE89_SQL_Injection.java",
            "src/main/java/cc14g17/SECdefects/CWE125_Out_of_Bounds_Read.java"
    };

    public static void main(String[] args) {

        // Do some validation on the given input i.e. check it's there
        if (args.length != 1 ) {
            System.out.println("[ERROR] Please provide only the java source code file path");
            return;
        }

        String file_path = args[0];

        // and check it's a .java file
        if (!FILE_EXT_IS_JAVA.matcher(file_path).matches()) {
            System.out.println("[ERROR] Please specify a java file, or append .java onto argument");
            return;
        }

        ClassAnalyzer classAnalyzer = new ClassAnalyzer();
        ClassReport classReport = new ClassReport();
        // Analyze the .java source code to give me a ClassReport to pass into the TestBuilder
        try
        {
            classReport = classAnalyzer.analyseClass(file_path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Begin building the class
        TestBuilder testBuilder = new TestBuilder();

        try
        {
            testBuilder.buildTest(classReport);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Finished running Main in main");
    }

}
