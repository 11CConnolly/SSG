package cc14g17.jparser;

import cc14g17.SECdefects.CWE22_Path_Traversal;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.squareup.javapoet.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestBuilder {

    private String className;
    private String packageName;
    private List<String> methodNames;
    private String instanceName;
    private String testclassName;

    // Regex pattern to match methods with bad... or good...
    private Pattern pattern = Pattern.compile("^bad[a-zA-Z0-9_]+|^good[a-zA-Z0-9)]+");

    TestBuilder() {

    }

    void readReport(ClassReport classReport) {
        this.className = classReport.getClassName();
        this.packageName = classReport.getPackageName();
        this.instanceName = classReport.getClassName().split("_")[0].toLowerCase();
        this.testclassName = classReport.getClassName() + "AUTOGEN__EXPLOIT_Test";

        List<String> relevantMethodNames = new ArrayList<>();
        for (String method : classReport.getMethodNames()) {
            if (pattern.matcher(method).matches()) {
                relevantMethodNames.add(method);
                System.out.println("Method that matches: " + method);
            }
        }
        this.methodNames = relevantMethodNames;
    }

    void buildTest() throws IOException {
        // Build setUp method to help with initialization
        MethodSpec setUp = MethodSpec.methodBuilder("setUp")
                .addAnnotation(Before.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$L = new $L()",
                        instanceName,
                        className)
                .build();

        ListParser listParser = new ListParser();

        // Build methods to test exploitation of defects
        List<MethodSpec> generatedTests = new ArrayList<>();
        for (String method : methodNames) {
            int count = 1;
            for (String payload : listParser.getPathTraversalPayloads()) {
                MethodSpec testCase = MethodSpec.methodBuilder(method + count)
                        .addAnnotation(Test.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L.$L($S)",
                                instanceName,
                                method,
                                payload + "passwords/passwd.txt")
                        .addStatement("$T.assertFalse($L.isFileRead())",
                                Assert.class,
                                instanceName) // + Oracle outputs the flag function
                        .build();
                generatedTests.add(testCase);
                count++;
            }
        }

        //Print out all the generated tests
        generatedTests.forEach(System.out::println);

        // Build test cases
        TypeSpec.Builder testCaseBuilder = TypeSpec.classBuilder(testclassName)
                .addModifiers(Modifier.PUBLIC)
                // FOR PROGRAM TO COMPILE WILL NEED TO TAKE BRACES SURROUNDING STATEMENT OUT
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

    private String generateNextTest() {

        return "";
    }

    /* @Deprecated effectively, don't need to use it but will keep it just in case
        TypeSpec junitTestSpec = TypeSpec.classBuilder(testclassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(setUp)
                .addMethod(basicTest)
                .build();
        */

}
