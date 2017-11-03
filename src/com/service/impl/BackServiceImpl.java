package com.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.dao.BackDao;
import com.dao.BookDao;
import com.dao.BorrowInfoDao;
import com.entity.BackInfo;
import com.entity.Book;
import com.entity.BorrowInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.service.BackService;
import com.utils.BeanFactory;
import com.utils.DataSourceUtils;

/**
 * 图书归还service
 * 
 * @author hasee
 *
 */
public class BackServiceImpl implements BackService {

	private static BackDao backDao = (BackDao) BeanFactory.getBean("BackDao");

	/**
	 * 按页码查找归还信息
	 */
	@Override
	public PageBean<BackInfo> findBackInfoByPage(int pageCode, int pageSize) throws SQLException {
		PageBean<BackInfo> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = backDao.getBackInfoCount();
			page.setTotalRecord(count);
			List<BackInfo> list = backDao.getBackfoList(pageCode, pageSize);
			page.setBeanList(list);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		return page;
	}

	// 通过Id获取归还信息
	@Override
	public BackInfo getBackInfoById(BackInfo backInfo) throws SQLException {
		return backDao.getBackInfoById(backInfo);
	}

	// 还书
	@Override
	public int addBackInfo(BackInfo backInfo) throws SQLException {
		// 还书的步骤
		/*
		 * 1. 获得操作的借阅编号
		 * 
		 * 2. 获得当前的管理员
		 * 
		 * 3. 获得借阅的书籍 3.1 书籍的在馆数量增加
		 * 
		 * 
		 * 4. 获取当前时间
		 * 
		 * 5. 设置操作管理员
		 * 
		 * 6. 设置归还时间
		 * 
		 * 
		 * 7. 设置借阅的状态 7.1 如果当前借阅不属于续借，则设置为归还 7.2 如果当前借阅属于续借,则设置为续借归还
		 * 
		 * 8. 查看该借阅记录有逾期罚金未缴纳的记录 8.1 如果有，返回状态码2,提示读者去缴费 8.2 如果没有,则结束
		 * 
		 * 
		 * 
		 */
		BorrowInfoDao borrowInfoDao = (BorrowInfoDao) BeanFactory.getBean("BorrowInfoDao");
		BorrowInfo borrowInfoById = borrowInfoDao.getBorrowInfoById(backInfo.getBorrowInfo());// 获得操作的借阅编号
		if (borrowInfoById.getState() == 2 || borrowInfoById.getState() == 5) {// 如果已经归还了。
			return -1;// 该书已还
		}
		borrowInfoById.setAid(backInfo.getAid());
		
		BookDao bookDao = (BookDao) BeanFactory.getBean("BookDao");
		Book book = borrowInfoById.getBook();
		Book bookById = bookDao.getBookById(book);// 获得借阅的书籍
		
		bookById.setCurrentNum(bookById.getCurrentNum() + 1);
		
		boolean b = bookDao.updateBookInfo(bookById);// 书籍的在馆数量增加
		
		Date backDate = new Date(System.currentTimeMillis());// 获取当前时间
		
		BackInfo backInfoById = backDao.getBackInfoById(backInfo);
		
		backInfoById.setAdmin(backInfo.getAdmin());// 设置管理员
		backInfoById.setAid(backInfo.getAid());
		backInfoById.setBackDate(backDate);// 设置归还时间
		
		int state = borrowInfoById.getState();
		boolean ba = false;
		if (b) {
			ba = backDao.updateBackInfo(backInfoById);// 修改归还记录
		}
		if (borrowInfoById.getState() == 0 || borrowInfoById.getState() == 1) {
			borrowInfoById.setState(2);// 设置借阅的状态
		}
		if (borrowInfoById.getState() == 3 || borrowInfoById.getState() == 4) {
			borrowInfoById.setState(5);// 设置借阅的状态
		}
		boolean bi = false;
		if (ba) {
			bi = borrowInfoDao.updateBorrowInfo(borrowInfoById);
		}
		if (bi) {
			if (state == 1 || state == 4) {
				return 2;// 提示读者去缴费
			} else {
				return 1;
			}
		}
		return 0;
	}

	//按条件查询归还信息
	@Override
	public PageBean<BackInfo> queryBackInfo(String iSBN, String paperNO, int borrowId, int pageCode, int pageSize)
			throws SQLException {
		PageBean<BackInfo> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = backDao.getBackInfoCountByCondition(iSBN,paperNO,borrowId);
			if(count > 0){
				page.setTotalRecord(count);
				List<BackInfo> list = backDao.getBackfoListByCondition(iSBN,paperNO,borrowId,pageCode, pageSize);
				page.setBeanList(list);
			}else{
				page.setTotalRecord(0);
				page.setBeanList(null);
			}
			
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		return page;
	}

	//查询我的借阅信息
	@Override
	public PageBean<BackInfo> findMyBorrowInfoByPage(Reader reader, int pageCode, int pageSize) throws SQLException {
		String iSBN = "";
		int borrowId = 0;
		String paperNO = reader.getPaperNO();
		return queryBackInfo(iSBN, paperNO, borrowId, pageCode, pageSize);
	}

}
