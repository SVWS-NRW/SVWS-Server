package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.GeschlechtConverter;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;
import de.svws_nrw.db.converter.current.SchuelerStatusConverter;
import de.svws_nrw.db.converter.current.VerkehrssprachenConverter;

import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.core.types.schule.Verkehrssprache;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;
import de.svws_nrw.csv.converter.current.GeschlechtConverterSerializer;
import de.svws_nrw.csv.converter.current.GeschlechtConverterDeserializer;
import de.svws_nrw.csv.converter.current.NationalitaetenConverterSerializer;
import de.svws_nrw.csv.converter.current.NationalitaetenConverterDeserializer;
import de.svws_nrw.csv.converter.current.SchuelerStatusConverterSerializer;
import de.svws_nrw.csv.converter.current.SchuelerStatusConverterDeserializer;
import de.svws_nrw.csv.converter.current.VerkehrssprachenConverterSerializer;
import de.svws_nrw.csv.converter.current.VerkehrssprachenConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schueler")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "GU_ID", "SrcID", "IDext", "Status", "Nachname", "Vorname", "AlleVornamen", "Geburtsname", "Strassenname", "HausNr", "HausNrZusatz", "Ort_ID", "Ortsteil_ID", "Telefon", "Email", "Fax", "Geburtsdatum", "Geburtsort", "Volljaehrig", "Geschlecht", "StaatKrz", "StaatKrz2", "Aussiedler", "Religion_ID", "Religionsabmeldung", "Religionsanmeldung", "Bafoeg", "Sportbefreiung_ID", "Fahrschueler_ID", "Haltestelle_ID", "SchulpflichtErf", "Aufnahmedatum", "Einschulungsjahr", "Einschulungsart_ID", "LSSchulNr", "LSSchulformSIM", "LSJahrgang", "LSSchulEntlassDatum", "LSVersetzung", "LSFachklKennung", "LSFachklSIM", "LSEntlassgrund", "LSEntlassArt", "LSKlassenart", "LSRefPaed", "Entlassjahrgang", "Entlassjahrgang_ID", "Entlassdatum", "Entlassgrund", "Entlassart", "SchulwechselNr", "Schulwechseldatum", "Geloescht", "Gesperrt", "ModifiziertAm", "ModifiziertVon", "Markiert", "FotoVorhanden", "JVA", "KeineAuskunft", "Beruf", "AbschlussDatum", "BeginnBildungsgang", "DurchschnittsNote", "LSSGL", "LSSchulform", "KonfDruck", "DSN_Text", "Berufsabschluss", "LSSGL_SIM", "BerufsschulpflErf", "StatusNSJ", "FachklasseNSJ_ID", "BuchKonto", "VerkehrsspracheFamilie", "JahrZuzug", "DauerKindergartenbesuch", "VerpflichtungSprachfoerderkurs", "TeilnahmeSprachfoerderkurs", "SchulbuchgeldBefreit", "GeburtslandSchueler", "GeburtslandVater", "GeburtslandMutter", "Uebergangsempfehlung_JG5", "ErsteSchulform_SI", "JahrWechsel_SI", "JahrWechsel_SII", "Migrationshintergrund", "ExterneSchulNr", "Kindergarten_ID", "LetzterBerufsAbschluss", "LetzterAllgAbschluss", "Land", "Duplikat", "EinschulungsartASD", "DurchschnittsnoteFHR", "DSN_FHR_Text", "Eigenanteil", "BKAZVO", "HatBerufsausbildung", "Ausweisnummer", "EPJahre", "LSBemerkung", "WechselBestaetigt", "DauerBildungsgang", "AnmeldeDatum", "MeisterBafoeg", "OnlineAnmeldung", "Dokumentenverzeichnis", "Berufsqualifikation", "EndeEingliederung", "SchulEmail", "EndeAnschlussfoerderung", "MasernImpfnachweis", "Lernstandsbericht", "SprachfoerderungVon", "SprachfoerderungBis", "EntlassungBemerkung", "CredentialID", "NeuZugewandert"})
public final class DTOSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchueler e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchueler e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchueler e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchueler e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchueler e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM DTOSchueler e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM DTOSchueler e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SrcID */
	public static final String QUERY_BY_SRCID = "SELECT e FROM DTOSchueler e WHERE e.SrcID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SrcID */
	public static final String QUERY_LIST_BY_SRCID = "SELECT e FROM DTOSchueler e WHERE e.SrcID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IDext */
	public static final String QUERY_BY_IDEXT = "SELECT e FROM DTOSchueler e WHERE e.IDext = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IDext */
	public static final String QUERY_LIST_BY_IDEXT = "SELECT e FROM DTOSchueler e WHERE e.IDext IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM DTOSchueler e WHERE e.Status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM DTOSchueler e WHERE e.Status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nachname */
	public static final String QUERY_BY_NACHNAME = "SELECT e FROM DTOSchueler e WHERE e.Nachname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nachname */
	public static final String QUERY_LIST_BY_NACHNAME = "SELECT e FROM DTOSchueler e WHERE e.Nachname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname */
	public static final String QUERY_BY_VORNAME = "SELECT e FROM DTOSchueler e WHERE e.Vorname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname */
	public static final String QUERY_LIST_BY_VORNAME = "SELECT e FROM DTOSchueler e WHERE e.Vorname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AlleVornamen */
	public static final String QUERY_BY_ALLEVORNAMEN = "SELECT e FROM DTOSchueler e WHERE e.AlleVornamen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AlleVornamen */
	public static final String QUERY_LIST_BY_ALLEVORNAMEN = "SELECT e FROM DTOSchueler e WHERE e.AlleVornamen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsname */
	public static final String QUERY_BY_GEBURTSNAME = "SELECT e FROM DTOSchueler e WHERE e.Geburtsname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsname */
	public static final String QUERY_LIST_BY_GEBURTSNAME = "SELECT e FROM DTOSchueler e WHERE e.Geburtsname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM DTOSchueler e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM DTOSchueler e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM DTOSchueler e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM DTOSchueler e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM DTOSchueler e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM DTOSchueler e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort_ID */
	public static final String QUERY_BY_ORT_ID = "SELECT e FROM DTOSchueler e WHERE e.Ort_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort_ID */
	public static final String QUERY_LIST_BY_ORT_ID = "SELECT e FROM DTOSchueler e WHERE e.Ort_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ortsteil_ID */
	public static final String QUERY_BY_ORTSTEIL_ID = "SELECT e FROM DTOSchueler e WHERE e.Ortsteil_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ortsteil_ID */
	public static final String QUERY_LIST_BY_ORTSTEIL_ID = "SELECT e FROM DTOSchueler e WHERE e.Ortsteil_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefon */
	public static final String QUERY_BY_TELEFON = "SELECT e FROM DTOSchueler e WHERE e.Telefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefon */
	public static final String QUERY_LIST_BY_TELEFON = "SELECT e FROM DTOSchueler e WHERE e.Telefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOSchueler e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOSchueler e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fax */
	public static final String QUERY_BY_FAX = "SELECT e FROM DTOSchueler e WHERE e.Fax = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fax */
	public static final String QUERY_LIST_BY_FAX = "SELECT e FROM DTOSchueler e WHERE e.Fax IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsdatum */
	public static final String QUERY_BY_GEBURTSDATUM = "SELECT e FROM DTOSchueler e WHERE e.Geburtsdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsdatum */
	public static final String QUERY_LIST_BY_GEBURTSDATUM = "SELECT e FROM DTOSchueler e WHERE e.Geburtsdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geburtsort */
	public static final String QUERY_BY_GEBURTSORT = "SELECT e FROM DTOSchueler e WHERE e.Geburtsort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geburtsort */
	public static final String QUERY_LIST_BY_GEBURTSORT = "SELECT e FROM DTOSchueler e WHERE e.Geburtsort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Volljaehrig */
	public static final String QUERY_BY_VOLLJAEHRIG = "SELECT e FROM DTOSchueler e WHERE e.Volljaehrig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Volljaehrig */
	public static final String QUERY_LIST_BY_VOLLJAEHRIG = "SELECT e FROM DTOSchueler e WHERE e.Volljaehrig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geschlecht */
	public static final String QUERY_BY_GESCHLECHT = "SELECT e FROM DTOSchueler e WHERE e.Geschlecht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geschlecht */
	public static final String QUERY_LIST_BY_GESCHLECHT = "SELECT e FROM DTOSchueler e WHERE e.Geschlecht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StaatKrz */
	public static final String QUERY_BY_STAATKRZ = "SELECT e FROM DTOSchueler e WHERE e.StaatKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StaatKrz */
	public static final String QUERY_LIST_BY_STAATKRZ = "SELECT e FROM DTOSchueler e WHERE e.StaatKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StaatKrz2 */
	public static final String QUERY_BY_STAATKRZ2 = "SELECT e FROM DTOSchueler e WHERE e.StaatKrz2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StaatKrz2 */
	public static final String QUERY_LIST_BY_STAATKRZ2 = "SELECT e FROM DTOSchueler e WHERE e.StaatKrz2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aussiedler */
	public static final String QUERY_BY_AUSSIEDLER = "SELECT e FROM DTOSchueler e WHERE e.Aussiedler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aussiedler */
	public static final String QUERY_LIST_BY_AUSSIEDLER = "SELECT e FROM DTOSchueler e WHERE e.Aussiedler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Religion_ID */
	public static final String QUERY_BY_RELIGION_ID = "SELECT e FROM DTOSchueler e WHERE e.Religion_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Religion_ID */
	public static final String QUERY_LIST_BY_RELIGION_ID = "SELECT e FROM DTOSchueler e WHERE e.Religion_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Religionsabmeldung */
	public static final String QUERY_BY_RELIGIONSABMELDUNG = "SELECT e FROM DTOSchueler e WHERE e.Religionsabmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Religionsabmeldung */
	public static final String QUERY_LIST_BY_RELIGIONSABMELDUNG = "SELECT e FROM DTOSchueler e WHERE e.Religionsabmeldung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Religionsanmeldung */
	public static final String QUERY_BY_RELIGIONSANMELDUNG = "SELECT e FROM DTOSchueler e WHERE e.Religionsanmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Religionsanmeldung */
	public static final String QUERY_LIST_BY_RELIGIONSANMELDUNG = "SELECT e FROM DTOSchueler e WHERE e.Religionsanmeldung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bafoeg */
	public static final String QUERY_BY_BAFOEG = "SELECT e FROM DTOSchueler e WHERE e.Bafoeg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bafoeg */
	public static final String QUERY_LIST_BY_BAFOEG = "SELECT e FROM DTOSchueler e WHERE e.Bafoeg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sportbefreiung_ID */
	public static final String QUERY_BY_SPORTBEFREIUNG_ID = "SELECT e FROM DTOSchueler e WHERE e.Sportbefreiung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sportbefreiung_ID */
	public static final String QUERY_LIST_BY_SPORTBEFREIUNG_ID = "SELECT e FROM DTOSchueler e WHERE e.Sportbefreiung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fahrschueler_ID */
	public static final String QUERY_BY_FAHRSCHUELER_ID = "SELECT e FROM DTOSchueler e WHERE e.Fahrschueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fahrschueler_ID */
	public static final String QUERY_LIST_BY_FAHRSCHUELER_ID = "SELECT e FROM DTOSchueler e WHERE e.Fahrschueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Haltestelle_ID */
	public static final String QUERY_BY_HALTESTELLE_ID = "SELECT e FROM DTOSchueler e WHERE e.Haltestelle_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Haltestelle_ID */
	public static final String QUERY_LIST_BY_HALTESTELLE_ID = "SELECT e FROM DTOSchueler e WHERE e.Haltestelle_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulpflichtErf */
	public static final String QUERY_BY_SCHULPFLICHTERF = "SELECT e FROM DTOSchueler e WHERE e.SchulpflichtErf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulpflichtErf */
	public static final String QUERY_LIST_BY_SCHULPFLICHTERF = "SELECT e FROM DTOSchueler e WHERE e.SchulpflichtErf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aufnahmedatum */
	public static final String QUERY_BY_AUFNAHMEDATUM = "SELECT e FROM DTOSchueler e WHERE e.Aufnahmedatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aufnahmedatum */
	public static final String QUERY_LIST_BY_AUFNAHMEDATUM = "SELECT e FROM DTOSchueler e WHERE e.Aufnahmedatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Einschulungsjahr */
	public static final String QUERY_BY_EINSCHULUNGSJAHR = "SELECT e FROM DTOSchueler e WHERE e.Einschulungsjahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Einschulungsjahr */
	public static final String QUERY_LIST_BY_EINSCHULUNGSJAHR = "SELECT e FROM DTOSchueler e WHERE e.Einschulungsjahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Einschulungsart_ID */
	public static final String QUERY_BY_EINSCHULUNGSART_ID = "SELECT e FROM DTOSchueler e WHERE e.Einschulungsart_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Einschulungsart_ID */
	public static final String QUERY_LIST_BY_EINSCHULUNGSART_ID = "SELECT e FROM DTOSchueler e WHERE e.Einschulungsart_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulNr */
	public static final String QUERY_BY_LSSCHULNR = "SELECT e FROM DTOSchueler e WHERE e.LSSchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulNr */
	public static final String QUERY_LIST_BY_LSSCHULNR = "SELECT e FROM DTOSchueler e WHERE e.LSSchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulformSIM */
	public static final String QUERY_BY_LSSCHULFORMSIM = "SELECT e FROM DTOSchueler e WHERE e.LSSchulformSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulformSIM */
	public static final String QUERY_LIST_BY_LSSCHULFORMSIM = "SELECT e FROM DTOSchueler e WHERE e.LSSchulformSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSJahrgang */
	public static final String QUERY_BY_LSJAHRGANG = "SELECT e FROM DTOSchueler e WHERE e.LSJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSJahrgang */
	public static final String QUERY_LIST_BY_LSJAHRGANG = "SELECT e FROM DTOSchueler e WHERE e.LSJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulEntlassDatum */
	public static final String QUERY_BY_LSSCHULENTLASSDATUM = "SELECT e FROM DTOSchueler e WHERE e.LSSchulEntlassDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulEntlassDatum */
	public static final String QUERY_LIST_BY_LSSCHULENTLASSDATUM = "SELECT e FROM DTOSchueler e WHERE e.LSSchulEntlassDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSVersetzung */
	public static final String QUERY_BY_LSVERSETZUNG = "SELECT e FROM DTOSchueler e WHERE e.LSVersetzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSVersetzung */
	public static final String QUERY_LIST_BY_LSVERSETZUNG = "SELECT e FROM DTOSchueler e WHERE e.LSVersetzung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSFachklKennung */
	public static final String QUERY_BY_LSFACHKLKENNUNG = "SELECT e FROM DTOSchueler e WHERE e.LSFachklKennung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSFachklKennung */
	public static final String QUERY_LIST_BY_LSFACHKLKENNUNG = "SELECT e FROM DTOSchueler e WHERE e.LSFachklKennung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSFachklSIM */
	public static final String QUERY_BY_LSFACHKLSIM = "SELECT e FROM DTOSchueler e WHERE e.LSFachklSIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSFachklSIM */
	public static final String QUERY_LIST_BY_LSFACHKLSIM = "SELECT e FROM DTOSchueler e WHERE e.LSFachklSIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSEntlassgrund */
	public static final String QUERY_BY_LSENTLASSGRUND = "SELECT e FROM DTOSchueler e WHERE e.LSEntlassgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSEntlassgrund */
	public static final String QUERY_LIST_BY_LSENTLASSGRUND = "SELECT e FROM DTOSchueler e WHERE e.LSEntlassgrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSEntlassArt */
	public static final String QUERY_BY_LSENTLASSART = "SELECT e FROM DTOSchueler e WHERE e.LSEntlassArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSEntlassArt */
	public static final String QUERY_LIST_BY_LSENTLASSART = "SELECT e FROM DTOSchueler e WHERE e.LSEntlassArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSKlassenart */
	public static final String QUERY_BY_LSKLASSENART = "SELECT e FROM DTOSchueler e WHERE e.LSKlassenart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSKlassenart */
	public static final String QUERY_LIST_BY_LSKLASSENART = "SELECT e FROM DTOSchueler e WHERE e.LSKlassenart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSRefPaed */
	public static final String QUERY_BY_LSREFPAED = "SELECT e FROM DTOSchueler e WHERE e.LSRefPaed = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSRefPaed */
	public static final String QUERY_LIST_BY_LSREFPAED = "SELECT e FROM DTOSchueler e WHERE e.LSRefPaed IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassjahrgang */
	public static final String QUERY_BY_ENTLASSJAHRGANG = "SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassjahrgang */
	public static final String QUERY_LIST_BY_ENTLASSJAHRGANG = "SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassjahrgang_ID */
	public static final String QUERY_BY_ENTLASSJAHRGANG_ID = "SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassjahrgang_ID */
	public static final String QUERY_LIST_BY_ENTLASSJAHRGANG_ID = "SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassdatum */
	public static final String QUERY_BY_ENTLASSDATUM = "SELECT e FROM DTOSchueler e WHERE e.Entlassdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassdatum */
	public static final String QUERY_LIST_BY_ENTLASSDATUM = "SELECT e FROM DTOSchueler e WHERE e.Entlassdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassgrund */
	public static final String QUERY_BY_ENTLASSGRUND = "SELECT e FROM DTOSchueler e WHERE e.Entlassgrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassgrund */
	public static final String QUERY_LIST_BY_ENTLASSGRUND = "SELECT e FROM DTOSchueler e WHERE e.Entlassgrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Entlassart */
	public static final String QUERY_BY_ENTLASSART = "SELECT e FROM DTOSchueler e WHERE e.Entlassart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Entlassart */
	public static final String QUERY_LIST_BY_ENTLASSART = "SELECT e FROM DTOSchueler e WHERE e.Entlassart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulwechselNr */
	public static final String QUERY_BY_SCHULWECHSELNR = "SELECT e FROM DTOSchueler e WHERE e.SchulwechselNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulwechselNr */
	public static final String QUERY_LIST_BY_SCHULWECHSELNR = "SELECT e FROM DTOSchueler e WHERE e.SchulwechselNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulwechseldatum */
	public static final String QUERY_BY_SCHULWECHSELDATUM = "SELECT e FROM DTOSchueler e WHERE e.Schulwechseldatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulwechseldatum */
	public static final String QUERY_LIST_BY_SCHULWECHSELDATUM = "SELECT e FROM DTOSchueler e WHERE e.Schulwechseldatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Geloescht */
	public static final String QUERY_BY_GELOESCHT = "SELECT e FROM DTOSchueler e WHERE e.Geloescht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Geloescht */
	public static final String QUERY_LIST_BY_GELOESCHT = "SELECT e FROM DTOSchueler e WHERE e.Geloescht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gesperrt */
	public static final String QUERY_BY_GESPERRT = "SELECT e FROM DTOSchueler e WHERE e.Gesperrt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gesperrt */
	public static final String QUERY_LIST_BY_GESPERRT = "SELECT e FROM DTOSchueler e WHERE e.Gesperrt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ModifiziertAm */
	public static final String QUERY_BY_MODIFIZIERTAM = "SELECT e FROM DTOSchueler e WHERE e.ModifiziertAm = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ModifiziertAm */
	public static final String QUERY_LIST_BY_MODIFIZIERTAM = "SELECT e FROM DTOSchueler e WHERE e.ModifiziertAm IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ModifiziertVon */
	public static final String QUERY_BY_MODIFIZIERTVON = "SELECT e FROM DTOSchueler e WHERE e.ModifiziertVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ModifiziertVon */
	public static final String QUERY_LIST_BY_MODIFIZIERTVON = "SELECT e FROM DTOSchueler e WHERE e.ModifiziertVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Markiert */
	public static final String QUERY_BY_MARKIERT = "SELECT e FROM DTOSchueler e WHERE e.Markiert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Markiert */
	public static final String QUERY_LIST_BY_MARKIERT = "SELECT e FROM DTOSchueler e WHERE e.Markiert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FotoVorhanden */
	public static final String QUERY_BY_FOTOVORHANDEN = "SELECT e FROM DTOSchueler e WHERE e.FotoVorhanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FotoVorhanden */
	public static final String QUERY_LIST_BY_FOTOVORHANDEN = "SELECT e FROM DTOSchueler e WHERE e.FotoVorhanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JVA */
	public static final String QUERY_BY_JVA = "SELECT e FROM DTOSchueler e WHERE e.JVA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JVA */
	public static final String QUERY_LIST_BY_JVA = "SELECT e FROM DTOSchueler e WHERE e.JVA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KeineAuskunft */
	public static final String QUERY_BY_KEINEAUSKUNFT = "SELECT e FROM DTOSchueler e WHERE e.KeineAuskunft = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KeineAuskunft */
	public static final String QUERY_LIST_BY_KEINEAUSKUNFT = "SELECT e FROM DTOSchueler e WHERE e.KeineAuskunft IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beruf */
	public static final String QUERY_BY_BERUF = "SELECT e FROM DTOSchueler e WHERE e.Beruf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beruf */
	public static final String QUERY_LIST_BY_BERUF = "SELECT e FROM DTOSchueler e WHERE e.Beruf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschlussDatum */
	public static final String QUERY_BY_ABSCHLUSSDATUM = "SELECT e FROM DTOSchueler e WHERE e.AbschlussDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschlussDatum */
	public static final String QUERY_LIST_BY_ABSCHLUSSDATUM = "SELECT e FROM DTOSchueler e WHERE e.AbschlussDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BeginnBildungsgang */
	public static final String QUERY_BY_BEGINNBILDUNGSGANG = "SELECT e FROM DTOSchueler e WHERE e.BeginnBildungsgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BeginnBildungsgang */
	public static final String QUERY_LIST_BY_BEGINNBILDUNGSGANG = "SELECT e FROM DTOSchueler e WHERE e.BeginnBildungsgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DurchschnittsNote */
	public static final String QUERY_BY_DURCHSCHNITTSNOTE = "SELECT e FROM DTOSchueler e WHERE e.DurchschnittsNote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DurchschnittsNote */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTE = "SELECT e FROM DTOSchueler e WHERE e.DurchschnittsNote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSGL */
	public static final String QUERY_BY_LSSGL = "SELECT e FROM DTOSchueler e WHERE e.LSSGL = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSGL */
	public static final String QUERY_LIST_BY_LSSGL = "SELECT e FROM DTOSchueler e WHERE e.LSSGL IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSchulform */
	public static final String QUERY_BY_LSSCHULFORM = "SELECT e FROM DTOSchueler e WHERE e.LSSchulform = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSchulform */
	public static final String QUERY_LIST_BY_LSSCHULFORM = "SELECT e FROM DTOSchueler e WHERE e.LSSchulform IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KonfDruck */
	public static final String QUERY_BY_KONFDRUCK = "SELECT e FROM DTOSchueler e WHERE e.KonfDruck = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KonfDruck */
	public static final String QUERY_LIST_BY_KONFDRUCK = "SELECT e FROM DTOSchueler e WHERE e.KonfDruck IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DSN_Text */
	public static final String QUERY_BY_DSN_TEXT = "SELECT e FROM DTOSchueler e WHERE e.DSN_Text = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DSN_Text */
	public static final String QUERY_LIST_BY_DSN_TEXT = "SELECT e FROM DTOSchueler e WHERE e.DSN_Text IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Berufsabschluss */
	public static final String QUERY_BY_BERUFSABSCHLUSS = "SELECT e FROM DTOSchueler e WHERE e.Berufsabschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Berufsabschluss */
	public static final String QUERY_LIST_BY_BERUFSABSCHLUSS = "SELECT e FROM DTOSchueler e WHERE e.Berufsabschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSSGL_SIM */
	public static final String QUERY_BY_LSSGL_SIM = "SELECT e FROM DTOSchueler e WHERE e.LSSGL_SIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSSGL_SIM */
	public static final String QUERY_LIST_BY_LSSGL_SIM = "SELECT e FROM DTOSchueler e WHERE e.LSSGL_SIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BerufsschulpflErf */
	public static final String QUERY_BY_BERUFSSCHULPFLERF = "SELECT e FROM DTOSchueler e WHERE e.BerufsschulpflErf = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BerufsschulpflErf */
	public static final String QUERY_LIST_BY_BERUFSSCHULPFLERF = "SELECT e FROM DTOSchueler e WHERE e.BerufsschulpflErf IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes StatusNSJ */
	public static final String QUERY_BY_STATUSNSJ = "SELECT e FROM DTOSchueler e WHERE e.StatusNSJ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes StatusNSJ */
	public static final String QUERY_LIST_BY_STATUSNSJ = "SELECT e FROM DTOSchueler e WHERE e.StatusNSJ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachklasseNSJ_ID */
	public static final String QUERY_BY_FACHKLASSENSJ_ID = "SELECT e FROM DTOSchueler e WHERE e.FachklasseNSJ_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachklasseNSJ_ID */
	public static final String QUERY_LIST_BY_FACHKLASSENSJ_ID = "SELECT e FROM DTOSchueler e WHERE e.FachklasseNSJ_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BuchKonto */
	public static final String QUERY_BY_BUCHKONTO = "SELECT e FROM DTOSchueler e WHERE e.BuchKonto = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BuchKonto */
	public static final String QUERY_LIST_BY_BUCHKONTO = "SELECT e FROM DTOSchueler e WHERE e.BuchKonto IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VerkehrsspracheFamilie */
	public static final String QUERY_BY_VERKEHRSSPRACHEFAMILIE = "SELECT e FROM DTOSchueler e WHERE e.VerkehrsspracheFamilie = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VerkehrsspracheFamilie */
	public static final String QUERY_LIST_BY_VERKEHRSSPRACHEFAMILIE = "SELECT e FROM DTOSchueler e WHERE e.VerkehrsspracheFamilie IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrZuzug */
	public static final String QUERY_BY_JAHRZUZUG = "SELECT e FROM DTOSchueler e WHERE e.JahrZuzug = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrZuzug */
	public static final String QUERY_LIST_BY_JAHRZUZUG = "SELECT e FROM DTOSchueler e WHERE e.JahrZuzug IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DauerKindergartenbesuch */
	public static final String QUERY_BY_DAUERKINDERGARTENBESUCH = "SELECT e FROM DTOSchueler e WHERE e.DauerKindergartenbesuch = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DauerKindergartenbesuch */
	public static final String QUERY_LIST_BY_DAUERKINDERGARTENBESUCH = "SELECT e FROM DTOSchueler e WHERE e.DauerKindergartenbesuch IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VerpflichtungSprachfoerderkurs */
	public static final String QUERY_BY_VERPFLICHTUNGSPRACHFOERDERKURS = "SELECT e FROM DTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VerpflichtungSprachfoerderkurs */
	public static final String QUERY_LIST_BY_VERPFLICHTUNGSPRACHFOERDERKURS = "SELECT e FROM DTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TeilnahmeSprachfoerderkurs */
	public static final String QUERY_BY_TEILNAHMESPRACHFOERDERKURS = "SELECT e FROM DTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TeilnahmeSprachfoerderkurs */
	public static final String QUERY_LIST_BY_TEILNAHMESPRACHFOERDERKURS = "SELECT e FROM DTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulbuchgeldBefreit */
	public static final String QUERY_BY_SCHULBUCHGELDBEFREIT = "SELECT e FROM DTOSchueler e WHERE e.SchulbuchgeldBefreit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulbuchgeldBefreit */
	public static final String QUERY_LIST_BY_SCHULBUCHGELDBEFREIT = "SELECT e FROM DTOSchueler e WHERE e.SchulbuchgeldBefreit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeburtslandSchueler */
	public static final String QUERY_BY_GEBURTSLANDSCHUELER = "SELECT e FROM DTOSchueler e WHERE e.GeburtslandSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeburtslandSchueler */
	public static final String QUERY_LIST_BY_GEBURTSLANDSCHUELER = "SELECT e FROM DTOSchueler e WHERE e.GeburtslandSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeburtslandVater */
	public static final String QUERY_BY_GEBURTSLANDVATER = "SELECT e FROM DTOSchueler e WHERE e.GeburtslandVater = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeburtslandVater */
	public static final String QUERY_LIST_BY_GEBURTSLANDVATER = "SELECT e FROM DTOSchueler e WHERE e.GeburtslandVater IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GeburtslandMutter */
	public static final String QUERY_BY_GEBURTSLANDMUTTER = "SELECT e FROM DTOSchueler e WHERE e.GeburtslandMutter = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GeburtslandMutter */
	public static final String QUERY_LIST_BY_GEBURTSLANDMUTTER = "SELECT e FROM DTOSchueler e WHERE e.GeburtslandMutter IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Uebergangsempfehlung_JG5 */
	public static final String QUERY_BY_UEBERGANGSEMPFEHLUNG_JG5 = "SELECT e FROM DTOSchueler e WHERE e.Uebergangsempfehlung_JG5 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Uebergangsempfehlung_JG5 */
	public static final String QUERY_LIST_BY_UEBERGANGSEMPFEHLUNG_JG5 = "SELECT e FROM DTOSchueler e WHERE e.Uebergangsempfehlung_JG5 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErsteSchulform_SI */
	public static final String QUERY_BY_ERSTESCHULFORM_SI = "SELECT e FROM DTOSchueler e WHERE e.ErsteSchulform_SI = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErsteSchulform_SI */
	public static final String QUERY_LIST_BY_ERSTESCHULFORM_SI = "SELECT e FROM DTOSchueler e WHERE e.ErsteSchulform_SI IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrWechsel_SI */
	public static final String QUERY_BY_JAHRWECHSEL_SI = "SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SI = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrWechsel_SI */
	public static final String QUERY_LIST_BY_JAHRWECHSEL_SI = "SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SI IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrWechsel_SII */
	public static final String QUERY_BY_JAHRWECHSEL_SII = "SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrWechsel_SII */
	public static final String QUERY_LIST_BY_JAHRWECHSEL_SII = "SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Migrationshintergrund */
	public static final String QUERY_BY_MIGRATIONSHINTERGRUND = "SELECT e FROM DTOSchueler e WHERE e.Migrationshintergrund = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Migrationshintergrund */
	public static final String QUERY_LIST_BY_MIGRATIONSHINTERGRUND = "SELECT e FROM DTOSchueler e WHERE e.Migrationshintergrund IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ExterneSchulNr */
	public static final String QUERY_BY_EXTERNESCHULNR = "SELECT e FROM DTOSchueler e WHERE e.ExterneSchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ExterneSchulNr */
	public static final String QUERY_LIST_BY_EXTERNESCHULNR = "SELECT e FROM DTOSchueler e WHERE e.ExterneSchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kindergarten_ID */
	public static final String QUERY_BY_KINDERGARTEN_ID = "SELECT e FROM DTOSchueler e WHERE e.Kindergarten_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kindergarten_ID */
	public static final String QUERY_LIST_BY_KINDERGARTEN_ID = "SELECT e FROM DTOSchueler e WHERE e.Kindergarten_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LetzterBerufsAbschluss */
	public static final String QUERY_BY_LETZTERBERUFSABSCHLUSS = "SELECT e FROM DTOSchueler e WHERE e.LetzterBerufsAbschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LetzterBerufsAbschluss */
	public static final String QUERY_LIST_BY_LETZTERBERUFSABSCHLUSS = "SELECT e FROM DTOSchueler e WHERE e.LetzterBerufsAbschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LetzterAllgAbschluss */
	public static final String QUERY_BY_LETZTERALLGABSCHLUSS = "SELECT e FROM DTOSchueler e WHERE e.LetzterAllgAbschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LetzterAllgAbschluss */
	public static final String QUERY_LIST_BY_LETZTERALLGABSCHLUSS = "SELECT e FROM DTOSchueler e WHERE e.LetzterAllgAbschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Land */
	public static final String QUERY_BY_LAND = "SELECT e FROM DTOSchueler e WHERE e.Land = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Land */
	public static final String QUERY_LIST_BY_LAND = "SELECT e FROM DTOSchueler e WHERE e.Land IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Duplikat */
	public static final String QUERY_BY_DUPLIKAT = "SELECT e FROM DTOSchueler e WHERE e.Duplikat = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Duplikat */
	public static final String QUERY_LIST_BY_DUPLIKAT = "SELECT e FROM DTOSchueler e WHERE e.Duplikat IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EinschulungsartASD */
	public static final String QUERY_BY_EINSCHULUNGSARTASD = "SELECT e FROM DTOSchueler e WHERE e.EinschulungsartASD = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EinschulungsartASD */
	public static final String QUERY_LIST_BY_EINSCHULUNGSARTASD = "SELECT e FROM DTOSchueler e WHERE e.EinschulungsartASD IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DurchschnittsnoteFHR */
	public static final String QUERY_BY_DURCHSCHNITTSNOTEFHR = "SELECT e FROM DTOSchueler e WHERE e.DurchschnittsnoteFHR = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DurchschnittsnoteFHR */
	public static final String QUERY_LIST_BY_DURCHSCHNITTSNOTEFHR = "SELECT e FROM DTOSchueler e WHERE e.DurchschnittsnoteFHR IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DSN_FHR_Text */
	public static final String QUERY_BY_DSN_FHR_TEXT = "SELECT e FROM DTOSchueler e WHERE e.DSN_FHR_Text = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DSN_FHR_Text */
	public static final String QUERY_LIST_BY_DSN_FHR_TEXT = "SELECT e FROM DTOSchueler e WHERE e.DSN_FHR_Text IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Eigenanteil */
	public static final String QUERY_BY_EIGENANTEIL = "SELECT e FROM DTOSchueler e WHERE e.Eigenanteil = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Eigenanteil */
	public static final String QUERY_LIST_BY_EIGENANTEIL = "SELECT e FROM DTOSchueler e WHERE e.Eigenanteil IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BKAZVO */
	public static final String QUERY_BY_BKAZVO = "SELECT e FROM DTOSchueler e WHERE e.BKAZVO = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BKAZVO */
	public static final String QUERY_LIST_BY_BKAZVO = "SELECT e FROM DTOSchueler e WHERE e.BKAZVO IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HatBerufsausbildung */
	public static final String QUERY_BY_HATBERUFSAUSBILDUNG = "SELECT e FROM DTOSchueler e WHERE e.HatBerufsausbildung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HatBerufsausbildung */
	public static final String QUERY_LIST_BY_HATBERUFSAUSBILDUNG = "SELECT e FROM DTOSchueler e WHERE e.HatBerufsausbildung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ausweisnummer */
	public static final String QUERY_BY_AUSWEISNUMMER = "SELECT e FROM DTOSchueler e WHERE e.Ausweisnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ausweisnummer */
	public static final String QUERY_LIST_BY_AUSWEISNUMMER = "SELECT e FROM DTOSchueler e WHERE e.Ausweisnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EPJahre */
	public static final String QUERY_BY_EPJAHRE = "SELECT e FROM DTOSchueler e WHERE e.EPJahre = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EPJahre */
	public static final String QUERY_LIST_BY_EPJAHRE = "SELECT e FROM DTOSchueler e WHERE e.EPJahre IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LSBemerkung */
	public static final String QUERY_BY_LSBEMERKUNG = "SELECT e FROM DTOSchueler e WHERE e.LSBemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LSBemerkung */
	public static final String QUERY_LIST_BY_LSBEMERKUNG = "SELECT e FROM DTOSchueler e WHERE e.LSBemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WechselBestaetigt */
	public static final String QUERY_BY_WECHSELBESTAETIGT = "SELECT e FROM DTOSchueler e WHERE e.WechselBestaetigt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WechselBestaetigt */
	public static final String QUERY_LIST_BY_WECHSELBESTAETIGT = "SELECT e FROM DTOSchueler e WHERE e.WechselBestaetigt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DauerBildungsgang */
	public static final String QUERY_BY_DAUERBILDUNGSGANG = "SELECT e FROM DTOSchueler e WHERE e.DauerBildungsgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DauerBildungsgang */
	public static final String QUERY_LIST_BY_DAUERBILDUNGSGANG = "SELECT e FROM DTOSchueler e WHERE e.DauerBildungsgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnmeldeDatum */
	public static final String QUERY_BY_ANMELDEDATUM = "SELECT e FROM DTOSchueler e WHERE e.AnmeldeDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnmeldeDatum */
	public static final String QUERY_LIST_BY_ANMELDEDATUM = "SELECT e FROM DTOSchueler e WHERE e.AnmeldeDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MeisterBafoeg */
	public static final String QUERY_BY_MEISTERBAFOEG = "SELECT e FROM DTOSchueler e WHERE e.MeisterBafoeg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MeisterBafoeg */
	public static final String QUERY_LIST_BY_MEISTERBAFOEG = "SELECT e FROM DTOSchueler e WHERE e.MeisterBafoeg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OnlineAnmeldung */
	public static final String QUERY_BY_ONLINEANMELDUNG = "SELECT e FROM DTOSchueler e WHERE e.OnlineAnmeldung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OnlineAnmeldung */
	public static final String QUERY_LIST_BY_ONLINEANMELDUNG = "SELECT e FROM DTOSchueler e WHERE e.OnlineAnmeldung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Dokumentenverzeichnis */
	public static final String QUERY_BY_DOKUMENTENVERZEICHNIS = "SELECT e FROM DTOSchueler e WHERE e.Dokumentenverzeichnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Dokumentenverzeichnis */
	public static final String QUERY_LIST_BY_DOKUMENTENVERZEICHNIS = "SELECT e FROM DTOSchueler e WHERE e.Dokumentenverzeichnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Berufsqualifikation */
	public static final String QUERY_BY_BERUFSQUALIFIKATION = "SELECT e FROM DTOSchueler e WHERE e.Berufsqualifikation = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Berufsqualifikation */
	public static final String QUERY_LIST_BY_BERUFSQUALIFIKATION = "SELECT e FROM DTOSchueler e WHERE e.Berufsqualifikation IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EndeEingliederung */
	public static final String QUERY_BY_ENDEEINGLIEDERUNG = "SELECT e FROM DTOSchueler e WHERE e.EndeEingliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EndeEingliederung */
	public static final String QUERY_LIST_BY_ENDEEINGLIEDERUNG = "SELECT e FROM DTOSchueler e WHERE e.EndeEingliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulEmail */
	public static final String QUERY_BY_SCHULEMAIL = "SELECT e FROM DTOSchueler e WHERE e.SchulEmail = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulEmail */
	public static final String QUERY_LIST_BY_SCHULEMAIL = "SELECT e FROM DTOSchueler e WHERE e.SchulEmail IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EndeAnschlussfoerderung */
	public static final String QUERY_BY_ENDEANSCHLUSSFOERDERUNG = "SELECT e FROM DTOSchueler e WHERE e.EndeAnschlussfoerderung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EndeAnschlussfoerderung */
	public static final String QUERY_LIST_BY_ENDEANSCHLUSSFOERDERUNG = "SELECT e FROM DTOSchueler e WHERE e.EndeAnschlussfoerderung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MasernImpfnachweis */
	public static final String QUERY_BY_MASERNIMPFNACHWEIS = "SELECT e FROM DTOSchueler e WHERE e.MasernImpfnachweis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MasernImpfnachweis */
	public static final String QUERY_LIST_BY_MASERNIMPFNACHWEIS = "SELECT e FROM DTOSchueler e WHERE e.MasernImpfnachweis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernstandsbericht */
	public static final String QUERY_BY_LERNSTANDSBERICHT = "SELECT e FROM DTOSchueler e WHERE e.Lernstandsbericht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernstandsbericht */
	public static final String QUERY_LIST_BY_LERNSTANDSBERICHT = "SELECT e FROM DTOSchueler e WHERE e.Lernstandsbericht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SprachfoerderungVon */
	public static final String QUERY_BY_SPRACHFOERDERUNGVON = "SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SprachfoerderungVon */
	public static final String QUERY_LIST_BY_SPRACHFOERDERUNGVON = "SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SprachfoerderungBis */
	public static final String QUERY_BY_SPRACHFOERDERUNGBIS = "SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SprachfoerderungBis */
	public static final String QUERY_LIST_BY_SPRACHFOERDERUNGBIS = "SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EntlassungBemerkung */
	public static final String QUERY_BY_ENTLASSUNGBEMERKUNG = "SELECT e FROM DTOSchueler e WHERE e.EntlassungBemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EntlassungBemerkung */
	public static final String QUERY_LIST_BY_ENTLASSUNGBEMERKUNG = "SELECT e FROM DTOSchueler e WHERE e.EntlassungBemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM DTOSchueler e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM DTOSchueler e WHERE e.CredentialID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NeuZugewandert */
	public static final String QUERY_BY_NEUZUGEWANDERT = "SELECT e FROM DTOSchueler e WHERE e.NeuZugewandert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NeuZugewandert */
	public static final String QUERY_LIST_BY_NEUZUGEWANDERT = "SELECT e FROM DTOSchueler e WHERE e.NeuZugewandert IN ?1";

	/** ID des Schülerdatensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	@Convert(converter = SchuelerStatusConverter.class)
	@JsonSerialize(using = SchuelerStatusConverterSerializer.class)
	@JsonDeserialize(using = SchuelerStatusConverterDeserializer.class)
	public SchuelerStatus Status;

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

	/** Geburtsdatum des Schülers */
	@Column(name = "Geburtsdatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Geburtsdatum;

	/** Geburtsort des Schülers */
	@Column(name = "Geburtsort")
	@JsonProperty
	public String Geburtsort;

	/** Gibt an ob der Schüler volljährig ist */
	@Column(name = "Volljaehrig")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Volljaehrig;

	/** Geschlecht des Schülers */
	@Column(name = "Geschlecht")
	@JsonProperty
	@Convert(converter = GeschlechtConverter.class)
	@JsonSerialize(using = GeschlechtConverterSerializer.class)
	@JsonDeserialize(using = GeschlechtConverterDeserializer.class)
	public Geschlecht Geschlecht;

	/** Kürzel der 1. Staatsangehörigkeit */
	@Column(name = "StaatKrz")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten StaatKrz;

	/** Kürzel der 2. Staatsangehörigkeit */
	@Column(name = "StaatKrz2")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten StaatKrz2;

	/** DEPRECATED: Gibt an ob der Schüler ausgesiedelt ist (wird nicht mehr erfasst) */
	@Column(name = "Aussiedler")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Aussiedler;

	/** ID des Religionskatalogeintrags */
	@Column(name = "Religion_ID")
	@JsonProperty
	public Long Religion_ID;

	/** Abmeldetdateum vom Religionsunterricht */
	@Column(name = "Religionsabmeldung")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Religionsabmeldung;

	/** Anmeldedatum zum Religionsunterricht wenn vorher abgemeldet */
	@Column(name = "Religionsanmeldung")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Religionsanmeldung;

	/** Gibt an ob ein Schüler BAFög bezieht */
	@Column(name = "Bafoeg")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Bafoeg;

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

	/** Gibt an ob die Vollzeitschulpflicht erfüllt ist Ja Nein */
	@Column(name = "SchulpflichtErf")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchulpflichtErf;

	/** Aufnahmedatum */
	@Column(name = "Aufnahmedatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
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
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
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
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Schulwechseldatum;

	/** Löschmarkierung Schülerdatensatz */
	@Column(name = "Geloescht")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Geloescht;

	/** Datensatz gesperrt Ja Nein */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/** zuletzt geändert Datum */
	@Column(name = "ModifiziertAm")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String ModifiziertAm;

	/** zuletzt geändert Benutzer */
	@Column(name = "ModifiziertVon")
	@JsonProperty
	public String ModifiziertVon;

	/** Datensatz ist markiert */
	@Column(name = "Markiert")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Markiert;

	/** DEPRECATED: nicht mehr genutzt Zustimmung Foto */
	@Column(name = "FotoVorhanden")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FotoVorhanden;

	/** Ist Schüler einer Justizvollzugsanstalt */
	@Column(name = "JVA")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean JVA;

	/** Keine Auskunft an Dritte Ja Nein */
	@Column(name = "KeineAuskunft")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean KeineAuskunft;

	/** Berufsbezeichnung wenn der Schüler einen hat */
	@Column(name = "Beruf")
	@JsonProperty
	public String Beruf;

	/** Abschlussdatum */
	@Column(name = "AbschlussDatum")
	@JsonProperty
	public String AbschlussDatum;

	/** Beginn des Bildungsgangs BK */
	@Column(name = "BeginnBildungsgang")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String BeginnBildungsgang;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean KonfDruck;

	/** Speichert die Durchschnittsnote im Klartext (also befriedigend bei einer Durchschnittsnote von z.B. 3.1) */
	@Column(name = "DSN_Text")
	@JsonProperty
	public String DSN_Text;

	/** Bezeichnung Berufsabschluss */
	@Column(name = "Berufsabschluss")
	@JsonProperty
	public String Berufsabschluss;

	/** Letzte Schule Schulgiederung (wichtig wenn BK) */
	@Column(name = "LSSGL_SIM")
	@JsonProperty
	public String LSSGL_SIM;

	/** Gibt an ob die Berufsschulpflicht erfüllt ist (Ja/Nein) */
	@Column(name = "BerufsschulpflErf")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = VerkehrssprachenConverter.class)
	@JsonSerialize(using = VerkehrssprachenConverterSerializer.class)
	@JsonDeserialize(using = VerkehrssprachenConverterDeserializer.class)
	public Verkehrssprache VerkehrsspracheFamilie;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean VerpflichtungSprachfoerderkurs;

	/** Teilnahme an einen Sprachförderkurs (Ja/Nein) */
	@Column(name = "TeilnahmeSprachfoerderkurs")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean TeilnahmeSprachfoerderkurs;

	/** Vom Schulbuchgeld befreit (Ja/Nein) */
	@Column(name = "SchulbuchgeldBefreit")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchulbuchgeldBefreit;

	/** Migrationshintergrund Geburtsland des Schülers */
	@Column(name = "GeburtslandSchueler")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten GeburtslandSchueler;

	/** Migrationshintergrund Geburtsland des Vaters */
	@Column(name = "GeburtslandVater")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten GeburtslandVater;

	/** Migrationshintergrund Geburtsland der Mutter */
	@Column(name = "GeburtslandMutter")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten GeburtslandMutter;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Duplikat;

	/** ASD-Kürzel Einschulungsart IT.NRW */
	@Column(name = "EinschulungsartASD")
	@JsonProperty
	public String EinschulungsartASD;

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

	/** Gibt an ob der Schüler in einem Bildungsgang nach BKAZVO ist (BK) */
	@Column(name = "BKAZVO")
	@JsonProperty
	public Integer BKAZVO;

	/** Gibt an ob der Schüler eine Berufsausbildung hat (BK) */
	@Column(name = "HatBerufsausbildung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean HatBerufsausbildung;

	/** Nummer des Schülerausweises */
	@Column(name = "Ausweisnummer")
	@JsonProperty
	public String Ausweisnummer;

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
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean WechselBestaetigt;

	/** Dauer des Bildungsgangs am BK */
	@Column(name = "DauerBildungsgang")
	@JsonProperty
	public Integer DauerBildungsgang;

	/** Anmeldedatum des Schülers */
	@Column(name = "AnmeldeDatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String AnmeldeDatum;

	/** Gibt an ob ein Schüler MeisterBafög bezieht BK */
	@Column(name = "MeisterBafoeg")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MeisterBafoeg;

	/** Schüler hat sich Online angemeldet (Ja/Nein) */
	@Column(name = "OnlineAnmeldung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean OnlineAnmeldung;

	/** Pfad zum Dokumentenverzeichnis */
	@Column(name = "Dokumentenverzeichnis")
	@JsonProperty
	public String Dokumentenverzeichnis;

	/** Karteireiter Schulbesuch Berufsausbildung vorhanden (Ja/Nein) */
	@Column(name = "Berufsqualifikation")
	@JsonProperty
	public String Berufsqualifikation;

	/** Ende der Eingliederung bei zugezogenen Schülern (Flüchtlingen) */
	@Column(name = "EndeEingliederung")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String EndeEingliederung;

	/** schulische E-Mail-Adresse des Schülers */
	@Column(name = "SchulEmail")
	@JsonProperty
	public String SchulEmail;

	/** Ende der Anschlussförderung bei zugezogenen Schülern (Flüchtlingen) */
	@Column(name = "EndeAnschlussfoerderung")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String EndeAnschlussfoerderung;

	/** Gibt an, ob ein Nachweis über die Masern-Impfung erbracht wurde */
	@Column(name = "MasernImpfnachweis")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MasernImpfnachweis;

	/** Gibt an ob ein Schüler Sprachförderung in Deutsch (DAZ) erhält und somit Lernstandsberichte statt Zeugnisse */
	@Column(name = "Lernstandsbericht")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Lernstandsbericht;

	/** Datum des Beginns der Sprachförderung */
	@Column(name = "SprachfoerderungVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String SprachfoerderungVon;

	/** Datum des Endes der Sprachförderung */
	@Column(name = "SprachfoerderungBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String SprachfoerderungBis;

	/** Bemerkung bei Entlassung von der eigenen Schule */
	@Column(name = "EntlassungBemerkung")
	@JsonProperty
	public String EntlassungBemerkung;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/** Gibt an, ob der Schueler neu zugewandert ist (1) oder nicht (0). Wurde in der Ukraine Krise im Migrationshintergrund geschaffen. */
	@Column(name = "NeuZugewandert")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean NeuZugewandert;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchueler ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param GU_ID   der Wert für das Attribut GU_ID
	 * @param NeuZugewandert   der Wert für das Attribut NeuZugewandert
	 */
	public DTOSchueler(final long ID, final String GU_ID, final Boolean NeuZugewandert) {
		this.ID = ID;
		if (GU_ID == null) {
			throw new NullPointerException("GU_ID must not be null");
		}
		this.GU_ID = GU_ID;
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
		DTOSchueler other = (DTOSchueler) obj;
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
		return "DTOSchueler(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", GU_ID=" + this.GU_ID + ", SrcID=" + this.SrcID + ", IDext=" + this.IDext + ", Status=" + this.Status + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", AlleVornamen=" + this.AlleVornamen + ", Geburtsname=" + this.Geburtsname + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", Ortsteil_ID=" + this.Ortsteil_ID + ", Telefon=" + this.Telefon + ", Email=" + this.Email + ", Fax=" + this.Fax + ", Geburtsdatum=" + this.Geburtsdatum + ", Geburtsort=" + this.Geburtsort + ", Volljaehrig=" + this.Volljaehrig + ", Geschlecht=" + this.Geschlecht + ", StaatKrz=" + this.StaatKrz + ", StaatKrz2=" + this.StaatKrz2 + ", Aussiedler=" + this.Aussiedler + ", Religion_ID=" + this.Religion_ID + ", Religionsabmeldung=" + this.Religionsabmeldung + ", Religionsanmeldung=" + this.Religionsanmeldung + ", Bafoeg=" + this.Bafoeg + ", Sportbefreiung_ID=" + this.Sportbefreiung_ID + ", Fahrschueler_ID=" + this.Fahrschueler_ID + ", Haltestelle_ID=" + this.Haltestelle_ID + ", SchulpflichtErf=" + this.SchulpflichtErf + ", Aufnahmedatum=" + this.Aufnahmedatum + ", Einschulungsjahr=" + this.Einschulungsjahr + ", Einschulungsart_ID=" + this.Einschulungsart_ID + ", LSSchulNr=" + this.LSSchulNr + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSJahrgang=" + this.LSJahrgang + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", LSEntlassgrund=" + this.LSEntlassgrund + ", LSEntlassArt=" + this.LSEntlassArt + ", LSKlassenart=" + this.LSKlassenart + ", LSRefPaed=" + this.LSRefPaed + ", Entlassjahrgang=" + this.Entlassjahrgang + ", Entlassjahrgang_ID=" + this.Entlassjahrgang_ID + ", Entlassdatum=" + this.Entlassdatum + ", Entlassgrund=" + this.Entlassgrund + ", Entlassart=" + this.Entlassart + ", SchulwechselNr=" + this.SchulwechselNr + ", Schulwechseldatum=" + this.Schulwechseldatum + ", Geloescht=" + this.Geloescht + ", Gesperrt=" + this.Gesperrt + ", ModifiziertAm=" + this.ModifiziertAm + ", ModifiziertVon=" + this.ModifiziertVon + ", Markiert=" + this.Markiert + ", FotoVorhanden=" + this.FotoVorhanden + ", JVA=" + this.JVA + ", KeineAuskunft=" + this.KeineAuskunft + ", Beruf=" + this.Beruf + ", AbschlussDatum=" + this.AbschlussDatum + ", BeginnBildungsgang=" + this.BeginnBildungsgang + ", DurchschnittsNote=" + this.DurchschnittsNote + ", LSSGL=" + this.LSSGL + ", LSSchulform=" + this.LSSchulform + ", KonfDruck=" + this.KonfDruck + ", DSN_Text=" + this.DSN_Text + ", Berufsabschluss=" + this.Berufsabschluss + ", LSSGL_SIM=" + this.LSSGL_SIM + ", BerufsschulpflErf=" + this.BerufsschulpflErf + ", StatusNSJ=" + this.StatusNSJ + ", FachklasseNSJ_ID=" + this.FachklasseNSJ_ID + ", BuchKonto=" + this.BuchKonto + ", VerkehrsspracheFamilie=" + this.VerkehrsspracheFamilie + ", JahrZuzug=" + this.JahrZuzug + ", DauerKindergartenbesuch=" + this.DauerKindergartenbesuch + ", VerpflichtungSprachfoerderkurs=" + this.VerpflichtungSprachfoerderkurs + ", TeilnahmeSprachfoerderkurs=" + this.TeilnahmeSprachfoerderkurs + ", SchulbuchgeldBefreit=" + this.SchulbuchgeldBefreit + ", GeburtslandSchueler=" + this.GeburtslandSchueler + ", GeburtslandVater=" + this.GeburtslandVater + ", GeburtslandMutter=" + this.GeburtslandMutter + ", Uebergangsempfehlung_JG5=" + this.Uebergangsempfehlung_JG5 + ", ErsteSchulform_SI=" + this.ErsteSchulform_SI + ", JahrWechsel_SI=" + this.JahrWechsel_SI + ", JahrWechsel_SII=" + this.JahrWechsel_SII + ", Migrationshintergrund=" + this.Migrationshintergrund + ", ExterneSchulNr=" + this.ExterneSchulNr + ", Kindergarten_ID=" + this.Kindergarten_ID + ", LetzterBerufsAbschluss=" + this.LetzterBerufsAbschluss + ", LetzterAllgAbschluss=" + this.LetzterAllgAbschluss + ", Land=" + this.Land + ", Duplikat=" + this.Duplikat + ", EinschulungsartASD=" + this.EinschulungsartASD + ", DurchschnittsnoteFHR=" + this.DurchschnittsnoteFHR + ", DSN_FHR_Text=" + this.DSN_FHR_Text + ", Eigenanteil=" + this.Eigenanteil + ", BKAZVO=" + this.BKAZVO + ", HatBerufsausbildung=" + this.HatBerufsausbildung + ", Ausweisnummer=" + this.Ausweisnummer + ", EPJahre=" + this.EPJahre + ", LSBemerkung=" + this.LSBemerkung + ", WechselBestaetigt=" + this.WechselBestaetigt + ", DauerBildungsgang=" + this.DauerBildungsgang + ", AnmeldeDatum=" + this.AnmeldeDatum + ", MeisterBafoeg=" + this.MeisterBafoeg + ", OnlineAnmeldung=" + this.OnlineAnmeldung + ", Dokumentenverzeichnis=" + this.Dokumentenverzeichnis + ", Berufsqualifikation=" + this.Berufsqualifikation + ", EndeEingliederung=" + this.EndeEingliederung + ", SchulEmail=" + this.SchulEmail + ", EndeAnschlussfoerderung=" + this.EndeAnschlussfoerderung + ", MasernImpfnachweis=" + this.MasernImpfnachweis + ", Lernstandsbericht=" + this.Lernstandsbericht + ", SprachfoerderungVon=" + this.SprachfoerderungVon + ", SprachfoerderungBis=" + this.SprachfoerderungBis + ", EntlassungBemerkung=" + this.EntlassungBemerkung + ", CredentialID=" + this.CredentialID + ", NeuZugewandert=" + this.NeuZugewandert + ")";
	}

}
