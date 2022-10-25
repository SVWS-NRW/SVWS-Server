package de.nrw.schule.svws.db.schema;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.adt.Pair;
import de.nrw.schule.svws.db.DBDriver;

/**
 * Diese Klasse dient als Basisklasse für die Schema-Definitionen
 * von SVWS-Datenbank-Tabellen.  
 */
public class SchemaTabelle {
	
	/** Der Name der Tabelle */
	private final String _name;
	
	/** Die Revision, ab wann die Tabelle gültig ist. */
	private final SchemaRevisionen _revision;
	
	/** Die Revision, ab wann die Tabelle veraltet ist, sofern sie veraltet ist, ansonsten {@link SchemaRevisionen#UNDEFINED} */
	private SchemaRevisionen _veraltet = SchemaRevisionen.UNDEFINED;
	
	/** Gibt an, ob die Tabelle bei der Migration aus einer Schild 2 - Datenbank berücksichtigt werden soll */
	private boolean _migrate = false; 

	/** Gibt an, ob die Tabelle bei Import und Export-Operationen berücksichtigt werden soll. */
	private boolean _importExport = false;
	
	/** Das Package unterhalb des allgemeinen DTO-Packages für Datenbanktabellen an, wo das zugehörige DTO-Objekt erzeugt werden soll */
	private String _javaSubPackage;

	/** Der Java-DTO-Klassenname für die Tabelle */
	private String _javaClassName;

	/** Die Beschreibung für die Tabelle an, welche bei der DTO-Klassendefinition verwendet wird. */
	private String _javaComment;


	/** Die Spalten der Tabelle*/
	private Vector<SchemaTabelleSpalte> _spalten = new Vector<>();
	
	/** Eine Map für die Spalten der Tabelle */
	private LinkedHashMap<String, SchemaTabelleSpalte> _mapSpalten = new LinkedHashMap<>();
	
	/** Gibt an, ob der Primärschlüssel dieser Tabelle aus einer numerischen Spalte besteht und ein Auto-Inkrement hat. */
	private boolean _pkAutoIncrement = false;
	
	/** Die Spalten des Primärschlüssels dieser Tabelle */
	private LinkedHashSet<SchemaTabelleSpalte> _pkSpalten = new LinkedHashSet<>();

	/** Die Fremdschlüssel dieser Tabelle */
	private Vector<SchemaTabelleFremdschluessel> _fremdschluessel = new Vector<>();

	/** Die Indizes dieser Tabelle */
	private Vector<SchemaTabelleIndex> _indizes = new Vector<>();

	/** Die Unqiue-Indizes dieser Tabelle */
	private Vector<SchemaTabelleUniqueIndex> _unique = new Vector<>();

	/** Die Trigger, die dieser Tabelle zugeordnet sind */
	private Vector<SchemaTabelleTrigger> _trigger = new Vector<>();
	
	/** Der Core-Type, welcher dieser Tabelle für Update-Prozesse zugeordnet ist. */
	private SchemaTabelleCoreType _coreType = null;


