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
 * Servlet implementation class EditChannelServlet
 */
@WebServlet("/edit-channel")
public class EditChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(EditChannelServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("セッションタイムアウト {}", request.getRemoteAddr());
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		// ログインユーザ情報を取得する
		UserDto user = (UserDto) session.getAttribute("user");

		// エラーメッセージをリクエストに保持する
		request.setAttribute("errorMessageList", session.getAttribute("errorMessageList"));
		session.removeAttribute("errorMessageList");

		ChannelDto channelDto = (ChannelDto) session.getAttribute("channelDto");

		// セッションスコープにチャンネル情報がある場合
		if (channelDto != null) {
			// セッションスコープからチャンネル情報を削除する
			session.removeAttribute("channelDto");
		} else {
			// フォームのデータを取得する
			request.setCharacterEncoding("UTF-8");
			int channelId = Integer.parseInt(request.getParameter("channelId"));

			// コネクションを取得する
			try (Connection conn = DataSourceManager.getConnection()) {

				// ナレッジ情報を取得する
				ChannelDao channelDao = new ChannelDao(conn);

				// チャンネル情報を取得する
				channelDto = channelDao.selectByChannelId(channelId);
				
				// ログインユーザに管理権限がない、かつナレッジのユーザIDと一致しない場合
				if (!user.isAdministratorFlg() && channelDto.getUserId() != user.getUserId()) {
					request.getRequestDispatcher("access-denied.jsp").forward(request, response);
					return;
				}

			} catch (SQLException | NamingException e) {

				logger.error("{} {}", e.getClass(), e.getMessage());

				// システムエラー画面に遷移する
				request.getRequestDispatcher("system-error.jsp").forward(request, response);
				return;
			}
		}
		// TODO ログインユーザ更新時に、管理者ユーザが先に更新してしまった場合、dtoが取得できない可能性の考慮
		request.setAttribute("channelDto", channelDto);
		
		// チャンネル編集画面に遷移する
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
		
		// ログインユーザ情報を取得する
		UserDto user = (UserDto) session.getAttribute("user");
				
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		ChannelDto channelDto = new ChannelDto();
		channelDto.setChannelId(Integer.parseInt(request.getParameter("channelId")));
		channelDto.setChannelName(request.getParameter("channelName"));
		channelDto.setOverview(request.getParameter("overview"));
		channelDto.setUpdateNumber(Integer.parseInt(request.getParameter("updateNumber")));

		// 入力チェック
		List<String> errorMessageList = FieldValidator.channelValidation(channelDto);
		if (errorMessageList.size() != 0) {
			// チャンネル編集画面に遷移する
			session.setAttribute("errorMessageList", errorMessageList);
			session.setAttribute("channelDto", channelDto);
			response.sendRedirect("edit-channel?channelId=" + request.getParameter("channelId"));
			return;
		}
				
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			
			ChannelDao channelDao = new ChannelDao(conn);
			
			// ログインユーザに管理権限がない、かつナレッジのユーザIDと一致しない場合
			if (!user.isAdministratorFlg() && channelDao.selectByChannelId(channelDto.getChannelId()).getUserId() != user.getUserId()) {
				response.sendRedirect("access-denied.jsp");
				return;
			}
			
			// チャンネルを更新する
			channelDao.update(channelDto);
			
			// チャンネル名をリクエストスコープに保持する
			session.setAttribute("message", channelDto.getChannelName() + "を更新しました");

			// チャンネル一覧画面に遷移する
			response.sendRedirect("list-channel");
			
		} catch (SQLException | NamingException e) {
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// チャンネル名が重複している場合
			if (e.getMessage().contains("Duplicate entry")) {
				
				// エラーメッセージをリクエストスコープに保持する
				errorMessageList.add("チャンネル「" + channelDto.getChannelName() + "」は既に存在します");
				session.setAttribute("errorMessageList", errorMessageList);
				
				// フォームのデータをリクエストスコープに保持する
				session.setAttribute("channelDto", channelDto);

				// チャンネル編集画面に遷移する
				response.sendRedirect("edit-channel?channelId=" + request.getParameter("channelId"));
			} else {
				// システムエラー画面に遷移する
				response.sendRedirect("system-error.jsp");
			}			
		}
	}

}
