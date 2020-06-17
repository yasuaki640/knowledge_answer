package jp.kronos.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kronos.DataSourceManager;
import jp.kronos.dao.UserDao;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class UserListServlet
 */
@WebServlet("/list-user")
public class ListUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(ListUserServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null || !((UserDto)session.getAttribute("user")).isAdministratorFlg()) {
			logger.warn("セッションタイムアウトまたは、未ログインアクセス");
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			
			// ユーザ情報リストを取得し、リクエストに保持する
			UserDao userDao = new UserDao(conn);
			request.setAttribute("userList", userDao.selectAll());
			
			// エラーメッセージをリクエストに保持する
			request.setAttribute("errorMessageList", session.getAttribute("errorMessageList"));
			session.removeAttribute("errorMessageList");
			
			// メッセージをリクエストに保持する
			request.setAttribute("message", session.getAttribute("message"));
			session.removeAttribute("message");
			
			// ユーザ管理画面に遷移する
			request.getRequestDispatcher("WEB-INF/list-user.jsp").forward(request, response);

		} catch (SQLException | NamingException e) {
			
			logger.error("{} {}", e.getClass(), e.getMessage());

			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
