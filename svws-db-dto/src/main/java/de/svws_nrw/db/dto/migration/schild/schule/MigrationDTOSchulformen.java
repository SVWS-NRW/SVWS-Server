package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Schulformen")
@NamedQuery(name = "MigrationDTOSchulformen.all", query = "SELECT e FROM MigrationDTOSchulformen e")
@NamedQuery(name = "MigrationDTOSchulformen.id", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchulformen.id.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.sgl", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.SGL = :value")
@NamedQuery(name = "MigrationDTOSchulformen.sgl.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.SGL IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.sf_sgl", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.SF_SGL = :value")
@NamedQuery(name = "MigrationDTOSchulformen.sf_sgl.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.SF_SGL IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.schulform", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Schulform = :value")
@NamedQuery(name = "MigrationDTOSchulformen.schulform.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Schulform IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.doppelqualifikation", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.DoppelQualifikation = :value")
@NamedQuery(name = "MigrationDTOSchulformen.doppelqualifikation.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.DoppelQualifikation IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.sortierung", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOSchulformen.sortierung.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.sichtbar", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOSchulformen.sichtbar.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.bkindex", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.BKIndex = :value")
@NamedQuery(name = "MigrationDTOSchulformen.bkindex.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.BKIndex IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.schulnreigner", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchulformen.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.schulform2", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Schulform2 = :value")
@NamedQuery(name = "MigrationDTOSchulformen.schulform2.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.Schulform2 IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchulformen.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchulformen.all.migration", query = "SELECT e FROM MigrationDTOSchulformen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SGL", "SF_SGL", "Schulform", "DoppelQualifikation", "Sortierung", "Sichtbar", "BKIndex", "SchulnrEigner", "Schulform2"})
public final class MigrationDTOSchulformen {

	/** ID der Schulgliederung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schulgliedererung die an der Schule vorkommt */
	@Column(name = "SGL")
	@JsonProperty
	public String SGL;

	/** Statistikkürzel SchulformSchulgliederung */
	@Column(name = "SF_SGL")
	@JsonProperty
	public String SF_SGL;

	/** Schulform der SGL */
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Gibt an, ob am Berufskolleg die SGL mit Doppelqualifikation abgeschlossen werden kann */
	@Column(name = "DoppelQualifikation")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean DoppelQualifikation;

	/** Sortierung der SGL */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der SGL */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** BKIndex zur SGL (IT.NRW) */
	@Column(name = "BKIndex")
	@JsonProperty
	public Integer BKIndex;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schulform2 zur SGL */
	@Column(name = "Schulform2")
	@JsonProperty
	public String Schulform2;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchulformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOSchulformen(final Long ID) {
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
		MigrationDTOSchulformen other = (MigrationDTOSchulformen) obj;
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
		return "MigrationDTOSchulformen(ID=" + this.ID + ", SGL=" + this.SGL + ", SF_SGL=" + this.SF_SGL + ", Schulform=" + this.Schulform + ", DoppelQualifikation=" + this.DoppelQualifikation + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", BKIndex=" + this.BKIndex + ", SchulnrEigner=" + this.SchulnrEigner + ", Schulform2=" + this.Schulform2 + ")";
	}

}
