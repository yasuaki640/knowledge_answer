package jp.kronos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jp.kronos.dto.UserDto;

/**
 * ナレッジテーブルのDataAccessObject
 * @author Mr.X
 */
public class UserDao {

	/** コネクション */
	protected Connection conn;

	/**
	 * コンストラクタ
	 * コネクションをフィールドに設定する
	 * @param conn コネクション
	 */
    public UserDao(Connection conn) {
        this.conn = conn;
    }
    
    public UserDto findByIdAndPassword(String id, String password) throws SQLException {

    	// SQL文を作成する
    	StringBuffer sb = new StringBuffer();
    	sb.append(" select");
    	sb.append("        USER_ID");
    	sb.append("       ,EMAIL");
    	sb.append("       ,FIRST_NAME");
    	sb.append("       ,LAST_NAME");
    	sb.append("       ,ADMINISTRATOR_FLG");
    	sb.append("       ,UPDATE_USER_ID");
    	sb.append("       ,UPDATE_AT");
    	sb.append("       ,UPDATE_NUMBER");
    	sb.append("       ,DEL_FLG");
    	sb.append("   from USER");
    	sb.append("  where EMAIL = ?");
    	sb.append("    and PASSWORD = sha2(?, 256)");
    	sb.append("    and DEL_FLG = 0");

    	// ステートメントオブジェクトを作成する
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
            ps.setString(1, id);
            ps.setString(2, password);
            
            // SQLを実行する
            ResultSet rs = ps.executeQuery();
            
            // 結果をDTOに詰める
            if (rs.next()) {
                UserDto user = new UserDto();
                user.setUserId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("EMAIL"));
                user.setFirstName(rs.getString("FIRST_NAME"));
                user.setLastName(rs.getString("LAST_NAME"));
                user.setAdministratorFlg(rs.getBoolean("ADMINISTRATOR_FLG"));
                user.setUpdateUserId(rs.getInt("UPDATE_USER_ID"));
                user.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
                user.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
                user.setDelFlg(rs.getBoolean("DEL_FLG"));
                return user;
            }
            // 該当するデータがない場合はnullを返却する
        	return null;
        }
    }

	public ArrayList<UserDto> selectAll() throws SQLException {
		
    	// SQL文を作成する
    	StringBuffer sb = new StringBuffer();
    	sb.append("   select");
    	sb.append("          USER_ID");
    	sb.append("         ,EMAIL");
    	sb.append("         ,FIRST_NAME");
    	sb.append("         ,LAST_NAME");
    	sb.append("         ,ADMINISTRATOR_FLG");
    	sb.append("         ,UPDATE_USER_ID");
    	sb.append("         ,UPDATE_AT");
    	sb.append("         ,UPDATE_NUMBER");
    	sb.append("         ,DEL_FLG");
    	sb.append("     from USER");
    	sb.append("    where DEL_FLG = 0");
    	sb.append(" order by USER_ID");

    	// ステートメントオブジェクトを作成する
        try (Statement st = conn.createStatement()) {
            
            // SQLを実行する
            ResultSet rs = st.executeQuery(sb.toString());
            
            ArrayList<UserDto> list = new ArrayList<UserDto>();
            // 結果をDTOに詰める
            while (rs.next()) {
                UserDto user = new UserDto();
                user.setUserId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("EMAIL"));
                user.setFirstName(rs.getString("FIRST_NAME"));
                user.setLastName(rs.getString("LAST_NAME"));
                user.setAdministratorFlg(rs.getBoolean("ADMINISTRATOR_FLG"));
                user.setUpdateUserId(rs.getInt("UPDATE_USER_ID"));
                user.setUpdateAt(rs.getTimestamp("UPDATE_AT"));
                user.setUpdateNumber(rs.getInt("UPDATE_NUMBER"));
                user.setDelFlg(rs.getBoolean("DEL_FLG"));
                list.add(user);
            }
            // 該当するデータがない場合はnullを返却する
        	return list;
        }
	}

	public int insert(UserDto dto) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into USER");
		sb.append("           (");
		sb.append("             EMAIL");
		sb.append("            ,PASSWORD");
		sb.append("            ,FIRST_NAME");
		sb.append("            ,LAST_NAME");
		sb.append("            ,ADMINISTRATOR_FLG");
		sb.append("            ,UPDATE_USER_ID");
		sb.append("            ,UPDATE_NUMBER");
		sb.append("           )");
		sb.append("      values");
		sb.append("           (");
		sb.append("             ?");
		sb.append("            ,sha2(?, 256)");
		sb.append("            ,?");
		sb.append("            ,?");
		sb.append("            ,?");
		sb.append("            ,?");
		sb.append("            ,0");
		sb.append("        )");

    	// ステートメントオブジェクトを作成する
        try (PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS)) {

        	// プレースホルダーに値をセットする
            ps.setString(1, dto.getEmail());
            ps.setString(2, dto.getPassword());
            ps.setString(3, dto.getFirstName());
            ps.setString(4, dto.getLastName());
            ps.setBoolean(5, dto.isAdministratorFlg());
            ps.setInt(6, dto.getUpdateUserId());

            // SQLを実行する
            ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}

			return 0;
        }
	}

	public int delete(UserDto dto) throws SQLException {
		
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" delete from USER");
		sb.append("       where USER_ID = ?");
		sb.append("         and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
        	// プレースホルダーに値をセットする
			ps.setInt(1, dto.getUserId());
			ps.setInt(2, dto.getUpdateNumber());
			
            // SQLを実行する
			return ps.executeUpdate();
		}
	}
	
	public int updatePassword(UserDto dto) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" update USER");
		sb.append("   set");
		sb.append("        PASSWORD = sha2(?, 256)");
		sb.append("       ,UPDATE_USER_ID = ?");
		sb.append("       ,UPDATE_NUMBER = UPDATE_NUMBER + 1");
		sb.append("  where USER_ID = ?");
		sb.append("    and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			
        	// プレースホルダーに値をセットする
			ps.setString(1, dto.getPassword());
			ps.setInt(2, dto.getUpdateUserId());
			ps.setInt(3, dto.getUserId());
			ps.setInt(4, dto.getUpdateNumber());
			
            // SQLを実行する
			return ps.executeUpdate();
		}
	}

	public int update(UserDto dto) throws SQLException {
	
    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" update USER");
		sb.append("    set");
		sb.append("        EMAIL = ?");
		sb.append("       ,FIRST_NAME = ?");
		sb.append("       ,LAST_NAME = ?");
		sb.append("       ,ADMINISTRATOR_FLG = ?");
		sb.append("       ,UPDATE_USER_ID = ?");
		sb.append("       ,UPDATE_NUMBER = UPDATE_NUMBER + 1");
		sb.append("  where USER_ID = ?");
		sb.append("    and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			
        	// プレースホルダーに値をセットする
			ps.setString(1, dto.getEmail());
			ps.setString(2, dto.getFirstName());
			ps.setString(3, dto.getLastName());
			ps.setBoolean(4, dto.isAdministratorFlg());
			ps.setInt(5, dto.getUpdateUserId());
			ps.setInt(6, dto.getUserId());
			ps.setInt(7, dto.getUpdateNumber());
			
            // SQLを実行する
			return ps.executeUpdate();
		}
	}

	public int updateAdministratorFlg(UserDto dto) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" update USER");
		sb.append("    set");
		sb.append("        ADMINISTRATOR_FLG = ?");
		sb.append("       ,UPDATE_USER_ID = ?");
		sb.append("       ,UPDATE_NUMBER = UPDATE_NUMBER + 1");
		sb.append("  where USER_ID = ?");
		sb.append("    and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			
        	// プレースホルダーに値をセットする
			ps.setBoolean(1, dto.isAdministratorFlg());
			ps.setInt(2, dto.getUpdateUserId());
			ps.setInt(3, dto.getUserId());
			ps.setInt(4, dto.getUpdateNumber());
			
            // SQLを実行する
			return ps.executeUpdate();
		}
	}

	public int updateDelFlg(UserDto dto) throws SQLException {

    	// SQL文を作成する
		StringBuffer sb = new StringBuffer();
		sb.append(" update USER");
		sb.append("    set");
		sb.append("        DEL_FLG = ?");
		sb.append("       ,UPDATE_USER_ID = ?");
		sb.append("       ,UPDATE_NUMBER = UPDATE_NUMBER + 1");
		sb.append("  where USER_ID = ?");
		sb.append("    and UPDATE_NUMBER = ?");

    	// ステートメントオブジェクトを作成する
		try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
			
        	// プレースホルダーに値をセットする
			ps.setBoolean(1, dto.isDelFlg());
			ps.setInt(2, dto.getUpdateUserId());
			ps.setInt(3, dto.getUserId());
			ps.setInt(4, dto.getUpdateNumber());
			
            // SQLを実行する
			return ps.executeUpdate();
		}
	}
}
