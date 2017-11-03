package com.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanFactory {

	/**
	 * 通过给定一个id返回一个指定的实现类
	 * @param id
	 * @return
	 */
	public static Object getBean(String id){
		
		try {
			//通过new SAXReader来读取一个输入流获取document对象
			Document document = new SAXReader().read(BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml"));
			//根据规则选择指定的节点元素
			Element ele = (Element) document.selectSingleNode("//bean[@id='"+id+"']");
			//获取元素的class属性的值
			String value = ele.attributeValue("class");
			
			return Class.forName(value).newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
//	public static void main(String[] args) {
//		System.out.println(getBean("ReaderService"));;
//	}
	
	
}
