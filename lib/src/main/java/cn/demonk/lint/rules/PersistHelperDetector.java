package cn.demonk.lint.rules;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.Arrays;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ConstructorInvocation;
import lombok.ast.For;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.If;
import lombok.ast.Node;
import lombok.ast.Try;

/**
 * Created by ligs on 8/11/16.
 */
public class PersistHelperDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE = Issue.create("PersistHelperDetector",
            "simple note",
            "full note",
            Category.SECURITY,
            10,
            Severity.FATAL,
            new Implementation(PersistHelperDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Arrays.asList(Try.class, If.class, For.class, ConstructorInvocation.class/*, Annotation.class/*, MethodInvocation.class*/);
    }

    public AstVisitor createJavaVisitor(@NonNull JavaContext javaContext) {
        //使用这个类里的方法进行处理
        return new ForwardingAstVisitor() {

            public boolean visitNode(Node node) {
//                int i = 0;
//                for (Node c : node.getChildren()) {
//                    System.out.println((i++) + ":node=" + c.toString());
//                }
                return false;
            }

            @Override
            public boolean visitTry(Try node) {
                //... 在这里对try语句做你需要的检查
//                System.out.println("try");

                return super.visitTry(node);
            }

            @Override
            public boolean visitFor(For node) {
                Node body = node.getChildren().get(node.getChildren().size() - 1);
                String content = body.toString().replaceAll("\n", "");
                if (!(content.startsWith("{") && content.endsWith("}"))) {
                    javaContext.report(ISSUE, javaContext.getLocation(node), "for should wrap with {}");
                }

                return super.visitFor(node);
            }

            @Override
            public boolean visitIf(If node) {
                //... 在这里对if语句做你需要的检查
//                System.out.println("if");

                return super.visitIf(node);
            }

            public boolean visitConstructorInvocation(ConstructorInvocation node) {
                //.... 针对对象创建时的检查
//                System.out.println("constructor");

                return super.visitConstructorInvocation(node);
            }
        };
    }
}
