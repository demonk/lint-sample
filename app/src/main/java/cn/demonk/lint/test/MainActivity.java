package cn.demonk.lint.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ligs on 8/11/16.
 */
public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(cn.demonk.lint.test.R.layout.activity_main);

        Log.e("demonk", "test");
    }
}
