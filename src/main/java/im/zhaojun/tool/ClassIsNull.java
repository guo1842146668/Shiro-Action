package im.zhaojun.tool;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author idmin
 */
public class ClassIsNull {
    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        // 得到类对象
        Class clazz = object.getClass();
        // 得到所有属性
        Field[] fields = clazz.getDeclaredFields();
        //定义返回结果，默认为true
        boolean flag = true;
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                //得到属性值
                fieldValue = field.get(object);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            //只要有一个属性值不为null 就返回false 表示对象不为null
            if (fieldValue != null) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isNotNull(Object object) {
        if (object == null) {
            return false;
        }
        // 得到类对象
        Class clazz = object.getClass();
        // 只能得到实体类中的所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 定义返回结果，默认为true
        boolean flag = true;
        for (Field field : fields) {
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            if ("online".equals(field.getName())) {
                continue;
            }
            if ("personnelMap".equals(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                //得到属性值
                fieldValue = field.get(object);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            //只要有一个属性值为null 就返回false 表示对象中有空
            if (fieldValue == null) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isMapNull(Map<String, Object> map) {
        if(map == null){
            return false;
        }

        for (String key : map.keySet()) {
            if (map.get(key) == null || "null".equals(map.get(key))) {
                return false;
            }
        }
        return true;
    }

}
