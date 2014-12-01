package data.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Datastore;

public class JavaDBDatastore extends Datastore{
	private static final Logger log = Logger.getLogger(JavaDBDatastore.class.getName());
	private static final JavaDBDatastore SINGLETON = new JavaDBDatastore();
	
	public static final String DB_EXISTS		= "01J01";
    public static final String DRIVER 			= "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String DBNAME 			= "heatedplanetp3t1-db";
    public static final String CONNECTION_URL	= "jdbc:derby:" + DBNAME + ";create=true";

    private static ThreadLocal<Connection> threadLocalConn;
    
    private JavaDBDatastore(){
    	try {
			create();
			if(isNew()){
				init();
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
    }
    
    public static final JavaDBDatastore getInstance(){
    	return SINGLETON;
    }
    
    @Override
	protected boolean create() {
		try {
			threadLocalConn = new ThreadLocal<Connection>();
			Class.forName(DRIVER);
			getConnection();
			return true;
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}

    @Override
	protected boolean init() {
		try {
			Connection connection =  getConnection();
			for(String sql :  CreateSQL.CREATE_SQL){
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
			}
			return true;
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return false;
	}
    
	@Override
	public Connection getConnection() {
		try {
			if(threadLocalConn.get() == null){
				createConnection();
			} else if(threadLocalConn.get().isClosed()){
				threadLocalConn.remove();
				createConnection();
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			throw new IllegalAccessError("Unable to connect to data store");
		}
		
		return threadLocalConn.get();
	}
	
	private void createConnection() throws SQLException{
		Connection c = DriverManager.getConnection(CONNECTION_URL);
		threadLocalConn.set(c);
	}
	
	private boolean isNew() throws SQLException{
		List<String> tableNames = getTableNames();
		
		return tableNames == null || tableNames.size() == 0;
	}
}
