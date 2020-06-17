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
import jp.kronos.dao.KnowledgeDao;
import jp.kronos.dto.KnowledgeDto;

/**
 * Servlet implementation class ThredDetailServlet
 */
@WebServlet("/list-knowledge")
public class ListKnowledgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(ListKnowledgeServlet.class);
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());

		// チャンネルIDを取得する
		int channelId = Integer.parseInt(request.getParameter("channelId"));

		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			HttpSession session = request.getSession(false);
			
			// ナレッジ情報リストを取得する
			KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
			List<KnowledgeDto> knowledgeList = knowledgeDao.selectByChannelId(channelId);
			
			// ナレッジ情報リストをリクエストに保持する
			request.setAttribute("knowledgeList", knowledgeList);
			
			// URIをリクエストに保持する
			request.setAttribute("uri", request.getRequestURI().concat("?").concat(request.getQueryString()));
			
			// メッセージをリクエストに保持する
			request.setAttribute("message", session.getAttribute("message"));
			session.removeAttribute("message");
			
			// ナブバーメッセージをリクエストに保持する
			request.setAttribute("navbarMessage", session.getAttribute("navbarMessage"));
			session.removeAttribute("navbarMessage");
			
			// ナレッジ一覧画面に遷移する
			request.getRequestDispatcher("WEB-INF/list-knowledge.jsp").forward(request, response);
			
		}catch (SQLException | NamingException e) {
			
			logger.error("{} {}", e.getClass(), e.getMessage());
			
			// システムエラー画面に遷移する
			response.sendRedirect("system-error.jsp");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
