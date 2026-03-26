package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerUnterrichtsfaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerUnterrichtsfaecher")
@JsonPropertyOrder({"ID", "Lehrer_ID", "Fach_ID", "IstSek1", "IstSek2", "Bemerkung", "GueltigVon", "GueltigBis"})
public final class DTOLehrerUnterrichtsfach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerUnterrichtsfach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstSek1 */
	public static final String QUERY_BY_ISTSEK1 = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.IstSek1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstSek1 */
	public static final String QUERY_LIST_BY_ISTSEK1 = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.IstSek1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstSek2 */
	public static final String QUERY_BY_ISTSEK2 = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.IstSek2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstSek2 */
	public static final String QUERY_LIST_BY_ISTSEK2 = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.IstSek2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.GueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.GueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.GueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOLehrerUnterrichtsfach e WHERE e.GueltigBis IN ?1";

	/** ID für den Eintrag für das Unterrichtsfach */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Lehrers */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Die ID des Fachs */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf */
	@Column(name = "IstSek1")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstSek1;

	/** Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf */
	@Column(name = "IstSek2")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstSek2;

	/** Bemerkung zum Unterrichtsfach */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Das Datum, ab dem die Lehrkraft das Fach unterrichtet */
	@Column(name = "GueltigVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigVon;

	/** Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet */
	@Column(name = "GueltigBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String GueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerUnterrichtsfach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerUnterrichtsfach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerUnterrichtsfach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param IstSek1   der Wert für das Attribut IstSek1
	 * @param IstSek2   der Wert für das Attribut IstSek2
	 */
	public DTOLehrerUnterrichtsfach(final long ID, final long Lehrer_ID, final long Fach_ID, final Boolean IstSek1, final Boolean IstSek2) {
		this.ID = ID;
		this.Lehrer_ID = Lehrer_ID;
		this.Fach_ID = Fach_ID;
		this.IstSek1 = IstSek1;
		this.IstSek2 = IstSek2;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerUnterrichtsfach other = (DTOLehrerUnterrichtsfach) obj;
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
		return "DTOLehrerUnterrichtsfach(ID=" + this.ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Fach_ID=" + this.Fach_ID + ", IstSek1=" + this.IstSek1 + ", IstSek2=" + this.IstSek2 + ", Bemerkung=" + this.Bemerkung + ", GueltigVon=" + this.GueltigVon + ", GueltigBis=" + this.GueltigBis + ")";
	}

}
