package de.svws_nrw.db.dto.current.schild.schueler.abitur;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.gost.AbiturBelegungsartConverter;
import de.svws_nrw.db.converter.current.gost.AbiturKursMarkierungConverter;
import de.svws_nrw.db.converter.current.gost.GOStAbiturFachConverter;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;

import de.svws_nrw.core.data.gost.AbiturKursMarkierung;
import de.svws_nrw.core.types.gost.AbiturBelegungsart;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostKursart;


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
import de.svws_nrw.csv.converter.current.gost.AbiturBelegungsartConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.AbiturBelegungsartConverterDeserializer;
import de.svws_nrw.csv.converter.current.gost.AbiturKursMarkierungConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.AbiturKursMarkierungConverterDeserializer;
import de.svws_nrw.csv.converter.current.gost.GOStAbiturFachConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStAbiturFachConverterDeserializer;
import de.svws_nrw.csv.converter.current.gost.GOStKursartConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStKursartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbiFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbiFaecher")
@JsonPropertyOrder({"ID", "Schueler_ID", "Fach_ID", "FachKuerzel", "FachSortierung", "Kurs_ID", "KursartAllgemein", "Fachlehrer_ID", "AbiturFach", "EF_HJ1_Notenpunkte", "EF_HJ1_BelegungArt", "EF_HJ2_Notenpunkte", "EF_HJ2_BelegungArt", "Facharbeit_Notenpunkte", "Facharbeit_MarkiertFuerAbiturBerechnung", "Q1_HJ1_Wochenstunden", "Q1_HJ1_Notenpunkte", "-", "Q1_HJ1_MarkiertFuerAbiturBerechnung", "Q1_HJ1_BelegungArt", "Q1_HJ2_Wochenstunden", "Q1_HJ2_Notenpunkte", "-", "Q1_HJ2_MarkiertFuerAbiturBerechnung", "Q1_HJ2_BelegungArt", "Q2_HJ1_Wochenstunden", "Q2_HJ1_Notenpunkte", "-", "Q2_HJ1_MarkiertFuerAbiturBerechnung", "Q2_HJ1_BelegungArt", "Q2_HJ2_Wochenstunden", "Q2_HJ2_Notenpunkte", "-", "Q2_HJ2_MarkiertFuerAbiturBerechnung", "Q2_HJ2_BelegungArt", "ZulassungPunktsumme", "ZulassungNotenpunktdurchschnitt", "PruefungNotenpunkte", "PruefungPunktsummeZwischenstand", "PruefungMuendlichAbweichung", "PruefungMuendlichBestehen", "PruefungMuendlichFreiwillig", "PruefungMuendlichNotenpunkte", "PruefungMuendlichReihenfolge", "PruefungPunktsummeGesamt"})
public final class DTOSchuelerAbiturFach {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerAbiturFach e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachKuerzel */
	public static final String QUERY_BY_FACHKUERZEL = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.FachKuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachKuerzel */
	public static final String QUERY_LIST_BY_FACHKUERZEL = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.FachKuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachSortierung */
	public static final String QUERY_BY_FACHSORTIERUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.FachSortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachSortierung */
	public static final String QUERY_LIST_BY_FACHSORTIERUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.FachSortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurs_ID */
	public static final String QUERY_BY_KURS_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurs_ID */
	public static final String QUERY_LIST_BY_KURS_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllgemein */
	public static final String QUERY_BY_KURSARTALLGEMEIN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.KursartAllgemein = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllgemein */
	public static final String QUERY_LIST_BY_KURSARTALLGEMEIN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.KursartAllgemein IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachlehrer_ID */
	public static final String QUERY_BY_FACHLEHRER_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fachlehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachlehrer_ID */
	public static final String QUERY_LIST_BY_FACHLEHRER_ID = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fachlehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiturFach */
	public static final String QUERY_BY_ABITURFACH = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.AbiturFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiturFach */
	public static final String QUERY_LIST_BY_ABITURFACH = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.AbiturFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF_HJ1_Notenpunkte */
	public static final String QUERY_BY_EF_HJ1_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF_HJ1_Notenpunkte */
	public static final String QUERY_LIST_BY_EF_HJ1_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF_HJ1_BelegungArt */
	public static final String QUERY_BY_EF_HJ1_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_BelegungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF_HJ1_BelegungArt */
	public static final String QUERY_LIST_BY_EF_HJ1_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_BelegungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF_HJ2_Notenpunkte */
	public static final String QUERY_BY_EF_HJ2_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF_HJ2_Notenpunkte */
	public static final String QUERY_LIST_BY_EF_HJ2_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF_HJ2_BelegungArt */
	public static final String QUERY_BY_EF_HJ2_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_BelegungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF_HJ2_BelegungArt */
	public static final String QUERY_LIST_BY_EF_HJ2_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_BelegungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Facharbeit_Notenpunkte */
	public static final String QUERY_BY_FACHARBEIT_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Facharbeit_Notenpunkte */
	public static final String QUERY_LIST_BY_FACHARBEIT_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Facharbeit_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_BY_FACHARBEIT_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_MarkiertFuerAbiturBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Facharbeit_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_LIST_BY_FACHARBEIT_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_MarkiertFuerAbiturBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ1_Wochenstunden */
	public static final String QUERY_BY_Q1_HJ1_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ1_Wochenstunden */
	public static final String QUERY_LIST_BY_Q1_HJ1_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ1_Notenpunkte */
	public static final String QUERY_BY_Q1_HJ1_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ1_Notenpunkte */
	public static final String QUERY_LIST_BY_Q1_HJ1_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ1_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_BY_Q1_HJ1_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_MarkiertFuerAbiturBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ1_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_LIST_BY_Q1_HJ1_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_MarkiertFuerAbiturBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ1_BelegungArt */
	public static final String QUERY_BY_Q1_HJ1_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_BelegungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ1_BelegungArt */
	public static final String QUERY_LIST_BY_Q1_HJ1_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_BelegungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ2_Wochenstunden */
	public static final String QUERY_BY_Q1_HJ2_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ2_Wochenstunden */
	public static final String QUERY_LIST_BY_Q1_HJ2_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ2_Notenpunkte */
	public static final String QUERY_BY_Q1_HJ2_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ2_Notenpunkte */
	public static final String QUERY_LIST_BY_Q1_HJ2_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ2_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_BY_Q1_HJ2_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_MarkiertFuerAbiturBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ2_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_LIST_BY_Q1_HJ2_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_MarkiertFuerAbiturBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q1_HJ2_BelegungArt */
	public static final String QUERY_BY_Q1_HJ2_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_BelegungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q1_HJ2_BelegungArt */
	public static final String QUERY_LIST_BY_Q1_HJ2_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_BelegungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ1_Wochenstunden */
	public static final String QUERY_BY_Q2_HJ1_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ1_Wochenstunden */
	public static final String QUERY_LIST_BY_Q2_HJ1_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ1_Notenpunkte */
	public static final String QUERY_BY_Q2_HJ1_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ1_Notenpunkte */
	public static final String QUERY_LIST_BY_Q2_HJ1_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ1_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_BY_Q2_HJ1_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_MarkiertFuerAbiturBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ1_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_LIST_BY_Q2_HJ1_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_MarkiertFuerAbiturBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ1_BelegungArt */
	public static final String QUERY_BY_Q2_HJ1_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_BelegungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ1_BelegungArt */
	public static final String QUERY_LIST_BY_Q2_HJ1_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_BelegungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ2_Wochenstunden */
	public static final String QUERY_BY_Q2_HJ2_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ2_Wochenstunden */
	public static final String QUERY_LIST_BY_Q2_HJ2_WOCHENSTUNDEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ2_Notenpunkte */
	public static final String QUERY_BY_Q2_HJ2_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ2_Notenpunkte */
	public static final String QUERY_LIST_BY_Q2_HJ2_NOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ2_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_BY_Q2_HJ2_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_MarkiertFuerAbiturBerechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ2_MarkiertFuerAbiturBerechnung */
	public static final String QUERY_LIST_BY_Q2_HJ2_MARKIERTFUERABITURBERECHNUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_MarkiertFuerAbiturBerechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q2_HJ2_BelegungArt */
	public static final String QUERY_BY_Q2_HJ2_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_BelegungArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q2_HJ2_BelegungArt */
	public static final String QUERY_LIST_BY_Q2_HJ2_BELEGUNGART = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_BelegungArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungPunktsumme */
	public static final String QUERY_BY_ZULASSUNGPUNKTSUMME = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungPunktsumme = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungPunktsumme */
	public static final String QUERY_LIST_BY_ZULASSUNGPUNKTSUMME = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungPunktsumme IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungNotenpunktdurchschnitt */
	public static final String QUERY_BY_ZULASSUNGNOTENPUNKTDURCHSCHNITT = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungNotenpunktdurchschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungNotenpunktdurchschnitt */
	public static final String QUERY_LIST_BY_ZULASSUNGNOTENPUNKTDURCHSCHNITT = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungNotenpunktdurchschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungNotenpunkte */
	public static final String QUERY_BY_PRUEFUNGNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungNotenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungNotenpunkte */
	public static final String QUERY_LIST_BY_PRUEFUNGNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungNotenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungPunktsummeZwischenstand */
	public static final String QUERY_BY_PRUEFUNGPUNKTSUMMEZWISCHENSTAND = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeZwischenstand = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungPunktsummeZwischenstand */
	public static final String QUERY_LIST_BY_PRUEFUNGPUNKTSUMMEZWISCHENSTAND = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeZwischenstand IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungMuendlichAbweichung */
	public static final String QUERY_BY_PRUEFUNGMUENDLICHABWEICHUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichAbweichung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungMuendlichAbweichung */
	public static final String QUERY_LIST_BY_PRUEFUNGMUENDLICHABWEICHUNG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichAbweichung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungMuendlichBestehen */
	public static final String QUERY_BY_PRUEFUNGMUENDLICHBESTEHEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichBestehen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungMuendlichBestehen */
	public static final String QUERY_LIST_BY_PRUEFUNGMUENDLICHBESTEHEN = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichBestehen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungMuendlichFreiwillig */
	public static final String QUERY_BY_PRUEFUNGMUENDLICHFREIWILLIG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichFreiwillig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungMuendlichFreiwillig */
	public static final String QUERY_LIST_BY_PRUEFUNGMUENDLICHFREIWILLIG = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichFreiwillig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungMuendlichNotenpunkte */
	public static final String QUERY_BY_PRUEFUNGMUENDLICHNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichNotenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungMuendlichNotenpunkte */
	public static final String QUERY_LIST_BY_PRUEFUNGMUENDLICHNOTENPUNKTE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichNotenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungMuendlichReihenfolge */
	public static final String QUERY_BY_PRUEFUNGMUENDLICHREIHENFOLGE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichReihenfolge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungMuendlichReihenfolge */
	public static final String QUERY_LIST_BY_PRUEFUNGMUENDLICHREIHENFOLGE = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichReihenfolge IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefungPunktsummeGesamt */
	public static final String QUERY_BY_PRUEFUNGPUNKTSUMMEGESAMT = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeGesamt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefungPunktsummeGesamt */
	public static final String QUERY_LIST_BY_PRUEFUNGPUNKTSUMMEGESAMT = "SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeGesamt IN ?1";

