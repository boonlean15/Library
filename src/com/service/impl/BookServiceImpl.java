package com.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BookDao;
import com.dao.BorrowDao;
import com.dao.ForfeitDao;
import com.entity.Book;
import com.entity.BorrowInfo;
import com.entity.ForfeitInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.service.BookService;
import com.utils.BeanFactory;
import com.utils.DataSourceUtils;

public class BookServiceImpl implements BookService {
	private static final BookDao bookDao = (BookDao) BeanFactory.getBean("BookDao");
	private static final BorrowDao borrowDao = (BorrowDao) BeanFactory.getBean("BorrowDao");
	private static final ForfeitDao forfeitDao = (ForfeitDao) BeanFactory.getBean("ForfeitDao");

	/**
	 * 按页码查找Book
	 */
	@Override
	public PageBean<Book> findBookByPage(int pageCode, int pageSize) throws SQLException {
		PageBean<Book> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = bookDao.getBookPageCount();
			page.setTotalRecord(count);
			List<Book> list = bookDao.getBookList(pageCode, pageSize);
			page.setBeanList(list);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		return page;
	}

	/**
	 * 根据图书ID获取图书信息
	 */
	@Override
	public Book getBookById(Book book) throws SQLException {
		return bookDao.getBookById(book);
	}

	/**
	 * 修改图书信息
	 */
	@Override
	public boolean updateBookInfo(Book updateBook) throws SQLException {
		return bookDao.updateBookInfo(updateBook);
	}

	/**
	 * 添加图书
	 */
	@Override
	public boolean addBook(Book book) throws SQLException {
		return bookDao.addBook(book);
	}

	/**
	 * 删除图书
	 */
	@Override
	public int deleteBook(Book book) throws SQLException {
		// 删除图书需要注意的事项：如果该书有尚未归还的记录或者尚未缴纳的罚款记录,则不能删除
		// 得到该书的借阅记录
		List<BorrowInfo> borrowInfos = borrowDao.getBorrowInfoByBook(book);
		for (BorrowInfo borrowInfo : borrowInfos) {
			if (borrowInfo.getState() == 0) {
				return -1;// 有尚未归还的书籍
			} else if (borrowInfo.getState() == 1 || borrowInfo.getState() == 4) {

				// 得到该借阅记录的罚金信息
				ForfeitInfo forfeitInfo = new ForfeitInfo();
				forfeitInfo.setBorrowId(borrowInfo.getBorrowId());
				ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
				if (forfeitInfoById != null) {
					if (forfeitInfoById.getIsPay() == 0) {
						return -2;// 尚未缴纳的罚款
					}
				}
			}
		}
		boolean deleteBook = bookDao.deleteBook(book);
		if (deleteBook) {
			return 1;
		}

		return 0;
	}

	/**
	 * 按条件查询图书
	 */
	@Override
	public PageBean<Book> queryBook(Book book, int pageCode, int pageSize) throws SQLException {
		PageBean<Book> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = bookDao.queryBookPageCount(book);
			page.setTotalRecord(count);
			List<Book> list = bookDao.queryReaderList(book,pageCode, pageSize);
			page.setBeanList(list);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		
		return page;
	}

}
