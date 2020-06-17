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
import jp.kronos.dao.ChannelDao;
import jp.kronos.dao.KnowledgeDao;
import jp.kronos.dto.ChannelDto;
import jp.kronos.dto.KnowledgeDto;
import jp.kronos.dto.UserDto;

/**
 * Servlet implementation class EditKnowledgeServlet
 */
@WebServlet("/edit-knowledge")
public class EditKnowledgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(EditKnowledgeServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
		UserDto user = (UserDto) session.getAttribute("user");
				
		// エラーメッセージをリクエストに保持する
		request.setAttribute("errorMessageList", session.getAttribute("errorMessageList"));
		session.removeAttribute("errorMessageList");

		try (Connection conn = DataSourceManager.getConnection()) {

			KnowledgeDto knowledgeDto = (KnowledgeDto)session.getAttribute("knowledgeDto");

			// セッションスコープにナレッジ情報がある場合
			if (knowledgeDto != null) {
				// セッションスコープからナレッジ情報を削除する
				session.removeAttribute("knowledgeDto");
			} else {
				// フォームのデータを取得する
				request.setCharacterEncoding("UTF-8");
				int knowledgeId = Integer.parseInt(request.getParameter("knowledgeId"));

				// ナレッジ情報を取得する
				KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
				
				// ナレッジ情報を取得する
				knowledgeDto = knowledgeDao.selectByKnowledgeId(knowledgeId);
				
				// ログインユーザに管理権限がない、かつナレッジのユーザIDと一致しない場合
				if (!user.isAdministratorFlg() && knowledgeDto.getUserId() != user.getUserId()) {
					request.getRequestDispatcher("access-denied.jsp").forward(request, response);
					return;
				}
			}
			// ナレッジ情報をリクエストスコープに保持する
			request.setAttribute("knowledgeDto", knowledgeDto);

			// チャンネル情報を取得する
			ChannelDao channelDao = new ChannelDao(conn);
			List<ChannelDto> channelList = channelDao.selectAll();
			request.setAttribute("channelList", channelList);

			// ナレッジ編集画面に遷移する
			request.getRequestDispatcher("WEB-INF/view-knowledge.jsp").forward(request, response);

		} catch (SQLException | NamingException e) {

			logger.error("{} {}", e.getClass(), e.getMessage());

			// システムエラー画面に遷移する
			request.getRequestDispatcher("system-error.jsp").forward(request, response);
		}
		
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
			// 未ログインならチャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		// ログインユーザ情報を取得する
		UserDto user = (UserDto) session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		KnowledgeDto knowledgeDto = new KnowledgeDto();
		knowledgeDto.setKnowledgeId(Integer.parseInt(request.getParameter("knowledgeId")));
		knowledgeDto.setKnowledge(request.getParameter("knowledge"));
		knowledgeDto.setChannelId(Integer.parseInt(request.getParameter("channelId")));
		knowledgeDto.setUpdateNumber(Integer.parseInt(request.getParameter("updateNumber")));
		
		// 入力チェック
		List<String> errorMessageList = FieldValidator.knowledgeValidation(knowledgeDto);
		if (errorMessageList.size() != 0) {
			// ナレッジ編集画面に遷移する
			session.setAttribute("errorMessageList", errorMessageList);
			session.setAttribute("knowledgeDto", knowledgeDto);
			response.sendRedirect("edit-knowledge?knowledgeId=" + request.getParameter("knowledgeId"));
			return;
		}
				
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			
			KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
			
			// ログインユーザに管理権限がない、かつナレッジのユーザIDと一致しない場合
			if (!user.isAdministratorFlg() && knowledgeDao.selectByKnowledgeId(knowledgeDto.getKnowledgeId()).getUserId() != user.getUserId()) {
				response.sendRedirect("access-denied.jsp");
				return;
			}
			
			// ナレッジ情報を更新する
			knowledgeDao.update(knowledgeDto);
			
			session.setAttribute("message", "更新しました");
			
			// ナレッジ一覧画面に遷移する
			response.sendRedirect("list-knowledge?channelId=" + request.getParameter("channelId"));
			
		} catch (SQLException | NamingException e) {

			logger.error("{} {}", e.getClass(), e.getMessage());

			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}		
	}

}
