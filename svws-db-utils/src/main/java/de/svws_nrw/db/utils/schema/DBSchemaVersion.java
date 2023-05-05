package de.svws_nrw.db.utils.schema;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;

/**
 * Diese Klasse repräsentiert eine Schema-Version der SVWS-Datenbank
 */
public final class DBSchemaVersion implements Comparable<Long> {

	/** die Schema-Revision */
	private final Long _revision;

	/** Gibt an, ob das Schema durch eine Entwicklerversion "verdorben" wurde und nicht mehr für den Produktivbetrieb genutzt werden sollte. */
	private final boolean _tainted;


	/**
	 * Erzeugt eine neues Objekt für eien Schema-Version
	 *
	 * @param revision    die zu setzen Revisionsnummer
	 * @param isTainted   gibt an, ob es sich um eine "verdorbene" Datenbank-Revision
	 *                    handelt, die nicht mehr für den Produktivbetrieb genutzt
	 *                    werden sollte (z.B. aufgrund von Entwicklerversionen des SVWS-Servers)
	 */
	public DBSchemaVersion(final Long revision, final boolean isTainted) {
		this._revision = (revision == null) || (revision < 0) ? null : revision;
		this._tainted = isTainted;
	}


	/**
	 * Gibt zurück, ob diese Datenbank-Version gültig ist
	 *
	 * @return true, falls sie gültig ist und ansonsten false
	 */
	public boolean isValid() {
		return _revision != null;
	}


	/**
	 * Gibt die Revision des Schemas zurück.
	 *
	 * @return die Revision des Schemas
	 *
	 * @throws DeveloperNotificationException   tritt auf, wenn das Schema keine gültige Revision hat
	 */
	public long getRevision() throws DeveloperNotificationException {
	  if (_revision == null)
		  throw new DeveloperNotificationException("Das Schema besitzt keine gültige Revision");
	  return _revision;
	}


	/**
	 * Gibt zurück, ob es sich um eine "verdorbene" Datenbank-Revision handelt, die
	 * nicht mehr für den Produktivbetrieb genutzt werden sollte (z.B. aufgrund von
	 * Entwicklerversionen des SVWS-Servers)
	 *
	 * @return true, wenn es sich um eine "verdorbene" Datenbank-Revision und ansonsten false
	 */
	public boolean isTainted() {
		return _tainted;
	}


	/**
	 * Gibt die Revision des Schemas zurück. Wenn keine gültige Revision definiert
	 * wurde, so wird der übergeben Default-Wert zurückgegeben.
	 *
	 * @param default_revision   der Default-Wert für die Revision
	 *
	 * @return die Revision des Schemas oder der Default-Wert
	 */
	public long getRevisionOrDefault(final long default_revision) {
		  if (_revision == null)
			  return default_revision;
		  return _revision;
	}


	@Override
	public int compareTo(final Long otherRevision) {
		if ((this._revision == null) || this._tainted)
			return (otherRevision == null) ? 0 : -1;
		if (otherRevision == null)
			return 1;
		return Long.compare(_revision, otherRevision);
	}


	@Override
	public int hashCode() {
		if (_revision == null)
			return Integer.MIN_VALUE;
		return (int) (_tainted ? -_revision : _revision);
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DBSchemaVersion other = (DBSchemaVersion) obj;
		if ((_revision == null) || (other._revision == null))
			return false;
		if (_tainted || other._tainted)
			return false;
		return _revision.equals(other._revision);
	}


	/**
	 * Prüft, ob die Schema-Version mit der angegeben Revision übereinstimmt.
	 *
	 * @param revision   die zu vergleichende Revision
	 *
	 * @return true, falls sie übereinstimmen und ansonsten false
	 */
	public boolean equals(final int revision) {
		return (this._revision != null) && (this._revision == revision);
	}


	@Override
	public String toString() {
		return (this._revision == null) ? "unknown" : "" + _revision;
	}


}


