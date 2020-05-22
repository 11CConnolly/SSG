package cc14g17.jparser;

import java.util.Arrays;
import java.util.List;

/**
 * Abstraction of inputted java file created by parsing the relevant information and used for building test suites.
 */
public class ClassReport {

    private String packageName;
    private String className;
    private List<String> methodNames;

    ClassReport() {
    }

    /**
     * @param packageName
     * @param className
     * @param methodNames
     */
    ClassReport(String packageName, String className, List<String> methodNames) {
        this.packageName = packageName;
        this.className = className;
        this.methodNames = methodNames;
    }

    /**
     * @return String of class report information
     */
    @Override
    public String toString() {
        return packageName + "|" + className + "|" + Arrays.toString(methodNames.toArray());
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getMethodNames() {
        return methodNames;
    }
}