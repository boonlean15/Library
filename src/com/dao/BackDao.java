package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.BackInfo;

public interface BackDao {

	int getBackInfoCount() throws SQLException;

	List<BackInfo> getBackfoList(int pageCode, int pageSize) throws SQLException;

	int addBack(BackInfo backinfo) throws SQLException;

	BackInfo getBackInfoById(BackInfo backInfo) throws SQLException;

	boolean updateBackInfo(BackInfo backInfoById) throws SQLException;

	int getBackInfoCountByCondition(String iSBN, String paperNO, int borrowId) throws SQLException;

	List<BackInfo> getBackfoListByCondition(String iSBN, String paperNO, int borrowId, int pageCode, int pageSize) throws SQLException;

	boolean deleteBackInfo(BackInfo backInfo) throws SQLException;

}
