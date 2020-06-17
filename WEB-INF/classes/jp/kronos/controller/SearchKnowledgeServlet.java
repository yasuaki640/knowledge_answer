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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kronos.DataSourceManager;
import jp.kronos.dao.KnowledgeDao;

/**
 * Servlet implementation class SearchKnowledgeServlet
 */
@WebServlet("/search-knowledge")
public class SearchKnowledgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(SearchKnowledgeServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());
		
		// フォームのデータを取得する
		request.setCharacterEncoding("UTF-8");
		String q = request.getParameter("q");		
		
		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			
			// ナレッジ情報を検索し、リクエストに保持する
			KnowledgeDao knowledgeDao = new KnowledgeDao(conn);
			
			if (q.length() == 0) {
				request.setAttribute("knowledgeList", knowledgeDao.selectAll());
			} else {
				// 検索キーワードをセッションに保持する
				String[] queries = q.split(" ");
				request.setAttribute("queries", queries);
				request.setAttribute("knowledgeList", knowledgeDao.selectByQueries(queries));
			}
			
			// URIをリクエストに保持する
			request.setAttribute("uri", request.getRequestURL().append('?').append(request.getQueryString()));
			
			// ナレッジ一覧画面に遷移する
			request.getRequestDispatcher("WEB-INF/list-knowledge.jsp").forward(request, response);
			
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
		doGet(request, response);
	}

}
