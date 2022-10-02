package de.nrw.schule.svws.db.schema;

import java.util.Vector;

import de.nrw.schule.svws.core.adt.Pair;

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
	
	/** Gibt an, ob der Primärschlüssel dieser Tabelle aus einer numerischen Spalte besteht und ein Auto-Inkrement hat. */
	private boolean _pkAutoIncrement = false;
	
	/** Die Spalten des Primärschlüssels dieser Tabelle */
	private Vector<SchemaTabelleSpalte> _pkSpalten = new Vector<>();

	/** Die Fremdschlüssel dieser Tabelle */
	private Vector<SchemaTabelleFremdschluessel> _fremdschluessel = new Vector<>();

	/** Die Indizes dieser Tabelle */
	private Vector<SchemaTabelleIndex> _indizes = new Vector<>();

	/** Die Unqiue-Indizes dieser Tabelle */
	private Vector<SchemaTabelleUniqueIndex> _unique = new Vector<>();
	

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
		if ((_pkSpalten.size() > 1) || ((_pkSpalten.size() == 1) && (!_pkSpalten.get(0).datentyp().isIntType())))
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

}
