package org.javaparser.samples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.io.File;
import java.util.List;

public class CommentReporterStarter {

    private static final String FILE_PATH = "src/main/java/org/javaparser/samples/ReversePolishNotation.java";

    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        List<Comment> comments = cu.getAllContainedComments();

        comments.forEach(System.out::println);
    }
}
