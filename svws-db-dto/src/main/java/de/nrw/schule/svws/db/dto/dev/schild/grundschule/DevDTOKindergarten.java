package de.nrw.schule.svws.db.dto.dev.schild.grundschule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Kindergarten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Kindergarten")
@NamedQuery(name="DevDTOKindergarten.all", query="SELECT e FROM DevDTOKindergarten e")
@NamedQuery(name="DevDTOKindergarten.id", query="SELECT e FROM DevDTOKindergarten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOKindergarten.id.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOKindergarten.bezeichnung", query="SELECT e FROM DevDTOKindergarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOKindergarten.bezeichnung.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOKindergarten.plz", query="SELECT e FROM DevDTOKindergarten e WHERE e.PLZ = :value")
@NamedQuery(name="DevDTOKindergarten.plz.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.PLZ IN :value")
@NamedQuery(name="DevDTOKindergarten.ort", query="SELECT e FROM DevDTOKindergarten e WHERE e.Ort = :value")
@NamedQuery(name="DevDTOKindergarten.ort.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Ort IN :value")
@NamedQuery(name="DevDTOKindergarten.strassenname", query="SELECT e FROM DevDTOKindergarten e WHERE e.Strassenname = :value")
@NamedQuery(name="DevDTOKindergarten.strassenname.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Strassenname IN :value")
@NamedQuery(name="DevDTOKindergarten.hausnr", query="SELECT e FROM DevDTOKindergarten e WHERE e.HausNr = :value")
@NamedQuery(name="DevDTOKindergarten.hausnr.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.HausNr IN :value")
@NamedQuery(name="DevDTOKindergarten.hausnrzusatz", query="SELECT e FROM DevDTOKindergarten e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="DevDTOKindergarten.hausnrzusatz.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="DevDTOKindergarten.tel", query="SELECT e FROM DevDTOKindergarten e WHERE e.Tel = :value")
@NamedQuery(name="DevDTOKindergarten.tel.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Tel IN :value")
@NamedQuery(name="DevDTOKindergarten.email", query="SELECT e FROM DevDTOKindergarten e WHERE e.Email = :value")
@NamedQuery(name="DevDTOKindergarten.email.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Email IN :value")
@NamedQuery(name="DevDTOKindergarten.bemerkung", query="SELECT e FROM DevDTOKindergarten e WHERE e.Bemerkung = :value")
@NamedQuery(name="DevDTOKindergarten.bemerkung.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Bemerkung IN :value")
@NamedQuery(name="DevDTOKindergarten.sichtbar", query="SELECT e FROM DevDTOKindergarten e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOKindergarten.sichtbar.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOKindergarten.sortierung", query="SELECT e FROM DevDTOKindergarten e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOKindergarten.sortierung.multiple", query="SELECT e FROM DevDTOKindergarten e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOKindergarten.primaryKeyQuery", query="SELECT e FROM DevDTOKindergarten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOKindergarten.all.migration", query="SELECT e FROM DevDTOKindergarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","PLZ","Ort","Strassenname","HausNr","HausNrZusatz","Tel","Email","Bemerkung","Sichtbar","Sortierung"})
public class DevDTOKindergarten {

	/** ID des Kindergartens */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Kindergartens */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** PLZ  des Kindergartens */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ort des Kindergartens */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Strassenname des Kindergartens */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer des Kindergarten */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnumemrzusatz des Kindergartens */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** Telefonnummer des Kindergartens */
	@Column(name = "Tel")
	@JsonProperty
	public String Tel;

	/** E-Mail-Adresse des Kindergartens */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Bemerkung zum Kindergarten */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Sichbarkeit des Kindergartens */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierung des Kindergartens */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKindergarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKindergarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKindergarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DevDTOKindergarten(final Long ID) {
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
		DevDTOKindergarten other = (DevDTOKindergarten) obj;
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
		return "DevDTOKindergarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Tel=" + this.Tel + ", Email=" + this.Email + ", Bemerkung=" + this.Bemerkung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}