package data.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {
	protected void close(ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(Statement stmnt){
		if(stmnt != null){
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close(ResultSet rs, Statement stmnt){
		close(rs);
		close(stmnt);
	}
}
