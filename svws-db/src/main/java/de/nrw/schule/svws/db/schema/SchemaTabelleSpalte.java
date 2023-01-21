package de.nrw.schule.svws.db.schema;

import org.apache.commons.lang3.math.NumberUtils;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;

/**
 * Diese Klasse dient der Definition des Schemas von SVWS-Datenbank-Tabellenspalten.  
 */
public class SchemaTabelleSpalte implements Comparable<SchemaTabelleSpalte> {

	/** Die Tabelle, der diese Spalte zugeordnet ist */
	private final SchemaTabelle _tabelle;
	
	/** Die Position der Spalte bei der Definition der Tabelle */
	private final int _sortierung;
	
	/** Der Name der Spalte */
	private final String _name;
	
	/** Der Datentyp der Spalte */
	private final SchemaDatentypen _datentyp;
	
	/** Die Länge des Datentyps (z.B. bei String-Datentypen) */
	private Integer _datenlaenge = null;

	/** Der Default-Wert für diese Tabellenspalte */
	private String _default;
	
	/** Gibt an, ob die Tabellenspalte eine NOT-NULL-Beschränkung hat oder nicht */
	private boolean _notNull = false;

	/** Die Revision, ab wann die Spalte gültig ist. */
	private SchemaRevisionen _revision;
	
	/** Die Revision, ab wann die Spalte veraltet ist, sofern sie veraltet ist, ansonsten {@link SchemaRevisionen#UNDEFINED} */
	private SchemaRevisionen _veraltet;

	/** Gibt den Namen an, welchen das zugehörige Java-Attribut erhält, sofern dieser sich vom Spaltennamen unterscheiden sollte */
    private String _javaAttributName;
	  
	/** Gibt an, ob (null) und welcher Java-Converter-Klasse genutzt werden soll, um dass Attribut in einen zuhörigen Java-Datentyp umzuwandeln */  
    private Class<? extends DBAttributeConverter<?, ?>> _javaConverter;
    
    /** Gibt an, ab welcher Revision der Attribut-Converter eingesetzt werden soll.*/
    private SchemaRevisionen _javaConverterRevision;
    
    /** Gibt an, ab welcher Revision der Attribut-Converter als veraltet markiert ist und bis zu welcher er nur eingesetzt werden soll. */
    private SchemaRevisionen _javaConverterVeraltet;

	/** Gibt den Javadoc-Kommentar für das Attribut innerhalt der Java-DTO-Klasse an */
    private String _javaComment;
	
	/**
	 * Erstellt eine neue Spalte mit dem übergebenen Namen und dem übergebenen 
	 * Datentyp. 
	 * 
	 * @param tab          die Tabelle, der diese Spalte zugeordnet ist.
	 * @param sortierung   die Position der Spalte bei der Tabelle
	 * @param name         der Name der Spalte
	 * @param typ          der Datentyp der Spalte
	 */
	SchemaTabelleSpalte(SchemaTabelle tab, int sortierung, String name, SchemaDatentypen typ) {
		this._tabelle = tab;
		this._sortierung = sortierung;
		this._name = name;
		this._datentyp = typ;
		this._revision = tab.revision();
		this._veraltet = tab.veraltet();
	}


