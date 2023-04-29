package de.svws_nrw.db.dto.current.svws.dav;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle DavRessourceCollectionsACL.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "DavRessourceCollectionsACL")
@NamedQuery(name = "DTODavRessourceCollectionsACL.all", query = "SELECT e FROM DTODavRessourceCollectionsACL e")
@NamedQuery(name = "DTODavRessourceCollectionsACL.id", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.ID = :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.id.multiple", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.ID IN :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.benutzer_id", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.Benutzer_ID = :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.benutzer_id.multiple", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.ressourcecollection_id", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.RessourceCollection_ID = :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.ressourcecollection_id.multiple", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.RessourceCollection_ID IN :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.berechtigungen", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.berechtigungen = :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.berechtigungen.multiple", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.berechtigungen IN :value")
@NamedQuery(name = "DTODavRessourceCollectionsACL.primaryKeyQuery", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.ID = ?1")
@NamedQuery(name = "DTODavRessourceCollectionsACL.all.migration", query = "SELECT e FROM DTODavRessourceCollectionsACL e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Benutzer_ID", "RessourceCollection_ID", "berechtigungen"})
public final class DTODavRessourceCollectionsACL {

	/** ID des ACL Eintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Benutzers dieses ACL-Eintrags */
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public long Benutzer_ID;

	/** Die ID der RessourceCollection dieses ACL-Eintrags */
	@Column(name = "RessourceCollection_ID")
	@JsonProperty
	public long RessourceCollection_ID;

	/** Gibt die Berechtigungen dieses ACL-Eintrags wieder, ähnlich einer unix-file permission - bspw. 'r-' für nur Leserecht oder 'rw' für Lese- und Schreibrecht. */
	@Column(name = "berechtigungen")
	@JsonProperty
	public String berechtigungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessourceCollectionsACL ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTODavRessourceCollectionsACL() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessourceCollectionsACL ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param RessourceCollection_ID   der Wert für das Attribut RessourceCollection_ID
	 * @param berechtigungen   der Wert für das Attribut berechtigungen
	 */
	public DTODavRessourceCollectionsACL(final long ID, final long Benutzer_ID, final long RessourceCollection_ID, final String berechtigungen) {
		this.ID = ID;
		this.Benutzer_ID = Benutzer_ID;
		this.RessourceCollection_ID = RessourceCollection_ID;
		if (berechtigungen == null) {
			throw new NullPointerException("berechtigungen must not be null");
		}
		this.berechtigungen = berechtigungen;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTODavRessourceCollectionsACL other = (DTODavRessourceCollectionsACL) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTODavRessourceCollectionsACL(ID=" + this.ID + ", Benutzer_ID=" + this.Benutzer_ID + ", RessourceCollection_ID=" + this.RessourceCollection_ID + ", berechtigungen=" + this.berechtigungen + ")";
	}

}
