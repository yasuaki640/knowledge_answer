package jp.kronos.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
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
 * Servlet implementation class ResetPasswordServlet
 */
@WebServlet(urlPatterns={"/reset-password"}, initParams={@WebInitParam(name="password", value="knowledge123")})
public class ResetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(ResetPasswordServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// トップページに遷移する
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null || !((UserDto)session.getAttribute("user")).isAdministratorFlg()) {
			logger.warn("セッションタイムアウト {}", request.getRemoteAddr());
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		// ログインユーザ情報を取得する
		UserDto user = (UserDto)session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		UserDto userDto = new UserDto();
		userDto.setUserId(Integer.parseInt(request.getParameter("userId")));
		userDto.setPassword(getInitParameter("password"));
		userDto.setUpdateUserId(user.getUserId());
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			// パスワードを更新する
			UserDao userDao = new UserDao(conn);
			userDao.updatePassword(userDto);
			
			session.setAttribute("message", "パスワードを初期化しました");
			
			// ユーザ管理画面に遷移する
			response.sendRedirect("list-user");
			
		} catch (SQLException | NamingException e) {
			logger.error("{} {}", e.getClass(), e.getMessage());
			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}		
	}

}
