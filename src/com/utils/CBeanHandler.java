package com.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


public class CBeanHandler<T> implements ResultSetHandlerItfs<T> {

	private Class<T> clazz;

	public CBeanHandler(Class<T> t) {

		this.clazz = t;

	}

	/**
	 * 处理结果集返回一个bean对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Override
	public T hander(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {

		Map<String, Object> map = BeanUtils.resultSetToMap(rs);
		
		@SuppressWarnings("all")
		T bean = (T) BeanUtils.mapToBean(clazz.newInstance(), map);

		return bean;
	}

}
