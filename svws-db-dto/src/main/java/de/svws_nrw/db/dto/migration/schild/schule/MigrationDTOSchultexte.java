package de.svws_nrw.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Texte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Texte")
@NamedQuery(name = "MigrationDTOSchultexte.all", query = "SELECT e FROM MigrationDTOSchultexte e")
@NamedQuery(name = "MigrationDTOSchultexte.id", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchultexte.id.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.schulnreigner", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchultexte.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.kuerzel", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTOSchultexte.kuerzel.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.inhalt", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Inhalt = :value")
@NamedQuery(name = "MigrationDTOSchultexte.inhalt.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Inhalt IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.beschreibung", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Beschreibung = :value")
@NamedQuery(name = "MigrationDTOSchultexte.beschreibung.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Beschreibung IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.aenderbar", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOSchultexte.aenderbar.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchultexte.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchultexte.all.migration", query = "SELECT e FROM MigrationDTOSchultexte e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Kuerzel", "Inhalt", "Beschreibung", "Aenderbar"})
public final class MigrationDTOSchultexte {

	/** ID des Textes unter Schulverwaltung Eigene Schule bearbeiten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Kürzel des Schultextes */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Inhalt des Schultextes */
	@Column(name = "Inhalt")
	@JsonProperty
	public String Inhalt;

	/** Beschreibung des Schultextes */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Gibt an ob der Text änderbar ist */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchultexte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchultexte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchultexte ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOSchultexte(final Long ID, final Integer SchulnrEigner) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchultexte other = (MigrationDTOSchultexte) obj;
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
		return "MigrationDTOSchultexte(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Kuerzel=" + this.Kuerzel + ", Inhalt=" + this.Inhalt + ", Beschreibung=" + this.Beschreibung + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
