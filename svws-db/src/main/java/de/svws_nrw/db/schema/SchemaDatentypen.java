package de.svws_nrw.db.schema;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBDriver;

/**
 * Diese Klasse beschreibt die unterschiedlichen grundlegenden Datentypen,
 * welche in einem SVWS-Datenbankschema vorkommen können.
 */
public enum SchemaDatentypen {

	/** Ein Boolean-Wert (intern als Integer mit 0/1 gespeichert) */
	BOOLEAN,

	/** Ganzzahl 16 Bit */
	SMALLINT,

	/** Ganzzahl 32 Bit */
	INT,

	/** Ganzzahl 64 Bit */
	BIGINT,

	/** Ein Fließkommawert */
	FLOAT,

	/** Eine Zeichenkette mit festgelegter Länge */
	CHAR,

	/** Eine Zeichenkette mit einer maximaler Länge */
	VARCHAR,

	/** Langer Text */
	TEXT,

	/** Eine Zeitangabe (ohne Datum) */
	TIME,

	/** Eine Datumsangabe */
	DATE,

	/** Eine Datums und Zeitangabe */
	DATETIME,

	/** Binärdaten */
	LONGBLOB;


	/** Eine Map für den schnellen Zugriff auf die einzelnen Elemente der Aufzählung anhand des Namens. */
	private static Map<String, SchemaDatentypen> _typen = null;


	/**
	 * Der Name zur eindeutigen und DBMS-unabhängigen Identidikation des Datentyps.
	 *
	 * @return der Name des Datentyps.
	 */
    public String getName() {
    	return this.name().toLowerCase();
    }

    /**
     * Gibt an, ob dies ein Datentyp für Ganzzahlen ist oder nicht.
     *
     * @return true, falls es sich um einen Datentyp für Ganzzahlen handelt und ansonsten false
     */
    public boolean isIntType() {
    	return switch (this) {
			case CHAR, VARCHAR, TEXT -> false;
			case DATE, TIME, DATETIME -> false;
    		case BOOLEAN, FLOAT, LONGBLOB -> false;
    		case SMALLINT, INT, BIGINT -> true;
    		default -> throw new IllegalArgumentException("Unexpected value: " + this);
    	};
    }

	/**
	 * Gibt an, ob Anführungszeichen im SQL-Code verwendet werden sollen, da es sich in SQL um Strings handelt
	 *
	 * @return true, falls es sich in SQL um einen String handelt.
	 */
    public boolean isQuoted() {
    	return switch (this) {
			case CHAR, VARCHAR, TEXT -> true;
			case DATE, TIME, DATETIME -> true;
    		case BOOLEAN, SMALLINT, INT, BIGINT, FLOAT, LONGBLOB -> false;
    		default -> throw new IllegalArgumentException("Unexpected value: " + this);
    	};
    }

	/**
	 * Gibt an, ob es sich um einen Typ für Zeichenketten handelt.
	 *
	 * @return true, falls es sich um einen Typ für Zeichenketten handelt und ansonsten false
	 */
    public boolean isCharString() {
    	return switch (this) {
			case CHAR, VARCHAR, TEXT -> true;
			case DATE, TIME, DATETIME -> false;
    		case BOOLEAN, SMALLINT, INT, BIGINT, FLOAT, LONGBLOB -> false;
    		default -> throw new IllegalArgumentException("Unexpected value: " + this);
    	};
    }

	/**
	 * Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS MariaDB.
	 *
	 * @return der Name des Datentyps für das DBMS MariaDB
	 */
    public String mariadb() {
    	return switch (this) {
			case TEXT -> "longtext";
			case BOOLEAN -> "int";
    		default -> this.name().toLowerCase();
    	};
    }

	/**
	 * Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS MySQL.
	 *
	 * @return der Name des Datentyps für das DBMS MySQL
	 */
    public String mysql() {
    	return switch (this) {
			case TEXT -> "longtext";
			case BOOLEAN -> "int";
    		default -> this.name().toLowerCase();
    	};
    }

	/**
	 * Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS Microsoft SQL Server.
	 *
	 * @return der Name des Datentyps für das DBMS Microsoft SQL Server
	 */
    public String mssql() {
    	return switch (this) {
    		case CHAR -> "nchar";
    		case DATETIME -> "datetime2";
			case LONGBLOB -> "varbinary(max)";
			case TEXT -> "nvarchar(max)";
			case VARCHAR -> "nvarchar";
			case BOOLEAN -> "int";
    		default -> this.name().toLowerCase();
    	};
    }

	/**
	 * Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS Microsoft SQLite.
	 *
	 * @return der Name des Datentyps für das DBMS SQLite
	 */
    public String sqlite() {
    	return switch (this) {
			case BOOLEAN -> "int";
    		default -> this.name().toLowerCase();
    	};
    }

	/**
	 * Der Name des Datentyps für die Verwendung im SQL-Code für das alte Microsoft Access Format mdb.
	 *
	 * @return der Name des Datentyps für das alte Microsoft Access Format mdb
	 */
    public String mdb() {
    	return switch (this) {
			case LONGBLOB -> "varbinary(16777216)";
			case BOOLEAN -> "int";
			case TIME -> "timestamp";
			default -> this.name().toLowerCase();
		};
    }

	/**
	 * Der Name des Datentyps für die Verwendung in Java als elementaren Datentyp (ohne Einsatz von JPA-Konvertern)
	 *
	 * @return der Name des unter Java zu verwendenden Datentyps (ohne Einsatz von JPA-Konvertern)
	 */
    public String java() {
    	return switch (this) {
			case BIGINT -> "Long";
			case CHAR -> "String";
			case DATE -> "String";
			case DATETIME -> "String";
			case FLOAT -> "Double";
			case INT -> "Integer";
			case LONGBLOB -> "byte[]";
			case SMALLINT -> "Integer";
			case TEXT -> "String";
			case VARCHAR -> "String";
			case BOOLEAN -> "Boolean";
			case TIME -> "String";
		};
    }



	/**
	 * Liefert den speziellen SQL-Datentyp für das angegebene DBMS
	 *
	 * @param dbms     das DBMS
	 *
	 * @return der spezielle SQL-Datentyp des angegebenen DBMS
	 */
    public String getDBType(final DBDriver dbms) {
		switch (dbms) {
			case MARIA_DB:
				return this.mariadb();
			case MDB:
				return this.mdb();
			case MSSQL:
				return this.mssql();
			case MYSQL:
				return this.mysql();
			case SQLITE:
				return this.sqlite();
			default:
				return null;
		}
    }

    /**
     * Liefert den Datentyp anhand des übergebenen Namens zurück.
     *
     * @param name   der Name des Datentyps
     *
     * @return der Datentyp
     */
    public static SchemaDatentypen getByName(final String name) {
    	if (_typen == null)
    		_typen = Arrays.stream(SchemaDatentypen.values()).collect(Collectors.toMap(t -> t.getName(), t -> t));
    	return _typen.get(name);
    }

}
