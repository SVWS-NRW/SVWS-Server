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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLernabschnittsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLernabschnittsdaten")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.all", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schuljahresabschnitts_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schuljahresabschnitts_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.wechselnr", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.WechselNr = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.wechselnr.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.WechselNr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schulbesuchsjahre", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schulbesuchsjahre = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schulbesuchsjahre.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schulbesuchsjahre IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.hochrechnung", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Hochrechnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.hochrechnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Hochrechnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.semesterwertung", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SemesterWertung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.semesterwertung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SemesterWertung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.pruefordnung", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.PruefOrdnung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.pruefordnung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.PruefOrdnung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klassen_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klassen_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klassen_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klassen_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.tutor_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Tutor_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.tutor_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Tutor_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.verspaetet", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Verspaetet = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.verspaetet.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Verspaetet IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npv_fach_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPV_Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npv_fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPV_Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npv_notekrz", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPV_NoteKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npv_notekrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPV_NoteKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npv_datum", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPV_Datum = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npv_datum.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPV_Datum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npaa_fach_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npaa_fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npaa_notekrz", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPAA_NoteKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npaa_notekrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPAA_NoteKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npaa_datum", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Datum = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npaa_datum.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPAA_Datum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npbq_fach_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npbq_fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npbq_notekrz", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_NoteKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npbq_notekrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_NoteKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npbq_datum", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Datum = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.npbq_datum.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.NPBQ_Datum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.versetzungkrz", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.VersetzungKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.versetzungkrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.VersetzungKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschlussart", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AbschlussArt = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschlussart.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AbschlussArt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschlistprognose", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AbschlIstPrognose = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschlistprognose.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AbschlIstPrognose IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.konferenzdatum", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Konferenzdatum = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.konferenzdatum.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Konferenzdatum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zeugnisdatum", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisDatum = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zeugnisdatum.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisDatum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klassenlehrer", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klassenlehrer = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klassenlehrer.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klassenlehrer IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schulgliederung", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schulgliederung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schulgliederung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schulgliederung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.asdjahrgang", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ASDJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.asdjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.jahrgang_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.jahrgang_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.fachklasse_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.fachklasse_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schwerpunkt_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schwerpunkt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schwerpunkt_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schwerpunkt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zeugnisbem", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisBem = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zeugnisbem.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ZeugnisBem IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schwerbehinderung", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schwerbehinderung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schwerbehinderung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Schwerbehinderung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.foerderschwerpunkt_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.foerderschwerpunkt_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.orgformkrz", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.OrgFormKrz = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.orgformkrz.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.OrgFormKrz IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.refpaed", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.RefPaed = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.refpaed.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.RefPaed IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klassenart", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klassenart = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klassenart.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klassenart IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sumfehlstd", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStd = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sumfehlstd.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStd IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sumfehlstdu", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStdU = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sumfehlstdu.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SumFehlStdU IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.wiederholung", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Wiederholung = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.wiederholung.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Wiederholung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.gesamtnote_gs", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_GS = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.gesamtnote_gs.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_GS IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.gesamtnote_nw", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_NW = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.gesamtnote_nw.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Gesamtnote_NW IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.folgeklasse_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Folgeklasse_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.folgeklasse_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Folgeklasse_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.foerderschwerpunkt2_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt2_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.foerderschwerpunkt2_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Foerderschwerpunkt2_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschluss", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Abschluss = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschluss.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Abschluss IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschluss_b", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Abschluss_B = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschluss_b.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Abschluss_B IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.dsnote", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DSNote = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.dsnote.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DSNote IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.av_leist", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AV_Leist = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.av_leist.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AV_Leist IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.av_zuv", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AV_Zuv = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.av_zuv.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AV_Zuv IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.av_selbst", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AV_Selbst = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.av_selbst.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AV_Selbst IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sv_verant", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SV_Verant = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sv_verant.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SV_Verant IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sv_konfl", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SV_Konfl = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sv_konfl.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SV_Konfl IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sv_koop", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SV_Koop = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sv_koop.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SV_Koop IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.stvklassenlehrer_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.StvKlassenlehrer_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.stvklassenlehrer_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.StvKlassenlehrer_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.moeglnpfaecher", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.MoeglNPFaecher = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.moeglnpfaecher.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.MoeglNPFaecher IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zertifikate", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Zertifikate = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zertifikate.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Zertifikate IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.datumfhr", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DatumFHR = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.datumfhr.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DatumFHR IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.pruefalgoergebnis", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.PruefAlgoErgebnis = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.pruefalgoergebnis.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.PruefAlgoErgebnis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zeugnisart", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Zeugnisart = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zeugnisart.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Zeugnisart IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.datumvon", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DatumVon = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.datumvon.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DatumVon IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.datumbis", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DatumBis = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.datumbis.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.DatumBis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.fehlstundengrenzwert", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.FehlstundenGrenzwert = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.fehlstundengrenzwert.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.FehlstundenGrenzwert IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sonderpaedagoge_id", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Sonderpaedagoge_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.sonderpaedagoge_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Sonderpaedagoge_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.fachpraktanteilausr", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.FachPraktAnteilAusr = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.fachpraktanteilausr.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.FachPraktAnteilAusr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.bilingualerzweig", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.BilingualerZweig = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.bilingualerzweig.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.BilingualerZweig IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.aosf", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AOSF = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.aosf.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.AOSF IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.autist", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Autist = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.autist.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Autist IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zieldifferenteslernen", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ZieldifferentesLernen = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.zieldifferenteslernen.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ZieldifferentesLernen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.jahr", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Jahr = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.jahr.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Jahr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschnitt", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.abschnitt.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klasse", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klasse = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.klasse.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Klasse IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.folgeklasse", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Folgeklasse = :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.folgeklasse.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.Folgeklasse IN :value")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOSchuelerLernabschnittsdaten.all.migration", query = "SELECT e FROM MigrationDTOSchuelerLernabschnittsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Schuljahresabschnitts_ID", "WechselNr", "Schulbesuchsjahre", "Hochrechnung", "SemesterWertung", "PruefOrdnung", "Klassen_ID", "Tutor_ID", "Verspaetet", "NPV_Fach_ID", "NPV_NoteKrz", "NPV_Datum", "NPAA_Fach_ID", "NPAA_NoteKrz", "NPAA_Datum", "NPBQ_Fach_ID", "NPBQ_NoteKrz", "NPBQ_Datum", "VersetzungKrz", "AbschlussArt", "AbschlIstPrognose", "Konferenzdatum", "ZeugnisDatum", "Klassenlehrer", "Schulgliederung", "ASDJahrgang", "Jahrgang_ID", "Fachklasse_ID", "Schwerpunkt_ID", "ZeugnisBem", "Schwerbehinderung", "Foerderschwerpunkt_ID", "OrgFormKrz", "RefPaed", "Klassenart", "SumFehlStd", "SumFehlStdU", "Wiederholung", "Gesamtnote_GS", "Gesamtnote_NW", "Folgeklasse_ID", "Foerderschwerpunkt2_ID", "Abschluss", "Abschluss_B", "DSNote", "AV_Leist", "AV_Zuv", "AV_Selbst", "SV_Verant", "SV_Konfl", "SV_Koop", "StvKlassenlehrer_ID", "MoeglNPFaecher", "Zertifikate", "DatumFHR", "PruefAlgoErgebnis", "Zeugnisart", "DatumVon", "DatumBis", "FehlstundenGrenzwert", "Sonderpaedagoge_ID", "FachPraktAnteilAusr", "BilingualerZweig", "AOSF", "Autist", "ZieldifferentesLernen", "Jahr", "Abschnitt", "SchulnrEigner", "Klasse", "Folgeklasse"})
public final class MigrationDTOSchuelerLernabschnittsdaten {

