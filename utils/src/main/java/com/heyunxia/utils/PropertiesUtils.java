package com.heyunxia.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-20 下午3:13
 */
public class PropertiesUtils {

    /**
     * 根据对象列表和对象的某个属性返回属性的List集合
     * 类似 guava中的　Lists.transform
     *
     * @param objList      对象列表
     * @param propertyName 要操作的属性名称
     * @return <pre>
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static <T, E> List<E> getPropertyList(List<T> objList, String propertyName) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        if (objList == null || objList.size() == 0)
            throw new IllegalArgumentException("No objList specified");
        if (propertyName == null || "".equals(propertyName)) {
            throw new IllegalArgumentException("No propertyName specified for bean class '" + objList.get(0).getClass() + "'");
        }
        List<E> propList = new LinkedList<>();
        for (int i = 0; i < objList.size(); i++) {
            T obj = objList.get(i);
            propList.add((E) getProperty(propertyName, obj));
        }
        return propList;
    }

    private static <T> T getProperty(String propertyName, Object object) {
        try {

            PropertyDescriptor pd = new PropertyDescriptor(propertyName, object.getClass());
            Method method = pd.getReadMethod();
            return (T) method.invoke(object);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    /**
     * 对象集合，将对象某一属性做为Map的key,　value为对像集合
     * @param objList
     * @param propertyName
     * @param <E>
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static <E, T> Map<E, List<T>> list2GroupedMap(List<T> objList, String propertyName) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        if (objList == null || objList.size() == 0)
            throw new IllegalArgumentException("invalid argument objList");
        if (propertyName == null || "".equals(propertyName)) {
            throw new IllegalArgumentException("invalid argument propertyName");
        }
        Map<E, List<T>> map = new ConcurrentHashMap<>();

        for (int i = 0; i < objList.size(); i++) {
            T obj = objList.get(i);
            Object propertyValue = getProperty(propertyName, obj);
            List<T> innerList = map.get(propertyValue);
            if (innerList == null || innerList.size() == 0) {
                innerList = new ArrayList<>();
            }
            innerList.add(obj);
            map.put((E) propertyValue, innerList);
        }
        return map;
    }

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {

            for (int j = 0; j < 1000; j++) {
                userList.add(new User("heyunixa" + i + "" + j, "F" + j, "age" + j, "class" + i));
            }
        }

        /*userList.add(new User("heyunixa", "F", "8", "1"));
        userList.add(new User("heyunixa1", "1F", "18", "1"));
        userList.add(new User("heyunixa2", "2F", "28", "1"));
        userList.add(new User("heyunixa3", "3F", "38", "1"));
        userList.add(new User("heyunixa4", "4F", "48", "1"));
        userList.add(new User("heyunixa5", "5F", "58", "1"));
        userList.add(new User("heyunixa6", "6F", "68", "1"));


        userList.add(new User("heyunixa1", "F", "8", "2"));
        userList.add(new User("heyunixa11", "1F", "18", "2"));
        userList.add(new User("heyunixa12", "2F", "28", "2"));
        userList.add(new User("heyunixa13", "3F", "38", "2"));
        userList.add(new User("heyunixa14", "4F", "48", "2"));
        userList.add(new User("heyunixa15", "5F", "58", "2"));
        userList.add(new User("heyunixa16", "6F", "68", "2"));


        userList.add(new User("heyunixa2", "F", "8", "3"));
        userList.add(new User("heyunixa21", "1F", "18", "3"));
        userList.add(new User("heyunixa22", "2F", "28", "3"));
        userList.add(new User("heyunixa23", "3F", "38", "3"));
        userList.add(new User("heyunixa24", "4F", "48", "3"));
        userList.add(new User("heyunixa25", "5F", "58", "3"));
        userList.add(new User("heyunixa26", "6F", "68", "3"));*/


        try {
            /*List<String> oneList = getPropertyList(userList, "name");
            System.out.println(oneList);
            Map<String, List<Object>> map = getPropertiesMap(userList, "name", "age");
            System.out.println(map);*/
            long start = System.currentTimeMillis();
            Map<String, List<User>> map = list2GroupedMap(userList, "classId");
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    static class User {
        String name;
        String sex;
        String age;

        String classId;

        public User(String name, String sex, String age, String classId) {
            this.name = name;
            this.sex = sex;
            this.age = age;
            this.classId = classId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }
    }
}
