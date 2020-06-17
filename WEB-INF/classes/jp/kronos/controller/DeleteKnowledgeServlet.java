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
import jp.kronos.dao.KnowledgeDao;
import jp.kronos.dto.KnowledgeDto;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class DeleteKnowledgeServlet
 */
@WebServlet("/delete-knowledge")
public class DeleteKnowledgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(DeleteKnowledgeServlet.class);

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
		KnowledgeDto knowledgeDto = new KnowledgeDto();
		knowledgeDto.setKnowledgeId(Integer.parseInt(request.getParameter("knowledgeId")));
		knowledgeDto.setUpdateNumber(Integer.parseInt(request.getParameter("updateNumber")));
		knowledgeDto.setChannelId(Integer.parseInt(request.getParameter("channelId")));
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			
			KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
			
			// ログインユーザに管理権限がない、かつナレッジのユーザIDと一致しない場合
			if (!user.isAdministratorFlg() && knowledgeDao.selectByKnowledgeId(knowledgeDto.getKnowledgeId()).getUserId() != user.getUserId()) {
				response.sendRedirect("access-denied.jsp");
				return;
			}
						
			// ナレッジ情報を削除する
			knowledgeDao.deleteByKnowledgeId(knowledgeDto);
			
			session.setAttribute("message", "削除しました");
			
			// ナレッジ一覧画面に遷移する
			response.sendRedirect("list-knowledge?channelId=" + knowledgeDto.getChannelId());
			
		} catch (SQLException | NamingException e) {

			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}		
	}

}
