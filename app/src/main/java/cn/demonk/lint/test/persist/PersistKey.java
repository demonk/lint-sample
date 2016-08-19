package cn.demonk.lint.test.persist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ligs on 8/11/16.
 */
public class PersistKey {
    @PersistType(classType = String.class, category = PersistCategory.DATA)
    public static final String INIT_DATA = "d_data";

    @Inherited
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PersistType {
        Class classType();

        PersistCategory category();
    }
}
