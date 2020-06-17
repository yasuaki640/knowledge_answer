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
import jp.kronos.dao.ChannelDao;
import jp.kronos.dao.KnowledgeDao;
import jp.kronos.dao.UserDao;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class DeleteUserServlet
 */
@WebServlet("/delete-user")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(DeleteUserServlet.class);

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
		UserDto loginUser = (UserDto)session.getAttribute("user");

		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		String index = request.getParameter("index");
		UserDto userDto = new UserDto();
		userDto.setUserId(Integer.parseInt(request.getParameter("userId" + index)));
		userDto.setUpdateNumber(Integer.parseInt(request.getParameter("updateNumber" + index)));

		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {

			int count = 0;
			// ナレッジの数を取得する
			KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
			count += knowledgeDao.selectCountByUserId(userDto.getUserId());
			
			// チャンネルの数を取得する
			ChannelDao channelDao = new ChannelDao(conn);
			count += channelDao.selectCountByUserId(userDto.getUserId());

			UserDao userDao = new UserDao(conn);
			if (count == 0) {
				// 物理削除
				userDao.delete(userDto);
			} else {
				// 論理削除
				userDto.setDelFlg(true);
				userDto.setUpdateUserId(loginUser.getUserId());
				userDao.updateDelFlg(userDto);
			}
			
			session.setAttribute("message", "削除しました");
			
			// ユーザ管理画面に遷移する
			response.sendRedirect("list-user");
			
		} catch (SQLException | NamingException e) {
			
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}
	}
}