package de.svws_nrw.db.dto.migration.schild.schueler.abitur;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbiFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbiFaecher")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.all", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.id", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fach_id", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fachkuerzel", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.FachKuerzel = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fachkuerzel.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.FachKuerzel IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.kurs_id", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Kurs_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.kurs_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Kurs_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.kursartallgemein", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.KursartAllgemein = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.kursartallgemein.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.KursartAllgemein IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fachlehrer_id", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fachlehrer_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.abiturfach", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.AbiturFach = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.abiturfach.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.AbiturFach IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj1_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ1_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj1_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ1_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj1_belegungart", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ1_BelegungArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj1_belegungart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ1_BelegungArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj2_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ2_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj2_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ2_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj2_belegungart", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ2_BelegungArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.ef_hj2_belegungart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.EF_HJ2_BelegungArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.facharbeit_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Facharbeit_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.facharbeit_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Facharbeit_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.facharbeit_markiertfuerabiturberechnung", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Facharbeit_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.facharbeit_markiertfuerabiturberechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Facharbeit_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_wochenstunden", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Wochenstunden = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_wochenstunden.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Wochenstunden IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_markiertfuerabiturberechnung", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_markiertfuerabiturberechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_belegungart", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_BelegungArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj1_belegungart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ1_BelegungArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_wochenstunden", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Wochenstunden = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_wochenstunden.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Wochenstunden IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_markiertfuerabiturberechnung", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_markiertfuerabiturberechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_belegungart", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_BelegungArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q1_hj2_belegungart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q1_HJ2_BelegungArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_wochenstunden", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Wochenstunden = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_wochenstunden.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Wochenstunden IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_markiertfuerabiturberechnung", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_markiertfuerabiturberechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_belegungart", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_BelegungArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj1_belegungart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ1_BelegungArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_wochenstunden", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Wochenstunden = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_wochenstunden.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Wochenstunden IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_notenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Notenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_notenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_markiertfuerabiturberechnung", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_markiertfuerabiturberechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_belegungart", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_BelegungArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.q2_hj2_belegungart.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Q2_HJ2_BelegungArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.zulassungpunktsumme", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ZulassungPunktsumme = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.zulassungpunktsumme.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ZulassungPunktsumme IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.zulassungnotenpunktdurchschnitt", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ZulassungNotenpunktdurchschnitt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.zulassungnotenpunktdurchschnitt.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ZulassungNotenpunktdurchschnitt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungnotenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungNotenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungnotenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungNotenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungpunktsummezwischenstand", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeZwischenstand = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungpunktsummezwischenstand.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeZwischenstand IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichabweichung", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichAbweichung = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichabweichung.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichAbweichung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichbestehen", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichBestehen = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichbestehen.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichBestehen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichfreiwillig", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichFreiwillig = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichfreiwillig.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichFreiwillig IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichnotenpunkte", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichNotenpunkte = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichnotenpunkte.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichNotenpunkte IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichreihenfolge", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichReihenfolge = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungmuendlichreihenfolge.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungMuendlichReihenfolge IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungpunktsummegesamt", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeGesamt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.pruefungpunktsummegesamt.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeGesamt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fachlehrer", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Fachlehrer = :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.fachlehrer.multiple", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.Fachlehrer IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerAbiturFach.all.migration", query = "SELECT e FROM MigrationDTOSchuelerAbiturFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Fach_ID", "FachKuerzel", "-", "Kurs_ID", "KursartAllgemein", "Fachlehrer_ID", "AbiturFach", "EF_HJ1_Notenpunkte", "EF_HJ1_BelegungArt", "EF_HJ2_Notenpunkte", "EF_HJ2_BelegungArt", "Facharbeit_Notenpunkte", "Facharbeit_MarkiertFuerAbiturBerechnung", "Q1_HJ1_Wochenstunden", "Q1_HJ1_Notenpunkte", "-", "Q1_HJ1_MarkiertFuerAbiturBerechnung", "Q1_HJ1_BelegungArt", "Q1_HJ2_Wochenstunden", "Q1_HJ2_Notenpunkte", "-", "Q1_HJ2_MarkiertFuerAbiturBerechnung", "Q1_HJ2_BelegungArt", "Q2_HJ1_Wochenstunden", "Q2_HJ1_Notenpunkte", "-", "Q2_HJ1_MarkiertFuerAbiturBerechnung", "Q2_HJ1_BelegungArt", "Q2_HJ2_Wochenstunden", "Q2_HJ2_Notenpunkte", "-", "Q2_HJ2_MarkiertFuerAbiturBerechnung", "Q2_HJ2_BelegungArt", "ZulassungPunktsumme", "ZulassungNotenpunktdurchschnitt", "PruefungNotenpunkte", "PruefungPunktsummeZwischenstand", "PruefungMuendlichAbweichung", "PruefungMuendlichBestehen", "PruefungMuendlichFreiwillig", "PruefungMuendlichNotenpunkte", "PruefungMuendlichReihenfolge", "PruefungPunktsummeGesamt", "SchulnrEigner", "Fachlehrer"})
public final class MigrationDTOSchuelerAbiturFach {

