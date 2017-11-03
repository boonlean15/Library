package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.dao.BackDao;
import com.entity.Admin;
import com.entity.BackInfo;
import com.entity.Book;
import com.entity.BookType;
import com.entity.BorrowInfo;
import com.entity.Reader;
import com.entity.ReaderType;
import com.utils.CBeanHandler;
import com.utils.CBeanListHandler;
import com.utils.CscalarHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;

public class BackDaoImpl implements BackDao{

	//按页面查询归还信息数量
	@Override
	public int getBackInfoCount() throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from backinfo";

		return ((Long) qu.query(DataSourceUtils.getConnection(), sql, new CscalarHandler(), null)).intValue();
	}
	//按页码查询归还信息
	@Override
	public List<BackInfo> getBackfoList(int pageCode, int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from backinfo order by backDate desc limit ? , ?";
		List<BackInfo> list = qu.query(DataSourceUtils.getConnection(), sql, new CBeanListHandler<>(BackInfo.class),
				pageSize * (pageCode - 1), pageSize);

		if (list != null && list.size() > 0) {

			for (BackInfo backinfo : list) {
				//查询借阅信息
				sql = "select * from borrowinfo where borrowId = ?";
				BorrowInfo borrowInfo = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(BorrowInfo.class), backinfo.getBorrowId());
				//查询设置图书
				sql = "select * from book where bookId = ?";
				Book book = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Book.class), borrowInfo.getBookId());
				borrowInfo.setBook(book);
				//查询设置读者信息
				sql = "select * from reader where readerId = ?";
				Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class), borrowInfo.getReaderId());
				borrowInfo.setReader(reader);
				//设置借阅信息
				backinfo.setBorrowInfo(borrowInfo);
			}
			return list;
		}
		return null;
	}
	/**
	 * 在借书时添加归还信息
	 */
	@Override
	public int addBack(BackInfo backinfo) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "insert into backinfo values(?,?,?)";
		int code = qu.update(sql, backinfo.getBorrowId(),backinfo.getBorrowInfo().getEndDate(),backinfo.getAid());
		
		return code;
	}
	/**
	 * 根据Id获取归还信息
	 */
	@Override
	public BackInfo getBackInfoById(BackInfo backInfo) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from backinfo where borrowId = ?";
		BackInfo backinfo = qu.query(sql, new CBeanHandler<>(BackInfo.class), backInfo.getBorrowId());
		
		//查询借阅信息
		sql = "select * from borrowinfo where borrowId = ?";
		BorrowInfo borrowInfo = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(BorrowInfo.class), backinfo.getBorrowId());
		//查询设置图书
		sql = "select * from book where bookId = ?";
		Book book = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Book.class), borrowInfo.getBookId());
		
		sql = "select * from booktype where typeId = ?";
		BookType bookType = qu.query(sql, new CBeanHandler<>(BookType.class), book.getTypeId());
		book.setBookType(bookType);
		borrowInfo.setBook(book);
		
		//查询设置读者信息
		sql = "select * from reader where readerId = ?";
		Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class), borrowInfo.getReaderId());
		sql = "select * from readertype where readerTypeId = ?";
		ReaderType readerType = qu.query(sql, new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
		reader.setReaderType(readerType);
		borrowInfo.setReader(reader);
		
		sql = "select * from admin where aid = ?";
		Admin admin = qu.query(sql, new CBeanHandler<>(Admin.class), backinfo.getAid());
		backinfo.setAdmin(admin);
		//设置借阅信息
		backinfo.setBorrowInfo(borrowInfo);
		
		
		return backinfo;
	}
	//修改归还信息
	@Override
	public boolean updateBackInfo(BackInfo backInfoById) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update backinfo set backDate = ? , aid = ? where borrowId = ?";
		int code = qu.update(sql, backInfoById.getBackDate(),backInfoById.getAid(),backInfoById.getBorrowId());
		
		return code != 0?true:false;
	}
	
	//按条件查询数量
	@Override
	public int getBackInfoCountByCondition(String iSBN, String paperNO, int borrowId) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from backinfo ba ,borrowinfo bo,book bk,reader r "
				+"where ba.borrowId=bo.borrowId and bk.bookId=bo.bookId and bo.readerId=r.readerId ";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);

		if(!"".equals(iSBN.trim())){
			sb.append(" and bk.ISBN like '%" + iSBN +"%'");
		}
		if(!"".equals(paperNO.trim())){
			sb.append(" and r.paperNO like '%" + paperNO +"%'");
		}
		if(borrowId!=0){
			sb.append(" and bo.borrowId like '%" + borrowId +"%'");
		}

		return ((Long) qu.query(DataSourceUtils.getConnection(), sb.toString(), new CscalarHandler(), null)).intValue();
	}
	
	//按条件查询List
	@Override
	public List<BackInfo> getBackfoListByCondition(String iSBN, String paperNO, int borrowId, int pageCode,
			int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select ba.* from backinfo ba ,borrowinfo bo,book bk,reader r "
				+"where ba.borrowId=bo.borrowId and bk.bookId=bo.bookId and bo.readerId=r.readerId ";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		
		if(!"".equals(iSBN.trim())){
			sb.append(" and bk.ISBN like '%" + iSBN +"%'");
		}
		if(!"".equals(paperNO.trim())){
			sb.append(" and r.paperNO like '%" + paperNO +"%'");
		}
		if(borrowId!=0){
			sb.append(" and bo.borrowId like '%" + borrowId +"%'");
		}
		
		sb.append(" order by ba.backDate desc limit ? , ?");

		List<BackInfo> list = qu.query(DataSourceUtils.getConnection(), sb.toString(), new CBeanListHandler<>(BackInfo.class),
				pageSize * (pageCode - 1), pageSize);
		if (list != null && list.size() > 0) {

			for (BackInfo backinfo : list) {
				//查询借阅信息
				sql = "select * from borrowinfo where borrowId = ?";
				BorrowInfo borrowInfo = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(BorrowInfo.class), backinfo.getBorrowId());
				//查询设置图书
				sql = "select * from book where bookId = ?";
				Book book = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Book.class), borrowInfo.getBookId());
				
				sql = "select * from booktype where typeId = ?";
				BookType bookType = qu.query(DataSourceUtils.getConnection(),sql, new CBeanHandler<>(BookType.class), book.getTypeId());
				book.setBookType(bookType);
				borrowInfo.setBook(book);
				
				//查询设置读者信息
				sql = "select * from reader where readerId = ?";
				Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class), borrowInfo.getReaderId());
				sql = "select * from readertype where readerTypeId = ?";
				ReaderType readerType = qu.query(DataSourceUtils.getConnection(),sql, new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
				reader.setReaderType(readerType);
				borrowInfo.setReader(reader);
				
				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(),sql, new CBeanHandler<>(Admin.class), backinfo.getAid());
				backinfo.setAdmin(admin);
				//设置借阅信息
				backinfo.setBorrowInfo(borrowInfo);
			}
			return list;
		}

		return null;
	}
	//删除归还信息
	@Override
	public boolean deleteBackInfo(BackInfo backInfo) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "delete from backinfo where borrowId = ?";
		int code = qu.update(sql, backInfo.getBorrowId());
		return code!=0?true:false;
	}

}