	/**
	 * Setzt die Länge der Daten (z.B. bei String-Datentypen).
	 * 
	 * @param laenge   die Länge der Daten
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setDatenlaenge(int laenge) {
		this._datenlaenge = laenge;
		return this;
	}

	/**
	 * Setzt den Default-Wert für diese Spalte.
	 * 
	 * @param def   der default-Wert
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setDefault(String def) {
		this._default = def;
		return this;
	}

	/**
	 * Setzt eine NOT-NULL-Beschränkung auf diese Spalte.
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setNotNull() {
		this._notNull = true;
		return this;
	}

	/**
	 * Setzt die Revision, ab wann die Spalte gültig ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben. 
	 * 
	 * @param revision   die Revision
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setRevision(SchemaRevisionen revision) {
		if (revision == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision einer Spalte kann nicht auf undefiniert gesetzt werden.");
		if (revision.revision <= this._revision.revision)
			throw new RuntimeException("Die Revision einer Spalte kann nur Überschrieben werden, wenn die Revision größer ist als die, welche bei der Tabelle gesetzt ist.");
		this._revision = revision;
		return this;
	}

	/**
	 * Setzt die Revision, ab wann die Spalte veraltet ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 * 
	 * @param veraltet   die Revision
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setVeraltet(SchemaRevisionen veraltet) {
		if (veraltet == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision, wann eine Spalte veraltet, kann nicht auf undefiniert gesetzt werden, da in diesem Fall das Erben des Veraltet-Attributes der Tabelle vorrangig ist.");
		if ((this._veraltet != SchemaRevisionen.UNDEFINED) && (veraltet.revision >= this._veraltet.revision))
			throw new RuntimeException("Die Revision, wann eine Spalte veraltet, kann nicht auf eine Revision größer oder gleich der Revision gesetzt werden, wo die zugehörige Tabelle veraltet.");
		this._veraltet = veraltet;
		return this;
	}

	/**
	 * Setzt den Namen des Java-Attributes, sofern dieser vom Spaltennamen abweicht.
	 * 
	 * @param name   der Name des Java-Attributes
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setJavaName(String name) {
		this._javaAttributName = name;
		return this;
	}

	/**
	 * Setzt den zu verwendenden Java-Attribut-Converter
	 * 
	 * @param converter   der Konverter
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setConverter(Class<? extends DBAttributeConverter<?, ?>> converter) {
		this._javaConverter = converter;
		return this;
	}

	/**
	 * Setzt die Revision, ab der der Konverter eingesetzt werden soll.
	 * 
	 * @param revision   die Revision
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setConverterRevision(SchemaRevisionen revision) {
		this._javaConverterRevision = revision;
		return this;
	}

	/**
	 * Setzt die Revision, ab der der Konverter nicht mehr eingesetzt werden soll.
	 * 
	 * @param veraltet   die Revision
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setConverterVeraltet(SchemaRevisionen veraltet) {
		this._javaConverterVeraltet = veraltet;
		return this;
	}

	/**
	 * Setzt den Kommentar, der für das Java-Doc der Spalte verwendet werden soll.
	 * 
	 * @param comment   der Kommentar
	 * 
	 * @return dieses Objekt
	 */
	public SchemaTabelleSpalte setJavaComment(String comment) {
		this._javaComment = comment;
		return this;
	}

	/**
	 * Gibt die Tabelle zurück, der diese Spalte zugeordnet ist.
	 * 
	 * @return die Tabelle
	 */
	public SchemaTabelle tabelle() {
		return _tabelle;
	}

	/**
	 * Gibt die Position der Spalte bei der Tabelle zurück.
	 * 
	 * @return die Position der Spalte für eine Sortierung
	 */
	public int sortierung() {
		return _sortierung;
	}

	/**
	 * Gibt den Namen der Spalte zurück.
	 * 
	 * @return der Name der Spalte
	 */
	public String name() {
		return _name;
	}
	
	/**
	 * Gibt den Datentyp der Spalte zurück.
	 * 
	 * @return der Datentyp der Spalte
	 */
	public SchemaDatentypen datentyp() {
		return _datentyp;
	}

	/**
	 * Gibt die Länge des Datentyps (z.B. bei String-Datentypen) der Spalte zurück.
	 * 
	 * @return der Länge des Datentyps der Spalte
	 */
	public Integer datenlaenge() {
		return _datenlaenge;
	}

	/**
	 * Gibt den Default-Wert der Tabellenspalte zurück.
	 * 
	 * @return der Default-Wert
	 */
	public String defaultWert() {
		return _default;
	}

	/**
	 * Gibt zurück, ob die Tabellenspalte eine NOT-NULL-Beschränkung hat oder nicht.
	 * 
	 * @return true, falls eine NOT-NULL-Beschränkung existiert und ansonsten false
	 */
	public boolean notNull() {
		return _notNull;
	}

	/**
	 * Gibt die Revision zurück, ab wann die Spalte gültig ist.
	 * 
	 * @return die Revision 
	 */
	public SchemaRevisionen revision() {
		return _revision;
	}

	/**
	 * Gibt die Revision zurück, ab wann die Spalte veraltet ist.
	 * Ist sie nicht veraltet, so wird {@link SchemaRevisionen#UNDEFINED}
	 * zurückgegeben.
	 * 
	 * @return die Revision, ab wann die Spalte veraltet ist, oder {@link SchemaRevisionen#UNDEFINED}
	 */
	public SchemaRevisionen veraltet() {
		return _veraltet;
	}

