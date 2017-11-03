package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.dao.AdminDao;
import com.entity.Admin;
import com.entity.Authorization;
import com.entity.BorrowInfo;
import com.entity.Reader;
import com.entity.ReaderType;
import com.utils.CBeanHandler;
import com.utils.CBeanListHandler;
import com.utils.CscalarHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;
import com.utils.UUIDUtils;

public class AdminDaoImpl implements AdminDao {

	@Override
	public Admin getAdminByUsernameAndPwd(String username, String pwd) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from admin where username = ? and pwd = ?";
		Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class), username, pwd);

		return admin;
	}

	/**
	 * 通过aid获取用户的管理权限
	 */
	@Override
	public Authorization getAuthorizationByAid(Integer aid) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from authorization where aid = ?";

		Authorization authorization = qu.query(DataSourceUtils.getConnection(), sql,
				new CBeanHandler<>(Authorization.class), aid);

		return authorization;
	}

	/**
	 * 更新密码
	 * 
	 * @throws SQLException
	 */
	@Override
	public void updatePwd(Admin admin, String newPwd) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update admin set pwd = ? where aid = ?";

		qu.update(sql, newPwd, admin.getAid());
	}

	/**
	 * 修改信息
	 */
	@Override
	public boolean updateInfo(Admin admin, String name, String phone, String username) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update admin set name = ? , phone = ? , username = ? where aid = ?";
		int code = qu.update(sql, name, phone, username, admin.getAid());
		return code != 0 ? true : false;
	}

	/**
	 * 查询所有读者类型
	 */
	@Override
	public List<ReaderType> getAllReaderType() throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from readertype";
		List<ReaderType> list = qu.query(sql, new CBeanListHandler<>(ReaderType.class), null);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	/**
	 * 根据Id查询读者类型
	 */
	@Override
	public ReaderType getReaderType(int id) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from readertype where readerTypeId = ?";
		ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(ReaderType.class),
				id);

		return readerType;
	}

	@Override
	public boolean updateReaderType(ReaderType readerType) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "update readertype set readerTypeName = ? , maxNum = ? , bday = ? , penalty = ? , renewDays = ? where readerTypeId = ?";
		int code = qu.update(DataSourceUtils.getConnection(), sql, readerType.getReaderTypeName(),
				readerType.getMaxNum(), readerType.getBday(), readerType.getPenalty(), readerType.getRenewDays(),
				readerType.getReaderTypeId());

		return code != 0 ? true : false;
	}

	// 查找读者数量
	@Override
	public int getReaderPageCount() throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from reader";

		return ((Long) qu.query(DataSourceUtils.getConnection(), sql, new CscalarHandler(), null)).intValue();
	}

	// 查找读者集合
	@Override
	public List<Reader> getReaderList(int pageCode, int pageSize) throws SQLException {

		QueryUtils qu = new QueryUtils();
		String sql = "select * from reader order by createTime desc limit ? , ?";
		List<Reader> list = qu.query(DataSourceUtils.getConnection(), sql, new CBeanListHandler<>(Reader.class),
				pageSize * (pageCode - 1), pageSize);

		if (list != null && list.size() > 0) {

			for (Reader reader : list) {
				sql = "select * from readertype where readerTypeId = ?";
				ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
				reader.setReaderType(readerType);
				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						reader.getAid());
				reader.setAdmin(admin);
			}
			return list;
		}

		return null;
	}

	// 根据Id获取读者，设置了readerType和Admin
	@Override
	public Reader getReaderById(String readerId) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from reader where readerId = ?";
		Reader reader = qu.query(sql, new CBeanHandler<>(Reader.class), readerId);
		sql = "select * from readertype where readerTypeId = ?";
		ReaderType readerType = qu.query(sql, new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
		reader.setReaderType(readerType);
		sql = "select * from admin where aid = ?";
		Admin admin = qu.query(sql, new CBeanHandler<>(Admin.class), reader.getAid());
		reader.setAdmin(admin);

		return reader;
	}

	// 通过读者id获取读者借阅信息集合
	@Override
	public List<BorrowInfo> getBorrowInfoByReaderId(String readerId) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from borrowinfo where readerId = ?";
		List<BorrowInfo> borrowInfos = qu.query(sql, new CBeanListHandler<>(BorrowInfo.class), readerId);
		if(borrowInfos.size()>0 && borrowInfos!=null){
			return borrowInfos;
		}
		
		
		return null;
	}

	@Override
	public boolean deleteReader(String readerId) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "delete from reader where readerId = ?";
		int code = qu.update(sql, readerId);
		return code != 0 ? true : false;
	}

	/**
	 * 修改读者信息
	 */
	@Override
	public boolean updateReaderInfo(Reader updateReader) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update reader set readerTypeId = ?,name = ?, phone = ?, email = ?, paperNO = ? where readerId = ?";
		int code = qu.update(sql, updateReader.getReaderTypeId(), updateReader.getName(), updateReader.getPhone(),
				updateReader.getEmail(), updateReader.getPaperNO(), updateReader.getReaderId());

		return code != 0 ? true : false;
	}

	@Override
	public Reader getReaderBypaperNO(String paperNO) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from reader where paperNO = ?";
		Reader reader = qu.query(sql, new CBeanHandler<>(Reader.class), paperNO);
		if (reader.getReaderId() != null && reader.getReaderId().length() > 0) {
			return reader;
		}

		return null;
	}

	@Override
	public boolean addReader(Reader reader) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "insert into reader values(?,?,?,?,?,?,?,?,?)";
		int code = qu.update(sql, UUIDUtils.getId(), reader.getPwd(), reader.getName(), reader.getPaperNO(),
				reader.getPhone(), reader.getEmail(), reader.getCreateTime(), reader.getReaderTypeId(),
				reader.getAid());
		return code != 0 ? true : false;
	}

	@Override
	public int queryReaderPageCount(Reader reader) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(readerId) from reader where 1=1";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);

		if (!"".equals(reader.getPaperNO().trim())) {
			sb.append(" and paperNO like '%" + reader.getPaperNO() + "%'");
		}
		if (!"".equals(reader.getName().trim())) {
			sb.append(" and name like '%" + reader.getName() + "%'");
		}
		if (reader.getReaderTypeId() != -1) {
			sb.append(" and readerTypeId = " + reader.getReaderTypeId());
		}

		return ((Long) qu.query(DataSourceUtils.getConnection(), sb.toString(), new CscalarHandler(), null)).intValue();
	}

	@Override
	public List<Reader> queryReaderList(Reader reader, int pageCode, int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from reader where 1=1";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		if (!"".equals(reader.getPaperNO().trim())) {
			sb.append(" and paperNO like '%" + reader.getPaperNO() + "%'");
		}
		if (!"".equals(reader.getName().trim())) {
			sb.append(" and name like '%" + reader.getName() + "%'");
		}
		if (reader.getReaderTypeId() != -1) {
			sb.append(" and readerTypeId = " + reader.getReaderTypeId());
		}
		sb.append(" order by createTime desc limit ? , ?");

		List<Reader> list = qu.query(DataSourceUtils.getConnection(), sb.toString(), new CBeanListHandler<>(Reader.class),
				pageSize * (pageCode - 1), pageSize);
		if (list != null && list.size() > 0) {

			for (Reader newreader : list) {
				sql = "select * from readertype where readerTypeId = ?";
				ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(ReaderType.class), newreader.getReaderTypeId());
				newreader.setReaderType(readerType);
				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						newreader.getAid());
				newreader.setAdmin(admin);
			}
			return list;
		}

		return null;
	}

}
