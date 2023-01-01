package de.nrw.schule.svws.db.dto.rev9.svws.dav;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle DavRessourceCollectionsACL.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "DavRessourceCollectionsACL")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.all", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.id", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.id.multiple", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.benutzer_id", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.Benutzer_ID = :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.benutzer_id.multiple", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.ressourcecollection_id", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.RessourceCollection_ID = :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.ressourcecollection_id.multiple", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.RessourceCollection_ID IN :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.berechtigungen", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.berechtigungen = :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.berechtigungen.multiple", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.berechtigungen IN :value")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.primaryKeyQuery", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTODavRessourceCollectionsACL.all.migration", query="SELECT e FROM Rev9DTODavRessourceCollectionsACL e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Benutzer_ID","RessourceCollection_ID","berechtigungen"})
public class Rev9DTODavRessourceCollectionsACL {

	/** ID des ACL Eintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des Benutzers dieses ACL-Eintrags */
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/** Die ID der RessourceCollection dieses ACL-Eintrags */
	@Column(name = "RessourceCollection_ID")
	@JsonProperty
	public Long RessourceCollection_ID;

	/** Gibt die Berechtigungen dieses ACL-Eintrags wieder, ähnlich einer unix-file permission - bspw. 'r-' für nur Leserecht oder 'rw' für Lese- und Schreibrecht. */
	@Column(name = "berechtigungen")
	@JsonProperty
	public String berechtigungen;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTODavRessourceCollectionsACL ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTODavRessourceCollectionsACL() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTODavRessourceCollectionsACL ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param RessourceCollection_ID   der Wert für das Attribut RessourceCollection_ID
	 * @param berechtigungen   der Wert für das Attribut berechtigungen
	 */
	public Rev9DTODavRessourceCollectionsACL(final Long ID, final Long Benutzer_ID, final Long RessourceCollection_ID, final String berechtigungen) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Benutzer_ID == null) { 
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
		if (RessourceCollection_ID == null) { 
			throw new NullPointerException("RessourceCollection_ID must not be null");
		}
		this.RessourceCollection_ID = RessourceCollection_ID;
		if (berechtigungen == null) { 
			throw new NullPointerException("berechtigungen must not be null");
		}
		this.berechtigungen = berechtigungen;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTODavRessourceCollectionsACL other = (Rev9DTODavRessourceCollectionsACL) obj;
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
		return "Rev9DTODavRessourceCollectionsACL(ID=" + this.ID + ", Benutzer_ID=" + this.Benutzer_ID + ", RessourceCollection_ID=" + this.RessourceCollection_ID + ", berechtigungen=" + this.berechtigungen + ")";
	}

}