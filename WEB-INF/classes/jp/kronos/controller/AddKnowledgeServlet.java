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
 * Servlet implementation class AddKnowledgeServlet
 */
@WebServlet("/add-knowledge")
public class AddKnowledgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(AddKnowledgeServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("セッションタイムアウト {}", request.getRemoteAddr());
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {

			// チャンネル情報リストを取得する
			ChannelDao channelDao = new ChannelDao(conn);
			List<ChannelDto> channelList = channelDao.selectAll();
			
			// チャンネル情報リストをリクエストスコープに保持する
			request.setAttribute("channelList", channelList);
			
			// 追加モード
			request.setAttribute("isAdd", true);
			
			// エラーメッセージをリクエストに保持する
			request.setAttribute("errorMessageList", session.getAttribute("errorMessageList"));
			session.removeAttribute("errorMessageList");
			
			// ナレッジ一覧に遷移する
			request.getRequestDispatcher("WEB-INF/view-knowledge.jsp").forward(request, response);
			
		} catch (SQLException | NamingException e) {

			logger.error("{} {}", e.getClass(), e.getMessage());

			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// セッションを取得する
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("セッションタイムアウト {}", request.getRemoteAddr());
			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		// ログインユーザ情報を取得する
		UserDto user = (UserDto)session.getAttribute("user");
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		KnowledgeDto knowledgeDto = new KnowledgeDto();
		knowledgeDto.setKnowledge(request.getParameter("knowledge"));
		knowledgeDto.setChannelId(Integer.parseInt(request.getParameter("channelId")));
		knowledgeDto.setUserId(user.getUserId());
		
		// 入力チェック
		List<String> errorMessageList = FieldValidator.knowledgeValidation(knowledgeDto);
		if (errorMessageList.size() != 0) {
			// ナレッジ登録画面に遷移する
			session.setAttribute("errorMessageList", errorMessageList);
			response.sendRedirect("add-knowledge");
			return;
		}
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			
			// ナレッジ情報を追加する
			KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
			knowledgeDao.insert(knowledgeDto);
			
			session.setAttribute("message", "追加しました");
			
			// ナレッジ一覧画面に遷移する
			response.sendRedirect("list-knowledge?channelId=" + request.getParameter("channelId"));
			
		} catch (SQLException | NamingException e) {
			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}		
	}
}
