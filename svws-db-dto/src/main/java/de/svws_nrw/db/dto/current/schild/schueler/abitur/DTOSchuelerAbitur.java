package de.svws_nrw.db.dto.current.schild.schueler.abitur;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.gost.GOStBesondereLernleistungConverter;

import de.svws_nrw.core.types.gost.GostBesondereLernleistung;


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
import de.svws_nrw.csv.converter.current.gost.GOStBesondereLernleistungConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStBesondereLernleistungConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbitur.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbitur")
@JsonPropertyOrder({"ID", "Schueler_ID", "Schuljahresabschnitts_ID", "FacharbeitFach", "FacharbeitNotenpunkte", "FehlstundenSumme", "FehlstundenSummeUnentschuldigt", "-", "-", "-", "-", "-", "-", "-", "ProjektkursThema", "FremdspracheSekIManuellGeprueft", "BlockI_AnzahlKurseEingebracht", "BlockI_AnzahlDefiziteEingebracht", "BlockI_AnzahlDefiziteLK", "BlockI_AnzahlDefizite0Punkte", "BlockI_PunktsummeNormiert", "BlockI_NotenpunktdurchschnittEingebrachterKurse", "BlockI_SummeNotenpunkteGK", "BlockI_SummeNotenpunkteLK", "-", "BlockI_HatZulassung", "BesondereLernleistungArt", "BesondereLernleistungNotenpunkte", "BesondereLernleistungThema", "Pruefung_Punktsumme", "Pruefung_AnzahlDefizite", "Pruefung_AnzahlDefiziteLK", "Pruefung_hatBestanden", "AbiturNote", "AbiturGesamtPunktzahl", "VerbesserungAbPunktzahl", "VerbesserungBenoetigePunkte"})
public final class DTOSchuelerAbitur {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerAbitur e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FacharbeitFach */
	public static final String QUERY_BY_FACHARBEITFACH = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FacharbeitFach */
	public static final String QUERY_LIST_BY_FACHARBEITFACH = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FacharbeitNotenpunkte */
	public static final String QUERY_BY_FACHARBEITNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitNotenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FacharbeitNotenpunkte */
	public static final String QUERY_LIST_BY_FACHARBEITNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FacharbeitNotenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FehlstundenSumme */
	public static final String QUERY_BY_FEHLSTUNDENSUMME = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSumme = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FehlstundenSumme */
	public static final String QUERY_LIST_BY_FEHLSTUNDENSUMME = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSumme IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FehlstundenSummeUnentschuldigt */
	public static final String QUERY_BY_FEHLSTUNDENSUMMEUNENTSCHULDIGT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSummeUnentschuldigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FehlstundenSummeUnentschuldigt */
	public static final String QUERY_LIST_BY_FEHLSTUNDENSUMMEUNENTSCHULDIGT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FehlstundenSummeUnentschuldigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ProjektkursThema */
	public static final String QUERY_BY_PROJEKTKURSTHEMA = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ProjektkursThema = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ProjektkursThema */
	public static final String QUERY_LIST_BY_PROJEKTKURSTHEMA = "SELECT e FROM DTOSchuelerAbitur e WHERE e.ProjektkursThema IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FremdspracheSekIManuellGeprueft */
	public static final String QUERY_BY_FREMDSPRACHESEKIMANUELLGEPRUEFT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FremdspracheSekIManuellGeprueft = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FremdspracheSekIManuellGeprueft */
	public static final String QUERY_LIST_BY_FREMDSPRACHESEKIMANUELLGEPRUEFT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.FremdspracheSekIManuellGeprueft IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_AnzahlKurseEingebracht */
	public static final String QUERY_BY_BLOCKI_ANZAHLKURSEEINGEBRACHT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlKurseEingebracht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_AnzahlKurseEingebracht */
	public static final String QUERY_LIST_BY_BLOCKI_ANZAHLKURSEEINGEBRACHT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlKurseEingebracht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_AnzahlDefiziteEingebracht */
	public static final String QUERY_BY_BLOCKI_ANZAHLDEFIZITEEINGEBRACHT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteEingebracht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_AnzahlDefiziteEingebracht */
	public static final String QUERY_LIST_BY_BLOCKI_ANZAHLDEFIZITEEINGEBRACHT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteEingebracht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_AnzahlDefiziteLK */
	public static final String QUERY_BY_BLOCKI_ANZAHLDEFIZITELK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_AnzahlDefiziteLK */
	public static final String QUERY_LIST_BY_BLOCKI_ANZAHLDEFIZITELK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefiziteLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_AnzahlDefizite0Punkte */
	public static final String QUERY_BY_BLOCKI_ANZAHLDEFIZITE0PUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefizite0Punkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_AnzahlDefizite0Punkte */
	public static final String QUERY_LIST_BY_BLOCKI_ANZAHLDEFIZITE0PUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_AnzahlDefizite0Punkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_PunktsummeNormiert */
	public static final String QUERY_BY_BLOCKI_PUNKTSUMMENORMIERT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_PunktsummeNormiert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_PunktsummeNormiert */
	public static final String QUERY_LIST_BY_BLOCKI_PUNKTSUMMENORMIERT = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_PunktsummeNormiert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_NotenpunktdurchschnittEingebrachterKurse */
	public static final String QUERY_BY_BLOCKI_NOTENPUNKTDURCHSCHNITTEINGEBRACHTERKURSE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_NotenpunktdurchschnittEingebrachterKurse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_NotenpunktdurchschnittEingebrachterKurse */
	public static final String QUERY_LIST_BY_BLOCKI_NOTENPUNKTDURCHSCHNITTEINGEBRACHTERKURSE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_NotenpunktdurchschnittEingebrachterKurse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_SummeNotenpunkteGK */
	public static final String QUERY_BY_BLOCKI_SUMMENOTENPUNKTEGK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteGK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_SummeNotenpunkteGK */
	public static final String QUERY_LIST_BY_BLOCKI_SUMMENOTENPUNKTEGK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteGK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_SummeNotenpunkteLK */
	public static final String QUERY_BY_BLOCKI_SUMMENOTENPUNKTELK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_SummeNotenpunkteLK */
	public static final String QUERY_LIST_BY_BLOCKI_SUMMENOTENPUNKTELK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_SummeNotenpunkteLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BlockI_HatZulassung */
	public static final String QUERY_BY_BLOCKI_HATZULASSUNG = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_HatZulassung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BlockI_HatZulassung */
	public static final String QUERY_LIST_BY_BLOCKI_HATZULASSUNG = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BlockI_HatZulassung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BesondereLernleistungArt */
	public static final String QUERY_BY_BESONDERELERNLEISTUNGART = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BesondereLernleistungArt */
	public static final String QUERY_LIST_BY_BESONDERELERNLEISTUNGART = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BesondereLernleistungNotenpunkte */
	public static final String QUERY_BY_BESONDERELERNLEISTUNGNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungNotenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BesondereLernleistungNotenpunkte */
	public static final String QUERY_LIST_BY_BESONDERELERNLEISTUNGNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungNotenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BesondereLernleistungThema */
	public static final String QUERY_BY_BESONDERELERNLEISTUNGTHEMA = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungThema = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BesondereLernleistungThema */
	public static final String QUERY_LIST_BY_BESONDERELERNLEISTUNGTHEMA = "SELECT e FROM DTOSchuelerAbitur e WHERE e.BesondereLernleistungThema IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Pruefung_Punktsumme */
	public static final String QUERY_BY_PRUEFUNG_PUNKTSUMME = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_Punktsumme = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Pruefung_Punktsumme */
	public static final String QUERY_LIST_BY_PRUEFUNG_PUNKTSUMME = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_Punktsumme IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Pruefung_AnzahlDefizite */
	public static final String QUERY_BY_PRUEFUNG_ANZAHLDEFIZITE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefizite = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Pruefung_AnzahlDefizite */
	public static final String QUERY_LIST_BY_PRUEFUNG_ANZAHLDEFIZITE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefizite IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Pruefung_AnzahlDefiziteLK */
	public static final String QUERY_BY_PRUEFUNG_ANZAHLDEFIZITELK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefiziteLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Pruefung_AnzahlDefiziteLK */
	public static final String QUERY_LIST_BY_PRUEFUNG_ANZAHLDEFIZITELK = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_AnzahlDefiziteLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Pruefung_hatBestanden */
	public static final String QUERY_BY_PRUEFUNG_HATBESTANDEN = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_hatBestanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Pruefung_hatBestanden */
	public static final String QUERY_LIST_BY_PRUEFUNG_HATBESTANDEN = "SELECT e FROM DTOSchuelerAbitur e WHERE e.Pruefung_hatBestanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiturNote */
	public static final String QUERY_BY_ABITURNOTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturNote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiturNote */
	public static final String QUERY_LIST_BY_ABITURNOTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturNote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiturGesamtPunktzahl */
	public static final String QUERY_BY_ABITURGESAMTPUNKTZAHL = "SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturGesamtPunktzahl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiturGesamtPunktzahl */
	public static final String QUERY_LIST_BY_ABITURGESAMTPUNKTZAHL = "SELECT e FROM DTOSchuelerAbitur e WHERE e.AbiturGesamtPunktzahl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VerbesserungAbPunktzahl */
	public static final String QUERY_BY_VERBESSERUNGABPUNKTZAHL = "SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungAbPunktzahl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VerbesserungAbPunktzahl */
	public static final String QUERY_LIST_BY_VERBESSERUNGABPUNKTZAHL = "SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungAbPunktzahl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VerbesserungBenoetigePunkte */
	public static final String QUERY_BY_VERBESSERUNGBENOETIGEPUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungBenoetigePunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VerbesserungBenoetigePunkte */
	public static final String QUERY_LIST_BY_VERBESSERUNGBENOETIGEPUNKTE = "SELECT e FROM DTOSchuelerAbitur e WHERE e.VerbesserungBenoetigePunkte IN ?1";

