package de.svws_nrw.db.dto.migration.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Lehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Lehrer")
@NamedQuery(name = "MigrationDTOLehrer.all", query = "SELECT e FROM MigrationDTOLehrer e")
@NamedQuery(name = "MigrationDTOLehrer.id", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOLehrer.id.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOLehrer.gu_id", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.GU_ID = :value")
@NamedQuery(name = "MigrationDTOLehrer.gu_id.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.GU_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrer.kuerzel", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTOLehrer.kuerzel.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOLehrer.kuerzellid", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.kuerzelLID = :value")
@NamedQuery(name = "MigrationDTOLehrer.kuerzellid.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.kuerzelLID IN :value")
@NamedQuery(name = "MigrationDTOLehrer.nachname", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Nachname = :value")
@NamedQuery(name = "MigrationDTOLehrer.nachname.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Nachname IN :value")
@NamedQuery(name = "MigrationDTOLehrer.vorname", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Vorname = :value")
@NamedQuery(name = "MigrationDTOLehrer.vorname.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Vorname IN :value")
@NamedQuery(name = "MigrationDTOLehrer.persontyp", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PersonTyp = :value")
@NamedQuery(name = "MigrationDTOLehrer.persontyp.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PersonTyp IN :value")
@NamedQuery(name = "MigrationDTOLehrer.schulnreigner", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOLehrer.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOLehrer.sortierung", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOLehrer.sortierung.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOLehrer.sichtbar", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOLehrer.sichtbar.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOLehrer.aenderbar", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOLehrer.aenderbar.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOLehrer.fuerexport", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.FuerExport = :value")
@NamedQuery(name = "MigrationDTOLehrer.fuerexport.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.FuerExport IN :value")
@NamedQuery(name = "MigrationDTOLehrer.statistikrelevant", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.statistikRelevant = :value")
@NamedQuery(name = "MigrationDTOLehrer.statistikrelevant.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.statistikRelevant IN :value")
@NamedQuery(name = "MigrationDTOLehrer.strasse", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strasse = :value")
@NamedQuery(name = "MigrationDTOLehrer.strasse.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strasse IN :value")
@NamedQuery(name = "MigrationDTOLehrer.strassenname", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strassenname = :value")
@NamedQuery(name = "MigrationDTOLehrer.strassenname.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strassenname IN :value")
@NamedQuery(name = "MigrationDTOLehrer.hausnr", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNr = :value")
@NamedQuery(name = "MigrationDTOLehrer.hausnr.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNr IN :value")
@NamedQuery(name = "MigrationDTOLehrer.hausnrzusatz", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNrZusatz = :value")
@NamedQuery(name = "MigrationDTOLehrer.hausnrzusatz.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name = "MigrationDTOLehrer.ort_id", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ort_ID = :value")
@NamedQuery(name = "MigrationDTOLehrer.ort_id.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ort_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrer.plz", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PLZ = :value")
@NamedQuery(name = "MigrationDTOLehrer.plz.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PLZ IN :value")
@NamedQuery(name = "MigrationDTOLehrer.ortsteil_id", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ortsteil_ID = :value")
@NamedQuery(name = "MigrationDTOLehrer.ortsteil_id.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ortsteil_ID IN :value")
@NamedQuery(name = "MigrationDTOLehrer.telefon", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefon = :value")
@NamedQuery(name = "MigrationDTOLehrer.telefon.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefon IN :value")
@NamedQuery(name = "MigrationDTOLehrer.telefonmobil", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefonMobil = :value")
@NamedQuery(name = "MigrationDTOLehrer.telefonmobil.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefonMobil IN :value")
@NamedQuery(name = "MigrationDTOLehrer.emailprivat", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailPrivat = :value")
@NamedQuery(name = "MigrationDTOLehrer.emailprivat.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailPrivat IN :value")
@NamedQuery(name = "MigrationDTOLehrer.emaildienstlich", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailDienstlich = :value")
@NamedQuery(name = "MigrationDTOLehrer.emaildienstlich.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailDienstlich IN :value")
@NamedQuery(name = "MigrationDTOLehrer.staatsangehoerigkeit", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.staatsangehoerigkeit = :value")
@NamedQuery(name = "MigrationDTOLehrer.staatsangehoerigkeit.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.staatsangehoerigkeit IN :value")
@NamedQuery(name = "MigrationDTOLehrer.geburtsdatum", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geburtsdatum = :value")
@NamedQuery(name = "MigrationDTOLehrer.geburtsdatum.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geburtsdatum IN :value")
@NamedQuery(name = "MigrationDTOLehrer.geschlecht", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geschlecht = :value")
@NamedQuery(name = "MigrationDTOLehrer.geschlecht.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geschlecht IN :value")
@NamedQuery(name = "MigrationDTOLehrer.anrede", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Anrede = :value")
@NamedQuery(name = "MigrationDTOLehrer.anrede.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Anrede IN :value")
@NamedQuery(name = "MigrationDTOLehrer.amtsbezeichnung", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Amtsbezeichnung = :value")
@NamedQuery(name = "MigrationDTOLehrer.amtsbezeichnung.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Amtsbezeichnung IN :value")
@NamedQuery(name = "MigrationDTOLehrer.titel", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Titel = :value")
@NamedQuery(name = "MigrationDTOLehrer.titel.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Titel IN :value")
@NamedQuery(name = "MigrationDTOLehrer.faecher", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Faecher = :value")
@NamedQuery(name = "MigrationDTOLehrer.faecher.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Faecher IN :value")
@NamedQuery(name = "MigrationDTOLehrer.identnrteil1", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil1 = :value")
@NamedQuery(name = "MigrationDTOLehrer.identnrteil1.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil1 IN :value")
@NamedQuery(name = "MigrationDTOLehrer.identnrteil2sernr", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil2SerNr = :value")
@NamedQuery(name = "MigrationDTOLehrer.identnrteil2sernr.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil2SerNr IN :value")
@NamedQuery(name = "MigrationDTOLehrer.panr", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PANr = :value")
@NamedQuery(name = "MigrationDTOLehrer.panr.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PANr IN :value")
@NamedQuery(name = "MigrationDTOLehrer.personalnrlbv", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.personalNrLBV = :value")
@NamedQuery(name = "MigrationDTOLehrer.personalnrlbv.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.personalNrLBV IN :value")
@NamedQuery(name = "MigrationDTOLehrer.verguetungsschluessel", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.verguetungsSchluessel = :value")
@NamedQuery(name = "MigrationDTOLehrer.verguetungsschluessel.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.verguetungsSchluessel IN :value")
@NamedQuery(name = "MigrationDTOLehrer.datumzugang", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumZugang = :value")
@NamedQuery(name = "MigrationDTOLehrer.datumzugang.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumZugang IN :value")
@NamedQuery(name = "MigrationDTOLehrer.grundzugang", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundZugang = :value")
@NamedQuery(name = "MigrationDTOLehrer.grundzugang.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundZugang IN :value")
@NamedQuery(name = "MigrationDTOLehrer.datumabgang", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumAbgang = :value")
@NamedQuery(name = "MigrationDTOLehrer.datumabgang.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumAbgang IN :value")
@NamedQuery(name = "MigrationDTOLehrer.grundabgang", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundAbgang = :value")
@NamedQuery(name = "MigrationDTOLehrer.grundabgang.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundAbgang IN :value")
@NamedQuery(name = "MigrationDTOLehrer.pflichtstdsoll", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PflichtstdSoll = :value")
@NamedQuery(name = "MigrationDTOLehrer.pflichtstdsoll.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PflichtstdSoll IN :value")
@NamedQuery(name = "MigrationDTOLehrer.rechtsverhaeltnis", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Rechtsverhaeltnis = :value")
@NamedQuery(name = "MigrationDTOLehrer.rechtsverhaeltnis.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Rechtsverhaeltnis IN :value")
@NamedQuery(name = "MigrationDTOLehrer.beschaeftigungsart", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Beschaeftigungsart = :value")
@NamedQuery(name = "MigrationDTOLehrer.beschaeftigungsart.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Beschaeftigungsart IN :value")
@NamedQuery(name = "MigrationDTOLehrer.einsatzstatus", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Einsatzstatus = :value")
@NamedQuery(name = "MigrationDTOLehrer.einsatzstatus.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Einsatzstatus IN :value")
@NamedQuery(name = "MigrationDTOLehrer.stammschulnr", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.StammschulNr = :value")
@NamedQuery(name = "MigrationDTOLehrer.stammschulnr.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.StammschulNr IN :value")
@NamedQuery(name = "MigrationDTOLehrer.unterrichtsstd", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.UnterrichtsStd = :value")
@NamedQuery(name = "MigrationDTOLehrer.unterrichtsstd.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.UnterrichtsStd IN :value")
@NamedQuery(name = "MigrationDTOLehrer.mehrleistungstd", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.MehrleistungStd = :value")
@NamedQuery(name = "MigrationDTOLehrer.mehrleistungstd.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.MehrleistungStd IN :value")
@NamedQuery(name = "MigrationDTOLehrer.entlastungstd", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.EntlastungStd = :value")
@NamedQuery(name = "MigrationDTOLehrer.entlastungstd.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.EntlastungStd IN :value")
@NamedQuery(name = "MigrationDTOLehrer.anrechnungstd", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.AnrechnungStd = :value")
@NamedQuery(name = "MigrationDTOLehrer.anrechnungstd.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.AnrechnungStd IN :value")
@NamedQuery(name = "MigrationDTOLehrer.reststd", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.RestStd = :value")
@NamedQuery(name = "MigrationDTOLehrer.reststd.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.RestStd IN :value")
@NamedQuery(name = "MigrationDTOLehrer.lpassword", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.LPassword = :value")
@NamedQuery(name = "MigrationDTOLehrer.lpassword.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.LPassword IN :value")
@NamedQuery(name = "MigrationDTOLehrer.pwaktuell", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PWAktuell = :value")
@NamedQuery(name = "MigrationDTOLehrer.pwaktuell.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.PWAktuell IN :value")
@NamedQuery(name = "MigrationDTOLehrer.schildweb_fl", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_FL = :value")
@NamedQuery(name = "MigrationDTOLehrer.schildweb_fl.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_FL IN :value")
@NamedQuery(name = "MigrationDTOLehrer.schildweb_kl", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_KL = :value")
@NamedQuery(name = "MigrationDTOLehrer.schildweb_kl.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_KL IN :value")
@NamedQuery(name = "MigrationDTOLehrer.schildweb_config", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_Config = :value")
@NamedQuery(name = "MigrationDTOLehrer.schildweb_config.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_Config IN :value")
@NamedQuery(name = "MigrationDTOLehrer.kennworttools", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortTools = :value")
@NamedQuery(name = "MigrationDTOLehrer.kennworttools.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortTools IN :value")
@NamedQuery(name = "MigrationDTOLehrer.antwort1", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort1 = :value")
@NamedQuery(name = "MigrationDTOLehrer.antwort1.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort1 IN :value")
@NamedQuery(name = "MigrationDTOLehrer.antwort2", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort2 = :value")
@NamedQuery(name = "MigrationDTOLehrer.antwort2.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort2 IN :value")
@NamedQuery(name = "MigrationDTOLehrer.kennworttoolsaktuell", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortToolsAktuell = :value")
@NamedQuery(name = "MigrationDTOLehrer.kennworttoolsaktuell.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortToolsAktuell IN :value")
@NamedQuery(name = "MigrationDTOLehrer.xnmpassword", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword = :value")
@NamedQuery(name = "MigrationDTOLehrer.xnmpassword.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword IN :value")
@NamedQuery(name = "MigrationDTOLehrer.xnmpassword2", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword2 = :value")
@NamedQuery(name = "MigrationDTOLehrer.xnmpassword2.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword2 IN :value")
@NamedQuery(name = "MigrationDTOLehrer.credentialid", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.CredentialID = :value")
@NamedQuery(name = "MigrationDTOLehrer.credentialid.multiple", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.CredentialID IN :value")
@NamedQuery(name = "MigrationDTOLehrer.primaryKeyQuery", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOLehrer.all.migration", query = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "GU_ID", "Kuerzel", "kuerzelLID", "Nachname", "Vorname", "PersonTyp", "SchulnrEigner", "Sortierung", "Sichtbar", "Aenderbar", "FuerExport", "statistikRelevant", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Ort_ID", "PLZ", "Ortsteil_ID", "telefon", "telefonMobil", "eMailPrivat", "eMailDienstlich", "staatsangehoerigkeit", "Geburtsdatum", "Geschlecht", "Anrede", "Amtsbezeichnung", "Titel", "Faecher", "identNrTeil1", "identNrTeil2SerNr", "PANr", "personalNrLBV", "verguetungsSchluessel", "DatumZugang", "GrundZugang", "DatumAbgang", "GrundAbgang", "PflichtstdSoll", "Rechtsverhaeltnis", "Beschaeftigungsart", "Einsatzstatus", "StammschulNr", "UnterrichtsStd", "MehrleistungStd", "EntlastungStd", "AnrechnungStd", "RestStd", "LPassword", "PWAktuell", "SchILDweb_FL", "SchILDweb_KL", "SchILDweb_Config", "KennwortTools", "Antwort1", "Antwort2", "KennwortToolsAktuell", "XNMPassword", "XNMPassword2", "CredentialID"})
public final class MigrationDTOLehrer {

	/** Eindeutige ID zur Kennzeichnung des Lehrer-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Eindeutige ID Datenbank übergreifend. Wurde früher mal für Logineo genutzt, kann später mal zur Identifizierung genutzt werden. */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** Lehrer-Kürzel für eine lesbare eindeutige Identifikation des Lehrers */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Lehrer-Kürzel für eine eindeutige Identifikation des Lehrers – Verwendung für die Statistik - TODO lassen sich kuerzel und LIDKrz nicht sinnvoll zusammenfassen? */
	@Column(name = "LIDKrz")
	@JsonProperty
	public String kuerzelLID;

	/** Der Nachname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Nachname")
	@JsonProperty
	public String Nachname;

	/** Der Vorname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Vorname")
	@JsonProperty
	public String Vorname;

	/** Die Art der Person – wurde nachträglich hinzugefügt, damit auch Nicht-Lehrer in die Liste aufgenommen und unterschieden werden können */
	@Column(name = "PersonTyp")
	@JsonProperty
	public String PersonTyp;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Eine Nummer, die zur Sortierung der Lehrer-Datensätze verwendet werden kann. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an, ob der Lehrer-Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht.  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an, ob Änderungen am Lehrer-Datensatz erlaubt sind. */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Gibt an, ob der Lehrer-Datensatz für den Export in andere Software verwendet werden soll - TODO fuer welche(n) Zweck(e) wird dies gespeichert - gehört dies an diese Stelle?  */
	@Column(name = "FuerExport")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean FuerExport;

	/** Gibt an, ob der Lehrer-Datensatz bei der Statistik berücksichtigt werden soll. */
	@Column(name = "Statistik")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean statistikRelevant;

	/** Adressdaten des Lehrers: Strasse */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Straßenname der Lehrkraft */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** Adressdaten des Lehrers: Fremdschlüssel auf die Katalog-Tabelle mit den Orten */
	@Column(name = "Ort_ID")
	@JsonProperty
	public Long Ort_ID;

	/** DEPRECATED: Adressdaten des Lehrers: Postleitzahl - Ort als Spalte in der Tabelle ergänzen, ähnlich wie bei Schüler, wichtig da ansonsten Probleme auftauchen, wenn ein PLZ ? Ortsname nicht eindeutig ist, was gelegentlich vorkommt */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Adressdaten des Lehrers: Fremdschlüssel auf die Katalog-Tabelle mit den Ortsteilen */
	@Column(name = "Ortsteil_ID")
	@JsonProperty
	public Long Ortsteil_ID;

	/** Adressdaten des Lehrers: Telefonnummer */
	@Column(name = "Tel")
	@JsonProperty
	public String telefon;

	/** Adressdaten des Lehrers: Mobilnummer oder Faxnummer */
	@Column(name = "Handy")
	@JsonProperty
	public String telefonMobil;

	/** Adressdaten des Lehrers: Private Email-Adresse */
	@Column(name = "Email")
	@JsonProperty
	public String eMailPrivat;

	/** Adressdaten des Lehrers: Dienstliche Email-Adresse */
	@Column(name = "EmailDienstlich")
	@JsonProperty
	public String eMailDienstlich;

	/** Die erste Staatsangehörigkeit des Lehrers */
	@Column(name = "StaatKrz")
	@JsonProperty
	public String staatsangehoerigkeit;

	/** Das Geburtsdatum des Lehrers */
	@Column(name = "Geburtsdatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Geburtsdatum;

	/** Das Geschlecht des Lehrers - TODO ist in der Datenbank als String und nicht als Integer (3/4) hinterlegt, dies sollte in allen Tabellen einheitlich sein */
	@Column(name = "Geschlecht")
	@JsonProperty
	public String Geschlecht;

	/** Die Anrede für den Lehrer */
	@Column(name = "Anrede")
	@JsonProperty
	public String Anrede;

	/** Die Amtsbezeichnung des Lehrers */
	@Column(name = "Amtsbezeichnung")
	@JsonProperty
	public String Amtsbezeichnung;

	/** Ggf. der Titel des Lehrers */
	@Column(name = "Titel")
	@JsonProperty
	public String Titel;

	/** Die Fächer, die der Lehrer unterrichtet - TODO hat dieses Feld noch einen Zweck? Fächer sind dem Lehrer eigentlich anders zugeordnet...   */
	@Column(name = "Faecher")
	@JsonProperty
	public String Faecher;

	/** Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen */
	@Column(name = "IdentNr1")
	@JsonProperty
	public String identNrTeil1;

	/** Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben */
	@Column(name = "SerNr")
	@JsonProperty
	public String identNrTeil2SerNr;

	/** Personalakten-Nummer (wird von den Bezirksregierungen ggf genutzt) */
	@Column(name = "PANr")
	@JsonProperty
	public String PANr;

	/** Die Personalnummer beim Landesamt für Besoldung und Versorgung (LBV) */
	@Column(name = "LBVNr")
	@JsonProperty
	public String personalNrLBV;

	/** Der Vergütungsschlüssel */
	@Column(name = "VSchluessel")
	@JsonProperty
	public String verguetungsSchluessel;

	/** Das Datum, wann der Lehrer an die Schule gekommen ist. */
	@Column(name = "DatumZugang")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String DatumZugang;

	/** Der Grund für den Zugang des Lehrers */
	@Column(name = "GrundZugang")
	@JsonProperty
	public String GrundZugang;

	/** Das Datum, wann der Lehrer die Schule verlassen hat. */
	@Column(name = "DatumAbgang")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String DatumAbgang;

	/** Der Grund für das Verlassen der Schule durch den Lehrer */
	@Column(name = "GrundAbgang")
	@JsonProperty
	public String GrundAbgang;

	/** Das Pflichtstundensoll des Lehrers */
	@Column(name = "PflichtstdSoll")
	@JsonProperty
	public Double PflichtstdSoll;

	/** Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) */
	@Column(name = "Rechtsverhaeltnis")
	@JsonProperty
	public String Rechtsverhaeltnis;

	/** Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) */
	@Column(name = "Beschaeftigungsart")
	@JsonProperty
	public String Beschaeftigungsart;

	/** Der Einsatzstatus (z.B. Stammschule, nur hier tätig) */
	@Column(name = "Einsatzstatus")
	@JsonProperty
	public String Einsatzstatus;

	/** Die Schulnummer der Stammschule, sofern diese abweicht */
	@Column(name = "StammschulNr")
	@JsonProperty
	public String StammschulNr;

	/** Berechnetes Feld: Die Anzahl der unterrichteten Stunden */
	@Column(name = "UnterrichtsStd")
	@JsonProperty
	public Double UnterrichtsStd;

	/** Berechnetes Feld: Die Stunden für eine Mehrleistung */
	@Column(name = "MehrleistungStd")
	@JsonProperty
	public Double MehrleistungStd;

	/** Berechnetes Feld: Die Stunden für eine Entlastung */
	@Column(name = "EntlastungStd")
	@JsonProperty
	public Double EntlastungStd;

	/** Berechnetes Feld: Die angerechneten Stunden für eine nichtunterrichtliche Tätigkeit */
	@Column(name = "AnrechnungStd")
	@JsonProperty
	public Double AnrechnungStd;

	/** Berechnetes Feld: Reststunden */
	@Column(name = "RestStd")
	@JsonProperty
	public Double RestStd;

	/** Wird für das GS-Modul genutzt (gehashed). */
	@Column(name = "LPassword")
	@JsonProperty
	public String LPassword;

	/** Gibt an ob das LPassword geändert wurde oder ob es noch das Initialkennwort ist */
	@Column(name = "PWAktuell")
	@JsonProperty
	public String PWAktuell;

	/** DEPRECATED: nicht mehr genutzt SchildWeb */
	@Column(name = "SchILDweb_FL")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean SchILDweb_FL;

	/** DEPRECATED: nicht mehr genutzt SchildWeb */
	@Column(name = "SchILDweb_KL")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean SchILDweb_KL;

	/** DEPRECATED: nicht mehr genutzt SchildWeb */
	@Column(name = "SchILDweb_Config")
	@JsonProperty
	public String SchILDweb_Config;

	/** DEPRECATED: wurde nie verwendet */
	@Column(name = "KennwortTools")
	@JsonProperty
	public String KennwortTools;

	/** DEPRECATED: wurde nie verwendet */
	@Column(name = "Antwort1")
	@JsonProperty
	public String Antwort1;

	/** DEPRECATED: wurde nie verwendet */
	@Column(name = "Antwort2")
	@JsonProperty
	public String Antwort2;

	/** DEPRECATED: wurde nie verwendet */
	@Column(name = "KennwortToolsAktuell")
	@JsonProperty
	public String KennwortToolsAktuell;

	/** Passwort für das XNM-Tool  */
	@Column(name = "XNMPassword")
	@JsonProperty
	public String XNMPassword;

	/** zweites Passwort für das XNM-Tool  */
	@Column(name = "XNMPassword2")
	@JsonProperty
	public String XNMPassword2;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Nachname   der Wert für das Attribut Nachname
	 */
	public MigrationDTOLehrer(final Long ID, final String Kuerzel, final String Nachname) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Nachname == null) {
			throw new NullPointerException("Nachname must not be null");
		}
		this.Nachname = Nachname;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrer other = (MigrationDTOLehrer) obj;
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
		return "MigrationDTOLehrer(ID=" + this.ID + ", GU_ID=" + this.GU_ID + ", Kuerzel=" + this.Kuerzel + ", kuerzelLID=" + this.kuerzelLID + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", PersonTyp=" + this.PersonTyp + ", SchulnrEigner=" + this.SchulnrEigner + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", FuerExport=" + this.FuerExport + ", statistikRelevant=" + this.statistikRelevant + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", PLZ=" + this.PLZ + ", Ortsteil_ID=" + this.Ortsteil_ID + ", telefon=" + this.telefon + ", telefonMobil=" + this.telefonMobil + ", eMailPrivat=" + this.eMailPrivat + ", eMailDienstlich=" + this.eMailDienstlich + ", staatsangehoerigkeit=" + this.staatsangehoerigkeit + ", Geburtsdatum=" + this.Geburtsdatum + ", Geschlecht=" + this.Geschlecht + ", Anrede=" + this.Anrede + ", Amtsbezeichnung=" + this.Amtsbezeichnung + ", Titel=" + this.Titel + ", Faecher=" + this.Faecher + ", identNrTeil1=" + this.identNrTeil1 + ", identNrTeil2SerNr=" + this.identNrTeil2SerNr + ", PANr=" + this.PANr + ", personalNrLBV=" + this.personalNrLBV + ", verguetungsSchluessel=" + this.verguetungsSchluessel + ", DatumZugang=" + this.DatumZugang + ", GrundZugang=" + this.GrundZugang + ", DatumAbgang=" + this.DatumAbgang + ", GrundAbgang=" + this.GrundAbgang + ", PflichtstdSoll=" + this.PflichtstdSoll + ", Rechtsverhaeltnis=" + this.Rechtsverhaeltnis + ", Beschaeftigungsart=" + this.Beschaeftigungsart + ", Einsatzstatus=" + this.Einsatzstatus + ", StammschulNr=" + this.StammschulNr + ", UnterrichtsStd=" + this.UnterrichtsStd + ", MehrleistungStd=" + this.MehrleistungStd + ", EntlastungStd=" + this.EntlastungStd + ", AnrechnungStd=" + this.AnrechnungStd + ", RestStd=" + this.RestStd + ", LPassword=" + this.LPassword + ", PWAktuell=" + this.PWAktuell + ", SchILDweb_FL=" + this.SchILDweb_FL + ", SchILDweb_KL=" + this.SchILDweb_KL + ", SchILDweb_Config=" + this.SchILDweb_Config + ", KennwortTools=" + this.KennwortTools + ", Antwort1=" + this.Antwort1 + ", Antwort2=" + this.Antwort2 + ", KennwortToolsAktuell=" + this.KennwortToolsAktuell + ", XNMPassword=" + this.XNMPassword + ", XNMPassword2=" + this.XNMPassword2 + ", CredentialID=" + this.CredentialID + ")";
	}

}
