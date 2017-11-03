package com.service;

import java.sql.SQLException;
import java.util.List;

import com.entity.Reader;

public interface ReaderService {

	Reader getReaderByUsernameAndPwd(Reader reader) throws SQLException;

	List<Reader> getListByUsernameAndPwd(Reader reader) throws SQLException;

	void updatePwd(Reader reader, String newPwd) throws SQLException;

	boolean updateInfo(Reader reader, String name, String phone, String email) throws SQLException;
	
}
