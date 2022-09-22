package de.nrw.schule.svws.db.schema.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;
import de.nrw.schule.svws.db.DBDriver;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Datentypen.
 */
public class Datentypen {

	/** Der Name, welche einen Datentyp der SVWS-DB eindeutig und DBMS-unabhängig identifiziert */
    @JsonProperty public String Name;
	
	/** Gibt an, ob Anfürhungszeichen im SQL-Code verwendet werden sollen, da es sich in SQL um Strings handelt */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean isQuoted;

	/** Gibt an, ob es sich um einen Char-String-Typ handelt */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean isCharString;

	/** Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS Maria-DB */
    @JsonProperty public String mariadb;

	/** Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS MySQL */
    @JsonProperty public String mysql;
	
	/** Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS Microsoft SQL Server */
    @JsonProperty public String mssql;
	
	/** Der Name des Datentyps für die Verwendung im SQL-Code für das DBMS Microsoft SQL Server */
    @JsonProperty public String sqlite;
	
	/** Der Name des Datentyps für die Verwendung im SQL-Code für das alte Microsoft Access Format mdb */
    @JsonProperty public String mdb;

	/** Der Name des Datentyps für die Verwendung in Java als elementaren Datentyp (ohne Einsatz von JPA-Konvertern) */
    @JsonProperty public String java;
    

    
	/**
	 * Liefert den speziellen SQL-Datentyp für das angegebene DBMS
	 *  
	 * @param dbms     das DBMS
	 * 
	 * @return der spezielle SQL-Datentyp des angegebenen DBMS
	 */
    @JsonIgnore
    public String getDBType(DBDriver dbms) {
		switch (dbms) {
			case MARIA_DB:
				return this.mariadb;
			case MDB:
				return this.mdb;
			case MSSQL:
				return this.mssql;
			case MYSQL:
				return this.mysql;
			case SQLITE:
				return this.sqlite;
			default:
				return null;
		}    	
    }

}
