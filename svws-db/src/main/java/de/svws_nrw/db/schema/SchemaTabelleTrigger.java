package de.svws_nrw.db.schema;

import java.util.Arrays;
import java.util.List;

import de.svws_nrw.db.DBDriver;

/**
 * Diese Klasse dient der Definition eines Triggers bei einer SVWS-Datenbank-Tabelle.
 */
public class SchemaTabelleTrigger {

	/** Die Tabelle, der dieser Trigger zugeordnet ist */
	private final SchemaTabelle _tabelle;

    /** Das DBMS für welches der Trigger definiert wurde. */
    private final DBDriver _dbms;

	/** Der Name des Triggers */
	private final String _name;

	/** Die Tabellen, die von diesem Trigger genutzt werden */
	private final List<SchemaTabelle> _tabellenGenutzt;

	/** Die Revision, ab wann der Trigger gültig ist. */
	private SchemaRevisionen _revision;

	/** Die Revision, ab wann der Trigger veraltet ist, sofern sie veraltet ist, ansonsten {@link SchemaRevisionen#UNDEFINED} */
	private SchemaRevisionen _veraltet;

	/** Der SQL-Befehl zum Erstellen des Triggers in dem entsprechenden SQL-Dialekt des DBMS */
    private final String _sql_create;

	/** Der SQL-Befehl zum Löschen des Triggers in dem entsprechenden SQL-Dialekt des DBMS */
    private final String _sql_drop;


	/**
	 * Erstellt einen neuen Trigger mit dem übergebenen Namen und dem übergebenen
	 * Skript zum Erstellen des Triggers.
	 *
	 * @param tab       die Tabelle, der diese Index zugeordnet ist
	 * @param name      der Name des Index
	 * @param dbms      das DBMS für welches der Trigger eingerichtet wird
	 * @param sql       der Teil des SQL-Befehls für das Erstellen des Triggers hinter "CREATE TRIGGER name "
	 * @param genutzt   die Spalten des Index
	 */
	SchemaTabelleTrigger(final SchemaTabelle tab, final String name, final DBDriver dbms, final String sql, final SchemaTabelle... genutzt) {
		this._tabelle = tab;
		this._name = name;
		this._dbms = dbms;
		this._tabellenGenutzt = Arrays.stream(genutzt).toList();
		this._revision = tab.revision();
		this._veraltet = tab.veraltet();
		this._sql_create = "CREATE TRIGGER " + name + " " + sql;
		this._sql_drop = switch (dbms) {
			case MARIA_DB, MYSQL, SQLITE -> "DROP TRIGGER IF EXISTS " + name + ";";
			case MDB -> "";   // bei MDB werden keine Trigger unterstützt
			case MSSQL -> "DROP TRIGGER " + name + ";";
			default -> throw new IllegalArgumentException("Kein gültiges DBMS für den Trigger angegeben.");
		};
	}


	/**
	 * Setzt die Revision, ab wann der Trigger gültig ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 *
	 * @param revision   die Revision
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleTrigger setRevision(final SchemaRevisionen revision) {
		if (revision == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision eines Triggers kann nicht auf undefiniert gesetzt werden.");
		if (revision.revision <= this._revision.revision)
			throw new RuntimeException("Die Revision eines Triggers kann nur Überschrieben werden, wenn die Revision größer ist als die, welche bei der Tabelle gesetzt ist.");
		this._revision = revision;
		return this;
	}

	/**
	 * Setzt die Revision, ab wann der Trigger veraltet ist. Dabei wird
	 * der übernommene Wert von der Tabelle überschrieben.
	 *
	 * @param veraltet   die Revision
	 *
	 * @return dieses Objekt
	 */
	public SchemaTabelleTrigger setVeraltet(final SchemaRevisionen veraltet) {
		if (veraltet == SchemaRevisionen.UNDEFINED)
			throw new RuntimeException("Die Revision, wann ein Trigger veraltet, kann nicht auf undefiniert gesetzt werden, da in diesem Fall das Erben des Veraltet-Attributes der Tabelle vorrangig ist.");
		if ((this._veraltet != SchemaRevisionen.UNDEFINED) && (veraltet.revision >= this._veraltet.revision))
			throw new RuntimeException("Die Revision, wann ein Trigger veraltet, kann nicht auf eine Revision größer oder gleich der Revision gesetzt werden, wo die zugehörige Tabelle veraltet.");
		this._veraltet = veraltet;
		return this;
	}

	/**
	 * Gibt die Tabelle zurück, der dieser Trigger zugeordnet ist.
	 *
	 * @return die Tabelle
	 */
	public SchemaTabelle tabelle() {
		return _tabelle;
	}

	/**
	 * Gibt den Namen des Triggers zurück.
	 *
	 * @return der Name des Triggers
	 */
	public String name() {
		return _name;
	}

	/**
	 * Gibt das DBMS zurück, in dessen SQL-Dialekt der Trigger
	 * formuliert wurde.
	 *
	 * @return das DBMS
	 */
	public DBDriver dbms() {
		return _dbms;
	}

	/**
	 * Gibt die Spalten des Triggers zurück.
	 *
	 * @return die Spalten des Triggers
	 */
	public List<SchemaTabelle> tabellenGenutzt() {
		return _tabellenGenutzt;
	}

	/**
	 * Gibt die Revision zurück, ab wann der Trigger gültig ist.
	 *
	 * @return die Revision
	 */
	public SchemaRevisionen revision() {
		return _revision;
	}

	/**
	 * Gibt die Revision zurück, ab wann der Trigger veraltet ist.
	 * Ist er nicht veraltet, so wird {@link SchemaRevisionen#UNDEFINED}
	 * zurückgegeben.
	 *
	 * @return die Revision, ab wann der Trigger veraltet ist, oder {@link SchemaRevisionen#UNDEFINED}
	 */
	public SchemaRevisionen veraltet() {
		return _veraltet;
	}


	/**
	 * Liefert die SQL-Skripte zum Erstellen oder Entfernen von Triggern
	 *
	 * @param dbms     das DBMS für welches das Skript angefragt wird
	 * @param create   gibt an, ob das Create-Skript oder das Drop-Skript angefragt wird.
	 *
	 * @return das SQL-Skript zum Erstellen oder Entfernen von Triggern
	 */
	public String getSQL(final DBDriver dbms, final boolean create) {
		if (!_dbms.equals(dbms))
			return null;
		return create ? this._sql_create : this._sql_drop;
	}

}
