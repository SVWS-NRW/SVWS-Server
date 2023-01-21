package de.nrw.schule.svws.db.dto.dev.svws.dav;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle DavSyncTokenSchueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "DavSyncTokenSchueler")
@NamedQuery(name="DevDTODavSyncTokenSchueler.all", query="SELECT e FROM DevDTODavSyncTokenSchueler e")
@NamedQuery(name="DevDTODavSyncTokenSchueler.id", query="SELECT e FROM DevDTODavSyncTokenSchueler e WHERE e.ID = :value")
@NamedQuery(name="DevDTODavSyncTokenSchueler.id.multiple", query="SELECT e FROM DevDTODavSyncTokenSchueler e WHERE e.ID IN :value")
@NamedQuery(name="DevDTODavSyncTokenSchueler.synctoken", query="SELECT e FROM DevDTODavSyncTokenSchueler e WHERE e.SyncToken = :value")
@NamedQuery(name="DevDTODavSyncTokenSchueler.synctoken.multiple", query="SELECT e FROM DevDTODavSyncTokenSchueler e WHERE e.SyncToken IN :value")
@NamedQuery(name="DevDTODavSyncTokenSchueler.primaryKeyQuery", query="SELECT e FROM DevDTODavSyncTokenSchueler e WHERE e.ID = ?1")
@NamedQuery(name="DevDTODavSyncTokenSchueler.all.migration", query="SELECT e FROM DevDTODavSyncTokenSchueler e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SyncToken"})
public class DevDTODavSyncTokenSchueler {

	/** ID des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Zeitstempel der letzten Änderung an für einen Card-DAV-Eintrag relevanten Schülerdaten. */
	@Column(name = "SyncToken")
	@JsonProperty
	public String SyncToken;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTODavSyncTokenSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTODavSyncTokenSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTODavSyncTokenSchueler ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SyncToken   der Wert für das Attribut SyncToken
	 */
	public DevDTODavSyncTokenSchueler(final Long ID, final String SyncToken) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SyncToken == null) { 
			throw new NullPointerException("SyncToken must not be null");
		}
		this.SyncToken = SyncToken;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTODavSyncTokenSchueler other = (DevDTODavSyncTokenSchueler) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTODavSyncTokenSchueler(ID=" + this.ID + ", SyncToken=" + this.SyncToken + ")";
	}

}