package de.svws_nrw.db.dto.migration.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Kursart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Kursart")
@NamedQuery(name = "MigrationDTOKursarten.all", query = "SELECT e FROM MigrationDTOKursarten e")
@NamedQuery(name = "MigrationDTOKursarten.id", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOKursarten.id.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOKursarten.bezeichnung", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOKursarten.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOKursarten.internbez", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.InternBez = :value")
@NamedQuery(name = "MigrationDTOKursarten.internbez.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.InternBez IN :value")
@NamedQuery(name = "MigrationDTOKursarten.kursart", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Kursart = :value")
@NamedQuery(name = "MigrationDTOKursarten.kursart.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Kursart IN :value")
@NamedQuery(name = "MigrationDTOKursarten.kursartallg", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.KursartAllg = :value")
@NamedQuery(name = "MigrationDTOKursarten.kursartallg.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.KursartAllg IN :value")
@NamedQuery(name = "MigrationDTOKursarten.sortierung", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOKursarten.sortierung.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOKursarten.sichtbar", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOKursarten.sichtbar.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOKursarten.aenderbar", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOKursarten.aenderbar.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOKursarten.schulnreigner", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOKursarten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOKursarten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOKursarten.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOKursarten.all.migration", query = "SELECT e FROM MigrationDTOKursarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "InternBez", "Kursart", "KursartAllg", "Sortierung", "Sichtbar", "Aenderbar", "SchulnrEigner"})
public final class MigrationDTOKursarten {

	/** ID des Kursarteneintrag */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Kursarteneintrags IT.NRW */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Interne Bezeichnung Kursarteneintrag */
	@Column(name = "InternBez")
	@JsonProperty
	public String InternBez;

	/** Kürzel Kursart */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Allgemeine Bezeichnung Kursart (zB GK bei GKM) */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Sortierung der Kursart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Kursart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Kursart änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKursarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKursarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKursarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOKursarten(final Long ID) {
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
		MigrationDTOKursarten other = (MigrationDTOKursarten) obj;
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
		return "MigrationDTOKursarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", InternBez=" + this.InternBez + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