	/** Eine eindeutige ID für den Lernabschnitt des Schülers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AbschlIstPrognose;

	/** Konferenzdatum */
	@Column(name = "Konferenzdatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Konferenzdatum;

	/** Zeugnisdatum */
	@Column(name = "ZeugnisDatum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String ZeugnisDatum;

	/** Klassenlehrer Kürzel (hier muss ID rein) */
	@Column(name = "Klassenlehrer")
	@JsonProperty
	public String Klassenlehrer;

	/** ASD-Kürzel SGL */
	@Column(name = "ASDSchulgliederung")
	@JsonProperty
	public String Schulgliederung;

	/** ASD-Jahrgang kann alles über ID geregelt werden */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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

	/** Stellvertretender Klassenlehrer Kürzel sollte ID sein */
	@Column(name = "StvKlassenlehrer_ID")
	@JsonProperty
	public Long StvKlassenlehrer_ID;

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
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String DatumVon;

	/** Ende Lernabschnitt */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
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
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean FachPraktAnteilAusr;

	/** Sprache des Bilingualen Zweigs */
	@Column(name = "BilingualerZweig")
	@JsonProperty
	public String BilingualerZweig;

	/** Gibt an ob der Schüler ein AOSF hat */
	@Column(name = "AOSF")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean AOSF;

	/** Gibt an ob Autismuss vorliegt (Ja/Nein) */
	@Column(name = "Autist")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Autist;

	/** Gibt an ob der Schüler zieldifferent unterrichtet wird */
	@Column(name = "ZieldifferentesLernen")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ZieldifferentesLernen;

	/** DEPRECATED: Schuljahr des Lernabschnitts */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** DEPRECATED: Abschnitt des Lernabschnitts */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** DEPRECATED: Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: Klassen-Bezeichnung für den Lernabschnitts */
	@Column(name = "Klasse")
	@JsonProperty
	public String Klasse;

	/** DEPRECATED: Bezeichnung der Folgeklasse für den Lernabschnitt des Schülers */
	@Column(name = "Folgeklasse")
	@JsonProperty
	public String Folgeklasse;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerLernabschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param FachPraktAnteilAusr   der Wert für das Attribut FachPraktAnteilAusr
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOSchuelerLernabschnittsdaten(final Long ID, final Long Schueler_ID, final Long Schuljahresabschnitts_ID, final Boolean FachPraktAnteilAusr, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Schuljahresabschnitts_ID == null) {
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (FachPraktAnteilAusr == null) {
			throw new NullPointerException("FachPraktAnteilAusr must not be null");
		}
		this.FachPraktAnteilAusr = FachPraktAnteilAusr;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerLernabschnittsdaten other = (MigrationDTOSchuelerLernabschnittsdaten) obj;
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
		return "MigrationDTOSchuelerLernabschnittsdaten(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", WechselNr=" + this.WechselNr + ", Schulbesuchsjahre=" + this.Schulbesuchsjahre + ", Hochrechnung=" + this.Hochrechnung + ", SemesterWertung=" + this.SemesterWertung + ", PruefOrdnung=" + this.PruefOrdnung + ", Klassen_ID=" + this.Klassen_ID + ", Tutor_ID=" + this.Tutor_ID + ", Verspaetet=" + this.Verspaetet + ", NPV_Fach_ID=" + this.NPV_Fach_ID + ", NPV_NoteKrz=" + this.NPV_NoteKrz + ", NPV_Datum=" + this.NPV_Datum + ", NPAA_Fach_ID=" + this.NPAA_Fach_ID + ", NPAA_NoteKrz=" + this.NPAA_NoteKrz + ", NPAA_Datum=" + this.NPAA_Datum + ", NPBQ_Fach_ID=" + this.NPBQ_Fach_ID + ", NPBQ_NoteKrz=" + this.NPBQ_NoteKrz + ", NPBQ_Datum=" + this.NPBQ_Datum + ", VersetzungKrz=" + this.VersetzungKrz + ", AbschlussArt=" + this.AbschlussArt + ", AbschlIstPrognose=" + this.AbschlIstPrognose + ", Konferenzdatum=" + this.Konferenzdatum + ", ZeugnisDatum=" + this.ZeugnisDatum + ", Klassenlehrer=" + this.Klassenlehrer + ", Schulgliederung=" + this.Schulgliederung + ", ASDJahrgang=" + this.ASDJahrgang + ", Jahrgang_ID=" + this.Jahrgang_ID + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Schwerpunkt_ID=" + this.Schwerpunkt_ID + ", ZeugnisBem=" + this.ZeugnisBem + ", Schwerbehinderung=" + this.Schwerbehinderung + ", Foerderschwerpunkt_ID=" + this.Foerderschwerpunkt_ID + ", OrgFormKrz=" + this.OrgFormKrz + ", RefPaed=" + this.RefPaed + ", Klassenart=" + this.Klassenart + ", SumFehlStd=" + this.SumFehlStd + ", SumFehlStdU=" + this.SumFehlStdU + ", Wiederholung=" + this.Wiederholung + ", Gesamtnote_GS=" + this.Gesamtnote_GS + ", Gesamtnote_NW=" + this.Gesamtnote_NW + ", Folgeklasse_ID=" + this.Folgeklasse_ID + ", Foerderschwerpunkt2_ID=" + this.Foerderschwerpunkt2_ID + ", Abschluss=" + this.Abschluss + ", Abschluss_B=" + this.Abschluss_B + ", DSNote=" + this.DSNote + ", AV_Leist=" + this.AV_Leist + ", AV_Zuv=" + this.AV_Zuv + ", AV_Selbst=" + this.AV_Selbst + ", SV_Verant=" + this.SV_Verant + ", SV_Konfl=" + this.SV_Konfl + ", SV_Koop=" + this.SV_Koop + ", StvKlassenlehrer_ID=" + this.StvKlassenlehrer_ID + ", MoeglNPFaecher=" + this.MoeglNPFaecher + ", Zertifikate=" + this.Zertifikate + ", DatumFHR=" + this.DatumFHR + ", PruefAlgoErgebnis=" + this.PruefAlgoErgebnis + ", Zeugnisart=" + this.Zeugnisart + ", DatumVon=" + this.DatumVon + ", DatumBis=" + this.DatumBis + ", FehlstundenGrenzwert=" + this.FehlstundenGrenzwert + ", Sonderpaedagoge_ID=" + this.Sonderpaedagoge_ID + ", FachPraktAnteilAusr=" + this.FachPraktAnteilAusr + ", BilingualerZweig=" + this.BilingualerZweig + ", AOSF=" + this.AOSF + ", Autist=" + this.Autist + ", ZieldifferentesLernen=" + this.ZieldifferentesLernen + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ", SchulnrEigner=" + this.SchulnrEigner + ", Klasse=" + this.Klasse + ", Folgeklasse=" + this.Folgeklasse + ")";
	}

}
