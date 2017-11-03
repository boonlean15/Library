package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.BackInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.service.BackService;
import com.utils.BeanFactory;

/**
 * 读者借阅
 */
public class ReaderborrowServlet extends BaseServlet {
	private static final long serialVersionUID = 777881552894484566L;
	private static final BackService backService = (BackService) BeanFactory.getBean("BackService");

	public String findMyBorrowInfoByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		Reader reader = (Reader) request.getSession().getAttribute("reader");
		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;
		PageBean<BackInfo> pb = null;
		pb = backService.findMyBorrowInfoByPage(reader, pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findMyBorrowInfoByPage&");
		}
		request.setAttribute("pb", pb);
		return "/WEB-INF/reader/borrow.jsp";
	}

	// 按条件查询读者借阅信息
	public String queryBorrowSearchInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;

		// 获取请求参数
		String ISBN = request.getParameter("ISBN");
		String parameter = request.getParameter("borrowId");
		int borrowId = 0;
		if(parameter!=null && parameter.length()>0){
			
		 borrowId = Integer.parseInt(parameter);
		}

		PageBean<BackInfo> pb = null;
		Reader reader = (Reader) request.getSession().getAttribute("reader");
		if ("".equals(ISBN.trim()) && borrowId == 0) {
			backService.findMyBorrowInfoByPage(reader, pageCode, pageSize);
		} else {
			pb = backService.queryBackInfo(ISBN, reader.getPaperNO(), borrowId, pageCode, pageSize);
		}
		if (pb != null) {
			pb.setUrl("queryBorrowSearchInfo&ISBN=" + ISBN + "&borrowId=" + borrowId + "&");
		}

		request.setAttribute("pb", pb);
		return "/WEB-INF/reader/borrow.jsp";
	}

}
