package jp.kronos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.kronos.dto.ChannelDto;

/**
 * チャンネルテーブルのDataAccessObject
 * @author Mr.X
 */
public class ChannelDao {

	/** コネクション */
	protected Connection conn;

	/**
	 * コンストラクタ
	 * コネクションをフィールドに設定する
	 * @param conn コネクション
	 */
	public ChannelDao(Connection conn) {
        this.conn = conn;
    }

	/**
	 * チャンネル情報を取得する
	 * @return チャンネル情報リスト
	 * @throws SQLException SQL例外
	 */
	public List<ChannelDto> selectAll() throws SQLException {

		// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append("   select");
		sb.append("          CHANNEL_ID");
		sb.append("         ,CHANNEL_NAME");
		sb.append("         ,OVERVIEW");
		sb.append("         ,USER_ID");
		sb.append("         ,UPDATE_AT");
		sb.append("         ,UPDATE_NUMBER");
		sb.append("     from CHANNEL");
		sb.append(" order by CHANNEL_ID");

		List<ChannelDto> list = new ArrayList<>();
    	// ステートメントオブジェクトを作成する
		try (Statement stmt = conn.createStatement()) {
		
			// SQL文を実行する
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				ChannelDto dto = new ChannelDto();
				dto.setChannelId(rs.getInt("CHANNEL_ID"));
				dto.setChannelName(rs.getString("CHANNEL_NAME"));
				dto.setOverview(rs.getString("OVERVIEW"));
				dto.setUserId(rs.getInt("USER_ID"));
				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
				list.add(dto);
			}
			return list;
		}
	}

	/**
	 * ユーザが作成したチャンネルの件数を取得する
	 * @param userId ユーザID
	 * @return チャンネル件数
	 * @throws SQLException SQL例外
	 */
	public int selectCountByUserId(int userId) throws SQLException {

		// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" select");
		sb.append("        count(*)");
		sb.append("   from CHANNEL");
		sb.append("  where USER_ID = ?");
		
    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
			ps.setInt(1, userId);
			
			// SQL文を実行する
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				// 該当件数を返却する
				return rs.getInt(1);
			}
		}
        // 該当するデータがない場合は0を返却する
		return 0;
	}

	/**
	 * チャンネル情報を取得する
	 * @param channelId　チャンネルID
	 * @return チャンネル情報
	 * @throws SQLException SQL例外
	 */
	public ChannelDto selectByChannelId(int channelId) throws SQLException {

		// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" select");
		sb.append("        CHANNEL_ID");
		sb.append("       ,CHANNEL_NAME");
		sb.append("       ,OVERVIEW");
		sb.append("       ,USER_ID");
		sb.append("       ,UPDATE_AT");
		sb.append("       ,UPDATE_NUMBER");
		sb.append("   from CHANNEL");
		sb.append("  where CHANNEL_ID = ?");
		
		ChannelDto dto = null;
		
    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
			ps.setInt(1, channelId);

			// SQL文を実行する
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dto = new ChannelDto();
				dto.setChannelId(rs.getInt("CHANNEL_ID"));
				dto.setChannelName(rs.getString("CHANNEL_NAME"));
				dto.setOverview(rs.getString("OVERVIEW"));
				dto.setUserId(rs.getInt("USER_ID"));
				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
			}
		}
		return dto;
	}

	/**
	 * チャンネル情報を取得する
	 * @param channelId　チャンネルID
	 * @param userId ユーザID
	 * @return チャンネル情報
	 * @throws SQLException SQL例外
	 */
//	public ChannelDto selectByChannelIdAndUserId(int channelId, int userId) throws SQLException {
//
//		// SQL文を作成する
//		StringBuffer sb = new StringBuffer();
//		sb.append(" select");
//		sb.append("        CHANNEL_ID");
//		sb.append("       ,CHANNEL_NAME");
//		sb.append("       ,OVERVIEW");
//		sb.append("       ,USER_ID");
//		sb.append("       ,UPDATE_AT");
//		sb.append("       ,UPDATE_NUMBER");
//		sb.append("   from CHANNEL");
//		sb.append("  where CHANNEL_ID = ?");
//		sb.append("    and USER_ID = ?");
//		
//		ChannelDto dto = null;
//		
//    	// ステートメントオブジェクトを作成する
//		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
//        	// プレースホルダーに値をセットする
//			ps.setInt(1, channelId);
//			ps.setInt(2, userId);
//
//			// SQL文を実行する
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				dto = new ChannelDto();
//				dto.setChannelId(rs.getInt("CHANNEL_ID"));
//				dto.setChannelName(rs.getString("CHANNEL_NAME"));
//				dto.setOverview(rs.getString("OVERVIEW"));
//				dto.setUserId(rs.getInt("USER_ID"));
//				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
//				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
//			}
//		}
//		return dto;
//	}

	/**
	 * チャンネル情報を削除する
	 * <pre>物理削除する</pre>
	 * @param dto チャンネル情報
	 * @return 更新件数
	 * @throws SQLException SQL例外
	 */
	public int delete(ChannelDto dto) throws SQLException {
		
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" delete from CHANNEL");
		sb.append("       where CHANNEL_ID = ?");
		sb.append("         and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
			ps.setInt(1, dto.getChannelId());
			ps.setInt(2, dto.getUpdateNumber());

            // SQLを実行する
			return ps.executeUpdate();
		}
	}

	/**
	 * チャンネル情報を追加する
	 * @param dto チャンネル情報
	 * @return 更新件数
	 * @throws SQLException SQL例外
	 */
	public int insert(ChannelDto dto) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into CHANNEL");
		sb.append("           (");
		sb.append("             CHANNEL_NAME");
		sb.append("            ,OVERVIEW");
		sb.append("            ,USER_ID");
		sb.append("            ,UPDATE_NUMBER");
		sb.append("           )");
		sb.append("      values");
		sb.append("           (");
		sb.append("             ?");
		sb.append("            ,?");
		sb.append("            ,?");
		sb.append("            ,0");
		sb.append("           )");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {

			// プレースホルダーに値をセットする
			ps.setString(1, dto.getChannelName());
			ps.setString(2, dto.getOverview());
			ps.setInt(3, dto.getUserId());

			// SQLを実行する
			return ps.executeUpdate();
		}
	}

	/**
	 * チャンネル情報を更新する
	 * @param dto チャンネル情報
	 * @return 更新件数
	 * @throws SQLException SQL例外
	 */
	public int update(ChannelDto dto) throws SQLException {
	
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" update");
		sb.append("        CHANNEL");
		sb.append("    set");
		sb.append("        CHANNEL_NAME = ?");
		sb.append("       ,OVERVIEW = ?");
		sb.append("       ,UPDATE_NUMBER = UPDATE_NUMBER + 1");
		sb.append("  where CHANNEL_ID = ?");
		sb.append("    and UPDATE_NUMBER = ?");
	
    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			
        	// プレースホルダーに値をセットする
			ps.setString(1, dto.getChannelName());
			ps.setString(2, dto.getOverview());
			ps.setInt(3, dto.getChannelId());
			ps.setInt(4, dto.getUpdateNumber());

            // SQLを実行する
			return ps.executeUpdate();
		}
	}
}