	/** Eine eindeutige ID für den Abiturdatensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** BK: Facharbeit – Fach */
	@Column(name = "FA_Fach")
	@JsonProperty
	public String FacharbeitFach;

	/** BK: Facharbeit – Notenpunkte der Facharbeit */
	@Column(name = "FA_Punkte")
	@JsonProperty
	public Integer FacharbeitNotenpunkte;

	/** Die Summe der Fehlstunden – sofern berechnet */
	@Column(name = "FehlStd")
	@JsonProperty
	public Integer FehlstundenSumme;

	/** Die Summe der unentschuldigten Fehlstunden – sofern berechnet */
	@Column(name = "uFehlStd")
	@JsonProperty
	public Integer FehlstundenSummeUnentschuldigt;

	/** Projektkurs: Das Thema des Projektkurses */
	@Column(name = "Thema_PJK")
	@JsonProperty
	public String ProjektkursThema;

	/** true, falls die zweite Fremsprache in der Sekundarstufe 1 manuell geprüft wurde und vom Algorithmus als gegeben angesehen werden kann. */
	@Column(name = "FS2_SekI_Manuell")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FremdspracheSekIManuellGeprueft;

	/** Block I: Die Anzahl der eingebrachten Kurse aus der Qualifikationsphase */
	@Column(name = "Kurse_I")
	@JsonProperty
	public Integer BlockI_AnzahlKurseEingebracht;

