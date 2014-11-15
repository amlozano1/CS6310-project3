package data.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {
	protected double setPrecision(double value, int decimalPlaces){
		BigDecimal bd = new BigDecimal(value);
		return bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
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
