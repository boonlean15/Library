package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BorrowDao;
import com.entity.Book;
import com.entity.BorrowInfo;
import com.utils.CBeanListHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;

public class BorrowDaoImpl implements BorrowDao {

	/**
	 * 根据图书ID查询借阅信息
	 */
	@Override
	public List<BorrowInfo> getBorrowInfoByBook(Book book) throws SQLException {

		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from borrowinfo where bookId = ?";
		List<BorrowInfo> borrowInfos = qu.query(sql, new CBeanListHandler<>(BorrowInfo.class), book.getBookId());
		
		if(borrowInfos.size()>0 && borrowInfos!=null){
			return borrowInfos;
		}
		
		
		return null;
	}

}