	/** Block I: Die Gesamtanzahl der eingebrachten Defizite aus der Qualifikationsphase */
	@Column(name = "Defizite_I")
	@JsonProperty
	public Integer BlockI_AnzahlDefiziteEingebracht;

	/** Block I: Die Anzahl der Defizite im LK-Bereich aus der Qualifikationsphase */
	@Column(name = "LK_Defizite_I")
	@JsonProperty
	public Integer BlockI_AnzahlDefiziteLK;

	/** Block I: Die Anzahl der Defizite mit 0 Punkten */
	@Column(name = "AnzahlKurse_0")
	@JsonProperty
	public Integer BlockI_AnzahlDefizite0Punkte;

	/** Block I: Die normierte Punktesumme aus der Qualifikationsphase */
	@Column(name = "Punktsumme_I")
	@JsonProperty
	public Integer BlockI_PunktsummeNormiert;

	/** Block I: Der Notenpunktdurchschnitt aus den eingebrachten Kurse der Qualifikationsphase */
	@Column(name = "Durchschnitt_I")
	@JsonProperty
	public Double BlockI_NotenpunktdurchschnittEingebrachterKurse;

	/** Block I: Die Summe der erreichten Notepunkte aus dem Grundkurs-Bereich */
	@Column(name = "SummeGK")
	@JsonProperty
	public Integer BlockI_SummeNotenpunkteGK;

	/** Block I: Die Summe der erreichten Notepunkte aus dem Leistungskurs-Bereich */
	@Column(name = "SummeLK")
	@JsonProperty
	public Integer BlockI_SummeNotenpunkteLK;

	/** Block I: Gibt an, ob der Schüler zu den Abiturprüfungen zugelassen ist, d.h. die Bedingungen für Block I erfüllt hat (+: Ja, -: Nein, R: Freiwilliger Rücktritt) */
	@Column(name = "Zugelassen")
	@JsonProperty
	public String BlockI_HatZulassung;

	/** Besondere Lernleistung: Die Art der besonderen Lernleistung(K: Keine, E: Externe, P: in einem Projektkurs) */
	@Column(name = "BLL_Art")
	@JsonProperty
	@Convert(converter = GOStBesondereLernleistungConverter.class)
	@JsonSerialize(using = GOStBesondereLernleistungConverterSerializer.class)
	@JsonDeserialize(using = GOStBesondereLernleistungConverterDeserializer.class)
	public GostBesondereLernleistung BesondereLernleistungArt;

	/** Besondere Lernleistung: Die Notenpunkte, welche bei der besonderen Lernleistung erreicht wurden – einfach gewichtet */
	@Column(name = "BLL_Punkte")
	@JsonProperty
	public Integer BesondereLernleistungNotenpunkte;

