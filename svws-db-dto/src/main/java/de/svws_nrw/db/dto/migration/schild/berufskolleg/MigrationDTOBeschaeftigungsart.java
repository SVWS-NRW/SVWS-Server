package de.svws_nrw.db.dto.migration.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_BeschaeftigungsArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_BeschaeftigungsArt")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.all", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.id", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.id.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.bezeichnung", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.sortierung", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.sortierung.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.sichtbar", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.sichtbar.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.aenderbar", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.aenderbar.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.schulnreigner", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.primaryKeyQuery", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOBeschaeftigungsart.all.migration", query = "SELECT e FROM MigrationDTOBeschaeftigungsart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar", "SchulnrEigner"})
public final class MigrationDTOBeschaeftigungsart {

	/** ID der Beschäftigungsart unter weitere Adressen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Beschäftigungsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Beschäftigungsart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Beschäftigungsart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Beschäftigungsart ist änderbar Ja Nein */
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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBeschaeftigungsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBeschaeftigungsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBeschaeftigungsart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOBeschaeftigungsart(final Long ID, final String Bezeichnung) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBeschaeftigungsart other = (MigrationDTOBeschaeftigungsart) obj;
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
		return "MigrationDTOBeschaeftigungsart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
