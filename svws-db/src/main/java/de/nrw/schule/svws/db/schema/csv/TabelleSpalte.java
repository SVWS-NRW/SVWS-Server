package de.nrw.schule.svws.db.schema.csv;

import java.util.HashMap;

import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import de.nrw.schule.svws.db.schema.DBSchemaDefinition;



/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchemaTabelleSpalte.
 */
public class TabelleSpalte implements Comparable<TabelleSpalte> {
	
	/** Der Name der Tabelle zu der die Spalte gehört */
    @JsonProperty public String NameTabelle;

	/** Der eindeutige Spaltenname in der Tabelle */
    @JsonProperty public String NameSpalte;
	
	/** Die Revision, in welcher die Spalte eingeführt wurde - in der CSV-Datei NULL, wenn die Revision mit der Revision der Tabelle übereinstimmt */
    @JsonProperty public Integer Revision;
	
	/** Die Revision, in welcher die Spalte als veraltet definiert wurde - in der CSV-Datei NULL, wenn diese mit der Tabelle übereinstimmt */
    @JsonProperty public Integer Veraltet;
	  
	/** Gibt an, in welcher Reihenfolge die Spalten bei der Code-Erzeugung für SQL bzw. Java aufgelistet werden */
    @JsonProperty public Integer Sortierung;
	  
	/** Der Datentyp der Tabellenspalte */
    @JsonProperty public String Datentyp;
	  
	/** Die Länge des Datentyps (z.B. bei String-Datentypen) */
    @JsonProperty public Integer Datenlaenge;
	  
	/** Ein Standard-Wert für die Tabellenspalte */
    @JsonProperty public String DefaultWert;

	/** Gibt an, ob der Tabellenspalte eine NOT-NULL-Beschränkung auferlegt wird oder nicht */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean IsNotNull;

	/** Gibt den Namen an, welchen das zugehörige Java-Attribut erhält, sofern dieser sich vom Spaltennamen unterscheiden soll */
    @JsonProperty private String JavaAttributName;
	  
	/** Gibt an, ob welche Java-Converter-Klasse genutzt werden soll, um dass Attribut in einen zuhörigen Java-Datentyp umzuwandeln */  
    @JsonProperty private String JavaAttributConverter;
    
    /** Gibt an, ab welcher Revision der Attribut-Converter eingesetzt werden soll.*/
    @JsonProperty private Integer JavaAttributConverterRevision;
    
    /** Gibt an, ab welcher Revision der Attribut-Converter als veraltet markiert ist und bis zu welcher er nur eingesetzt werden soll. */
    @JsonProperty private Integer JavaAttributConverterVeraltet;
    
	  
	/** Gibt den Javadoc-Kommentar für das Attribut innerhalt der Java-DTO-Klasse an */
    @JsonProperty public String JavaKommentar;
	
	

    /** Die Revision, bei der die Tabellenspalte erstellt wird */
    @JsonIgnore public Versionen dbRevision;

    /** Die Revision, ab der die Tabellenspalte veraltet ist, oder null */
    @JsonIgnore public Versionen dbRevisionVeraltet;
    
    /** Das zugeordnete Tabellen-Objekt */
    @JsonIgnore public Tabelle tabelle;
    
    /** Gibt an, ob diese Spalte zu dem Primärschlüssel gehört */
    @JsonIgnore public boolean istPrimaerschlusselAttribut = false;
    
    /** Eine Map mit den Indizes, die auf dieser Spalte definiert sind. */ 
    @JsonIgnore public final HashMap<String, TabelleIndex> indizes = new HashMap<>();
    
