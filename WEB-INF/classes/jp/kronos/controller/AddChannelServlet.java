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

import jp.kronos.FieldValidator;
import jp.kronos.DataSourceManager;
import jp.kronos.dao.ChannelDao;
import jp.kronos.dto.ChannelDto;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class AddKnowledgeServlet
 */
@WebServlet("/add-channel")
public class AddChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(AddChannelServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 追加モード
		request.setAttribute("isAdd", true);
		
		HttpSession session = request.getSession();
		
		// エラーメッセージをリクエストに保持する
		request.setAttribute("errorMessageList", session.getAttribute("errorMessageList"));
		session.removeAttribute("errorMessageList");
		
		// チャンネル登録画面に遷移する
		request.getRequestDispatcher("WEB-INF/view-channel.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("セッションタイムアウト {}", request.getRemoteAddr());
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		// セッションからログインユーザ情報を取得する
		UserDto user = (UserDto)session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		ChannelDto channelDto = new ChannelDto();
		channelDto.setChannelName(request.getParameter("channelName"));
		channelDto.setOverview(request.getParameter("overview"));
		channelDto.setUserId(user.getUserId());
	
		// 入力チェック
		List<String> errorMessageList = FieldValidator.channelValidation(channelDto);
		if (errorMessageList.size() != 0) {
			// チャンネル登録画面に遷移する
			session.setAttribute("errorMessageList", errorMessageList);
			response.sendRedirect("add-channel");
			return;
		}
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			// チャンネルを追加する
			ChannelDao channelDao = new ChannelDao(conn);
			channelDao.insert(channelDto);

			// チャンネル名をリクエストスコープに保持する
			session.setAttribute("message", channelDto.getChannelName() + "を登録しました");
			
			// チャンネル一覧画面に遷移する
			response.sendRedirect("list-channel");
			
		} catch (SQLException | NamingException e) {
			
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// チャンネル名が重複している場合
			if (e.getMessage().contains("Duplicate entry")) {
				
				// エラーメッセージをセッションスコープに保持する
				errorMessageList.add("チャンネル「" + channelDto.getChannelName() + "」は既に存在します");
				session.setAttribute("errorMessageList", errorMessageList);
				
				// フォームのデータをセッションスコープに保持する
				session.setAttribute("channelDto", channelDto);

				// チャンネル登録画面に遷移する
				response.sendRedirect("add-channel");
			} else {
				// システムエラー画面に遷移する
				response.sendRedirect("system-error.jsp");
			}
		}
	}
}
