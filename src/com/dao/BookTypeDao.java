package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.entity.BookType;

public interface BookTypeDao {

	List<BookType> getAllBookTypes() throws SQLException;

}