	/**
	 * Gibt den Namen des zugehörigen Java-Attributs zurück.
	 * 
	 * @return der Name des Java-Attributs
	 */
	public String javaAttributName() {
    	if (this._javaAttributName == null)
    		return this._name;
    	return _javaAttributName;
    }

	/**
	 * Gibt die Java-Attribut-Converter-Klasse zurück, welche genutzt wird um dass Attribut 
	 * in den zuhörigen Java-Datentyp umzuwandeln.
	 * Wird kein Converter genutzt, so wird null zurückgegeben.
	 * 
	 * @param rev   die Revision, für welche der Attribute-Converter bestimmt werden soll.
	 * 
	 * @return der Converter oder null
	 */
	public DBAttributeConverter<?, ?> javaConverter(final long rev) {
		final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
		if (_javaConverter == null)
			return null;
		if ((_javaConverterRevision != null) && (revision < _javaConverterRevision.revision))
			return null;
		if ((_javaConverterVeraltet != null) && (_javaConverterVeraltet.revision >= 0) && (revision >= _javaConverterVeraltet.revision))
			return null;
		if (rev == 0)
			return DBAttributeConverter.getByClassName("Migration" + _javaConverter.getSimpleName());
		if (rev <= SchemaRevisionen.maxRevision.revision)
			return DBAttributeConverter.getByClass(_javaConverter);
		return DBAttributeConverter.getByClassName("Dev" + _javaConverter.getSimpleName());
    }

    /** 
     * Gibt die Revision zurück, ab welcher der Attribut-Converter eingesetzt werden soll.
     * 
     * @return die Revision
     */
	public SchemaRevisionen javaConverterRevision() {
    	if (this._javaConverterRevision == null)
    		return this._revision;
    	return this._javaConverterRevision;
    }

    /** 
     * Gibt an, ab welcher Revision der Attribut-Converter als veraltet markiert ist und 
     * bis zu welcher er nur eingesetzt werden soll.
     * 
     * @return die Revision
     */
	public SchemaRevisionen javaConverterVeraltet() {
    	if (this._javaConverterVeraltet == null)
    		return this._veraltet;
    	return this._javaConverterVeraltet;
	}

