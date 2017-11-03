package com.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;


/**
 * 查询工具类
 * @author hasee
 *
 */
public class QueryUtils extends AbstractQueryUtils{


	//没有传入连接池，调用者自己关闭连接
	public QueryUtils(){
		super();
	}
	//传入了连接池，我们帮忙关闭连接
	public QueryUtils(DataSource ds){
		super(ds);
	}
	//传入连接则调用者自己关闭连接
	public <T> T query(Connection conn,String sql,ResultSetHandlerItfs<T> rsh,Object... params) throws SQLException{
		return this.query(conn, false, sql, rsh, params);
	}
	//没有传入连接，传入了连接池，处理关闭连接
	public <T> T query(String sql,ResultSetHandlerItfs<T> rsh,Object... params) throws SQLException{
		Connection conn = this.prepareConnection();
		return this.query(conn, true, sql, rsh, params);
	}
	
	
	
	private <T> T query(Connection conn,boolean closeConn,String sql,ResultSetHandlerItfs<T> rsh,Object... params) throws SQLException{
		 if (conn == null) {
	            throw new SQLException("Null connection");
	        }

	        if (sql == null) {
	            if (closeConn) {
	                close(conn);
	            }
	            throw new SQLException("Null SQL statement");
	        }

	        if (rsh == null) {
	            if (closeConn) {
	                close(conn);
	            }
	            throw new SQLException("Null ResultSetHandler");
	        }
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        T result = null;
		
	        try {
	        	pstmt = conn.prepareStatement(sql);
	        	this.fillStatement(pstmt, params);
	        	rs = pstmt.executeQuery();
	        	result = rsh.hander(rs);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				DataSourceUtils.closeResource(pstmt, rs);
				if(closeConn){
					close(conn);
				}
			}
		
		return result;
	}
	
	public int update(Connection conn,String sql,Object... params) throws SQLException{
		return this.update(conn, false, sql, params);
	}
	public int update(String sql,Object...params) throws SQLException{
		Connection conn = this.prepareConnection();
		return this.update(conn, true, sql, params);
	}
	
	
	private int update(Connection conn,boolean closeConn,String sql,Object... params) throws SQLException{
		
		if(conn == null){
			throw new SQLException("Null connection");
		}
		if(sql == null){
			if(closeConn){
				close(conn);
			}
			throw new SQLException("Null sql");
		}
		
		PreparedStatement pstmt = null;
		int row = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			this.fillStatement(pstmt, params);
			row = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DataSourceUtils.closeStatement(pstmt);
			if(closeConn){
				close(conn);
			}
		}
		return row;
	}
	
}
