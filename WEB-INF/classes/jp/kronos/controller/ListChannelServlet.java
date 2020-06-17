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
import jp.kronos.dao.ChannelDao;
import jp.kronos.dto.ChannelDto;

/**
 * Servlet implementation class ThreadListServlet
 */
@WebServlet("/list-channel")
public class ListChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Logger logger = LoggerFactory.getLogger(ListChannelServlet.class);
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.info("start:{} {}", Thread.currentThread().getStackTrace()[1].getMethodName(), request.getRemoteAddr());

		// コネクションを取得する
		try (Connection conn = DataSourceManager.getConnection()) {
			// セッションを取得する
			HttpSession session = request.getSession(true);
			session.removeAttribute("queries");
			
			// チャンネル一覧を取得する
			ChannelDao channelDao = new ChannelDao(conn);
			List<ChannelDto> channelList = channelDao.selectAll();
			
			// チャンネル一覧データをリクエストに保持する
			request.setAttribute("channelList", channelList);
			
			// URIをリクエストに保持する
			request.setAttribute("uri", request.getRequestURI());
			
			// メッセージをリクエストに保持する
			request.setAttribute("message", session.getAttribute("message"));
			session.removeAttribute("message");
			
			// ナブバーメッセージをリクエストに保持する
			request.setAttribute("navbarMessage", session.getAttribute("navbarMessage"));
			session.removeAttribute("navbarMessage");

			// チャンネル一覧画面に遷移する
			request.getRequestDispatcher("WEB-INF/list-channel.jsp").forward(request, response);
			
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
