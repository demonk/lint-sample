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
import java.util.Iterator;
import java.util.List;

import lombok.ast.Annotation;
import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ConstructorInvocation;
import lombok.ast.Expression;
import lombok.ast.For;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.If;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.StrictListAccessor;
import lombok.ast.Try;


/**
 * Created by ligs on 8/8/16.
 */
public class Persist extends Detector implements Detector.JavaScanner {
    public static final Issue ISSUE = Issue.create("checkPersistsHelper",
            "type not compat",
            "you should put the correct type compat with the key",
            Category.MESSAGES,
            9,
            Severity.ERROR,
            new Implementation(Persist.class, Scope.JAVA_FILE_SCOPE));

    //java关键字////////////////////////////////////////////////////
    @Override
    public AstVisitor createJavaVisitor(@NonNull JavaContext javaContext) {
        //使用这个类里的方法进行处理
        return new ForwardingAstVisitor() {

            @Override
            public boolean visitTry(Try node) {
                //... 在这里对try语句做你需要的检查

                return super.visitTry(node);
            }

            @Override
            public boolean visitFor(For node) {
                //... 在这里对for语句做你需要的检查

                return super.visitFor(node);
            }

            @Override
            public boolean visitIf(If node) {
                //... 在这里对if语句做你需要的检查
                System.out.println("if===================");

                return super.visitIf(node);
            }

            public boolean visitConstructorInvocation(ConstructorInvocation node) {
                JavaParser.ResolvedNode resolvedType = javaContext.resolve(node.astTypeReference());
                JavaParser.ResolvedClass resolvedClass = (JavaParser.ResolvedClass) resolvedType;

                if (resolvedClass != null
                        && resolvedClass.isSubclassOf("android.os.Message", false)) {
                    javaContext.report(ISSUE,
                            node,
                            javaContext.getLocation(node),
                            "You should not call `new Message()` directly.");

                    return true;
                }

                return super.visitConstructorInvocation(node);
            }

            public boolean visitAnnotation(Annotation node) {
                String type = node.astAnnotationTypeReference().getTypeName();
                System.out.println("annotation=" + type);

                System.out.println("value=" + node.getValueValues());
                System.out.println("value2=" + node.astElements().first().astValue().toString());

                node.astElements().first().astValue().toString();
                Node parent = node.getParent();
                return super.visitAnnotation(node);
            }
        };
    }

    //时机：try,if ,for 以及创建新对象时（构建方法invoke时）
    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Arrays.asList(Try.class, For.class, ConstructorInvocation.class/*, Annotation.class/*, MethodInvocation.class*/);
    }
    //////////////////////////////////////////////////////


    // 特别方法检查////////////////////////////////////////////////////
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("put");
    }

    @Override
    public void visitMethod(JavaContext javaContext, AstVisitor astVisitor, MethodInvocation methodInvocation) {
        String methodName = methodInvocation.astName().astValue();
        JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) javaContext.resolve(methodInvocation);
        JavaParser.ResolvedClass clazz = method.getContainingClass();

        if (methodName.equals("put") && clazz.getName().equals("cn.demonk.lint.test.persist.PersistHelper")) {
            System.out.println("found put method");

            StrictListAccessor<Expression, MethodInvocation> params = methodInvocation.astArguments();

            Iterator<Expression> it = params.iterator();

            while (it.hasNext()) {
                Expression ex = it.next();
                JavaParser.ResolvedField field = (JavaParser.ResolvedField) javaContext.resolve(ex);
                Iterable iterable = field.getAnnotations();

                if (iterable != null) {
                    Iterator<JavaParser.ResolvedAnnotation> iter = iterable.iterator();

                    while (iter.hasNext()) {
                        JavaParser.ResolvedAnnotation anno = iter.next();

                        //得到声明
                        System.out.println(anno.getValue("classType"));


//                        SingleMemberAnnotation sma = (SingleMemberAnnotation)anno.getAnnotation();
//                        Object vpd = sma.getStructuralProperty(SingleMemberAnnotation.VALUE_PROPERTY);
//                        if (vpd instanceof StringLiteral) {
//                            StringLiteral sl = (StringLiteral)vpd;
//                            return new Region(sl.getStartPosition() + 1, sl.getLength());
//                        }
//                        if (vpd instanceof ASTNode) {
//                            ASTNode astNode = (ASTNode)vpd;
//                            return new Region(astNode.getStartPosition(),astNode.getLength());
//                        }

                        JavaParser.ResolvedClass ac = anno.getClassType();

                        Iterator<JavaParser.ResolvedMethod> acf = ac.getMethods("classType", false).iterator();

                        while (acf.hasNext()) {
                            JavaParser.ResolvedMethod acff = acf.next();
                            System.out.println("method=" + acff.getName());

                            JavaParser.TypeDescriptor acfftype = acff.getReturnType();

                            for (Node n : acfftype.getNode().getChildren()) {
                                System.out.println("n=" + n);
                            }

                            System.out.println("value=" + (acfftype.equals(String.class)));
                        }
//
//                        System.out.println("sign=" + anno.getSignature());
//                        System.out.println("type=" + anno.getType());
//                        System.out.println("name=" + anno.getName());
//                        System.out.println("name=" + anno.getValues());

                    }
                }
            }
        } else {
            super.visitMethod(javaContext, astVisitor, methodInvocation);
        }
    }
    //////////////////////////////////////////////////////

    // 特殊类的构建方法////////////////////////////////////////////////////
    @Override
    public List<String> getApplicableConstructorTypes() {
        return Arrays.asList("cn.demonk.lint.MainActivity", "android.os.Message");
    }

    //处理上述选出的方法
    @Override
    public void visitConstructor(JavaContext javaContext, AstVisitor astVisitor, ConstructorInvocation constructorInvocation, JavaParser.ResolvedMethod resolvedMethod) {
        System.out.println("===visitConstructor node = " + constructorInvocation
                + "\nlocation = " + javaContext.getLocation(constructorInvocation).getStart().getLine());
    }

    //////////////////////////////////////////////////////

    // 用途于资源引用 ////////////////////////////////////////////////////
    @Override
    public boolean appliesToResourceRefs() {
        return true;
    }

    @Override
    public void visitResourceReference(JavaContext javaContext, AstVisitor astVisitor, Node node, String s, String s1, boolean b) {

    }
    //////////////////////////////////////////////////////

    // 检查父class ////////////////////////////////////////////////////
    @Override
    public List<String> applicableSuperClasses() {
        return null;
    }

    @Override
    public void checkClass(JavaContext javaContext, ClassDeclaration classDeclaration, Node node, JavaParser.ResolvedClass resolvedClass) {

    }

}
