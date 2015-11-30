package com.heyunxia.utils;

import sun.misc.Unsafe;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-25 下午2:15
 */
public class CoverBeanUtils {

    private static final long serialVersionUID = 5454856921754781038L;

    private static final String GET = "get";
    private static final String SET = "set";
    private static final List ignoreClass = Arrays.asList(Integer.class, Long.class, Float.class, Double.class);
    private static final List ignoreName = Arrays.asList("int", "long", "float", "double");

    static Unsafe unsafe;
    private static long fieldOffSet;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
            f.setAccessible(true);
            try {
                unsafe = (Unsafe) f.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public static void copy(Object source, Object target) {

        Class sourceCls = source.getClass();
        Class targetCls = target.getClass();
        if (!(sourceCls.equals(targetCls) || sourceCls.isAssignableFrom(targetCls))) {
            //类型异常
            throw new WrongMethodTypeException("类型不一");
        }

        Field[] sourceFields = loadAllFields(sourceCls);
        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            //System.out.println(sourceField.getName() + "--" + sourceField.getType() + "--" + sourceField.getModifiers());
            Field targetField = loadField(targetCls, fieldName);

            if (targetField == null) {
                continue;
            }

            if (sourceField.getType() != targetField.getType()) {//判断字段类型
                continue;
            }

            // 由属性名字得到对应get和set方法的名字
            String getMethodName = GET + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setMethodName = SET + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            // 由方法的名字得到get和set方法的Method对象
            Method getMethod = loadMethod(sourceCls, getMethodName);
            Method setMethod = loadMethod(targetCls, setMethodName, sourceField.getType());

            try {
                Object value = getMethod.invoke(source);
                if (value == null || value == 0) continue;

                if ((ignoreClass.contains(sourceField.getType()) || ignoreName.contains(sourceField.getType().getName()))
                        && Long.valueOf(value.toString()) == 0 ) {
                    continue;
                }

                //fieldOffSet = unsafe.objectFieldOffset(targetField);

                setMethod.invoke(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    public static Method loadMethod(Class<?> cls, String methodName, Class<?>... param) {
        Method method = null;
        try {
            method = cls.getMethod(methodName, param);
            if (method == null) {
                return loadMethod(cls.getSuperclass(), methodName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;

    }

    public static Field loadField(Class<?> cls, String fieldName) {
        Field field = null;
        try {
            field = cls.getDeclaredField(fieldName);
            if (field == null && cls.getSuperclass() != Object.class) {
                return loadField(cls.getSuperclass(), fieldName);
            }
        } catch (NoSuchFieldException e) {

            if (cls.getSuperclass() != Object.class) {
                return loadField(cls.getSuperclass(), fieldName);
            } else {
                e.printStackTrace();
            }
        }
        return field;
    }


    public static Field[] loadAllFields(Class cls) {

        List<Field> list = new ArrayList<>();

        Field[] fields = cls.getDeclaredFields();
        list.addAll(Arrays.asList(fields));

        if (cls.getSuperclass() != Object.class) {
            list.addAll(Arrays.asList(loadAllFields(cls.getSuperclass())));
        }

        return list.toArray(new Field[]{});
    }
}
