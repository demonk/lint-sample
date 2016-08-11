package cn.demonk.lint.rules;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ligs on 8/11/16.
 */
public class LogDetector extends Detector implements Detector.ClassScanner {

    public static final Issue ISSUE = Issue.create("LogChecker",
            "Please use LogUtil",
            "You shouldn't call Log directly,please use LogUtil instead",
            Category.MESSAGES,
            5,
            Severity.ERROR,
            new Implementation(LogDetector.class, Scope.CLASS_FILE_SCOPE));

    public List<String> getApplicableCallNames() {
        return Arrays.asList("e", "d", "i", "w", "wtf");
    }

    public void checkCall(ClassContext classContext, ClassNode classNode, MethodNode methodNode, MethodInsnNode methodinsnNode) {
        String owner = methodinsnNode.owner;
        if ("android/util/Log".equals(owner)) {
            classContext.report(ISSUE,
                    methodNode,
                    methodinsnNode,
                    classContext.getLocation(methodinsnNode),
                    "You shouldn't use Log directly");
        }
    }

}
