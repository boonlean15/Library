package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.BorrowInfo;
import com.entity.Reader;

public interface BorrowInfoDao {

	int getBorrowInfoCount() throws SQLException;

	List<BorrowInfo> getBorrowInfoList(int pageCode, int pageSize) throws SQLException;

	BorrowInfo getBorrowInfoById(BorrowInfo info) throws SQLException;

	List<BorrowInfo> getNoBackBorrowInfoByReader(Reader reader) throws SQLException;

	int addBorrow(BorrowInfo borrowInfo) throws SQLException;

	boolean updateBorrowInfo(BorrowInfo borrowInfoById) throws SQLException;

	List<BorrowInfo> getBorrowInfoByNoBackState() throws SQLException;

}
