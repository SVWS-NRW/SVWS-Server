package de.svws_nrw.db.dto.current.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Kindergarten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Kindergarten")
@NamedQuery(name = "DTOKindergarten.all", query = "SELECT e FROM DTOKindergarten e")
@NamedQuery(name = "DTOKindergarten.id", query = "SELECT e FROM DTOKindergarten e WHERE e.ID = :value")
@NamedQuery(name = "DTOKindergarten.id.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKindergarten.bezeichnung", query = "SELECT e FROM DTOKindergarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOKindergarten.bezeichnung.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOKindergarten.plz", query = "SELECT e FROM DTOKindergarten e WHERE e.PLZ = :value")
@NamedQuery(name = "DTOKindergarten.plz.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.PLZ IN :value")
@NamedQuery(name = "DTOKindergarten.ort", query = "SELECT e FROM DTOKindergarten e WHERE e.Ort = :value")
@NamedQuery(name = "DTOKindergarten.ort.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Ort IN :value")
@NamedQuery(name = "DTOKindergarten.strassenname", query = "SELECT e FROM DTOKindergarten e WHERE e.Strassenname = :value")
@NamedQuery(name = "DTOKindergarten.strassenname.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Strassenname IN :value")
@NamedQuery(name = "DTOKindergarten.hausnr", query = "SELECT e FROM DTOKindergarten e WHERE e.HausNr = :value")
@NamedQuery(name = "DTOKindergarten.hausnr.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.HausNr IN :value")
@NamedQuery(name = "DTOKindergarten.hausnrzusatz", query = "SELECT e FROM DTOKindergarten e WHERE e.HausNrZusatz = :value")
@NamedQuery(name = "DTOKindergarten.hausnrzusatz.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name = "DTOKindergarten.tel", query = "SELECT e FROM DTOKindergarten e WHERE e.Tel = :value")
@NamedQuery(name = "DTOKindergarten.tel.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Tel IN :value")
@NamedQuery(name = "DTOKindergarten.email", query = "SELECT e FROM DTOKindergarten e WHERE e.Email = :value")
@NamedQuery(name = "DTOKindergarten.email.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Email IN :value")
@NamedQuery(name = "DTOKindergarten.bemerkung", query = "SELECT e FROM DTOKindergarten e WHERE e.Bemerkung = :value")
@NamedQuery(name = "DTOKindergarten.bemerkung.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "DTOKindergarten.sichtbar", query = "SELECT e FROM DTOKindergarten e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOKindergarten.sichtbar.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOKindergarten.sortierung", query = "SELECT e FROM DTOKindergarten e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOKindergarten.sortierung.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOKindergarten.primaryKeyQuery", query = "SELECT e FROM DTOKindergarten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKindergarten.primaryKeyQuery.multiple", query = "SELECT e FROM DTOKindergarten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKindergarten.all.migration", query = "SELECT e FROM DTOKindergarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "PLZ", "Ort", "Strassenname", "HausNr", "HausNrZusatz", "Tel", "Email", "Bemerkung", "Sichtbar", "Sortierung"})
public final class DTOKindergarten {

	/** ID des Kindergartens */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierung des Kindergartens */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKindergarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKindergarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKindergarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOKindergarten(final long ID) {
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
		DTOKindergarten other = (DTOKindergarten) obj;
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
		return "DTOKindergarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Tel=" + this.Tel + ", Email=" + this.Email + ", Bemerkung=" + this.Bemerkung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}
