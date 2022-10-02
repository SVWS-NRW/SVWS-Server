package de.nrw.schule.svws.db.schema;

/**
 * Diese Klasse dient der Definition des Schemas von SVWS-Datenbank-Tabellenspalten.  
 */
public class SchemaTabelleSpalte {

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
    private String _javaConverter;
    
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
	public SchemaTabelleSpalte setConverter(String converter) {
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
	 * @return der Converter oder null
	 */
	public String javaConverter() {
    	return this._javaConverter;
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

}
