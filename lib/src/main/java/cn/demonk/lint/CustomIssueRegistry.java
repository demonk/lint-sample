package cn.demonk.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.ArrayList;
import java.util.List;

import cn.demonk.lint.rules.LogDetector;

/**
 * Created by ligs on 8/11/16.
 */
public class CustomIssueRegistry extends IssueRegistry {

    @Override
    public List<Issue> getIssues() {
        ArrayList<Issue> list = new ArrayList<Issue>();
        //添加需要检查的规则
        list.add(LogDetector.ISSUE);

        return list;
    }
}
