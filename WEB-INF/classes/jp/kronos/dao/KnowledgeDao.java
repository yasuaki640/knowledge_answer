package jp.kronos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.kronos.dto.KnowledgeDto;

/**
 * ナレッジテーブルのDataAccessObject
 * @author Mr.X
 */
public class KnowledgeDao {

	/** コネクション */
	protected Connection conn;

	/**
	 * コンストラクタ
	 * コネクションをフィールドに設定する
	 * @param conn コネクション
	 */
	public KnowledgeDao(Connection conn) {
		this.conn = conn;
	}

	public List<KnowledgeDto> selectAll() throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append("     select");
		sb.append("            KNOWLEDGE_ID");
		sb.append("           ,KNOWLEDGE");
		sb.append("           ,k.CHANNEL_ID");
		sb.append("           ,CHANNEL_NAME");
		sb.append("           ,k.USER_ID");
		sb.append("           ,k.UPDATE_AT");
		sb.append("           ,k.UPDATE_NUMBER");
		sb.append("       from KNOWLEDGE k");
		sb.append(" inner join CHANNEL c");
		sb.append("         on k.CHANNEL_ID = c.CHANNEL_ID");
		sb.append("   order by KNOWLEDGE_ID desc");

		List<KnowledgeDto> list = new ArrayList<>();
		
		// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			// SQL文を実行する
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KnowledgeDto dto = new KnowledgeDto();
				dto.setKnowledgeId(rs.getInt("KNOWLEDGE_ID"));
				dto.setKnowledge(rs.getString("KNOWLEDGE"));
				dto.setChannelId(rs.getInt("CHANNEL_ID"));
				dto.setChannelName(rs.getString("CHANNEL_NAME"));
				dto.setUserId(rs.getInt("USER_ID"));
				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
				list.add(dto);
			}
		}
		return list;
	}
	
	public List<KnowledgeDto> selectByChannelId(int channelId) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append("     select");
		sb.append("            KNOWLEDGE_ID");
		sb.append("           ,KNOWLEDGE");
		sb.append("           ,k.CHANNEL_ID");
		sb.append("           ,CHANNEL_NAME");
		sb.append("           ,k.USER_ID");
		sb.append("           ,k.UPDATE_AT");
		sb.append("           ,k.UPDATE_NUMBER");
		sb.append("       from KNOWLEDGE k");
		sb.append(" inner join CHANNEL c");
		sb.append("         on k.CHANNEL_ID = c.CHANNEL_ID");
		sb.append("      where k.CHANNEL_ID = ?");
		sb.append("   order by KNOWLEDGE_ID desc");

		List<KnowledgeDto> list = new ArrayList<>();
		
		// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			// プレースホルダーに値をセットする
			ps.setInt(1, channelId);
			
			// SQL文を実行する
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KnowledgeDto dto = new KnowledgeDto();
				dto.setKnowledgeId(rs.getInt("KNOWLEDGE_ID"));
				dto.setKnowledge(rs.getString("KNOWLEDGE"));
				dto.setChannelId(channelId);
				dto.setChannelName(rs.getString("CHANNEL_NAME"));
				dto.setUserId(rs.getInt("USER_ID"));
				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
				list.add(dto);
			}
		}
		return list;
	}

	public KnowledgeDto selectByKnowledgeId(int knowledgeId) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append("     select");
		sb.append("            KNOWLEDGE_ID");
		sb.append("           ,KNOWLEDGE");
		sb.append("           ,k.CHANNEL_ID");
		sb.append("           ,CHANNEL_NAME");
		sb.append("           ,k.USER_ID");
		sb.append("           ,k.UPDATE_AT");
		sb.append("           ,k.UPDATE_NUMBER");
		sb.append("       from KNOWLEDGE k");
		sb.append(" inner join CHANNEL c");
		sb.append("         on k.CHANNEL_ID = c.CHANNEL_ID");
		sb.append("      where KNOWLEDGE_ID = ?");

		KnowledgeDto dto = null;
		
		// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			// プレースホルダーに値をセットする
			ps.setInt(1, knowledgeId);
			
			// SQL文を実行する
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dto = new KnowledgeDto();
				dto.setKnowledgeId(knowledgeId);
				dto.setKnowledge(rs.getString("KNOWLEDGE"));
				dto.setChannelId(rs.getInt("CHANNEL_ID"));
				dto.setChannelName(rs.getString("CHANNEL_NAME"));
				dto.setUserId(rs.getInt("USER_ID"));
				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
			}
		}
		return dto;
	}
	
	public List<KnowledgeDto> selectByQueries(String[] queries) throws SQLException {
		
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append("     select");
		sb.append("            KNOWLEDGE_ID");
		sb.append("           ,KNOWLEDGE");
		sb.append("           ,k.CHANNEL_ID");
		sb.append("           ,CHANNEL_NAME");
		sb.append("           ,k.USER_ID");
		sb.append("           ,k.UPDATE_AT");
		sb.append("           ,k.UPDATE_NUMBER");
		sb.append("       from KNOWLEDGE k");
		sb.append(" inner join CHANNEL c");
		sb.append("         on k.CHANNEL_ID = c.CHANNEL_ID");
		sb.append("      where ");
		for (int i = 0; i < queries.length; i++) {
			if (i != 0) {
				sb.append(" and ");
			}
			sb.append("    KNOWLEDGE like ?");
		}
		sb.append("   order by KNOWLEDGE_ID desc");

		List<KnowledgeDto> list = new ArrayList<>();
		
		// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			// プレースホルダーに値をセットする
			for (int i = 0; i < queries.length; i++) {
				ps.setString(i + 1, "%" + queries[i] + "%");
			}
			
			// SQL文を実行する
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				KnowledgeDto dto = new KnowledgeDto();
				dto.setKnowledgeId(rs.getInt("KNOWLEDGE_ID"));
				dto.setKnowledge(rs.getString("KNOWLEDGE"));
				dto.setChannelId(rs.getInt("CHANNEL_ID"));
				dto.setChannelName(rs.getString("CHANNEL_NAME"));
				dto.setUserId(rs.getInt("USER_ID"));
				dto.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
				dto.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
				list.add(dto);
			}
		}
		return list;
	}

	public int selectCountByUserId(int userId) throws SQLException {

		// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" select");
		sb.append("        count(*)");
		sb.append("   from KNOWLEDGE");
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

	public void deleteByKnowledgeId(KnowledgeDto dto) throws SQLException {
		
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" delete from KNOWLEDGE");
		sb.append("       where KNOWLEDGE_ID = ?");
		sb.append("         and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
			ps.setInt(1, dto.getKnowledgeId());
			ps.setInt(2, dto.getUpdateNumber());

            // SQLを実行する
			ps.executeUpdate();
		}
	}

	public void deleteByChannelId(int channelId) throws SQLException {
		
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" delete from KNOWLEDGE");
		sb.append("       where CHANNEL_ID = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
			ps.setInt(1, channelId);

            // SQLを実行する
			ps.executeUpdate();
		}
	}

	public void insert(KnowledgeDto dto) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into KNOWLEDGE");
		sb.append("           (");
		sb.append("             KNOWLEDGE");
		sb.append("            ,CHANNEL_ID");
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
        try (PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS)) {

        	// プレースホルダーに値をセットする
            ps.setString(1, dto.getKnowledge());
            ps.setInt(2, dto.getChannelId());
            ps.setInt(3, dto.getUserId());

            // SQLを実行する
            ps.executeUpdate();
        }
	}

	public int update(KnowledgeDto dto) throws SQLException {

		// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" update");
		sb.append("        KNOWLEDGE");
		sb.append("    set");
		sb.append("        KNOWLEDGE = ?");
		sb.append("       ,UPDATE_NUMBER = UPDATE_NUMBER + 1");
		sb.append("  where KNOWLEDGE_ID = ?");
		sb.append("    and UPDATE_NUMBER = ?");
	
    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			
        	// プレースホルダーに値をセットする
			ps.setString(1, dto.getKnowledge());
			ps.setInt(2, dto.getKnowledgeId());
			ps.setInt(3, dto.getUpdateNumber());

            // SQLを実行する
			return ps.executeUpdate();
		}
	}
}