	/** Besondere Lernleistung: Das Thema der besonderen Lernleistung */
	@Column(name = "Thema_BLL")
	@JsonProperty
	public String BesondereLernleistungThema;

	/** Block II: Die Punktsumme aus den Abiturprüfungen, NULL falls noch nicht berechnet */
	@Column(name = "Punktsumme_II")
	@JsonProperty
	public Integer Pruefung_Punktsumme;

	/** Block II: Die Anzahl der Defizite bei den Abiturprüfungen, NULL falls noch nicht berechnet */
	@Column(name = "Defizite_II")
	@JsonProperty
	public Integer Pruefung_AnzahlDefizite;

	/** Block II: Die Anzahl der LK-Defizite bei den Abiturprüfungen, NULL falls noch nicht berechnet */
	@Column(name = "LK_Defizite_II")
	@JsonProperty
	public Integer Pruefung_AnzahlDefiziteLK;

	/** Gesamt: Gibt an, ob die Abiturprüfung bestanden wurde */
	@Column(name = "PruefungBestanden")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Pruefung_hatBestanden;

	/** Gesamt: Die Abschlussnote des Abiturs */
	@Column(name = "Note")
	@JsonProperty
	public String AbiturNote;

	/** Gesamt: Die Gesamtpunktzahl aus beiden Blöcken (Qualifikationsphase und Abiturprüfung) */
	@Column(name = "GesamtPunktzahl")
	@JsonProperty
	public Integer AbiturGesamtPunktzahl;

	/** Gesamt: Die Gesamtpunktzahl, ab der eine höhere Notenstufe erreicht würde */
	@Column(name = "Notensprung")
	@JsonProperty
	public Integer VerbesserungAbPunktzahl;

	/** Gesamt: Die Anzahl an Punkten, die zur nächsthöheren Note fehlt */
	@Column(name = "FehlendePunktzahl")
	@JsonProperty
	public Integer VerbesserungBenoetigePunkte;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbitur ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAbitur() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbitur ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerAbitur(final long ID, final long Schueler_ID) {
		this.ID = ID;
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
		DTOSchuelerAbitur other = (DTOSchuelerAbitur) obj;
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
		return "DTOSchuelerAbitur(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", FacharbeitFach=" + this.FacharbeitFach + ", FacharbeitNotenpunkte=" + this.FacharbeitNotenpunkte + ", FehlstundenSumme=" + this.FehlstundenSumme + ", FehlstundenSummeUnentschuldigt=" + this.FehlstundenSummeUnentschuldigt + ", ProjektkursThema=" + this.ProjektkursThema + ", FremdspracheSekIManuellGeprueft=" + this.FremdspracheSekIManuellGeprueft + ", BlockI_AnzahlKurseEingebracht=" + this.BlockI_AnzahlKurseEingebracht + ", BlockI_AnzahlDefiziteEingebracht=" + this.BlockI_AnzahlDefiziteEingebracht + ", BlockI_AnzahlDefiziteLK=" + this.BlockI_AnzahlDefiziteLK + ", BlockI_AnzahlDefizite0Punkte=" + this.BlockI_AnzahlDefizite0Punkte + ", BlockI_PunktsummeNormiert=" + this.BlockI_PunktsummeNormiert + ", BlockI_NotenpunktdurchschnittEingebrachterKurse=" + this.BlockI_NotenpunktdurchschnittEingebrachterKurse + ", BlockI_SummeNotenpunkteGK=" + this.BlockI_SummeNotenpunkteGK + ", BlockI_SummeNotenpunkteLK=" + this.BlockI_SummeNotenpunkteLK + ", BlockI_HatZulassung=" + this.BlockI_HatZulassung + ", BesondereLernleistungArt=" + this.BesondereLernleistungArt + ", BesondereLernleistungNotenpunkte=" + this.BesondereLernleistungNotenpunkte + ", BesondereLernleistungThema=" + this.BesondereLernleistungThema + ", Pruefung_Punktsumme=" + this.Pruefung_Punktsumme + ", Pruefung_AnzahlDefizite=" + this.Pruefung_AnzahlDefizite + ", Pruefung_AnzahlDefiziteLK=" + this.Pruefung_AnzahlDefiziteLK + ", Pruefung_hatBestanden=" + this.Pruefung_hatBestanden + ", AbiturNote=" + this.AbiturNote + ", AbiturGesamtPunktzahl=" + this.AbiturGesamtPunktzahl + ", VerbesserungAbPunktzahl=" + this.VerbesserungAbPunktzahl + ", VerbesserungBenoetigePunkte=" + this.VerbesserungBenoetigePunkte + ")";
	}

}
