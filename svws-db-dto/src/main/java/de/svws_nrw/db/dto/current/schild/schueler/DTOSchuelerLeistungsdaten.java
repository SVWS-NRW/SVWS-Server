package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLeistungsdaten")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Fach_ID", "Hochrechnung", "Fachlehrer_ID", "Kursart", "KursartAllg", "Kurs_ID", "NotenKrz", "NotenKrzQuartal", "Warnung", "Warndatum", "AbiFach", "Wochenstunden", "AbiZeugnis", "Prognose", "FehlStd", "uFehlStd", "Sortierung", "Lernentw", "Gekoppelt", "VorherAbgeschl", "AbschlussJahrgang", "HochrechnungStatus", "SchulNr", "Zusatzkraft_ID", "WochenstdZusatzkraft", "Prf10Fach", "AufZeugnis", "Gewichtung", "NoteAbschlussBA", "Umfang"})
public final class DTOSchuelerLeistungsdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerLeistungsdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Hochrechnung */
	public static final String QUERY_BY_HOCHRECHNUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Hochrechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Hochrechnung */
	public static final String QUERY_LIST_BY_HOCHRECHNUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Hochrechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachlehrer_ID */
	public static final String QUERY_BY_FACHLEHRER_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachlehrer_ID */
	public static final String QUERY_LIST_BY_FACHLEHRER_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart */
	public static final String QUERY_BY_KURSART = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart */
	public static final String QUERY_LIST_BY_KURSART = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurs_ID */
	public static final String QUERY_BY_KURS_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurs_ID */
	public static final String QUERY_LIST_BY_KURS_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotenKrz */
	public static final String QUERY_BY_NOTENKRZ = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotenKrz */
	public static final String QUERY_LIST_BY_NOTENKRZ = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NotenKrzQuartal */
	public static final String QUERY_BY_NOTENKRZQUARTAL = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrzQuartal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NotenKrzQuartal */
	public static final String QUERY_LIST_BY_NOTENKRZQUARTAL = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NotenKrzQuartal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Warnung */
	public static final String QUERY_BY_WARNUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Warnung */
	public static final String QUERY_LIST_BY_WARNUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Warndatum */
	public static final String QUERY_BY_WARNDATUM = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warndatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Warndatum */
	public static final String QUERY_LIST_BY_WARNDATUM = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Warndatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiFach */
	public static final String QUERY_BY_ABIFACH = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiFach */
	public static final String QUERY_LIST_BY_ABIFACH = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochenstunden */
	public static final String QUERY_BY_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochenstunden */
	public static final String QUERY_LIST_BY_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiZeugnis */
	public static final String QUERY_BY_ABIZEUGNIS = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiZeugnis */
	public static final String QUERY_LIST_BY_ABIZEUGNIS = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Prognose */
	public static final String QUERY_BY_PROGNOSE = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prognose = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Prognose */
	public static final String QUERY_LIST_BY_PROGNOSE = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prognose IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FehlStd */
	public static final String QUERY_BY_FEHLSTD = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.FehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FehlStd */
	public static final String QUERY_LIST_BY_FEHLSTD = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.FehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes uFehlStd */
	public static final String QUERY_BY_UFEHLSTD = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.uFehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes uFehlStd */
	public static final String QUERY_LIST_BY_UFEHLSTD = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.uFehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernentw */
	public static final String QUERY_BY_LERNENTW = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Lernentw = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernentw */
	public static final String QUERY_LIST_BY_LERNENTW = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Lernentw IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gekoppelt */
	public static final String QUERY_BY_GEKOPPELT = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gekoppelt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gekoppelt */
	public static final String QUERY_LIST_BY_GEKOPPELT = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gekoppelt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VorherAbgeschl */
	public static final String QUERY_BY_VORHERABGESCHL = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VorherAbgeschl */
	public static final String QUERY_LIST_BY_VORHERABGESCHL = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschlussJahrgang */
	public static final String QUERY_BY_ABSCHLUSSJAHRGANG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschlussJahrgang */
	public static final String QUERY_LIST_BY_ABSCHLUSSJAHRGANG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HochrechnungStatus */
	public static final String QUERY_BY_HOCHRECHNUNGSTATUS = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HochrechnungStatus */
	public static final String QUERY_LIST_BY_HOCHRECHNUNGSTATUS = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulNr */
	public static final String QUERY_BY_SCHULNR = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.SchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulNr */
	public static final String QUERY_LIST_BY_SCHULNR = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.SchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zusatzkraft_ID */
	public static final String QUERY_BY_ZUSATZKRAFT_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zusatzkraft_ID */
	public static final String QUERY_LIST_BY_ZUSATZKRAFT_ID = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstdZusatzkraft */
	public static final String QUERY_BY_WOCHENSTDZUSATZKRAFT = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstdZusatzkraft */
	public static final String QUERY_LIST_BY_WOCHENSTDZUSATZKRAFT = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Prf10Fach */
	public static final String QUERY_BY_PRF10FACH = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prf10Fach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Prf10Fach */
	public static final String QUERY_LIST_BY_PRF10FACH = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Prf10Fach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AufZeugnis */
	public static final String QUERY_BY_AUFZEUGNIS = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AufZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AufZeugnis */
	public static final String QUERY_LIST_BY_AUFZEUGNIS = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.AufZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gewichtung */
	public static final String QUERY_BY_GEWICHTUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gewichtung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gewichtung */
	public static final String QUERY_LIST_BY_GEWICHTUNG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Gewichtung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteAbschlussBA */
	public static final String QUERY_BY_NOTEABSCHLUSSBA = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteAbschlussBA */
	public static final String QUERY_LIST_BY_NOTEABSCHLUSSBA = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Umfang */
	public static final String QUERY_BY_UMFANG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Umfang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Umfang */
	public static final String QUERY_LIST_BY_UMFANG = "SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Umfang IN ?1";

	/** Eine eindeutige ID für die Leistungsdaten des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die eindeutige ID des zugehörigen Lernabschnittes – verweist auf den Lernabschnitt des Schülers */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Die eindeutige ID des zugehörigen Faches – verweist auf das Fach */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Gibt an ob der Datensatz aus einem vorherigen Abschnitt geholt wurde die Minuszahl gibt die Schritte zurück an */
	@Column(name = "Hochrechnung")
	@JsonProperty
	public Integer Hochrechnung;

	/** Die ID des zugehörigen Fach-Lehrers */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/** Die Kursart */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Die allgemeine Kursart des Faches (z.B. GK, LK) */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Die eindeutige ID des zugehörigen Kurses, sofern kein Klassenunterricht vorliegt – verweist auf den Kurs */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Das Notenkürzel der erteilten Note */
	@Column(name = "NotenKrz")
	@JsonProperty
	public String NotenKrz;

	/** Das Notenkürzel der Quartals-Note */
	@Column(name = "NotenKrzQuartal")
	@JsonProperty
	public String NotenKrzQuartal;

	/** gibt an, ob die Leistung gemahnt wurde bzw. gemahnt werden soll – sie Mahndatum */
	@Column(name = "Warnung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Warnung;

	/** gibt das Datum an, wann die Leistung gemahnt wurde */
	@Column(name = "Warndatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Warndatum;

	/** gibt an, ob das Fach als Abiturfach belegt wurde (NULL, 1, 2, 3, 4) */
	@Column(name = "AbiFach")
	@JsonProperty
	public String AbiFach;

	/** gibt die Wochenstunden */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public Integer Wochenstunden;

	/** DEPRECATED: Relikt aus Winschild nicht mehr benötigt */
	@Column(name = "AbiZeugnis")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AbiZeugnis;

	/** DEPRECATED: Relikt aus Winschild nicht mehr benötigt */
	@Column(name = "Prognose")
	@JsonProperty
	public String Prognose;

	/** Fachbezogene Fehlstunden */
	@Column(name = "FehlStd")
	@JsonProperty
	public Integer FehlStd;

	/** Fachbezogene Fehlstunden unentschuldigt */
	@Column(name = "uFehlStd")
	@JsonProperty
	public Integer uFehlStd;

	/** Sortierungnummer des Leistungsdatensatzes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Text für Fachbezogene Lernentwicklung Bemerkung */
	@Column(name = "Lernentw")
	@JsonProperty
	public String Lernentw;

	/** DEPRECATED: Relikt aus Winschild nicht mehr benötigt */
	@Column(name = "Gekoppelt")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gekoppelt;

	/** Gibt an ob das Fach Epochal war oder ist */
	@Column(name = "VorherAbgeschl")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean VorherAbgeschl;

	/** Hier wird beim Holen von abgeschlossenen Fächern eingetragen, wann das Fach zuletzt unterrichtet wurde */
	@Column(name = "AbschlussJahrgang")
	@JsonProperty
	public String AbschlussJahrgang;

	/** DEPRECATED: Hier wurde früher gespeichert, ob es sich um eine temporäre oder dauerhafte Hochrechnung handelt. Eine Hochrechnung ist nur noch am BK möglich und immer temporär. */
	@Column(name = "HochrechnungStatus")
	@JsonProperty
	public String HochrechnungStatus;

	/** Schulnummer bei externem Unterricht */
	@Column(name = "SchulNr")
	@JsonProperty
	public Integer SchulNr;

	/** Die Lehrer-ID der Zusatzkraft */
	@Column(name = "Zusatzkraft_ID")
	@JsonProperty
	public Long Zusatzkraft_ID;

	/** Wochenstunden Zusatzkraft */
	@Column(name = "WochenstdZusatzkraft")
	@JsonProperty
	public Integer WochenstdZusatzkraft;

	/** Ist Fach für ZP10 / ZK10 */
	@Column(name = "Prf10Fach")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Prf10Fach;

	/** Fach kommt aufs Zeugnnis Ja Nein */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AufZeugnis;

	/** Gewichtung allgemeinbidend BK */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** Beim BK wird hier die Note Berufsabschluss eingetragen. Ist vermutl. nicht mehr notwendig, wenn alle Zeugnisse sich aus dem BKAbschluss-Fächern bedienen */
	@Column(name = "NoteAbschlussBA")
	@JsonProperty
	public String NoteAbschlussBA;

	/** Facheigenschaft für Lernstandsberichte (V voller Umfang) (R reduzierter Umfang) */
	@Column(name = "Umfang")
	@JsonProperty
	public String Umfang;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerLeistungsdaten(final long ID, final long Abschnitt_ID, final long Fach_ID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerLeistungsdaten other = (DTOSchuelerLeistungsdaten) obj;
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
		return "DTOSchuelerLeistungsdaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Fach_ID=" + this.Fach_ID + ", Hochrechnung=" + this.Hochrechnung + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Kurs_ID=" + this.Kurs_ID + ", NotenKrz=" + this.NotenKrz + ", NotenKrzQuartal=" + this.NotenKrzQuartal + ", Warnung=" + this.Warnung + ", Warndatum=" + this.Warndatum + ", AbiFach=" + this.AbiFach + ", Wochenstunden=" + this.Wochenstunden + ", AbiZeugnis=" + this.AbiZeugnis + ", Prognose=" + this.Prognose + ", FehlStd=" + this.FehlStd + ", uFehlStd=" + this.uFehlStd + ", Sortierung=" + this.Sortierung + ", Lernentw=" + this.Lernentw + ", Gekoppelt=" + this.Gekoppelt + ", VorherAbgeschl=" + this.VorherAbgeschl + ", AbschlussJahrgang=" + this.AbschlussJahrgang + ", HochrechnungStatus=" + this.HochrechnungStatus + ", SchulNr=" + this.SchulNr + ", Zusatzkraft_ID=" + this.Zusatzkraft_ID + ", WochenstdZusatzkraft=" + this.WochenstdZusatzkraft + ", Prf10Fach=" + this.Prf10Fach + ", AufZeugnis=" + this.AufZeugnis + ", Gewichtung=" + this.Gewichtung + ", NoteAbschlussBA=" + this.NoteAbschlussBA + ", Umfang=" + this.Umfang + ")";
	}

}
