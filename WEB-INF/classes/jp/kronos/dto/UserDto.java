package jp.kronos.dto;

import java.sql.Timestamp;

/**
 * ユーザ情報
 * @author Mr.X
 */
public class UserDto {
	
	/* ユーザID */
	private int userId;
	
	/* eメール */
	private String email;
	
	/* パスワード */
	private String password;

	/* 名 */
	private String firstName;

	/* 姓 */
	private String lastName;
	
	/* 管理権限フラグ */
	private boolean administratorFlg;

	/* 更新ユーザID */
	private int updateUserId;
	
	/* 更新時刻 */
	private Timestamp updateAt;
	
	/* 更新番号 */
	private int updateNumber;

	/* 削除フラグ */
	private boolean delFlg;

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
	 * eメールを取得する
	 * @return eメール
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * eメールを設定する
	 * @param email eメール
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * パスワードを取得する
	 * @return パスワード
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * パスワードを設定する
	 * @param password パスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 名を取得する
	 * @return 名
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 名を設定する
	 * @param firstName 名
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 姓を取得する
	 * @return 姓
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 姓を設定する
	 * @param lastName 姓
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 管理権限フラグを取得する
	 * @return 管理権限フラグ
	 */
	public boolean isAdministratorFlg() {
		return administratorFlg;
	}

	/**
	 * 管理権限フラグを設定する
	 * @param administratorFlg 管理権限フラグ
	 */
	public void setAdministratorFlg(boolean administratorFlg) {
		this.administratorFlg = administratorFlg;
	}
	
	/**
	 * 更新ユーザIDを取得する
	 * @return 更新ユーザID
	 */
	public int getUpdateUserId() {
		return updateUserId;
	}

	/**
	 * 更新ユーザIDを設定する
	 * @param updateUserId 更新ユーザID
	 */
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
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

	/**
	 * 削除フラグを取得する
	 * @return 削除フラグ
	 */
	public boolean isDelFlg() {
		return delFlg;
	}

	/**
	 * 削除フラグを設定する
	 * @param delFlg 削除フラグ
	 */
	public void setDelFlg(boolean delFlg) {
		this.delFlg = delFlg;
	}
	
}
