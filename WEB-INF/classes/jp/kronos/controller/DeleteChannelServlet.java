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

import jp.kronos.BusinessLogicException;
import jp.kronos.DataSourceManager;
import jp.kronos.dao.ChannelDao;
import jp.kronos.dao.KnowledgeDao;
import jp.kronos.dto.ChannelDto;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class DeleteChannelServlet
 */
@WebServlet("/delete-channel")
public class DeleteChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(DeleteChannelServlet.class);

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
		UserDto user = (UserDto) session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		ChannelDto channelDto = new ChannelDto();
		channelDto.setChannelId(Integer.parseInt(request.getParameter("channelId")));
		channelDto.setUpdateNumber(Integer.parseInt(request.getParameter("updateNumber")));
		
		// コネクションを取得する
		String channelName = "";
		try (Connection conn = DataSourceManager.getConnection()) {

			try {
				// トランザクションを開始する
				conn.setAutoCommit(false);

				ChannelDao channelDao = new ChannelDao(conn);
				
				// ログインユーザに管理権限がない、かつナレッジのユーザIDと一致しない場合
				if (!user.isAdministratorFlg() && channelDao.selectByChannelId(channelDto.getChannelId()).getUserId() != user.getUserId()) {
					response.sendRedirect("access-denied.jsp");
					return;
				}
				
				// ナレッジ情報を削除する
				KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
				knowledgeDao.deleteByChannelId(channelDto.getChannelId());

				channelName = channelDao.selectByChannelId(channelDto.getChannelId()).getChannelName();
				// チャンネル情報を削除する
				int count = channelDao.delete(channelDto);
							
				if (count == 0) {
					throw new BusinessLogicException("排他制御（楽観ロック）例外");
				}
				// コミットする
				conn.commit();
				
				session.setAttribute("message", "「" + channelName + "」チャンネルを削除しました");
			} catch (BusinessLogicException e) {

				logger.error("{} {}", e.getClass(), e.getMessage());

				// ロールバックする
				conn.rollback();
				session.setAttribute("message", "「" + channelName + "」チャンネルを削除できませんでした。 - 排他制御");
				
			} catch (SQLException e) {

				logger.error("{} {}", e.getClass(), e.getMessage());
				
				// ロールバックする
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
			}

			// チャンネル一覧画面に遷移する
			response.sendRedirect("list-channel");
			
		} catch (SQLException | NamingException e) {

			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}
	}
}
