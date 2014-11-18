package data.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

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
	
	protected Time toTime(long simulationTime) {
		// TODO Auto-generated method stub
		return new Time(0);
	}

	protected Date toDate(long simulationTime) {
		// TODO Auto-generated method stub
		return new Date(0);
	}
	
	protected long toSimulationTime(Date date, Time time) {
		// TODO Auto-generated method stub
		return 0;
	}
}
