package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.Book;

public interface BookDao {

	int getBookPageCount() throws SQLException;

	List<Book> getBookList(int pageCode, int pageSize) throws SQLException;

	Book getBookById(Book book) throws SQLException;

	boolean updateBookInfo(Book updateBook) throws SQLException;

	boolean addBook(Book book) throws SQLException;

	boolean deleteBook(Book book) throws SQLException;

	int queryBookPageCount(Book book) throws SQLException;

	List<Book> queryReaderList(Book book, int pageCode, int pageSize) throws SQLException;

	Book getBookByISBN(Book book) throws SQLException;

}
