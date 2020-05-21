package cc14g17.jparser;

import com.squareup.javapoet.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TestBuilder {

    private String className;
    private String packageName;
    private List<String> methodNames;
    private String instanceName;
    private String testclassName;
    private String oracleFlag = "";

    // Regex pattern to match methods with bad... or good...
    private Pattern methodPatten = Pattern.compile("^bad[a-zA-Z0-9_]+|^good[a-zA-Z0-9)]+");
    // Regex pattern to find methods starting with is... as these are our Oracle flags
    private Pattern oraclePattern = Pattern.compile("^is[a-zA-Z0-9_]+");

    TestBuilder() {

    }

    void readReport(ClassReport classReport) {
        this.className = classReport.getClassName();
        this.packageName = classReport.getPackageName();
        this.instanceName = classReport.getClassName().split("_")[0].toLowerCase();
        this.testclassName = classReport.getClassName() + "AUTOGEN_EXPLOIT_Test";

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
        this.methodNames = relevantMethodNames;
    }

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
            case STRING_SQL_INJECTION:
                generatedTests = generateSQLTests(listParser.getSQLPayloads());
                break;
            case STRING_PATH_TRAVERSAL:
                generatedTests = generatePathTraversalTests(listParser.getPathTraversalPayloads());
                break;
            case INTEGER_ATTACK:
                generatedTests = generateIntegerTests(listParser.getIntegerPayloads());
                break;
            default:
                System.out.println("Improper defect Type");
                throw new IllegalArgumentException();
        }

        // Check our list of generated tests and then print them out
        if (generatedTests == null) {
            System.out.println("No generated tests provided");
            return;
        }
        generatedTests.forEach(System.out::println);


        /* Build test suite class and add our relevant methods */
        TypeSpec.Builder testCaseBuilder = TypeSpec.classBuilder(testclassName)
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
        System.out.println("[SUCCESS] Java file: " + testclassName + " written out to: " + FILE_PATH_OUT);
    }

    private List<MethodSpec> generateSQLTests (List<String> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : methodNames) {
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

    private List<MethodSpec> generatePathTraversalTests (List<String> payloads) {
        List<MethodSpec> genTests = new ArrayList<>();
        for (String method : methodNames) {
            int count = 1;
            for (String payload : payloads) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($S)",
                                instanceName,
                                method,
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

    private List<MethodSpec> generateIntegerTests (List<Integer> payloads) {

        return null;
    }

}
