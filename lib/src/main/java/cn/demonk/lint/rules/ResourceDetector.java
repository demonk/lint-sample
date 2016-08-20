package cn.demonk.lint.rules;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Collection;
import java.util.Collections;


/**
 * Created by ligs on 8/20/16.
 */
public class ResourceDetector extends ResourceXmlDetector {

    public static final Issue ISSUE = Issue.create("CheckXmlResource",
            "simple note",
            "full note",
            Category.TYPOGRAPHY,
            5,
            Severity.WARNING,
            new Implementation(ResourceDetector.class, Scope.RESOURCE_FILE_SCOPE));

    //只检查xml
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return LintUtils.isXmlFile(file);
    }

    //只接受values文件夹
    public boolean appliesTo(ResourceFolderType folderType) {
        return ResourceFolderType.VALUES == folderType;
    }

    //只检查attr的标签
    public Collection<String> getApplicableElements() {
        return Collections.singleton("attr");
    }

    //只检查name的属性
    public Collection<String> getApplicableAttributes() {
        return Collections.singleton("name");
    }

    public void visitElement(XmlContext context, Element element) {
        final Attr attributeNode = element.getAttributeNode("name");
        if (attributeNode != null) {
            final String value = attributeNode.getValue();//获取到属性的节点值
            System.out.println("value=" + value);
            if (!value.startsWith("dk_")) {
                context.report(ISSUE, attributeNode, context.getLocation(attributeNode), "prefix custom attr by 'dk_");
            }
        }
    }
}
