package de.svws_nrw.db;

/**
 * Diese Aufzählung repräsentiert die unterstützten bzw. zum Teil unterstützten
 * DBMS und deren Treiber.
 */
public enum DBDriver {
	
	/** Microsoft Access MDB */
	MDB,       

	/** Microsoft SQL Server */
	MSSQL,     
	
	/** MySQL */
	MYSQL,     
	
	/** Maria DB */
	MARIA_DB,  
	
	/** SQLite */
	SQLITE;    

	
	
	/**
	 * Gibt an, ob der Treiber für die Verwendung mit der SVWS-Datenbank
	 * unterstützt wird.
	 * 
	 * @return true, falls der Treiber zumindest zum Teil unterstützt 
	 *         wird und ansonsten false
	 */
	public boolean hasSupportSVWSDB() {
		switch (this) {
			case MSSQL:
			case MYSQL:
			case MARIA_DB:
			case SQLITE:
				return true;
			case MDB:
			default:
				return false;
		}
	}

	
	/**
	 * Gibt zurück, ob das DBMS ein einfaches Datei-basiertes DBMS ist.
	 * 
	 * @return true, falls das DBMS ein einfaches Datei-basiertes DBMS ist und ansonsten false
	 */
	public boolean isFileBased() {
		switch (this) {
			case MDB:
			case SQLITE:
				return true;
			default:
				return false;
		}		
	}
	
	
	/**
	 * Gibt zurück, ob das DBMS eine Unterstützung für mehrere Schemata hat.
	 * 
	 * @return true, falls das DBMS eine Unterstützung für mehrere Schemata hat und ansonsten false
	 */
	public boolean hasMultiSchemaSupport() {
		switch (this) {
			case MSSQL:
			case MYSQL:
			case MARIA_DB:
				return true;
			default:
				return false;
		}
	}
		
	
	/**
	 * Gibt zurück, ob das DBMS keine Unterstützung für eine Benutzerauthentifizierung hat.
	 * 
	 * @return true, falls es keine Unterstützung hat und ansonsten false
	 */
	public boolean noUserAuthenticationSupport() {
		switch (this) {
			case MDB:
			case SQLITE:
				return true;
			default:
				return false;
		}
	}
	
	
	/**
	 * Gibt zurück, ob das SQL des DBMS das Schlüsselwort "IF EXISTS" in der 
	 * Data Definition Language (DDL) unterstützt oder nicht 
	 * 
	 * @return true, falls "IF EXISTS" unterstützt wird und ansonsten false
	 */
	public boolean supportsIfExists() {
		switch (this) {
			case MYSQL:
			case MARIA_DB:
			case SQLITE:
				return true;
			default:
				return false;
		}		
	}
	
	
	/**
	 * Gibt die Default-Collation für das SVWS-Datenbankschema in
	 * Abhängigkeit des DBMS zurück.
	 *
	 * @return die Collation
	 */
	public String getCollation() {
		return switch(this) {
			case MARIA_DB -> "utf8mb4_bin";
			case MYSQL -> "utf8mb4_bin";
			case MSSQL -> "Latin1_General_100_BIN2_UTF8";
			default -> null;
		};
	}

	
	/**
	 * Gibt die Klasse des JDBC-Treibers als String zurück.
	 *  
	 * @return die Klasse des JDBC-Treibers als String
	 */
	public String getJDBCDriver() {
		switch (this) {
			case MDB:
				return "net.ucanaccess.jdbc.UcanaccessDriver";
			case MSSQL:
				return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			case MYSQL:
				return "com.mysql.cj.jdbc.Driver";
			case MARIA_DB:
				return "org.mariadb.jdbc.Driver";
			case SQLITE:
				return "org.sqlite.JDBC";
			default:
				return "org.mariadb.jdbc.Driver";
		}		
	}
	
	
	/**
	 * Gibt die JDBC-URL zurück.
	 * 
	 * @param location   der Ort, an dem sich die Datenbank befindet (z.B. localhost, ein andere Hostname ggf. mit Port oder ein Dateiname)
	 * @param schema     das Schema in der Datenbank, sofern mehrere Schemata vom DBMS unterstützt werden
	 * 
	 * @return die JDBC-URL
	 */
	public String getJDBCUrl(String location, String schema) {
		switch (this) {
			case MDB:
				return "jdbc:ucanaccess://" + location + ";memory=false;immediatelyReleaseResources=true";
			case MSSQL:
				return "jdbc:sqlserver://" + location + ";databaseName=" + schema;
			case MYSQL:
				return "jdbc:mysql://" + location + "/" + schema;
			case MARIA_DB:
				return "jdbc:mariadb://" + location + "/" + schema;
			case SQLITE:
				return "jdbc:sqlite:" + location;
			default:
				return "jdbc:mariadb://" + location + "/" + schema;
		}
	}
	
	
	/**
	 * Gibt den Namen des "root"-Schemas des DBMS zurück, sofern das DBMS eines hat.
	 * 
	 * @return der Namen des "root"-Schemas
	 */
	public String getRootSchema() {
		switch (this) {
			case MDB:
				return "PUBLIC";
			case MSSQL:
				return "master";
			case MYSQL:
				return "mysql";
			case MARIA_DB:
				return "mysql";
			case SQLITE:
				return "";
			default:
				return null;
		}		
	}

	
	/**
	 * Liefert zu dem Treibernamen als String das zugehörige Treiber-Objekt
	 * dieser Aufzählung
	 * 
	 * @param driverName   der Treiername als String
	 * 
	 * @return das Treiber-Objekt dieser Aufzählung
	 */
	public static DBDriver fromString(String driverName) {
		switch (driverName.toUpperCase()) {
			case "MDB":
				return MDB;
			case "MSSQL":
				return MSSQL;
			case "MYSQL":
				return MYSQL;
			case "MARIA_DB":
				return MARIA_DB;
			case "SQLITE":
				return SQLITE;
		}
		return null;
	}
	
	
	/**
	 * Prüft, ob dieser Treiber der gleiche ist, wie der durch den String übergebene
	 * Treiber.
	 * 
	 * @param driverName   der Name des Treiber zum Vergleichen
	 * 
	 * @return true, falls die Treiber übereinstimmen und ansonsten false
	 */
	public boolean equals(String driverName) {
		return this == fromString(driverName);
	}
	
}	
