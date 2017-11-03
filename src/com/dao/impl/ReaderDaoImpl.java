package com.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dao.ReaderDao;
import com.entity.Reader;
import com.entity.ReaderType;
import com.utils.BeanUtils;
import com.utils.CBeanHandler;
import com.utils.CBeanListHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;

public class ReaderDaoImpl implements ReaderDao {

	/**
	 * 根据用户名和密码 查询是否有读者，有返回一个读者
	 * 
	 * @throws SQLException
	 */
	@Override
	public Reader getReaderByUsernameAndPwd(Reader reader) throws SQLException {

		QueryUtils qr = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from reader where paperNO = ? and pwd = ?";
		Reader newReader = qr.query(sql, new CBeanHandler<>(Reader.class), reader.getPaperNO(), reader.getPwd());

		return newReader;
	}

	@Override
	public List<Reader> getListByUsernameAndPwd(Reader reader) throws SQLException {

		QueryUtils qr = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from reader where pwd = ?";
		List<Reader> list = qr.query(sql, new CBeanListHandler<>(Reader.class), reader.getPwd());

		return list;
	}

	/**
	 * 更新密码
	 * @throws SQLException 
	 */
	@Override
	public void updatePwd(Reader reader, String newPwd) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update reader set pwd = ? where readerId = ?";
		
		qu.update(sql, newPwd,reader.getReaderId());
	}

	/**
	 * 修改信息
	 */
	@Override
	public boolean updateInfo(Reader reader,String name, String phone, String email) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update reader set name = ? , phone = ? , email = ? where readerId = ?";
		int code = qu.update(sql, name,phone,email,reader.getReaderId());
		return code!=0?true:false;
	}

	@Override
	public Reader getReaderBypaperNO(Reader reader) throws SQLException {
		QueryUtils qr = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from reader where paperNO = ?";
		Reader newReader = qr.query(sql, new CBeanHandler<>(Reader.class), reader.getPaperNO());
		if(newReader != null && newReader.getName().length()>0){
			//查询读者类型
			sql = "select * from readertype where readerTypeId = ?";
			ReaderType readerType = qr.query(sql, new CBeanHandler<>(ReaderType.class), newReader.getReaderTypeId());
			newReader.setReaderType(readerType);
		}
		
		return newReader;
	}
	
	
	
	
/*	public static void main(String[] args) throws SQLException {
		ReaderDaoImpl dao = new ReaderDaoImpl();
		dao.save();
	}*/


}
