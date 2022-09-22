package de.nrw.schule.svws.db.schema.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.nrw.schule.svws.db.DBEntityManager;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;


/**
 * Diese Klasse definiert ein DTO für Informationen des DBMS zu den Spalten von Tabellen.
 */
@Entity
@Table(name = "INFORMATION_SCHEMA.COLUMNS")
@Cacheable(DBEntityManager.use_db_caching)
@NamedNativeQuery(name="DTOInformationSchemaTableColumn.mysql", query="SELECT TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=? AND TABLE_NAME=?", resultClass=DTOInformationSchemaTableColumn.class)
@NamedNativeQuery(name="DTOInformationSchemaTableColumn.mdb", query="SELECT TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_CATALOG = 'PUBLIC' AND TABLE_SCHEMA='PUBLIC' AND TABLE_NAME=?", resultClass=DTOInformationSchemaTableColumn.class)
@NamedNativeQuery(name="DTOInformationSchemaTableColumn.mssql", query="SELECT DISTINCT TABLE_NAME, COLUMN_NAME, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_CATALOG=? AND TABLE_NAME=?", resultClass=DTOInformationSchemaTableColumn.class)
@NamedNativeQuery(name="DTOInformationSchemaTableColumn.sqlite", query="SELECT ? AS TABLE_NAME, name AS COLUMN_NAME, cid + 1 AS ORDINAL_POSITION, dflt_value AS COLUMN_DEFAULT, CASE \"notnull\" WHEN 0 THEN 'YES' ELSE 'NO' END AS IS_NULLABLE, CASE WHEN instr(type, '(') = 0 OR instr(type, ')') = 0 THEN type ELSE substr(type, 0, instr(type, '(')) END AS DATA_TYPE, CASE WHEN instr(type, '(') = 0 OR instr(type, ')') = 0 THEN NULL ELSE substr(type, instr(type, '(') + 1, instr(type, ')') - instr(type, '(') - 1) END AS CHARACTER_MAXIMUM_LENGTH, * FROM pragma_table_info((?))", resultClass=DTOInformationSchemaTableColumn.class)
public class DTOInformationSchemaTableColumn {
	
	/** Der Name der Tabelle */
	@Column(name = "TABLE_NAME")
	private String NameTabelle;

	/** Der Name der Tabellenspalte */
	@Id
	@Column(name = "COLUMN_NAME")
	private String Name;	
	
	/** Die Reihenfolge der Spalte in Bezug auf alle Spalten der Tabelle */
	@Column(name = "ORDINAL_POSITION")
	private Integer Reihenfolge;

	/** Der Default-Wert der Spalte */
	@Column(name = "COLUMN_DEFAULT")
	private String DefaultValue;
	
	/** Gibt an, ob die Spalte auch NULL enthalten darf. */
	@Column(name = "IS_NULLABLE")
	private String Nullable;
	
	/** Der Datentyp der Spalte */
	@Column(name = "DATA_TYPE")
	private String DataType;
	
	/** Die maximale Länge der Daten der Spalte (Anzahl der Zeichen) */
	@Column(name = "CHARACTER_MAXIMUM_LENGTH")
	private Long DataLength;
	
	
	/**
	 * Default-Konstruktor für das Erzeugen dieser DBEntity 
	 */
	private DTOInformationSchemaTableColumn() {
	}

	
	/**
	 * Gibt den Namen der Tabelle zurück, zu der die Spalte gehört.
	 * 
	 * @return der Tabellenname
	 */
	public String getNameTabelle() {
		return NameTabelle;
	}

	
	/**
	 * Gibt den Namen der Spalte zurück.
	 * 
	 * @return der Spaltenname
	 */
	public String getName() {
		return Name;
	}


	/**
	 * Gibt die Position der Spalte in der Reihenfolge an, wie die Spalten defininiert wurden.
	 * 
	 * @return die Position der Spalte in der Reihenfolge, wie die Spalten defininiert wurden
	 */
	public Integer getReihenfolge() {
		return Reihenfolge;
	}


	/**
	 * Gibt den Default-Wert für diese Spalte zurück.
	 * 
	 * @return der Default-Wert
	 */
	public String getDefaultValue() {
		return DefaultValue;
	}

	
	/**
	 * Gibt zurück, ob die Spalte Null-Werte zulässt oder nicht
	 * 
	 * @return die Information, ob die Spalte Null-Werte zulässt oder nicht
	 */
	public String getNullable() {
		return Nullable;
	}


