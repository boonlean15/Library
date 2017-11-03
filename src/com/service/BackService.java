package com.service;

import java.sql.SQLException;

import com.entity.BackInfo;
import com.entity.PageBean;
import com.entity.Reader;

public interface BackService {

	PageBean<BackInfo> findBackInfoByPage(int pageCode, int pageSize) throws SQLException;

	BackInfo getBackInfoById(BackInfo backInfo) throws SQLException;

	int addBackInfo(BackInfo backInfo) throws SQLException;

	PageBean<BackInfo> queryBackInfo(String iSBN, String paperNO, int borrowId, int pageCode, int pageSize) throws SQLException;

	PageBean<BackInfo> findMyBorrowInfoByPage(Reader reader, int pageCode, int pageSize) throws SQLException;

}
