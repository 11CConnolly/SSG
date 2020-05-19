package org.javaparser.samples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.io.File;
import java.util.regex.Pattern;

public class ModifyingVisitorStarter {

    private static final String FILE_PATH = "src/main/java/org/javaparser/samples/ReversePolishNotation.java";
    private static final Pattern LOOK_AHEAD_THREE = Pattern.compile("(\\d)(?=(\\d{3})+$)");

    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        ModifierVisitor<?> numericLiteralVisitor = new IntegerLiteralModifier();
        numericLiteralVisitor.visit(cu, null);

        System.out.println(cu.toString());

    }

    static String formatWithUnderscores(String value) {
        String withoutUnderscores = value.replaceAll("_", "");
        return LOOK_AHEAD_THREE.matcher(withoutUnderscores).replaceAll("$1_");
    }

    private static class IntegerLiteralModifier extends ModifierVisitor<Void> {

        @Override
        public FieldDeclaration visit(FieldDeclaration fd, Void arg) {
            super.visit(fd, arg);
            // iterate over fields in our field declaration
            fd.getVariables().forEach( v_->
                    v_.getInitializer().ifPresent(i -> {
                        if (i instanceof IntegerLiteralExpr) {
                            // set the string of the initial value to the result of the current value
                            v_.setInitializer(formatWithUnderscores(((IntegerLiteralExpr) i).getValue()));
                        }
                    }));
            return fd;
        }
    }

}
