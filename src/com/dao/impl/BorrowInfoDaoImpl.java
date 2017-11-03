package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BorrowInfoDao;
import com.entity.Admin;
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

/**
 * 借阅信息Dao
 * @author hasee
 *
 */
public class BorrowInfoDaoImpl implements BorrowInfoDao {

	
	
	@Override
	public int getBorrowInfoCount() throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from borrowinfo";

		return ((Long) qu.query(DataSourceUtils.getConnection(), sql, new CscalarHandler(), null)).intValue();
	}

	@Override
	public List<BorrowInfo> getBorrowInfoList(int pageCode, int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from borrowinfo order by borrowDate desc limit ? , ?";
		List<BorrowInfo> list = qu.query(DataSourceUtils.getConnection(), sql, new CBeanListHandler<>(BorrowInfo.class),
				pageSize * (pageCode - 1), pageSize);
		//遍历借阅信息
		if (list != null && list.size() > 0) {

			for (BorrowInfo borrowinfo : list) {
				//查询借阅的书籍
				sql = "select * from book where bookId = ?";
				Book book = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(Book.class), borrowinfo.getBookId());
				borrowinfo.setBook(book);
				//查询操作的管理员
				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						borrowinfo.getAid());
				borrowinfo.setAdmin(admin);
				//查询借阅信息的借阅人
				sql = "select * from reader where readerId = ?";
				Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class), borrowinfo.getReaderId());
				borrowinfo.setReader(reader);
				//查询借阅人的读者类型
				sql = "select * from readertype where readerTypeId = ?";
				ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
				reader.setReaderType(readerType);
			}
			return list;
		}

		return null;
	}

	/**
	 * 根据ID获取借阅信息
	 */
	@Override
	public BorrowInfo getBorrowInfoById(BorrowInfo info) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from borrowinfo where borrowId = ?";
		
		BorrowInfo borrowinfo = qu.query(sql, new CBeanHandler<>(BorrowInfo.class), info.getBorrowId());
		
		//查询借阅的书籍
		sql = "select * from book where bookId = ?";
		Book book = qu.query(DataSourceUtils.getConnection(), sql,
				new CBeanHandler<>(Book.class), borrowinfo.getBookId());
		borrowinfo.setBook(book);
		
		//查询借阅图书类型
		sql = "select * from booktype where typeId = ?";
		BookType bookType = qu.query(sql, new CBeanHandler<>(BookType.class), book.getTypeId());
		book.setBookType(bookType);
		
		//查询操作的管理员
		sql = "select * from admin where aid = ?";
		Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
				borrowinfo.getAid());
		borrowinfo.setAdmin(admin);
		
		//查询借阅信息的借阅人
		sql = "select * from reader where readerId = ?";
		Reader reader = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Reader.class), borrowinfo.getReaderId());
		borrowinfo.setReader(reader);
		
		//查询借阅人的读者类型
		sql = "select * from readertype where readerTypeId = ?";
		ReaderType readerType = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(ReaderType.class), reader.getReaderTypeId());
		reader.setReaderType(readerType);
		
		return borrowinfo;
	}

	/**
	 * 查询未归换借阅信息
	 */
	@Override
	public List<BorrowInfo> getNoBackBorrowInfoByReader(Reader reader) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from borrowinfo b where (b.state=0 or b.state=1 or b.state=3 or b.state=4) and b.readerId=?";
		List<BorrowInfo> list = qu.query(sql, new CBeanListHandler<>(BorrowInfo.class), reader.getReaderId());
		if( list != null && list.size()>0){
			return list;
		}
		
		return null;
	}

	/**
	 * 添加借阅信息
	 */
	@Override
	public int addBorrow(BorrowInfo borrowInfo) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String countsql = "select count(*) from borrowinfo";
		int count = ((Long)qu.query(countsql, new CscalarHandler(), null)).intValue();
		
		String sql = "insert into borrowinfo values(?,?,?,?,?,?,?,?,?)";
		int code = qu.update(sql,count+1,borrowInfo.getBorrowDate(),borrowInfo.getEndDate()
				,0,0,borrowInfo.getReaderId(),borrowInfo.getBookId(),borrowInfo.getPenalty(),borrowInfo.getAid());
		if(code != 0){
			return count+1;
		}
		return code;
	}

	/**
	 * 修改借阅信息
	 */
	@Override
	public boolean updateBorrowInfo(BorrowInfo borrowInfoById) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "update borrowinfo set state = ? , aid = ? , endDate = ? , overday = ? where borrowId = ?";
		int code = qu.update(sql, borrowInfoById.getState(),borrowInfoById.getAid(),borrowInfoById.getEndDate(), borrowInfoById.getOverday(),borrowInfoById.getBorrowId());
		
		
		return code != 0?true:false;
	}

	/**
	 * 获取未归还的借阅信息
	 */
	@Override
	public List<BorrowInfo> getBorrowInfoByNoBackState() throws SQLException {
		
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from borrowinfo b where b.state=0 or b.state=1 or b.state=3 or b.state=4";
		List<BorrowInfo> list = qu.query(sql, new CBeanListHandler<>(BorrowInfo.class), null);
		if( list != null && list.size()>0){
			return list;
		}
		
		return null;
	}

}
