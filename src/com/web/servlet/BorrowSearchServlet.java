package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.BackInfo;
import com.entity.PageBean;
import com.service.BackService;
import com.utils.BeanFactory;

/**
 * 借阅查询
 */
public class BorrowSearchServlet extends BaseServlet {

	private static final long serialVersionUID = -6267838310943146575L;
	private static final BackService backService = (BackService) BeanFactory.getBean("BackService");
	//分页查找借阅信息
	public String findBackInfoByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;

		PageBean<BackInfo> pb = backService.findBackInfoByPage(pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findBackInfoByPage&");
		}
		// 存入request域中
		request.setAttribute("pb", pb);
		return "/WEB-INF/admin/borrowSearch.jsp";
	}
	
	//分页查找借阅信息
		public String queryBorrowSearchInfo(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException, SQLException {

			int pageCode = Integer.parseInt(request.getParameter("pageCode"));
			//获取页面传递过来的当前页码数
			if(pageCode==0){
				pageCode = 1;
			}
			//给pageSize,每页的记录数赋值
			int pageSize = 3;
			
			String ISBN = request.getParameter("ISBN");
			String paperNO = request.getParameter("paperNO");
			String parameter = request.getParameter("borrowId");
			int borrowId = 1;
			if(parameter!=null && parameter.length()>0){
				
				 borrowId = Integer.parseInt(parameter);
			}
			
			PageBean<BackInfo> pb = null;
			if("".equals(ISBN.trim()) && "".equals(paperNO.trim()) && borrowId==0){
				pb = backService.findBackInfoByPage(pageCode,pageSize);
			}else{
				pb = backService.queryBackInfo(ISBN,paperNO,borrowId,pageCode,pageSize);
			}
			if(pb!=null){
				pb.setUrl("queryBorrowSearchInfo&ISBN="+ISBN+"&paperNO="+paperNO+"&borrowId="+borrowId+"&");
			}

			request.setAttribute("pb", pb);
			return "/WEB-INF/admin/borrowSearch.jsp";
		}
		

}
