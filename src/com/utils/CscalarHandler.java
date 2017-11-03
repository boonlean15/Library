package com.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;

public class CscalarHandler implements ResultSetHandlerItfs<Object> {

	private final int columnIndex;
	private final String columnName;

	public CscalarHandler() {
		this(1, null);
	}

	public CscalarHandler(int columnIndex) {
		this(columnIndex, null);
	}
	
	public CscalarHandler(String columnName) {
        this(1, columnName);
    }

	private CscalarHandler(int columnIndex, String columnName) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
	}

	@Override
	public Object hander(ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException {
		
			if (rs.next()) {
				if (this.columnName == null) {
					return rs.getObject(this.columnIndex);
				} else {
					return rs.getObject(this.columnName);
				}

			} else {
				return null;
			}
		
	}

}
