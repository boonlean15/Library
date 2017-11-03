package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Admin;
import com.entity.Authorization;
import com.entity.PageBean;
import com.entity.Reader;
import com.entity.ReaderType;
import com.service.AdminService;
import com.service.BorrowService;
import com.utils.BeanFactory;
import com.utils.JsonUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * 管理员Servlet
 */
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = -5219162641346942402L;
	private static final AdminService adminService = (AdminService) BeanFactory.getBean("AdminService");
	private static final BorrowService borrowService = (BorrowService) BeanFactory.getBean("BorrowService");
	/**
	 * 登录
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		// System.out.println(username+"=="+pwd);
		Admin admin = adminService.getAdminByUsernameAndPwd(username, pwd);
		int login = 0;
		if (admin != null) {
			login = 1;
			request.getSession().setAttribute("admin", admin);
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
	 */
	public String logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath() + "/adminLogin.jsp");
		return null;
	}

	/**
	 * 修改管理员密码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String updatePwd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// oldPwd newPwd confirmPwd 1修改成功 0确认密码不一致 -1原密码错误 其他值：修改失败

		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		String confirmPwd = request.getParameter("confirmPwd");

		int code = -2;
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		String pwd = admin.getPwd();

		// 判断密码是否和提交的oldPwd相同
		if (!pwd.equals(oldPwd)) {
			code = -1;
		} else if (!newPwd.equals(confirmPwd)) {
			code = 0;
		} else {
			// 更新密码
			adminService.updatePwd(admin, newPwd);
			admin.setPwd(confirmPwd);
			code = 1;
			request.getSession().setAttribute("admin", admin);
		}

		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(code);

		return null;
	}

	/**
	 * 修改管理员信息
	 * 
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
		// System.out.println(name);
		String phone = request.getParameter("phone");
		String username = request.getParameter("username");
		Admin admin = (Admin) request.getSession().getAttribute("admin");

		boolean flag = adminService.updateInfo(admin, name, phone, username);
		int code = 0;
		if (flag) {
			code = 1;
			admin.setName(name);
			admin.setPhone(phone);
			admin.setUsername(username);
			request.getSession().setAttribute("admin", admin);
		}

		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(code);

		return null;
	}

	/**
	 * 获取读者类型
	 * 
	 * @throws SQLException
	 */
	public String getAllReaderType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 查询读者类型
		List<ReaderType> readertypes = adminService.getAllReaderType();

		request.setAttribute("readertypes", readertypes);

		return "/WEB-INF/admin/readerTypeManage.jsp";
	}

	// 获取所有读者类型返回json数据
	public String getAllReaderTypes(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 查询读者类型
		List<ReaderType> allReaderTypes = adminService.getAllReaderType();

		response.setContentType("application/json;charset=utf-8");
		String json = JsonUtil.list2json(allReaderTypes);// List------->JSONArray
		response.getWriter().print(json);
		return null;
	}

	// 根据ID获取读者类型
	public String getReaderType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 查询读者类型
		String sid = request.getParameter("id");
		int id = Integer.parseInt(sid);
		ReaderType readertype = adminService.getReaderType(id);

		String result = JsonUtil.object2json(readertype);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().print(result);

		return null;
	}

	// 修改读者类型
	public String updateReaderType(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		int readerTypeId = Integer.parseInt(request.getParameter("id"));
		int maxNum = Integer.parseInt(request.getParameter("maxNum"));
		int bday = Integer.parseInt(request.getParameter("bday"));
		double penalty = Double.parseDouble(request.getParameter("penalty"));

		String readerTypeName = request.getParameter("readerTypeName");
		int renewDays = Integer.parseInt(request.getParameter("renewDays"));

		boolean flag = adminService.updateReaderType(readerTypeId, maxNum, bday, penalty, readerTypeName, renewDays);
		int success = 0;

		if (flag) {
			success = 1;
		}

		response.getWriter().print(success);

		return null;
	}

	// 通过Id获取reader
	public String getReader(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		response.setContentType("application/json;charset=utf-8");

		String readerId = request.getParameter("readerId");

		Reader reader = adminService.getReaderById(readerId);
		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object obj, String name, Object value) {
				if (obj instanceof Set || name.equals("borrowInfos") || obj instanceof Authorization
						|| name.equals("authorization")) {// 过滤掉集合
					return true;
				} else {
					return false;
				}
			}
		});

		JSONObject jsonObject = JSONObject.fromObject(reader, jsonConfig);
		response.getWriter().print(jsonObject);

		return null;
	}

	// 通过Id删除reader
	public String deleteReader(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 删除读者需要注意的点：如果该读者有尚未归还的书籍或者尚未缴纳的罚款,则不能删除
		String readerId = request.getParameter("readerId");
		int success = adminService.deleteReader(readerId);

		response.getWriter().print(success);

		return null;
	}

	// 根据Id修改reader
	public String updateReader(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 获取请求参数
		String readerId = request.getParameter("readerId");
		int readerTypeId = Integer.parseInt(request.getParameter("readerType"));
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String paperNO = request.getParameter("paperNO");
		// 获取读者
		Reader updateReader = adminService.getReaderById(readerId);// 查出需要修改的读者对象;
		updateReader.setName(name);
		updateReader.setPhone(phone);
		updateReader.setPaperNO(paperNO);
		updateReader.setEmail(email);
		updateReader.setReaderTypeId(readerTypeId);
		// 设置reader的值

		boolean flag = adminService.updateReaderInfo(updateReader);
		int success = 0;
		if (flag) {
			success = 1;
			// 由于是转发并且js页面刷新,所以无需重查
		}
		response.getWriter().print(success);
		return null;
	}

	// 添加读者
	public String addReader(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 得到当前时间
		Date createTime = new Date(System.currentTimeMillis());
		int readerTypeId = Integer.parseInt(request.getParameter("readerType"));
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String paperNO = request.getParameter("paperNO");
		// 得到当前管理员
		Admin admin = (Admin) request.getSession().getAttribute("admin");

		// 得到当前读者类型
		ReaderType type = new ReaderType();
		type.setReaderTypeId(readerTypeId);
		Reader reader = new Reader(name, "1234", phone, type, email, admin, paperNO, createTime);
		reader.setAid(admin.getAid());
		reader.setReaderTypeId(readerTypeId);

		Reader oldReader = adminService.getReaderBypaperNO(paperNO);// 检查是否已经存在该paperNO的读者
		int success = 0;
		if (oldReader != null) {
			success = -1;// 已存在该id
		} else {

			boolean flag = adminService.addReader(reader);
			if (flag) {
				success = 1;
			}
		}
		response.getWriter().print(success);

		return null;
	}

	// 分页查找读者
	public String findReaderByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		int pageCode = Integer.parseInt(request.getParameter("pageCode"));

		// 给pageSize,每页的记录数赋值
		int pageSize = 3;
		PageBean<Reader> pb = adminService.findReaderByPage(pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findReaderByPage&");
		}
		// 存入request域中
		request.setAttribute("pb", pb);

		return "/WEB-INF/admin/readerManage.jsp";
	}

	// 查询读者
	public String queryReader(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		String name = request.getParameter("name");
		String paperNO = request.getParameter("paperNO");
		int readerTypeId = Integer.parseInt(request.getParameter("readerType"));

		// 给pageSize,每页的记录数赋值
		int pageSize = 3;
		PageBean<Reader> pb = null;
		if ("".equals(paperNO.trim()) && "".equals(name.trim()) && readerTypeId == -1) {
			pb = adminService.findReaderByPage(pageCode, pageSize);
		} else {
			Reader reader = new Reader();

			reader.setName(name);

			reader.setPaperNO(paperNO);

			reader.setReaderTypeId(readerTypeId);

			pb = adminService.queryReader(reader, pageCode, pageSize);
		}
		if (pb != null) {
			pb.setUrl("queryReader&paperNO=" + paperNO + "&name=" + name + "&readerType=" + readerTypeId + "&");
		}
		request.setAttribute("pb", pb);
		
		
		return "/WEB-INF/admin/readerManage.jsp";
	}

}
