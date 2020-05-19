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
        TypeSpec junitTestSpec = TypeSpec.classBuilder(classReport.getClassName() + "AUTOGEN_Test")
                .addModifiers(Modifier.PUBLIC)
                .build();


        // Setup java file to write out to
        JavaFile javaTestFile = JavaFile
                .builder(classReport.getPackageName(), junitTestSpec)
                .indent("    ")
                .build();

        String FILE_PATH_OUT = classReport.getPackageName();

        javaTestFile.writeTo(new File(FILE_PATH_OUT));
    }
}
