package de.svws_nrw.db.dto.current.views.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BenutzerTypConverter;
import de.svws_nrw.db.converter.current.Boolean01Converter;

import de.svws_nrw.core.types.benutzer.BenutzerTyp;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BenutzerTypConverterSerializer;
import de.svws_nrw.csv.converter.current.BenutzerTypConverterDeserializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbank-View V_BenutzerDetails.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "V_BenutzerDetails")
@JsonPropertyOrder({"ID", "credentialID", "Typ", "TypID", "AnzeigeName", "Benutzername", "PasswordHash", "IstAdmin"})
public final class DTOViewBenutzerdetails {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOViewBenutzerdetails e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes credentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.credentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes credentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.credentialID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Typ */
	public static final String QUERY_BY_TYP = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.Typ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Typ */
	public static final String QUERY_LIST_BY_TYP = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.Typ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TypID */
	public static final String QUERY_BY_TYPID = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.TypID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TypID */
	public static final String QUERY_LIST_BY_TYPID = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.TypID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzeigeName */
	public static final String QUERY_BY_ANZEIGENAME = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.AnzeigeName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzeigeName */
	public static final String QUERY_LIST_BY_ANZEIGENAME = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.AnzeigeName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzername */
	public static final String QUERY_BY_BENUTZERNAME = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.Benutzername = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzername */
	public static final String QUERY_LIST_BY_BENUTZERNAME = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.Benutzername IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PasswordHash */
	public static final String QUERY_BY_PASSWORDHASH = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.PasswordHash = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PasswordHash */
	public static final String QUERY_LIST_BY_PASSWORDHASH = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.PasswordHash IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstAdmin */
	public static final String QUERY_BY_ISTADMIN = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.IstAdmin = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstAdmin */
	public static final String QUERY_LIST_BY_ISTADMIN = "SELECT e FROM DTOViewBenutzerdetails e WHERE e.IstAdmin IN ?1";

	/** Die eindeutige ID des Benutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID der Benutzer-Credentials */
	@Column(name = "credentialID")
	@JsonProperty
	public Long credentialID;

	/** Der Typ des Benutzers */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter = BenutzerTypConverter.class)
	@JsonSerialize(using = BenutzerTypConverterSerializer.class)
	@JsonDeserialize(using = BenutzerTypConverterDeserializer.class)
	public BenutzerTyp Typ;

	/** Die ID des Benutzers in der Benutzer-Typ-spezifischen Tabelle (z.B. eine Schüler-ID) */
	@Column(name = "TypID")
	@JsonProperty
	public Long TypID;

	/** Der Anzeige-Name des Benutzers (z.B. Max Mustermann) */
	@Column(name = "AnzeigeName")
	@JsonProperty
	public String AnzeigeName;

	/** Der Anmeldename des Benutzers (z.B. max.mustermann) */
	@Column(name = "Benutzername")
	@JsonProperty
	public String Benutzername;

	/** Der bcrypt-Password-Hash zur Überprüfung des Benutzer-Kennwortes */
	@Column(name = "PasswordHash")
	@JsonProperty
	public String PasswordHash;

	/** Gibt an, ob es sich um einen administrativen Benutzer handelt oder nicht */
	@Column(name = "IstAdmin")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstAdmin;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOViewBenutzerdetails ohne eine Initialisierung der Attribute.
	 */
	private DTOViewBenutzerdetails() {
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOViewBenutzerdetails other = (DTOViewBenutzerdetails) obj;
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
		return "DTOViewBenutzerdetails(ID=" + this.ID + ", credentialID=" + this.credentialID + ", Typ=" + this.Typ + ", TypID=" + this.TypID + ", AnzeigeName=" + this.AnzeigeName + ", Benutzername=" + this.Benutzername + ", PasswordHash=" + this.PasswordHash + ", IstAdmin=" + this.IstAdmin + ")";
	}

}
