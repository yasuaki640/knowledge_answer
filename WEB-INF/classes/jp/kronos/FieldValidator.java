package jp.kronos;

import java.util.ArrayList;
import java.util.List;

import jp.kronos.dto.ChannelDto;
import jp.kronos.dto.KnowledgeDto;
import jp.kronos.dto.UserDto;

public class FieldValidator {

	/**
	 * 入力チェック
	 * @param dto チャンネル情報
	 * @return エラーメッセージ
	 */
	public static List<String> channelValidation(ChannelDto dto) {
		List<String> errorMessageList = new ArrayList<>();
		
		if ("".equals(dto.getChannelName())) {
			errorMessageList.add("チャンネルを入力してください");
		}
		
		if (dto.getChannelName().length() > 30) {
			errorMessageList.add("チャンネルは30文字以内で入力してください");
		}
		
		if ("".equals(dto.getOverview())) {
			errorMessageList.add("概要を入力してください");
		}
		
		if (dto.getOverview().length() > 200) {
			errorMessageList.add("概要は200文字以内で入力してください");
		}
		
		return errorMessageList;
	}
	
	/**
	 * 入力チェック
	 * @param dto ナレッジ情報
	 * @return エラーメッセージ
	 */
	public static List<String> knowledgeValidation(KnowledgeDto dto) {
		List<String> errorMessageList = new ArrayList<>();
		
		if ("".equals(dto.getKnowledge())) {
			errorMessageList.add("ナレッジを入力してください");
		}
		
		return errorMessageList;
	}
	
	/**
	 * 入力チェック
	 * @param dto ユーザ情報
	 * @return エラーメッセージ
	 */
	public static List<String> userValidation(UserDto dto) {
		List<String> errorMessageList = new ArrayList<>();
		
		if ("".equals(dto.getEmail())) {
			errorMessageList.add("メールアドレスを入力してください");
		}
		
		if (dto.getEmail().length() > 255) {
			errorMessageList.add("メールアドレスは255文字以内で入力してください");
		}
		
		if ("".equals(dto.getLastName())) {
			errorMessageList.add("姓を入力してください");
		}
		
		if (dto.getLastName().length() > 30) {
			errorMessageList.add("姓は30文字以内で入力してください");
		}
		
		if (dto.getFirstName().length() > 30) {
			errorMessageList.add("名は30文字以内で入力してください");
		}
		
		return errorMessageList;
	}
	
}
