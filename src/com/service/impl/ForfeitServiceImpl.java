package com.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.BorrowInfoDao;
import com.dao.ForfeitDao;
import com.entity.BackInfo;
import com.entity.BorrowInfo;
import com.entity.ForfeitInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.service.ForfeitService;
import com.utils.BeanFactory;
import com.utils.DataSourceUtils;

public class ForfeitServiceImpl implements ForfeitService {

	private static final ForfeitDao forfeitDao = (ForfeitDao) BeanFactory.getBean("ForfeitDao");
	private static final BorrowInfoDao borrowInfoDao = (BorrowInfoDao) BeanFactory.getBean("BorrowInfoDao");
	
	
	@Override
	public PageBean<ForfeitInfo> findForfeitInfoByPage(int pageCode, int pageSize) throws SQLException {
		PageBean<ForfeitInfo> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = forfeitDao.getForfeitInfoCount();
			page.setTotalRecord(count);
			List<ForfeitInfo> list = forfeitDao.getForfeitInfoList(pageCode, pageSize);
			page.setBeanList(list);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		return page;
	}

	@Override
	public ForfeitInfo getForfeitInfoById(ForfeitInfo forfeitInfo) throws SQLException {

		return forfeitDao.getForfeitInfoById(forfeitInfo);
	}

	@Override
	public int payForfeit(ForfeitInfo forfeitInfo) throws SQLException {

		// 支付罚金步骤
		/*
		 * 1. 得到借阅记录
		 * 
		 * 2. 查看当前的借阅状态 2.1 如果当前状态为未归还(逾期未归还,借阅逾期未归还),则提示读者先去还书再来缴纳罚金,返回-1 2.2
		 * 如果当前状态为归还,则继续下一步
		 * 
		 * 3. 获得当前的管理员
		 * 
		 * 4. 为当前罚金记录进行设置为已支付并设置管理员
		 * 
		 * 5. 修改罚金记录
		 */
		// 得到借阅记录
		BorrowInfo info = new BorrowInfo();
		info.setBorrowId(forfeitInfo.getBorrowId());
		BorrowInfo borrowInfoById = borrowInfoDao.getBorrowInfoById(info);
		// 查看当前的借阅状态
		int state = borrowInfoById.getState();
		if (state == 1 || state == 4) {
			// 如果当前状态为未归还(逾期未归还,借阅逾期未归还),则提示读者先去还书再来缴纳罚金,返回-1
			return -1;
		}
		// 得到当前罚金
		ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
		// 如果当前罚金状态为已支付
		if (forfeitInfoById.getIsPay() == 1) {
			// 提示已经缴纳罚金了
			return -2;
		}
		// 为当前罚金记录进行设置为已支付并设置管理员
		forfeitInfoById.setAdmin(forfeitInfo.getAdmin());
		forfeitInfoById.setIsPay(1);
		forfeitInfoById.setAid(forfeitInfo.getAdmin().getAid());
		// 修改罚金记录
		boolean flag = forfeitDao.updateForfeitInfo(forfeitInfoById);
		if (flag) {
			return 1;
		}
		return 0;
	}

	//按条件查询逾期信息
	@Override
	public PageBean<ForfeitInfo> queryForfeitInfo(String iSBN, String paperNO, int borrowId, int pageCode, int pageSize)
			throws SQLException {

		PageBean<ForfeitInfo> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = forfeitDao.getForfeitInfoCountByCondition(iSBN,paperNO,borrowId);
			if(count > 0){
				page.setTotalRecord(count);
				List<ForfeitInfo> list = forfeitDao.getForfeitInfoListByCondition(iSBN,paperNO,borrowId,pageCode, pageSize);
				page.setBeanList(list);
			}else{
				page.setTotalRecord(0);
				page.setBeanList(null);
				return null;
			}
			
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		return page;
	}

	@Override
	public PageBean<ForfeitInfo> findMyForfeitInfoByPage(Reader reader, int pageCode, int pageSize)
			throws SQLException {
		String iSBN = "";
		int borrowId = 0;
		String paperNO = reader.getPaperNO();
		return queryForfeitInfo(iSBN, paperNO, borrowId, pageCode, pageSize);
	}

}
