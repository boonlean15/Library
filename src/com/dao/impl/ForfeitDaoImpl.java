package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.ForfeitDao;
import com.entity.Admin;
import com.entity.BackInfo;
import com.entity.Book;
import com.entity.BookType;
import com.entity.BorrowInfo;
import com.entity.ForfeitInfo;
import com.entity.Reader;
import com.entity.ReaderType;
import com.utils.CBeanHandler;
import com.utils.CBeanListHandler;
import com.utils.CscalarHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;

public class ForfeitDaoImpl implements ForfeitDao {

	// 得到该借阅记录的罚金信息
	@Override
	public ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from forfeitinfo where borrowId = ?";
		ForfeitInfo info = qu.query(sql, new CBeanHandler<>(ForfeitInfo.class), forfeitInfo.getBorrowId());

		sql = "select * from borrowinfo where borrowId = ?";
		BorrowInfo borrowInfo = qu.query(sql, new CBeanHandler<>(BorrowInfo.class), info.getBorrowId());
		// 查询设置图书
		sql = "select * from book where bookId = ?";
		Book book = qu.query(sql, new CBeanHandler<>(Book.class), borrowInfo.getBookId());

		sql = "select * from booktype where typeId = ?";
		BookType bookType = qu.query(sql, new CBeanHandler<>(BookType.class), book.getTypeId());
		book.setBookType(bookType);
		borrowInfo.setBook(book);

		// 查询设置读者信息
		sql = "select * from reader where readerId = ?";
		Reader reader = qu.query(sql, new CBeanHandler<>(Reader.class), borrowInfo.getReaderId());
		sql = "select * from readertype where readerTypeId = ?";
		ReaderType readerType = qu.query(sql, new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
		reader.setReaderType(readerType);
		borrowInfo.setReader(reader);

		sql = "select * from admin where aid = ?";
		Admin admin = qu.query(sql, new CBeanHandler<>(Admin.class), info.getAid());
		info.setAdmin(admin);
		// 设置借阅信息
		info.setBorrowInfo(borrowInfo);

		return info;
	}

	@Override
	public List<ForfeitInfo> getForfeitByReader(Reader reader) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "SELECT f.borrowId,f.forfeit,f.isPay,f.aid FROM forfeitinfo  f,borrowinfo  b where  b.borrowId = f.borrowId and b.readerId =?";

		List<ForfeitInfo> list = qu.query(sql, new CBeanListHandler<>(ForfeitInfo.class), reader.getReaderId());
		if (list != null && list.size() > 0) {
			return list;
		}

