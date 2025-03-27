package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLernabschnittsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLernabschnittsdaten")
@JsonPropertyOrder({"ID", "Schueler_ID", "Schuljahresabschnitts_ID", "WechselNr", "Schulbesuchsjahre", "Hochrechnung", "SemesterWertung", "PruefOrdnung", "Klassen_ID", "Tutor_ID", "Verspaetet", "NPV_Fach_ID", "NPV_NoteKrz", "NPV_Datum", "NPAA_Fach_ID", "NPAA_NoteKrz", "NPAA_Datum", "NPBQ_Fach_ID", "NPBQ_NoteKrz", "NPBQ_Datum", "VersetzungKrz", "AbschlussArt", "AbschlIstPrognose", "Konferenzdatum", "ZeugnisDatum", "Schulgliederung", "ASDJahrgang", "EPJahre", "Jahrgang_ID", "Fachklasse_ID", "Schwerpunkt_ID", "ZeugnisBem", "Schwerbehinderung", "Foerderschwerpunkt_ID", "OrgFormKrz", "RefPaed", "Klassenart", "SumFehlStd", "SumFehlStdU", "Wiederholung", "Gesamtnote_GS", "Gesamtnote_NW", "Folgeklasse_ID", "Foerderschwerpunkt2_ID", "Abschluss", "Abschluss_B", "DSNote", "AV_Leist", "AV_Zuv", "AV_Selbst", "SV_Verant", "SV_Konfl", "SV_Koop", "MoeglNPFaecher", "Zertifikate", "DatumFHR", "PruefAlgoErgebnis", "Zeugnisart", "DatumVon", "DatumBis", "FehlstundenGrenzwert", "Sonderpaedagoge_ID", "FachPraktAnteilAusr", "BilingualerZweig", "AOSF", "Autist", "ZieldifferentesLernen", "meldungBAN"})
public final class DTOSchuelerLernabschnittsdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerLernabschnittsdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WechselNr */
	public static final String QUERY_BY_WECHSELNR = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.WechselNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WechselNr */
	public static final String QUERY_LIST_BY_WECHSELNR = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.WechselNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulbesuchsjahre */
	public static final String QUERY_BY_SCHULBESUCHSJAHRE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schulbesuchsjahre = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulbesuchsjahre */
	public static final String QUERY_LIST_BY_SCHULBESUCHSJAHRE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schulbesuchsjahre IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Hochrechnung */
	public static final String QUERY_BY_HOCHRECHNUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Hochrechnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Hochrechnung */
	public static final String QUERY_LIST_BY_HOCHRECHNUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Hochrechnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SemesterWertung */
	public static final String QUERY_BY_SEMESTERWERTUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SemesterWertung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SemesterWertung */
	public static final String QUERY_LIST_BY_SEMESTERWERTUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SemesterWertung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefOrdnung */
	public static final String QUERY_BY_PRUEFORDNUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.PruefOrdnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefOrdnung */
	public static final String QUERY_LIST_BY_PRUEFORDNUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.PruefOrdnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassen_ID */
	public static final String QUERY_BY_KLASSEN_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Klassen_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassen_ID */
	public static final String QUERY_LIST_BY_KLASSEN_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Klassen_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Tutor_ID */
	public static final String QUERY_BY_TUTOR_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Tutor_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Tutor_ID */
	public static final String QUERY_LIST_BY_TUTOR_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Tutor_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Verspaetet */
	public static final String QUERY_BY_VERSPAETET = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Verspaetet = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Verspaetet */
	public static final String QUERY_LIST_BY_VERSPAETET = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Verspaetet IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPV_Fach_ID */
	public static final String QUERY_BY_NPV_FACH_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPV_Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPV_Fach_ID */
	public static final String QUERY_LIST_BY_NPV_FACH_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPV_Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPV_NoteKrz */
	public static final String QUERY_BY_NPV_NOTEKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPV_NoteKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPV_NoteKrz */
	public static final String QUERY_LIST_BY_NPV_NOTEKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPV_NoteKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPV_Datum */
	public static final String QUERY_BY_NPV_DATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPV_Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPV_Datum */
	public static final String QUERY_LIST_BY_NPV_DATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPV_Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPAA_Fach_ID */
	public static final String QUERY_BY_NPAA_FACH_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPAA_Fach_ID */
	public static final String QUERY_LIST_BY_NPAA_FACH_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPAA_NoteKrz */
	public static final String QUERY_BY_NPAA_NOTEKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPAA_NoteKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPAA_NoteKrz */
	public static final String QUERY_LIST_BY_NPAA_NOTEKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPAA_NoteKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPAA_Datum */
	public static final String QUERY_BY_NPAA_DATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPAA_Datum */
	public static final String QUERY_LIST_BY_NPAA_DATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPBQ_Fach_ID */
	public static final String QUERY_BY_NPBQ_FACH_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPBQ_Fach_ID */
	public static final String QUERY_LIST_BY_NPBQ_FACH_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPBQ_NoteKrz */
	public static final String QUERY_BY_NPBQ_NOTEKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_NoteKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPBQ_NoteKrz */
	public static final String QUERY_LIST_BY_NPBQ_NOTEKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_NoteKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NPBQ_Datum */
	public static final String QUERY_BY_NPBQ_DATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NPBQ_Datum */
	public static final String QUERY_LIST_BY_NPBQ_DATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes VersetzungKrz */
	public static final String QUERY_BY_VERSETZUNGKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.VersetzungKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes VersetzungKrz */
	public static final String QUERY_LIST_BY_VERSETZUNGKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.VersetzungKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschlussArt */
	public static final String QUERY_BY_ABSCHLUSSART = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AbschlussArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschlussArt */
	public static final String QUERY_LIST_BY_ABSCHLUSSART = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AbschlussArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschlIstPrognose */
	public static final String QUERY_BY_ABSCHLISTPROGNOSE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AbschlIstPrognose = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschlIstPrognose */
	public static final String QUERY_LIST_BY_ABSCHLISTPROGNOSE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AbschlIstPrognose IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Konferenzdatum */
	public static final String QUERY_BY_KONFERENZDATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Konferenzdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Konferenzdatum */
	public static final String QUERY_LIST_BY_KONFERENZDATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Konferenzdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisDatum */
	public static final String QUERY_BY_ZEUGNISDATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisDatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisDatum */
	public static final String QUERY_LIST_BY_ZEUGNISDATUM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisDatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulgliederung */
	public static final String QUERY_BY_SCHULGLIEDERUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schulgliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulgliederung */
	public static final String QUERY_LIST_BY_SCHULGLIEDERUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schulgliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EPJahre */
	public static final String QUERY_BY_EPJAHRE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.EPJahre = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EPJahre */
	public static final String QUERY_LIST_BY_EPJAHRE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.EPJahre IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang_ID */
	public static final String QUERY_BY_JAHRGANG_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Jahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang_ID */
	public static final String QUERY_LIST_BY_JAHRGANG_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Jahrgang_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachklasse_ID */
	public static final String QUERY_BY_FACHKLASSE_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Fachklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachklasse_ID */
	public static final String QUERY_LIST_BY_FACHKLASSE_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Fachklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schwerpunkt_ID */
	public static final String QUERY_BY_SCHWERPUNKT_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schwerpunkt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schwerpunkt_ID */
	public static final String QUERY_LIST_BY_SCHWERPUNKT_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schwerpunkt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZeugnisBem */
	public static final String QUERY_BY_ZEUGNISBEM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisBem = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZeugnisBem */
	public static final String QUERY_LIST_BY_ZEUGNISBEM = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisBem IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schwerbehinderung */
	public static final String QUERY_BY_SCHWERBEHINDERUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schwerbehinderung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schwerbehinderung */
	public static final String QUERY_LIST_BY_SCHWERBEHINDERUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schwerbehinderung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Foerderschwerpunkt_ID */
	public static final String QUERY_BY_FOERDERSCHWERPUNKT_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Foerderschwerpunkt_ID */
	public static final String QUERY_LIST_BY_FOERDERSCHWERPUNKT_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes OrgFormKrz */
	public static final String QUERY_BY_ORGFORMKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.OrgFormKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes OrgFormKrz */
	public static final String QUERY_LIST_BY_ORGFORMKRZ = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.OrgFormKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes RefPaed */
	public static final String QUERY_BY_REFPAED = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.RefPaed = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes RefPaed */
	public static final String QUERY_LIST_BY_REFPAED = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.RefPaed IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassenart */
	public static final String QUERY_BY_KLASSENART = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Klassenart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassenart */
	public static final String QUERY_LIST_BY_KLASSENART = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Klassenart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SumFehlStd */
	public static final String QUERY_BY_SUMFEHLSTD = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SumFehlStd */
	public static final String QUERY_LIST_BY_SUMFEHLSTD = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SumFehlStdU */
	public static final String QUERY_BY_SUMFEHLSTDU = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStdU = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SumFehlStdU */
	public static final String QUERY_LIST_BY_SUMFEHLSTDU = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStdU IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wiederholung */
	public static final String QUERY_BY_WIEDERHOLUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Wiederholung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wiederholung */
	public static final String QUERY_LIST_BY_WIEDERHOLUNG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Wiederholung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gesamtnote_GS */
	public static final String QUERY_BY_GESAMTNOTE_GS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_GS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gesamtnote_GS */
	public static final String QUERY_LIST_BY_GESAMTNOTE_GS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_GS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gesamtnote_NW */
	public static final String QUERY_BY_GESAMTNOTE_NW = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_NW = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gesamtnote_NW */
	public static final String QUERY_LIST_BY_GESAMTNOTE_NW = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_NW IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Folgeklasse_ID */
	public static final String QUERY_BY_FOLGEKLASSE_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Folgeklasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Folgeklasse_ID */
	public static final String QUERY_LIST_BY_FOLGEKLASSE_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Folgeklasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Foerderschwerpunkt2_ID */
	public static final String QUERY_BY_FOERDERSCHWERPUNKT2_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt2_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Foerderschwerpunkt2_ID */
	public static final String QUERY_LIST_BY_FOERDERSCHWERPUNKT2_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt2_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschluss */
	public static final String QUERY_BY_ABSCHLUSS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Abschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschluss */
	public static final String QUERY_LIST_BY_ABSCHLUSS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Abschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschluss_B */
	public static final String QUERY_BY_ABSCHLUSS_B = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Abschluss_B = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschluss_B */
	public static final String QUERY_LIST_BY_ABSCHLUSS_B = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Abschluss_B IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DSNote */
	public static final String QUERY_BY_DSNOTE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DSNote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DSNote */
	public static final String QUERY_LIST_BY_DSNOTE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DSNote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AV_Leist */
	public static final String QUERY_BY_AV_LEIST = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AV_Leist = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AV_Leist */
	public static final String QUERY_LIST_BY_AV_LEIST = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AV_Leist IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AV_Zuv */
	public static final String QUERY_BY_AV_ZUV = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AV_Zuv = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AV_Zuv */
	public static final String QUERY_LIST_BY_AV_ZUV = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AV_Zuv IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AV_Selbst */
	public static final String QUERY_BY_AV_SELBST = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AV_Selbst = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AV_Selbst */
	public static final String QUERY_LIST_BY_AV_SELBST = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AV_Selbst IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SV_Verant */
	public static final String QUERY_BY_SV_VERANT = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SV_Verant = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SV_Verant */
	public static final String QUERY_LIST_BY_SV_VERANT = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SV_Verant IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SV_Konfl */
	public static final String QUERY_BY_SV_KONFL = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SV_Konfl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SV_Konfl */
	public static final String QUERY_LIST_BY_SV_KONFL = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SV_Konfl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SV_Koop */
	public static final String QUERY_BY_SV_KOOP = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SV_Koop = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SV_Koop */
	public static final String QUERY_LIST_BY_SV_KOOP = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.SV_Koop IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MoeglNPFaecher */
	public static final String QUERY_BY_MOEGLNPFAECHER = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.MoeglNPFaecher = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MoeglNPFaecher */
	public static final String QUERY_LIST_BY_MOEGLNPFAECHER = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.MoeglNPFaecher IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zertifikate */
	public static final String QUERY_BY_ZERTIFIKATE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Zertifikate = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zertifikate */
	public static final String QUERY_LIST_BY_ZERTIFIKATE = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Zertifikate IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumFHR */
	public static final String QUERY_BY_DATUMFHR = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DatumFHR = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumFHR */
	public static final String QUERY_LIST_BY_DATUMFHR = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DatumFHR IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PruefAlgoErgebnis */
	public static final String QUERY_BY_PRUEFALGOERGEBNIS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.PruefAlgoErgebnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PruefAlgoErgebnis */
	public static final String QUERY_LIST_BY_PRUEFALGOERGEBNIS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.PruefAlgoErgebnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zeugnisart */
	public static final String QUERY_BY_ZEUGNISART = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Zeugnisart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zeugnisart */
	public static final String QUERY_LIST_BY_ZEUGNISART = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Zeugnisart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumVon */
	public static final String QUERY_BY_DATUMVON = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DatumVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumVon */
	public static final String QUERY_LIST_BY_DATUMVON = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DatumVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes DatumBis */
	public static final String QUERY_BY_DATUMBIS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DatumBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes DatumBis */
	public static final String QUERY_LIST_BY_DATUMBIS = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.DatumBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FehlstundenGrenzwert */
	public static final String QUERY_BY_FEHLSTUNDENGRENZWERT = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.FehlstundenGrenzwert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FehlstundenGrenzwert */
	public static final String QUERY_LIST_BY_FEHLSTUNDENGRENZWERT = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.FehlstundenGrenzwert IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sonderpaedagoge_ID */
	public static final String QUERY_BY_SONDERPAEDAGOGE_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Sonderpaedagoge_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sonderpaedagoge_ID */
	public static final String QUERY_LIST_BY_SONDERPAEDAGOGE_ID = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Sonderpaedagoge_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachPraktAnteilAusr */
	public static final String QUERY_BY_FACHPRAKTANTEILAUSR = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.FachPraktAnteilAusr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachPraktAnteilAusr */
	public static final String QUERY_LIST_BY_FACHPRAKTANTEILAUSR = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.FachPraktAnteilAusr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BilingualerZweig */
	public static final String QUERY_BY_BILINGUALERZWEIG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.BilingualerZweig = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BilingualerZweig */
	public static final String QUERY_LIST_BY_BILINGUALERZWEIG = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.BilingualerZweig IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AOSF */
	public static final String QUERY_BY_AOSF = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AOSF = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AOSF */
	public static final String QUERY_LIST_BY_AOSF = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.AOSF IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Autist */
	public static final String QUERY_BY_AUTIST = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Autist = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Autist */
	public static final String QUERY_LIST_BY_AUTIST = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Autist IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZieldifferentesLernen */
	public static final String QUERY_BY_ZIELDIFFERENTESLERNEN = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ZieldifferentesLernen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZieldifferentesLernen */
	public static final String QUERY_LIST_BY_ZIELDIFFERENTESLERNEN = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.ZieldifferentesLernen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes meldungBAN */
	public static final String QUERY_BY_MELDUNGBAN = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.meldungBAN = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes meldungBAN */
	public static final String QUERY_LIST_BY_MELDUNGBAN = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.meldungBAN IN ?1";

	/** Eine eindeutige ID für den Lernabschnitt des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

	/** Wird für Wiederholungen im Laufenden Abschnitt genutzt 0=aktueller/neuester Abschnitt 1=vor dem ersten Wechsel 2=vor dem zweiten Wechsel usw */
	@Column(name = "WechselNr")
	@JsonProperty
	public Integer WechselNr;

	/** Schulbesuchsjahre für den Lernabschnitt */
	@Column(name = "Schulbesuchsjahre")
	@JsonProperty
	public Integer Schulbesuchsjahre;

	/** Lernabschnitt ist Hochrechnung (nur noch BK) */
	@Column(name = "Hochrechnung")
	@JsonProperty
	public Integer Hochrechnung;

	/** Gewerteter Abschnitt (Ja/Nein) */
	@Column(name = "SemesterWertung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean SemesterWertung;

	/** Prüfungsordnung des Lernabschnitts */
	@Column(name = "PruefOrdnung")
	@JsonProperty
	public String PruefOrdnung;

	/** Klassen_ID des Schülers für den Lernabschnitt */
	@Column(name = "Klassen_ID")
	@JsonProperty
	public Long Klassen_ID;

	/** Die Lehrer-ID des Tutors, der dem Schüler zugeordnet ist, oder null falls keine Zuordnung vorgenommen wurde */
	@Column(name = "Tutor_ID")
	@JsonProperty
	public Long Tutor_ID;

	/** ID des Nachprüfungsfaches */
	@Column(name = "Verspaetet")
	@JsonProperty
	public Integer Verspaetet;

	/** TODO: Note der Nachprüfung */
	@Column(name = "NPV_Fach_ID")
	@JsonProperty
	public Long NPV_Fach_ID;

	/** TODO: Datum der Nachprüfung */
	@Column(name = "NPV_NoteKrz")
	@JsonProperty
	public String NPV_NoteKrz;

	/** TODO: BK: ID des Nachprüfungsfaches für den allgemein-bildenen Abschluss */
	@Column(name = "NPV_Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String NPV_Datum;

	/** TODO: BK: Note des Nachprüfungsfaches für den allgemein-bildenen Abschluss */
	@Column(name = "NPAA_Fach_ID")
	@JsonProperty
	public Long NPAA_Fach_ID;

	/** TODO: BK: Datum der Nachprüfung für den allgemein-bildenen Abschluss */
	@Column(name = "NPAA_NoteKrz")
	@JsonProperty
	public String NPAA_NoteKrz;

	/** TODO: BK: dito für berufs-qualifizierende Nachprüfung */
	@Column(name = "NPAA_Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String NPAA_Datum;

	/** TODO: BK: dito für berufs-qualifizierende Nachprüfung */
	@Column(name = "NPBQ_Fach_ID")
	@JsonProperty
	public Long NPBQ_Fach_ID;

	/** TODO: BK: dito für berufs-qualifizierende Nachprüfung */
	@Column(name = "NPBQ_NoteKrz")
	@JsonProperty
	public String NPBQ_NoteKrz;

	/** TODO: ID des Nachprüfungsfaches */
	@Column(name = "NPBQ_Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String NPBQ_Datum;

	/** Kürzel des Versetungsvermerk */
	@Column(name = "VersetzungKrz")
	@JsonProperty
	public String VersetzungKrz;

	/** Art des Abschlusses */
	@Column(name = "AbschlussArt")
	@JsonProperty
	public Integer AbschlussArt;

	/** Gibt an ob Abschluss Prognose ist (GE, PS und SK) */
	@Column(name = "AbschlIstPrognose")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AbschlIstPrognose;

	/** Konferenzdatum */
	@Column(name = "Konferenzdatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Konferenzdatum;

	/** Zeugnisdatum */
	@Column(name = "ZeugnisDatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String ZeugnisDatum;

	/** ASD-Kürzel SGL */
	@Column(name = "ASDSchulgliederung")
	@JsonProperty
	public String Schulgliederung;

	/** ASD-Jahrgang kann alles über ID geregelt werden */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** bisherige Anzahl der Jahre in der Schuleingangssphase */
	@Column(name = "EPJahre")
	@JsonProperty
	public Integer EPJahre;

	/** ID des Jahrgangs der zum Report zugeordnet wird */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** ID der Fachklasse (BK) */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** ID des Schwerpunkts aus dem Katalog */
	@Column(name = "Schwerpunkt_ID")
	@JsonProperty
	public Long Schwerpunkt_ID;

	/** Text für Zeugnisbemerkung */
	@Column(name = "ZeugnisBem")
	@JsonProperty
	public String ZeugnisBem;

	/** Schwerbehinderung (Ja/Nein) */
	@Column(name = "Schwerbehinderung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Schwerbehinderung;

	/** ID Hauptförderschwerpunkt */
	@Column(name = "Foerderschwerpunkt_ID")
	@JsonProperty
	public Long Foerderschwerpunkt_ID;

	/** Kürzel der Organisationsform */
	@Column(name = "OrgFormKrz")
	@JsonProperty
	public String OrgFormKrz;

	/** TODO DEPRECATED: Reformpädagogik */
	@Column(name = "RefPaed")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RefPaed;

	/** Klassenart */
	@Column(name = "Klassenart")
	@JsonProperty
	public String Klassenart;

	/** Summer der Fehlstunden */
	@Column(name = "SumFehlStd")
	@JsonProperty
	public Integer SumFehlStd;

	/** Summer der Fehlstunden unentschuldigt */
	@Column(name = "SumFehlStdU")
	@JsonProperty
	public Integer SumFehlStdU;

	/** Lernabschnitt wurde wiederholt (Ja/Nein) */
	@Column(name = "Wiederholung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Wiederholung;

	/** Lernbereichnote Gesellschaftswissenschaft oder Arbeitlehre HA10 */
	@Column(name = "Gesamtnote_GS")
	@JsonProperty
	public Integer Gesamtnote_GS;

	/** Lernbereichnote Naturwissenschaft HA10 */
	@Column(name = "Gesamtnote_NW")
	@JsonProperty
	public Integer Gesamtnote_NW;

	/** ID der Folgeklasse für den Lernabschnitt, sofern dieser vom Standard der Klassentabelle abweicht */
	@Column(name = "Folgeklasse_ID")
	@JsonProperty
	public Long Folgeklasse_ID;

	/** Weiterer Förderschwerpunkt ID */
	@Column(name = "Foerderschwerpunkt2_ID")
	@JsonProperty
	public Long Foerderschwerpunkt2_ID;

	/** allgemeinbildender Abschluss */
	@Column(name = "Abschluss")
	@JsonProperty
	public String Abschluss;

	/** berufsbezogener Abschnluss (BK) */
	@Column(name = "Abschluss_B")
	@JsonProperty
	public String Abschluss_B;

	/** Durchschnittsnote im betreffenden Abschnitt Ist allerdings in der Programmoberfläche nicht verfügbar Der Inhalt wird durch die Prüfungsalgorithmen gefüllt es ist eine Ausgabe in Reports möglich */
	@Column(name = "DSNote")
	@JsonProperty
	public String DSNote;

	/** DEPRECATED: Kopfnote */
	@Column(name = "AV_Leist")
	@JsonProperty
	public Integer AV_Leist;

	/** DEPRECATED: Kopfnote */
	@Column(name = "AV_Zuv")
	@JsonProperty
	public Integer AV_Zuv;

	/** DEPRECATED: Kopfnote */
	@Column(name = "AV_Selbst")
	@JsonProperty
	public Integer AV_Selbst;

	/** DEPRECATED: Kopfnote */
	@Column(name = "SV_Verant")
	@JsonProperty
	public Integer SV_Verant;

	/** DEPRECATED: Kopfnote */
	@Column(name = "SV_Konfl")
	@JsonProperty
	public Integer SV_Konfl;

	/** DEPRECATED: Kopfnote */
	@Column(name = "SV_Koop")
	@JsonProperty
	public Integer SV_Koop;

	/** Auflistung der mögllichen Nachprüfungsfächer kommagetrennt */
	@Column(name = "MoeglNPFaecher")
	@JsonProperty
	public String MoeglNPFaecher;

	/** DEPRECATED: Hier war mal geplant, Texte für Zertifikate einzugeben */
	@Column(name = "Zertifikate")
	@JsonProperty
	public String Zertifikate;

	/** Datum FHR */
	@Column(name = "DatumFHR")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumFHR;

	/** Text für die Ergebnisse der Abschlussberechnungen */
	@Column(name = "PruefAlgoErgebnis")
	@JsonProperty
	public String PruefAlgoErgebnis;

	/** Angabe der Zeugnisart */
	@Column(name = "Zeugnisart")
	@JsonProperty
	public String Zeugnisart;

	/** Beginn Lernabschnitt */
	@Column(name = "DatumVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumVon;

	/** Ende Lernabschnitt */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumBis;

	/** Grenzwert für Fehlstunden (BK Warnbriefe zur Entlassung) */
	@Column(name = "FehlstundenGrenzwert")
	@JsonProperty
	public Integer FehlstundenGrenzwert;

	/** Hier kann die ID einer Lehrkraft eingetragen werden die dann die Schüler als Förderpädagoge betreut und im Notenmodul hat */
	@Column(name = "Sonderpaedagoge_ID")
	@JsonProperty
	public Long Sonderpaedagoge_ID;

	/** Enthält die Angabe, ob die Fachpraktischen Anteile in Anlage B08 B09 B10 ausreichend sind für Versetzung (BK) */
	@Column(name = "FachPraktAnteilAusr")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean FachPraktAnteilAusr;

	/** Sprache des Bilingualen Zweigs */
	@Column(name = "BilingualerZweig")
	@JsonProperty
	public String BilingualerZweig;

	/** Gibt an ob der Schüler ein AOSF hat */
	@Column(name = "AOSF")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AOSF;

	/** Gibt an ob Autismuss vorliegt (Ja/Nein) */
	@Column(name = "Autist")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Autist;

	/** Gibt an ob der Schüler zieldifferent unterrichtet wird */
	@Column(name = "ZieldifferentesLernen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ZieldifferentesLernen;

	/** Gibt an, ob der Schüler im aktuellen Abschnitt an das BAN-Portal gemeldet werden soll (1) oder nicht (0) */
	@Column(name = "meldungBAN")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean meldungBAN;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerLernabschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param FachPraktAnteilAusr   der Wert für das Attribut FachPraktAnteilAusr
	 * @param meldungBAN   der Wert für das Attribut meldungBAN
	 */
	public DTOSchuelerLernabschnittsdaten(final long ID, final long Schueler_ID, final long Schuljahresabschnitts_ID, final Boolean FachPraktAnteilAusr, final Boolean meldungBAN) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (FachPraktAnteilAusr == null) {
			throw new NullPointerException("FachPraktAnteilAusr must not be null");
		}
		this.FachPraktAnteilAusr = FachPraktAnteilAusr;
		this.meldungBAN = meldungBAN;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerLernabschnittsdaten other = (DTOSchuelerLernabschnittsdaten) obj;
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
		return "DTOSchuelerLernabschnittsdaten(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", WechselNr=" + this.WechselNr + ", Schulbesuchsjahre=" + this.Schulbesuchsjahre + ", Hochrechnung=" + this.Hochrechnung + ", SemesterWertung=" + this.SemesterWertung + ", PruefOrdnung=" + this.PruefOrdnung + ", Klassen_ID=" + this.Klassen_ID + ", Tutor_ID=" + this.Tutor_ID + ", Verspaetet=" + this.Verspaetet + ", NPV_Fach_ID=" + this.NPV_Fach_ID + ", NPV_NoteKrz=" + this.NPV_NoteKrz + ", NPV_Datum=" + this.NPV_Datum + ", NPAA_Fach_ID=" + this.NPAA_Fach_ID + ", NPAA_NoteKrz=" + this.NPAA_NoteKrz + ", NPAA_Datum=" + this.NPAA_Datum + ", NPBQ_Fach_ID=" + this.NPBQ_Fach_ID + ", NPBQ_NoteKrz=" + this.NPBQ_NoteKrz + ", NPBQ_Datum=" + this.NPBQ_Datum + ", VersetzungKrz=" + this.VersetzungKrz + ", AbschlussArt=" + this.AbschlussArt + ", AbschlIstPrognose=" + this.AbschlIstPrognose + ", Konferenzdatum=" + this.Konferenzdatum + ", ZeugnisDatum=" + this.ZeugnisDatum + ", Schulgliederung=" + this.Schulgliederung + ", ASDJahrgang=" + this.ASDJahrgang + ", EPJahre=" + this.EPJahre + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Schwerpunkt_ID=" + this.Schwerpunkt_ID + ", ZeugnisBem=" + this.ZeugnisBem + ", Schwerbehinderung=" + this.Schwerbehinderung + ", Foerderschwerpunkt_ID=" + this.Foerderschwerpunkt_ID + ", OrgFormKrz=" + this.OrgFormKrz + ", RefPaed=" + this.RefPaed + ", Klassenart=" + this.Klassenart + ", SumFehlStd=" + this.SumFehlStd + ", SumFehlStdU=" + this.SumFehlStdU + ", Wiederholung=" + this.Wiederholung + ", Gesamtnote_GS=" + this.Gesamtnote_GS + ", Gesamtnote_NW=" + this.Gesamtnote_NW + ", Folgeklasse_ID=" + this.Folgeklasse_ID + ", Foerderschwerpunkt2_ID=" + this.Foerderschwerpunkt2_ID + ", Abschluss=" + this.Abschluss + ", Abschluss_B=" + this.Abschluss_B + ", DSNote=" + this.DSNote + ", AV_Leist=" + this.AV_Leist + ", AV_Zuv=" + this.AV_Zuv + ", AV_Selbst=" + this.AV_Selbst + ", SV_Verant=" + this.SV_Verant + ", SV_Konfl=" + this.SV_Konfl + ", SV_Koop=" + this.SV_Koop + ", MoeglNPFaecher=" + this.MoeglNPFaecher + ", Zertifikate=" + this.Zertifikate + ", DatumFHR=" + this.DatumFHR + ", PruefAlgoErgebnis=" + this.PruefAlgoErgebnis + ", Zeugnisart=" + this.Zeugnisart + ", DatumVon=" + this.DatumVon + ", DatumBis=" + this.DatumBis + ", FehlstundenGrenzwert=" + this.FehlstundenGrenzwert + ", Sonderpaedagoge_ID=" + this.Sonderpaedagoge_ID + ", FachPraktAnteilAusr=" + this.FachPraktAnteilAusr + ", BilingualerZweig=" + this.BilingualerZweig + ", AOSF=" + this.AOSF + ", Autist=" + this.Autist + ", ZieldifferentesLernen=" + this.ZieldifferentesLernen + ", meldungBAN=" + this.meldungBAN + ")";
	}

}
