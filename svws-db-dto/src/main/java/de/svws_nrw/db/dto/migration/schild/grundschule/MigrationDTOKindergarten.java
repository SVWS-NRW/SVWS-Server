package de.svws_nrw.db.dto.migration.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Kindergarten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Kindergarten")
@NamedQuery(name = "MigrationDTOKindergarten.all", query = "SELECT e FROM MigrationDTOKindergarten e")
@NamedQuery(name = "MigrationDTOKindergarten.id", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOKindergarten.id.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.bezeichnung", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOKindergarten.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.plz", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.PLZ = :value")
@NamedQuery(name = "MigrationDTOKindergarten.plz.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.PLZ IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.ort", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Ort = :value")
@NamedQuery(name = "MigrationDTOKindergarten.ort.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Ort IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.strasse", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Strasse = :value")
@NamedQuery(name = "MigrationDTOKindergarten.strasse.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Strasse IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.strassenname", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Strassenname = :value")
@NamedQuery(name = "MigrationDTOKindergarten.strassenname.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Strassenname IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.hausnr", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.HausNr = :value")
@NamedQuery(name = "MigrationDTOKindergarten.hausnr.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.HausNr IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.hausnrzusatz", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.HausNrZusatz = :value")
@NamedQuery(name = "MigrationDTOKindergarten.hausnrzusatz.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.tel", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Tel = :value")
@NamedQuery(name = "MigrationDTOKindergarten.tel.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Tel IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.email", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Email = :value")
@NamedQuery(name = "MigrationDTOKindergarten.email.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Email IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.bemerkung", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Bemerkung = :value")
@NamedQuery(name = "MigrationDTOKindergarten.bemerkung.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.sichtbar", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOKindergarten.sichtbar.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.sortierung", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOKindergarten.sortierung.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.schulnreigner", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOKindergarten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOKindergarten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOKindergarten.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOKindergarten.all.migration", query = "SELECT e FROM MigrationDTOKindergarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "PLZ", "Ort", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Tel", "Email", "Bemerkung", "Sichtbar", "Sortierung", "SchulnrEigner"})
public final class MigrationDTOKindergarten {

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

	/** Straße des Kindergartens */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierung des Kindergartens */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKindergarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKindergarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKindergarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOKindergarten(final Long ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
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
		MigrationDTOKindergarten other = (MigrationDTOKindergarten) obj;
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
		return "MigrationDTOKindergarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Tel=" + this.Tel + ", Email=" + this.Email + ", Bemerkung=" + this.Bemerkung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
