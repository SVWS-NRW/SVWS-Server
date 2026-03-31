package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_LehrerAnrechnungsstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_LehrerAnrechnungsstunden")
@JsonPropertyOrder({"ID", "Lehrer_ID", "AnrechnungsgrundKrz", "AnzahlStunden", "GueltigVon", "GueltigBis"})
public final class DTOUvLehrerAnrechnungsstunden {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungsgrundKrz */
	public static final String QUERY_BY_ANRECHNUNGSGRUNDKRZ = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.AnrechnungsgrundKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungsgrundKrz */
	public static final String QUERY_LIST_BY_ANRECHNUNGSGRUNDKRZ = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.AnrechnungsgrundKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzahlStunden */
	public static final String QUERY_BY_ANZAHLSTUNDEN = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.AnzahlStunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzahlStunden */
	public static final String QUERY_LIST_BY_ANZAHLSTUNDEN = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.AnzahlStunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.GueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.GueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.GueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOUvLehrerAnrechnungsstunden e WHERE e.GueltigBis IN ?1";

	/** ID des Anrechnungsstunden-Eintrags (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des UV-Lehrers des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Lehrer */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Anrechnungsstundentext für die Anrechnungsstunden */
	@Column(name = "AnrechnungsgrundKrz")
	@JsonProperty
	public String AnrechnungsgrundKrz;

	/** Anzahl der Anrechnungsstunden */
	@Column(name = "AnzahlStunden")
	@JsonProperty
	public double AnzahlStunden;

	/** Das Datum, ab dem der Raum gültig ist */
	@Column(name = "GueltigVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigVon;

	/** Das Datum, bis wann die Anrechnungsstunde gültig ist */
	@Column(name = "GueltigBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLehrerAnrechnungsstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvLehrerAnrechnungsstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLehrerAnrechnungsstunden ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param AnrechnungsgrundKrz   der Wert für das Attribut AnrechnungsgrundKrz
	 * @param AnzahlStunden   der Wert für das Attribut AnzahlStunden
	 * @param GueltigVon   der Wert für das Attribut GueltigVon
	 * @param GueltigBis   der Wert für das Attribut GueltigBis
	 */
	public DTOUvLehrerAnrechnungsstunden(final long ID, final long Lehrer_ID, final String AnrechnungsgrundKrz, final double AnzahlStunden, final String GueltigVon, final String GueltigBis) {
		this.ID = ID;
		this.Lehrer_ID = Lehrer_ID;
		if (AnrechnungsgrundKrz == null) {
			throw new NullPointerException("AnrechnungsgrundKrz must not be null");
		}
		this.AnrechnungsgrundKrz = AnrechnungsgrundKrz;
		this.AnzahlStunden = AnzahlStunden;
		if (GueltigVon == null) {
			throw new NullPointerException("GueltigVon must not be null");
		}
		this.GueltigVon = GueltigVon;
		if (GueltigBis == null) {
			throw new NullPointerException("GueltigBis must not be null");
		}
		this.GueltigBis = GueltigBis;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOUvLehrerAnrechnungsstunden other = (DTOUvLehrerAnrechnungsstunden) obj;
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
		return "DTOUvLehrerAnrechnungsstunden(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", AnrechnungsgrundKrz=" + this.AnrechnungsgrundKrz + ", AnzahlStunden=" + this.AnzahlStunden + ", GueltigVon=" + this.GueltigVon + ", GueltigBis=" + this.GueltigBis + ")";
	}

}
