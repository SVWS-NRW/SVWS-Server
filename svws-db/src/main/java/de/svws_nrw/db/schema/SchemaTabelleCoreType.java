package de.svws_nrw.db.schema;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enthält die Definition einer Zuordnung eines Core-Types
 * zu einer Tabelle des Datenbank-Schemas
 */
public class SchemaTabelleCoreType {

	/** Die Tabelle, welcher der Core-Type zugeordnet ist */
	final SchemaTabelle _tabelle;

	/** Die Klasse des Core-Types */
	final Class<?> _ctClass;

	/** Die Version, in welcher der Core-Type vorliegt */
	final long _ctVersion;

	/** Ein Supplier für die Bestimmmung der Daten des Core-Types und der Erzeugung der VALUE-Einträge im SQL-Insert-Befehl. */
	final Function<Long, List<String>> valueSupplier;


	/**
	 * Erstellt eine neue Zuordnung von einem Core-Type zu der
	 * angegebenen Tabelle
	 *
	 * @param tabelle         die Tabelle
	 * @param ctClass         die Klasse des Core-Types
	 * @param ctVersion       die Version des Core-Types
	 * @param valueSupplier   der Supplier für den SQL-Insert-Befehl
	 */
	public SchemaTabelleCoreType(final SchemaTabelle tabelle, final Class<?> ctClass, final long ctVersion, final Function<Long, List<String>> valueSupplier) {
		this._tabelle = tabelle;
		this._ctClass = ctClass;
		this._ctVersion = ctVersion;
		this.valueSupplier = valueSupplier;
	}

	/**
	 * Gibt den Name der Core-Type-Klasse zurück.
	 *
	 * @return der Name der Core-Type-Klasse.
	 */
	public String getCoreTypeName() {
		return _ctClass.getCanonicalName();
	}

	/**
	 * Gibt die Version des Core-Types zurück
	 *
	 * @return die Version des Core-Types
	 */
	public long getCoreTypeVersion() {
		return _ctVersion;
	}

	/**
	 * Gibt den SQL-Code zu Einfügen der Daten
	 * des Core-Types zurück.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 * @param isBackup   gibt an, ob ein SQL-Befehl für ein SQLite-Backup erzeugt wird und eine Duplikatprüfung entfallen kann/muss
	 *
	 * @return der SQL-Code zum Einfügen oder null, falls die Tabelle in der Revision nicht definiert ist.
	 */
	public String getSQLInsert(final long revision, final boolean isBackup) {
		if (!_tabelle.isDefined(revision))
			return "";
		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(_tabelle.name());
		sql.append(_tabelle.getSpalten().stream().map(sp -> sp.name()).collect(Collectors.joining(", ", "(", ")")));
		sql.append(valueSupplier.apply(revision).stream().collect(Collectors.joining("), (", " VALUES (", ")")));
		if (!isBackup) {
			sql.append(" ON DUPLICATE KEY UPDATE ");
			sql.append(_tabelle.getSpalten().stream().map(sp -> sp.name() + "=VALUE(" + sp.name() + ")").collect(Collectors.joining(", ")));
		}
		return sql.toString();
	}

}
