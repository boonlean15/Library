package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.ForfeitInfo;
import com.entity.Reader;

public interface ForfeitDao {

	ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo) throws SQLException;

	List<ForfeitInfo> getForfeitByReader(Reader reader) throws SQLException;

	int getForfeitInfoCount() throws SQLException;

	List<ForfeitInfo> getForfeitInfoList(int pageCode, int pageSize) throws SQLException;

	boolean addForfeitInfo(ForfeitInfo forfeitInfo) throws SQLException;

	boolean updateForfeitInfo(ForfeitInfo forfeitInfoById) throws SQLException;

	int getForfeitInfoCountByCondition(String iSBN, String paperNO, int borrowId) throws SQLException;

	List<ForfeitInfo> getForfeitInfoListByCondition(String iSBN, String paperNO, int borrowId, int pageCode,
			int pageSize) throws SQLException;

}
