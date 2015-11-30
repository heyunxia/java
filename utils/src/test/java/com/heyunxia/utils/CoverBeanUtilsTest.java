package com.heyunxia.utils;

import com.heyunxia.domain.Follow;
import com.heyunxia.domain.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-25 下午2:22
 */

public class CoverBeanUtilsTest {

    @org.junit.Test
    public void testCopy() throws Exception {


        User source = new User();
        source.setLoginName("123");
        source.setCreateBy("中国");

        User target = new User();
        target.setUserName("张三");
        target.setCreateAt(System.currentTimeMillis());

        CoverBeanUtils.copy(source, target);

        System.out.println(source.toString());
        System.out.println(target.toString());
    }


    @Test
    public void testLoadFiles(){
        Field [] fields = CoverBeanUtils.loadAllFields(Follow.class);
        for (Field field : fields) {
            System.out.println(field.getName());
        }
    }

    @Test
    public void testLoadMethod(){
        Method method = CoverBeanUtils.loadMethod(Follow.class, "getUserName");
        System.out.println(method + "--" + method.getName());
    }

    @Test
    public void testLoadField() {
        Field field = CoverBeanUtils.loadField(Follow.class, "follower");
        System.out.println(field);

        field = CoverBeanUtils.loadField(Follow.class, "loginName");
        System.out.println(field);

    }
}