package com.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.ReaderDao;
import com.entity.Reader;
import com.service.ReaderService;
import com.utils.BeanFactory;

public class ReaderServiceImpl implements ReaderService {
	
	private static final ReaderDao readerDao = (ReaderDao) BeanFactory.getBean("ReaderDao");

	/**
	 * 根据用户名和密码获取读者
	 * @throws SQLException 
	 */
	@Override
	public Reader getReaderByUsernameAndPwd(Reader reader) throws SQLException {
		
		Reader newReader  = readerDao.getReaderByUsernameAndPwd(reader);
		
		return newReader;
	}

	/**
	 * 查询list
	 */
	@Override
	public List<Reader> getListByUsernameAndPwd(Reader reader) throws SQLException{
		List<Reader> list= readerDao.getListByUsernameAndPwd(reader);
		return list;
	}

	/**
	 *	更新读者密码
	 * @throws SQLException 
	 */
	@Override
	public void updatePwd(Reader reader, String newPwd) throws SQLException {
		readerDao.updatePwd(reader,newPwd);
		
	}

	/**
	 * 修改读者信息
	 */
	@Override
	public boolean updateInfo(Reader reader,String name, String phone, String email) throws SQLException {
		return readerDao.updateInfo(reader,name,phone,email);
	}

}