		return null;
	}

	@Override
	public int getForfeitInfoCount() throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from forfeitinfo";

		return ((Long) qu.query(DataSourceUtils.getConnection(), sql, new CscalarHandler(), null)).intValue();
	}

	@Override
	public List<ForfeitInfo> getForfeitInfoList(int pageCode, int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from forfeitinfo order by isPay desc limit ? , ?";
		List<ForfeitInfo> list = qu.query(DataSourceUtils.getConnection(), sql,
				new CBeanListHandler<>(ForfeitInfo.class), pageSize * (pageCode - 1), pageSize);

		if (list != null && list.size() > 0) {

			for (ForfeitInfo forfeitinfo : list) {
				// 查询借阅信息
				sql = "select * from borrowinfo where borrowId = ?";
				BorrowInfo borrowInfo = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(BorrowInfo.class), forfeitinfo.getBorrowId());
				// 查询设置图书
				sql = "select * from book where bookId = ?";
				Book book = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Book.class),
						borrowInfo.getBookId());
				borrowInfo.setBook(book);
				sql = "select * from booktype where typeId = ?";
				BookType bookType = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(BookType.class),
						book.getTypeId());
				book.setBookType(bookType);
				borrowInfo.setBook(book);

				// 查询设置读者信息
				sql = "select * from reader where readerId = ?";
				Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class),
						borrowInfo.getReaderId());
				borrowInfo.setReader(reader);

				sql = "select * from readertype where readerTypeId = ?";
				ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
				reader.setReaderType(readerType);
				borrowInfo.setReader(reader);

				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						forfeitinfo.getAid());
				forfeitinfo.setAdmin(admin);

				// 设置借阅信息
				forfeitinfo.setBorrowInfo(borrowInfo);
			}
			return list;
		}

		return null;
	}

	// 添加罚金信息
	@Override
	public boolean addForfeitInfo(ForfeitInfo forfeitInfo) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from forfeitinfo where borrowId = ?";

		ForfeitInfo info = qu.query(sql, new CBeanHandler<>(ForfeitInfo.class), forfeitInfo.getBorrowId());
		if (info != null && info.getBorrowId() != 0) {
			return true;
		}

		sql = "insert into forfeitinfo values(?,?,?,?)";
		int code = qu.update(sql, forfeitInfo.getBorrowId(), forfeitInfo.getForfeit(), forfeitInfo.getIsPay(),
				forfeitInfo.getAid());

		return code != 0 ? true : false;
	}

	@Override
	public boolean updateForfeitInfo(ForfeitInfo forfeitInfoById) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update forfeitinfo set aid = ? , isPay = ? where borrowId = ?";
		int code = qu.update(sql, forfeitInfoById.getAid(), forfeitInfoById.getIsPay(), forfeitInfoById.getBorrowId());
		return code != 0 ? true : false;
	}

	@Override
	public int getForfeitInfoCountByCondition(String iSBN, String paperNO, int borrowId) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from forfeitinfo fo ,borrowinfo bo,book bk,reader r "
				+ "where fo.borrowId=bo.borrowId and bk.bookId=bo.bookId and bo.readerId=r.readerId ";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);

		if (!"".equals(iSBN.trim())) {
			sb.append(" and bk.ISBN like '%" + iSBN + "%'");
		}
		if (!"".equals(paperNO.trim())) {
			sb.append(" and r.paperNO like '%" + paperNO + "%'");
		}
		if (borrowId != 0) {
			sb.append(" and bo.borrowId like '%" + borrowId + "%'");
		}

		return ((Long) qu.query(DataSourceUtils.getConnection(), sb.toString(), new CscalarHandler(), null)).intValue();
	}

	@Override
	public List<ForfeitInfo> getForfeitInfoListByCondition(String iSBN, String paperNO, int borrowId, int pageCode,
			int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select ba.* from forfeitinfo ba ,borrowinfo bo,book bk,reader r "
				+ "where ba.borrowId=bo.borrowId and bk.bookId=bo.bookId and bo.readerId=r.readerId ";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);

		if (!"".equals(iSBN.trim())) {
			sb.append(" and bk.ISBN like '%" + iSBN + "%'");
		}
		if (!"".equals(paperNO.trim())) {
			sb.append(" and r.paperNO like '%" + paperNO + "%'");
		}
		if (borrowId != 0) {
			sb.append(" and bo.borrowId like '%" + borrowId + "%'");
		}

		sb.append(" order by ba.isPay desc limit ? , ?");

		List<ForfeitInfo> list = qu.query(DataSourceUtils.getConnection(), sb.toString(),
				new CBeanListHandler<>(ForfeitInfo.class), pageSize * (pageCode - 1), pageSize);
		if (list != null && list.size() > 0) {

			for (ForfeitInfo info : list) {
				// 查询借阅信息
				sql = "select * from borrowinfo where borrowId = ?";
				BorrowInfo borrowInfo = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(BorrowInfo.class), info.getBorrowId());
				// 查询设置图书
				sql = "select * from book where bookId = ?";
				Book book = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Book.class),
						borrowInfo.getBookId());

				sql = "select * from booktype where typeId = ?";
				BookType bookType = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(BookType.class),
						book.getTypeId());
				book.setBookType(bookType);
				borrowInfo.setBook(book);

				// 查询设置读者信息
				sql = "select * from reader where readerId = ?";
				Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class),
						borrowInfo.getReaderId());
				sql = "select * from readertype where readerTypeId = ?";
				ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
				reader.setReaderType(readerType);
				borrowInfo.setReader(reader);

				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						info.getAid());
				info.setAdmin(admin);
				// 设置借阅信息
				info.setBorrowInfo(borrowInfo);
			}
			return list;
		}

		return null;
	}

}
