package de.nrw.schule.svws.db.dto.rev9.views.benutzer;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BenutzerTypConverter;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;

import de.nrw.schule.svws.core.types.benutzer.BenutzerTyp;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nrw.schule.svws.csv.converter.current.BenutzerTypConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BenutzerTypConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbank-View V_BenutzerDetails.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "V_BenutzerDetails")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.all", query="SELECT e FROM Rev9DTOViewBenutzerdetails e")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.id", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.id.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.credentialid", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.credentialID = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.credentialid.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.credentialID IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.typ", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.Typ = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.typ.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.Typ IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.typid", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.TypID = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.typid.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.TypID IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.anzeigename", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.AnzeigeName = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.anzeigename.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.AnzeigeName IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.benutzername", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.Benutzername = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.benutzername.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.Benutzername IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.passwordhash", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.PasswordHash = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.passwordhash.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.PasswordHash IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.istadmin", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.IstAdmin = :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.istadmin.multiple", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.IstAdmin IN :value")
@NamedQuery(name="Rev9DTOViewBenutzerdetails.primaryKeyQuery", query="SELECT e FROM Rev9DTOViewBenutzerdetails e WHERE e.ID = ?1")
@JsonPropertyOrder({"ID","credentialID","Typ","TypID","AnzeigeName","Benutzername","PasswordHash","IstAdmin"})
public class Rev9DTOViewBenutzerdetails {

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
	@Convert(converter=BenutzerTypConverter.class)
	@JsonSerialize(using=BenutzerTypConverterSerializer.class)
	@JsonDeserialize(using=BenutzerTypConverterDeserializer.class)
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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstAdmin;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOViewBenutzerdetails ohne eine Initialisierung der Attribute.
	 */
	private Rev9DTOViewBenutzerdetails() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOViewBenutzerdetails other = (Rev9DTOViewBenutzerdetails) obj;
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
		return "Rev9DTOViewBenutzerdetails(ID=" + this.ID + ", credentialID=" + this.credentialID + ", Typ=" + this.Typ + ", TypID=" + this.TypID + ", AnzeigeName=" + this.AnzeigeName + ", Benutzername=" + this.Benutzername + ", PasswordHash=" + this.PasswordHash + ", IstAdmin=" + this.IstAdmin + ")";
	}

}