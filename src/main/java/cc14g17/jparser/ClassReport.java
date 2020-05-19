package cc14g17.jparser;

import java.util.Arrays;
import java.util.List;

public class ClassReport {

    private String packageName;
    private String className;
    private List<String> methodNames;

    ClassReport() {
    }

    ClassReport(String packageName, String className, List<String> methodNames) {
        this.packageName = packageName;
        this.className = className;
        this.methodNames = methodNames;
    }

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