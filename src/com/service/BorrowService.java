package com.service;

import java.sql.SQLException;

import com.entity.BorrowInfo;
import com.entity.PageBean;

public interface BorrowService {

	PageBean<BorrowInfo> findBorrowInfoByPage(int pageCode, int pageSize) throws SQLException;

	BorrowInfo getBorrowInfoById(BorrowInfo info) throws SQLException;

	int addBorrow(BorrowInfo borrowInfo) throws SQLException;

	int renewBook(BorrowInfo borrowInfo) throws SQLException;

	boolean checkBorrowInfo() throws SQLException;

}
