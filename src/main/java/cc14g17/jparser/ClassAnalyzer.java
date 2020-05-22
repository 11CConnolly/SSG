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

/**
 * Analyzes the AST of a given .java file using the JavaParser and relevant ethods
 */
public class ClassAnalyzer {

    ClassAnalyzer () {
    }

    /**
     * Parse and analyze the AST of a .java source code file
     *
     * @param file_path Input file that can either be relative or absolute
     * @return ClassReport of relevant information
     * @throws IOException Can be thrown as we are parsing the file
     */
    ClassReport analyseClass (String file_path) throws IOException {

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

        // Write out logging information
        System.out.println("[INFO] Writing output from classReport");
        System.out.println(classReport.toString());
        System.out.println("[INFO] Finished output from classReport");

        return classReport;
    }

    /**
     * Returns the package name after visiting the required nodes from the root
     */
    private static class PackageNameGetter extends GenericVisitorAdapter<String, Void> {

        @Override
        public String visit(PackageDeclaration pd, Void arg) {
            super.visit(pd, arg);
            return pd.getNameAsString();
        }
    }

    /**
     * Returns the class name after visiting the required nodes from the root
     */
    private static class ClassNameGetter extends GenericVisitorAdapter<String, Void> {

        @Override
        public String visit(ClassOrInterfaceDeclaration cd, Void arg) {
            super.visit(cd, arg);
            return cd.getNameAsString();
        }
    }

    /**
     * Returns a list of all methods in the class visiting nodes from the root
     */
    private static class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

        @Override
        public void visit(MethodDeclaration md, List<String> collector) {
            super.visit(md, collector);
            collector.add(md.getNameAsString());
        }
    }
}
