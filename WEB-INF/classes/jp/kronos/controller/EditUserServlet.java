package jp.kronos.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
import jp.kronos.FieldValidator;
import jp.kronos.dao.UserDao;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class EditUserServlet
 */
@WebServlet("/edit-user")
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(EditUserServlet.class);

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
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("セッションタイムアウト");
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		// ログインユーザ情報を取得する
		UserDto user = (UserDto)session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		String index = request.getParameter("index");
		UserDto userDto = new UserDto();
		userDto.setUserId(Integer.parseInt(request.getParameter("userId" + index)));
		userDto.setEmail(request.getParameter("email" + index));
		userDto.setLastName(request.getParameter("lastName" + index));
		userDto.setFirstName(request.getParameter("firstName" + index));
		userDto.setAdministratorFlg(request.getParameterValues("administratorFlg" + index) != null);
		userDto.setUpdateUserId(user.getUserId());
		userDto.setUpdateNumber(Integer.parseInt(request.getParameter("updateNumber" + index)));
		
		// 入力チェック
		List<String> errorMessageList = FieldValidator.userValidation(userDto);
		if (errorMessageList.size() != 0) {
			session.setAttribute("errorMessageList", errorMessageList);
			response.sendRedirect("list-user");
			return;
		}
				
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			// ユーザ情報を更新する
			UserDao userDao = new UserDao(conn);
			userDao.update(userDto);
			
			session.setAttribute("message", "更新しました");
			
			// ユーザ管理画面に遷移する
			response.sendRedirect("list-user");
			
		} catch (SQLException | NamingException e) {
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
