package data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Datastore {
	private static final String[] TYPES = new String[] { "TABLE" };
	private static final String METADATA_TABLE_CAT 		= "TABLE_CAT"; //String => table catalog (may be null)
	private static final String METADATA_TABLE_SCHEM	= "TABLE_SCHEM"; // String => table schema (may be null)
	private static final String METADATA_TABLE_NAME		= "TABLE_NAME"; // String => table name
	private static final String METADATA_TABLE_TYPE		= "TABLE_TYPE"; // String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	private static final String METADATA_REMARKS		= "REMARKS"; // String => explanatory comment on the table
	private static final String METADATA_TYPE_CAT		= "TYPE_CAT"; // String => the types catalog (may be null)
	private static final String METADATA_TYPE_SCHEM		= "TYPE_SCHEM"; // String => the types schema (may be null)
	private static final String METADATA_TYPE_NAME		= "TYPE_NAME"; // String => type name (may be null)
	private static final String METADATA_SELF_REFERENCING_COL_NAME= "SELF_REFERENCING_COL_NAME"; // String => name of the designated "identifier" column of a typed table (may be null)
	private static final String METADATA_REF_GENERATION	= "REF_GENERATION"; // String => specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)

	public abstract Connection getConnection();

	protected boolean tableExists(String tableName) throws SQLException {
		boolean exists = false;

		DatabaseMetaData meta = getConnection().getMetaData();
		ResultSet rs = meta.getTables(null, null, null, TYPES);
		while (rs.next()) {
			if (tableName.equalsIgnoreCase(rs.getString(METADATA_TABLE_NAME))) {
				exists = true;
				break;
			}
		}
		
		return exists;
	}
}
