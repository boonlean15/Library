package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Admin;
import com.entity.Authorization;
import com.entity.ForfeitInfo;
import com.entity.PageBean;
import com.service.BorrowService;
import com.service.ForfeitService;
import com.utils.BeanFactory;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * 逾期管理
 */
public class ForfeitManageServlet extends BaseServlet {
	private static final long serialVersionUID = 3582015563969487750L;
	private static final ForfeitService forfeitService = (ForfeitService) BeanFactory.getBean("ForfeitService");

	public String findForfeitInfoByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 获取页面传递过来的当前页码数

		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;

		PageBean<ForfeitInfo> pb = forfeitService.findForfeitInfoByPage(pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findForfeitInfoByPage&");
		}
		// 存入request域中
		request.setAttribute("pb", pb);

		return "/WEB-INF/admin/forfeitManage.jsp";
	}

	/**
	 * 根据ID获取罚款信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String getForfeitInfoById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		response.setContentType("application/json;charset=utf-8");
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));

		ForfeitInfo forfeitInfo = new ForfeitInfo();
		forfeitInfo.setBorrowId(borrowId);

		ForfeitInfo newForfeitInfo = forfeitService.getForfeitInfoById(forfeitInfo);
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
		JSONObject jsonObject = JSONObject.fromObject(newForfeitInfo, jsonConfig);
		response.getWriter().print(jsonObject);

		return null;
	}

	/**
	 * 根据ID获取罚款信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String payForfeit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
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
		int borrowId = Integer.parseInt(request.getParameter("borrowId"));
		ForfeitInfo forfeitInfo = new ForfeitInfo();
		forfeitInfo.setBorrowId(borrowId);
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		forfeitInfo.setAdmin(admin);
		forfeitInfo.setAid(admin.getAid());
		int pay = forfeitService.payForfeit(forfeitInfo);
		response.getWriter().print(pay);
		return null;

	}

	/**
	 * 根据ID获取罚款信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String queryForfeitInfo(HttpServletRequest request, HttpServletResponse response)
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
		int borrowId = 0;
		if(parameter!=null && parameter.length()>0){
			
			 borrowId = Integer.parseInt(parameter);
		}
		PageBean<ForfeitInfo> pb = null;
		if ("".equals(ISBN.trim()) && "".equals(paperNO.trim()) && borrowId == 0) {
			pb = forfeitService.findForfeitInfoByPage(pageCode, pageSize);
		} else {
			pb = forfeitService.queryForfeitInfo(ISBN, paperNO, borrowId, pageCode, pageSize);
		}
		if (pb != null) {
			pb.setUrl("queryForfeitInfo&ISBN=" + ISBN + "&paperNO=" + paperNO + "&borrowId=" + borrowId + "&");
		}

		request.setAttribute("pb", pb);

		return "/WEB-INF/admin/forfeitManage.jsp";
	}
}