	/** Eine eindeutige ID für die Abitur-Fach-Informationen des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Die eindeutige ID des Faches – verweist auf das Fach */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Das Kürzel des Faches */
	@Column(name = "FachKrz")
	@JsonProperty
	public String FachKuerzel;

	/** Letzer Kurs Q2, 2.Hj: Die Kurs ID - verweist auf Kurse */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Die allgemeine Kursart des Faches (z.B. GK, LK) */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllgemein;

	/** Letzer Kurs Q2, 2.Hj: Die ID des Fachlehrers, der als letztes in diesem Fach unterrichtet hat */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/** Gibt an, ob das Fach bei der Abiturprüfung gewählt wurde und dann die Nummer des Abiturfaches (1-4) */
	@Column(name = "AbiFach")
	@JsonProperty
	public String AbiturFach;

	/** EF, 1. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P11_1")
	@JsonProperty
	public String EF_HJ1_Notenpunkte;

	/** EF, 1. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich) */
	@Column(name = "S11_1")
	@JsonProperty
	public String EF_HJ1_BelegungArt;

	/** EF, 2. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P11_2")
	@JsonProperty
	public String EF_HJ2_Notenpunkte;

	/** EF, 2. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich) */
	@Column(name = "S11_2")
	@JsonProperty
	public String EF_HJ2_BelegungArt;

	/** BK: eingebrachte Facharbeit – Notenpunkte */
	@Column(name = "P_FA")
	@JsonProperty
	public String Facharbeit_Notenpunkte;

