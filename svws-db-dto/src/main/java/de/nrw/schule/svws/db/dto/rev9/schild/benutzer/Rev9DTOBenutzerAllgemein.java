package de.nrw.schule.svws.db.dto.rev9.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzerAllgemein.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzerAllgemein")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.all", query="SELECT e FROM Rev9DTOBenutzerAllgemein e")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.id", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.id.multiple", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.anzeigename", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.AnzeigeName = :value")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.anzeigename.multiple", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.AnzeigeName IN :value")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.credentialid", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.CredentialID = :value")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.credentialid.multiple", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.CredentialID IN :value")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.primaryKeyQuery", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOBenutzerAllgemein.all.migration", query="SELECT e FROM Rev9DTOBenutzerAllgemein e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","AnzeigeName","CredentialID"})
public class Rev9DTOBenutzerAllgemein {

	/** Die ID des allgemeinen Benutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Anzeigename für den allgemeinen Benutzer */
	@Column(name = "AnzeigeName")
	@JsonProperty
	public String AnzeigeName;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOBenutzerAllgemein ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOBenutzerAllgemein() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOBenutzerAllgemein ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev9DTOBenutzerAllgemein(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOBenutzerAllgemein other = (Rev9DTOBenutzerAllgemein) obj;
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
		return "Rev9DTOBenutzerAllgemein(ID=" + this.ID + ", AnzeigeName=" + this.AnzeigeName + ", CredentialID=" + this.CredentialID + ")";
	}

}