package com.service;

import java.sql.SQLException;

import com.entity.ForfeitInfo;
import com.entity.PageBean;
import com.entity.Reader;

public interface ForfeitService {

	PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode, int pageSize) throws SQLException;

	ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo) throws SQLException;

	int payForfeit(ForfeitInfo forfeitInfo) throws SQLException;

	PageBean<ForfeitInfo> queryForfeitInfo(String iSBN, String paperNO, int borrowId, int pageCode, int pageSize) throws SQLException;

	PageBean<ForfeitInfo> findMyForfeitInfoByPage(Reader reader, int pageCode, int pageSize) throws SQLException;

}