	/** Eine eindeutige ID für die Abitur-Fach-Informationen des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Die eindeutige ID des Faches – verweist auf das Fach */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Das Kürzel des Faches */
	@Column(name = "FachKrz")
	@JsonProperty
	public String FachKuerzel;

	/** Deprecated: Eine Zahl, welche die Sortierung der Fächer angibt */
	@Column(name = "FSortierung")
	@JsonProperty
	public Integer FachSortierung;

	/** Letzer Kurs Q2, 2.Hj: Die Kurs ID - verweist auf Kurse */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Die allgemeine Kursart des Faches (z.B. GK, LK) */
	@Column(name = "KursartAllg")
	@JsonProperty
	@Convert(converter = GOStKursartConverter.class)
	@JsonSerialize(using = GOStKursartConverterSerializer.class)
	@JsonDeserialize(using = GOStKursartConverterDeserializer.class)
	public GostKursart KursartAllgemein;

	/** Letzer Kurs Q2, 2.Hj: Die ID des Fachlehrers, der als letztes in diesem Fach unterrichtet hat */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/** Gibt an, ob das Fach bei der Abiturprüfung gewählt wurde und dann die Nummer des Abiturfaches (1-4) */
	@Column(name = "AbiFach")
	@JsonProperty
	@Convert(converter = GOStAbiturFachConverter.class)
	@JsonSerialize(using = GOStAbiturFachConverterSerializer.class)
	@JsonDeserialize(using = GOStAbiturFachConverterDeserializer.class)
	public GostAbiturFach AbiturFach;

