package com.service;

import java.sql.SQLException;

import com.entity.Book;
import com.entity.PageBean;

public interface BookService {

	PageBean<Book> findBookByPage(int pageCode, int pageSize) throws SQLException;

	Book getBookById(Book book) throws SQLException;

	boolean updateBookInfo(Book updateBook) throws SQLException;

	boolean addBook(Book book) throws SQLException;

	int deleteBook(Book book) throws SQLException;

	PageBean<Book> queryBook(Book book, int pageCode, int pageSize) throws SQLException;

}
