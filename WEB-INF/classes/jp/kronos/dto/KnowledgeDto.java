package jp.kronos.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class KnowledgeDto {

	/* ナレッジID */
	private int knowledgeId;

	/* ナレッジ */
	private String knowledge;

	/* チャンネルID */
	private int channelId;

	/* チャンネル名 */
	private String channelName;
	
	/* ユーザID */
	private int userId;

	/* 更新時刻 */
	private Timestamp updateAt;

	/* 更新番号 */
	private int updateNumber;

	/**
	 * ナレッジIDを取得する
	 * @return ナレッジID
	 */
	public int getKnowledgeId() {
		return knowledgeId;
	}

	/**
	 * ナレッジIDを設定する
	 * @param knowledgeId ナレッジID
	 */
	public void setKnowledgeId(int knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	/**
	 * ナレッジを取得する
	 * @return ナレッジ
	 */
	public String getKnowledge() {
		return knowledge;
	}

	/**
	 * ナレッジを設定する
	 * @param knowledge ナレッジ
	 */
	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	/**
	 * チャンネルIDを取得する
	 * @return チャンネルID
	 */
	public int getChannelId() {
		return channelId;
	}

	/**
	 * チャンネルIDを設定する
	 * @param channelId チャンネルID
	 */
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	/**
	 * チャンネル名を取得する
	 * @return チャンネル名
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * チャンネル名を設定する
	 * @param channelName チャンネル名
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * ユーザIDを取得する
	 * @return ユーザID
	 */
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 更新時刻を取得する
	 * @return 更新時刻
	 */
	public String getUpdateAt() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(updateAt);
//		return updateAt;
	}

	/**
	 * 更新時刻を設定する
	 * @param updateAt 更新時刻
	 */
	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

	/**
	 * 更新ユーザIDを取得する
	 * @return 更新ユーザID
	 */
	public int getUpdateNumber() {
		return updateNumber;
	}

	/**
	 * 更新ユーザIDを設定する
	 * @param updateNumber 更新ユーザID
	 */
	public void setUpdateNumber(int updateNumber) {
		this.updateNumber = updateNumber;
	}

}