package com.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandlerItfs<T> {

	
	
	T hander(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException;
	
	
	
	
	
}
