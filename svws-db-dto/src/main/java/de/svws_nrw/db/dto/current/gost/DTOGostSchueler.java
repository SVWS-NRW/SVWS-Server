package de.svws_nrw.db.dto.current.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Schueler")
@JsonPropertyOrder({"Schueler_ID", "DatumBeratung", "DatumRuecklauf", "HatSportattest", "Kommentar", "Beratungslehrer_ID", "PruefPhase", "BesondereLernleistung_Art", "BesondereLernleistung_Punkte"})
public final class DTOGostSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOGostSchueler e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumBeratung */
	public static final String QUERY_BY_DATUMBERATUNG = "SELECT e FROM DTOGostSchueler e WHERE e.DatumBeratung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumBeratung */
	public static final String QUERY_LIST_BY_DATUMBERATUNG = "SELECT e FROM DTOGostSchueler e WHERE e.DatumBeratung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumRuecklauf */
	public static final String QUERY_BY_DATUMRUECKLAUF = "SELECT e FROM DTOGostSchueler e WHERE e.DatumRuecklauf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumRuecklauf */
	public static final String QUERY_LIST_BY_DATUMRUECKLAUF = "SELECT e FROM DTOGostSchueler e WHERE e.DatumRuecklauf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HatSportattest */
	public static final String QUERY_BY_HATSPORTATTEST = "SELECT e FROM DTOGostSchueler e WHERE e.HatSportattest = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HatSportattest */
	public static final String QUERY_LIST_BY_HATSPORTATTEST = "SELECT e FROM DTOGostSchueler e WHERE e.HatSportattest IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kommentar */
	public static final String QUERY_BY_KOMMENTAR = "SELECT e FROM DTOGostSchueler e WHERE e.Kommentar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kommentar */
	public static final String QUERY_LIST_BY_KOMMENTAR = "SELECT e FROM DTOGostSchueler e WHERE e.Kommentar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beratungslehrer_ID */
	public static final String QUERY_BY_BERATUNGSLEHRER_ID = "SELECT e FROM DTOGostSchueler e WHERE e.Beratungslehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beratungslehrer_ID */
	public static final String QUERY_LIST_BY_BERATUNGSLEHRER_ID = "SELECT e FROM DTOGostSchueler e WHERE e.Beratungslehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefPhase */
	public static final String QUERY_BY_PRUEFPHASE = "SELECT e FROM DTOGostSchueler e WHERE e.PruefPhase = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefPhase */
	public static final String QUERY_LIST_BY_PRUEFPHASE = "SELECT e FROM DTOGostSchueler e WHERE e.PruefPhase IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BesondereLernleistung_Art */
	public static final String QUERY_BY_BESONDERELERNLEISTUNG_ART = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Art = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BesondereLernleistung_Art */
	public static final String QUERY_LIST_BY_BESONDERELERNLEISTUNG_ART = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Art IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BesondereLernleistung_Punkte */
	public static final String QUERY_BY_BESONDERELERNLEISTUNG_PUNKTE = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BesondereLernleistung_Punkte */
	public static final String QUERY_LIST_BY_BESONDERELERNLEISTUNG_PUNKTE = "SELECT e FROM DTOGostSchueler e WHERE e.BesondereLernleistung_Punkte IN ?1";

	/** Gymnasiale Oberstufe - Schülerdaten: Die ID des Schülers in der Schülertabelle */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Gymnasiale Oberstufe - Schülerdaten: Das Datum der letzten Beratung des Schülers */
	@Column(name = "DatumBeratung")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumBeratung;

	/** Gymnasiale Oberstufe - Schülerdaten: Das Datum an dem der letzte Beratungsbogen des Schülersmit seiner Fächerwahl in der Schule eingereicht wurde */
	@Column(name = "DatumRuecklauf")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumRuecklauf;

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an, ob ein Sportattest bei dem Schüler vorliegt oder nicht und die Wahl eines Ersatzfaches zulässig ist: 1 - true, 0 - false */
	@Column(name = "HatSportattest")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean HatSportattest;

	/** Gymnasiale Oberstufe - Schülerdaten: Kommentar des Beratungslehrers zur der Wahl des Schülers */
	@Column(name = "Kommentar")
	@JsonProperty
	public String Kommentar;

	/** Gymnasiale Oberstufe - Schülerdaten: ID des Beratungslehrers, der die letzte Beratung vorgenommen hat */
	@Column(name = "Beratungslehrer_ID")
	@JsonProperty
	public Long Beratungslehrer_ID;

	/** Gymnasiale Oberstufe - Schülerdaten: Gibt an welche Halbjahre bei der Belegprüfung geprüft werden sollen (E - nur EF.1, G - Gesamtprüfung bis einschließlich Q2.2) */
	@Column(name = "PruefPhase")
	@JsonProperty
	public String PruefPhase;

	/** Gymnasiale Oberstufe - Schülerdaten: Die Art einer besonderen Lernleistung */
	@Column(name = "BesondereLernleistung_Art")
	@JsonProperty
	public String BesondereLernleistung_Art;

	/** Gymnasiale Oberstufe - Schülerdaten: Die Notenpunkte der besonderen Lernleistung  */
	@Column(name = "BesondereLernleistung_Punkte")
	@JsonProperty
	public Integer BesondereLernleistung_Punkte;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchueler ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param HatSportattest   der Wert für das Attribut HatSportattest
	 */
	public DTOGostSchueler(final long Schueler_ID, final Boolean HatSportattest) {
		this.Schueler_ID = Schueler_ID;
		this.HatSportattest = HatSportattest;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostSchueler other = (DTOGostSchueler) obj;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostSchueler(Schueler_ID=" + this.Schueler_ID + ", DatumBeratung=" + this.DatumBeratung + ", DatumRuecklauf=" + this.DatumRuecklauf + ", HatSportattest=" + this.HatSportattest + ", Kommentar=" + this.Kommentar + ", Beratungslehrer_ID=" + this.Beratungslehrer_ID + ", PruefPhase=" + this.PruefPhase + ", BesondereLernleistung_Art=" + this.BesondereLernleistung_Art + ", BesondereLernleistung_Punkte=" + this.BesondereLernleistung_Punkte + ")";
	}

}
