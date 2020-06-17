package jp.kronos.dto;

import java.sql.Timestamp;

/**
 * チャンネル情報
 * @author Mr.X
 */
public class ChannelDto {

	/* チャンネルID */
	private int channelId;
	
	/* チャンネル名 */
	private String channelName;
	
	/* 概要 */
	private String overview;
	
	/* ユーザID */
	private int userId;
	
	/* 更新時刻 */
	private Timestamp updateAt;

	/* 更新番号 */
	private int updateNumber;

	/**
	 * チャンネルIDを取得する
	 * @return the channelId
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
	 * 概要を取得する
	 * @return 概要
	 */
	public String getOverview() {
		return overview;
	}

	/**
	 * 概要を設定する
	 * @param overview 概要
	 */
	public void setOverview(String overview) {
		this.overview = overview;
	}

	/**
	 * ユーザIDを取得する
	 * @return ユーザID
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * ユーザIDを設定する
	 * @param userId ユーザID
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 更新時刻を取得する
	 * @return 更新時刻
	 */
	public Timestamp getUpdateAt() {
		return updateAt;
	}

	/**
	 * 更新時刻を設定する
	 * @param updateAt 更新時刻
	 */
	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

	/**
	 * 更新番号を取得する
	 * @return 更新番号
	 */
	public int getUpdateNumber() {
		return updateNumber;
	}

	/**
	 * 更新番号を設定する
	 * @param updateNumber 更新番号
	 */
	public void setUpdateNumber(int updateNumber) {
		this.updateNumber = updateNumber;
	}
	
}
