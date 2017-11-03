package com.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * 连接池工具类
 * @author hasee
 *
 */
public class DataSourceUtils {
	//成员变量，连接池和ThreadLocal
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	private static ThreadLocal<Connection> tl = new ThreadLocal<>();
	/**
	 * 获取数据源
	 */
	public static DataSource getDataSource(){
		return dataSource;
	}
	
	/**
	 * 获取连接
	 */
	public static Connection getConnection() throws SQLException{
		Connection conn = tl.get();
		if(conn == null){
			conn = dataSource.getConnection();
			
			tl.set(conn);
			
		}
		return conn;
	}
	/**
	 * 关闭结果集
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs= null;
		}
	}
	/**
	 * 关闭Statement
	 * @param st
	 */
	public static void closeStatement(Statement st){
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			st = null;
		}
	}
	/**
	 * 关闭连接
	 */
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
				tl.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	
	/**
	 * 关闭资源
	 * @param st
	 * @param rs
	 */
	public static void closeResource(Statement st,ResultSet rs){
		closeResultSet(rs);
		closeStatement(st);
	}
	/**
	 * 关闭资源
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void closeResource(Connection conn,Statement st,ResultSet rs){
		closeConnection(conn);
		closeResource(st, rs);
	}
	/**
	 * 开启事物
	 * @throws SQLException
	 */
	public static void startTransaction() throws SQLException{
		getConnection().setAutoCommit(false);
	}	
	/**
	 * 提交并关闭连接
	 */
	public static void commitAndClose(){
		Connection conn = null;
		try {
			conn = getConnection();
			conn.commit();
			conn.close();
			tl.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 事物回滚并关闭连接
	 */
	public static void rollbackAndClose(){
		Connection conn  = null;
		try {
			conn.rollback();
			conn.close();
			tl.remove();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
