package de.svws_nrw.db.schema;

import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBDriver;

/**
 * Diese Klasse dient der Definition eines Fremdschlüssels bei SVWS-Datenbank-Tabellen.
 */
public class SchemaTabelleFremdschluessel {

	/** Der Name des Fremdschlüssels */
	private final String _name;

	/** Die Tabelle, der dieser Fremdschluessel zugeordnet ist */
	private final SchemaTabelle _tabelle;

	/** Die Tabelle, die dieser Fremdschluessel referenziert */
	private final SchemaTabelle _tabelleReferenziert;

	/** Die Spalten dieser Tabelle */
	private final List<SchemaTabelleSpalte> _spalten;

	/** Die zugehörigen Spalten der referenzierten Tabelle */
	private final List<SchemaTabelleSpalte> _spaltenReferenziert;

	/** Die Aktion bei einer Aktualisierung des referenzierten Schlüsselwertes */
	private final SchemaFremdschluesselAktionen _onUpdate;

	/** Die Aktion bei dem Entfernen des referenzierten Schlüsselwertes */
	private final SchemaFremdschluesselAktionen _onDelete;

	/** Die Revision, ab wann der Fremdschluessel gültig ist. */
	private SchemaRevisionen _revision;

	/** Die Revision, ab wann der Fremdschluessel veraltet ist, sofern er veraltet ist, ansonsten {@link SchemaRevisionen#UNDEFINED} */
	private SchemaRevisionen _veraltet;


	/**
	 * Erstellt einen neuen Fremdschluessel.
	 *
	 * @param name         der Name des Index
	 * @param tab          die Tabelle, der diese Index zugeordnet ist.
	 * @param tabRef       die referenzierte Tabelle
	 * @param onUpdate     die Aktion bei einer Aktualisierung des referenzierten Schlüsselwertes
	 * @param onDelete     die Aktion bei dem Entfernen des referenzierten Schlüsselwertes
	 * @param spalten      die Spalten des Fremdschlüssel dieser Tabelle
	 * @param spaltenRef   die referenzierten Spalten der referentierten Tabelle des Fremdschlüssels
	 */
	SchemaTabelleFremdschluessel(final String name, final SchemaTabelle tab, final SchemaTabelle tabRef, final SchemaFremdschluesselAktionen onUpdate,
			final SchemaFremdschluesselAktionen onDelete, final List<SchemaTabelleSpalte> spalten, final List<SchemaTabelleSpalte> spaltenRef) {
		this._name = name;
		this._tabelle = tab;
		this._tabelleReferenziert = tabRef;
		this._spalten = spalten;
		this._spaltenReferenziert = spaltenRef;
		this._onUpdate = onUpdate;
		this._onDelete = onDelete;
		this._revision = tab.revision();
		this._veraltet = tab.veraltet();
	}


	/**
	 * Setzt die Revision, ab wann der Fremdschlüssel gültig ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 *
	 * @param revision   die Revision
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleFremdschluessel setRevision(final SchemaRevisionen revision) {
		if (revision == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision eines Fremdschlüssels kann nicht auf undefiniert gesetzt werden.");
		if (revision.revision <= this._revision.revision)
			throw new RuntimeException("Die Revision eines Fremdschlüssels kann nur Überschrieben werden, wenn die Revision größer ist als die, welche bei der Tabelle gesetzt ist.");
		this._revision = revision;
		return this;
	}

	/**
	 * Setzt die Revision, ab wann der Fremdschlüssel veraltet ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 *
	 * @param veraltet   die Revision
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleFremdschluessel setVeraltet(final SchemaRevisionen veraltet) {
		if (veraltet == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision, wann ein Fremdschlüssel veraltet, kann nicht auf undefiniert gesetzt werden, da in diesem Fall das Erben des Veraltet-Attributes der Tabelle vorrangig ist.");
		if ((this._veraltet != SchemaRevisionen.UNDEFINED) && (veraltet.revision >= this._veraltet.revision))
			throw new RuntimeException("Die Revision, wann ein Fremdschlüssel veraltet, kann nicht auf eine Revision größer oder gleich der Revision gesetzt werden, wo die zugehörige Tabelle veraltet.");
		this._veraltet = veraltet;
		return this;
	}

	/**
	 * Gibt die Tabelle zurück, der dieser Fremdschlüssel zugeordnet ist.
	 *
	 * @return die Tabelle
	 */
	public SchemaTabelle tabelle() {
		return _tabelle;
	}

	/**
	 * Gibt die vom Fremdschlüssel referenzierte Tabelle zurück.
	 *
	 * @return die referenzierte Tabelle
	 */
	public SchemaTabelle tabelleReferenziert() {
		return _tabelleReferenziert;
	}

	/**
	 * Gibt den Namen des Fremdschlüssels zurück.
	 *
	 * @return der Name des Fremdschlüssels
	 */
	public String name() {
		return _name;
	}

