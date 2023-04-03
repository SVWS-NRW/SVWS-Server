package de.svws_nrw.db.schema;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBDriver;

/**
 * Diese Klasse dient der Definition eines Unique-Index bei SVWS-Datenbank-Tabellen.
 */
public class SchemaTabelleUniqueIndex {

	/** Die Tabelle, der dieser Index zugeordnet ist */
	private final SchemaTabelle _tabelle;

	/** Der Name der Spalte */
	private final String _name;

	/** Die Spalten, die zu diesem Index gehören */
	private final List<SchemaTabelleSpalte> _spalten;

	/** Die Revision, ab wann die Spalte gültig ist. */
	private SchemaRevisionen _revision;

	/** Die Revision, ab wann die Spalte veraltet ist, sofern sie veraltet ist, ansonsten {@link SchemaRevisionen#UNDEFINED} */
	private SchemaRevisionen _veraltet;


	/**
	 * Erstellt einen neuen Unique-Index mit dem übergebenen Namen und den übergebenen
	 * Spalten.
	 *
	 * @param tab       die Tabelle, der diese Index zugeordnet ist.
	 * @param name      der Name des Index
	 * @param spalten   die Spalten des Index
	 */
	SchemaTabelleUniqueIndex(final SchemaTabelle tab, final String name, final SchemaTabelleSpalte... spalten) {
		this._tabelle = tab;
		this._name = name;
		this._spalten = Arrays.stream(spalten).toList();
		this._revision = tab.revision();
		this._veraltet = tab.veraltet();
	}


	/**
	 * Setzt die Revision, ab wann der Index gültig ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 *
	 * @param revision   die Revision
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleUniqueIndex setRevision(final SchemaRevisionen revision) {
		if (revision == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision eines Index kann nicht auf undefiniert gesetzt werden.");
		if (revision.revision <= this._revision.revision)
			throw new RuntimeException("Die Revision eines Index kann nur Überschrieben werden, wenn die Revision größer ist als die, welche bei der Tabelle gesetzt ist.");
		this._revision = revision;
		return this;
	}

	/**
	 * Setzt die Revision, ab wann der Index veraltet ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 *
	 * @param veraltet   die Revision
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleUniqueIndex setVeraltet(final SchemaRevisionen veraltet) {
		if (veraltet == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision, wann ein Index veraltet, kann nicht auf undefiniert gesetzt werden, da in diesem Fall das Erben des Veraltet-Attributes der Tabelle vorrangig ist.");
		if ((this._veraltet != SchemaRevisionen.UNDEFINED) && (veraltet.revision >= this._veraltet.revision))
			throw new RuntimeException("Die Revision, wann ein Index veraltet, kann nicht auf eine Revision größer oder gleich der Revision gesetzt werden, wo die zugehörige Tabelle veraltet.");
		this._veraltet = veraltet;
		return this;
	}

	/**
	 * Gibt die Tabelle zurück, der dieser Unique-Index zugeordnet ist.
	 *
	 * @return die Tabelle
	 */
	public SchemaTabelle tabelle() {
		return _tabelle;
	}

	/**
	 * Gibt den Namen des Index zurück.
	 *
	 * @return der Name des Index
	 */
	public String name() {
		return _name;
	}

	/**
	 * Gibt die Spalten des Index zurück.
	 *
	 * @return die Spalten des Index
	 */
	public List<SchemaTabelleSpalte> spalten() {
		return _spalten;
	}

	/**
	 * Gibt die Revision zurück, ab wann der Index gültig ist.
	 *
	 * @return die Revision
	 */
	public SchemaRevisionen revision() {
		return _revision;
	}

	/**
	 * Gibt die Revision zurück, ab wann der Index veraltet ist.
	 * Ist er nicht veraltet, so wird {@link SchemaRevisionen#UNDEFINED}
	 * zurückgegeben.
	 *
	 * @return die Revision, ab wann der Index veraltet ist, oder {@link SchemaRevisionen#UNDEFINED}
	 */
	public SchemaRevisionen veraltet() {
		return _veraltet;
	}

    /**
     * Liefert die Tabellenspalten der Unique-Constraint in der durch das Feld Sortierung definierten Reihenfolge
     *
     * @return die Tabellenspalten der Unique-Constraint in der durch das Feld Sortierung definierten Reihenfolge
     */
    public List<SchemaTabelleSpalte> getSpalten() {
    	return _spalten.stream().sorted((a, b) -> { return Integer.compare(a.sortierung(), b.sortierung()); }).collect(Collectors.toList());
    }


	/**
	 * Erstellt einen SQL-String für das Erstellen einer Unique-Constraint
	 *
	 * @return der SQL-String für das Erstellen der Unique-Constraint
	 */
	public String getSQL() {
		return "CONSTRAINT " + this._name + " UNIQUE ("
				+ getSpalten().stream().map(spalte -> spalte.name()).collect(Collectors.joining(", "))
				+ ")";
	}


	/**
	 * Erstellt einen SQL-String für das nachträgliche Erstellen einer Unique-Constraint
	 * für den SQL-Dialekt des angegebenen DBMS
	 *
	 * @param dbms   das DBMS
	 *
	 * @return der SQL-String für das nachträgliche Erstellen der Unique-Constraint
	 */
	public String getSQLCreate(final DBDriver dbms) {
		return switch (dbms) {
			case SQLITE -> null; // TODO currently not supported
			case MDB, MARIA_DB, MYSQL, MSSQL -> "ALTER TABLE " + this._tabelle.name() + " ADD " + this.getSQL();
			default -> "ALTER TABLE " + this._tabelle.name() + " ADD " + this.getSQL();
		};
	}


	/**
	 * Erzeugt den SQL-Drop-Befehl für diese Unique-Constraint für den SQL-Dialekt des angegebenen DBMS
	 *
	 * @param dbms   das DBMS
	 *
	 * @return der SQL-Drop-Befehl
	 */
	public String getSQLDrop(final DBDriver dbms) {
		return switch (dbms) {
			case SQLITE -> null; // TODO SQLite - Currently not supported
			case MARIA_DB, MYSQL -> "ALTER TABLE " + this._tabelle.name() + " DROP INDEX " + this._name + ";";
			case MDB, MSSQL -> "ALTER TABLE " + this._tabelle.name() + " DROP CONSTRAINT " + this._name + ";";
			default -> "ALTER TABLE " + this._tabelle.name() + " DROP INDEX " + this._name + ";";
		};
	}

}
