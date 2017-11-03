package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Admin;
import com.entity.Authorization;
import com.entity.BackInfo;
import com.entity.BorrowInfo;
import com.entity.PageBean;
import com.service.BackService;
import com.utils.BeanFactory;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * 图书归还
 */
public class BackManageServlet extends BaseServlet {

	private static final long serialVersionUID = 7480003198227117010L;
	private static final BackService backService = (BackService) BeanFactory.getBean("BackService");

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

		return "/WEB-INF/admin/backManage.jsp";
	}

	/**
	 * 根据ID获取归还信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String getBackInfoById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		response.setContentType("application/json;charset=utf-8");
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));

		BackInfo backInfo = new BackInfo();
		backInfo.setBorrowId(borrowId);

		BackInfo newBackInfo = backService.getBackInfoById(backInfo);
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

		JSONObject jsonObject = JSONObject.fromObject(newBackInfo, jsonConfig);
		response.getWriter().print(jsonObject);
		return null;
	}

	/**
	 * 还书
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String backBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 还书的步骤
		/*
		 * 1. 获得操作的借阅编号 2. 获得当前的管理员 3. 获得借阅的书籍 3.1 书籍的在馆数量增加 4. 获取当前时间 5.
		 * 设置操作管理员 6. 设置归还时间 7. 设置归还的状态 8. 设置借阅的状态
		 */
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));
		BackInfo backInfo = new BackInfo();
		backInfo.setBorrowId(borrowId);

		backInfo.setAdmin(admin);
		backInfo.setAid(admin.getAid());

		BorrowInfo borrowInfo = new BorrowInfo();
		borrowInfo.setBorrowId(borrowId);
		backInfo.setBorrowInfo(borrowInfo);

		int success = backService.addBackInfo(backInfo);
		response.getWriter().print(success);
		return null;
	}

	//查询归还信息
	public String queryBackInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;
		String ISBN = request.getParameter("ISBN");
		String paperNO = request.getParameter("paperNO");
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));
		
		
		PageBean<BackInfo> pb = null;
		if ("".equals(ISBN.trim()) && "".equals(paperNO.trim()) && borrowId == 0) {
			pb = backService.findBackInfoByPage(pageCode, pageSize);
		} else {
			pb = backService.queryBackInfo(ISBN, paperNO, borrowId, pageCode, pageSize);
		}
		if (pb != null) {
			pb.setUrl("queryBackInfo&ISBN=" + ISBN + "&paperNO=" + paperNO + "&borrowId=" + borrowId + "&");
		}

		request.setAttribute("pb", pb);
		return "/WEB-INF/admin/backManage.jsp";
	}

}
