package cc14g17.jparser;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestBuilder {

    TestBuilder() {

    }

    void buildTest(ClassReport classReport) throws IOException {

        String testClassName = classReport.getClassName() + "AUTOGEN_Test";

        // Construct file to build
        TypeSpec junitTestSpec = TypeSpec.classBuilder(testClassName)
                .addModifiers(Modifier.PUBLIC)
                .build();

        // Setup java file to write out to
        JavaFile javaTestFile = JavaFile
                .builder(classReport.getPackageName(), junitTestSpec)
                .indent("    ") // Default indentation is 2 spaces so we set this to 4 spaces instead
                .build();

        // Write the java file out and print info
        String FILE_PATH_OUT = classReport.getPackageName();

        javaTestFile.writeTo(new File(FILE_PATH_OUT));

        System.out.println("[SUCCESS] Java file: " + testClassName + " written out to: " + FILE_PATH_OUT);
    }
}
