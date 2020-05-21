package cc14g17.jparser;


import java.io.IOException;
import java.util.Arrays;
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

        if (args.length != 2 ) {
            System.out.println("[ERROR] Please provide only the java source code file path and defect. Use -h for help");
            throw new IllegalArgumentException();
        }

        // Show defect types including string
        if (args[0].equals("-h")) {
            System.out.println("[INFO] Please provide source code file including .java extension and defect type. Defect types include:");
            Arrays.asList(DefectType.values()).forEach(System.out::println);
            return;
        }

        // Get necessary values from supplied arguments
        // Get the file path
        String file_path = args[0];

        // and check it's a .java file
        if (!FILE_EXT_IS_JAVA.matcher(file_path).matches()) {
            System.out.println("[ERROR] Please specify a java file, or append .java onto argument");
            throw new IllegalArgumentException();
        }

        // Get the defect choice
        DefectType defectType;
        try
        {
            defectType = DefectType.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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
            System.out.println("[INFO] Error when analyzing java file - does the file compile?");
            e.printStackTrace();
        }


        // Begin building the test class
        TestBuilder testBuilder = new TestBuilder();
        testBuilder.readReport(classReport);

        try
        {
            testBuilder.buildTest(defectType);
        }
        catch (IOException e)
        {
            System.out.println("[INFO] Error when building test file");
            e.printStackTrace();
        }

        System.out.println("Finished running Main in main");
    }

}