	/**
	 * Erstellt eine neue Schema-Definition für eine 
	 * Tabelle der SVWS-Datenbank. 
	 * 
	 * @param name       der Tabellenname
	 * @param revision   die Revision, ab wann die Tabelle gültig ist.
	 */
	public SchemaTabelle(String name, SchemaRevisionen revision) {
		if (revision == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision, ab wann eine Tabelle gültig ist muss definiert sein.");
		this._name = name;
		this._revision = revision;
		this._javaSubPackage = "svws";
		this._javaClassName = "DTO" + name;
	}
	
	/**
	 * Setzt die Revision, ab wann diese Tabelle veraltet ist.
	 * 
	 * @param veraltet    die Revision, ab wann die Tabelle veraltet ist
	 */
	public void setVeraltet(SchemaRevisionen veraltet) {
		if (veraltet == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision, ab wann eine Tabelle veraltet ist kann nicht auf undefiniert gesetzt werden. Dies ist bereits der Default-Wert.");
		this._veraltet = veraltet;
	}

	/** 
	 * Setzt die Information, ob die Tabelle bei der Migration aus einer Schild 2 - Datenbank 
	 * berücksichtigt werden soll.
	 *  
	 * @param migrate   true, falls sie berücksichtigt werden soll und ansonsten false 
	 */
	public void setMigrate(boolean migrate) {
		this._migrate = migrate;
	}

	/** 
	 * Setzt die Information ob die Tabelle bei Import und Export-Operationen berücksichtigt werden soll.
	 * 
	 * @param importExport   true, falls sie berücksichtigt werden soll und ansonsten false
	 */
	public void setImportExport(boolean importExport) {
		this._importExport = importExport;
	}
	
	/** 
	 * Setzt das Package unterhalb des allgemeinen DTO-Packages für Datenbanktabellen, 
	 * wo das zugehörige DTO-Objekt erzeugt werden soll.
	 * 
	 * @param subPackage   das Sub-Package
	 */
	public void setJavaSubPackage(String subPackage) {
		this._javaSubPackage = subPackage;
	}

	/** 
	 * Setzt den Java-DTO-Klassennamen für die Tabelle.
	 * 
	 * @param name   der Java-DTO-Klassenname
	 */
	public void setJavaClassName(String name) {
		this._javaClassName = name;
	}

	/** 
	 * Setzt die Beschreibung für die Tabelle, welche bei der DTO-Klassendefinition 
	 * verwendet wird.
	 * 
	 * @param beschreibung   die Beschreibung
	 */
	public void setJavaComment(String beschreibung) {
		this._javaComment = beschreibung;
	}

	/** 
	 * Setzt, ob der Primärschlüssel dieser Tabellen ein Auto-Inkrement 
	 * unterstützt oder nicht.
	 */
	public void setPKAutoIncrement() {
		this._pkAutoIncrement = true;
		if ((_pkSpalten.size() > 1) || ((_pkSpalten.size() == 1) && (!_pkSpalten.iterator().next().datentyp().isIntType())))
			throw new RuntimeException("Ein Primärschlüssel mit Auto-Inkrement muss aus einer Spalte mit ganzzahligen Werten bestehen.");
	}

	/**
	 * Gibt den Namen der Tabelle zurück.
	 * 
	 * @return der Name der Tabelle
	 */
	public String name() {
		return _name;
	}
	
	/**
	 * Gibt die Revision zurück, ab wann die Tabelle gültig ist.
	 * 
	 * @return die Revision 
	 */
	public SchemaRevisionen revision() {
		return _revision;
	}

	/**
	 * Gibt die Revision zurück, ab wann die Tabelle veraltet ist.
	 * Ist sie nicht veraltet, so wird {@link SchemaRevisionen#UNDEFINED}
	 * zurückgegeben.
	 * 
	 * @return die Revision, ab wann die Tabelle veraltet ist, oder {@link SchemaRevisionen#UNDEFINED}
	 */
	public SchemaRevisionen veraltet() {
		return _veraltet;
	}
	
	/**
	 * Gibt an, ob die Tabelle bei der Migration aus einer Schild 2 - Datenbank berücksichtigt werden soll.
	 * 
	 * @return true, falls die Tabelle bei der Migration berücksichtigt werden soll.
	 */
	public boolean migrate() {
		return _migrate;
	}

	/**
	 * Gibt an, ob die Tabelle bei Import und Export-Operationen berücksichtigt werden soll.
	 * 
	 * @return true, falls die Tabelle bei Import-/Export-Operationen berücksichtigt werden soll.
	 */
	public boolean importExport() {
		return _importExport;
	}

	/**
	 * Gibt das Package unterhalb des allgemeinen DTO-Packages für Datenbanktabellen zurück, 
	 * wo das zugehörige DTO-Objekt erzeugt werden soll.
	 * 
	 * @return das Sub-Package
	 */
	public String javaSubPackage() {
		return _javaSubPackage;
	}

	/**
	 * Gibt den Java-DTO-Klassennamen für die Tabelle zurück.
	 * 
	 * @return der Klassenname
	 */
	public String javaClassName() {
		return _javaClassName;
	}

	/**
	 * Gibt die Beschreibung für die Tabelle zurück, welche bei der DTO-Klassendefinition verwendet wird.
	 * 
	 * @return die Beschreibung
	 */
	public String javaComment() {
		return _javaComment;
	}


	/**
	 * Gibt das Set der Spalten des Primärschlüssels zurück.
	 * 
	 * @return das Set der Primärschlüsselpalten
	 */
	public LinkedHashSet<SchemaTabelleSpalte> pkSpalten() {
		return _pkSpalten;
	}


	/**
	 * Gibt die Liste der Fremdschlüsel zurück.
	 * 
	 * @return die Liste der Fremdschlüsel
	 */
	public List<SchemaTabelleFremdschluessel> fremdschluessel() {
		return _fremdschluessel;
	}


	/**
	 * Gibt die Liste der Indizes zurück.
	 * 
	 * @return die Liste der Indizes
	 */
	public List<SchemaTabelleIndex> indizes() {
		return _indizes;
	}


	/**
	 * Gibt die Liste der Unique-Indizes zurück.
	 * 
	 * @return die Liste der Unique-Indizes
	 */
	public List<SchemaTabelleUniqueIndex> unique() {
		return _unique;
	}


	/**
	 * Gibt die Liste der Trigger zurück.
	 * 
	 * @return die Liste der Trigger
	 */
	public List<SchemaTabelleTrigger> trigger() {
		return _trigger;
	}


	/**
	 * Fügt eine neue Spalte zu dieser Tabelle hinzu
	 * 
	 * @param name   der Name der Spalte
	 * @param typ    der Typ der Spalte
	 * @param pk     gibt an, ob die Spalte zu dem Primärschlüssel gehört oder nicht.
	 * 
	 * @return die Tabellenspalte
	 */
	public SchemaTabelleSpalte add(String name, SchemaDatentypen typ, boolean pk) {
		SchemaTabelleSpalte col = new SchemaTabelleSpalte(this, _spalten.size() + 1, name, typ);
		_spalten.add(col);
		_mapSpalten.put(name, col);
		if (pk) {
			_pkSpalten.add(col);
			if ((this._pkAutoIncrement) && ((_pkSpalten.size() != 1) || (!typ.isIntType())))
				throw new RuntimeException("Ein Primärschlüssel mit Auto-Inkrement muss aus einer Spalte mit ganzzahligen Werten bestehen.");
		}
		return col;
	}

	/**
	 * Gibt zurück, ob der Primärschlüssel dieser Tabelle aus einer numerischen Spalte besteht 
	 * und ein Auto-Inkrement hat oder nicht.
	 *
	 * @return true, falls der Primärschlüssel ein Autoinkrement unterstützt oder nicht.
	 */
	public boolean pkAutoIncrement() {
		return _pkAutoIncrement;
	}

	/**
	 * Fügt einen neuen Fremdschlüssel zu der Tabelle hinzu
	 * 
	 * @param name           der Name des Fremdschlüssels
	 * @param onUpdate       die Aktion bei einer Aktualisierung des referenzierten Schlüssels
	 * @param onDelete       die Aktion bei einem Entfernen des referenzierten Schlüssels
	 * @param referenziert   die Paare von Spalte aus dieser Tabelle und der referenzierten Tabelle
	 * 
	 * @return der Fremdschlüssel
	 */
	@SafeVarargs 
	public final SchemaTabelleFremdschluessel addForeignKey(String name, SchemaFremdschluesselAktionen onUpdate, 
			SchemaFremdschluesselAktionen onDelete, Pair<SchemaTabelleSpalte, SchemaTabelleSpalte>... referenziert) {
		if (referenziert.length <= 0)
			throw new RuntimeException("Ein Fremdschlüssel muss mindestens eine fremde Spalte referenzieren.");
		SchemaTabelle tabReferenziert = null;
		Vector<SchemaTabelleSpalte> spalten = new Vector<>();
		Vector<SchemaTabelleSpalte> spaltenReferenziert = new Vector<>();
		for (Pair<SchemaTabelleSpalte, SchemaTabelleSpalte> ref : referenziert) {
			if (ref.a.tabelle() != this)
				throw new RuntimeException("Die Tabelle der ersten Spalte muss diese Tabelle referenzieren.");
			if (tabReferenziert == null)
				tabReferenziert = ref.b.tabelle();
			if (ref.b.tabelle() == this)
				throw new RuntimeException("Die Tabelle der zweiten Spalte darf nicht dieser Tabelle entsprechen. Die Tabelle kann nicht auf sich selbst verweisen");
			if (ref.b.tabelle() != tabReferenziert)
				throw new RuntimeException("Die zweiten Spalten müssen immer zu der gleichen Tabelle gehören.");
			spalten.add(ref.a);
			spaltenReferenziert.add(ref.b);
		}
		SchemaTabelleFremdschluessel fk = new SchemaTabelleFremdschluessel(name, this, tabReferenziert, onUpdate, onDelete, spalten, spaltenReferenziert);
		_fremdschluessel.add(fk);
		return fk;
	}

	/**
	 * Fügt einen neuen Non-Unqiue-Index zu dieser Tabelle hinzu
	 * 
	 * @param name      der Name des Index
	 * @param spalten   die Spalten des Index
	 * 
	 * @return der Index
	 */
	public SchemaTabelleIndex addIndex(String name, SchemaTabelleSpalte... spalten) {
		SchemaTabelleIndex idx = new SchemaTabelleIndex(this, name, spalten);
		_indizes.add(idx);
		return idx;
	}

	/**
	 * Fügt einen neuen Unqiue-Index zu dieser Tabelle hinzu
	 * 
	 * @param name      der Name des Index
	 * @param spalten   die Spalten des Index
	 * 
	 * @return der Index
	 */
	public SchemaTabelleUniqueIndex addUniqueIndex(String name, SchemaTabelleSpalte... spalten) {
		SchemaTabelleUniqueIndex idx = new SchemaTabelleUniqueIndex(this, name, spalten);
		_unique.add(idx);
		return idx;
	}


	/**
	 * Fügt einen neuen Trigger zu dieser Tabelle hinzu
	 * 
	 * @param name      der Name des Triggers
	 * @param dbDriver  das DBMS für welches der Trigger eingerichtet wird
	 * @param sql       der Teil des SQL-Befehls für das Erstellen des Triggers hinter "CREATE TRIGGER name "
	 * @param genutzt   die Spalten des Index
	 * 
	 * @return der Trigger
	 */
	public SchemaTabelleTrigger addTrigger(String name, DBDriver dbDriver, String sql, SchemaTabelle... genutzt) {
		SchemaTabelleTrigger trig = new SchemaTabelleTrigger(this, name, dbDriver, sql, genutzt);
		_trigger.add(trig);
		return trig;
	}
	

	/**
	 * Ordnet dieser Tabelle einen Core-Type zu.
	 * 
	 * @param coreType    die Klasse mit den Informationen zur des Core-Types
	 */
	public void setCoreType(SchemaTabelleCoreType coreType) {
	    if (_coreType != null)
	        throw new RuntimeException("Der Tabelle wurde bereits zuvor ein Core-Type zugeordnet. Zwei Zuordnungen sind nicht zulässig.");
	    _coreType = coreType;
	}
	
	
	/**
	 * Gibt zurück, ob diese Tabelle einen Core-Type zugeordnet hat oder nicht.
	 * 
	 * @return true, falls ein Core-Type zugeordet ist und ansonsten false
	 */
	public boolean hasCoreType() {
	    return (_coreType != null);
	}


    /**
     * Gibt die Core-Type-Zuordnung dieser Tabelle zurück, falls
     * einer zugeordnet ist. Ansonsten wird null zurückgegeben.
     * 
     * @return die Core-Type-Zuordnung oder null
     */
    public SchemaTabelleCoreType getCoreType() {
        return _coreType;
    }


    /**
     * Prüft, ob die Tabelle in der angegebenen Revision definiert ist oder nicht.
     * 
     * @param rev   die zu prüfende Revision
     * 
     * @return true, falls die Tabelle in der Revision definiert ist und ansonsten false
     */
    public boolean isDefined(final long rev) {
    	final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
    	return (revision >= _revision.revision) && ((_veraltet.revision < 0) || (revision < _veraltet.revision));
    }
    
    
    /**
     * Liefert die Tabellenspalten in der durch das Feld Sortierung definierten Reihenfolge
     * 
     * @return die Tabellenspalten in der durch das Feld Sortierung definierten Reihenfolge
     */
    public List<SchemaTabelleSpalte> getSpalten() {
    	return _spalten.stream().sorted((a,b) -> { return Integer.compare(a.sortierung(), b.sortierung()); }).collect(Collectors.toList());
    }
    
    
    /**
     * Liefert die Tabellenspalten, die in der angegeben Revision definiert sind in der durch das 
     * Feld Sortierung definierten Reihenfolge.
     * 
     * @param rev   die Revision, in der die Tabellenspalte definiert sein muss
     * 
     * @return die Tabellenspalten in der durch das Feld Sortierung definierten Reihenfolge
     */
    public List<SchemaTabelleSpalte> getSpalten(final long rev) {
    	final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
    	return _spalten.stream()
    			.filter(sp -> (revision >= sp.revision().revision) && ((sp.veraltet().revision < 0) || (revision < sp.veraltet().revision)))
    			.sorted((a,b) -> { return Integer.compare(a.sortierung(), b.sortierung()); }).collect(Collectors.toList());
    }


    /**
     * Liefert den Namen der Java-Klasse, wie er in der angegebenn Revision genutzt werden soll.
     * 
     * @param rev   die Revision
     * 
     * @return der Name der Java-Klasse
     */
    public String getJavaKlasse(final long rev) {
    	if (rev > 0)
    		return "Rev" + rev + _javaClassName;
    	if (rev == 0)
    		return "Migration" + _javaClassName;
		return _javaClassName;
	}
	
    
	/**
     * Liefert die Spalte mit dem angegebenen Spaltennamen
     * 
     * @param name   der Spaltenname
     * 
     * @return die Tabellenspalte
     */
    public SchemaTabelleSpalte getSpalte(String name) {
    	return _mapSpalten.get(name);
    }


    /**
     * Liefert den CREATE TABLE-Befehl in der entsprechenden Version zu dieser Tabelle. Dabei
     * wird der Dialekt des angegebenen DBMS genutzt.
     *  
     * @param dbms     das DBMS in dessen Dialekt der CREATE TABLE Befehl formuliert ist
     * @param rev      die Revision des Schemas, für welche der SQL-Befehl erzeugt wird 
     * 
     * @return der entsprechende SQL-Befehl
     */
    public String getSQL(DBDriver dbms, long rev) {
		String newline = System.lineSeparator();
		String pk = this.getPrimaerschluesselSQL();
		return "CREATE TABLE " + this._name + " (" + newline
				+ "  " + getSQLSpalten(dbms, rev)
				+ (((pk == null) || ("".equals(pk))) ? "" : "," + newline + "  " + pk)
				+ this.getSQLFremdschluessel(rev)
				+ this.getSQLUniqueContraints(rev)
				+ newline + ")" + ((dbms == DBDriver.MARIA_DB || dbms == DBDriver.MYSQL) ? " COMMENT '" + javaComment().replace("'", "''") + "'" : "") + ";";
    }


	/**
	 * Erzeugt den SQL-Drop-Befehl für diese Tabelle für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Drop-Befehl
	 */
	public String getSQLDrop(DBDriver dbms) {
		return "DROP TABLE " + (dbms.supportsIfExists() ? "IF EXISTS " : "") + this._name + ";";
	}


    /**
     * Liefert alle in der angegebenen Revision gültigen Fremdschlüssel
     * 
     * @param rev   die Revision
     * 
     * @return die in der angegebenen Revision gültigen Fremdschlüssel
     */
    public List<SchemaTabelleFremdschluessel> getFremdschluessel(long rev) {
    	return _fremdschluessel.stream()
			.filter(fk -> ((rev == -1) && (fk.veraltet().revision == -1)) 
				|| ((rev != -1) && (rev >= fk.revision().revision) && ((fk.veraltet().revision == -1) || (rev < fk.veraltet().revision))))
			.collect(Collectors.toList());
    }
    
    
    /**
     * Generiert den SQL-Code für die Fremdschlüssel-Constraints der Tabelle
     * 
     * @param rev   die Revision, für welche die Fremdschlüssel-Constraints der Tabelle erzeugt werden sollen
     * 
     * @return der SQL-Code für die Fremdschlüssel-Constraints der Tabelle
     */
    private String getSQLFremdschluessel(long rev) {
    	String result = _fremdschluessel.stream()
			.filter(fk -> ((rev == -1) && (fk.veraltet().revision == -1)) 
				|| ((rev != -1) && (rev >= fk.revision().revision) && ((fk.veraltet().revision == -1) || (rev < fk.veraltet().revision))))
			.map(fk -> fk.getSQL())
			.collect(Collectors.joining("," + System.lineSeparator() + "  "));
    	if ((result == null) || ("".equals(result)))
    		return "";
    	return "," + System.lineSeparator() + "  " + result;
    }

    
    /**
     * Erzeugt den SQL-Code für die Spalten der Tabelle.
     * 
     * @param dbms     das DBMS in dessen SQL-Dialekt formuliert wird
     * @param rev      die Revision des Schemas, für welche der Spalten-SQL-Code erzeugt wird
     *  
     * @return der SQL-Code
     */
    private String getSQLSpalten(DBDriver dbms, long rev) {
    	return getSpalten().stream()
			.filter(col -> ((rev == -1) && (col.veraltet().revision == -1)) 
					|| ((rev != -1) && (rev >= col.revision().revision) && ((col.veraltet().revision == -1) || (rev < col.veraltet().revision))))
			.map(col -> col.getSQL(dbms))
			.collect(Collectors.joining(", " + System.lineSeparator() + "  "));    	
    }


    /**
     * Generiert den SQL-Code für die Unique-Constraints der Tabelle
     * 
     * @param rev   die Revision, für welche die Unique-Constraints der Tabelle erzeugt werden sollen
     * 
     * @return der SQL-Code für die Unique-Constraints der Tabelle
     */
    private String getSQLUniqueContraints(long rev) {
    	String result = _unique.stream()
    		.filter(uc -> ((rev == -1) && (uc.veraltet().revision == -1)) 
    				|| ((rev != -1) && (rev >= uc.revision().revision) && ((uc.veraltet().revision == -1) || (rev < uc.veraltet().revision))))
    		.map(uc -> uc.getSQL())
    		.collect(Collectors.joining("," + System.lineSeparator() + "  "));
    	if ((result == null) || ("".equals(result)))
    		return "";
    	return "," + System.lineSeparator() + "  " + result;
    }


    /**
     * Generiert den SQL-Code für das Erstellen der Indizes der Tabelle
     * 
     * @param rev   die Revision, für welche die Indizes der Tabelle erzeugt werden sollen
     * 
     * @return der SQL-Code für die Indizes der Tabelle
     */
    public String getSQLIndizes(long rev) {
    	return _indizes.stream()
        		.filter(idx -> ((rev == -1) && (idx.veraltet().revision == -1)) 
        				|| ((rev != -1) && (rev >= idx.revision().revision) && ((idx.veraltet().revision == -1) || (rev < idx.veraltet().revision))))
        		.map(idx -> idx.getSQL())
        		.collect(Collectors.joining(System.lineSeparator()));
    }


    /**
     * Generiert den SQL-Code für das Erstellen oder Entfernen der Trigger der Tabelle
     * 
     * @param dbms     das DBMS, für welches der Trigger-SQL-Code generiert wird
     * @param rev      die Revision, für welche die Trigger der Tabelle erzeugt oder entfernt werden sollen
     * @param create   gibt an, ob der SQL-Code für das Erstellen oder das Entfernen von Triggern generiert wird. 
     * 
     * @return der SQL-Code für die Trigger der Tabelle
     */
    public String getSQLTrigger(DBDriver dbms, long rev, boolean create) {
    	// Lese die einzelnen Trigger aus
    	Vector<String> sqlTrigger = new Vector<>();
    	for (SchemaTabelleTrigger t : this._trigger) {
    		if (!dbms.equals(t.dbms()))
    			continue;
    		// Prüfe, ab wann die Trigger gültig bzw. ungültig sind
    		if (create) {
        		if (((rev == -1) && (t.veraltet().revision == -1))
    					|| ((rev != -1) && (rev >= t.revision().revision) && ((t.veraltet().revision == -1) || (rev < t.veraltet().revision))))
        			sqlTrigger.add(t.getSQL(dbms, true));
    		} else {
    			if ((t.veraltet().revision >= 0) && (rev >= t.veraltet().revision))
    				sqlTrigger.add(t.getSQL(dbms, false));
    		}
    	}
    	sqlTrigger.addAll(this.getPrimaerschluesselTriggerSQLList(dbms, rev, create));
    	// Füge die einzelnen SQL-Code-Abschnitte zu einem Skript zusammen
    	var newline = System.lineSeparator();
    	if (create) {
	    	if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
	    		return sqlTrigger.stream().map(sql -> "delimiter $" + newline + sql + newline + "$" + newline + "delimiter ;" + newline)
	    				.collect(Collectors.joining(newline + newline));
	    	} else if (DBDriver.MSSQL.equals(dbms)) {
	    		return sqlTrigger.stream().map(sql -> sql + newline + "GO" + newline)
	    				.collect(Collectors.joining(newline + newline));    		
	    	} 
	    	// DBDriver.SQLITE.equals(dbms))
			return sqlTrigger.stream().collect(Collectors.joining(newline + newline));
    	}
    	return sqlTrigger.stream().collect(Collectors.joining(newline));
    }


    /**
     * Prüft, ob die angegebene Spalte eine Spalte des Primärschlüssels ist. 
     * 
     * @param col   die zu prüfende Spalte
     * 
     * @return true, falls die Spalte Teil des Primärschlüssels ist.
     */
    public boolean istPrimaerschlusselAttribut(SchemaTabelleSpalte col) {
    	return this._pkSpalten.contains(col);
    }


	/**
	 * Erstellt einen SQL-String für das Erstellen einen Primärschlüssel als SQL-CONSTRAINT 
	 * 
	 * @return der SQL-String für das Erstellen des Primärschlüssels
	 */
	public String getPrimaerschluesselSQL() {
		if (_pkSpalten.size() <= 0)
			return "";
		return _pkSpalten.stream().map(spalte -> spalte.name()).collect(Collectors.joining(", ", "CONSTRAINT PK_" + this._name + " PRIMARY KEY (", ")"));
	}


	
	/**
	 * Erstellt die SQL-Skripte zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 * 
	 * @param dbms     das DBMS für welches das Skript angefragt wird
     * @param rev      die Revision, für welche die Trigger der Tabelle erzeugt oder entfernt werden sollen
	 * @param create   gibt an, ob das CREATE-Skript oder das Drop-Skript angefragt wird.
	 * 
	 * @return das SQL-Skript zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 */
	public String getPrimaerschluesselTriggerSQL(DBDriver dbms, int rev, boolean create) {
    	var triggerList = getPrimaerschluesselTriggerSQLList(dbms, rev, create);
    	if (triggerList.size() <= 0)
    		return "";
    	var newline = System.lineSeparator();
    	if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
    		return triggerList.stream().map(sql -> "delimiter $" + newline + sql + newline + "$" + newline + "delimiter ;" + newline)
    				.collect(Collectors.joining(newline + newline));
    	} else if (DBDriver.MSSQL.equals(dbms)) {
    		return triggerList.stream().map(sql -> sql + newline + "GO" + newline)
    				.collect(Collectors.joining(newline + newline));    		
    	} 
    	// DBDriver.SQLITE.equals(dbms))
		return triggerList.stream().collect(Collectors.joining(newline + newline));    		
	}
	
	
	
