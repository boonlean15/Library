package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BookTypeDao;
import com.entity.BookType;
import com.utils.CBeanListHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;

public class BookTypeDaoImpl implements BookTypeDao {

	/**
	 * 获取图书类型
	 */
	@Override
	public List<BookType> getAllBookTypes() throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from booktype";
		List<BookType> list = qu.query(sql, new CBeanListHandler<>(BookType.class), null);
		if(list.size()>0 && list != null){
			return list;
		}
		return null;
	}


}
