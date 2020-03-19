package com.world.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guankaili
 * @version v1.0.0
 * @date 2020/2/26$ 18:48$
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2020/2/26$ 18:48$     guankaili          v1.0.0           两个对象或集合同名属性赋值
 */
@Slf4j
public class ObjectConversion {
    /**
     * 从List<A> copy到List<B>
     *
     * @param list  List<B>
     * @param clazz B
     * @return List<B>
     */
    public static <T> List<T> copy(List<?> list, Class<T> clazz) {
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }

    /**
     * 从对象A copy到 对象B
     *
     * @param ob    A
     * @param clazz B.class
     * @return B
     */
    public static <T> T copy(Object ob, Class<T> clazz) {
        String oldOb = JSON.toJSONString(ob);
        return JSON.parseObject(oldOb, clazz);
    }

    /**
     * Java实体类转List<Map>
     *
     * @param obj
     * @return
     */
    public static List<Map<String, Object>> entityToMap1(Object obj) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            Map<String, Object> map = new HashMap<String, Object>();
            field.setAccessible(true);
            String fieldName = field.getName();
            Object object = null;
            try {
                object = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.info(e.getMessage());
            }
            if (fieldName.toLowerCase().endsWith("m1")) {
                map.put("month", "一月");
            } else if (fieldName.toLowerCase().endsWith("m2")) {
                map.put("month", "二月");
            } else if (fieldName.toLowerCase().endsWith("m3")) {
                map.put("month", "三月");
            } else if (fieldName.toLowerCase().endsWith("m4")) {
                map.put("month", "四月");
            } else if (fieldName.toLowerCase().endsWith("m5")) {
                map.put("month", "五月");
            } else if (fieldName.toLowerCase().endsWith("m6")) {
                map.put("month", "六月");
            } else if (fieldName.toLowerCase().endsWith("m7")) {
                map.put("month", "七月");
            } else if (fieldName.toLowerCase().endsWith("m8")) {
                map.put("month", "八月");
            } else if (fieldName.toLowerCase().endsWith("m9")) {
                map.put("month", "九月");
            } else if (fieldName.toLowerCase().endsWith("m10")) {
                map.put("month", "十月");
            } else if (fieldName.toLowerCase().endsWith("m11")) {
                map.put("month", "十一月");
            } else if (fieldName.toLowerCase().endsWith("m12")) {
                map.put("month", "十二月");
            }
            map.put(fieldName, object);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 将javabean转换为map
     */
    public static List<Map<String,Object>> beanTOMap(Object bean) throws Exception{//传入一个javabean对象
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        //获取类的属性描述器
        BeanInfo beaninfo= Introspector.getBeanInfo(bean.getClass(),Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor property : pro) {
            String key=property.getName();//得到属性的name
            Method get=property.getReadMethod();
            Object value=get.invoke(bean);//执行get方法得到属性的值
            Map<String, Object> map=new HashMap<>();
            map.put("name",key);
            map.put("value",value);
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * @Author guankaili
     * @Description double去除小数点后的0
     * @Date 13:18 2020/3/18
     * @Param [d]
     * @return java.lang.String
     **/
    public static String formatDouble(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}