    /** Eine Map mit den Unique-Constraints, die auf dieser Spalte definiert sind. */ 
    @JsonIgnore public final HashMap<String, TabelleUnique> unique = new HashMap<>();
    
    
	/**
	 * Liefert den Java-Attributnamen, sofern einer gesetzt wurde. Wurde keiner gesetzt, 
	 * so wird der Name der Spalte als Java-Attributname zurückgegeben.
	 * 
	 * @return der Java-Attributname
	 */
	@JsonIgnore
	public String getJavaAttributName() {
		if ((JavaAttributName == null) || (JavaAttributName.isEmpty()))
			return NameSpalte;
		return JavaAttributName;
	}
	
	
	/**
	 * Liefert den Attribute Converter, sofern einer gesetzt wurde und in der angegebenen
	 * Revision gültig ist.
	 *  
	 * @param rev   die Revision, für welche der Attribute-Converter bestimmt werden soll.
	 * 
	 * @return der Name des Attribut-Converters oder null
	 */
	@JsonIgnore
	public String getJavaAttributConverter(final int rev) {
		final int revision = (rev < 0) ? DBSchemaDefinition.getInstance().maxRevision : rev;
		if ((JavaAttributConverter == null) || ("".equals(JavaAttributConverter)))
			return null;
		if ((JavaAttributConverterRevision != null) && (revision < JavaAttributConverterRevision))
			return null;
		if ((JavaAttributConverterVeraltet != null) && (JavaAttributConverterVeraltet >= 0) && (revision >= JavaAttributConverterVeraltet))
			return null;
		if (rev < 0)
			return JavaAttributConverter;
		if (rev == 0)
			return "Migration" + JavaAttributConverter;
		return "Rev" + rev + JavaAttributConverter;
	}

	
	
