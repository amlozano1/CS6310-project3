package data.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
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

    private static Connection conn;
    
    private JavaDBDatastore(){
    	try {
			create();
			if(isNew(getConnection())){
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
		BufferedReader reader = null;
		try {
			Connection connection =  getConnection();
			URL createScriptUrl = getClass().getClassLoader().getResource("createTables.sql");
			reader = new BufferedReader(new FileReader(new File(createScriptUrl.toURI())));
			for(String line = reader.readLine(); line != null; line = reader.readLine()){
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(line);
			}
			return true;
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (URISyntaxException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					log.log(Level.WARNING, e.getMessage(), e);

				}
			}
		}

		return false;
	}
    
	@Override
	public Connection getConnection() {
		try {
			if(conn == null || !conn.isClosed()){
				createConnection();
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return conn;
	}
	
	private void createConnection() throws SQLException{
		conn = DriverManager.getConnection(CONNECTION_URL);
	}
	
	private boolean isNew(Connection conn) throws SQLException{
		SQLWarning warnings = conn.getWarnings();
		while(warnings!=null){
			if(DB_EXISTS.equals(warnings.getSQLState()))
					return false;
			warnings = warnings.getNextWarning();
		}
		
		return true;
	}
}
