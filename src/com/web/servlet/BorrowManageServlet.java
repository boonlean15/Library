package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Admin;
import com.entity.Authorization;
import com.entity.Book;
import com.entity.BorrowInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.service.BorrowService;
import com.utils.BeanFactory;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * Servlet implementation class BorrowManageServlet
 */
public class BorrowManageServlet extends BaseServlet {

	private static final long serialVersionUID = 3408988643199045682L;
	private static final BorrowService borrowService = (BorrowService) BeanFactory.getBean("BorrowService");

	/**
	 * 根据页码分页查询借阅信息
	 * 
	 * @throws SQLException
	 */
	public String findBorrowInfoByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;

		PageBean<BorrowInfo> pb = borrowService.findBorrowInfoByPage(pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findBorrowInfoByPage&");
		}
		// 存入request域中
		request.setAttribute("pb", pb);
		return "/WEB-INF/admin/borrowManage.jsp";
	}

	/**
	 * 根据借阅id查询该借阅信息
	 * 
	 * @throws SQLException
	 */
	public String getBorrowInfoById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		response.setContentType("application/json;charset=utf-8");
		BorrowInfo info = new BorrowInfo();
		// 获取请求参数
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));
		info.setBorrowId(borrowId);
		BorrowInfo newInfo = borrowService.getBorrowInfoById(info);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object obj, String name, Object value) {
				if (obj instanceof Authorization || name.equals("authorization") || obj instanceof List
						|| name.equals("borrowInfos")) {
					return true;
				} else {
					return false;
				}
			}
		});

		JSONObject jsonObject = JSONObject.fromObject(newInfo, jsonConfig);
		response.getWriter().print(jsonObject);
		System.out.println(jsonObject);
		return null;
	}

	/**
	 * 借书
	 * 
	 * @throws SQLException
	 */
	public String borrowBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 借阅的主要步骤

		/*
		 * 1. 得到借阅的读者
		 * 
		 * 2. 查看读者输入的密码是否匹配 2.1 如果不匹配提示 密码错误 2.2 如果匹配,执行第3步骤
		 * 
		 * 3. 查看该读者的借阅信息 3.1 查看借阅信息的条数 3.2 查看该读者的类型得出该读者的最大借阅数量 3.3
		 * 匹配借阅的数量是否小于最大借阅量 3.3.1 小于,则可以借阅 3.3.2 大于,则不可以借阅,直接返回借阅数量已超过 3.4
		 * 查看读者的罚款信息,是否有未缴纳的罚款 3.4.1 如果没有,继续第3的操作步骤 3.4.2 如果有,直接返回有尚未缴纳的罚金
		 * 
		 * 4. 查看借阅的书籍 4.1 查看该书籍的在馆数量是否大于1,,如果为1则不能借阅,必须留在馆内浏览 4.1.1
		 * 如果大于1,进入第4操作步骤 4.1.2 如果小于等于1,提示该图书为馆内最后一本,无法借阅
		 * 
		 * 5. 处理借阅信息 5.1 得到该读者的最大借阅天数,和每日罚金 5.1.1 获得当前时间 5.1.2 根据最大借阅天数,计算出截止日期
		 * 5.1.3 为借阅信息设置每日的罚金金额 5.2 获得该读者的信息,为借阅信息设置读者信息 5.3 获得图书信息,为借阅信息设置图书信息
		 * 5.4 获得管理员信息,为借阅信息设置操作的管理员信息
		 * 
		 * 6. 存储借阅信息
		 * 
		 */
		String paperNO = request.getParameter("paperNO");
		String ISBN = request.getParameter("ISBN");
		String pwd = request.getParameter("pwd");

		BorrowInfo borrowInfo = new BorrowInfo();
		Reader reader = new Reader();

		reader.setPaperNO(paperNO);
		reader.setPwd(pwd);

		borrowInfo.setReader(reader);

		Admin admin = (Admin) request.getSession().getAttribute("admin");
		borrowInfo.setAdmin(admin);
		borrowInfo.setAid(admin.getAid());

		Book book = new Book();
		book.setISBN(ISBN);
		borrowInfo.setBook(book);

		int addBorrow = borrowService.addBorrow(borrowInfo);
		response.getWriter().print(addBorrow);

		return null;
	}

	// 续借
	public String renewBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 续借步骤：
		/*
		 * 1. 得到借阅的记录
		 * 
		 * 2. 得到借阅的记录的状态 2.1 如果已经是续借状态(包括续借未归还,续借逾期未归还),则返回已经续借过了,返回-2 2.2
		 * 如果是归还状态(包括归还,续借归还),则返回该书已还,无法续借,返回-1 2.3 如果都不是以上情况,则进行下一步。
		 * 
		 * 3. 得到借阅的读者
		 * 
		 * 4. 得到借阅的读者类型
		 * 
		 * 5. 得到可续借的天数
		 * 
		 * 6. 在当前记录的截止日期上叠加可续借天数 6.1
		 * 如果叠加后的时候比当前时间小,则返回你已超续借期了,无法进行续借,提示读者快去还书和缴纳罚金 返回-3 6.2如果大于当前时间进行下一步
		 * 
		 * 7. 得到当前借阅记录的状态 7.1 如果当前记录为逾期未归还,则需要取消当前借阅记录的罚金记录,并将该记录的状态设置为续借未归还
		 * 7.2如果为未归还状态,直接将当前状态设置为续借未归还
		 * 
		 * 8. 为当前借阅记录进行设置,设置之后重新update该记录 返回1
		 */
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));
		
		BorrowInfo borrowInfo = new BorrowInfo();
		borrowInfo.setBorrowId(borrowId);
		int renewBook = borrowService.renewBook(borrowInfo);
		response.getWriter().print(renewBook);

		return null;
	}

}
