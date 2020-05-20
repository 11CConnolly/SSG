package cc14g17.jparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassAnalyzer {

    ClassAnalyzer () {
    }

    ClassReport analyseClass (String file_path) throws IOException {

        // FilePath can be in following format
        // "src/main/java/cc14g17/SECdefects/CWE20_Improper_Input_Validation.java"
        // or as an Absolute reference

        // Parse root node of java file
        CompilationUnit cu = StaticJavaParser.parse(new File(file_path));

        // Package name
        GenericVisitorAdapter<String, Void> packageNameGetter = new PackageNameGetter();
        String packageName = packageNameGetter.visit(cu, null);

        // Class name
        GenericVisitorAdapter<String, Void> classNameGetter = new ClassNameGetter();
        String className = classNameGetter.visit(cu, null);

        // Method names
        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        methodNameCollector.visit(cu, methodNames);

        // Add them to a ClassReport
        ClassReport classReport = new ClassReport(
                packageName,
                className,
                methodNames
        );

        System.out.println("[INFO] Writing output from classReport");
        System.out.println(classReport.toString());
        System.out.println("[INFO] Finished output from classReport");

        return classReport;
    }

    private static class PackageNameGetter extends GenericVisitorAdapter<String, Void> {

        @Override
        public String visit(PackageDeclaration pd, Void arg) {
            super.visit(pd, arg);
            return pd.getNameAsString();
        }
    }

    private static class ClassNameGetter extends GenericVisitorAdapter<String, Void> {

        @Override
        public String visit(ClassOrInterfaceDeclaration cd, Void arg) {
            super.visit(cd, arg);
            return cd.getNameAsString();
        }
    }

    private static class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

        @Override
        public void visit(MethodDeclaration md, List<String> collector) {
            super.visit(md, collector);
            collector.add(md.getNameAsString());
        }
    }
}