	/** EF, 1. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P11_1")
	@JsonProperty
	public String EF_HJ1_Notenpunkte;

	/** EF, 1. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich) */
	@Column(name = "S11_1")
	@JsonProperty
	@Convert(converter = AbiturBelegungsartConverter.class)
	@JsonSerialize(using = AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using = AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart EF_HJ1_BelegungArt;

	/** EF, 2. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P11_2")
	@JsonProperty
	public String EF_HJ2_Notenpunkte;

	/** EF, 2. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich) */
	@Column(name = "S11_2")
	@JsonProperty
	@Convert(converter = AbiturBelegungsartConverter.class)
	@JsonSerialize(using = AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using = AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart EF_HJ2_BelegungArt;

	/** BK: eingebrachte Facharbeit – Notenpunkte */
	@Column(name = "P_FA")
	@JsonProperty
	public String Facharbeit_Notenpunkte;

	/** BK: eingebrachte Facharbeit – Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R_FA")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Facharbeit_MarkiertFuerAbiturBerechnung;

	/** Q1, 1. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W12_1")
	@JsonProperty
	public Integer Q1_HJ1_Wochenstunden;

	/** Q1, 1. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P12_1")
	@JsonProperty
	public String Q1_HJ1_Notenpunkte;

	/** Q1, 1. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R12_1")
	@JsonProperty
	@Convert(converter = AbiturKursMarkierungConverter.class)
	@JsonSerialize(using = AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using = AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q1_HJ1_MarkiertFuerAbiturBerechnung;

	/** Q1, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S12_1")
	@JsonProperty
	@Convert(converter = AbiturBelegungsartConverter.class)
	@JsonSerialize(using = AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using = AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q1_HJ1_BelegungArt;

	/** Q1, 2. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W12_2")
	@JsonProperty
	public Integer Q1_HJ2_Wochenstunden;

	/** Q1, 2. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P12_2")
	@JsonProperty
	public String Q1_HJ2_Notenpunkte;

	/** Q1, 2. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R12_2")
	@JsonProperty
	@Convert(converter = AbiturKursMarkierungConverter.class)
	@JsonSerialize(using = AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using = AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q1_HJ2_MarkiertFuerAbiturBerechnung;

	/** Q1, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S12_2")
	@JsonProperty
	@Convert(converter = AbiturBelegungsartConverter.class)
	@JsonSerialize(using = AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using = AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q1_HJ2_BelegungArt;

	/** Q2, 1. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W13_1")
	@JsonProperty
	public Integer Q2_HJ1_Wochenstunden;

	/** Q2, 1. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P13_1")
	@JsonProperty
	public String Q2_HJ1_Notenpunkte;

	/** Q2, 1. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R13_1")
	@JsonProperty
	@Convert(converter = AbiturKursMarkierungConverter.class)
	@JsonSerialize(using = AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using = AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q2_HJ1_MarkiertFuerAbiturBerechnung;

	/** Q2, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S13_1")
	@JsonProperty
	@Convert(converter = AbiturBelegungsartConverter.class)
	@JsonSerialize(using = AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using = AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q2_HJ1_BelegungArt;

	/** Q2,21. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W13_2")
	@JsonProperty
	public Integer Q2_HJ2_Wochenstunden;

	/** Q2, 2. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P13_2")
	@JsonProperty
	public String Q2_HJ2_Notenpunkte;

	/** Q2, 2. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R13_2")
	@JsonProperty
	@Convert(converter = AbiturKursMarkierungConverter.class)
	@JsonSerialize(using = AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using = AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q2_HJ2_MarkiertFuerAbiturBerechnung;

	/** Q2, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S13_2")
	@JsonProperty
	@Convert(converter = AbiturBelegungsartConverter.class)
	@JsonSerialize(using = AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using = AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q2_HJ2_BelegungArt;

	/** Die Punkte für das Fach für die Abiturzulassung, NULL falls kein Kurs des Faches eingeht */
	@Column(name = "Zulassung")
	@JsonProperty
	public Integer ZulassungPunktsumme;

	/** Der Notendurchschnitt, falls das Fach eines der ersten drei Abiturfächer ist */
	@Column(name = "Durchschnitt")
	@JsonProperty
	public Double ZulassungNotenpunktdurchschnitt;

	/** Die Notenpunkte aus der Abiturprüfung, falls das Fach eines der vier Abiturfächer ist */
	@Column(name = "AbiPruefErgebnis")
	@JsonProperty
	public Integer PruefungNotenpunkte;

	/** Die Notenpunkte aus der Abiturprüfung multipliziert mit dem entsprechenden Faktor, falls das Fach eines der vier Abiturfächer ist (hier wird z.B. die besondere Lernleistung berücksichtigt */
	@Column(name = "Zwischenstand")
	@JsonProperty
	public Integer PruefungPunktsummeZwischenstand;

	/** true, falls eine mündliche Abweichungsprüfung in einem der ersten drei Abiturfächer nötig ist. */
	@Column(name = "MdlPflichtPruefung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichAbweichung;

	/** true, falls eine mündliche Bestehensprüfung in den ersten drei Abiturfächern notwendig ist, damit das Abitur noch bestanden werden kann. */
	@Column(name = "MdlBestPruefung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichBestehen;

	/** true, falls eine freiwillige Prüfung in einem der ersten drei Abiturfächer stattfindet. */
	@Column(name = "MdlFreiwPruefung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichFreiwillig;

	/** enthält die Notenpunkte aus der mündlichen Abiturprüfung in einem der ersten drei Abiturfächer, falls diese durchgeführt wird, ansonsten NULL */
	@Column(name = "MdlPruefErgebnis")
	@JsonProperty
	public Integer PruefungMuendlichNotenpunkte;

	/** enthält die Reihenfolge für mündliche Prüfungen in den ersten drei Abiturfächern, falls mehrere angesetzt werden (1, 2, 3) */
	@Column(name = "MdlPruefFolge")
	@JsonProperty
	public Integer PruefungMuendlichReihenfolge;

	/** Die Notenpunkte aus der Abiturprüfung multipliziert mit dem entsprechenden Faktor (hier wird z.B. die besondere Lernleistung) und unter Einbeziehung einer möglichen mündlicher Prüfung */
	@Column(name = "AbiErgebnis")
	@JsonProperty
	public Integer PruefungPunktsummeGesamt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbiturFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerAbiturFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerAbiturFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerAbiturFach(final long ID, final long Schueler_ID, final long Fach_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
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
		DTOSchuelerAbiturFach other = (DTOSchuelerAbiturFach) obj;
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
		return "DTOSchuelerAbiturFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", FachKuerzel=" + this.FachKuerzel + ", FachSortierung=" + this.FachSortierung + ", Kurs_ID=" + this.Kurs_ID + ", KursartAllgemein=" + this.KursartAllgemein + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", AbiturFach=" + this.AbiturFach + ", EF_HJ1_Notenpunkte=" + this.EF_HJ1_Notenpunkte + ", EF_HJ1_BelegungArt=" + this.EF_HJ1_BelegungArt + ", EF_HJ2_Notenpunkte=" + this.EF_HJ2_Notenpunkte + ", EF_HJ2_BelegungArt=" + this.EF_HJ2_BelegungArt + ", Facharbeit_Notenpunkte=" + this.Facharbeit_Notenpunkte + ", Facharbeit_MarkiertFuerAbiturBerechnung=" + this.Facharbeit_MarkiertFuerAbiturBerechnung + ", Q1_HJ1_Wochenstunden=" + this.Q1_HJ1_Wochenstunden + ", Q1_HJ1_Notenpunkte=" + this.Q1_HJ1_Notenpunkte + ", Q1_HJ1_MarkiertFuerAbiturBerechnung=" + this.Q1_HJ1_MarkiertFuerAbiturBerechnung + ", Q1_HJ1_BelegungArt=" + this.Q1_HJ1_BelegungArt + ", Q1_HJ2_Wochenstunden=" + this.Q1_HJ2_Wochenstunden + ", Q1_HJ2_Notenpunkte=" + this.Q1_HJ2_Notenpunkte + ", Q1_HJ2_MarkiertFuerAbiturBerechnung=" + this.Q1_HJ2_MarkiertFuerAbiturBerechnung + ", Q1_HJ2_BelegungArt=" + this.Q1_HJ2_BelegungArt + ", Q2_HJ1_Wochenstunden=" + this.Q2_HJ1_Wochenstunden + ", Q2_HJ1_Notenpunkte=" + this.Q2_HJ1_Notenpunkte + ", Q2_HJ1_MarkiertFuerAbiturBerechnung=" + this.Q2_HJ1_MarkiertFuerAbiturBerechnung + ", Q2_HJ1_BelegungArt=" + this.Q2_HJ1_BelegungArt + ", Q2_HJ2_Wochenstunden=" + this.Q2_HJ2_Wochenstunden + ", Q2_HJ2_Notenpunkte=" + this.Q2_HJ2_Notenpunkte + ", Q2_HJ2_MarkiertFuerAbiturBerechnung=" + this.Q2_HJ2_MarkiertFuerAbiturBerechnung + ", Q2_HJ2_BelegungArt=" + this.Q2_HJ2_BelegungArt + ", ZulassungPunktsumme=" + this.ZulassungPunktsumme + ", ZulassungNotenpunktdurchschnitt=" + this.ZulassungNotenpunktdurchschnitt + ", PruefungNotenpunkte=" + this.PruefungNotenpunkte + ", PruefungPunktsummeZwischenstand=" + this.PruefungPunktsummeZwischenstand + ", PruefungMuendlichAbweichung=" + this.PruefungMuendlichAbweichung + ", PruefungMuendlichBestehen=" + this.PruefungMuendlichBestehen + ", PruefungMuendlichFreiwillig=" + this.PruefungMuendlichFreiwillig + ", PruefungMuendlichNotenpunkte=" + this.PruefungMuendlichNotenpunkte + ", PruefungMuendlichReihenfolge=" + this.PruefungMuendlichReihenfolge + ", PruefungPunktsummeGesamt=" + this.PruefungPunktsummeGesamt + ")";
	}

}
