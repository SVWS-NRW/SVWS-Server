package de.svws_nrw.db.dto.current.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerAllgemein.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerAllgemein")
@NamedQuery(name = "DTOBenutzerAllgemein.all", query = "SELECT e FROM DTOBenutzerAllgemein e")
@NamedQuery(name = "DTOBenutzerAllgemein.id", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.ID = :value")
@NamedQuery(name = "DTOBenutzerAllgemein.id.multiple", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.ID IN :value")
@NamedQuery(name = "DTOBenutzerAllgemein.anzeigename", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.AnzeigeName = :value")
@NamedQuery(name = "DTOBenutzerAllgemein.anzeigename.multiple", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.AnzeigeName IN :value")
@NamedQuery(name = "DTOBenutzerAllgemein.credentialid", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.CredentialID = :value")
@NamedQuery(name = "DTOBenutzerAllgemein.credentialid.multiple", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.CredentialID IN :value")
@NamedQuery(name = "DTOBenutzerAllgemein.primaryKeyQuery", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.ID = ?1")
@NamedQuery(name = "DTOBenutzerAllgemein.all.migration", query = "SELECT e FROM DTOBenutzerAllgemein e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "AnzeigeName", "CredentialID"})
public final class DTOBenutzerAllgemein {

	/** Die ID des allgemeinen Benutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Anzeigename für den allgemeinen Benutzer */
	@Column(name = "AnzeigeName")
	@JsonProperty
	public String AnzeigeName;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerAllgemein ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzerAllgemein() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzerAllgemein ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOBenutzerAllgemein(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBenutzerAllgemein other = (DTOBenutzerAllgemein) obj;
		if (ID != other.ID)
			return false;
		return true;
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
		return "DTOBenutzerAllgemein(ID=" + this.ID + ", AnzeigeName=" + this.AnzeigeName + ", CredentialID=" + this.CredentialID + ")";
	}

}