	/**
	 * Liefert ein Object mit dem zugeordneten Default-Wert zurück. Der
	 * tatsächliche Typ hängt von dem Datentyp der Spalte ab.
	 *  
	 * @return der Default-Wert der Spalte
	 */
	@JsonIgnore
	public Object getDefaultWertConverted() {
    	if (DefaultWert == null)
    		return null;
		Object result = null;
		switch (Datentyp) {
		    case "bigint":
		    	if ("".equals(DefaultWert.trim()))
		    		return null;
		    	result = NumberUtils.toLong(DefaultWert);
		    	break;
		    case "float":
		    	if ("".equals(DefaultWert.trim()))
		    		return null;
		    	result = NumberUtils.toDouble(DefaultWert);
		    	break;
		    case "longblob":
		    	return null;
		    case "smallint":
		    case "int":
		    	if ("".equals(DefaultWert.trim()))
		    		return null;
		    	result = NumberUtils.toInt(DefaultWert);
		    	break;
		    case "boolean":
		    	if ("".equals(DefaultWert.trim()))
		    		return null;
			    result = DefaultWert.toLowerCase().equals("true");
		    	break;
		    case "char":
		    case "date":
		    case "datetime":
		    case "text":
		    case "varchar":
			    result = DefaultWert;
			    break;
			default: // unbekannter Typ
				return null;
		}
		if (JavaAttributConverter == null)
			return result;
		return DBAttributeConverter.getByClassName(JavaAttributConverter).convertToEntityAttributeFromObject(result);
	}
	
	
	/**
	 * Liefert den SQL-Code für diese Spalte und das angegeben DBMS
	 * 
	 * @param schema   eine Referenz auf die Schema-Definition
	 * @param dbms     das DBMS
	 * 
	 * @return der SQL-Code zur Verwendung beim Erzeugen der Tabellenspalte 
	 */
	@JsonIgnore
	public String getSQL(DBSchemaDefinition schema, DBDriver dbms) {
		Datentypen type = schema.datentypen.get(this.Datentyp);
		return this.NameSpalte + " " + type.getDBType(dbms)
	         + (((this.Datenlaenge == null) || (this.Datenlaenge <= 0)) ? "" : "(" + this.Datenlaenge + ")")
	         + this.getSQLCollation(schema, dbms)
	         + this.getSQLAutoinkrement(dbms)
	         + this.getSQLDefault(dbms, type)
	         + (this.IsNotNull ? " NOT NULL" : "");
	}
	
	
	/**
	 * Liefert den SQL-Code für das nachträgliche Hinzufügen dieser Spalte für das angegebene DBMS
	 * 
	 * @param schema   eine Referenz auf die Schema-Definition
	 * @param dbms     das DBMS
	 * 
	 * @return der SQL-Code zur nachträglichen Erzeugung der Tabellenspalte 
	 */
	@JsonIgnore
	public String getSQLCreate(DBSchemaDefinition schema, DBDriver dbms) {
		return "ALTER TABLE " + this.tabelle.Name + " ADD " + this.getSQL(schema, dbms);
	}
	
	
	/**
	 * Erzeugt den SQL-Drop-Befehl für diese Tabellenspalte für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Drop-Befehl
	 */
	@JsonIgnore
	public String getSQLDrop(DBDriver dbms) {
		switch (dbms) {
			case SQLITE:
				// TODO SQLite - Currently not supported
				return null;
			case MARIA_DB:
			case MYSQL:
			case MDB:
			case MSSQL:
			default:
				return "ALTER TABLE "  + this.NameTabelle + " DROP COLUMN " + this.NameSpalte + ";";
		}
	}
	

	
	/**
	 * Liefert den SQL-String für die Collation, die einer Spalte explizit zugeordnet werden kann.
	 * 
	 * @param schema   eine Referenz auf die Schema-Definition
	 * @param dbms     das DBMS
	 * 
	 * @return der SQL-String für die Zuordnung einer Collation
	 */
	@JsonIgnore
	private String getSQLCollation(DBSchemaDefinition schema, DBDriver dbms) {
		Datentypen type = schema.datentypen.get(this.Datentyp);
		Datenbanksysteme datenbanksystem = schema.dbms.get(dbms.toString());
		if ((!type.isCharString) || (datenbanksystem == null) || (datenbanksystem.CollationTable == null))
			return "";
		switch (dbms) {
			case MARIA_DB:
			case MYSQL:
			case MSSQL:
				return " COLLATE " + datenbanksystem.CollationTable;
			case MDB:
			case SQLITE:
			default:
				return "";
		}
	}
	
	
	/**
	 * Liefert in Abhängigkeit des angegebenen DBMS den SQL-Code,
	 * um bei dieser Spalte ein SQL-Autoinkrement zu ergänzen.
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Code für ein Autoinkrement bei dieser Spalte
	 */
	@JsonIgnore
	private String getSQLAutoinkrement(DBDriver dbms) {
		if ((!this.istPrimaerschlusselAttribut)
			|| (!this.tabelle.primaerschluessel.hatAutoIncrement) 
			|| ((!"int".equals(this.Datentyp)) &&
				(!"bigint".equals(this.Datentyp)) &&
				(!"smallint".equals(this.Datentyp))))
			return "";
		switch (dbms) {
			case MARIA_DB:
			case MSSQL:
			case MYSQL:
			case SQLITE:
				return " DEFAULT -1";
			case MDB:
				return " AUTOINCREMENT";
			default:
				return "";
		}
	}
	
	
	/**
	 * Gibt den SQL-Code für eine Default-Wert bei dieser Spalte zurück.
	 * Dabei wird das angegebene DBMS und der Datentyp berücksichtigt.
	 * 
	 * @param dbms   das DBMS
	 * @param type   der Datentyp
	 * 
	 * @return der SQL-Code für eine Default-Wert bei dieser Spalte
	 */
	@JsonIgnore
	private String getSQLDefault(DBDriver dbms, Datentypen type) {
		if (this.DefaultWert != null)
			return " DEFAULT " + (type.isQuoted ? "'" : "") + this.DefaultWert + (type.isQuoted ? "'" : "");
		if (!this.IsNotNull)
			return "";
		if ("date".equals(this.Datentyp)) {
			switch (dbms) {
				case MYSQL:
					return " DEFAULT (CURRENT_DATE)";
				case MARIA_DB:
					return " DEFAULT now()";
				case MSSQL:
					return " DEFAULT CURRENT_TIMESTAMP";
				case SQLITE:
					return " DEFAULT CURRENT_DATE";
				case MDB:
					return " DEFAULT Date()";
				default:
					return "";
			}
		} else if ("datetime".equals(this.Datentyp)) {
			switch (dbms) {
				case MYSQL:
					return " DEFAULT (CURRENT_TIME())";
				case MARIA_DB:
					return " DEFAULT (CURRENT_TIME())";
				case MSSQL:
					return " DEFAULT CURRENT_TIMESTAMP";
				case SQLITE:
					return " DEFAULT CURRENT_TIME";
				case MDB:
					return " DEFAULT Time()";
				default:
					return "";
			}
		} else if ("time".equals(this.Datentyp)) {
			switch (dbms) {
				case MYSQL:
					return " DEFAULT (CURRENT_TIME)";
				case MARIA_DB:
					return " DEFAULT Now()";
				case MSSQL:
					return " DEFAULT CURRENT_TIMESTAMP";
				case SQLITE:
					return " DEFAULT CURRENT_TIMESTAMP";
				case MDB:
					return " DEFAULT Now()";
				default:
					return "";
			}
		}
		return "";
	}
	

	@JsonIgnore
	@Override
	public int compareTo(TabelleSpalte other) {
		int result = this.NameTabelle.compareTo(other.NameTabelle); 
		if (result != 0)
			return result;
		return this.Sortierung < other.Sortierung ? -1 : 1;
	}

}
