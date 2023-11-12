package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerMerkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerMerkmale")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.all", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.id", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.kurztext", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.Kurztext = :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.kurztext.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.Kurztext IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.datumvon", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.DatumVon = :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.datumvon.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.DatumVon IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.datumbis", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.DatumBis = :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.datumbis.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.DatumBis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerMerkmale.all.migration", query = "SELECT e FROM MigrationDTOSchuelerMerkmale e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Kurztext", "SchulnrEigner", "DatumVon", "DatumBis"})
public final class MigrationDTOSchuelerMerkmale {

	/** ID des Eintrag bei besondere Merkmale zum Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schüler-ID des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Kurztext des Merkmals des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Datum Beginn des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumVon")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String DatumVon;

	/** Datum Ende des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String DatumBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerMerkmale(final Long ID, final Long Schueler_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerMerkmale other = (MigrationDTOSchuelerMerkmale) obj;
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
		return "MigrationDTOSchuelerMerkmale(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Kurztext=" + this.Kurztext + ", SchulnrEigner=" + this.SchulnrEigner + ", DatumVon=" + this.DatumVon + ", DatumBis=" + this.DatumBis + ")";
	}

}
