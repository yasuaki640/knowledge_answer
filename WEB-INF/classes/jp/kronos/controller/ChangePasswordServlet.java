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
 * Servlet implementation class ChangePasswordServlet
 */
@WebServlet(urlPatterns={"/change-password"}, initParams={@WebInitParam(name="password", value="knowledge123")})
public class ChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(ChangePasswordServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null || !((UserDto)session.getAttribute("user")).isAdministratorFlg()) {
			logger.warn("セッションタイムアウトまたは、未ログインアクセス");
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
				
		// エラーメッセージをリクエストに保持する
		request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
		session.removeAttribute("errorMessage");
		
		if (session.getAttribute("isChangeRequired") != null) {
			// パスワード変更画面に遷移する
			request.getRequestDispatcher("WEB-INF/change-password.jsp").forward(request, response);
		} else {
			// トップページに遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("セッションタイムアウトまたは、未ログインアクセス");
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		// ログインユーザ情報を取得する
		UserDto user = (UserDto)session.getAttribute("user");

		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		String pass1 = request.getParameter("password1");
		String pass2 = request.getParameter("password2");
		
		// フォームのデータをチェックする
		if ("".equals(pass1) || !pass1.equals(pass2)) {
			session.setAttribute("errorMessage", "新しいパスワードと確認用のパスワードが正しくありません");
			// パスワード変更画面に戻る
			response.sendRedirect("change-password");
			return;
		} else if (getInitParameter("password").equals(pass1)) {
			session.setAttribute("errorMessage", "初期化パスワードが変更されていません");
			// パスワード変更画面に戻る
			response.sendRedirect("change-password");
			return;
		}

		// パスワード変更情報をまとめる
		UserDto dto = new UserDto();
		dto.setPassword(pass1);
		dto.setUserId(user.getUserId());
		dto.setUpdateUserId(user.getUserId());
		dto.setUpdateNumber(user.getUpdateNumber());
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			// パスワードを変更する
			UserDao dao = new UserDao(conn);
			dao.updatePassword(dto);
			
			session.setAttribute("message", "初期化パスワードを変更しました");
			session.removeAttribute("isChangeRequired");
			
			// チャンネル一覧画面に遷移する
			response.sendRedirect("list-channel");
			
		} catch (SQLException | NamingException e) {
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// システムエラーに遷移する
			response.sendRedirect("system-error.jsp");
		}
	}
}
