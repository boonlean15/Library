package com.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CBeanListHandler<T> implements ResultSetHandlerItfs<List<T>>{

	private Class<T> clazz;
	//通过构造方法传入Class对象
	public CBeanListHandler(Class<T> t) {
		this.clazz = t;
	}
	/**
	 * 把ResultSet转换成List<Bean> 集合
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Override
	public List<T> hander(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
		
		List<T> listBean = new ArrayList<>();
		List<Map<String,Object>> list = BeanUtils.resultSetToListmap(rs);
		for (Map<String, Object> map : list) {
			@SuppressWarnings("unchecked")
			T bean = (T) BeanUtils.mapToBean(clazz.newInstance(), map);
			listBean.add(bean);
		}
		
		return listBean;
	}
	
	
	

}