	/**
	 * Gibt die Spalten der Tabelle des Fremdschlüssels zurück.
	 *
	 * @return die Spalten der Tabelle des Fremdschlüssels
	 */
	public List<SchemaTabelleSpalte> spalten() {
		return _spalten;
	}

	/**
	 * Gibt die referenzierten Spalten der referenzierten Tabelle des Fremdschlüssels zurück.
	 *
	 * @return die referenzierten Spalten der referenzierten Tabelle des Fremdschlüssels
	 */
	public List<SchemaTabelleSpalte> spaltenReferenziert() {
		return _spaltenReferenziert;
	}

	/**
	 * Gibt die Aktion bei einer Aktualisierung des referenzierten Schlüsselwertes.
	 *
	 * @return die Aktion bei einer Aktualisierung des referenzierten Schlüsselwertes
	 */
	public SchemaFremdschluesselAktionen onUpdate() {
		return _onUpdate;
	}

	/**
	 * Gibt die Aktion bei dem Entfernen des referenzierten Schlüsselwertes.
	 *
	 * @return die Aktion bei dem Entfernen des referenzierten Schlüsselwertes
	 */
	public SchemaFremdschluesselAktionen onDelete() {
		return _onDelete;
	}

	/**
	 * Gibt die Revision zurück, ab wann der Fremdschlüssel gültig ist.
	 *
	 * @return die Revision
	 */
	public SchemaRevisionen revision() {
		return _revision;
	}

	/**
	 * Gibt die Revision zurück, ab wann der Fremdschlüssel veraltet ist.
	 * Ist er nicht veraltet, so wird {@link SchemaRevisionen#UNDEFINED}
	 * zurückgegeben.
	 *
	 * @return die Revision, ab wann der Fremdschlüssel veraltet ist, oder {@link SchemaRevisionen#UNDEFINED}
	 */
	public SchemaRevisionen veraltet() {
		return _veraltet;
	}


    /**
     * Liefert die Tabellenspalten des Fremdschlüssels in der durch das Feld Sortierung definierten Reihenfolge
     *
     * @return die Tabellenspalten des Fremdschlüssels in der durch das Feld Sortierung definierten Reihenfolge
     */
    public List<SchemaTabelleSpalte> getSpalten() {
    	return _spalten.stream().sorted((a, b) -> Integer.compare(a.sortierung(), b.sortierung())).toList();
    }


	/**
	 * Erstellt einen SQL-String für das Erstellen einen Fremdschlüssels als SQL-CONSTRAINT
	 *
	 * @return der SQL-String für das Erstellen des Fremdschlüssels
	 */
	public String getSQL() {
		return "CONSTRAINT " + this.name() + " FOREIGN KEY ("
				+ getSpalten().stream().map(spalte -> spalte.name()).collect(Collectors.joining(", "))
				+ ") REFERENCES " + this._tabelleReferenziert.name() + '('
				+ _spaltenReferenziert.stream().map(spalte -> spalte.name()).collect(Collectors.joining(", "))
				+ ")"
				+ ((this._onUpdate == null) || this._onUpdate == SchemaFremdschluesselAktionen.NO_ACTION ? "" : " ON UPDATE " + this._onUpdate.sql())
				+ ((this._onDelete == null) || this._onDelete == SchemaFremdschluesselAktionen.NO_ACTION ? "" : " ON DELETE " + this._onDelete.sql());
	}


	/**
	 * Erstellt einen SQL-String für das nachträgliche Erstellen einen Fremdschlüssels
	 * für den SQL-Dialekt des angegebenen DBMS
	 *
	 * @param dbms   das DBMS
	 *
	 * @return der SQL-String für das nachträgliche Erstellen des Fremdschlüssels
	 */
	public String getSQLCreate(final DBDriver dbms) {
		switch (dbms) {
			case SQLITE:
				// TODO currently not supported
				return null;
			case MDB, MARIA_DB, MYSQL, MSSQL:
			default:
				return "ALTER TABLE " + this._tabelle.name() + " ADD " + this.getSQL();
		}
	}


	/**
	 * Erzeugt den SQL-Drop-Befehl für diesen Fremdschlüssel für den SQL-Dialekt des angegebenen DBMS
	 *
	 * @param dbms   das DBMS
	 *
	 * @return der SQL-Drop-Befehl
	 */
	public String getSQLDrop(final DBDriver dbms) {
		return switch (dbms) {
			// TODO SQLite - Currently not supported
			case SQLITE -> null;
			case MARIA_DB, MYSQL -> "ALTER TABLE " + this._tabelle.name() + " DROP FOREIGN KEY " + this._name + ";";
			case MDB, MSSQL -> "ALTER TABLE " + this._tabelle.name() + " DROP CONSTRAINT " + this._name + ";";
			default -> "ALTER TABLE " + this._tabelle.name() + " DROP FOREIGN KEY " + this._name + ";";
		};
	}

}
