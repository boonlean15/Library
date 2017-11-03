package com.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BookTypeDao;
import com.entity.BookType;
import com.service.BookTypeService;
import com.utils.BeanFactory;

public class BookTypeServiceImpl implements BookTypeService {
	
	private static final BookTypeDao bookTypeDao = (BookTypeDao) BeanFactory.getBean("BookTypeDao");
	/**
	 * 获取图书类型
	 */
	@Override
	public List<BookType> getAllBookTypes() throws SQLException {
		
		List<BookType> list = bookTypeDao.getAllBookTypes();
		return list;
	}

}
