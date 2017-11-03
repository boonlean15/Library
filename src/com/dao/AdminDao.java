package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.Admin;
import com.entity.Authorization;
import com.entity.BorrowInfo;
import com.entity.Reader;
import com.entity.ReaderType;

public interface AdminDao {

	Admin getAdminByUsernameAndPwd(String username, String pwd) throws SQLException;

	Authorization getAuthorizationByAid(Integer aid) throws SQLException;

	void updatePwd(Admin admin, String newPwd) throws SQLException;

	boolean updateInfo(Admin admin, String name, String phone, String username) throws SQLException;

	List<ReaderType> getAllReaderType() throws SQLException;

	ReaderType getReaderType(int id) throws SQLException;

	boolean updateReaderType(ReaderType readerType) throws SQLException;

	int getReaderPageCount() throws SQLException;

	List<Reader> getReaderList(int pageCode, int pageSize) throws SQLException;

	Reader getReaderById(String readerId) throws SQLException;

	List<BorrowInfo> getBorrowInfoByReaderId(String readerId) throws SQLException;

	boolean deleteReader(String readerId) throws SQLException;

	boolean updateReaderInfo(Reader updateReader) throws SQLException;

	Reader getReaderBypaperNO(String paperNO) throws SQLException;

	boolean addReader(Reader reader) throws SQLException;

	int queryReaderPageCount(Reader reader) throws SQLException;

	List<Reader> queryReaderList(Reader reader, int pageCode, int pageSize) throws SQLException; 

}
