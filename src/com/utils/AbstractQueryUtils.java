package com.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

public abstract class AbstractQueryUtils {

	protected final DataSource ds;
	
	public AbstractQueryUtils(){
		this.ds = null;
	}
	
	public AbstractQueryUtils(DataSource ds){
		this.ds = ds;
	}
	
	public DataSource getDataSource(){
		return this.ds;
	}
	
	
	protected Connection prepareConnection() throws SQLException{
		if(this.getDataSource()==null){
			throw new SQLException("查询工具类需要传入一个连接池或者一个连接");
		}
		return this.getDataSource().getConnection();
	} 
	
	protected void close(Connection conn) throws SQLException {
       DataSourceUtils.closeConnection(conn);
    }
	
	protected void fillStatement(PreparedStatement pstmt,Object... params) throws SQLException{
		
		if(params == null){
			return;
		}
		for (int i = 0; i < params.length; i++) {
			if(params[i]!=null){
				pstmt.setObject(i+1, params[i]);
			}
			
		}
	}
	
	
}