	/**
	 * Erstellt die SQL-Skripte zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 * 
	 * @param dbms     das DBMS für welches das Skript angefragt wird
     * @param rev      die Revision, für welche die Trigger der Tabelle erzeugt oder entfernt werden sollen
	 * @param create   gibt an, ob das CREATE-Skript oder das Drop-Skript angefragt wird.
	 * 
	 * @return das SQL-Skript zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 */
	public List<String> getPrimaerschluesselTriggerSQLList(DBDriver dbms, long rev, boolean create) {
		Vector<String> result = new Vector<>();
		if ((!this._pkAutoIncrement) || (this._pkSpalten.size() != 1))
			return result;
		// DBDriver.MDB wird hier nicht unterstützt !!!
		String tab = this._name;
		String spalte = this._pkSpalten.iterator().next().name();
		String newline = System.lineSeparator();
		if (create) {
			if (!(((rev == -1) && (this.veraltet().revision == -1))
					|| ((rev != -1) && (rev >= this.revision().revision) && ((this.veraltet().revision == -1) || (rev < this.veraltet().revision)))))
				return result;
			if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + newline +
						"BEFORE INSERT" + newline +
						"  ON " + tab + " FOR EACH ROW" + newline +
						"BEGIN" + newline +
						"  DECLARE tmpID bigint;" + newline +
						"  SELECT MaxID INTO tmpID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "';" + newline +
						"  IF tmpID IS NULL THEN" + newline +
						"    SELECT max(" + spalte + ") INTO tmpID FROM " + tab + ";" + newline +
						"    IF tmpID IS NULL THEN" + newline +
						"      SET tmpID = 0;" + newline +
						"    END IF;" + newline +
						"    INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', tmpID);" + newline +
						"  END IF;" + newline +
						"  IF NEW." + spalte + " < 0 THEN" + newline +
						"    SET NEW." + spalte + " = tmpID + 1;" + newline +
						"  END IF;" + newline +
						"  IF NEW." + spalte + " > tmpID THEN" + newline +
						"    UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle='" + tab + "';" + newline +
						"  END IF;" + newline +
						"END" + newline
				);
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + newline + 
						"BEFORE UPDATE" + newline + 
						"  ON " + tab + " FOR EACH ROW" + newline + 
						"BEGIN" + newline +
						"  DECLARE tmpID bigint;" + newline + 
						"  IF (OLD." + spalte + " <> NEW." + spalte + ") THEN" + newline + 
						"    SELECT MaxID INTO tmpID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "';" + newline +
						"    IF tmpID IS NULL THEN" + newline +
						"      SELECT max(" + spalte + ") INTO tmpID FROM " + tab + ";" + newline + 
						"      IF tmpID IS NULL THEN" + newline +
						"        SET tmpID = 0;" + newline +
						"      END IF;" + newline +
						"      INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', tmpID);" + newline + 
						"    END IF;" + newline +
						"    IF NEW." + spalte + " < 0 THEN" + newline + 
						"      SET NEW." + spalte + " = tmpID + 1;" + newline + 
						"    END IF;" + newline +
						"    IF NEW." + spalte + " > tmpID THEN" + newline + 
						"      UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle='" + tab + "';" + newline + 
						"    END IF;" + newline + 
						"  END IF;" + newline +
						"END" + newline
				);
			} else if (DBDriver.MSSQL.equals(dbms)) {
				result.add(
						"exec('" + newline + 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + " ON " + tab + " INSTEAD OF INSERT AS" + newline + 
						"BEGIN" + newline + 
						"  DECLARE @tmpID bigint" + newline + 
						"  DECLARE @maxInsertedID bigint" + newline + 
						newline + 
						"  SET @maxInsertedID = (SELECT max(" + spalte + ") FROM inserted WHERE " + spalte + " >= 0)" + newline + 
						"  INSERT INTO " + tab + newline + 
						"    SELECT * FROM inserted WHERE " + spalte + " >= 0" + newline + 
						"    " + newline + 
						"  SET @tmpID = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle = ''" + tab + "'')" + newline + 
						"  IF (@tmpID IS NULL)" + newline + 
						"    BEGIN" + newline + 
						"      SET @tmpID = (SELECT max(" + spalte + ") FROM " + tab + ")" + newline + 
						"      IF (@tmpID IS NULL)" + newline + 
						"        BEGIN" + newline + 
						"          SET @tmpID = 0" + newline + 
						"        END" + newline + 
						"      SET NOCOUNT ON" + newline + 
						"      INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES (''" + tab + "'', @tmpID)" + newline + 
						"      SET NOCOUNT OFF" + newline + 
						"    END" + newline + 
						"  " + newline + 
						"  IF ((SELECT count(*) FROM inserted WHERE " + spalte + " < 0) > 0)" + newline + 
						"    BEGIN  " + newline + 
						"      SELECT * INTO #tmp FROM inserted WHERE " + spalte + " < 0" + newline + 
						"      UPDATE #tmp SET " + spalte + " = @tmpID, @tmpID = @tmpID + 1" + newline + 
						"      INSERT INTO " + tab + newline + 
						"        SELECT * FROM #tmp" + newline + 
						"      DROP TABLE #tmp" + newline + 
						"    END" + newline + 
						"  " + newline + 
						"  SET NOCOUNT ON" + newline + 
						"  IF (@maxInsertedID > @tmpID)" + newline + 
						"    BEGIN" + newline + 
						"      SET @tmpID = @maxInsertedID" + newline + 
						"	 END" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = @tmpID WHERE NameTabelle = ''" + tab + "''" + newline + 
						"  SET NOCOUNT OFF" + newline + 
						"END;" + newline + 
						"')" + newline
				);
				result.add(
						"exec('" + newline +
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + " ON " + tab + " AFTER UPDATE AS" + newline + 
						"BEGIN" + newline + 
						"  if (UPDATE(" + spalte + "))" + newline + 
						"    BEGIN" + newline + 
						"      DECLARE @tmpID bigint" + newline + 
						"      DECLARE @maxInsertedID bigint" + newline + 
						newline + 
						"      SET @maxInsertedID = (SELECT max(" + spalte + ") FROM inserted WHERE " + spalte + " >= 0)" + newline + 
						"      SET @tmpID = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle = ''" + tab + "'')" + newline + 
						"      IF (@tmpID IS NULL)" + newline + 
						"        BEGIN" + newline + 
						"          SET @tmpID = (SELECT max(" + spalte + ") FROM " + tab + ")" + newline + 
						"          IF (@tmpID IS NULL)" + newline + 
						"            BEGIN" + newline + 
						"              SET @tmpID = 0" + newline + 
						"            END" + newline + 
						"          INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES (''" + tab + "'', @tmpID)" + newline + 
						"        END    " + newline + 
						"      IF (@maxInsertedID > @tmpID)" + newline + 
						"        BEGIN" + newline + 
						"          SET @tmpID = @maxInsertedID" + newline + 
						"	      END" + newline + 
						"      UPDATE SVWS_DB_AutoInkremente SET MaxID = @tmpID WHERE NameTabelle = ''" + tab + "''" + newline + 
						"    END" + newline + 
						"END;" + newline +
						"')" + newline
				);
			} else if (DBDriver.SQLITE.equals(dbms)) {
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_1 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL AND " + newline + 
						"	  NEW." + spalte + " > (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "')" + newline + 
						"BEGIN" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_2 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL" + newline + 
						"BEGIN" + newline + 
						"  UPDATE " + tab + " SET " + spalte + " = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') + 1 WHERE " + spalte + " = NEW." + spalte + ";" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = MaxID + 1 WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_3 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " < coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0));" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_4 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " >= coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  NEW." + spalte + ");" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_5 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL" + newline + 
						"BEGIN" + newline + 
						"  UPDATE " + tab + " SET " + spalte + " = coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0) + 1 WHERE " + spalte + " = NEW." + spalte + ";" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0) + 1);" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_1 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL AND " + newline + 
						"	  NEW." + spalte + " > (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "')" + newline + 
						"BEGIN" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_2 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL" + newline + 
						"BEGIN" + newline + 
						"  UPDATE " + tab + " SET " + spalte + " = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') + 1 WHERE " + spalte + " = NEW." + spalte + ";" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = MaxID + 1 WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_3 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " < coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0));" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_4 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " >= coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  NEW." + spalte + ");" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_5 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL" + newline + 
						"BEGIN" + newline + 
						"  -- Update der " + spalte + " in der Tabelle " + tab + " erfolgt durch den Autoinkrement-Trigger 2, daher hier auch kein +1, sondern nur den Max-Wert schreiben" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0));" + newline + 
						"END;\r\n"
				);
			}
		} else {
			if ((this.veraltet().revision < 0) || (rev < this.veraltet().revision))
				return result;
			if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + ";");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + ";");
			} else if (DBDriver.MSSQL.equals(dbms)) {
				result.add("DROP TRIGGER t_AutoIncrement_INSERT_" + tab + ";");
				result.add("DROP TRIGGER t_AutoIncrement_UPDATE_" + tab + ";"); 
			} else if (DBDriver.SQLITE.equals(dbms)) {
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_1;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_2;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_3;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_4;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_5;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_1;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_2;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_3;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_4;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_5;");
			}
		}
		return result;
	}

}
