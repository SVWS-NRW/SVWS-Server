package de.nrw.schule.svws.db.utils.schema;


/**
 * Diese Klasse repräsentiert eine Schema-Version der SVWS-Datenbank
 */
public class DBSchemaVersion implements Comparable<Long> {
	
	/** die Schema-Revision */
	private final Long revision;
	
	/** Gibt an, ob das Schema durch eine Entwicklerversion "verdorben" wurde und nicht mehr für den Produktivbetrieb genutzt werden sollte. */
	private final boolean tainted;


	/**
	 * Erzeugt eine neues Objekt für eien Schema-Version
	 * 
	 * @param revision    die zu setzen Revisionsnummer
	 * @param isTainted   gibt an, ob es sich um eine "verdorbene" Datenbank-Revision 
	 *                    handelt, die nicht mehr für den Produktivbetrieb genutzt 
	 *                    werden sollte (z.B. aufgrund von Entwicklerversionen des SVWS-Servers)      
	 */
	public DBSchemaVersion(Long revision, boolean isTainted) {
		this.revision = (revision == null) ? null : ((revision >= 0) ? revision : null);
		this.tainted = isTainted;
	}


	/**
	 * Gibt zurück, ob diese Datenbank-Version gültig ist
	 *  
	 * @return true, falls sie gültig ist und ansonsten false
	 */
	public boolean isValid() {
		return revision != null;
	}


	/**
	 * Gibt die Revision des Schemas zurück.
	 *  
	 * @return die Revision des Schemas
	 * 
	 * @throws Exception   tritt aus, wenn das Schema keine gültige Revision hat
	 */
	public long getRevision() throws Exception {
	  if (revision == null)
		  throw new Exception("Das Schema besitzt keine gültige revision");
	  return revision;
	}
	
	
	/**
	 * Gibt zurück, ob es sich um eine "verdorbene" Datenbank-Revision handelt, die 
	 * nicht mehr für den Produktivbetrieb genutzt werden sollte (z.B. aufgrund von 
	 * Entwicklerversionen des SVWS-Servers)
	 * 
	 * @return true, wenn es sich um eine "verdorbene" Datenbank-Revision und ansonsten false
	 */
	public boolean isTainted() {
		return tainted;
	}
	
	
	/**
	 * Gibt die Revision des Schemas zurück. Wenn keine gültige Revision definiert
	 * wurde, so wird der übergeben Default-Wert zurückgegeben.
	 * 
	 * @param default_revision   der Default-Wert für die Revision
	 * 
	 * @return die Revision des Schemas oder der Default-Wert
	 */
	public long getRevisionOrDefault(long default_revision) {
		  if (revision == null)
			  return default_revision;
		  return revision;		
	}


	@Override
	public int compareTo(Long otherRevision) {
		if ((this.revision == null) || this.tainted)
			return (otherRevision == null) ? 0 : -1;
		if (otherRevision == null)
			return 1;
		int result = revision.compareTo(otherRevision);
		return result;
	}


	@Override
	public int hashCode() {
		if (revision == null)
			return Integer.MIN_VALUE;
		return (int) (tainted ? -revision : revision);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBSchemaVersion other = (DBSchemaVersion) obj;
		if ((revision == null) || (other.revision == null))
			return false;
		if (tainted || other.tainted)
			return false;
		if (!revision.equals(other.revision))
			return false;
		return true;
	}


	/**
	 * Prüft, ob die Schema-Version mit der angegeben Revision übereinstimmt.
	 *  
	 * @param revision   die zu vergleichende Revision
	 * 
	 * @return true, falls sie übereinstimmen und ansonsten false
	 */
	public boolean equals(int revision) {
		return (this.revision == null) ? false : (this.revision == revision);
	}


	@Override
	public String toString() {
		return (this.revision == null) ? "unknown" : "" + revision;
	}


}