	/**
	 * Gibt den DBMS-spezifischen Datentyp der Spalte zurück.
	 * 
	 * @return der DBMS-spezifische Datentyp der Spalte 
	 */
	public String getDataType() {
		return DataType;
	}


	/**
	 * Gibt die maximale Länge von Daten in der Spalte zurück. 
	 * 
	 * @return die maximal zulässige Länge der Daten
	 */
	public Long getDataLength() {
		return DataLength;
	}




	/**
	 * Stellt eine Anfrage nach allen Spalten einer Datenbank-Tabelle.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param tabname   der Name der Datenbank-Tabelle
	 *
	 * @return die Map mit den Spalten-DTOs, welche den Tabellen-Namen in Kleinschreibung (!) zugeordnet sind.
	 */
	public static Map<String, DTOInformationSchemaTableColumn> query(DBEntityManager conn, String tabname) {
		List<DTOInformationSchemaTableColumn> results = null;
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				results = conn.queryNamed("DTOInformationSchemaTableColumn.mysql", DTOInformationSchemaTableColumn.class)
					.setParameter(1, conn.getDBSchema())
					.setParameter(2, tabname)
					.getResultList();
				break;
			case MDB:
				results = conn.queryNamed("DTOInformationSchemaTableColumn.mdb", DTOInformationSchemaTableColumn.class)
					.setParameter(1, tabname.toUpperCase())
					.getResultList();
				break;
			case MSSQL:
				results = conn.queryNamed("DTOInformationSchemaTableColumn.mssql", DTOInformationSchemaTableColumn.class)
					.setParameter(1, conn.getDBSchema())
					.setParameter(2, tabname)
					.getResultList();
				break;
			case SQLITE:
				results = conn.queryNamed("DTOInformationSchemaTableColumn.sqlite", DTOInformationSchemaTableColumn.class)
					.setParameter(1, tabname)
					.setParameter(2, tabname)
					.getResultList();
				break;
		}
		if (results == null)
			return null;
		// TODO Vereinheitlichung der Darstellung aus den Informations-Schemata der unterschiedlichen DBMS 		
		return results.stream().collect(Collectors.toMap(e -> e.Name.toLowerCase(), e -> e, (first, duplicate) -> first));
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DataLength == null) ? 0 : DataLength.hashCode());
		result = prime * result + ((DataType == null) ? 0 : DataType.hashCode());
		result = prime * result + ((DefaultValue == null) ? 0 : DefaultValue.hashCode());
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + ((NameTabelle == null) ? 0 : NameTabelle.hashCode());
		result = prime * result + ((Nullable == null) ? 0 : Nullable.hashCode());
		result = prime * result + ((Reihenfolge == null) ? 0 : Reihenfolge.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInformationSchemaTableColumn other = (DTOInformationSchemaTableColumn) obj;
		if (DataLength == null) {
			if (other.DataLength != null)
				return false;
		} else if (!DataLength.equals(other.DataLength))
			return false;
		if (DataType == null) {
			if (other.DataType != null)
				return false;
		} else if (!DataType.equals(other.DataType))
			return false;
		if (DefaultValue == null) {
			if (other.DefaultValue != null)
				return false;
		} else if (!DefaultValue.equals(other.DefaultValue))
			return false;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (NameTabelle == null) {
			if (other.NameTabelle != null)
				return false;
		} else if (!NameTabelle.equals(other.NameTabelle))
			return false;
		if (Nullable == null) {
			if (other.Nullable != null)
				return false;
		} else if (!Nullable.equals(other.Nullable))
			return false;
		if (Reihenfolge == null) {
			if (other.Reihenfolge != null)
				return false;
		} else if (!Reihenfolge.equals(other.Reihenfolge))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "DTOInformationSchemaTableColumn [NameTabelle=" + NameTabelle + ", Name=" + Name + ", Reihenfolge="
				+ Reihenfolge + ", DefaultValue=" + DefaultValue + ", Nullable=" + Nullable + ", DataType=" + DataType
				+ ", DataLength=" + DataLength + "]";
	}


}
