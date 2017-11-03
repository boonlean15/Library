package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Reader;
import com.service.BorrowService;
import com.service.ReaderService;
import com.utils.BeanFactory;
import com.utils.BeanUtils;

/**
 * 读者ReaderServlet，处理读者登录注册等操作
 */
public class ReaderServlet extends BaseServlet {
	

	private static final long serialVersionUID = -768214072857969945L;
	private static final ReaderService readerService = (ReaderService) BeanFactory.getBean("ReaderService");
	private static final BorrowService borrowService = (BorrowService) BeanFactory.getBean("BorrowService");
	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		Reader reader = new Reader();

		Map<String, String[]> parameterMap = request.getParameterMap();
		BeanUtils.mapToBean(reader, parameterMap);

		Reader newReader = readerService.getReaderByUsernameAndPwd(reader);
		int login = 0;
		// System.out.println(newReader);
		if (newReader != null) {
			login = 1;
			request.getSession().setAttribute("reader", newReader);
			borrowService.checkBorrowInfo();
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(login);
		return null;
	}

	/**
	 * 退出
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath() + "/reader.jsp");
		return null;
	}
	/**
	 * 修改读者密码
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public String updatePwd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		//oldPwd newPwd confirmPwd 1修改成功 0确认密码不一致 -1原密码错误 其他值：修改失败
		
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		String confirmPwd = request.getParameter("confirmPwd");
		
		int code = -2;
		Reader reader = (Reader) request.getSession().getAttribute("reader");
		String pwd = reader.getPwd();
		
		//判断密码是否和提交的oldPwd相同
		if(!pwd.equals(oldPwd)){
			code = -1;
		}else if(!newPwd.equals(confirmPwd)){
			code = 0;
		}else{
			//更新密码
			readerService.updatePwd(reader,newPwd);
			reader.setPwd(confirmPwd);
			code = 1;
			request.getSession().setAttribute("reader", reader);
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(code);
		
		return null;
	}
	
	/**
	 * 修改读者信息
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public String updateInfo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String name = request.getParameter("name");
//		System.out.println(name);
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		Reader reader = (Reader) request.getSession().getAttribute("reader");
		
		boolean flag  = readerService.updateInfo(reader,name,phone,email);
		int code = 0;
		if(flag){
			code = 1;
			reader.setName(name);
			reader.setPhone(phone);
			reader.setEmail(email);
			request.getSession().setAttribute("reader", reader);
		}
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(code);
		
		return null;
	}
	
	
	
}
