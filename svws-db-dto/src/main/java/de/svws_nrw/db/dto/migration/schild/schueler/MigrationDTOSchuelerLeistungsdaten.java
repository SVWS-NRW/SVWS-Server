package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLeistungsdaten")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.all", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.id", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abschnitt_id", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abschnitt_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fach_id", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.hochrechnung", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Hochrechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.hochrechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Hochrechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fachlehrer_id", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fachlehrer_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.kursart", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Kursart = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.kursart.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Kursart IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.kursartallg", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.KursartAllg = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.kursartallg.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.KursartAllg IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.kurs_id", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Kurs_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.kurs_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Kurs_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.notenkrz", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.NotenKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.notenkrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.NotenKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.notenkrzquartal", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.NotenKrzQuartal = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.notenkrzquartal.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.NotenKrzQuartal IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.warnung", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Warnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.warnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Warnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.warndatum", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Warndatum = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.warndatum.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Warndatum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abifach", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AbiFach = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abifach.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AbiFach IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.wochenstunden", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Wochenstunden = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.wochenstunden.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Wochenstunden IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abizeugnis", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abizeugnis.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AbiZeugnis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.prognose", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Prognose = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.prognose.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Prognose IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fehlstd", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.FehlStd = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fehlstd.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.FehlStd IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.ufehlstd", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.uFehlStd = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.ufehlstd.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.uFehlStd IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.sortierung", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.sortierung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.lernentw", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Lernentw = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.lernentw.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Lernentw IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.gekoppelt", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Gekoppelt = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.gekoppelt.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Gekoppelt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.vorherabgeschl", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.vorherabgeschl.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.VorherAbgeschl IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abschlussjahrgang", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.abschlussjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AbschlussJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.hochrechnungstatus", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.hochrechnungstatus.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.HochrechnungStatus IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.schulnr", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.SchulNr = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.schulnr.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.SchulNr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.zusatzkraft_id", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.zusatzkraft_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.wochenstdzusatzkraft", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.wochenstdzusatzkraft.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.WochenstdZusatzkraft IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.prf10fach", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Prf10Fach = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.prf10fach.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Prf10Fach IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.aufzeugnis", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AufZeugnis = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.aufzeugnis.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.AufZeugnis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.gewichtung", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Gewichtung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.gewichtung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Gewichtung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.noteabschlussba", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.noteabschlussba.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.NoteAbschlussBA IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.umfang", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Umfang = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.umfang.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Umfang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fachlehrer", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Fachlehrer = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.fachlehrer.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Fachlehrer IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.zusatzkraft", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft = :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.zusatzkraft.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.Zusatzkraft IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLeistungsdaten.all.migration", query = "SELECT e FROM MigrationDTOSchuelerLeistungsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Fach_ID", "Hochrechnung", "Fachlehrer_ID", "Kursart", "KursartAllg", "Kurs_ID", "NotenKrz", "NotenKrzQuartal", "Warnung", "Warndatum", "AbiFach", "Wochenstunden", "AbiZeugnis", "Prognose", "FehlStd", "uFehlStd", "Sortierung", "Lernentw", "Gekoppelt", "VorherAbgeschl", "AbschlussJahrgang", "HochrechnungStatus", "SchulNr", "Zusatzkraft_ID", "WochenstdZusatzkraft", "Prf10Fach", "AufZeugnis", "Gewichtung", "SchulnrEigner", "NoteAbschlussBA", "Umfang", "Fachlehrer", "Zusatzkraft"})
public final class MigrationDTOSchuelerLeistungsdaten {

	/** Eine eindeutige ID für die Leistungsdaten des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die eindeutige ID des zugehörigen Lernabschnittes – verweist auf den Lernabschnitt des Schülers */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Die eindeutige ID des zugehörigen Faches – verweist auf das Fach */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Warnung;

	/** gibt das Datum an, wann die Leistung gemahnt wurde */
	@Column(name = "Warndatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gekoppelt;

	/** Gibt an ob das Fach Epochal war oder ist */
	@Column(name = "VorherAbgeschl")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Prf10Fach;

	/** Fach kommt aufs Zeugnnis Ja Nein */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AufZeugnis;

	/** Gewichtung allgemeinbidend BK */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Integer Gewichtung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Beim BK wird hier die Note Berufsabschluss eingetragen. Ist vermutl. nicht mehr notwendig, wenn alle Zeugnisse sich aus dem BKAbschluss-Fächern bedienen */
	@Column(name = "NoteAbschlussBA")
	@JsonProperty
	public String NoteAbschlussBA;

	/** Facheigenschaft für Lernstandsberichte (V voller Umfang) (R reduzierter Umfang) */
	@Column(name = "Umfang")
	@JsonProperty
	public String Umfang;

	/** DEPRECATED: Das Kürzel des zugehörigen Fach-Lehrers */
	@Column(name = "Fachlehrer")
	@JsonProperty
	public String Fachlehrer;

	/** DEPRECATED: Das Kürzel der Zusatzkraft / des Lehrers */
	@Column(name = "Zusatzkraft")
	@JsonProperty
	public String Zusatzkraft;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOSchuelerLeistungsdaten(final Long ID, final Long Abschnitt_ID, final Long Fach_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Fach_ID == null) {
			throw new NullPointerException("Fach_ID must not be null");
		}
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
		MigrationDTOSchuelerLeistungsdaten other = (MigrationDTOSchuelerLeistungsdaten) obj;
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
		return "MigrationDTOSchuelerLeistungsdaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Fach_ID=" + this.Fach_ID + ", Hochrechnung=" + this.Hochrechnung + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Kurs_ID=" + this.Kurs_ID + ", NotenKrz=" + this.NotenKrz + ", NotenKrzQuartal=" + this.NotenKrzQuartal + ", Warnung=" + this.Warnung + ", Warndatum=" + this.Warndatum + ", AbiFach=" + this.AbiFach + ", Wochenstunden=" + this.Wochenstunden + ", AbiZeugnis=" + this.AbiZeugnis + ", Prognose=" + this.Prognose + ", FehlStd=" + this.FehlStd + ", uFehlStd=" + this.uFehlStd + ", Sortierung=" + this.Sortierung + ", Lernentw=" + this.Lernentw + ", Gekoppelt=" + this.Gekoppelt + ", VorherAbgeschl=" + this.VorherAbgeschl + ", AbschlussJahrgang=" + this.AbschlussJahrgang + ", HochrechnungStatus=" + this.HochrechnungStatus + ", SchulNr=" + this.SchulNr + ", Zusatzkraft_ID=" + this.Zusatzkraft_ID + ", WochenstdZusatzkraft=" + this.WochenstdZusatzkraft + ", Prf10Fach=" + this.Prf10Fach + ", AufZeugnis=" + this.AufZeugnis + ", Gewichtung=" + this.Gewichtung + ", SchulnrEigner=" + this.SchulnrEigner + ", NoteAbschlussBA=" + this.NoteAbschlussBA + ", Umfang=" + this.Umfang + ", Fachlehrer=" + this.Fachlehrer + ", Zusatzkraft=" + this.Zusatzkraft + ")";
	}

}
