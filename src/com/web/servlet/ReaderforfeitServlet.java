package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Authorization;
import com.entity.ForfeitInfo;
import com.entity.PageBean;
import com.entity.Reader;
import com.service.ForfeitService;
import com.utils.BeanFactory;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * Servlet implementation class ReaderforfeitServlet
 */
public class ReaderforfeitServlet extends BaseServlet {
	private static final long serialVersionUID = -5628932596634789107L;
	private static final ForfeitService forfeitService = (ForfeitService) BeanFactory.getBean("ForfeitService");

	public String findMyForfeitInfoByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		Reader reader = (Reader) request.getSession().getAttribute("reader");
		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 5;
		PageBean<ForfeitInfo> pb = null;
		pb = forfeitService.findMyForfeitInfoByPage(reader, pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findMyForfeitInfoByPage&");
		}
		request.setAttribute("pb", pb);
		return "/WEB-INF/reader/forfeit.jsp";
	}

	/**
	 * 根据ID查询逾期信息
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
	 * 查询读者逾期信息
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

		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 5;
		
		String ISBN = request.getParameter("ISBN");
		String parameter = request.getParameter("borrowId");
		int borrowId = 0;
		if(parameter!=null && parameter.length()>0){
			
		 borrowId = Integer.parseInt(parameter);
		}
		
		PageBean<ForfeitInfo> pb = null;
		Reader reader = (Reader) request.getSession().getAttribute("reader");
		if ("".equals(ISBN.trim()) && borrowId == 0) {
			pb = forfeitService.findMyForfeitInfoByPage(reader, pageCode, pageSize);
		} else {
			pb = forfeitService.queryForfeitInfo(ISBN, reader.getPaperNO(), borrowId, pageCode, pageSize);
		}
		if (pb != null && pb.getBeanList()!=null && pb.getBeanList().size()>0) {
			pb.setUrl("queryForfeitInfo&ISBN=" + ISBN + "&borrowId=" + borrowId + "&");
		}

		request.setAttribute("pb", pb);
		return "/WEB-INF/reader/forfeit.jsp";
	}

}
