package com.utils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.entity.BorrowInfo;
import com.entity.Reader;

/**
 * 把Map转换成Bean,实现了请求map转换成Bean
 * @author hasee
 *
 */
public class BeanUtils {

	//把结果集第一条数据封装成Bean对象返回
	public static Map<String,Object> resultSetToMap(ResultSet rs) throws SQLException{
		if(rs == null){
			return null;
		}
		
		Map<String,Object> map = new HashMap<>();
		if(rs.next()){
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				map.put(columnName, rs.getObject(i));
			}
		}
		
		return map;
	}
	
	public static List<Map<String,Object>> resultSetToListmap(ResultSet rs) throws SQLException{
		
		if(rs == null){
			return null;
		}
		
		List<Map<String,Object>> list = new ArrayList<>();
		while(rs.next()){
			//遍历每一条结果，一条结果封装在一个map中，列字段名为key，值为value
			Map<String,Object> map = new HashMap<>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++) {
				map.put(metaData.getColumnName(i), rs.getObject(i));
				
			}
			list.add(map);
		}
		
		
		
		
		return list;
	}
	
	
	/*
	  	1、通过把每一行的结果封装到Map中，key为列字段名，value为字段值。然后把每个map放入到List中
		2、把Map转换成Bean，通过传入bean和map作为参数完成
		具体步骤：
		1：通过反射，把bean里的每一个方法放入到map集合中，key为方法名大写，value为bean的具体的method方法。
		2：获取map的keyset的iterator迭代器，判断是否有下一个，进行循环。
		   在循环中的处理：
				a:获取map的key值，拼接上get得到一个name
				b:判断存放bean方法的map中是否包含这个name
				c:如果包含，获取存放bean方法的map集合中对应set加key的方法，即setMethod
				d:创建一Object[]，把对应map的value值赋值给obj，然后执行setMethod。
	 */
	public static Object mapToBean(Object bean,Map map){
		//创建一个新Map用来存储Bean的方法名和方法
		Map<String,Method> beanMap = new HashMap<>();
		//通过反射得到Bean的所以方法
		Method[] methods = bean.getClass().getMethods();
		//遍历methods，并存放到beanMap中
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName().toUpperCase();
			beanMap.put(methodName, method);

		}
		
		Iterator iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			String name = "GET"+key.toUpperCase();
//			System.out.println(name);
			//判断存放bean方法的map中是否包含这个name
			if(beanMap.containsKey(name)){
				//如果包含，获取存放bean方法的map集合中对应set加key的方法，即setMethod
				Method setmethod = beanMap.get("SET"+key.toUpperCase());
				try {
					if(setmethod!=null){    
						Object[] obj=null;    
						obj=new Object[1];    
						Object object = map.get(key);
						if(object instanceof Object[]){
							Object[] objs = (Object[]) object;
							obj[0] = objs[0];
						}else{
							obj[0] = object;
						}
						setmethod.invoke(bean, obj);
					}else{    
						continue;    
				    }
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			
			
		}
//		System.out.println(bean);
		return bean;
	}
	

	
	
}
