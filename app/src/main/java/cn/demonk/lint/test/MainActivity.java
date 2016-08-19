package cn.demonk.lint.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import cn.demonk.lint.test.persist.PersistHelper;
import cn.demonk.lint.test.persist.PersistKey;

/**
 * Created by ligs on 8/11/16.
 */
public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(cn.demonk.lint.test.R.layout.activity_main);

        Log.e("demonk", "test");

        for (int i = 0; i < 1; i++)
            PersistHelper.put(PersistKey.INIT_DATA, "Data");
    }
}
