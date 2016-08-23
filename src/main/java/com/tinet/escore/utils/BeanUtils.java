package com.tinet.escore.utils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
  
  
/** 
 * 对象与对象深度拷贝，包括嵌套的复合类型，可以忽略类型，只要有相通的属性名 
 * 数组也可以处理，即便dest是一个空数组 
 */  
public class BeanUtils {  
    private static final Logger LOG = Logger.getLogger(BeanUtils.class);  
    
    /**
     * map 转为 bean
     * @param map
     * @param obj
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) {  
  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                if (map.containsKey(key)) {  
                    Object value = map.get(key);  
                    // 得到property对应的setter方法  
                    Method setter = property.getWriteMethod();  
                    setter.invoke(obj, value);  
                }  
  
            }  
  
        } catch (Exception e) {  
            System.out.println("transMap2Bean Error " + e);  
        }  
  
        return;  
  
    } 
    
    /**
     * Bean 转为  Map
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {  
        if(obj == null){  
            return null;  
        }          
        Map<String, Object> map = new HashMap<String, Object>();  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                // 过滤class属性  
                if (!key.equals("class")) {  
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(obj);  
                    map.put(key, value);  
                }  
  
            }  
        } catch (Exception e) {  
            System.out.println("transBean2Map Error " + e);  
        }  
  
        return map;  
  
    }
    
    /** 
     *  
     * @param dest 目标对象 
     * @param orig 原始对象 
     * @throws Exception 
     */  
    public static void copy(Object dest, Object orig) throws Exception {  
        if (dest == null) {  
            throw new IllegalArgumentException("No destination bean specified");  
        }  
        if (orig == null) {  
            throw new IllegalArgumentException("No origin bean specified");  
        }  
          
        Class origClass = orig.getClass();  
        Class destClass = dest.getClass();  
        if (origClass == String.class || origClass.isPrimitive()) {  
            dest = orig;  
        }  
        if(orig.getClass().isArray()){  
            Object[] destArr = (Object[]) dest;  
            Object[] origArr = (Object[]) orig;  
            Class elemenClass = destArr.getClass().getComponentType();  
              
            for(int i=0;i<origArr.length;i++){  
                if(destArr[i]==null){  
                    destArr[i] = elemenClass.newInstance();  
                }  
                  
                copy(destArr[i], origArr[i]);  
            }  
        }  
        String classLogInfo = "origClass:"+origClass.getName()+",destClass:"+destClass.getName()+",";  
        PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(orig);  
        for (int i = 0; i < origDescriptors.length; i++) {  
            String name = origDescriptors[i].getName();  
            if ("class".equals(name)) {  
                continue;  
            }  
            Object value = null;  
            if (PropertyUtils.isReadable(orig, name)  
                    && PropertyUtils.isWriteable(dest, name)) {  
                try {  
                    value = PropertyUtils.getSimpleProperty(orig, name);  
                    PropertyUtils.setSimpleProperty(dest, name, value);  
                } catch (IllegalArgumentException e) {  
                    // 类型不同   
                    try {  
                        PropertyDescriptor targetDescriptor = PropertyUtils.getPropertyDescriptor(dest, name);  
                        Object new_value = targetDescriptor.getPropertyType().newInstance();  
                        copy(new_value, value);  
//                      LOG.info(new_value);   
                        PropertyUtils.setSimpleProperty(dest, name, new_value);  
                    }catch(IllegalArgumentException e1){  
                        //   
                    }   
                    catch (IllegalAccessException e1) {  
                        throw e1;  
                    } catch (InvocationTargetException e1) {  
                        throw e1;  
                    } catch (NoSuchMethodException e1) {  
                        throw e1;  
                    } catch (InstantiationException e1) {  
                        throw e1;  
                    }  
                } catch (NoSuchMethodException e) {  
                    throw e;  
                } catch (IllegalAccessException e) {  
                    throw e;  
                } catch (InvocationTargetException e) {  
                    throw e;  
                }  
            }  
        }  
    }  
}