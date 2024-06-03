package de.svws_nrw.db.dto.migration.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;


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
@JsonPropertyOrder({"ID", "GU_ID", "Kuerzel", "kuerzelLID", "Nachname", "Vorname", "PersonTyp", "SchulnrEigner", "Sortierung", "Sichtbar", "Aenderbar", "FuerExport", "statistikRelevant", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Ort_ID", "PLZ", "Ortsteil_ID", "telefon", "telefonMobil", "eMailPrivat", "eMailDienstlich", "staatsangehoerigkeit", "Geburtsdatum", "Geschlecht", "Anrede", "Amtsbezeichnung", "Titel", "Faecher", "identNrTeil1", "identNrTeil2SerNr", "PANr", "personalNrLBV", "verguetungsSchluessel", "DatumZugang", "GrundZugang", "DatumAbgang", "GrundAbgang", "KennwortTools", "KennwortToolsAktuell", "LPassword", "PWAktuell", "PflichtstdSoll", "Rechtsverhaeltnis", "Beschaeftigungsart", "Einsatzstatus", "StammschulNr", "UnterrichtsStd", "MehrleistungStd", "EntlastungStd", "AnrechnungStd", "RestStd", "SchILDweb_FL", "SchILDweb_KL", "SchILDweb_Config", "Antwort1", "Antwort2", "XNMPassword", "XNMPassword2", "CredentialID"})
public final class MigrationDTOLehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOLehrer e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOLehrer e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kuerzelLID */
	public static final String QUERY_BY_KUERZELLID = "SELECT e FROM MigrationDTOLehrer e WHERE e.kuerzelLID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kuerzelLID */
	public static final String QUERY_LIST_BY_KUERZELLID = "SELECT e FROM MigrationDTOLehrer e WHERE e.kuerzelLID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nachname */
	public static final String QUERY_BY_NACHNAME = "SELECT e FROM MigrationDTOLehrer e WHERE e.Nachname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nachname */
	public static final String QUERY_LIST_BY_NACHNAME = "SELECT e FROM MigrationDTOLehrer e WHERE e.Nachname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname */
	public static final String QUERY_BY_VORNAME = "SELECT e FROM MigrationDTOLehrer e WHERE e.Vorname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname */
	public static final String QUERY_LIST_BY_VORNAME = "SELECT e FROM MigrationDTOLehrer e WHERE e.Vorname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PersonTyp */
	public static final String QUERY_BY_PERSONTYP = "SELECT e FROM MigrationDTOLehrer e WHERE e.PersonTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PersonTyp */
	public static final String QUERY_LIST_BY_PERSONTYP = "SELECT e FROM MigrationDTOLehrer e WHERE e.PersonTyp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOLehrer e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM MigrationDTOLehrer e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM MigrationDTOLehrer e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FuerExport */
	public static final String QUERY_BY_FUEREXPORT = "SELECT e FROM MigrationDTOLehrer e WHERE e.FuerExport = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FuerExport */
	public static final String QUERY_LIST_BY_FUEREXPORT = "SELECT e FROM MigrationDTOLehrer e WHERE e.FuerExport IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes statistikRelevant */
	public static final String QUERY_BY_STATISTIKRELEVANT = "SELECT e FROM MigrationDTOLehrer e WHERE e.statistikRelevant = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes statistikRelevant */
	public static final String QUERY_LIST_BY_STATISTIKRELEVANT = "SELECT e FROM MigrationDTOLehrer e WHERE e.statistikRelevant IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strasse */
	public static final String QUERY_BY_STRASSE = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strasse */
	public static final String QUERY_LIST_BY_STRASSE = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM MigrationDTOLehrer e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOLehrer e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort_ID */
	public static final String QUERY_BY_ORT_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ort_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort_ID */
	public static final String QUERY_LIST_BY_ORT_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ort_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM MigrationDTOLehrer e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM MigrationDTOLehrer e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ortsteil_ID */
	public static final String QUERY_BY_ORTSTEIL_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ortsteil_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ortsteil_ID */
	public static final String QUERY_LIST_BY_ORTSTEIL_ID = "SELECT e FROM MigrationDTOLehrer e WHERE e.Ortsteil_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes telefon */
	public static final String QUERY_BY_TELEFON = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes telefon */
	public static final String QUERY_LIST_BY_TELEFON = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes telefonMobil */
	public static final String QUERY_BY_TELEFONMOBIL = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefonMobil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes telefonMobil */
	public static final String QUERY_LIST_BY_TELEFONMOBIL = "SELECT e FROM MigrationDTOLehrer e WHERE e.telefonMobil IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes eMailPrivat */
	public static final String QUERY_BY_EMAILPRIVAT = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailPrivat = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes eMailPrivat */
	public static final String QUERY_LIST_BY_EMAILPRIVAT = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailPrivat IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes eMailDienstlich */
	public static final String QUERY_BY_EMAILDIENSTLICH = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailDienstlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes eMailDienstlich */
	public static final String QUERY_LIST_BY_EMAILDIENSTLICH = "SELECT e FROM MigrationDTOLehrer e WHERE e.eMailDienstlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes staatsangehoerigkeit */
	public static final String QUERY_BY_STAATSANGEHOERIGKEIT = "SELECT e FROM MigrationDTOLehrer e WHERE e.staatsangehoerigkeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes staatsangehoerigkeit */
	public static final String QUERY_LIST_BY_STAATSANGEHOERIGKEIT = "SELECT e FROM MigrationDTOLehrer e WHERE e.staatsangehoerigkeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsdatum */
	public static final String QUERY_BY_GEBURTSDATUM = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geburtsdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsdatum */
	public static final String QUERY_LIST_BY_GEBURTSDATUM = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geburtsdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geschlecht */
	public static final String QUERY_BY_GESCHLECHT = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geschlecht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geschlecht */
	public static final String QUERY_LIST_BY_GESCHLECHT = "SELECT e FROM MigrationDTOLehrer e WHERE e.Geschlecht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anrede */
	public static final String QUERY_BY_ANREDE = "SELECT e FROM MigrationDTOLehrer e WHERE e.Anrede = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anrede */
	public static final String QUERY_LIST_BY_ANREDE = "SELECT e FROM MigrationDTOLehrer e WHERE e.Anrede IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Amtsbezeichnung */
	public static final String QUERY_BY_AMTSBEZEICHNUNG = "SELECT e FROM MigrationDTOLehrer e WHERE e.Amtsbezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Amtsbezeichnung */
	public static final String QUERY_LIST_BY_AMTSBEZEICHNUNG = "SELECT e FROM MigrationDTOLehrer e WHERE e.Amtsbezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Titel */
	public static final String QUERY_BY_TITEL = "SELECT e FROM MigrationDTOLehrer e WHERE e.Titel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Titel */
	public static final String QUERY_LIST_BY_TITEL = "SELECT e FROM MigrationDTOLehrer e WHERE e.Titel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Faecher */
	public static final String QUERY_BY_FAECHER = "SELECT e FROM MigrationDTOLehrer e WHERE e.Faecher = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Faecher */
	public static final String QUERY_LIST_BY_FAECHER = "SELECT e FROM MigrationDTOLehrer e WHERE e.Faecher IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes identNrTeil1 */
	public static final String QUERY_BY_IDENTNRTEIL1 = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes identNrTeil1 */
	public static final String QUERY_LIST_BY_IDENTNRTEIL1 = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes identNrTeil2SerNr */
	public static final String QUERY_BY_IDENTNRTEIL2SERNR = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil2SerNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes identNrTeil2SerNr */
	public static final String QUERY_LIST_BY_IDENTNRTEIL2SERNR = "SELECT e FROM MigrationDTOLehrer e WHERE e.identNrTeil2SerNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PANr */
	public static final String QUERY_BY_PANR = "SELECT e FROM MigrationDTOLehrer e WHERE e.PANr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PANr */
	public static final String QUERY_LIST_BY_PANR = "SELECT e FROM MigrationDTOLehrer e WHERE e.PANr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes personalNrLBV */
	public static final String QUERY_BY_PERSONALNRLBV = "SELECT e FROM MigrationDTOLehrer e WHERE e.personalNrLBV = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes personalNrLBV */
	public static final String QUERY_LIST_BY_PERSONALNRLBV = "SELECT e FROM MigrationDTOLehrer e WHERE e.personalNrLBV IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes verguetungsSchluessel */
	public static final String QUERY_BY_VERGUETUNGSSCHLUESSEL = "SELECT e FROM MigrationDTOLehrer e WHERE e.verguetungsSchluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes verguetungsSchluessel */
	public static final String QUERY_LIST_BY_VERGUETUNGSSCHLUESSEL = "SELECT e FROM MigrationDTOLehrer e WHERE e.verguetungsSchluessel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumZugang */
	public static final String QUERY_BY_DATUMZUGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumZugang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumZugang */
	public static final String QUERY_LIST_BY_DATUMZUGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumZugang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GrundZugang */
	public static final String QUERY_BY_GRUNDZUGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundZugang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GrundZugang */
	public static final String QUERY_LIST_BY_GRUNDZUGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundZugang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumAbgang */
	public static final String QUERY_BY_DATUMABGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumAbgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumAbgang */
	public static final String QUERY_LIST_BY_DATUMABGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.DatumAbgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GrundAbgang */
	public static final String QUERY_BY_GRUNDABGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundAbgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GrundAbgang */
	public static final String QUERY_LIST_BY_GRUNDABGANG = "SELECT e FROM MigrationDTOLehrer e WHERE e.GrundAbgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KennwortTools */
	public static final String QUERY_BY_KENNWORTTOOLS = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortTools = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KennwortTools */
	public static final String QUERY_LIST_BY_KENNWORTTOOLS = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortTools IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KennwortToolsAktuell */
	public static final String QUERY_BY_KENNWORTTOOLSAKTUELL = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortToolsAktuell = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KennwortToolsAktuell */
	public static final String QUERY_LIST_BY_KENNWORTTOOLSAKTUELL = "SELECT e FROM MigrationDTOLehrer e WHERE e.KennwortToolsAktuell IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LPassword */
	public static final String QUERY_BY_LPASSWORD = "SELECT e FROM MigrationDTOLehrer e WHERE e.LPassword = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LPassword */
	public static final String QUERY_LIST_BY_LPASSWORD = "SELECT e FROM MigrationDTOLehrer e WHERE e.LPassword IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PWAktuell */
	public static final String QUERY_BY_PWAKTUELL = "SELECT e FROM MigrationDTOLehrer e WHERE e.PWAktuell = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PWAktuell */
	public static final String QUERY_LIST_BY_PWAKTUELL = "SELECT e FROM MigrationDTOLehrer e WHERE e.PWAktuell IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PflichtstdSoll */
	public static final String QUERY_BY_PFLICHTSTDSOLL = "SELECT e FROM MigrationDTOLehrer e WHERE e.PflichtstdSoll = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PflichtstdSoll */
	public static final String QUERY_LIST_BY_PFLICHTSTDSOLL = "SELECT e FROM MigrationDTOLehrer e WHERE e.PflichtstdSoll IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Rechtsverhaeltnis */
	public static final String QUERY_BY_RECHTSVERHAELTNIS = "SELECT e FROM MigrationDTOLehrer e WHERE e.Rechtsverhaeltnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Rechtsverhaeltnis */
	public static final String QUERY_LIST_BY_RECHTSVERHAELTNIS = "SELECT e FROM MigrationDTOLehrer e WHERE e.Rechtsverhaeltnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschaeftigungsart */
	public static final String QUERY_BY_BESCHAEFTIGUNGSART = "SELECT e FROM MigrationDTOLehrer e WHERE e.Beschaeftigungsart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschaeftigungsart */
	public static final String QUERY_LIST_BY_BESCHAEFTIGUNGSART = "SELECT e FROM MigrationDTOLehrer e WHERE e.Beschaeftigungsart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Einsatzstatus */
	public static final String QUERY_BY_EINSATZSTATUS = "SELECT e FROM MigrationDTOLehrer e WHERE e.Einsatzstatus = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Einsatzstatus */
	public static final String QUERY_LIST_BY_EINSATZSTATUS = "SELECT e FROM MigrationDTOLehrer e WHERE e.Einsatzstatus IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StammschulNr */
	public static final String QUERY_BY_STAMMSCHULNR = "SELECT e FROM MigrationDTOLehrer e WHERE e.StammschulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StammschulNr */
	public static final String QUERY_LIST_BY_STAMMSCHULNR = "SELECT e FROM MigrationDTOLehrer e WHERE e.StammschulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes UnterrichtsStd */
	public static final String QUERY_BY_UNTERRICHTSSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.UnterrichtsStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes UnterrichtsStd */
	public static final String QUERY_LIST_BY_UNTERRICHTSSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.UnterrichtsStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MehrleistungStd */
	public static final String QUERY_BY_MEHRLEISTUNGSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.MehrleistungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MehrleistungStd */
	public static final String QUERY_LIST_BY_MEHRLEISTUNGSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.MehrleistungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EntlastungStd */
	public static final String QUERY_BY_ENTLASTUNGSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.EntlastungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EntlastungStd */
	public static final String QUERY_LIST_BY_ENTLASTUNGSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.EntlastungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnrechnungStd */
	public static final String QUERY_BY_ANRECHNUNGSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.AnrechnungStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnrechnungStd */
	public static final String QUERY_LIST_BY_ANRECHNUNGSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.AnrechnungStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RestStd */
	public static final String QUERY_BY_RESTSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.RestStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RestStd */
	public static final String QUERY_LIST_BY_RESTSTD = "SELECT e FROM MigrationDTOLehrer e WHERE e.RestStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchILDweb_FL */
	public static final String QUERY_BY_SCHILDWEB_FL = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_FL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchILDweb_FL */
	public static final String QUERY_LIST_BY_SCHILDWEB_FL = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_FL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchILDweb_KL */
	public static final String QUERY_BY_SCHILDWEB_KL = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_KL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchILDweb_KL */
	public static final String QUERY_LIST_BY_SCHILDWEB_KL = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_KL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchILDweb_Config */
	public static final String QUERY_BY_SCHILDWEB_CONFIG = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_Config = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchILDweb_Config */
	public static final String QUERY_LIST_BY_SCHILDWEB_CONFIG = "SELECT e FROM MigrationDTOLehrer e WHERE e.SchILDweb_Config IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Antwort1 */
	public static final String QUERY_BY_ANTWORT1 = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Antwort1 */
	public static final String QUERY_LIST_BY_ANTWORT1 = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Antwort2 */
	public static final String QUERY_BY_ANTWORT2 = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Antwort2 */
	public static final String QUERY_LIST_BY_ANTWORT2 = "SELECT e FROM MigrationDTOLehrer e WHERE e.Antwort2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes XNMPassword */
	public static final String QUERY_BY_XNMPASSWORD = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes XNMPassword */
	public static final String QUERY_LIST_BY_XNMPASSWORD = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes XNMPassword2 */
	public static final String QUERY_BY_XNMPASSWORD2 = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes XNMPassword2 */
	public static final String QUERY_LIST_BY_XNMPASSWORD2 = "SELECT e FROM MigrationDTOLehrer e WHERE e.XNMPassword2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM MigrationDTOLehrer e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM MigrationDTOLehrer e WHERE e.CredentialID IN ?1";

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

	/** Der Vorname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
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

	/** Wird für das GS-Modul genutzt (gehashed). */
	@Column(name = "KennwortTools")
	@JsonProperty
	public String KennwortTools;

	/** Gibt an ob das LPassword geändert wurde oder ob es noch das Initialkennwort ist */
	@Column(name = "KennwortToolsAktuell")
	@JsonProperty
	public String KennwortToolsAktuell;

	/** DEPRECATED: Wird für das GS-Modul genutzt (gehashed). */
	@Column(name = "LPassword")
	@JsonProperty
	public String LPassword;

	/** DEPRECATED: Gibt an ob das LPassword geändert wurde oder ob es noch das Initialkennwort ist */
	@Column(name = "PWAktuell")
	@JsonProperty
	public String PWAktuell;

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

	/** [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) */
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
	@Column(name = "Antwort1")
	@JsonProperty
	public String Antwort1;

	/** DEPRECATED: wurde nie verwendet */
	@Column(name = "Antwort2")
	@JsonProperty
	public String Antwort2;

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
		return "MigrationDTOLehrer(ID=" + this.ID + ", GU_ID=" + this.GU_ID + ", Kuerzel=" + this.Kuerzel + ", kuerzelLID=" + this.kuerzelLID + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", PersonTyp=" + this.PersonTyp + ", SchulnrEigner=" + this.SchulnrEigner + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", FuerExport=" + this.FuerExport + ", statistikRelevant=" + this.statistikRelevant + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", PLZ=" + this.PLZ + ", Ortsteil_ID=" + this.Ortsteil_ID + ", telefon=" + this.telefon + ", telefonMobil=" + this.telefonMobil + ", eMailPrivat=" + this.eMailPrivat + ", eMailDienstlich=" + this.eMailDienstlich + ", staatsangehoerigkeit=" + this.staatsangehoerigkeit + ", Geburtsdatum=" + this.Geburtsdatum + ", Geschlecht=" + this.Geschlecht + ", Anrede=" + this.Anrede + ", Amtsbezeichnung=" + this.Amtsbezeichnung + ", Titel=" + this.Titel + ", Faecher=" + this.Faecher + ", identNrTeil1=" + this.identNrTeil1 + ", identNrTeil2SerNr=" + this.identNrTeil2SerNr + ", PANr=" + this.PANr + ", personalNrLBV=" + this.personalNrLBV + ", verguetungsSchluessel=" + this.verguetungsSchluessel + ", DatumZugang=" + this.DatumZugang + ", GrundZugang=" + this.GrundZugang + ", DatumAbgang=" + this.DatumAbgang + ", GrundAbgang=" + this.GrundAbgang + ", KennwortTools=" + this.KennwortTools + ", KennwortToolsAktuell=" + this.KennwortToolsAktuell + ", LPassword=" + this.LPassword + ", PWAktuell=" + this.PWAktuell + ", PflichtstdSoll=" + this.PflichtstdSoll + ", Rechtsverhaeltnis=" + this.Rechtsverhaeltnis + ", Beschaeftigungsart=" + this.Beschaeftigungsart + ", Einsatzstatus=" + this.Einsatzstatus + ", StammschulNr=" + this.StammschulNr + ", UnterrichtsStd=" + this.UnterrichtsStd + ", MehrleistungStd=" + this.MehrleistungStd + ", EntlastungStd=" + this.EntlastungStd + ", AnrechnungStd=" + this.AnrechnungStd + ", RestStd=" + this.RestStd + ", SchILDweb_FL=" + this.SchILDweb_FL + ", SchILDweb_KL=" + this.SchILDweb_KL + ", SchILDweb_Config=" + this.SchILDweb_Config + ", Antwort1=" + this.Antwort1 + ", Antwort2=" + this.Antwort2 + ", XNMPassword=" + this.XNMPassword + ", XNMPassword2=" + this.XNMPassword2 + ", CredentialID=" + this.CredentialID + ")";
	}

}
