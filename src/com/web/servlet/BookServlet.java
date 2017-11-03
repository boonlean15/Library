package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Admin;
import com.entity.Authorization;
import com.entity.Book;
import com.entity.BookType;
import com.entity.PageBean;
import com.service.BookService;
import com.service.BookTypeService;
import com.utils.BeanFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * 图书管理Servlet
 */
public class BookServlet extends BaseServlet {

	private static final long serialVersionUID = -8814661660529196409L;
	private static final BookService bookService = (BookService) BeanFactory.getBean("BookService");
	private static final BookTypeService bookTypeService = (BookTypeService) BeanFactory.getBean("BookTypeService");

	/**
	 * 根据页码查询图书
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String findBookByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;

		PageBean<Book> pb = bookService.findBookByPage(pageCode, pageSize);
		if (pb != null) {
			pb.setUrl("findBookByPage&");
		}
		// 存入request域中
		request.setAttribute("pb", pb);
		return "/WEB-INF/admin/bookManage.jsp";
	}

	/**
	 * 得到图书类型的集合 ajax请求该方法 返回图书类型集合的json对象
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getAllBookTypes(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		response.setContentType("application/json;charset=utf-8");
		List<BookType> allBookTypes = bookTypeService.getAllBookTypes();

		String json = JSONArray.fromObject(allBookTypes).toString();// List------->JSONArray
		response.getWriter().print(json);

		return null;
	}

	/**
	 * 得到指定图书编号的图书信息 ajax请求该方法 返回该图书信息的json对象
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		response.setContentType("application/json;charset=utf-8");
		int bookId = Integer.parseInt(request.getParameter("bookId"));
		Book book = new Book();
		book.setBookId(bookId);
		Book newBook = bookService.getBookById(book);// 得到图书

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			public boolean apply(Object obj, String name, Object value) {
				if (obj instanceof Authorization || name.equals("authorization")) {
					return true;
				} else {
					return false;
				}
			}
		});

		JSONObject jsonObject = JSONObject.fromObject(newBook, jsonConfig);
		response.getWriter().print(jsonObject);
		return null;
	}

	/**
	 * 修改图书
	 * 
	 * @return
	 */
	public String updateBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		Book book = new Book();
		// 获取请求参数
		int bookId = Integer.parseInt(request.getParameter("bookId"));
		book.setBookId(bookId);
		// 得到修改的图书信息
		Book updateBook = bookService.getBookById(book);
		// 获取请求参数
		String bookName = request.getParameter("bookName");
		int bookTypeId = Integer.parseInt(request.getParameter("bookTypeId"));
		String autho = request.getParameter("autho");
		String press = request.getParameter("press");
		double price = Double.parseDouble(request.getParameter("price"));
		String description = request.getParameter("description");
		String ISBN = request.getParameter("ISBN");

		updateBook.setBookName(bookName);
		updateBook.setAutho(autho);// 对图书进行修改
		updateBook.setISBN(ISBN);
		BookType type = new BookType();
		type.setTypeId(bookTypeId);
		updateBook.setBookType(type);// 设置图书类型
		updateBook.setDescription(description);
		updateBook.setPress(press);
		updateBook.setPrice(price);// 对图书进行修改
		boolean flag = bookService.updateBookInfo(updateBook);// 修改图书信息对象
		int success = 0;
		if (flag) {
			success = 1;
			// 由于是转发并且js页面刷新,所以无需重查
		}
		response.getWriter().print(success);

		return null;
	}

	/**
	 * 添加图书
	 * 
	 * @return
	 */
	public String addBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 获取请求参数
		String bookName = request.getParameter("bookName");
		String autho = request.getParameter("autho");
		String press = request.getParameter("press");
		String ISBN = request.getParameter("ISBN");
		String description = request.getParameter("description");
		// 不是String类型参数
		int num = Integer.parseInt(request.getParameter("num"));
		double price = Double.parseDouble(request.getParameter("price"));
		int typeId = Integer.parseInt(request.getParameter("bookTypeId"));

		BookType bookType = new BookType();
		bookType.setTypeId(typeId);
		// 得到当前时间,作为上架时间
		Date putdate = new Date(System.currentTimeMillis());
		// 得到操作管理员
		Admin admin = (Admin) request.getSession().getAttribute("admin");

		Book book = new Book(ISBN, bookType, bookName, autho, press, putdate, num, num, price, description, admin);// 设置图书
		book.setTypeId(typeId);
		book.setAid(admin.getAid());

		boolean b = bookService.addBook(book);// 添加图书.返回是否成功添加
		int success = 0;
		if (b) {
			success = 1;
		}
		response.getWriter().print(success);

		return null;
	}

	/**
	 * 删除指定图书
	 * 
	 * @return
	 */
	public String deleteBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {

		String parameter = request.getParameter("bookId");
		int bookId = Integer.parseInt(parameter);

		// 删除图书需要注意的事项：如果该书有尚未归还的记录或者尚未缴纳的罚款记录,则不能删除
		Book book = new Book();
		book.setBookId(bookId);
		int success = bookService.deleteBook(book);// 删除图书

		response.getWriter().print(success);

		return null;
	}

	/**
	 * 多条件查询图书
	 * @return
	 */
	public String queryBook(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		// 获取页面传递过来的当前页码数
		int pageCode = Integer.parseInt(request.getParameter("pageCode"));
		if (pageCode == 0) {
			pageCode = 1;
		}
		// 给pageSize,每页的记录数赋值
		int pageSize = 3;
		//获取请求参数
		String ISBN = request.getParameter("ISBN");
		int typeId = Integer.parseInt(request.getParameter("bookTypeId"));
		String bookName = request.getParameter("bookName");
		String autho = request.getParameter("autho");
		String press = request.getParameter("press");
		
		PageBean<Book> pb = null;

		if ("".equals(ISBN.trim()) && "".equals(bookName.trim()) && typeId == -1 && "".equals(press.trim())
				&& "".equals(autho.trim())) {
			pb = bookService.findBookByPage(pageCode, pageSize);
		} else {
			BookType bookType = new BookType();
			bookType.setTypeId(typeId);
			
			Book book = new Book(ISBN, bookType, bookName, autho, press);
			book.setTypeId(typeId);

			pb = bookService.queryBook(book, pageCode, pageSize);
		}
		if (pb != null) {
			pb.setUrl("queryBook&ISBN=" + ISBN + "&bookName=" + bookName + "&bookTypeId=" + typeId
					+ "&press=" + press + "&autho=" + autho + "&");
		}
		request.setAttribute("pb", pb);
		return "/WEB-INF/admin/bookManage.jsp";
	}
}
