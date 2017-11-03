package com.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.dao.AdminDao;
import com.dao.ForfeitDao;
import com.entity.Admin;
import com.entity.Authorization;
import com.entity.BorrowInfo;
import com.entity.ForfeitInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.entity.ReaderType;
import com.service.AdminService;
import com.utils.BeanFactory;
import com.utils.DataSourceUtils;

public class AdminServiceImpl implements AdminService {

	private static final AdminDao adminDao = (AdminDao) BeanFactory.getBean("AdminDao");
	private static final ForfeitDao forfeitDao = (ForfeitDao) BeanFactory.getBean("ForfeitDao");

	/**
	 * 根据用户名和密码查询管理员
	 * 
	 * @throws SQLException
	 */
	@Override
	public Admin getAdminByUsernameAndPwd(String username, String pwd) throws SQLException {
		Admin admin = null;
		try {
			DataSourceUtils.startTransaction();

			admin = adminDao.getAdminByUsernameAndPwd(username, pwd);
			if (admin != null) {
				Authorization authodrization = adminDao.getAuthorizationByAid(admin.getAid());
				admin.setAuthorization(authodrization);
			}

			DataSourceUtils.commitAndClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return admin;
	}

	@Override
	public void updatePwd(Admin admin, String newPwd) throws SQLException {
		adminDao.updatePwd(admin, newPwd);
	}

	@Override
	public boolean updateInfo(Admin admin, String name, String phone, String username) throws SQLException {
		return adminDao.updateInfo(admin, name, phone, username);
	}

	/**
	 * 查询所有读者类型
	 */
	@Override
	public List<ReaderType> getAllReaderType() throws SQLException {
		return adminDao.getAllReaderType();
	}

	/**
	 * 根据ID查询读者类型
	 */
	@Override
	public ReaderType getReaderType(int id) throws SQLException {

		return adminDao.getReaderType(id);
	}

	// 更新读者类型
	@Override
	public boolean updateReaderType(int readerTypeId, int maxNum, int bday, double penalty, String readerTypeName,
			int renewDays) throws SQLException {
		boolean flag = false;
		try {
			DataSourceUtils.startTransaction();
			// 先获取在更新
			ReaderType readerType = adminDao.getReaderType(readerTypeId);
			readerType.setBday(bday);
			readerType.setMaxNum(maxNum);
			readerType.setPenalty(penalty);
			readerType.setReaderTypeName(readerTypeName);
			readerType.setRenewDays(renewDays);

			flag = adminDao.updateReaderType(readerType);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {

			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
		}

		return flag;
	}

	/**
	 * 分页查找
	 */
	@Override
	public PageBean<Reader> findReaderByPage(int pageCode, int pageSize) throws SQLException {
		PageBean<Reader> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = adminDao.getReaderPageCount();
			page.setTotalRecord(count);
			List<Reader> list = adminDao.getReaderList(pageCode, pageSize);
			page.setBeanList(list);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}

		return page;
	}

	// 根据Id获取读者
	@Override
	public Reader getReaderById(String readerId) throws SQLException {
		return adminDao.getReaderById(readerId);
	}

	// 删除读者
	@Override
	public int deleteReader(String readerId) throws SQLException {
			//1先根据读者ID查询借阅信息
			List<BorrowInfo> borrowInfos = adminDao.getBorrowInfoByReaderId(readerId);
			//2遍历判断
			for (BorrowInfo borrowInfo : borrowInfos) {
				if(borrowInfo.getState()==0 ){
					return -1;//有尚未归还的书籍
				}else if(borrowInfo.getState()==1 || borrowInfo.getState()==4){
				
				//得到该借阅记录的罚金信息
				ForfeitInfo forfeitInfo = new ForfeitInfo();
				forfeitInfo.setBorrowId(borrowInfo.getBorrowId());
				ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
				if(forfeitInfoById!=null){
					if(forfeitInfoById.getIsPay()==0){
						return -2;//尚未缴纳的罚款
					}
				}
				}
			}
			boolean deleteReader = adminDao.deleteReader(readerId);
			if(deleteReader){
				return 1;
			}
		
		return 0;
	}

	/**
	 * 修改读者信息
	 * @param updateReader
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean updateReaderInfo(Reader updateReader) throws SQLException {
		return adminDao.updateReaderInfo(updateReader);
	}

	/**
	 * 根据证件好获取读者
	 */
	@Override
	public Reader getReaderBypaperNO(String paperNO) throws SQLException {
		
		return adminDao.getReaderBypaperNO(paperNO);
	}

	/**
	 * 添加读者
	 */
	@Override
	public boolean addReader(Reader reader) throws SQLException {
		return adminDao.addReader(reader);
	}

	/**
	 * 按条件查询读者
	 */
	@Override
	public PageBean<Reader> queryReader(Reader reader, int pageCode, int pageSize) throws SQLException {
		PageBean<Reader> page = null;
		try {
			DataSourceUtils.startTransaction();
			page = new PageBean<>();
			page.setPageCode(pageCode);
			page.setPageSize(pageSize);

			int count = adminDao.queryReaderPageCount(reader);
			page.setTotalRecord(count);
			List<Reader> list = adminDao.queryReaderList(reader,pageCode, pageSize);
			page.setBeanList(list);
			DataSourceUtils.commitAndClose();

		} catch (SQLException e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		
		return page;
	}

}
