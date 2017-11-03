package com.service;

import java.sql.SQLException;
import java.util.List;

import com.entity.BookType;

public interface BookTypeService {

	List<BookType> getAllBookTypes() throws SQLException;

}
