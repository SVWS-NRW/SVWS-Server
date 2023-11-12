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
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "MigrationDTOSchueler.all", query = "SELECT e FROM MigrationDTOSchueler e")
@NamedQuery(name = "MigrationDTOSchueler.id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schuljahresabschnitts_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.schuljahresabschnitts_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.gu_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GU_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.gu_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GU_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.srcid", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SrcID = :value")
@NamedQuery(name = "MigrationDTOSchueler.srcid.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SrcID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.idext", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.IDext = :value")
@NamedQuery(name = "MigrationDTOSchueler.idext.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.IDext IN :value")
@NamedQuery(name = "MigrationDTOSchueler.status", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Status = :value")
@NamedQuery(name = "MigrationDTOSchueler.status.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Status IN :value")
@NamedQuery(name = "MigrationDTOSchueler.nachname", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Nachname = :value")
@NamedQuery(name = "MigrationDTOSchueler.nachname.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Nachname IN :value")
@NamedQuery(name = "MigrationDTOSchueler.vorname", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Vorname = :value")
@NamedQuery(name = "MigrationDTOSchueler.vorname.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Vorname IN :value")
@NamedQuery(name = "MigrationDTOSchueler.allevornamen", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AlleVornamen = :value")
@NamedQuery(name = "MigrationDTOSchueler.allevornamen.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AlleVornamen IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtsname", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsname = :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtsname.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsname IN :value")
@NamedQuery(name = "MigrationDTOSchueler.strasse", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strasse = :value")
@NamedQuery(name = "MigrationDTOSchueler.strasse.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strasse IN :value")
@NamedQuery(name = "MigrationDTOSchueler.strassenname", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strassenname = :value")
@NamedQuery(name = "MigrationDTOSchueler.strassenname.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Strassenname IN :value")
@NamedQuery(name = "MigrationDTOSchueler.hausnr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNr = :value")
@NamedQuery(name = "MigrationDTOSchueler.hausnr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNr IN :value")
@NamedQuery(name = "MigrationDTOSchueler.hausnrzusatz", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNrZusatz = :value")
@NamedQuery(name = "MigrationDTOSchueler.hausnrzusatz.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name = "MigrationDTOSchueler.ort_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ort_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.ort_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ort_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.plz", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.PLZ = :value")
@NamedQuery(name = "MigrationDTOSchueler.plz.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.PLZ IN :value")
@NamedQuery(name = "MigrationDTOSchueler.ortabk", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrtAbk = :value")
@NamedQuery(name = "MigrationDTOSchueler.ortabk.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrtAbk IN :value")
@NamedQuery(name = "MigrationDTOSchueler.ortsteil_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ortsteil_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.ortsteil_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ortsteil_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.telefon", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Telefon = :value")
@NamedQuery(name = "MigrationDTOSchueler.telefon.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Telefon IN :value")
@NamedQuery(name = "MigrationDTOSchueler.email", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Email = :value")
@NamedQuery(name = "MigrationDTOSchueler.email.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Email IN :value")
@NamedQuery(name = "MigrationDTOSchueler.fax", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fax = :value")
@NamedQuery(name = "MigrationDTOSchueler.fax.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fax IN :value")
@NamedQuery(name = "MigrationDTOSchueler.klassen_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassen_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.klassen_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassen_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrgangschueler", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrgangSchueler = :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrgangschueler.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrgangSchueler IN :value")
@NamedQuery(name = "MigrationDTOSchueler.pruefordnung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.PruefOrdnung = :value")
@NamedQuery(name = "MigrationDTOSchueler.pruefordnung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.PruefOrdnung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtsdatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsdatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtsdatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsdatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtsort", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsort = :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtsort.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geburtsort IN :value")
@NamedQuery(name = "MigrationDTOSchueler.volljaehrig", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Volljaehrig = :value")
@NamedQuery(name = "MigrationDTOSchueler.volljaehrig.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Volljaehrig IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geschlecht", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geschlecht = :value")
@NamedQuery(name = "MigrationDTOSchueler.geschlecht.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geschlecht IN :value")
@NamedQuery(name = "MigrationDTOSchueler.staatkrz", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz = :value")
@NamedQuery(name = "MigrationDTOSchueler.staatkrz.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz IN :value")
@NamedQuery(name = "MigrationDTOSchueler.staatkrz2", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz2 = :value")
@NamedQuery(name = "MigrationDTOSchueler.staatkrz2.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatKrz2 IN :value")
@NamedQuery(name = "MigrationDTOSchueler.staatabk", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk = :value")
@NamedQuery(name = "MigrationDTOSchueler.staatabk.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk IN :value")
@NamedQuery(name = "MigrationDTOSchueler.aussiedler", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aussiedler = :value")
@NamedQuery(name = "MigrationDTOSchueler.aussiedler.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aussiedler IN :value")
@NamedQuery(name = "MigrationDTOSchueler.religion_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religion_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.religion_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religion_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.religionabk", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ReligionAbk = :value")
@NamedQuery(name = "MigrationDTOSchueler.religionabk.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ReligionAbk IN :value")
@NamedQuery(name = "MigrationDTOSchueler.religionsabmeldung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsabmeldung = :value")
@NamedQuery(name = "MigrationDTOSchueler.religionsabmeldung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsabmeldung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.religionsanmeldung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsanmeldung = :value")
@NamedQuery(name = "MigrationDTOSchueler.religionsanmeldung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Religionsanmeldung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.bafoeg", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bafoeg = :value")
@NamedQuery(name = "MigrationDTOSchueler.bafoeg.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bafoeg IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schwerbehinderung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerbehinderung = :value")
@NamedQuery(name = "MigrationDTOSchueler.schwerbehinderung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerbehinderung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.sportbefreiung_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Sportbefreiung_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.sportbefreiung_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Sportbefreiung_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.fahrschueler_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fahrschueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.fahrschueler_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fahrschueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.haltestelle_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Haltestelle_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.haltestelle_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Haltestelle_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.haltestelleabk", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HaltestelleAbk = :value")
@NamedQuery(name = "MigrationDTOSchueler.haltestelleabk.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HaltestelleAbk IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulgliederung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulgliederung = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulgliederung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulgliederung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrgang_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrgang_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.asdjahrgang", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ASDJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchueler.asdjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchueler.fachklasse_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.fachklasse_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulpflichterf", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulpflichtErf = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulpflichterf.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulpflichtErf IN :value")
@NamedQuery(name = "MigrationDTOSchueler.aufnahmedatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aufnahmedatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.aufnahmedatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Aufnahmedatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.einschulungsjahr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsjahr = :value")
@NamedQuery(name = "MigrationDTOSchueler.einschulungsjahr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsjahr IN :value")
@NamedQuery(name = "MigrationDTOSchueler.einschulungsart_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsart_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.einschulungsart_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Einschulungsart_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulnr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulNr = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulnr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulNr IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulformsim", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulformSIM = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulformsim.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulformSIM IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsjahrgang", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulentlassdatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulEntlassDatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulentlassdatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulEntlassDatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsversetzung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSVersetzung = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsversetzung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSVersetzung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsfachklkennung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklKennung = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsfachklkennung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklKennung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsfachklsim", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklSIM = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsfachklsim.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSFachklSIM IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsentlassgrund", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassgrund = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsentlassgrund.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassgrund IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsentlassart", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassArt = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsentlassart.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSEntlassArt IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsklassenart", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSKlassenart = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsklassenart.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSKlassenart IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsrefpaed", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSRefPaed = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsrefpaed.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSRefPaed IN :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassjahrgang", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang = :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassjahrgang_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassjahrgang_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassjahrgang_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassdatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassdatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassdatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassdatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassgrund", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassgrund = :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassgrund.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassgrund IN :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassart", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassart = :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassart.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Entlassart IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulwechselnr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulwechselNr = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulwechselnr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulwechselNr IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulwechseldatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulwechseldatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulwechseldatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schulwechseldatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geloescht", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geloescht = :value")
@NamedQuery(name = "MigrationDTOSchueler.geloescht.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Geloescht IN :value")
@NamedQuery(name = "MigrationDTOSchueler.gesperrt", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Gesperrt = :value")
@NamedQuery(name = "MigrationDTOSchueler.gesperrt.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Gesperrt IN :value")
@NamedQuery(name = "MigrationDTOSchueler.modifiziertam", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertAm = :value")
@NamedQuery(name = "MigrationDTOSchueler.modifiziertam.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertAm IN :value")
@NamedQuery(name = "MigrationDTOSchueler.modifiziertvon", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertVon = :value")
@NamedQuery(name = "MigrationDTOSchueler.modifiziertvon.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ModifiziertVon IN :value")
@NamedQuery(name = "MigrationDTOSchueler.markiert", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Markiert = :value")
@NamedQuery(name = "MigrationDTOSchueler.markiert.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Markiert IN :value")
@NamedQuery(name = "MigrationDTOSchueler.fotovorhanden", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.FotoVorhanden = :value")
@NamedQuery(name = "MigrationDTOSchueler.fotovorhanden.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.FotoVorhanden IN :value")
@NamedQuery(name = "MigrationDTOSchueler.jva", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JVA = :value")
@NamedQuery(name = "MigrationDTOSchueler.jva.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JVA IN :value")
@NamedQuery(name = "MigrationDTOSchueler.refpaed", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.RefPaed = :value")
@NamedQuery(name = "MigrationDTOSchueler.refpaed.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.RefPaed IN :value")
@NamedQuery(name = "MigrationDTOSchueler.keineauskunft", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.KeineAuskunft = :value")
@NamedQuery(name = "MigrationDTOSchueler.keineauskunft.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.KeineAuskunft IN :value")
@NamedQuery(name = "MigrationDTOSchueler.beruf", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Beruf = :value")
@NamedQuery(name = "MigrationDTOSchueler.beruf.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Beruf IN :value")
@NamedQuery(name = "MigrationDTOSchueler.abschlussdatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AbschlussDatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.abschlussdatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AbschlussDatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.bemerkungen", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "MigrationDTOSchueler.bemerkungen.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "MigrationDTOSchueler.beginnbildungsgang", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BeginnBildungsgang = :value")
@NamedQuery(name = "MigrationDTOSchueler.beginnbildungsgang.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BeginnBildungsgang IN :value")
@NamedQuery(name = "MigrationDTOSchueler.orgformkrz", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrgFormKrz = :value")
@NamedQuery(name = "MigrationDTOSchueler.orgformkrz.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.OrgFormKrz IN :value")
@NamedQuery(name = "MigrationDTOSchueler.klassenart", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassenart = :value")
@NamedQuery(name = "MigrationDTOSchueler.klassenart.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klassenart IN :value")
@NamedQuery(name = "MigrationDTOSchueler.durchschnittsnote", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsNote = :value")
@NamedQuery(name = "MigrationDTOSchueler.durchschnittsnote.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsNote IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lssgl", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL = :value")
@NamedQuery(name = "MigrationDTOSchueler.lssgl.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulform", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulform = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsschulform.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSchulform IN :value")
@NamedQuery(name = "MigrationDTOSchueler.konfdruck", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.KonfDruck = :value")
@NamedQuery(name = "MigrationDTOSchueler.konfdruck.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.KonfDruck IN :value")
@NamedQuery(name = "MigrationDTOSchueler.dsn_text", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_Text = :value")
@NamedQuery(name = "MigrationDTOSchueler.dsn_text.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_Text IN :value")
@NamedQuery(name = "MigrationDTOSchueler.berufsabschluss", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsabschluss = :value")
@NamedQuery(name = "MigrationDTOSchueler.berufsabschluss.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsabschluss IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schwerpunkt_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerpunkt_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.schwerpunkt_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Schwerpunkt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lssgl_sim", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL_SIM = :value")
@NamedQuery(name = "MigrationDTOSchueler.lssgl_sim.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSSGL_SIM IN :value")
@NamedQuery(name = "MigrationDTOSchueler.berufsschulpflerf", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BerufsschulpflErf = :value")
@NamedQuery(name = "MigrationDTOSchueler.berufsschulpflerf.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BerufsschulpflErf IN :value")
@NamedQuery(name = "MigrationDTOSchueler.statusnsj", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StatusNSJ = :value")
@NamedQuery(name = "MigrationDTOSchueler.statusnsj.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StatusNSJ IN :value")
@NamedQuery(name = "MigrationDTOSchueler.fachklassensj_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.FachklasseNSJ_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.fachklassensj_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.FachklasseNSJ_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.buchkonto", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BuchKonto = :value")
@NamedQuery(name = "MigrationDTOSchueler.buchkonto.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BuchKonto IN :value")
@NamedQuery(name = "MigrationDTOSchueler.verkehrssprachefamilie", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerkehrsspracheFamilie = :value")
@NamedQuery(name = "MigrationDTOSchueler.verkehrssprachefamilie.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerkehrsspracheFamilie IN :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrzuzug", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrZuzug = :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrzuzug.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrZuzug IN :value")
@NamedQuery(name = "MigrationDTOSchueler.dauerkindergartenbesuch", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerKindergartenbesuch = :value")
@NamedQuery(name = "MigrationDTOSchueler.dauerkindergartenbesuch.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerKindergartenbesuch IN :value")
@NamedQuery(name = "MigrationDTOSchueler.verpflichtungsprachfoerderkurs", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs = :value")
@NamedQuery(name = "MigrationDTOSchueler.verpflichtungsprachfoerderkurs.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs IN :value")
@NamedQuery(name = "MigrationDTOSchueler.teilnahmesprachfoerderkurs", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs = :value")
@NamedQuery(name = "MigrationDTOSchueler.teilnahmesprachfoerderkurs.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulbuchgeldbefreit", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulbuchgeldBefreit = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulbuchgeldbefreit.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulbuchgeldBefreit IN :value")
@NamedQuery(name = "MigrationDTOSchueler.autist", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Autist = :value")
@NamedQuery(name = "MigrationDTOSchueler.autist.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Autist IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtslandschueler", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandSchueler = :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtslandschueler.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandSchueler IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtslandvater", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandVater = :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtslandvater.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandVater IN :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtslandmutter", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandMutter = :value")
@NamedQuery(name = "MigrationDTOSchueler.geburtslandmutter.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.GeburtslandMutter IN :value")
@NamedQuery(name = "MigrationDTOSchueler.uebergangsempfehlung_jg5", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Uebergangsempfehlung_JG5 = :value")
@NamedQuery(name = "MigrationDTOSchueler.uebergangsempfehlung_jg5.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Uebergangsempfehlung_JG5 IN :value")
@NamedQuery(name = "MigrationDTOSchueler.ersteschulform_si", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ErsteSchulform_SI = :value")
@NamedQuery(name = "MigrationDTOSchueler.ersteschulform_si.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ErsteSchulform_SI IN :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrwechsel_si", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SI = :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrwechsel_si.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SI IN :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrwechsel_sii", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SII = :value")
@NamedQuery(name = "MigrationDTOSchueler.jahrwechsel_sii.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.JahrWechsel_SII IN :value")
@NamedQuery(name = "MigrationDTOSchueler.migrationshintergrund", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Migrationshintergrund = :value")
@NamedQuery(name = "MigrationDTOSchueler.migrationshintergrund.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Migrationshintergrund IN :value")
@NamedQuery(name = "MigrationDTOSchueler.externeschulnr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ExterneSchulNr = :value")
@NamedQuery(name = "MigrationDTOSchueler.externeschulnr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ExterneSchulNr IN :value")
@NamedQuery(name = "MigrationDTOSchueler.kindergarten_id", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Kindergarten_ID = :value")
@NamedQuery(name = "MigrationDTOSchueler.kindergarten_id.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Kindergarten_ID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.letzterberufsabschluss", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterBerufsAbschluss = :value")
@NamedQuery(name = "MigrationDTOSchueler.letzterberufsabschluss.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterBerufsAbschluss IN :value")
@NamedQuery(name = "MigrationDTOSchueler.letzterallgabschluss", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterAllgAbschluss = :value")
@NamedQuery(name = "MigrationDTOSchueler.letzterallgabschluss.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LetzterAllgAbschluss IN :value")
@NamedQuery(name = "MigrationDTOSchueler.land", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Land = :value")
@NamedQuery(name = "MigrationDTOSchueler.land.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Land IN :value")
@NamedQuery(name = "MigrationDTOSchueler.duplikat", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Duplikat = :value")
@NamedQuery(name = "MigrationDTOSchueler.duplikat.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Duplikat IN :value")
@NamedQuery(name = "MigrationDTOSchueler.einschulungsartasd", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EinschulungsartASD = :value")
@NamedQuery(name = "MigrationDTOSchueler.einschulungsartasd.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EinschulungsartASD IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulnreigner", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchueler.bilingualerzweig", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BilingualerZweig = :value")
@NamedQuery(name = "MigrationDTOSchueler.bilingualerzweig.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BilingualerZweig IN :value")
@NamedQuery(name = "MigrationDTOSchueler.durchschnittsnotefhr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsnoteFHR = :value")
@NamedQuery(name = "MigrationDTOSchueler.durchschnittsnotefhr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DurchschnittsnoteFHR IN :value")
@NamedQuery(name = "MigrationDTOSchueler.dsn_fhr_text", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_FHR_Text = :value")
@NamedQuery(name = "MigrationDTOSchueler.dsn_fhr_text.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DSN_FHR_Text IN :value")
@NamedQuery(name = "MigrationDTOSchueler.eigenanteil", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Eigenanteil = :value")
@NamedQuery(name = "MigrationDTOSchueler.eigenanteil.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Eigenanteil IN :value")
@NamedQuery(name = "MigrationDTOSchueler.staatabk2", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk2 = :value")
@NamedQuery(name = "MigrationDTOSchueler.staatabk2.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.StaatAbk2 IN :value")
@NamedQuery(name = "MigrationDTOSchueler.bkazvo", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BKAZVO = :value")
@NamedQuery(name = "MigrationDTOSchueler.bkazvo.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.BKAZVO IN :value")
@NamedQuery(name = "MigrationDTOSchueler.hatberufsausbildung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HatBerufsausbildung = :value")
@NamedQuery(name = "MigrationDTOSchueler.hatberufsausbildung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.HatBerufsausbildung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.ausweisnummer", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ausweisnummer = :value")
@NamedQuery(name = "MigrationDTOSchueler.ausweisnummer.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Ausweisnummer IN :value")
@NamedQuery(name = "MigrationDTOSchueler.aosf", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AOSF = :value")
@NamedQuery(name = "MigrationDTOSchueler.aosf.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AOSF IN :value")
@NamedQuery(name = "MigrationDTOSchueler.epjahre", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EPJahre = :value")
@NamedQuery(name = "MigrationDTOSchueler.epjahre.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EPJahre IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lsbemerkung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSBemerkung = :value")
@NamedQuery(name = "MigrationDTOSchueler.lsbemerkung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.LSBemerkung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.wechselbestaetigt", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.WechselBestaetigt = :value")
@NamedQuery(name = "MigrationDTOSchueler.wechselbestaetigt.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.WechselBestaetigt IN :value")
@NamedQuery(name = "MigrationDTOSchueler.dauerbildungsgang", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerBildungsgang = :value")
@NamedQuery(name = "MigrationDTOSchueler.dauerbildungsgang.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.DauerBildungsgang IN :value")
@NamedQuery(name = "MigrationDTOSchueler.anmeldedatum", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AnmeldeDatum = :value")
@NamedQuery(name = "MigrationDTOSchueler.anmeldedatum.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AnmeldeDatum IN :value")
@NamedQuery(name = "MigrationDTOSchueler.meisterbafoeg", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.MeisterBafoeg = :value")
@NamedQuery(name = "MigrationDTOSchueler.meisterbafoeg.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.MeisterBafoeg IN :value")
@NamedQuery(name = "MigrationDTOSchueler.onlineanmeldung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.OnlineAnmeldung = :value")
@NamedQuery(name = "MigrationDTOSchueler.onlineanmeldung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.OnlineAnmeldung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.dokumentenverzeichnis", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Dokumentenverzeichnis = :value")
@NamedQuery(name = "MigrationDTOSchueler.dokumentenverzeichnis.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Dokumentenverzeichnis IN :value")
@NamedQuery(name = "MigrationDTOSchueler.berufsqualifikation", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsqualifikation = :value")
@NamedQuery(name = "MigrationDTOSchueler.berufsqualifikation.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Berufsqualifikation IN :value")
@NamedQuery(name = "MigrationDTOSchueler.zieldifferenteslernen", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZieldifferentesLernen = :value")
@NamedQuery(name = "MigrationDTOSchueler.zieldifferenteslernen.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZieldifferentesLernen IN :value")
@NamedQuery(name = "MigrationDTOSchueler.zusatznachname", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZusatzNachname = :value")
@NamedQuery(name = "MigrationDTOSchueler.zusatznachname.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ZusatzNachname IN :value")
@NamedQuery(name = "MigrationDTOSchueler.endeeingliederung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeEingliederung = :value")
@NamedQuery(name = "MigrationDTOSchueler.endeeingliederung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeEingliederung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.schulemail", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulEmail = :value")
@NamedQuery(name = "MigrationDTOSchueler.schulemail.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SchulEmail IN :value")
@NamedQuery(name = "MigrationDTOSchueler.endeanschlussfoerderung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeAnschlussfoerderung = :value")
@NamedQuery(name = "MigrationDTOSchueler.endeanschlussfoerderung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EndeAnschlussfoerderung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.masernimpfnachweis", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.MasernImpfnachweis = :value")
@NamedQuery(name = "MigrationDTOSchueler.masernimpfnachweis.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.MasernImpfnachweis IN :value")
@NamedQuery(name = "MigrationDTOSchueler.lernstandsbericht", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Lernstandsbericht = :value")
@NamedQuery(name = "MigrationDTOSchueler.lernstandsbericht.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Lernstandsbericht IN :value")
@NamedQuery(name = "MigrationDTOSchueler.sprachfoerderungvon", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungVon = :value")
@NamedQuery(name = "MigrationDTOSchueler.sprachfoerderungvon.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungVon IN :value")
@NamedQuery(name = "MigrationDTOSchueler.sprachfoerderungbis", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungBis = :value")
@NamedQuery(name = "MigrationDTOSchueler.sprachfoerderungbis.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.SprachfoerderungBis IN :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassungbemerkung", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EntlassungBemerkung = :value")
@NamedQuery(name = "MigrationDTOSchueler.entlassungbemerkung.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.EntlassungBemerkung IN :value")
@NamedQuery(name = "MigrationDTOSchueler.credentialid", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.CredentialID = :value")
@NamedQuery(name = "MigrationDTOSchueler.credentialid.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.CredentialID IN :value")
@NamedQuery(name = "MigrationDTOSchueler.aktschuljahr", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktSchuljahr = :value")
@NamedQuery(name = "MigrationDTOSchueler.aktschuljahr.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktSchuljahr IN :value")
@NamedQuery(name = "MigrationDTOSchueler.aktabschnitt", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktAbschnitt = :value")
@NamedQuery(name = "MigrationDTOSchueler.aktabschnitt.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.AktAbschnitt IN :value")
@NamedQuery(name = "MigrationDTOSchueler.klasse", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klasse = :value")
@NamedQuery(name = "MigrationDTOSchueler.klasse.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.Klasse IN :value")
@NamedQuery(name = "MigrationDTOSchueler.neuzugewandert", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.NeuZugewandert = :value")
@NamedQuery(name = "MigrationDTOSchueler.neuzugewandert.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.NeuZugewandert IN :value")
@NamedQuery(name = "MigrationDTOSchueler.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchueler.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOSchueler.all.migration", query = "SELECT e FROM MigrationDTOSchueler e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schuljahresabschnitts_ID", "GU_ID", "SrcID", "IDext", "Status", "Nachname", "Vorname", "AlleVornamen", "Geburtsname", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Ort_ID", "PLZ", "OrtAbk", "Ortsteil_ID", "Telefon", "Email", "Fax", "Klassen_ID", "JahrgangSchueler", "PruefOrdnung", "Geburtsdatum", "Geburtsort", "Volljaehrig", "Geschlecht", "StaatKrz", "StaatKrz2", "StaatAbk", "Aussiedler", "Religion_ID", "ReligionAbk", "Religionsabmeldung", "Religionsanmeldung", "Bafoeg", "Schwerbehinderung", "Sportbefreiung_ID", "Fahrschueler_ID", "Haltestelle_ID", "HaltestelleAbk", "Schulgliederung", "Jahrgang_ID", "ASDJahrgang", "Fachklasse_ID", "SchulpflichtErf", "Aufnahmedatum", "Einschulungsjahr", "Einschulungsart_ID", "LSSchulNr", "LSSchulformSIM", "LSJahrgang", "LSSchulEntlassDatum", "LSVersetzung", "LSFachklKennung", "LSFachklSIM", "LSEntlassgrund", "LSEntlassArt", "LSKlassenart", "LSRefPaed", "Entlassjahrgang", "Entlassjahrgang_ID", "Entlassdatum", "Entlassgrund", "Entlassart", "SchulwechselNr", "Schulwechseldatum", "Geloescht", "Gesperrt", "ModifiziertAm", "ModifiziertVon", "Markiert", "FotoVorhanden", "JVA", "RefPaed", "KeineAuskunft", "Beruf", "AbschlussDatum", "Bemerkungen", "BeginnBildungsgang", "OrgFormKrz", "Klassenart", "DurchschnittsNote", "LSSGL", "LSSchulform", "KonfDruck", "DSN_Text", "Berufsabschluss", "Schwerpunkt_ID", "LSSGL_SIM", "BerufsschulpflErf", "StatusNSJ", "FachklasseNSJ_ID", "BuchKonto", "VerkehrsspracheFamilie", "JahrZuzug", "DauerKindergartenbesuch", "VerpflichtungSprachfoerderkurs", "TeilnahmeSprachfoerderkurs", "SchulbuchgeldBefreit", "Autist", "GeburtslandSchueler", "GeburtslandVater", "GeburtslandMutter", "Uebergangsempfehlung_JG5", "ErsteSchulform_SI", "JahrWechsel_SI", "JahrWechsel_SII", "Migrationshintergrund", "ExterneSchulNr", "Kindergarten_ID", "LetzterBerufsAbschluss", "LetzterAllgAbschluss", "Land", "Duplikat", "EinschulungsartASD", "SchulnrEigner", "BilingualerZweig", "DurchschnittsnoteFHR", "DSN_FHR_Text", "Eigenanteil", "StaatAbk2", "BKAZVO", "HatBerufsausbildung", "Ausweisnummer", "AOSF", "EPJahre", "LSBemerkung", "WechselBestaetigt", "DauerBildungsgang", "AnmeldeDatum", "MeisterBafoeg", "OnlineAnmeldung", "Dokumentenverzeichnis", "Berufsqualifikation", "ZieldifferentesLernen", "ZusatzNachname", "EndeEingliederung", "SchulEmail", "EndeAnschlussfoerderung", "MasernImpfnachweis", "Lernstandsbericht", "SprachfoerderungVon", "SprachfoerderungBis", "EntlassungBemerkung", "CredentialID", "AktSchuljahr", "AktAbschnitt", "Klasse", "NeuZugewandert"})
public final class MigrationDTOSchueler {

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

	/** Text für Bemerkungen zum Schüler Memofeld */
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
