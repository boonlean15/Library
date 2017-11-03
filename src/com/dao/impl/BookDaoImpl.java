package com.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BookDao;
import com.entity.Admin;
import com.entity.Book;
import com.entity.BookType;
import com.entity.Reader;
import com.entity.ReaderType;
import com.utils.CBeanHandler;
import com.utils.CBeanListHandler;
import com.utils.CscalarHandler;
import com.utils.DataSourceUtils;
import com.utils.QueryUtils;
import com.utils.RandomUtils;

public class BookDaoImpl implements BookDao {

	/**
	 * 获取书数量
	 */
	@Override
	public int getBookPageCount() throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from book";

		return ((Long) qu.query(DataSourceUtils.getConnection(), sql, new CscalarHandler(), null)).intValue();
	}

	/**
	 * 获取书集合
	 */
	@Override
	public List<Book> getBookList(int pageCode, int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from book order by putdate desc limit ? , ?";
		List<Book> list = qu.query(DataSourceUtils.getConnection(), sql, new CBeanListHandler<>(Book.class),
				pageSize * (pageCode - 1), pageSize);

		if (list != null && list.size() > 0) {

			for (Book book : list) {
				sql = "select * from booktype where typeId = ?";
				BookType bookType = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(BookType.class), book.getTypeId());
				book.setBookType(bookType);
				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						book.getAid());
				book.setAdmin(admin);
			}
			return list;
		}

		return null;
	}

	//根据图书ID获取图书
	@Override
	public Book getBookById(Book book) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from book where bookId = ?";
		Book newBook = qu.query(sql, new CBeanHandler<>(Book.class), book.getBookId());
		
		sql = "select * from booktype where typeId = ?";
		BookType bookType = qu.query(DataSourceUtils.getConnection(), sql,
				new CBeanHandler<>(BookType.class), newBook.getTypeId());
		newBook.setBookType(bookType);
		
		sql = "select * from admin where aid = ?";
		Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
				newBook.getAid());
		newBook.setAdmin(admin);
		return newBook;
	}

	/**
	 * 修改图书信息
	 */
	@Override
	public boolean updateBookInfo(Book book) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql ="update book set bookName = ? , ISBN = ? , autho = ? , press = ? , price = ? , description = ? , typeId = ? , currentNum = ? where bookId = ?";
		int code = qu.update(sql, book.getBookName(),book.getISBN(),book.getAutho(),book.getPress(),book.getPrice(),
				book.getDescription(),book.getTypeId(),book.getCurrentNum(),book.getBookId());
		return code != 0?true:false;
	}

	/**
	 * 添加图书
	 */
	@Override
	public boolean addBook(Book book) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String countsql = "select count(*) from book";
		int count = ((Long)qu.query(countsql, new CscalarHandler(), null)).intValue();
		
		String sql = "insert into book values(?,?,?,?,?,?,?,?,?,?,?,?)";
		int code = qu.update(sql, count+2,book.getBookName(),book.getISBN(),book.getAutho(),book.getNum(),
				book.getCurrentNum(),book.getPress(),book.getDescription(),book.getPrice(),book.getPutdate(),book.getTypeId(),
				book.getAid());
		
		return code!=0?true:false;
	}

	//删除图书
	@Override
	public boolean deleteBook(Book book) throws SQLException {
		QueryUtils qu = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "delete from book where bookId = ?";
			
		int code = qu.update(sql, book.getBookId());
		return code!=0?true:false;
	}

	/**
	 * 按条件查询数量
	 */
	@Override
	public int queryBookPageCount(Book book) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select count(*) from book where 1=1";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);

		if (!"".equals(book.getISBN().trim())) {
			sb.append(" and ISBN like '%" + book.getISBN() + "%'");
		}
		if (!"".equals(book.getBookName().trim())) {
			sb.append(" and bookName like '%" + book.getBookName() + "%'");
		}
		if (!"".equals(book.getAutho().trim())) {
			sb.append(" and autho like '%" + book.getAutho() + "%'");
		}
		if (!"".equals(book.getPress().trim())) {
			sb.append(" and press like '%" + book.getPress() + "%'");
		}
		if (book.getTypeId() != -1) {
			sb.append(" and typeId = " + book.getTypeId());
		}

		return ((Long) qu.query(DataSourceUtils.getConnection(), sb.toString(), new CscalarHandler(), null)).intValue();
	}

	/**
	 * 按条件查询图书
	 */
	@Override
	public List<Book> queryReaderList(Book book, int pageCode, int pageSize) throws SQLException {
		QueryUtils qu = new QueryUtils();
		String sql = "select * from book where 1=1";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		if (!"".equals(book.getISBN().trim())) {
			sb.append(" and ISBN like '%" + book.getISBN() + "%'");
		}
		if (!"".equals(book.getBookName().trim())) {
			sb.append(" and bookName like '%" + book.getBookName() + "%'");
		}
		if (!"".equals(book.getAutho().trim())) {
			sb.append(" and autho like '%" + book.getAutho() + "%'");
		}
		if (!"".equals(book.getPress().trim())) {
			sb.append(" and press like '%" + book.getPress() + "%'");
		}
		if (book.getTypeId() != -1) {
			sb.append(" and typeId = " + book.getTypeId());
		}
		sb.append(" order by putdate desc limit ? , ?");

		List<Book> list = qu.query(DataSourceUtils.getConnection(), sb.toString(), new CBeanListHandler<>(Book.class),
				pageSize * (pageCode - 1), pageSize);
		if (list != null && list.size() > 0) {

			for (Book newbook : list) {
				sql = "select * from booktype where typeId = ?";
				BookType bookType = qu.query(DataSourceUtils.getConnection(), sql,
						new CBeanHandler<>(BookType.class), newbook.getTypeId());
				newbook.setBookType(bookType);
				sql = "select * from admin where aid = ?";
				Admin admin = qu.query(DataSourceUtils.getConnection(), sql, new CBeanHandler<>(Admin.class),
						newbook.getAid());
				newbook.setAdmin(admin);
			}
			return list;
		}

		return null;
	}

	@Override
	public Book getBookByISBN(Book book) throws SQLException {
		QueryUtils qr = new QueryUtils(DataSourceUtils.getDataSource());
		String sql = "select * from book where ISBN = ?";
		Book newBook = qr.query(sql, new CBeanHandler<>(Book.class), book.getISBN());
		if(newBook != null && newBook.getBookName().length()>0){
			sql = "select * from booktype where typeId = ?";
			BookType bookType = qr.query(sql, new CBeanHandler<>(BookType.class), newBook.getTypeId());
			newBook.setBookType(bookType);
		}
		
		
		return newBook;
	}
	

}