	/** BK: eingebrachte Facharbeit – Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R_FA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	public String Q1_HJ1_MarkiertFuerAbiturBerechnung;

	/** Q1, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S12_1")
	@JsonProperty
	public String Q1_HJ1_BelegungArt;

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
	public String Q1_HJ2_MarkiertFuerAbiturBerechnung;

	/** Q1, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S12_2")
	@JsonProperty
	public String Q1_HJ2_BelegungArt;

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
	public String Q2_HJ1_MarkiertFuerAbiturBerechnung;

	/** Q2, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S13_1")
	@JsonProperty
	public String Q2_HJ1_BelegungArt;

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
	public String Q2_HJ2_MarkiertFuerAbiturBerechnung;

	/** Q2, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S13_2")
	@JsonProperty
	public String Q2_HJ2_BelegungArt;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichAbweichung;

	/** true, falls eine mündliche Bestehensprüfung in den ersten drei Abiturfächern notwendig ist, damit das Abitur noch bestanden werden kann. */
	@Column(name = "MdlBestPruefung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichBestehen;

	/** true, falls eine freiwillige Prüfung in einem der ersten drei Abiturfächer stattfindet. */
	@Column(name = "MdlFreiwPruefung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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

	/** DEPRECATED: Die Nummer der Schule, zu der dieser Datensatz gehört (falls mehrere Schulen mit dem gleichen Schema arbeiten) */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: Letzer Kurs Q2, 2.Hj: Der Fachlehrer, der als letztes in diesem Fach unterrichtet hat */
	@Column(name = "Fachlehrer")
	@JsonProperty
	public String Fachlehrer;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAbiturFach ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerAbiturFach() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAbiturFach ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public MigrationDTOSchuelerAbiturFach(final Long ID, final Long Schueler_ID, final Long Fach_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
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
		MigrationDTOSchuelerAbiturFach other = (MigrationDTOSchuelerAbiturFach) obj;
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
		return "MigrationDTOSchuelerAbiturFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", FachKuerzel=" + this.FachKuerzel + ", Kurs_ID=" + this.Kurs_ID + ", KursartAllgemein=" + this.KursartAllgemein + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", AbiturFach=" + this.AbiturFach + ", EF_HJ1_Notenpunkte=" + this.EF_HJ1_Notenpunkte + ", EF_HJ1_BelegungArt=" + this.EF_HJ1_BelegungArt + ", EF_HJ2_Notenpunkte=" + this.EF_HJ2_Notenpunkte + ", EF_HJ2_BelegungArt=" + this.EF_HJ2_BelegungArt + ", Facharbeit_Notenpunkte=" + this.Facharbeit_Notenpunkte + ", Facharbeit_MarkiertFuerAbiturBerechnung=" + this.Facharbeit_MarkiertFuerAbiturBerechnung + ", Q1_HJ1_Wochenstunden=" + this.Q1_HJ1_Wochenstunden + ", Q1_HJ1_Notenpunkte=" + this.Q1_HJ1_Notenpunkte + ", Q1_HJ1_MarkiertFuerAbiturBerechnung=" + this.Q1_HJ1_MarkiertFuerAbiturBerechnung + ", Q1_HJ1_BelegungArt=" + this.Q1_HJ1_BelegungArt + ", Q1_HJ2_Wochenstunden=" + this.Q1_HJ2_Wochenstunden + ", Q1_HJ2_Notenpunkte=" + this.Q1_HJ2_Notenpunkte + ", Q1_HJ2_MarkiertFuerAbiturBerechnung=" + this.Q1_HJ2_MarkiertFuerAbiturBerechnung + ", Q1_HJ2_BelegungArt=" + this.Q1_HJ2_BelegungArt + ", Q2_HJ1_Wochenstunden=" + this.Q2_HJ1_Wochenstunden + ", Q2_HJ1_Notenpunkte=" + this.Q2_HJ1_Notenpunkte + ", Q2_HJ1_MarkiertFuerAbiturBerechnung=" + this.Q2_HJ1_MarkiertFuerAbiturBerechnung + ", Q2_HJ1_BelegungArt=" + this.Q2_HJ1_BelegungArt + ", Q2_HJ2_Wochenstunden=" + this.Q2_HJ2_Wochenstunden + ", Q2_HJ2_Notenpunkte=" + this.Q2_HJ2_Notenpunkte + ", Q2_HJ2_MarkiertFuerAbiturBerechnung=" + this.Q2_HJ2_MarkiertFuerAbiturBerechnung + ", Q2_HJ2_BelegungArt=" + this.Q2_HJ2_BelegungArt + ", ZulassungPunktsumme=" + this.ZulassungPunktsumme + ", ZulassungNotenpunktdurchschnitt=" + this.ZulassungNotenpunktdurchschnitt + ", PruefungNotenpunkte=" + this.PruefungNotenpunkte + ", PruefungPunktsummeZwischenstand=" + this.PruefungPunktsummeZwischenstand + ", PruefungMuendlichAbweichung=" + this.PruefungMuendlichAbweichung + ", PruefungMuendlichBestehen=" + this.PruefungMuendlichBestehen + ", PruefungMuendlichFreiwillig=" + this.PruefungMuendlichFreiwillig + ", PruefungMuendlichNotenpunkte=" + this.PruefungMuendlichNotenpunkte + ", PruefungMuendlichReihenfolge=" + this.PruefungMuendlichReihenfolge + ", PruefungPunktsummeGesamt=" + this.PruefungPunktsummeGesamt + ", SchulnrEigner=" + this.SchulnrEigner + ", Fachlehrer=" + this.Fachlehrer + ")";
	}

}
