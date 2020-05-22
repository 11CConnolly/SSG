package cc14g17.jparser;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Generates Test Suite based on report read and defect type chosen
 */
public class TestBuilder {

    /** Name of class from ClassReport*/
    private String className;
    /** Package of class from ClassReport*/
    private String packageName;
    /** All methods from ClassReport*/
    private List<String> allMethodNames;
    /** Only the good... and bad... methods from our class report*/
    private List<String> badAndGoodMethodNames;
    /** Instance we use for our JUnit test cases*/
    private String instanceName;
    /** Name of method used as our Oracle*/
    private String oracleFlag;

    /** name of whole test suite we want to build*/
    private String testSuiteName;

    /** Regex pattern to match methods with bad... or good...*/
    private final Pattern methodPatten = Pattern.compile("^bad[a-zA-Z0-9_]+|^good[a-zA-Z0-9)]+");
    /** Regex pattern to find methods starting with is... as these are our Oracle flags functions */
    private final Pattern oraclePattern = Pattern.compile("^is[a-zA-Z0-9_]+");

    /**
     * Constructor method for Test Builder
     */
    TestBuilder() {
    }

    /**
     * Reads a class Report passed in from main to then build a test and assign variables which will need to
     * be used in other methods in this class.
     *
     * @param classReport classReport of file to generate tests for. Must be built to certain specification
     */
    void readReport(ClassReport classReport) {
        this.className = classReport.getClassName();
        this.packageName = classReport.getPackageName();
        this.instanceName = classReport.getClassName().split("_")[0].toLowerCase();
        this.testSuiteName = classReport.getClassName() + "AUTOGEN_EXPLOIT_Test";
        this.allMethodNames = classReport.getMethodNames();

        // Put methods in their appropriate class after matching with regular expression
        List<String> relevantMethodNames = new ArrayList<>();
        for (String method : classReport.getMethodNames()) {
            if (methodPatten.matcher(method).matches()) {
                relevantMethodNames.add(method);
                System.out.println("Method that matches: " + method);
            }
            if (oraclePattern.matcher(method).matches()) {
                this.oracleFlag = method;
                System.out.println("Relevant Oracle method that matches regex is: " + method);
            }
        }
        this.badAndGoodMethodNames = relevantMethodNames;
    }


    /**
     * Builds test for certain class based on the class report previously passed in and writes to same package.
     * Outputted file may not be in same directory, however.
     *
     * NOTE: MUST REMOVE { } AROUND PROGRAM INITIALIZATION AT START FOR PROGRAM TO COMPILE SUCCESSFULLY
     *
     * @param defectType DefectType passed in by user used to choose which test data to use for generation.
     * @throws IOException Can throw IOException when outputting file if input is not built to required spec.
     */
    void buildTest(DefectType defectType) throws IOException {
        /* Build setUp method to reset program after each test has run */
        MethodSpec setUp = MethodSpec.methodBuilder("setUp")
                .addAnnotation(Before.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$L = new $L()",
                        instanceName,
                        className)
                .build();


        // Get chosen payload and build tests with test data
        ListParser listParser = new ListParser();
        List<MethodSpec> generatedTests;

        switch (defectType) {
            case INTEGER_ATTACK:
                generatedTests = generateIntegerTests(listParser.getIntegerPayloads());
                break;
            case INTEGER_VALIDATION:
                generatedTests = generateValidationTests(listParser.getIntegerPayloads());
                break;
            case STRING_PATH_TRAVERSAL:
                generatedTests = generatePathTraversalTests(listParser.getPathTraversalPayloads());
                break;
            case STRING_SQL_INJECTION:
                generatedTests = generateSQLTestsSingle(listParser.getSQLPayloads());
                break;
            case STRING_SQL_INJECTIONS:
                generatedTests = generateSQLTestsDouble(listParser.getSQLPayloads());
                break;
            default:
                System.out.println("Improper defect Type");
                throw new IllegalArgumentException();
        }
        generatedTests.forEach(System.out::println);


        /* Build test suite class and add our relevant methods */
        TypeSpec.Builder testCaseBuilder = TypeSpec.classBuilder(testSuiteName)
                .addModifiers(Modifier.PUBLIC)
                .addInitializerBlock(CodeBlock.builder()
                        .addStatement("private $L $L", className, instanceName)
                        .build())
                .addMethod(setUp);
                for (MethodSpec m : generatedTests) {
                    testCaseBuilder.addMethod(m);
                }

        // Setup java file to write out to
        JavaFile javaTestFile = JavaFile
                .builder(packageName, testCaseBuilder.build())
                .indent("    ") // Default indentation is 2 spaces so we set this to 4 spaces instead
                .build();


        // Write the java file out and print info
        String FILE_PATH_OUT = packageName;
        javaTestFile.writeTo(new File(FILE_PATH_OUT));
        System.out.println("[SUCCESS] Java file: " + testSuiteName + " written out to: " + FILE_PATH_OUT);
    }

