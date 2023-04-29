package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFehlstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFehlstunden")
@NamedQuery(name = "DTOSchuelerFehlstunden.all", query = "SELECT e FROM DTOSchuelerFehlstunden e")
@NamedQuery(name = "DTOSchuelerFehlstunden.id", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.id.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.abschnitt_id", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.abschnitt_id.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.datum", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Datum = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.datum.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Datum IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.fach_id", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Fach_ID = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.fach_id.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.fehlstd", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.FehlStd = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.fehlstd.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.FehlStd IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.vonstd", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.VonStd = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.vonstd.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.VonStd IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.bisstd", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.BisStd = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.bisstd.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.BisStd IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.entschuldigt", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Entschuldigt = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.entschuldigt.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Entschuldigt IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.lehrer_id", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.lehrer_id.multiple", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOSchuelerFehlstunden.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerFehlstunden.all.migration", query = "SELECT e FROM DTOSchuelerFehlstunden e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Datum", "Fach_ID", "FehlStd", "VonStd", "BisStd", "Entschuldigt", "Lehrer_ID"})
public final class DTOSchuelerFehlstunden {

	/** ID des Fehlstundeneintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** AbschnittsID des zugehörigen Lernabschnitts */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Datum der Fehlzeit */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Datum;

	/** FachID der Fehlzeit */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Anzahl der Fehlstunden */
	@Column(name = "FehlStd")
	@JsonProperty
	public double FehlStd;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Entschuldigt;

	/** LehrerID der Fehlzeit */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerFehlstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFehlstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Datum   der Wert für das Attribut Datum
	 * @param FehlStd   der Wert für das Attribut FehlStd
	 */
	public DTOSchuelerFehlstunden(final long ID, final long Abschnitt_ID, final String Datum, final double FehlStd) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		if (Datum == null) {
			throw new NullPointerException("Datum must not be null");
		}
		this.Datum = Datum;
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
		DTOSchuelerFehlstunden other = (DTOSchuelerFehlstunden) obj;
		if (ID != other.ID)
			return false;
		return true;
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
		return "DTOSchuelerFehlstunden(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Datum=" + this.Datum + ", Fach_ID=" + this.Fach_ID + ", FehlStd=" + this.FehlStd + ", VonStd=" + this.VonStd + ", BisStd=" + this.BisStd + ", Entschuldigt=" + this.Entschuldigt + ", Lehrer_ID=" + this.Lehrer_ID + ")";
	}

}
