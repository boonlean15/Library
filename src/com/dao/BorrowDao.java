package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.Book;
import com.entity.BorrowInfo;

public interface BorrowDao {

	List<BorrowInfo> getBorrowInfoByBook(Book book) throws SQLException;

}