    /**
     * Generates and passes back JavaPoet MethodSpecifications Junit tests of a single string parameter
     * testing for SQL Injection
     *
     * @param payloads SQLPayloads used for test method generation
     * @return List of methods testing for SQLInjections
     */
    private List<MethodSpec> generateSQLTestsSingle (List<String> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : badAndGoodMethodNames) {
            int count = 1;
            for (String payload : payloads) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($S)",
                                instanceName,
                                method,
                                payload)
                        .addStatement("$T.assertFalse($L.$L())",
                                Assert.class,
                                instanceName,
                                oracleFlag)
                        .build();
                genTests.add(testCase);
                count++;
            }
        }
        return genTests;
    }

    /**
     * Generates and passes back JavaPoet MethodSpecifications Junit tests of two string parameters
     * testing for SQL Injection
     *
     * @param payloads List of String of SQLPayloads used for test method generation
     * @return List of methods testing for SQLInjections
     */
    private List<MethodSpec> generateSQLTestsDouble (List<String> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : badAndGoodMethodNames) {
            int count = 1;
            for (String payload : payloads) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($S, $S)",
                                instanceName,
                                method,
                                payload,
                                payload)
                        .addStatement("$T.assertFalse($L.$L())",
                                Assert.class,
                                instanceName,
                                oracleFlag)
                        .build();
                genTests.add(testCase);
                count++;
            }
        }
        return genTests;
    }

    /**
     * Generates and passes back JavaPoet MethodSpecifications Junit tests of a single string parameter
     * testing for Path Traversal specifically for CWE22 in SECdefects
     *
     * @param payloads List of Strings of Path Traversal Attacks used for test method generation
     * @return List of methods testing for Path Attacks
     */
    private List<MethodSpec> generatePathTraversalTests (List<String> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : badAndGoodMethodNames) {
            int count = 1;
            for (String payload : payloads) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($S)",
                                instanceName,
                                method,
                                //ORACLE FOR CWE22 - CHANGE FOR FILE(S) YOU ARE TRYING TO FIND
                                payload + "passwords/passwd.txt")
                        .addStatement("$T.assertFalse($L.$L())",
                                Assert.class,
                                instanceName,
                                oracleFlag)
                        .build();
                genTests.add(testCase);
                count++;
            }
        }
        return genTests;
    }

    /**
     * Generates and passes back JavaPoet MethodSpecifications Junit tests of a single Integer parameter
     * testing for Integer Attacks
     *
     * @param payloads List of Integers for used for test method generation
     * @return List of methods testing for Integer Attacks
     */
    private List<MethodSpec> generateIntegerTests (List<Integer> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : badAndGoodMethodNames) {
            int count = 1;
            for (Integer payload : payloads) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($L)",
                                instanceName,
                                method,
                                payload)
                        .build();
                genTests.add(testCase);
                count++;
            }
        }
        return genTests;
    }


    /**
     * Generates and passes back JavaPoet MethodSpecifications Junit tests of a single Integer parameter
     * specifically for CWE20 of SECdefects
     *
     * @param payloads List of Integers for used for test method generation
     * @return List of methods testing for Integer Attacks
     */
    private List<MethodSpec> generateValidationTests (List<Integer> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : badAndGoodMethodNames) {
            int count = 1;
            for (Integer payload : payloads) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($L)",
                                instanceName,
                                method,
                                payload)
                        .addCode(validationOracleBlock(payload))
                        .build();
                genTests.add(testCase);
                count++;
            }
        }
        return genTests;
    }


    /**
     * Uses a custom Oracle to ensure code functions correctly and passes back relevant CodeBlock
     *
     * @param payload Integer payload for specific test
     * @return CodeBlock asserting correct function of program
     */
    private CodeBlock validationOracleBlock(Integer payload) {
        // Find relevant getter method
        String oracleMethod = "";
        for (String m : allMethodNames) {
            if (m.startsWith("get")) {
                oracleMethod = m;
            }
        }

        // Get the new balance I'm expecting
        if (payload <= 0 || payload > 200) {
            payload = 0;
        } else {
            payload = -payload;
        }

        // Build oracle assertion
        return CodeBlock.builder()
                .addStatement("$T.assertEquals($L,$L.$L(), 0.001)",
                        Assert.class,
                        payload,
                        instanceName,
                        oracleMethod)
                .build();
    }
}
