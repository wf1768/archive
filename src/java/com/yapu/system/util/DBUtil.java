package com.yapu.system.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	private Connection conn = null;

	public void getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			String URL = "jdbc:sqlserver://192.168.1.224:2115; DatabaseName=hkarchive"; // 连接服务器和数据库test
			String USER = "sa"; // 根据你自己设置的数据库连接用户进行设置
			String PASSWORD = "sa"; // 根据你自己设置的数据库连接密码进行设置
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testConnection() {
		if (conn == null)
			this.getConnection();
		try {
			//EDOC_Property:文档类型，1.发文，2.收文，3.内请
			//isend:1读取；读取完要update成0
			//表：a_74384a4b_02
			String sql = "SELECT Archive_ID,DocNO,Title,SubmitUnit,SubmitUser,SubmitDate,CenterName,signuser,signcomment,checkuser,checkcomment,meetcomment,leadercomment,doccomeunit,sender FROM EDoc_Archive where isend=1 and EDOC_Property=1";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("Archive_ID"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}
	}

	public static void main(String[] srg) {
		DBUtil db = new DBUtil();
		db.testConnection();
		
		
//		
//		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // 加载JDBC驱动
//		String dbURL = "jdbc:sqlserver://192.168.1.224:2115; DatabaseName=hkarchive"; // 连接服务器和数据库test
//		String userName = "sa"; // 默认用户名
//		String userPwd = "sa"; // 密码
//		Connection dbConn;
//		try {
//			Class.forName(driverName).newInstance();
//			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
//			System.out.println("Connection Successful!"); // 如果连接成功
//			// 控制台输出Connection
//			// Successful!
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
/*SELECT Archive_ID AS ID,
DocNO AS WJH,
Title AS TM,
SubmitUnit AS CBDW,
SubmitUser AS JBR,
SubmitDate AS WJRQ,
CenterName AS ZXMC,
signuser AS CBSWQBR,
signcomment AS CBSWQBYJ,
checkuser AS CBSWHGR,
checkcomment AS CBSWHGYJ,
meetcomment AS HQ,
leadercomment AS LDPS,
doccomeunit AS LWDW,
sender AS CS
FROM EDoc_Archive*/