package com.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.entity.Book;
import com.entity.BookType;
import com.entity.PageBean;
import com.service.BookService;
import com.utils.BeanFactory;

/**
 * 读者图书查询
 */
public class BookreaderServlet extends BaseServlet {
	private static final long serialVersionUID = -9124064074154843040L;
	private static final BookService bookService = (BookService) BeanFactory.getBean("BookService");

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
		return "/WEB-INF/reader/book.jsp";
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
		return "/WEB-INF/reader/book.jsp";
	}
	
	
}
