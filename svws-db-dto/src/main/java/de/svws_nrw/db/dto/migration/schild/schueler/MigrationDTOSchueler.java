package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schueler")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "GU_ID", "SrcID", "IDext", "Status", "Nachname", "Vorname", "AlleVornamen", "Geburtsname", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Ort_ID", "PLZ", "OrtAbk", "Ortsteil_ID", "Telefon", "Email", "Fax", "Klassen_ID", "JahrgangSchueler", "PruefOrdnung", "Geburtsdatum", "Geburtsort", "Volljaehrig", "Geschlecht", "StaatKrz", "StaatKrz2", "StaatAbk", "Aussiedler", "Religion_ID", "ReligionAbk", "Religionsabmeldung", "Religionsanmeldung", "Bafoeg", "Schwerbehinderung", "Sportbefreiung_ID", "Fahrschueler_ID", "Haltestelle_ID", "HaltestelleAbk", "Schulgliederung", "Jahrgang_ID", "ASDJahrgang", "Fachklasse_ID", "SchulpflichtErf", "Aufnahmedatum", "Einschulungsjahr", "Einschulungsart_ID", "LSSchulNr", "LSSchulformSIM", "LSJahrgang", "LSSchulEntlassDatum", "LSVersetzung", "LSFachklKennung", "LSFachklSIM", "LSEntlassgrund", "LSEntlassArt", "LSKlassenart", "LSRefPaed", "Entlassjahrgang", "Entlassjahrgang_ID", "Entlassdatum", "Entlassgrund", "Entlassart", "SchulwechselNr", "Schulwechseldatum", "Geloescht", "Gesperrt", "ModifiziertAm", "ModifiziertVon", "Markiert", "FotoVorhanden", "JVA", "RefPaed", "KeineAuskunft", "Beruf", "AbschlussDatum", "Bemerkungen", "BeginnBildungsgang", "OrgFormKrz", "Klassenart", "DurchschnittsNote", "LSSGL", "LSSchulform", "KonfDruck", "DSN_Text", "Berufsabschluss", "Schwerpunkt_ID", "LSSGL_SIM", "BerufsschulpflErf", "StatusNSJ", "FachklasseNSJ_ID", "BuchKonto", "VerkehrsspracheFamilie", "JahrZuzug", "DauerKindergartenbesuch", "VerpflichtungSprachfoerderkurs", "TeilnahmeSprachfoerderkurs", "SchulbuchgeldBefreit", "Autist", "GeburtslandSchueler", "GeburtslandVater", "GeburtslandMutter", "Uebergangsempfehlung_JG5", "ErsteSchulform_SI", "JahrWechsel_SI", "JahrWechsel_SII", "Migrationshintergrund", "ExterneSchulNr", "Kindergarten_ID", "LetzterBerufsAbschluss", "LetzterAllgAbschluss", "Land", "Duplikat", "EinschulungsartASD", "SchulnrEigner", "BilingualerZweig", "DurchschnittsnoteFHR", "DSN_FHR_Text", "Eigenanteil", "StaatAbk2", "BKAZVO", "HatBerufsausbildung", "Ausweisnummer", "AOSF", "EPJahre", "LSBemerkung", "WechselBestaetigt", "DauerBildungsgang", "AnmeldeDatum", "MeisterBafoeg", "OnlineAnmeldung", "Dokumentenverzeichnis", "Berufsqualifikation", "ZieldifferentesLernen", "ZusatzNachname", "EndeEingliederung", "SchulEmail", "EndeAnschlussfoerderung", "MasernImpfnachweis", "Lernstandsbericht", "SprachfoerderungVon", "SprachfoerderungBis", "EntlassungBemerkung", "CredentialID", "AktSchuljahr", "AktAbschnitt", "Klasse", "NeuZugewandert"})
public final class MigrationDTOSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SrcID */
	public static final String QUERY_BY_SRCID = "SELECT e FROM MigrationDTOSchueler e WHERE e.SrcID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SrcID */
	public static final String QUERY_LIST_BY_SRCID = "SELECT e FROM MigrationDTOSchueler e WHERE e.SrcID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IDext */
	public static final String QUERY_BY_IDEXT = "SELECT e FROM MigrationDTOSchueler e WHERE e.IDext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IDext */
	public static final String QUERY_LIST_BY_IDEXT = "SELECT e FROM MigrationDTOSchueler e WHERE e.IDext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM MigrationDTOSchueler e WHERE e.Status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM MigrationDTOSchueler e WHERE e.Status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nachname */
	public static final String QUERY_BY_NACHNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Nachname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nachname */
	public static final String QUERY_LIST_BY_NACHNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Nachname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname */
	public static final String QUERY_BY_VORNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Vorname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname */
	public static final String QUERY_LIST_BY_VORNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Vorname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AlleVornamen */
	public static final String QUERY_BY_ALLEVORNAMEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.AlleVornamen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AlleVornamen */
	public static final String QUERY_LIST_BY_ALLEVORNAMEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.AlleVornamen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsname */
	public static final String QUERY_BY_GEBURTSNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsname */
	public static final String QUERY_LIST_BY_GEBURTSNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strasse */
	public static final String QUERY_BY_STRASSE = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strasse */
	public static final String QUERY_LIST_BY_STRASSE = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort_ID */
	public static final String QUERY_BY_ORT_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ort_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort_ID */
	public static final String QUERY_LIST_BY_ORT_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ort_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OrtAbk */
	public static final String QUERY_BY_ORTABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrtAbk = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OrtAbk */
	public static final String QUERY_LIST_BY_ORTABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrtAbk IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ortsteil_ID */
	public static final String QUERY_BY_ORTSTEIL_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ortsteil_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ortsteil_ID */
	public static final String QUERY_LIST_BY_ORTSTEIL_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ortsteil_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefon */
	public static final String QUERY_BY_TELEFON = "SELECT e FROM MigrationDTOSchueler e WHERE e.Telefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefon */
	public static final String QUERY_LIST_BY_TELEFON = "SELECT e FROM MigrationDTOSchueler e WHERE e.Telefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM MigrationDTOSchueler e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM MigrationDTOSchueler e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fax */
	public static final String QUERY_BY_FAX = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fax = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fax */
	public static final String QUERY_LIST_BY_FAX = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fax IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassen_ID */
	public static final String QUERY_BY_KLASSEN_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassen_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassen_ID */
	public static final String QUERY_LIST_BY_KLASSEN_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassen_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrgangSchueler */
	public static final String QUERY_BY_JAHRGANGSCHUELER = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrgangSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrgangSchueler */
	public static final String QUERY_LIST_BY_JAHRGANGSCHUELER = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrgangSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefOrdnung */
	public static final String QUERY_BY_PRUEFORDNUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.PruefOrdnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefOrdnung */
	public static final String QUERY_LIST_BY_PRUEFORDNUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.PruefOrdnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsdatum */
	public static final String QUERY_BY_GEBURTSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsdatum */
	public static final String QUERY_LIST_BY_GEBURTSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsort */
	public static final String QUERY_BY_GEBURTSORT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsort */
	public static final String QUERY_LIST_BY_GEBURTSORT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Volljaehrig */
	public static final String QUERY_BY_VOLLJAEHRIG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Volljaehrig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Volljaehrig */
	public static final String QUERY_LIST_BY_VOLLJAEHRIG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Volljaehrig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geschlecht */
	public static final String QUERY_BY_GESCHLECHT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geschlecht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geschlecht */
	public static final String QUERY_LIST_BY_GESCHLECHT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geschlecht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StaatKrz */
	public static final String QUERY_BY_STAATKRZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StaatKrz */
	public static final String QUERY_LIST_BY_STAATKRZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StaatKrz2 */
	public static final String QUERY_BY_STAATKRZ2 = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StaatKrz2 */
	public static final String QUERY_LIST_BY_STAATKRZ2 = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StaatAbk */
	public static final String QUERY_BY_STAATABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StaatAbk */
	public static final String QUERY_LIST_BY_STAATABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aussiedler */
	public static final String QUERY_BY_AUSSIEDLER = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aussiedler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aussiedler */
	public static final String QUERY_LIST_BY_AUSSIEDLER = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aussiedler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Religion_ID */
	public static final String QUERY_BY_RELIGION_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religion_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Religion_ID */
	public static final String QUERY_LIST_BY_RELIGION_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religion_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ReligionAbk */
	public static final String QUERY_BY_RELIGIONABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.ReligionAbk = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ReligionAbk */
	public static final String QUERY_LIST_BY_RELIGIONABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.ReligionAbk IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Religionsabmeldung */
	public static final String QUERY_BY_RELIGIONSABMELDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsabmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Religionsabmeldung */
	public static final String QUERY_LIST_BY_RELIGIONSABMELDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsabmeldung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Religionsanmeldung */
	public static final String QUERY_BY_RELIGIONSANMELDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsanmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Religionsanmeldung */
	public static final String QUERY_LIST_BY_RELIGIONSANMELDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsanmeldung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bafoeg */
	public static final String QUERY_BY_BAFOEG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bafoeg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bafoeg */
	public static final String QUERY_LIST_BY_BAFOEG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bafoeg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schwerbehinderung */
	public static final String QUERY_BY_SCHWERBEHINDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerbehinderung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schwerbehinderung */
	public static final String QUERY_LIST_BY_SCHWERBEHINDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerbehinderung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sportbefreiung_ID */
	public static final String QUERY_BY_SPORTBEFREIUNG_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Sportbefreiung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sportbefreiung_ID */
	public static final String QUERY_LIST_BY_SPORTBEFREIUNG_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Sportbefreiung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fahrschueler_ID */
	public static final String QUERY_BY_FAHRSCHUELER_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fahrschueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fahrschueler_ID */
	public static final String QUERY_LIST_BY_FAHRSCHUELER_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fahrschueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Haltestelle_ID */
	public static final String QUERY_BY_HALTESTELLE_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Haltestelle_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Haltestelle_ID */
	public static final String QUERY_LIST_BY_HALTESTELLE_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Haltestelle_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HaltestelleAbk */
	public static final String QUERY_BY_HALTESTELLEABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.HaltestelleAbk = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HaltestelleAbk */
	public static final String QUERY_LIST_BY_HALTESTELLEABK = "SELECT e FROM MigrationDTOSchueler e WHERE e.HaltestelleAbk IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulgliederung */
	public static final String QUERY_BY_SCHULGLIEDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulgliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulgliederung */
	public static final String QUERY_LIST_BY_SCHULGLIEDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulgliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulpflichtErf */
	public static final String QUERY_BY_SCHULPFLICHTERF = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulpflichtErf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulpflichtErf */
	public static final String QUERY_LIST_BY_SCHULPFLICHTERF = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulpflichtErf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aufnahmedatum */
	public static final String QUERY_BY_AUFNAHMEDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aufnahmedatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aufnahmedatum */
	public static final String QUERY_LIST_BY_AUFNAHMEDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aufnahmedatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Einschulungsjahr */
	public static final String QUERY_BY_EINSCHULUNGSJAHR = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsjahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Einschulungsjahr */
	public static final String QUERY_LIST_BY_EINSCHULUNGSJAHR = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsjahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Einschulungsart_ID */
	public static final String QUERY_BY_EINSCHULUNGSART_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsart_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Einschulungsart_ID */
	public static final String QUERY_LIST_BY_EINSCHULUNGSART_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsart_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulNr */
	public static final String QUERY_BY_LSSCHULNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulNr */
	public static final String QUERY_LIST_BY_LSSCHULNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulformSIM */
	public static final String QUERY_BY_LSSCHULFORMSIM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulformSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulformSIM */
	public static final String QUERY_LIST_BY_LSSCHULFORMSIM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulformSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSJahrgang */
	public static final String QUERY_BY_LSJAHRGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSJahrgang */
	public static final String QUERY_LIST_BY_LSJAHRGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulEntlassDatum */
	public static final String QUERY_BY_LSSCHULENTLASSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulEntlassDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulEntlassDatum */
	public static final String QUERY_LIST_BY_LSSCHULENTLASSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulEntlassDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSVersetzung */
	public static final String QUERY_BY_LSVERSETZUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSVersetzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSVersetzung */
	public static final String QUERY_LIST_BY_LSVERSETZUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSVersetzung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSFachklKennung */
	public static final String QUERY_BY_LSFACHKLKENNUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklKennung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSFachklKennung */
	public static final String QUERY_LIST_BY_LSFACHKLKENNUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklKennung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSFachklSIM */
	public static final String QUERY_BY_LSFACHKLSIM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSFachklSIM */
	public static final String QUERY_LIST_BY_LSFACHKLSIM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSEntlassgrund */
	public static final String QUERY_BY_LSENTLASSGRUND = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSEntlassgrund */
	public static final String QUERY_LIST_BY_LSENTLASSGRUND = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassgrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSEntlassArt */
	public static final String QUERY_BY_LSENTLASSART = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSEntlassArt */
	public static final String QUERY_LIST_BY_LSENTLASSART = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSKlassenart */
	public static final String QUERY_BY_LSKLASSENART = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSKlassenart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSKlassenart */
	public static final String QUERY_LIST_BY_LSKLASSENART = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSKlassenart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSRefPaed */
	public static final String QUERY_BY_LSREFPAED = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSRefPaed = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSRefPaed */
	public static final String QUERY_LIST_BY_LSREFPAED = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSRefPaed IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassjahrgang */
	public static final String QUERY_BY_ENTLASSJAHRGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassjahrgang */
	public static final String QUERY_LIST_BY_ENTLASSJAHRGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassjahrgang_ID */
	public static final String QUERY_BY_ENTLASSJAHRGANG_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassjahrgang_ID */
	public static final String QUERY_LIST_BY_ENTLASSJAHRGANG_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassdatum */
	public static final String QUERY_BY_ENTLASSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassdatum */
	public static final String QUERY_LIST_BY_ENTLASSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassgrund */
	public static final String QUERY_BY_ENTLASSGRUND = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassgrund */
	public static final String QUERY_LIST_BY_ENTLASSGRUND = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassgrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassart */
	public static final String QUERY_BY_ENTLASSART = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassart */
	public static final String QUERY_LIST_BY_ENTLASSART = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulwechselNr */
	public static final String QUERY_BY_SCHULWECHSELNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulwechselNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulwechselNr */
	public static final String QUERY_LIST_BY_SCHULWECHSELNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulwechselNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulwechseldatum */
	public static final String QUERY_BY_SCHULWECHSELDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulwechseldatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulwechseldatum */
	public static final String QUERY_LIST_BY_SCHULWECHSELDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulwechseldatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geloescht */
	public static final String QUERY_BY_GELOESCHT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geloescht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geloescht */
	public static final String QUERY_LIST_BY_GELOESCHT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geloescht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gesperrt */
	public static final String QUERY_BY_GESPERRT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Gesperrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gesperrt */
	public static final String QUERY_LIST_BY_GESPERRT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Gesperrt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ModifiziertAm */
	public static final String QUERY_BY_MODIFIZIERTAM = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertAm = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ModifiziertAm */
	public static final String QUERY_LIST_BY_MODIFIZIERTAM = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertAm IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ModifiziertVon */
	public static final String QUERY_BY_MODIFIZIERTVON = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ModifiziertVon */
	public static final String QUERY_LIST_BY_MODIFIZIERTVON = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Markiert */
	public static final String QUERY_BY_MARKIERT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Markiert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Markiert */
	public static final String QUERY_LIST_BY_MARKIERT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Markiert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FotoVorhanden */
	public static final String QUERY_BY_FOTOVORHANDEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.FotoVorhanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FotoVorhanden */
	public static final String QUERY_LIST_BY_FOTOVORHANDEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.FotoVorhanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JVA */
	public static final String QUERY_BY_JVA = "SELECT e FROM MigrationDTOSchueler e WHERE e.JVA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JVA */
	public static final String QUERY_LIST_BY_JVA = "SELECT e FROM MigrationDTOSchueler e WHERE e.JVA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RefPaed */
	public static final String QUERY_BY_REFPAED = "SELECT e FROM MigrationDTOSchueler e WHERE e.RefPaed = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RefPaed */
	public static final String QUERY_LIST_BY_REFPAED = "SELECT e FROM MigrationDTOSchueler e WHERE e.RefPaed IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KeineAuskunft */
	public static final String QUERY_BY_KEINEAUSKUNFT = "SELECT e FROM MigrationDTOSchueler e WHERE e.KeineAuskunft = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KeineAuskunft */
	public static final String QUERY_LIST_BY_KEINEAUSKUNFT = "SELECT e FROM MigrationDTOSchueler e WHERE e.KeineAuskunft IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beruf */
	public static final String QUERY_BY_BERUF = "SELECT e FROM MigrationDTOSchueler e WHERE e.Beruf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beruf */
	public static final String QUERY_LIST_BY_BERUF = "SELECT e FROM MigrationDTOSchueler e WHERE e.Beruf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschlussDatum */
	public static final String QUERY_BY_ABSCHLUSSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.AbschlussDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschlussDatum */
	public static final String QUERY_LIST_BY_ABSCHLUSSDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.AbschlussDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BeginnBildungsgang */
	public static final String QUERY_BY_BEGINNBILDUNGSGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.BeginnBildungsgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BeginnBildungsgang */
	public static final String QUERY_LIST_BY_BEGINNBILDUNGSGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.BeginnBildungsgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OrgFormKrz */
	public static final String QUERY_BY_ORGFORMKRZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrgFormKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OrgFormKrz */
	public static final String QUERY_LIST_BY_ORGFORMKRZ = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrgFormKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassenart */
	public static final String QUERY_BY_KLASSENART = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassenart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassenart */
	public static final String QUERY_LIST_BY_KLASSENART = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassenart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DurchschnittsNote */
	public static final String QUERY_BY_DURCHSCHNITTSNOTE = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsNote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DurchschnittsNote */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTE = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsNote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSGL */
	public static final String QUERY_BY_LSSGL = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSGL */
	public static final String QUERY_LIST_BY_LSSGL = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulform */
	public static final String QUERY_BY_LSSCHULFORM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulform = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulform */
	public static final String QUERY_LIST_BY_LSSCHULFORM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulform IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KonfDruck */
	public static final String QUERY_BY_KONFDRUCK = "SELECT e FROM MigrationDTOSchueler e WHERE e.KonfDruck = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KonfDruck */
	public static final String QUERY_LIST_BY_KONFDRUCK = "SELECT e FROM MigrationDTOSchueler e WHERE e.KonfDruck IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DSN_Text */
	public static final String QUERY_BY_DSN_TEXT = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_Text = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DSN_Text */
	public static final String QUERY_LIST_BY_DSN_TEXT = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_Text IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Berufsabschluss */
	public static final String QUERY_BY_BERUFSABSCHLUSS = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsabschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Berufsabschluss */
	public static final String QUERY_LIST_BY_BERUFSABSCHLUSS = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsabschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schwerpunkt_ID */
	public static final String QUERY_BY_SCHWERPUNKT_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerpunkt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schwerpunkt_ID */
	public static final String QUERY_LIST_BY_SCHWERPUNKT_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerpunkt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSGL_SIM */
	public static final String QUERY_BY_LSSGL_SIM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL_SIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSGL_SIM */
	public static final String QUERY_LIST_BY_LSSGL_SIM = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL_SIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BerufsschulpflErf */
	public static final String QUERY_BY_BERUFSSCHULPFLERF = "SELECT e FROM MigrationDTOSchueler e WHERE e.BerufsschulpflErf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BerufsschulpflErf */
	public static final String QUERY_LIST_BY_BERUFSSCHULPFLERF = "SELECT e FROM MigrationDTOSchueler e WHERE e.BerufsschulpflErf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StatusNSJ */
	public static final String QUERY_BY_STATUSNSJ = "SELECT e FROM MigrationDTOSchueler e WHERE e.StatusNSJ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StatusNSJ */
	public static final String QUERY_LIST_BY_STATUSNSJ = "SELECT e FROM MigrationDTOSchueler e WHERE e.StatusNSJ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachklasseNSJ_ID */
	public static final String QUERY_BY_FACHKLASSENSJ_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.FachklasseNSJ_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachklasseNSJ_ID */
	public static final String QUERY_LIST_BY_FACHKLASSENSJ_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.FachklasseNSJ_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BuchKonto */
	public static final String QUERY_BY_BUCHKONTO = "SELECT e FROM MigrationDTOSchueler e WHERE e.BuchKonto = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BuchKonto */
	public static final String QUERY_LIST_BY_BUCHKONTO = "SELECT e FROM MigrationDTOSchueler e WHERE e.BuchKonto IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VerkehrsspracheFamilie */
	public static final String QUERY_BY_VERKEHRSSPRACHEFAMILIE = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerkehrsspracheFamilie = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VerkehrsspracheFamilie */
	public static final String QUERY_LIST_BY_VERKEHRSSPRACHEFAMILIE = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerkehrsspracheFamilie IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrZuzug */
	public static final String QUERY_BY_JAHRZUZUG = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrZuzug = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrZuzug */
	public static final String QUERY_LIST_BY_JAHRZUZUG = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrZuzug IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DauerKindergartenbesuch */
	public static final String QUERY_BY_DAUERKINDERGARTENBESUCH = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerKindergartenbesuch = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DauerKindergartenbesuch */
	public static final String QUERY_LIST_BY_DAUERKINDERGARTENBESUCH = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerKindergartenbesuch IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VerpflichtungSprachfoerderkurs */
	public static final String QUERY_BY_VERPFLICHTUNGSPRACHFOERDERKURS = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VerpflichtungSprachfoerderkurs */
	public static final String QUERY_LIST_BY_VERPFLICHTUNGSPRACHFOERDERKURS = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TeilnahmeSprachfoerderkurs */
	public static final String QUERY_BY_TEILNAHMESPRACHFOERDERKURS = "SELECT e FROM MigrationDTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TeilnahmeSprachfoerderkurs */
	public static final String QUERY_LIST_BY_TEILNAHMESPRACHFOERDERKURS = "SELECT e FROM MigrationDTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulbuchgeldBefreit */
	public static final String QUERY_BY_SCHULBUCHGELDBEFREIT = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulbuchgeldBefreit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulbuchgeldBefreit */
	public static final String QUERY_LIST_BY_SCHULBUCHGELDBEFREIT = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulbuchgeldBefreit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Autist */
	public static final String QUERY_BY_AUTIST = "SELECT e FROM MigrationDTOSchueler e WHERE e.Autist = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Autist */
	public static final String QUERY_LIST_BY_AUTIST = "SELECT e FROM MigrationDTOSchueler e WHERE e.Autist IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeburtslandSchueler */
	public static final String QUERY_BY_GEBURTSLANDSCHUELER = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeburtslandSchueler */
	public static final String QUERY_LIST_BY_GEBURTSLANDSCHUELER = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeburtslandVater */
	public static final String QUERY_BY_GEBURTSLANDVATER = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandVater = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeburtslandVater */
	public static final String QUERY_LIST_BY_GEBURTSLANDVATER = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandVater IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeburtslandMutter */
	public static final String QUERY_BY_GEBURTSLANDMUTTER = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandMutter = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeburtslandMutter */
	public static final String QUERY_LIST_BY_GEBURTSLANDMUTTER = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandMutter IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Uebergangsempfehlung_JG5 */
	public static final String QUERY_BY_UEBERGANGSEMPFEHLUNG_JG5 = "SELECT e FROM MigrationDTOSchueler e WHERE e.Uebergangsempfehlung_JG5 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Uebergangsempfehlung_JG5 */
	public static final String QUERY_LIST_BY_UEBERGANGSEMPFEHLUNG_JG5 = "SELECT e FROM MigrationDTOSchueler e WHERE e.Uebergangsempfehlung_JG5 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErsteSchulform_SI */
	public static final String QUERY_BY_ERSTESCHULFORM_SI = "SELECT e FROM MigrationDTOSchueler e WHERE e.ErsteSchulform_SI = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErsteSchulform_SI */
	public static final String QUERY_LIST_BY_ERSTESCHULFORM_SI = "SELECT e FROM MigrationDTOSchueler e WHERE e.ErsteSchulform_SI IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrWechsel_SI */
	public static final String QUERY_BY_JAHRWECHSEL_SI = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SI = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrWechsel_SI */
	public static final String QUERY_LIST_BY_JAHRWECHSEL_SI = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SI IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrWechsel_SII */
	public static final String QUERY_BY_JAHRWECHSEL_SII = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrWechsel_SII */
	public static final String QUERY_LIST_BY_JAHRWECHSEL_SII = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Migrationshintergrund */
	public static final String QUERY_BY_MIGRATIONSHINTERGRUND = "SELECT e FROM MigrationDTOSchueler e WHERE e.Migrationshintergrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Migrationshintergrund */
	public static final String QUERY_LIST_BY_MIGRATIONSHINTERGRUND = "SELECT e FROM MigrationDTOSchueler e WHERE e.Migrationshintergrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ExterneSchulNr */
	public static final String QUERY_BY_EXTERNESCHULNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.ExterneSchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ExterneSchulNr */
	public static final String QUERY_LIST_BY_EXTERNESCHULNR = "SELECT e FROM MigrationDTOSchueler e WHERE e.ExterneSchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kindergarten_ID */
	public static final String QUERY_BY_KINDERGARTEN_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Kindergarten_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kindergarten_ID */
	public static final String QUERY_LIST_BY_KINDERGARTEN_ID = "SELECT e FROM MigrationDTOSchueler e WHERE e.Kindergarten_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LetzterBerufsAbschluss */
	public static final String QUERY_BY_LETZTERBERUFSABSCHLUSS = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterBerufsAbschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LetzterBerufsAbschluss */
	public static final String QUERY_LIST_BY_LETZTERBERUFSABSCHLUSS = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterBerufsAbschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LetzterAllgAbschluss */
	public static final String QUERY_BY_LETZTERALLGABSCHLUSS = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterAllgAbschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LetzterAllgAbschluss */
	public static final String QUERY_LIST_BY_LETZTERALLGABSCHLUSS = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterAllgAbschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Land */
	public static final String QUERY_BY_LAND = "SELECT e FROM MigrationDTOSchueler e WHERE e.Land = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Land */
	public static final String QUERY_LIST_BY_LAND = "SELECT e FROM MigrationDTOSchueler e WHERE e.Land IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Duplikat */
	public static final String QUERY_BY_DUPLIKAT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Duplikat = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Duplikat */
	public static final String QUERY_LIST_BY_DUPLIKAT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Duplikat IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinschulungsartASD */
	public static final String QUERY_BY_EINSCHULUNGSARTASD = "SELECT e FROM MigrationDTOSchueler e WHERE e.EinschulungsartASD = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinschulungsartASD */
	public static final String QUERY_LIST_BY_EINSCHULUNGSARTASD = "SELECT e FROM MigrationDTOSchueler e WHERE e.EinschulungsartASD IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BilingualerZweig */
	public static final String QUERY_BY_BILINGUALERZWEIG = "SELECT e FROM MigrationDTOSchueler e WHERE e.BilingualerZweig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BilingualerZweig */
	public static final String QUERY_LIST_BY_BILINGUALERZWEIG = "SELECT e FROM MigrationDTOSchueler e WHERE e.BilingualerZweig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DurchschnittsnoteFHR */
	public static final String QUERY_BY_DURCHSCHNITTSNOTEFHR = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsnoteFHR = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DurchschnittsnoteFHR */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTEFHR = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsnoteFHR IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DSN_FHR_Text */
	public static final String QUERY_BY_DSN_FHR_TEXT = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_FHR_Text = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DSN_FHR_Text */
	public static final String QUERY_LIST_BY_DSN_FHR_TEXT = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_FHR_Text IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Eigenanteil */
	public static final String QUERY_BY_EIGENANTEIL = "SELECT e FROM MigrationDTOSchueler e WHERE e.Eigenanteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Eigenanteil */
	public static final String QUERY_LIST_BY_EIGENANTEIL = "SELECT e FROM MigrationDTOSchueler e WHERE e.Eigenanteil IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StaatAbk2 */
	public static final String QUERY_BY_STAATABK2 = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StaatAbk2 */
	public static final String QUERY_LIST_BY_STAATABK2 = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BKAZVO */
	public static final String QUERY_BY_BKAZVO = "SELECT e FROM MigrationDTOSchueler e WHERE e.BKAZVO = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BKAZVO */
	public static final String QUERY_LIST_BY_BKAZVO = "SELECT e FROM MigrationDTOSchueler e WHERE e.BKAZVO IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HatBerufsausbildung */
	public static final String QUERY_BY_HATBERUFSAUSBILDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.HatBerufsausbildung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HatBerufsausbildung */
	public static final String QUERY_LIST_BY_HATBERUFSAUSBILDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.HatBerufsausbildung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ausweisnummer */
	public static final String QUERY_BY_AUSWEISNUMMER = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ausweisnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ausweisnummer */
	public static final String QUERY_LIST_BY_AUSWEISNUMMER = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ausweisnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AOSF */
	public static final String QUERY_BY_AOSF = "SELECT e FROM MigrationDTOSchueler e WHERE e.AOSF = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AOSF */
	public static final String QUERY_LIST_BY_AOSF = "SELECT e FROM MigrationDTOSchueler e WHERE e.AOSF IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EPJahre */
	public static final String QUERY_BY_EPJAHRE = "SELECT e FROM MigrationDTOSchueler e WHERE e.EPJahre = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EPJahre */
	public static final String QUERY_LIST_BY_EPJAHRE = "SELECT e FROM MigrationDTOSchueler e WHERE e.EPJahre IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSBemerkung */
	public static final String QUERY_BY_LSBEMERKUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSBemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSBemerkung */
	public static final String QUERY_LIST_BY_LSBEMERKUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSBemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WechselBestaetigt */
	public static final String QUERY_BY_WECHSELBESTAETIGT = "SELECT e FROM MigrationDTOSchueler e WHERE e.WechselBestaetigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WechselBestaetigt */
	public static final String QUERY_LIST_BY_WECHSELBESTAETIGT = "SELECT e FROM MigrationDTOSchueler e WHERE e.WechselBestaetigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DauerBildungsgang */
	public static final String QUERY_BY_DAUERBILDUNGSGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerBildungsgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DauerBildungsgang */
	public static final String QUERY_LIST_BY_DAUERBILDUNGSGANG = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerBildungsgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnmeldeDatum */
	public static final String QUERY_BY_ANMELDEDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.AnmeldeDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnmeldeDatum */
	public static final String QUERY_LIST_BY_ANMELDEDATUM = "SELECT e FROM MigrationDTOSchueler e WHERE e.AnmeldeDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MeisterBafoeg */
	public static final String QUERY_BY_MEISTERBAFOEG = "SELECT e FROM MigrationDTOSchueler e WHERE e.MeisterBafoeg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MeisterBafoeg */
	public static final String QUERY_LIST_BY_MEISTERBAFOEG = "SELECT e FROM MigrationDTOSchueler e WHERE e.MeisterBafoeg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OnlineAnmeldung */
	public static final String QUERY_BY_ONLINEANMELDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.OnlineAnmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OnlineAnmeldung */
	public static final String QUERY_LIST_BY_ONLINEANMELDUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.OnlineAnmeldung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Dokumentenverzeichnis */
	public static final String QUERY_BY_DOKUMENTENVERZEICHNIS = "SELECT e FROM MigrationDTOSchueler e WHERE e.Dokumentenverzeichnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Dokumentenverzeichnis */
	public static final String QUERY_LIST_BY_DOKUMENTENVERZEICHNIS = "SELECT e FROM MigrationDTOSchueler e WHERE e.Dokumentenverzeichnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Berufsqualifikation */
	public static final String QUERY_BY_BERUFSQUALIFIKATION = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsqualifikation = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Berufsqualifikation */
	public static final String QUERY_LIST_BY_BERUFSQUALIFIKATION = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsqualifikation IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZieldifferentesLernen */
	public static final String QUERY_BY_ZIELDIFFERENTESLERNEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZieldifferentesLernen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZieldifferentesLernen */
	public static final String QUERY_LIST_BY_ZIELDIFFERENTESLERNEN = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZieldifferentesLernen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzNachname */
	public static final String QUERY_BY_ZUSATZNACHNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZusatzNachname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzNachname */
	public static final String QUERY_LIST_BY_ZUSATZNACHNAME = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZusatzNachname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EndeEingliederung */
	public static final String QUERY_BY_ENDEEINGLIEDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeEingliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EndeEingliederung */
	public static final String QUERY_LIST_BY_ENDEEINGLIEDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeEingliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulEmail */
	public static final String QUERY_BY_SCHULEMAIL = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulEmail = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulEmail */
	public static final String QUERY_LIST_BY_SCHULEMAIL = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulEmail IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EndeAnschlussfoerderung */
	public static final String QUERY_BY_ENDEANSCHLUSSFOERDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeAnschlussfoerderung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EndeAnschlussfoerderung */
	public static final String QUERY_LIST_BY_ENDEANSCHLUSSFOERDERUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeAnschlussfoerderung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MasernImpfnachweis */
	public static final String QUERY_BY_MASERNIMPFNACHWEIS = "SELECT e FROM MigrationDTOSchueler e WHERE e.MasernImpfnachweis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MasernImpfnachweis */
	public static final String QUERY_LIST_BY_MASERNIMPFNACHWEIS = "SELECT e FROM MigrationDTOSchueler e WHERE e.MasernImpfnachweis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernstandsbericht */
	public static final String QUERY_BY_LERNSTANDSBERICHT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Lernstandsbericht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernstandsbericht */
	public static final String QUERY_LIST_BY_LERNSTANDSBERICHT = "SELECT e FROM MigrationDTOSchueler e WHERE e.Lernstandsbericht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SprachfoerderungVon */
	public static final String QUERY_BY_SPRACHFOERDERUNGVON = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SprachfoerderungVon */
	public static final String QUERY_LIST_BY_SPRACHFOERDERUNGVON = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SprachfoerderungBis */
	public static final String QUERY_BY_SPRACHFOERDERUNGBIS = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SprachfoerderungBis */
	public static final String QUERY_LIST_BY_SPRACHFOERDERUNGBIS = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EntlassungBemerkung */
	public static final String QUERY_BY_ENTLASSUNGBEMERKUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.EntlassungBemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EntlassungBemerkung */
	public static final String QUERY_LIST_BY_ENTLASSUNGBEMERKUNG = "SELECT e FROM MigrationDTOSchueler e WHERE e.EntlassungBemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM MigrationDTOSchueler e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM MigrationDTOSchueler e WHERE e.CredentialID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AktSchuljahr */
	public static final String QUERY_BY_AKTSCHULJAHR = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktSchuljahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AktSchuljahr */
	public static final String QUERY_LIST_BY_AKTSCHULJAHR = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktSchuljahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AktAbschnitt */
	public static final String QUERY_BY_AKTABSCHNITT = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktAbschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AktAbschnitt */
	public static final String QUERY_LIST_BY_AKTABSCHNITT = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktAbschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klasse */
	public static final String QUERY_BY_KLASSE = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klasse */
	public static final String QUERY_LIST_BY_KLASSE = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NeuZugewandert */
	public static final String QUERY_BY_NEUZUGEWANDERT = "SELECT e FROM MigrationDTOSchueler e WHERE e.NeuZugewandert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NeuZugewandert */
	public static final String QUERY_LIST_BY_NEUZUGEWANDERT = "SELECT e FROM MigrationDTOSchueler e WHERE e.NeuZugewandert IN ?1";

	/** ID des Schülerdatensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** GU_ID des Schülerdatensatzes */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** DEPRECATED: Muss aber noch aus Schild2 und Schild3 entfernt werden */
	@Column(name = "SrcID")
	@JsonProperty
	public Integer SrcID;

	/** externe ID */
	@Column(name = "IDext")
	@JsonProperty
	public String IDext;

	/** Status des Schüler steuert die Einordnung in die Kästen Aktiv Neuaufnahme Abschluss usw. */
	@Column(name = "Status")
	@JsonProperty
	public Integer Status;

	/** Name des Schülers PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name")
	@JsonProperty
	public String Nachname;

	/** Vorname des Schülers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
	@Column(name = "Vorname")
	@JsonProperty
	public String Vorname;

	/** Alle gültigen Vornamen, wenn mehrere vorhanden sind. Ist nur ein Vorname vorhanden bleibt das Feld leer und Vorname wird genutzt. */
	@Column(name = "Zusatz")
	@JsonProperty
	public String AlleVornamen;

	/** Geburtsname des Schülers */
	@Column(name = "Geburtsname")
	@JsonProperty
	public String Geburtsname;

	/** Straße des Schülers */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Straßenname des Schülers */
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

	/** ID des Orts des Schülers */
	@Column(name = "Ort_ID")
	@JsonProperty
	public Long Ort_ID;

	/** DEPRECATED: PLZ des Schülers */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** DEPRECATED: Klartext des Orts des Schülers */
	@Column(name = "OrtAbk")
	@JsonProperty
	public String OrtAbk;

	/** ID des Ortsteils des Schülers */
	@Column(name = "Ortsteil_ID")
	@JsonProperty
	public Long Ortsteil_ID;

	/** Telefonnummer des Schülers */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** E-Mail-Adresse des Schülers */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Fax oder Mobilnummer des Schülers */
	@Column(name = "Fax")
	@JsonProperty
	public String Fax;

	/** DEPRECATED: ID der Klasse des Schülers - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public Long Klassen_ID;

	/** DEPRECATED: Schulbesuchsjahre - verschoben nach SchuelerLernabschittsdaten.Schulbesuchsjahre */
	@Column(name = "Jahrgang")
	@JsonProperty
	public Integer JahrgangSchueler;

	/** DEPRECATED: Prüfungsordnung des Schülers - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "PruefOrdnung")
	@JsonProperty
	public String PruefOrdnung;

	/** Geburtsdatum des Schülers */
	@Column(name = "Geburtsdatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Geburtsdatum;

	/** Geburtsort des Schülers */
	@Column(name = "Geburtsort")
	@JsonProperty
	public String Geburtsort;

	/** Gibt an ob der Schüler volljährig ist */
	@Column(name = "Volljaehrig")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Volljaehrig;

	/** Geschlecht des Schülers */
	@Column(name = "Geschlecht")
	@JsonProperty
	public Integer Geschlecht;

	/** Kürzel der 1. Staatsangehörigkeit */
	@Column(name = "StaatKrz")
	@JsonProperty
	public String StaatKrz;

	/** Kürzel der 2. Staatsangehörigkeit */
	@Column(name = "StaatKrz2")
	@JsonProperty
	public String StaatKrz2;

	/** DEPRECATED: Klartext der 1. Staatsangehörigkeit des Schülers */
	@Column(name = "StaatAbk")
	@JsonProperty
	public String StaatAbk;

	/** DEPRECATED: Gibt an ob der Schüler ausgesiedelt ist (wird nicht mehr erfasst) */
	@Column(name = "Aussiedler")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Aussiedler;

	/** ID des Religionskatalogeintrags */
	@Column(name = "Religion_ID")
	@JsonProperty
	public Long Religion_ID;

	/** Klartext des Religionseintrags */
	@Column(name = "ReligionAbk")
	@JsonProperty
	public String ReligionAbk;

	/** Abmeldetdateum vom Religionsunterricht */
	@Column(name = "Religionsabmeldung")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Religionsabmeldung;

	/** Anmeldedatum zum Religionsunterricht wenn vorher abgemeldet */
	@Column(name = "Religionsanmeldung")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Religionsanmeldung;

	/** Gibt an ob ein Schüler BAFög bezieht */
	@Column(name = "Bafoeg")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Bafoeg;

	/** DEPRECATED: Gibt an ob eine Schwerbehinderung vorliegt Ja Nein - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "Schwerbehinderung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Schwerbehinderung;

	/** ID der Sportbefreiung */
	@Column(name = "Sportbefreiung_ID")
	@JsonProperty
	public Long Sportbefreiung_ID;

	/** ID des Fahrschülereintras */
	@Column(name = "Fahrschueler_ID")
	@JsonProperty
	public Long Fahrschueler_ID;

	/** ID der Haltestelle */
	@Column(name = "Haltestelle_ID")
	@JsonProperty
	public Long Haltestelle_ID;

	/** DEPRECATED: Text der Haltestelle */
	@Column(name = "HaltestelleAbk")
	@JsonProperty
	public String HaltestelleAbk;

	/** DEPRECATED: ASD-Kürzel der Schulgliederung - Spalte fehlerhaft benannt! - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "ASDSchulform")
	@JsonProperty
	public String Schulgliederung;

	/** DEPRECATED: ID des Jahrgangs - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** DEPRECATED: ASD-Kürzel des Jahrgangs - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** DEPRECATED: ID der Fachklasse (BK) - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Gibt an ob die Vollzeitschulpflicht erfüllt ist Ja Nein */
	@Column(name = "SchulpflichtErf")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchulpflichtErf;

	/** Aufnahmedatum */
	@Column(name = "Aufnahmedatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Aufnahmedatum;

	/** Einschulungsjahr */
	@Column(name = "Einschulungsjahr")
	@JsonProperty
	public Integer Einschulungsjahr;

	/** ID der Einschlungsart IT.NRW */
	@Column(name = "Einschulungsart_ID")
	@JsonProperty
	public Long Einschulungsart_ID;

	/** letzte Schule Schulnummer */
	@Column(name = "LSSchulNr")
	@JsonProperty
	public String LSSchulNr;

	/** letzte Schule Schulgliederung */
	@Column(name = "LSSchulformSIM")
	@JsonProperty
	public String LSSchulformSIM;

	/** letzte Schule Jahrgang */
	@Column(name = "LSJahrgang")
	@JsonProperty
	public String LSJahrgang;

	/** letzte Schule Entlassdatum */
	@Column(name = "LSSchulEntlassDatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String LSSchulEntlassDatum;

	/** letzte Schule Versetzungsvermerk */
	@Column(name = "LSVersetzung")
	@JsonProperty
	public String LSVersetzung;

	/** letzte Schule Fachklassenkennung */
	@Column(name = "LSFachklKennung")
	@JsonProperty
	public String LSFachklKennung;

	/** letzte Schule Fachklassenschlüssel */
	@Column(name = "LSFachklSIM")
	@JsonProperty
	public String LSFachklSIM;

	/** letzte Schule Entlassgrund */
	@Column(name = "LSEntlassgrund")
	@JsonProperty
	public String LSEntlassgrund;

	/** letzte Schule Entlassart */
	@Column(name = "LSEntlassArt")
	@JsonProperty
	public String LSEntlassArt;

	/** letzte Schule Klassenart */
	@Column(name = "LSKlassenart")
	@JsonProperty
	public String LSKlassenart;

	/** letzte Schule Reformpädagogik */
	@Column(name = "LSRefPaed")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean LSRefPaed;

	/** Entlasung von eigener Schule Jahrgang */
	@Column(name = "Entlassjahrgang")
	@JsonProperty
	public String Entlassjahrgang;

	/** Entlasung von eigener Schule JahrgangsID */
	@Column(name = "Entlassjahrgang_ID")
	@JsonProperty
	public Long Entlassjahrgang_ID;

	/** Entlassdatum */
	@Column(name = "Entlassdatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Entlassdatum;

	/** Entlassgrund  */
	@Column(name = "Entlassgrund")
	@JsonProperty
	public String Entlassgrund;

	/** Entlassart */
	@Column(name = "Entlassart")
	@JsonProperty
	public String Entlassart;

	/** Schulnummer aufnehmende Schule */
	@Column(name = "SchulwechselNr")
	@JsonProperty
	public String SchulwechselNr;

	/** Datum des Schulwechsels */
	@Column(name = "Schulwechseldatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Schulwechseldatum;

	/** Löschmarkierung Schülerdatensatz */
	@Column(name = "Geloescht")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Geloescht;

	/** Datensatz gesperrt Ja Nein */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/** zuletzt geändert Datum */
	@Column(name = "ModifiziertAm")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String ModifiziertAm;

	/** zuletzt geändert Benutzer */
	@Column(name = "ModifiziertVon")
	@JsonProperty
	public String ModifiziertVon;

	/** Datensatz ist markiert */
	@Column(name = "Markiert")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Markiert;

	/** DEPRECATED: nicht mehr genutzt Zustimmung Foto */
	@Column(name = "FotoVorhanden")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FotoVorhanden;

	/** Ist Schüler einer Justizvollzugsanstalt */
	@Column(name = "JVA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean JVA;

	/** DEPRECATED: Teilnahme an Reformpädagogik - verschoben nach SchuelerLernabschittsdaten */
	@Column(name = "RefPaed")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RefPaed;

	/** Keine Auskunft an Dritte Ja Nein */
	@Column(name = "KeineAuskunft")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean KeineAuskunft;

	/** Berufsbezeichnung wenn der Schüler einen hat */
	@Column(name = "Beruf")
	@JsonProperty
	public String Beruf;

	/** Abschlussdatum */
	@Column(name = "AbschlussDatum")
	@JsonProperty
	public String AbschlussDatum;

	/** DEPRECATED: Text für Bemerkungen zum Schüler Memofeld */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Beginn des Bildungsgangs BK */
	@Column(name = "BeginnBildungsgang")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String BeginnBildungsgang;

	/** DEPRECATED: Kürzel der Organisationsform IT.NRW - verschoben nach SchuelerLernabschnittsdaten */
	@Column(name = "OrgFormKrz")
	@JsonProperty
	public String OrgFormKrz;

	/** DEPRECATED: Klassenart IT.NRW - verschoben nach SchuelerLernabschittsdaten  */
	@Column(name = "Klassenart")
	@JsonProperty
	public String Klassenart;

	/** Speichert die Durchschnittsnote mit einer Nachkommastelle (aber als Textfeld) Wird primär am BK benutzt */
	@Column(name = "DurchschnittsNote")
	@JsonProperty
	public String DurchschnittsNote;

	/** letzte Schule Gliederung */
	@Column(name = "LSSGL")
	@JsonProperty
	public String LSSGL;

	/** letzte Schule Schulform */
	@Column(name = "LSSchulform")
	@JsonProperty
	public String LSSchulform;

	/** Konfession aufs Zeugnis für den Druck */
	@Column(name = "KonfDruck")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean KonfDruck;

	/** Speichert die Durchschnittsnote im Klartext (also befriedigend bei einer Durchschnittsnote von z.B. 3.1) */
	@Column(name = "DSN_Text")
	@JsonProperty
	public String DSN_Text;

	/** Bezeichnung Berufsabschluss */
	@Column(name = "Berufsabschluss")
	@JsonProperty
	public String Berufsabschluss;

	/** DEPRECATED: ID des Schwerpunkt (BK und RS) - verschoben nach SchuelerLernabschnittsdaten */
	@Column(name = "Schwerpunkt_ID")
	@JsonProperty
	public Long Schwerpunkt_ID;

	/** Letzte Schule Schulgiederung (wichtig wenn BK) */
	@Column(name = "LSSGL_SIM")
	@JsonProperty
	public String LSSGL_SIM;

	/** Gibt an ob die Berufsschulpflicht erfüllt ist (Ja/Nein) */
	@Column(name = "BerufsschulpflErf")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean BerufsschulpflErf;

	/** Gibt an, welcher Status für das kommende Schuljahr geplant ist (nur BK) */
	@Column(name = "StatusNSJ")
	@JsonProperty
	public Integer StatusNSJ;

	/** Gibt an, welche Fachklasse für das kommende Schuljahr geplant ist (nur BK) */
	@Column(name = "FachklasseNSJ_ID")
	@JsonProperty
	public Long FachklasseNSJ_ID;

	/** DEPRECATED: SchildMedia */
	@Column(name = "BuchKonto")
	@JsonProperty
	public Double BuchKonto;

	/** Migrationshintergrund Verkehrssprache in der Familie */
	@Column(name = "VerkehrsspracheFamilie")
	@JsonProperty
	public String VerkehrsspracheFamilie;

	/** Migrationshintergrund Zuzugsjahr */
	@Column(name = "JahrZuzug")
	@JsonProperty
	public Integer JahrZuzug;

	/** Dauer des Kindergartenbesuchs */
	@Column(name = "DauerKindergartenbesuch")
	@JsonProperty
	public String DauerKindergartenbesuch;

	/** Wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein) */
	@Column(name = "VerpflichtungSprachfoerderkurs")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean VerpflichtungSprachfoerderkurs;

	/** Teilnahme an einen Sprachförderkurs (Ja/Nein) */
	@Column(name = "TeilnahmeSprachfoerderkurs")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean TeilnahmeSprachfoerderkurs;

	/** Vom Schulbuchgeld befreit (Ja/Nein) */
	@Column(name = "SchulbuchgeldBefreit")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchulbuchgeldBefreit;

	/** DEPRECATED: Gibt an ob Autismuss vorliegt (Ja/Nein) - verschoben nach Tabelle SchuelerLernabschnittsdaten */
	@Column(name = "Autist")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Autist;

	/** Migrationshintergrund Geburtsland des Schülers */
	@Column(name = "GeburtslandSchueler")
	@JsonProperty
	public String GeburtslandSchueler;

	/** Migrationshintergrund Geburtsland des Vaters */
	@Column(name = "GeburtslandVater")
	@JsonProperty
	public String GeburtslandVater;

	/** Migrationshintergrund Geburtsland der Mutter */
	@Column(name = "GeburtslandMutter")
	@JsonProperty
	public String GeburtslandMutter;

	/** Übergangsempfehlung für den Jahrgang 5 */
	@Column(name = "Uebergangsempfehlung_JG5")
	@JsonProperty
	public String Uebergangsempfehlung_JG5;

	/** Erste Schulform in der Sek1 */
	@Column(name = "ErsteSchulform_SI")
	@JsonProperty
	public String ErsteSchulform_SI;

	/** Jahr des Wechsels in die Sekundarstufe I */
	@Column(name = "JahrWechsel_SI")
	@JsonProperty
	public Integer JahrWechsel_SI;

	/** Jahr des Wechsels in die Sekundarstufe II */
	@Column(name = "JahrWechsel_SII")
	@JsonProperty
	public Integer JahrWechsel_SII;

	/** Migrationshintergrund vorhanden (Ja/Nein) */
	@Column(name = "Migrationshintergrund")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Migrationshintergrund;

	/** Schulnummer von externen Koopschüern */
	@Column(name = "ExterneSchulNr")
	@JsonProperty
	public String ExterneSchulNr;

	/** ID des Kinderkarteneintrags (GS) */
	@Column(name = "Kindergarten_ID")
	@JsonProperty
	public Long Kindergarten_ID;

	/** erreichter berufsbezogener Abschluss LSSchule */
	@Column(name = "LetzterBerufsAbschluss")
	@JsonProperty
	public String LetzterBerufsAbschluss;

	/** erreichter allgemeinbildender Abschluss LSSchule */
	@Column(name = "LetzterAllgAbschluss")
	@JsonProperty
	public String LetzterAllgAbschluss;

	/** Land des Wohnsitzes des Schüler (in Grenzgebieten möglich) */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/** Gibt an ob der Datensatz ein Duplikat ist */
	@Column(name = "Duplikat")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Duplikat;

	/** ASD-Kürzel Einschulungsart IT.NRW */
	@Column(name = "EinschulungsartASD")
	@JsonProperty
	public String EinschulungsartASD;

	/** DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: Sprache des Bilingualen Zweigs, verschoben nach Tabelle SchuelerLernabschnittsdaten */
	@Column(name = "BilingualerZweig")
	@JsonProperty
	public String BilingualerZweig;

	/** Speichert die Durchschnittsnote der FHR-Prüfung mit einer Nachkommastelle (aber als Textfeld) Wird nur am BK benutzt */
	@Column(name = "DurchschnittsnoteFHR")
	@JsonProperty
	public String DurchschnittsnoteFHR;

	/** Speichert die Durchschnittsnote der FHR-Prüfung im Klartext (also befriedigend bei einer Durchschnittsnote von z.B. 3.1) wird nur am BK verwendet */
	@Column(name = "DSN_FHR_Text")
	@JsonProperty
	public String DSN_FHR_Text;

	/** TODO DEPRECATED: Eigenanteil Ja/Nein  */
	@Column(name = "Eigenanteil")
	@JsonProperty
	public Double Eigenanteil;

	/** DEPRECATED: Kürzel der 2. Staatsangehörigkeit */
	@Column(name = "StaatAbk2")
	@JsonProperty
	public String StaatAbk2;

	/** Gibt an ob der Schüler in einem Bildungsgang nach BKAZVO ist (BK) */
	@Column(name = "BKAZVO")
	@JsonProperty
	public Integer BKAZVO;

	/** Gibt an ob der Schüler eine Berufsausbildung hat (BK) */
	@Column(name = "HatBerufsausbildung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean HatBerufsausbildung;

	/** Nummer des Schülerausweises */
	@Column(name = "Ausweisnummer")
	@JsonProperty
	public String Ausweisnummer;

	/** DEPRECATED Gibt an ob der Schüler ein AOSF hat - verschoben nach Tabelle SchuelerLernabschnittsdaten  */
	@Column(name = "AOSF")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AOSF;

	/** Anzahl der Jahre in der Schuleingangssphase */
	@Column(name = "EPJahre")
	@JsonProperty
	public Integer EPJahre;

	/** Bemerkung der zuletzt besuchten Schule */
	@Column(name = "LSBemerkung")
	@JsonProperty
	public String LSBemerkung;

	/** Wechsel zur aufnehmenden Schule bestätigt */
	@Column(name = "WechselBestaetigt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean WechselBestaetigt;

	/** Dauer des Bildungsgangs am BK */
	@Column(name = "DauerBildungsgang")
	@JsonProperty
	public Integer DauerBildungsgang;

	/** Anmeldedatum des Schülers */
	@Column(name = "AnmeldeDatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String AnmeldeDatum;

	/** Gibt an ob ein Schüler MeisterBafög bezieht BK */
	@Column(name = "MeisterBafoeg")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MeisterBafoeg;

	/** Schüler hat sich Online angemeldet (Ja/Nein) */
	@Column(name = "OnlineAnmeldung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean OnlineAnmeldung;

	/** Pfad zum Dokumentenverzeichnis */
	@Column(name = "Dokumentenverzeichnis")
	@JsonProperty
	public String Dokumentenverzeichnis;

	/** Karteireiter Schulbesuch Berufsausbildung vorhanden (Ja/Nein) */
	@Column(name = "Berufsqualifikation")
	@JsonProperty
	public String Berufsqualifikation;

	/** DEPRECATED: Gibt an ob der Schüler zieldifferent unterrichtet wird - verschoben nach Tabelle SchuelerLernabschnittsdaten */
	@Column(name = "ZieldifferentesLernen")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ZieldifferentesLernen;

	/** Gibt ggf. den Zusatz zum Nachnamen an. */
	@Column(name = "ZusatzNachname")
	@JsonProperty
	public String ZusatzNachname;

	/** Ende der Eingliederung bei zugezogenen Schülern (Flüchtlingen) */
	@Column(name = "EndeEingliederung")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String EndeEingliederung;

	/** schulische E-Mail-Adresse des Schülers */
	@Column(name = "SchulEmail")
	@JsonProperty
	public String SchulEmail;

	/** Ende der Anschlussförderung bei zugezogenen Schülern (Flüchtlingen) */
	@Column(name = "EndeAnschlussfoerderung")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String EndeAnschlussfoerderung;

	/** Gibt an, ob ein Nachweis über die Masern-Impfung erbracht wurde */
	@Column(name = "MasernImpfnachweis")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MasernImpfnachweis;

	/** Gibt an ob ein Schüler Sprachförderung in Deutsch (DAZ) erhält und somit Lernstandsberichte statt Zeugnisse */
	@Column(name = "Lernstandsbericht")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Lernstandsbericht;

	/** Datum des Beginns der Sprachförderung */
	@Column(name = "SprachfoerderungVon")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String SprachfoerderungVon;

	/** Datum des Endes der Sprachförderung */
	@Column(name = "SprachfoerderungBis")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String SprachfoerderungBis;

	/** Bemerkung bei Entlassung von der eigenen Schule */
	@Column(name = "EntlassungBemerkung")
	@JsonProperty
	public String EntlassungBemerkung;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/** DEPRECATED: Aktuelles Schuljahr des Schülers */
	@Column(name = "AktSchuljahr")
	@JsonProperty
	public Integer AktSchuljahr;

	/** DEPRECATED: Aktueller Abschnitt des Schülers */
	@Column(name = "AktAbschnitt")
	@JsonProperty
	public Integer AktAbschnitt;

	/** DEPRECATED: Klartext Klasse des Schülers */
	@Column(name = "Klasse")
	@JsonProperty
	public String Klasse;

	/** Gibt an, ob der Schueler neu zugewandert ist (1) oder nicht (0). Wurde in der Ukraine Krise im Migrationshintergrund geschaffen. */
	@Column(name = "NeuZugewandert")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean NeuZugewandert;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchueler ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param GU_ID   der Wert für das Attribut GU_ID
	 * @param NeuZugewandert   der Wert für das Attribut NeuZugewandert
	 */
	public MigrationDTOSchueler(final Long ID, final String GU_ID, final Boolean NeuZugewandert) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (GU_ID == null) {
			throw new NullPointerException("GU_ID must not be null");
		}
		this.GU_ID = GU_ID;
		if (NeuZugewandert == null) {
			throw new NullPointerException("NeuZugewandert must not be null");
		}
		this.NeuZugewandert = NeuZugewandert;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchueler other = (MigrationDTOSchueler) obj;
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
		return "MigrationDTOSchueler(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", GU_ID=" + this.GU_ID + ", SrcID=" + this.SrcID + ", IDext=" + this.IDext + ", Status=" + this.Status + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", AlleVornamen=" + this.AlleVornamen + ", Geburtsname=" + this.Geburtsname + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", PLZ=" + this.PLZ + ", OrtAbk=" + this.OrtAbk + ", Ortsteil_ID=" + this.Ortsteil_ID + ", Telefon=" + this.Telefon + ", Email=" + this.Email + ", Fax=" + this.Fax + ", Klassen_ID=" + this.Klassen_ID + ", JahrgangSchueler=" + this.JahrgangSchueler + ", PruefOrdnung=" + this.PruefOrdnung + ", Geburtsdatum=" + this.Geburtsdatum + ", Geburtsort=" + this.Geburtsort + ", Volljaehrig=" + this.Volljaehrig + ", Geschlecht=" + this.Geschlecht + ", StaatKrz=" + this.StaatKrz + ", StaatKrz2=" + this.StaatKrz2 + ", StaatAbk=" + this.StaatAbk + ", Aussiedler=" + this.Aussiedler + ", Religion_ID=" + this.Religion_ID + ", ReligionAbk=" + this.ReligionAbk + ", Religionsabmeldung=" + this.Religionsabmeldung + ", Religionsanmeldung=" + this.Religionsanmeldung + ", Bafoeg=" + this.Bafoeg + ", Schwerbehinderung=" + this.Schwerbehinderung + ", Sportbefreiung_ID=" + this.Sportbefreiung_ID + ", Fahrschueler_ID=" + this.Fahrschueler_ID + ", Haltestelle_ID=" + this.Haltestelle_ID + ", HaltestelleAbk=" + this.HaltestelleAbk + ", Schulgliederung=" + this.Schulgliederung + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", Fachklasse_ID=" + this.Fachklasse_ID + ", SchulpflichtErf=" + this.SchulpflichtErf + ", Aufnahmedatum=" + this.Aufnahmedatum + ", Einschulungsjahr=" + this.Einschulungsjahr + ", Einschulungsart_ID=" + this.Einschulungsart_ID + ", LSSchulNr=" + this.LSSchulNr + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSJahrgang=" + this.LSJahrgang + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", LSEntlassgrund=" + this.LSEntlassgrund + ", LSEntlassArt=" + this.LSEntlassArt + ", LSKlassenart=" + this.LSKlassenart + ", LSRefPaed=" + this.LSRefPaed + ", Entlassjahrgang=" + this.Entlassjahrgang + ", Entlassjahrgang_ID=" + this.Entlassjahrgang_ID + ", Entlassdatum=" + this.Entlassdatum + ", Entlassgrund=" + this.Entlassgrund + ", Entlassart=" + this.Entlassart + ", SchulwechselNr=" + this.SchulwechselNr + ", Schulwechseldatum=" + this.Schulwechseldatum + ", Geloescht=" + this.Geloescht + ", Gesperrt=" + this.Gesperrt + ", ModifiziertAm=" + this.ModifiziertAm + ", ModifiziertVon=" + this.ModifiziertVon + ", Markiert=" + this.Markiert + ", FotoVorhanden=" + this.FotoVorhanden + ", JVA=" + this.JVA + ", RefPaed=" + this.RefPaed + ", KeineAuskunft=" + this.KeineAuskunft + ", Beruf=" + this.Beruf + ", AbschlussDatum=" + this.AbschlussDatum + ", Bemerkungen=" + this.Bemerkungen + ", BeginnBildungsgang=" + this.BeginnBildungsgang + ", OrgFormKrz=" + this.OrgFormKrz + ", Klassenart=" + this.Klassenart + ", DurchschnittsNote=" + this.DurchschnittsNote + ", LSSGL=" + this.LSSGL + ", LSSchulform=" + this.LSSchulform + ", KonfDruck=" + this.KonfDruck + ", DSN_Text=" + this.DSN_Text + ", Berufsabschluss=" + this.Berufsabschluss + ", Schwerpunkt_ID=" + this.Schwerpunkt_ID + ", LSSGL_SIM=" + this.LSSGL_SIM + ", BerufsschulpflErf=" + this.BerufsschulpflErf + ", StatusNSJ=" + this.StatusNSJ + ", FachklasseNSJ_ID=" + this.FachklasseNSJ_ID + ", BuchKonto=" + this.BuchKonto + ", VerkehrsspracheFamilie=" + this.VerkehrsspracheFamilie + ", JahrZuzug=" + this.JahrZuzug + ", DauerKindergartenbesuch=" + this.DauerKindergartenbesuch + ", VerpflichtungSprachfoerderkurs=" + this.VerpflichtungSprachfoerderkurs + ", TeilnahmeSprachfoerderkurs=" + this.TeilnahmeSprachfoerderkurs + ", SchulbuchgeldBefreit=" + this.SchulbuchgeldBefreit + ", Autist=" + this.Autist + ", GeburtslandSchueler=" + this.GeburtslandSchueler + ", GeburtslandVater=" + this.GeburtslandVater + ", GeburtslandMutter=" + this.GeburtslandMutter + ", Uebergangsempfehlung_JG5=" + this.Uebergangsempfehlung_JG5 + ", ErsteSchulform_SI=" + this.ErsteSchulform_SI + ", JahrWechsel_SI=" + this.JahrWechsel_SI + ", JahrWechsel_SII=" + this.JahrWechsel_SII + ", Migrationshintergrund=" + this.Migrationshintergrund + ", ExterneSchulNr=" + this.ExterneSchulNr + ", Kindergarten_ID=" + this.Kindergarten_ID + ", LetzterBerufsAbschluss=" + this.LetzterBerufsAbschluss + ", LetzterAllgAbschluss=" + this.LetzterAllgAbschluss + ", Land=" + this.Land + ", Duplikat=" + this.Duplikat + ", EinschulungsartASD=" + this.EinschulungsartASD + ", SchulnrEigner=" + this.SchulnrEigner + ", BilingualerZweig=" + this.BilingualerZweig + ", DurchschnittsnoteFHR=" + this.DurchschnittsnoteFHR + ", DSN_FHR_Text=" + this.DSN_FHR_Text + ", Eigenanteil=" + this.Eigenanteil + ", StaatAbk2=" + this.StaatAbk2 + ", BKAZVO=" + this.BKAZVO + ", HatBerufsausbildung=" + this.HatBerufsausbildung + ", Ausweisnummer=" + this.Ausweisnummer + ", AOSF=" + this.AOSF + ", EPJahre=" + this.EPJahre + ", LSBemerkung=" + this.LSBemerkung + ", WechselBestaetigt=" + this.WechselBestaetigt + ", DauerBildungsgang=" + this.DauerBildungsgang + ", AnmeldeDatum=" + this.AnmeldeDatum + ", MeisterBafoeg=" + this.MeisterBafoeg + ", OnlineAnmeldung=" + this.OnlineAnmeldung + ", Dokumentenverzeichnis=" + this.Dokumentenverzeichnis + ", Berufsqualifikation=" + this.Berufsqualifikation + ", ZieldifferentesLernen=" + this.ZieldifferentesLernen + ", ZusatzNachname=" + this.ZusatzNachname + ", EndeEingliederung=" + this.EndeEingliederung + ", SchulEmail=" + this.SchulEmail + ", EndeAnschlussfoerderung=" + this.EndeAnschlussfoerderung + ", MasernImpfnachweis=" + this.MasernImpfnachweis + ", Lernstandsbericht=" + this.Lernstandsbericht + ", SprachfoerderungVon=" + this.SprachfoerderungVon + ", SprachfoerderungBis=" + this.SprachfoerderungBis + ", EntlassungBemerkung=" + this.EntlassungBemerkung + ", CredentialID=" + this.CredentialID + ", AktSchuljahr=" + this.AktSchuljahr + ", AktAbschnitt=" + this.AktAbschnitt + ", Klasse=" + this.Klasse + ", NeuZugewandert=" + this.NeuZugewandert + ")";
	}

}