	/** 
	 * Gibt den Javadoc-Kommentar für das Attribut innerhalt der Java-DTO-Klasse an
	 * 
	 * @return der Kommentar
	 */
	public String javaComment() {
		return this._javaComment;
	}

	
	/**
	 * Liefert den Attribute Converter, sofern einer gesetzt wurde und in der angegebenen
	 * Revision gültig ist.
	 *  
	 * @param rev   die Revision, für welche der Attribute-Converter bestimmt werden soll.
	 * 
	 * @return der Name des Attribut-Converters oder null
	 */
	public String getJavaAttributConverter(final long rev) {
		final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
		if (_javaConverter == null)
			return null;
		if ((_javaConverterRevision != null) && (revision < _javaConverterRevision.revision))
			return null;
		if ((_javaConverterVeraltet != null) && (_javaConverterVeraltet.revision >= 0) && (revision >= _javaConverterVeraltet.revision))
			return null;
		if (rev < 0)
			return _javaConverter.getSimpleName();
		if (rev == 0)
			return "Migration" + _javaConverter.getSimpleName();
		return "Dev" + _javaConverter.getSimpleName();
	}
	
	
	/**
	 * Liefert ein Object mit dem zugeordneten Default-Wert zurück. Der
	 * tatsächliche Typ hängt von dem Datentyp der Spalte ab.
	 *  
	 * @return der Default-Wert der Spalte
	 */
	public Object getDefaultWertConverted() {
    	if (_default == null)
    		return null;
		Object result = null;
		switch (_datentyp) {
		    case BIGINT:
		    	if ("".equals(_default.trim()))
		    		return null;
		    	result = NumberUtils.toLong(_default);
		    	break;
		    case FLOAT:
		    	if ("".equals(_default.trim()))
		    		return null;
		    	result = NumberUtils.toDouble(_default);
		    	break;
		    case LONGBLOB:
		    	return null;
		    case SMALLINT:
		    case INT:
		    	if ("".equals(_default.trim()))
		    		return null;
		    	result = NumberUtils.toInt(_default);
		    	break;
		    case BOOLEAN:
		    	if ("".equals(_default.trim()))
		    		return null;
			    result = _default.toLowerCase().equals("true");
		    	break;
		    case CHAR:
		    case DATE:
		    case DATETIME:
		    case TEXT:
		    case VARCHAR:
			    result = _default;
			    break;
			default: // unbekannter Typ
				return null;
		}
		if (_javaConverter == null)
			return result;
		return DBAttributeConverter.getByClass(_javaConverter).convertToEntityAttributeFromObject(result);
	}
	
	
	/**
	 * Liefert den SQL-Code für diese Spalte und das angegeben DBMS
	 * 
	 * @param dbms     das DBMS
	 * 
	 * @return der SQL-Code zur Verwendung beim Erzeugen der Tabellenspalte 
	 */
	public String getSQL(DBDriver dbms) {
	    boolean skipDatenlaenge = ((_datentyp == SchemaDatentypen.DATETIME) && ((dbms != DBDriver.MARIA_DB) && (dbms != DBDriver.MYSQL)));
		return this._name + " " + _datentyp.getDBType(dbms)
	         + (((this._datenlaenge == null) || (this._datenlaenge <= 0) || skipDatenlaenge) ? "" : "(" + this._datenlaenge + ")")
	         + this.getSQLAutoinkrement(dbms)
	         + this.getSQLDefault(dbms, _datentyp)
	         + (this._notNull ? " NOT NULL" : "")
	         + ((dbms == DBDriver.MARIA_DB || dbms == DBDriver.MYSQL) ? " COMMENT '" + javaComment().replace("'", "''") + "'" : "");
	}
	
	
	/**
	 * Liefert den SQL-Code für das nachträgliche Hinzufügen dieser Spalte für das angegebene DBMS
	 * 
	 * @param dbms     das DBMS
	 * 
	 * @return der SQL-Code zur nachträglichen Erzeugung der Tabellenspalte 
	 */
	public String getSQLCreate(DBDriver dbms) {
		return "ALTER TABLE " + this._tabelle.name() + " ADD " + this.getSQL(dbms);
	}
	
	
	/**
	 * Erzeugt den SQL-Drop-Befehl für diese Tabellenspalte für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Drop-Befehl
	 */
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
				return "ALTER TABLE "  + this._tabelle.name() + " DROP COLUMN " + this.name() + ";";
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
	private String getSQLAutoinkrement(DBDriver dbms) {
		if ((!this._tabelle.istPrimaerschlusselAttribut(this))
			|| (!this._tabelle.pkAutoIncrement()) 
			|| (!this._datentyp.isIntType()))
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
	private String getSQLDefault(DBDriver dbms, SchemaDatentypen type) {
		if (this._default != null)
			return " DEFAULT " + (type.isQuoted() ? "'" : "") + this._default + (type.isQuoted() ? "'" : "");
		if (!this._notNull)
			return "";
		return switch(this._datentyp) {
			case DATE -> switch (dbms) {
				case MYSQL -> " DEFAULT (CURRENT_DATE)";
				case MARIA_DB -> " DEFAULT now()";
				case MSSQL -> " DEFAULT CURRENT_TIMESTAMP";
				case SQLITE -> " DEFAULT CURRENT_DATE";
				case MDB -> " DEFAULT Date()";
				default -> "";
			};
			case DATETIME -> switch (dbms) {
				case MYSQL -> " DEFAULT (CURRENT_TIME())";
				case MARIA_DB -> " DEFAULT (CURRENT_TIME())";
				case MSSQL -> " DEFAULT CURRENT_TIMESTAMP";
				case SQLITE -> " DEFAULT CURRENT_TIME";
				case MDB -> " DEFAULT Time()";
				default -> "";
			};
			case TIME -> switch (dbms) {
				case MYSQL -> " DEFAULT (CURRENT_TIME)";
				case MARIA_DB -> " DEFAULT Now()";
				case MSSQL -> " DEFAULT CURRENT_TIMESTAMP";
				case SQLITE -> " DEFAULT CURRENT_TIMESTAMP";
				case MDB -> " DEFAULT Now()";
				default -> "";
			};
			default -> "";
		};
	}


	@Override
	public int compareTo(SchemaTabelleSpalte other) {
		int result = this._tabelle.name().compareTo(other._tabelle.name()); 
		if (result != 0)
			return result;
		return this.sortierung() < other.sortierung() ? -1 : 1;
	}

}
