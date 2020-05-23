package cc14g17.jparser;


import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Main class to allow interaction with command line and control program flow to read reports, parse
 * the appropriate lists and finally build the desired test suite
 *
 * @author Callum Connolly
 */
public class Main {

    /**
     *
     */
    private static final Pattern FILE_EXT_IS_JAVA = Pattern.compile(".*\\.java$");

    /**
     * Main method to take input from command line i.e. .java file and DefectType choice. Provides help with -h.
     *
     * @param args input from the user
     */
    public static void main(String[] args) {
        // Show defect types including string
        if (args[0].equals("-h")) {
            System.out.println("[ERROR] Please provide source code file including .java extension and defect type. Defect types include:");
            Arrays.asList(DefectType.values()).forEach(System.out::println);
            return;
        }
        // Simple validation check on number of arguments provided
        if (args.length != 2 ) {
            System.out.println("[ERROR] Please provide only the java source code file path and defect. Use -h for help");
            throw new IllegalArgumentException();
        }


        // Get necessary files including java source file and DefectType
        // Validate source code from input
        String file_path = args[0];
        if (!FILE_EXT_IS_JAVA.matcher(file_path).matches()) {
            System.out.println("[ERROR] Please specify a java file, or append .java onto argument");
            throw new IllegalArgumentException();
        }
        // Validate defectType from input
        DefectType defectType;
        try{
            defectType = DefectType.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Please provide an appropriate defect type. Defect types include: ");
            Arrays.asList(DefectType.values()).forEach(System.out::println);
            return;
        }


        // Analyze the .java source code to give me a ClassReport to pass into the TestBuilder
        ClassAnalyzer classAnalyzer = new ClassAnalyzer();
        ClassReport classReport = new ClassReport();
        try
        {
            classReport = classAnalyzer.analyseClass(file_path);
        }
        catch (IOException e)
        {
            System.out.println("[ERROR] Error when analyzing java file - does the file compile?");
            e.printStackTrace();
        }


        // Begin building the test suite, passing in the defect type and class report
        TestBuilder testBuilder = new TestBuilder();
        testBuilder.readReport(classReport);
        try
        {
            testBuilder.buildTest(defectType);
        }
        catch (IOException e)
        {
            System.out.println("[ERROR] Error when building test file");
            e.printStackTrace();
        }


        System.out.println("[INFO] FINISHED RUNNING PROGRAM MAIN IN MAIN");
    }

}
