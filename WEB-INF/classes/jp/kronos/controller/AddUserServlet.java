package jp.kronos.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
import jp.kronos.FieldValidator;
import jp.kronos.dao.UserDao;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class AddUserServlet
 */
@WebServlet(urlPatterns={"/add-user"}, initParams={@WebInitParam(name="password", value="knowledge123")})
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(AddUserServlet.class);

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
		
		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null || !((UserDto)session.getAttribute("user")).isAdministratorFlg()) {
			logger.warn("セッションタイムアウトまたは、未ログインアクセス");
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		// ログインユーザ情報を取得する
		UserDto user = (UserDto)session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		UserDto userDto = new UserDto();
		userDto.setEmail(request.getParameter("email"));
		userDto.setPassword(getInitParameter("password"));
		userDto.setFirstName(request.getParameter("firstName"));
		userDto.setLastName(request.getParameter("lastName"));
		userDto.setAdministratorFlg(request.getParameterValues("administratorFlg") != null);
		userDto.setUpdateUserId(user.getUserId());
		
		// 入力チェック
		List<String> errorMessageList = FieldValidator.userValidation(userDto);
		if (errorMessageList.size() != 0) {
			session.setAttribute("errorMessageList", errorMessageList);
			response.sendRedirect("list-user");
			return;
		}
				
		try (Connection conn = DataSourceManager.getConnection()) {
			UserDao userDao = new UserDao(conn);
			userDao.insert(userDto);
			
			session.setAttribute("message", userDto.getEmail() + "を追加しました");
			
			// ユーザ管理画面に遷移する
			response.sendRedirect("list-user");
			
		} catch (SQLException | NamingException e) {
			
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// メールアドレスが重複している場合
			if (e.getMessage().contains("Duplicate entry")) {
				// エラーメッセージをリクエストスコープに保持する
				errorMessageList.add("メールアドレス「" + userDto.getEmail() + "」は既に存在します");
				session.setAttribute("errorMessageList", errorMessageList);
				
				// ユーザ管理画面に遷移する
				response.sendRedirect("list-user");
			} else {
				// システムエラー画面に遷移する
				response.sendRedirect("system-error.jsp");
			}
		}
	}
}