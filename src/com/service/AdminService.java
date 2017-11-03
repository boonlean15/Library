package com.service;

import java.sql.SQLException;
import java.util.List;

import com.entity.Admin;
import com.entity.PageBean;
import com.entity.Reader;
import com.entity.ReaderType;

public interface AdminService {

	Admin getAdminByUsernameAndPwd(String username, String pwd) throws SQLException;

	void updatePwd(Admin admin, String newPwd) throws SQLException;

	boolean updateInfo(Admin admin, String name, String phone, String username) throws SQLException;

	List<ReaderType> getAllReaderType() throws SQLException;

	ReaderType getReaderType(int id) throws SQLException;

	boolean updateReaderType(int readerTypeId, int maxNum, int bday, double penalty, String readerTypeName, int renewDays) throws SQLException;

	PageBean<Reader> findReaderByPage(int pageCode, int pageSize) throws SQLException;

	Reader getReaderById(String readerId) throws SQLException;

	int deleteReader(String readerId) throws SQLException;

	boolean updateReaderInfo(Reader updateReader) throws SQLException;

	Reader getReaderBypaperNO(String paperNO) throws SQLException;

	boolean addReader(Reader reader) throws SQLException;

	PageBean<Reader> queryReader(Reader reader, int pageCode, int pageSize) throws SQLException;

}
