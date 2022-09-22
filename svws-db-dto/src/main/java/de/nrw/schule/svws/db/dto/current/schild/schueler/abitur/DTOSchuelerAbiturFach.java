package de.nrw.schule.svws.db.dto.current.schild.schueler.abitur;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromNotenpunkte;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromNotenpunkteString;
import de.nrw.schule.svws.db.converter.current.gost.AbiturBelegungsartConverter;
import de.nrw.schule.svws.db.converter.current.gost.AbiturKursMarkierungConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStAbiturFachConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStKursartConverter;

import de.nrw.schule.svws.core.data.gost.AbiturKursMarkierung;
import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.core.types.gost.AbiturBelegungsart;
import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.core.types.gost.GostKursart;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromNotenpunkteSerializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromNotenpunkteDeserializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromNotenpunkteStringSerializer;
import de.nrw.schule.svws.csv.converter.current.NoteConverterFromNotenpunkteStringDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.AbiturBelegungsartConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.AbiturBelegungsartConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.AbiturKursMarkierungConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.AbiturKursMarkierungConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStAbiturFachConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStAbiturFachConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStKursartConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStKursartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAbiFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAbiFaecher")
@NamedQuery(name="DTOSchuelerAbiturFach.all", query="SELECT e FROM DTOSchuelerAbiturFach e")
@NamedQuery(name="DTOSchuelerAbiturFach.id", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.id.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.schueler_id", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.schueler_id.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.fach_id", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fach_ID = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.fach_id.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.fachkuerzel", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.FachKuerzel = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.fachkuerzel.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.FachKuerzel IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.kurs_id", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Kurs_ID = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.kurs_id.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.kursartallgemein", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.KursartAllgemein = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.kursartallgemein.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.KursartAllgemein IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.fachlehrer_id", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.fachlehrer_id.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.abiturfach", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.AbiturFach = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.abiturfach.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.AbiturFach IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj1_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj1_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj1_belegungart", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_BelegungArt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj1_belegungart.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ1_BelegungArt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj2_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj2_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj2_belegungart", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_BelegungArt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.ef_hj2_belegungart.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.EF_HJ2_BelegungArt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.facharbeit_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.facharbeit_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.facharbeit_markiertfuerabiturberechnung", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.facharbeit_markiertfuerabiturberechnung.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Facharbeit_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_wochenstunden", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Wochenstunden = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_wochenstunden.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Wochenstunden IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_markiertfuerabiturberechnung", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_markiertfuerabiturberechnung.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_belegungart", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_BelegungArt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj1_belegungart.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ1_BelegungArt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_wochenstunden", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Wochenstunden = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_wochenstunden.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Wochenstunden IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_markiertfuerabiturberechnung", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_markiertfuerabiturberechnung.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_belegungart", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_BelegungArt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q1_hj2_belegungart.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q1_HJ2_BelegungArt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_wochenstunden", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Wochenstunden = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_wochenstunden.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Wochenstunden IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_markiertfuerabiturberechnung", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_markiertfuerabiturberechnung.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_belegungart", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_BelegungArt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj1_belegungart.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ1_BelegungArt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_wochenstunden", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Wochenstunden = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_wochenstunden.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Wochenstunden IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_notenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Notenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_notenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_Notenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_markiertfuerabiturberechnung", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_MarkiertFuerAbiturBerechnung = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_markiertfuerabiturberechnung.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_MarkiertFuerAbiturBerechnung IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_belegungart", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_BelegungArt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.q2_hj2_belegungart.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.Q2_HJ2_BelegungArt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.zulassungpunktsumme", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungPunktsumme = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.zulassungpunktsumme.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungPunktsumme IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.zulassungnotenpunktdurchschnitt", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungNotenpunktdurchschnitt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.zulassungnotenpunktdurchschnitt.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ZulassungNotenpunktdurchschnitt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungnotenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungNotenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungnotenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungNotenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungpunktsummezwischenstand", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeZwischenstand = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungpunktsummezwischenstand.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeZwischenstand IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichabweichung", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichAbweichung = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichabweichung.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichAbweichung IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichbestehen", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichBestehen = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichbestehen.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichBestehen IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichfreiwillig", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichFreiwillig = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichfreiwillig.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichFreiwillig IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichnotenpunkte", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichNotenpunkte = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichnotenpunkte.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichNotenpunkte IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichreihenfolge", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichReihenfolge = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungmuendlichreihenfolge.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungMuendlichReihenfolge IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungpunktsummegesamt", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeGesamt = :value")
@NamedQuery(name="DTOSchuelerAbiturFach.pruefungpunktsummegesamt.multiple", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.PruefungPunktsummeGesamt IN :value")
@NamedQuery(name="DTOSchuelerAbiturFach.primaryKeyQuery", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerAbiturFach.all.migration", query="SELECT e FROM DTOSchuelerAbiturFach e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","Fach_ID","FachKuerzel","-","Kurs_ID","KursartAllgemein","Fachlehrer_ID","AbiturFach","EF_HJ1_Notenpunkte","EF_HJ1_BelegungArt","EF_HJ2_Notenpunkte","EF_HJ2_BelegungArt","Facharbeit_Notenpunkte","Facharbeit_MarkiertFuerAbiturBerechnung","Q1_HJ1_Wochenstunden","Q1_HJ1_Notenpunkte","-","Q1_HJ1_MarkiertFuerAbiturBerechnung","Q1_HJ1_BelegungArt","Q1_HJ2_Wochenstunden","Q1_HJ2_Notenpunkte","-","Q1_HJ2_MarkiertFuerAbiturBerechnung","Q1_HJ2_BelegungArt","Q2_HJ1_Wochenstunden","Q2_HJ1_Notenpunkte","-","Q2_HJ1_MarkiertFuerAbiturBerechnung","Q2_HJ1_BelegungArt","Q2_HJ2_Wochenstunden","Q2_HJ2_Notenpunkte","-","Q2_HJ2_MarkiertFuerAbiturBerechnung","Q2_HJ2_BelegungArt","ZulassungPunktsumme","ZulassungNotenpunktdurchschnitt","PruefungNotenpunkte","PruefungPunktsummeZwischenstand","PruefungMuendlichAbweichung","PruefungMuendlichBestehen","PruefungMuendlichFreiwillig","PruefungMuendlichNotenpunkte","PruefungMuendlichReihenfolge","PruefungPunktsummeGesamt"})
public class DTOSchuelerAbiturFach {

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
	@Convert(converter=GOStKursartConverter.class)
	@JsonSerialize(using=GOStKursartConverterSerializer.class)
	@JsonDeserialize(using=GOStKursartConverterDeserializer.class)
	public GostKursart KursartAllgemein;

	/** Letzer Kurs Q2, 2.Hj: Die ID des Fachlehrers, der als letztes in diesem Fach unterrichtet hat */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/** Gibt an, ob das Fach bei der Abiturprüfung gewählt wurde und dann die Nummer des Abiturfaches (1-4) */
	@Column(name = "AbiFach")
	@JsonProperty
	@Convert(converter=GOStAbiturFachConverter.class)
	@JsonSerialize(using=GOStAbiturFachConverterSerializer.class)
	@JsonDeserialize(using=GOStAbiturFachConverterDeserializer.class)
	public GostAbiturFach AbiturFach;

	/** EF, 1. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P11_1")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note EF_HJ1_Notenpunkte;

	/** EF, 1. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich) */
	@Column(name = "S11_1")
	@JsonProperty
	@Convert(converter=AbiturBelegungsartConverter.class)
	@JsonSerialize(using=AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using=AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart EF_HJ1_BelegungArt;

	/** EF, 2. HJ: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P11_2")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note EF_HJ2_Notenpunkte;

	/** EF, 2. HJ: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich) */
	@Column(name = "S11_2")
	@JsonProperty
	@Convert(converter=AbiturBelegungsartConverter.class)
	@JsonSerialize(using=AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using=AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart EF_HJ2_BelegungArt;

	/** BK: eingebrachte Facharbeit – Notenpunkte */
	@Column(name = "P_FA")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note Facharbeit_Notenpunkte;

	/** BK: eingebrachte Facharbeit – Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R_FA")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Facharbeit_MarkiertFuerAbiturBerechnung;

	/** Q1, 1. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W12_1")
	@JsonProperty
	public Integer Q1_HJ1_Wochenstunden;

	/** Q1, 1. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P12_1")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note Q1_HJ1_Notenpunkte;

	/** Q1, 1. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R12_1")
	@JsonProperty
	@Convert(converter=AbiturKursMarkierungConverter.class)
	@JsonSerialize(using=AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using=AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q1_HJ1_MarkiertFuerAbiturBerechnung;

	/** Q1, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S12_1")
	@JsonProperty
	@Convert(converter=AbiturBelegungsartConverter.class)
	@JsonSerialize(using=AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using=AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q1_HJ1_BelegungArt;

	/** Q1, 2. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W12_2")
	@JsonProperty
	public Integer Q1_HJ2_Wochenstunden;

	/** Q1, 2. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P12_2")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note Q1_HJ2_Notenpunkte;

	/** Q1, 2. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R12_2")
	@JsonProperty
	@Convert(converter=AbiturKursMarkierungConverter.class)
	@JsonSerialize(using=AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using=AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q1_HJ2_MarkiertFuerAbiturBerechnung;

	/** Q1, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S12_2")
	@JsonProperty
	@Convert(converter=AbiturBelegungsartConverter.class)
	@JsonSerialize(using=AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using=AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q1_HJ2_BelegungArt;

	/** Q2, 1. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W13_1")
	@JsonProperty
	public Integer Q2_HJ1_Wochenstunden;

	/** Q2, 1. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P13_1")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note Q2_HJ1_Notenpunkte;

	/** Q2, 1. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R13_1")
	@JsonProperty
	@Convert(converter=AbiturKursMarkierungConverter.class)
	@JsonSerialize(using=AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using=AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q2_HJ1_MarkiertFuerAbiturBerechnung;

	/** Q2, 1. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S13_1")
	@JsonProperty
	@Convert(converter=AbiturBelegungsartConverter.class)
	@JsonSerialize(using=AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using=AbiturBelegungsartConverterDeserializer.class)
	public AbiturBelegungsart Q2_HJ1_BelegungArt;

	/** Q2,21. Hj: Die Anzahl der Wochenstunden in diesem Halbjahr */
	@Column(name = "W13_2")
	@JsonProperty
	public Integer Q2_HJ2_Wochenstunden;

	/** Q2, 2. Hj: Die Notenpunkte für das Fach, NULL falls das Fach in dieserm Halbjahr nicht belegt wurde */
	@Column(name = "P13_2")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkteString.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteStringSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteStringDeserializer.class)
	public Note Q2_HJ2_Notenpunkte;

	/** Q2, 2. Hj: Gibt an, ob die Notenpunkte bei der Abiturberechnung genutzt werden, d.h. zur Nutzung markiert wurden. (+: Nutze für Berechnung, -: Nutze nicht für die Berechnung; /: Für die Nutzung zur Berechnung gesperrt) */
	@Column(name = "R13_2")
	@JsonProperty
	@Convert(converter=AbiturKursMarkierungConverter.class)
	@JsonSerialize(using=AbiturKursMarkierungConverterSerializer.class)
	@JsonDeserialize(using=AbiturKursMarkierungConverterDeserializer.class)
	public AbiturKursMarkierung Q2_HJ2_MarkiertFuerAbiturBerechnung;

	/** Q2, 2. Hj: Gibt die Art und Schriftlichkeit des Kurses an (-: nicht belegt, M: GK mündlich, S: GK schriftlich, Z: Zusatzkurs, L: LK) */
	@Column(name = "S13_2")
	@JsonProperty
	@Convert(converter=AbiturBelegungsartConverter.class)
	@JsonSerialize(using=AbiturBelegungsartConverterSerializer.class)
	@JsonDeserialize(using=AbiturBelegungsartConverterDeserializer.class)
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
	@Convert(converter=NoteConverterFromNotenpunkte.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteDeserializer.class)
	public Note PruefungNotenpunkte;

	/** Die Notenpunkte aus der Abiturprüfung multipliziert mit dem entsprechenden Faktor, falls das Fach eines der vier Abiturfächer ist (hier wird z.B. die besondere Lernleistung berücksichtigt */
	@Column(name = "Zwischenstand")
	@JsonProperty
	public Integer PruefungPunktsummeZwischenstand;

	/** true, falls eine mündliche Abweichungsprüfung in einem der ersten drei Abiturfächer nötig ist. */
	@Column(name = "MdlPflichtPruefung")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichAbweichung;

	/** true, falls eine mündliche Bestehensprüfung in den ersten drei Abiturfächern notwendig ist, damit das Abitur noch bestanden werden kann. */
	@Column(name = "MdlBestPruefung")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichBestehen;

	/** true, falls eine freiwillige Prüfung in einem der ersten drei Abiturfächer stattfindet. */
	@Column(name = "MdlFreiwPruefung")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean PruefungMuendlichFreiwillig;

	/** enthält die Notenpunkte aus der mündlichen Abiturprüfung in einem der ersten drei Abiturfächer, falls diese durchgeführt wird, ansonsten NULL */
	@Column(name = "MdlPruefErgebnis")
	@JsonProperty
	@Convert(converter=NoteConverterFromNotenpunkte.class)
	@JsonSerialize(using=NoteConverterFromNotenpunkteSerializer.class)
	@JsonDeserialize(using=NoteConverterFromNotenpunkteDeserializer.class)
	public Note PruefungMuendlichNotenpunkte;

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
	public DTOSchuelerAbiturFach(final Long ID, final Long Schueler_ID, final Long Fach_ID) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerAbiturFach other = (DTOSchuelerAbiturFach) obj;
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
		return "DTOSchuelerAbiturFach(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Fach_ID=" + this.Fach_ID + ", FachKuerzel=" + this.FachKuerzel + ", Kurs_ID=" + this.Kurs_ID + ", KursartAllgemein=" + this.KursartAllgemein + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ", AbiturFach=" + this.AbiturFach + ", EF_HJ1_Notenpunkte=" + this.EF_HJ1_Notenpunkte + ", EF_HJ1_BelegungArt=" + this.EF_HJ1_BelegungArt + ", EF_HJ2_Notenpunkte=" + this.EF_HJ2_Notenpunkte + ", EF_HJ2_BelegungArt=" + this.EF_HJ2_BelegungArt + ", Facharbeit_Notenpunkte=" + this.Facharbeit_Notenpunkte + ", Facharbeit_MarkiertFuerAbiturBerechnung=" + this.Facharbeit_MarkiertFuerAbiturBerechnung + ", Q1_HJ1_Wochenstunden=" + this.Q1_HJ1_Wochenstunden + ", Q1_HJ1_Notenpunkte=" + this.Q1_HJ1_Notenpunkte + ", Q1_HJ1_MarkiertFuerAbiturBerechnung=" + this.Q1_HJ1_MarkiertFuerAbiturBerechnung + ", Q1_HJ1_BelegungArt=" + this.Q1_HJ1_BelegungArt + ", Q1_HJ2_Wochenstunden=" + this.Q1_HJ2_Wochenstunden + ", Q1_HJ2_Notenpunkte=" + this.Q1_HJ2_Notenpunkte + ", Q1_HJ2_MarkiertFuerAbiturBerechnung=" + this.Q1_HJ2_MarkiertFuerAbiturBerechnung + ", Q1_HJ2_BelegungArt=" + this.Q1_HJ2_BelegungArt + ", Q2_HJ1_Wochenstunden=" + this.Q2_HJ1_Wochenstunden + ", Q2_HJ1_Notenpunkte=" + this.Q2_HJ1_Notenpunkte + ", Q2_HJ1_MarkiertFuerAbiturBerechnung=" + this.Q2_HJ1_MarkiertFuerAbiturBerechnung + ", Q2_HJ1_BelegungArt=" + this.Q2_HJ1_BelegungArt + ", Q2_HJ2_Wochenstunden=" + this.Q2_HJ2_Wochenstunden + ", Q2_HJ2_Notenpunkte=" + this.Q2_HJ2_Notenpunkte + ", Q2_HJ2_MarkiertFuerAbiturBerechnung=" + this.Q2_HJ2_MarkiertFuerAbiturBerechnung + ", Q2_HJ2_BelegungArt=" + this.Q2_HJ2_BelegungArt + ", ZulassungPunktsumme=" + this.ZulassungPunktsumme + ", ZulassungNotenpunktdurchschnitt=" + this.ZulassungNotenpunktdurchschnitt + ", PruefungNotenpunkte=" + this.PruefungNotenpunkte + ", PruefungPunktsummeZwischenstand=" + this.PruefungPunktsummeZwischenstand + ", PruefungMuendlichAbweichung=" + this.PruefungMuendlichAbweichung + ", PruefungMuendlichBestehen=" + this.PruefungMuendlichBestehen + ", PruefungMuendlichFreiwillig=" + this.PruefungMuendlichFreiwillig + ", PruefungMuendlichNotenpunkte=" + this.PruefungMuendlichNotenpunkte + ", PruefungMuendlichReihenfolge=" + this.PruefungMuendlichReihenfolge + ", PruefungPunktsummeGesamt=" + this.PruefungPunktsummeGesamt + ")";
	}

}