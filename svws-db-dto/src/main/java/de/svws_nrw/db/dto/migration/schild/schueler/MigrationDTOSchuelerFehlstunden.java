package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFehlstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFehlstunden")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.all", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.id", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.abschnitt_id", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.abschnitt_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.datum", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Datum = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.datum.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Datum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.fach_id", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.fehlstd", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.FehlStd = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.fehlstd.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.FehlStd IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.vonstd", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.VonStd = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.vonstd.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.VonStd IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.bisstd", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.BisStd = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.bisstd.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.BisStd IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.entschuldigt", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Entschuldigt = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.entschuldigt.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Entschuldigt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.lehrer_id", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.lehrer_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerFehlstunden.all.migration", query = "SELECT e FROM MigrationDTOSchuelerFehlstunden e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Abschnitt_ID", "Datum", "Fach_ID", "FehlStd", "VonStd", "BisStd", "Entschuldigt", "Lehrer_ID"})
public final class MigrationDTOSchuelerFehlstunden {

	/** ID des Fehlstundeneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** AbschnittsID des zugehörigen Lernabschnitts */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Datum der Fehlzeit */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Datum;

	/** FachID der Fehlzeit */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Anzahl der Fehlstunden */
	@Column(name = "FehlStd")
	@JsonProperty
	public Double FehlStd;

	/** Beginn Stunde Fehlzeit */
	@Column(name = "VonStd")
	@JsonProperty
	public Integer VonStd;

	/** Ende Stunde Fehlzeit */
	@Column(name = "BisStd")
	@JsonProperty
	public Integer BisStd;

	/** Entschuldigt Ja Nein */
	@Column(name = "Entschuldigt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Entschuldigt;

	/** LehrerID der Fehlzeit */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerFehlstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Datum   der Wert für das Attribut Datum
	 * @param FehlStd   der Wert für das Attribut FehlStd
	 */
	public MigrationDTOSchuelerFehlstunden(final Long ID, final Integer SchulnrEigner, final Long Abschnitt_ID, final String Datum, final Double FehlStd) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Datum == null) {
			throw new NullPointerException("Datum must not be null");
		}
		this.Datum = Datum;
		if (FehlStd == null) {
			throw new NullPointerException("FehlStd must not be null");
		}
		this.FehlStd = FehlStd;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerFehlstunden other = (MigrationDTOSchuelerFehlstunden) obj;
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
		return "MigrationDTOSchuelerFehlstunden(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Datum=" + this.Datum + ", Fach_ID=" + this.Fach_ID + ", FehlStd=" + this.FehlStd + ", VonStd=" + this.VonStd + ", BisStd=" + this.BisStd + ", Entschuldigt=" + this.Entschuldigt + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}
