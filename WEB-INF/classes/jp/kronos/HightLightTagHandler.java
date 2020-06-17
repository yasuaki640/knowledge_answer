package jp.kronos;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HightLightTagHandler extends TagSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(HightLightTagHandler.class);

	private String keyword;

	private String data;
	
	@Override
	public int doEndTag() throws JspException {
		logger.info("start:{}", Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			// ?iの後にある文字は大文字小文字を区別しない
			// $1は置換前の文字列()の中
			pageContext.getOut().print(data.replaceAll("((?i)" + keyword + ")", "<mark>$1</mark>"));
		} catch (IOException e) {
			logger.error("{} {}", e.getClass(), e.getMessage());
		}
		return EVAL_PAGE;
	}
	
	/**
	 * keyword属性を設定する
	 * @param keyword ハイライトするキーワード
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * data属性を設定する
	 * @param data データ
	 */
	public void setData(String data) {
		this.data = data;
	}
}
