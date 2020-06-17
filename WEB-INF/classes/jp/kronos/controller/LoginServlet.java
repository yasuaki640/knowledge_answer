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
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns={"/login"}, initParams={@WebInitParam(name="password", value="knowledge123")})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		// トップページに遷移する
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		// フォームのデータを取得する
		String loginId = request.getParameter("id");
		String loginPassword = request.getParameter("password");
		String uri = request.getParameter("uri");

		// セッションを取得する
		HttpSession session = request.getSession(true);

		// ログインID、パスワードが未入力の場合
		if ("".equals(loginId) || "".equals(loginPassword)) {
			logger.warn("ログイン失敗 {}", request.getRemoteAddr());

			session.setAttribute("navbarMessage", "メールアドレス、パスワードを入力してください");
			
			// ログイン処理前にページ情報が存在しない場合はチャンネル一覧に遷移する
			response.sendRedirect(uri);
			return;
		}
		
		try (Connection conn = DataSourceManager.getConnection()) {

			// ログイン処理
			UserDao loginDao = new UserDao(conn);
			UserDto userDto = loginDao.findByIdAndPassword(loginId, loginPassword);

			session.setAttribute("user", userDto);
			session.removeAttribute("navbarMessage");

			// ログイン失敗時
			if (userDto == null) {
				logger.warn("ログイン失敗 {} mail={} pass={}", request.getRemoteAddr(), loginId, loginPassword);
				session.setAttribute("navbarMessage", "メールアドレスまたはパスワードが間違っています");
			}

			// 初回ログイン時
			if (getInitParameter("password").equals(loginPassword)) {
				session.setAttribute("isChangeRequired", true);
				response.sendRedirect("change-password");
				return;
			}
			
			// ログイン処理前にページ情報が存在しない場合はチャンネル一覧に遷移する
			logger.debug(uri);
			response.sendRedirect(uri);
			
		} catch (SQLException | NamingException e) {
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// システムエラーに遷移する
			response.sendRedirect("system-error.jsp");
		}
	}
}
