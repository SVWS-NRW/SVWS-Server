package de.nrw.schule.svws.db.dto.current.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.converter.current.GeschlechtConverter;
import de.nrw.schule.svws.db.converter.current.NationalitaetenConverter;
import de.nrw.schule.svws.db.converter.current.SchuelerStatusConverter;

import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.schule.Nationalitaeten;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.GeschlechtConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.GeschlechtConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.NationalitaetenConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.NationalitaetenConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.SchuelerStatusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.SchuelerStatusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schueler")
@NamedQuery(name="DTOSchueler.all", query="SELECT e FROM DTOSchueler e")
@NamedQuery(name="DTOSchueler.id", query="SELECT e FROM DTOSchueler e WHERE e.ID = :value")
@NamedQuery(name="DTOSchueler.id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchueler.schuljahresabschnitts_id", query="SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DTOSchueler.schuljahresabschnitts_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DTOSchueler.gu_id", query="SELECT e FROM DTOSchueler e WHERE e.GU_ID = :value")
@NamedQuery(name="DTOSchueler.gu_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.GU_ID IN :value")
@NamedQuery(name="DTOSchueler.srcid", query="SELECT e FROM DTOSchueler e WHERE e.SrcID = :value")
@NamedQuery(name="DTOSchueler.srcid.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SrcID IN :value")
@NamedQuery(name="DTOSchueler.idext", query="SELECT e FROM DTOSchueler e WHERE e.IDext = :value")
@NamedQuery(name="DTOSchueler.idext.multiple", query="SELECT e FROM DTOSchueler e WHERE e.IDext IN :value")
@NamedQuery(name="DTOSchueler.status", query="SELECT e FROM DTOSchueler e WHERE e.Status = :value")
@NamedQuery(name="DTOSchueler.status.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Status IN :value")
@NamedQuery(name="DTOSchueler.nachname", query="SELECT e FROM DTOSchueler e WHERE e.Nachname = :value")
@NamedQuery(name="DTOSchueler.nachname.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Nachname IN :value")
@NamedQuery(name="DTOSchueler.vorname", query="SELECT e FROM DTOSchueler e WHERE e.Vorname = :value")
@NamedQuery(name="DTOSchueler.vorname.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Vorname IN :value")
@NamedQuery(name="DTOSchueler.allevornamen", query="SELECT e FROM DTOSchueler e WHERE e.AlleVornamen = :value")
@NamedQuery(name="DTOSchueler.allevornamen.multiple", query="SELECT e FROM DTOSchueler e WHERE e.AlleVornamen IN :value")
@NamedQuery(name="DTOSchueler.geburtsname", query="SELECT e FROM DTOSchueler e WHERE e.Geburtsname = :value")
@NamedQuery(name="DTOSchueler.geburtsname.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Geburtsname IN :value")
@NamedQuery(name="DTOSchueler.strassenname", query="SELECT e FROM DTOSchueler e WHERE e.Strassenname = :value")
@NamedQuery(name="DTOSchueler.strassenname.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Strassenname IN :value")
@NamedQuery(name="DTOSchueler.hausnr", query="SELECT e FROM DTOSchueler e WHERE e.HausNr = :value")
@NamedQuery(name="DTOSchueler.hausnr.multiple", query="SELECT e FROM DTOSchueler e WHERE e.HausNr IN :value")
@NamedQuery(name="DTOSchueler.hausnrzusatz", query="SELECT e FROM DTOSchueler e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="DTOSchueler.hausnrzusatz.multiple", query="SELECT e FROM DTOSchueler e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="DTOSchueler.ort_id", query="SELECT e FROM DTOSchueler e WHERE e.Ort_ID = :value")
@NamedQuery(name="DTOSchueler.ort_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Ort_ID IN :value")
@NamedQuery(name="DTOSchueler.ortsteil_id", query="SELECT e FROM DTOSchueler e WHERE e.Ortsteil_ID = :value")
@NamedQuery(name="DTOSchueler.ortsteil_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Ortsteil_ID IN :value")
@NamedQuery(name="DTOSchueler.telefon", query="SELECT e FROM DTOSchueler e WHERE e.Telefon = :value")
@NamedQuery(name="DTOSchueler.telefon.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Telefon IN :value")
@NamedQuery(name="DTOSchueler.email", query="SELECT e FROM DTOSchueler e WHERE e.Email = :value")
@NamedQuery(name="DTOSchueler.email.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Email IN :value")
@NamedQuery(name="DTOSchueler.fax", query="SELECT e FROM DTOSchueler e WHERE e.Fax = :value")
@NamedQuery(name="DTOSchueler.fax.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Fax IN :value")
@NamedQuery(name="DTOSchueler.geburtsdatum", query="SELECT e FROM DTOSchueler e WHERE e.Geburtsdatum = :value")
@NamedQuery(name="DTOSchueler.geburtsdatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Geburtsdatum IN :value")
@NamedQuery(name="DTOSchueler.geburtsort", query="SELECT e FROM DTOSchueler e WHERE e.Geburtsort = :value")
@NamedQuery(name="DTOSchueler.geburtsort.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Geburtsort IN :value")
@NamedQuery(name="DTOSchueler.volljaehrig", query="SELECT e FROM DTOSchueler e WHERE e.Volljaehrig = :value")
@NamedQuery(name="DTOSchueler.volljaehrig.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Volljaehrig IN :value")
@NamedQuery(name="DTOSchueler.geschlecht", query="SELECT e FROM DTOSchueler e WHERE e.Geschlecht = :value")
@NamedQuery(name="DTOSchueler.geschlecht.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Geschlecht IN :value")
@NamedQuery(name="DTOSchueler.staatkrz", query="SELECT e FROM DTOSchueler e WHERE e.StaatKrz = :value")
@NamedQuery(name="DTOSchueler.staatkrz.multiple", query="SELECT e FROM DTOSchueler e WHERE e.StaatKrz IN :value")
@NamedQuery(name="DTOSchueler.staatkrz2", query="SELECT e FROM DTOSchueler e WHERE e.StaatKrz2 = :value")
@NamedQuery(name="DTOSchueler.staatkrz2.multiple", query="SELECT e FROM DTOSchueler e WHERE e.StaatKrz2 IN :value")
@NamedQuery(name="DTOSchueler.aussiedler", query="SELECT e FROM DTOSchueler e WHERE e.Aussiedler = :value")
@NamedQuery(name="DTOSchueler.aussiedler.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Aussiedler IN :value")
@NamedQuery(name="DTOSchueler.religion_id", query="SELECT e FROM DTOSchueler e WHERE e.Religion_ID = :value")
@NamedQuery(name="DTOSchueler.religion_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Religion_ID IN :value")
@NamedQuery(name="DTOSchueler.religionsabmeldung", query="SELECT e FROM DTOSchueler e WHERE e.Religionsabmeldung = :value")
@NamedQuery(name="DTOSchueler.religionsabmeldung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Religionsabmeldung IN :value")
@NamedQuery(name="DTOSchueler.religionsanmeldung", query="SELECT e FROM DTOSchueler e WHERE e.Religionsanmeldung = :value")
@NamedQuery(name="DTOSchueler.religionsanmeldung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Religionsanmeldung IN :value")
@NamedQuery(name="DTOSchueler.bafoeg", query="SELECT e FROM DTOSchueler e WHERE e.Bafoeg = :value")
@NamedQuery(name="DTOSchueler.bafoeg.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Bafoeg IN :value")
@NamedQuery(name="DTOSchueler.sportbefreiung_id", query="SELECT e FROM DTOSchueler e WHERE e.Sportbefreiung_ID = :value")
@NamedQuery(name="DTOSchueler.sportbefreiung_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Sportbefreiung_ID IN :value")
@NamedQuery(name="DTOSchueler.fahrschueler_id", query="SELECT e FROM DTOSchueler e WHERE e.Fahrschueler_ID = :value")
@NamedQuery(name="DTOSchueler.fahrschueler_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Fahrschueler_ID IN :value")
@NamedQuery(name="DTOSchueler.haltestelle_id", query="SELECT e FROM DTOSchueler e WHERE e.Haltestelle_ID = :value")
@NamedQuery(name="DTOSchueler.haltestelle_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Haltestelle_ID IN :value")
@NamedQuery(name="DTOSchueler.schulpflichterf", query="SELECT e FROM DTOSchueler e WHERE e.SchulpflichtErf = :value")
@NamedQuery(name="DTOSchueler.schulpflichterf.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SchulpflichtErf IN :value")
@NamedQuery(name="DTOSchueler.aufnahmedatum", query="SELECT e FROM DTOSchueler e WHERE e.Aufnahmedatum = :value")
@NamedQuery(name="DTOSchueler.aufnahmedatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Aufnahmedatum IN :value")
@NamedQuery(name="DTOSchueler.einschulungsjahr", query="SELECT e FROM DTOSchueler e WHERE e.Einschulungsjahr = :value")
@NamedQuery(name="DTOSchueler.einschulungsjahr.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Einschulungsjahr IN :value")
@NamedQuery(name="DTOSchueler.einschulungsart_id", query="SELECT e FROM DTOSchueler e WHERE e.Einschulungsart_ID = :value")
@NamedQuery(name="DTOSchueler.einschulungsart_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Einschulungsart_ID IN :value")
@NamedQuery(name="DTOSchueler.lsschulnr", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulNr = :value")
@NamedQuery(name="DTOSchueler.lsschulnr.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulNr IN :value")
@NamedQuery(name="DTOSchueler.lsschulformsim", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulformSIM = :value")
@NamedQuery(name="DTOSchueler.lsschulformsim.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulformSIM IN :value")
@NamedQuery(name="DTOSchueler.lsjahrgang", query="SELECT e FROM DTOSchueler e WHERE e.LSJahrgang = :value")
@NamedQuery(name="DTOSchueler.lsjahrgang.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSJahrgang IN :value")
@NamedQuery(name="DTOSchueler.lsschulentlassdatum", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulEntlassDatum = :value")
@NamedQuery(name="DTOSchueler.lsschulentlassdatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulEntlassDatum IN :value")
@NamedQuery(name="DTOSchueler.lsversetzung", query="SELECT e FROM DTOSchueler e WHERE e.LSVersetzung = :value")
@NamedQuery(name="DTOSchueler.lsversetzung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSVersetzung IN :value")
@NamedQuery(name="DTOSchueler.lsfachklkennung", query="SELECT e FROM DTOSchueler e WHERE e.LSFachklKennung = :value")
@NamedQuery(name="DTOSchueler.lsfachklkennung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSFachklKennung IN :value")
@NamedQuery(name="DTOSchueler.lsfachklsim", query="SELECT e FROM DTOSchueler e WHERE e.LSFachklSIM = :value")
@NamedQuery(name="DTOSchueler.lsfachklsim.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSFachklSIM IN :value")
@NamedQuery(name="DTOSchueler.lsentlassgrund", query="SELECT e FROM DTOSchueler e WHERE e.LSEntlassgrund = :value")
@NamedQuery(name="DTOSchueler.lsentlassgrund.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSEntlassgrund IN :value")
@NamedQuery(name="DTOSchueler.lsentlassart", query="SELECT e FROM DTOSchueler e WHERE e.LSEntlassArt = :value")
@NamedQuery(name="DTOSchueler.lsentlassart.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSEntlassArt IN :value")
@NamedQuery(name="DTOSchueler.lsklassenart", query="SELECT e FROM DTOSchueler e WHERE e.LSKlassenart = :value")
@NamedQuery(name="DTOSchueler.lsklassenart.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSKlassenart IN :value")
@NamedQuery(name="DTOSchueler.lsrefpaed", query="SELECT e FROM DTOSchueler e WHERE e.LSRefPaed = :value")
@NamedQuery(name="DTOSchueler.lsrefpaed.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSRefPaed IN :value")
@NamedQuery(name="DTOSchueler.entlassjahrgang", query="SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang = :value")
@NamedQuery(name="DTOSchueler.entlassjahrgang.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang IN :value")
@NamedQuery(name="DTOSchueler.entlassjahrgang_id", query="SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang_ID = :value")
@NamedQuery(name="DTOSchueler.entlassjahrgang_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Entlassjahrgang_ID IN :value")
@NamedQuery(name="DTOSchueler.entlassdatum", query="SELECT e FROM DTOSchueler e WHERE e.Entlassdatum = :value")
@NamedQuery(name="DTOSchueler.entlassdatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Entlassdatum IN :value")
@NamedQuery(name="DTOSchueler.entlassgrund", query="SELECT e FROM DTOSchueler e WHERE e.Entlassgrund = :value")
@NamedQuery(name="DTOSchueler.entlassgrund.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Entlassgrund IN :value")
@NamedQuery(name="DTOSchueler.entlassart", query="SELECT e FROM DTOSchueler e WHERE e.Entlassart = :value")
@NamedQuery(name="DTOSchueler.entlassart.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Entlassart IN :value")
@NamedQuery(name="DTOSchueler.schulwechselnr", query="SELECT e FROM DTOSchueler e WHERE e.SchulwechselNr = :value")
@NamedQuery(name="DTOSchueler.schulwechselnr.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SchulwechselNr IN :value")
@NamedQuery(name="DTOSchueler.schulwechseldatum", query="SELECT e FROM DTOSchueler e WHERE e.Schulwechseldatum = :value")
@NamedQuery(name="DTOSchueler.schulwechseldatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Schulwechseldatum IN :value")
@NamedQuery(name="DTOSchueler.geloescht", query="SELECT e FROM DTOSchueler e WHERE e.Geloescht = :value")
@NamedQuery(name="DTOSchueler.geloescht.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Geloescht IN :value")
@NamedQuery(name="DTOSchueler.gesperrt", query="SELECT e FROM DTOSchueler e WHERE e.Gesperrt = :value")
@NamedQuery(name="DTOSchueler.gesperrt.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Gesperrt IN :value")
@NamedQuery(name="DTOSchueler.modifiziertam", query="SELECT e FROM DTOSchueler e WHERE e.ModifiziertAm = :value")
@NamedQuery(name="DTOSchueler.modifiziertam.multiple", query="SELECT e FROM DTOSchueler e WHERE e.ModifiziertAm IN :value")
@NamedQuery(name="DTOSchueler.modifiziertvon", query="SELECT e FROM DTOSchueler e WHERE e.ModifiziertVon = :value")
@NamedQuery(name="DTOSchueler.modifiziertvon.multiple", query="SELECT e FROM DTOSchueler e WHERE e.ModifiziertVon IN :value")
@NamedQuery(name="DTOSchueler.markiert", query="SELECT e FROM DTOSchueler e WHERE e.Markiert = :value")
@NamedQuery(name="DTOSchueler.markiert.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Markiert IN :value")
@NamedQuery(name="DTOSchueler.fotovorhanden", query="SELECT e FROM DTOSchueler e WHERE e.FotoVorhanden = :value")
@NamedQuery(name="DTOSchueler.fotovorhanden.multiple", query="SELECT e FROM DTOSchueler e WHERE e.FotoVorhanden IN :value")
@NamedQuery(name="DTOSchueler.jva", query="SELECT e FROM DTOSchueler e WHERE e.JVA = :value")
@NamedQuery(name="DTOSchueler.jva.multiple", query="SELECT e FROM DTOSchueler e WHERE e.JVA IN :value")
@NamedQuery(name="DTOSchueler.keineauskunft", query="SELECT e FROM DTOSchueler e WHERE e.KeineAuskunft = :value")
@NamedQuery(name="DTOSchueler.keineauskunft.multiple", query="SELECT e FROM DTOSchueler e WHERE e.KeineAuskunft IN :value")
@NamedQuery(name="DTOSchueler.beruf", query="SELECT e FROM DTOSchueler e WHERE e.Beruf = :value")
@NamedQuery(name="DTOSchueler.beruf.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Beruf IN :value")
@NamedQuery(name="DTOSchueler.abschlussdatum", query="SELECT e FROM DTOSchueler e WHERE e.AbschlussDatum = :value")
@NamedQuery(name="DTOSchueler.abschlussdatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.AbschlussDatum IN :value")
@NamedQuery(name="DTOSchueler.bemerkungen", query="SELECT e FROM DTOSchueler e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DTOSchueler.bemerkungen.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DTOSchueler.beginnbildungsgang", query="SELECT e FROM DTOSchueler e WHERE e.BeginnBildungsgang = :value")
@NamedQuery(name="DTOSchueler.beginnbildungsgang.multiple", query="SELECT e FROM DTOSchueler e WHERE e.BeginnBildungsgang IN :value")
@NamedQuery(name="DTOSchueler.durchschnittsnote", query="SELECT e FROM DTOSchueler e WHERE e.DurchschnittsNote = :value")
@NamedQuery(name="DTOSchueler.durchschnittsnote.multiple", query="SELECT e FROM DTOSchueler e WHERE e.DurchschnittsNote IN :value")
@NamedQuery(name="DTOSchueler.lssgl", query="SELECT e FROM DTOSchueler e WHERE e.LSSGL = :value")
@NamedQuery(name="DTOSchueler.lssgl.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSSGL IN :value")
@NamedQuery(name="DTOSchueler.lsschulform", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulform = :value")
@NamedQuery(name="DTOSchueler.lsschulform.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSSchulform IN :value")
@NamedQuery(name="DTOSchueler.konfdruck", query="SELECT e FROM DTOSchueler e WHERE e.KonfDruck = :value")
@NamedQuery(name="DTOSchueler.konfdruck.multiple", query="SELECT e FROM DTOSchueler e WHERE e.KonfDruck IN :value")
@NamedQuery(name="DTOSchueler.dsn_text", query="SELECT e FROM DTOSchueler e WHERE e.DSN_Text = :value")
@NamedQuery(name="DTOSchueler.dsn_text.multiple", query="SELECT e FROM DTOSchueler e WHERE e.DSN_Text IN :value")
@NamedQuery(name="DTOSchueler.berufsabschluss", query="SELECT e FROM DTOSchueler e WHERE e.Berufsabschluss = :value")
@NamedQuery(name="DTOSchueler.berufsabschluss.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Berufsabschluss IN :value")
@NamedQuery(name="DTOSchueler.lssgl_sim", query="SELECT e FROM DTOSchueler e WHERE e.LSSGL_SIM = :value")
@NamedQuery(name="DTOSchueler.lssgl_sim.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSSGL_SIM IN :value")
@NamedQuery(name="DTOSchueler.berufsschulpflerf", query="SELECT e FROM DTOSchueler e WHERE e.BerufsschulpflErf = :value")
@NamedQuery(name="DTOSchueler.berufsschulpflerf.multiple", query="SELECT e FROM DTOSchueler e WHERE e.BerufsschulpflErf IN :value")
@NamedQuery(name="DTOSchueler.statusnsj", query="SELECT e FROM DTOSchueler e WHERE e.StatusNSJ = :value")
@NamedQuery(name="DTOSchueler.statusnsj.multiple", query="SELECT e FROM DTOSchueler e WHERE e.StatusNSJ IN :value")
@NamedQuery(name="DTOSchueler.fachklassensj_id", query="SELECT e FROM DTOSchueler e WHERE e.FachklasseNSJ_ID = :value")
@NamedQuery(name="DTOSchueler.fachklassensj_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.FachklasseNSJ_ID IN :value")
@NamedQuery(name="DTOSchueler.buchkonto", query="SELECT e FROM DTOSchueler e WHERE e.BuchKonto = :value")
@NamedQuery(name="DTOSchueler.buchkonto.multiple", query="SELECT e FROM DTOSchueler e WHERE e.BuchKonto IN :value")
@NamedQuery(name="DTOSchueler.verkehrssprachefamilie", query="SELECT e FROM DTOSchueler e WHERE e.VerkehrsspracheFamilie = :value")
@NamedQuery(name="DTOSchueler.verkehrssprachefamilie.multiple", query="SELECT e FROM DTOSchueler e WHERE e.VerkehrsspracheFamilie IN :value")
@NamedQuery(name="DTOSchueler.jahrzuzug", query="SELECT e FROM DTOSchueler e WHERE e.JahrZuzug = :value")
@NamedQuery(name="DTOSchueler.jahrzuzug.multiple", query="SELECT e FROM DTOSchueler e WHERE e.JahrZuzug IN :value")
@NamedQuery(name="DTOSchueler.dauerkindergartenbesuch", query="SELECT e FROM DTOSchueler e WHERE e.DauerKindergartenbesuch = :value")
@NamedQuery(name="DTOSchueler.dauerkindergartenbesuch.multiple", query="SELECT e FROM DTOSchueler e WHERE e.DauerKindergartenbesuch IN :value")
@NamedQuery(name="DTOSchueler.verpflichtungsprachfoerderkurs", query="SELECT e FROM DTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs = :value")
@NamedQuery(name="DTOSchueler.verpflichtungsprachfoerderkurs.multiple", query="SELECT e FROM DTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs IN :value")
@NamedQuery(name="DTOSchueler.teilnahmesprachfoerderkurs", query="SELECT e FROM DTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs = :value")
@NamedQuery(name="DTOSchueler.teilnahmesprachfoerderkurs.multiple", query="SELECT e FROM DTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs IN :value")
@NamedQuery(name="DTOSchueler.schulbuchgeldbefreit", query="SELECT e FROM DTOSchueler e WHERE e.SchulbuchgeldBefreit = :value")
@NamedQuery(name="DTOSchueler.schulbuchgeldbefreit.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SchulbuchgeldBefreit IN :value")
@NamedQuery(name="DTOSchueler.geburtslandschueler", query="SELECT e FROM DTOSchueler e WHERE e.GeburtslandSchueler = :value")
@NamedQuery(name="DTOSchueler.geburtslandschueler.multiple", query="SELECT e FROM DTOSchueler e WHERE e.GeburtslandSchueler IN :value")
@NamedQuery(name="DTOSchueler.geburtslandvater", query="SELECT e FROM DTOSchueler e WHERE e.GeburtslandVater = :value")
@NamedQuery(name="DTOSchueler.geburtslandvater.multiple", query="SELECT e FROM DTOSchueler e WHERE e.GeburtslandVater IN :value")
@NamedQuery(name="DTOSchueler.geburtslandmutter", query="SELECT e FROM DTOSchueler e WHERE e.GeburtslandMutter = :value")
@NamedQuery(name="DTOSchueler.geburtslandmutter.multiple", query="SELECT e FROM DTOSchueler e WHERE e.GeburtslandMutter IN :value")
@NamedQuery(name="DTOSchueler.uebergangsempfehlung_jg5", query="SELECT e FROM DTOSchueler e WHERE e.Uebergangsempfehlung_JG5 = :value")
@NamedQuery(name="DTOSchueler.uebergangsempfehlung_jg5.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Uebergangsempfehlung_JG5 IN :value")
@NamedQuery(name="DTOSchueler.ersteschulform_si", query="SELECT e FROM DTOSchueler e WHERE e.ErsteSchulform_SI = :value")
@NamedQuery(name="DTOSchueler.ersteschulform_si.multiple", query="SELECT e FROM DTOSchueler e WHERE e.ErsteSchulform_SI IN :value")
@NamedQuery(name="DTOSchueler.jahrwechsel_si", query="SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SI = :value")
@NamedQuery(name="DTOSchueler.jahrwechsel_si.multiple", query="SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SI IN :value")
@NamedQuery(name="DTOSchueler.jahrwechsel_sii", query="SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SII = :value")
@NamedQuery(name="DTOSchueler.jahrwechsel_sii.multiple", query="SELECT e FROM DTOSchueler e WHERE e.JahrWechsel_SII IN :value")
@NamedQuery(name="DTOSchueler.migrationshintergrund", query="SELECT e FROM DTOSchueler e WHERE e.Migrationshintergrund = :value")
@NamedQuery(name="DTOSchueler.migrationshintergrund.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Migrationshintergrund IN :value")
@NamedQuery(name="DTOSchueler.externeschulnr", query="SELECT e FROM DTOSchueler e WHERE e.ExterneSchulNr = :value")
@NamedQuery(name="DTOSchueler.externeschulnr.multiple", query="SELECT e FROM DTOSchueler e WHERE e.ExterneSchulNr IN :value")
@NamedQuery(name="DTOSchueler.kindergarten_id", query="SELECT e FROM DTOSchueler e WHERE e.Kindergarten_ID = :value")
@NamedQuery(name="DTOSchueler.kindergarten_id.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Kindergarten_ID IN :value")
@NamedQuery(name="DTOSchueler.letzterberufsabschluss", query="SELECT e FROM DTOSchueler e WHERE e.LetzterBerufsAbschluss = :value")
@NamedQuery(name="DTOSchueler.letzterberufsabschluss.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LetzterBerufsAbschluss IN :value")
@NamedQuery(name="DTOSchueler.letzterallgabschluss", query="SELECT e FROM DTOSchueler e WHERE e.LetzterAllgAbschluss = :value")
@NamedQuery(name="DTOSchueler.letzterallgabschluss.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LetzterAllgAbschluss IN :value")
@NamedQuery(name="DTOSchueler.land", query="SELECT e FROM DTOSchueler e WHERE e.Land = :value")
@NamedQuery(name="DTOSchueler.land.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Land IN :value")
@NamedQuery(name="DTOSchueler.duplikat", query="SELECT e FROM DTOSchueler e WHERE e.Duplikat = :value")
@NamedQuery(name="DTOSchueler.duplikat.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Duplikat IN :value")
@NamedQuery(name="DTOSchueler.einschulungsartasd", query="SELECT e FROM DTOSchueler e WHERE e.EinschulungsartASD = :value")
@NamedQuery(name="DTOSchueler.einschulungsartasd.multiple", query="SELECT e FROM DTOSchueler e WHERE e.EinschulungsartASD IN :value")
@NamedQuery(name="DTOSchueler.durchschnittsnotefhr", query="SELECT e FROM DTOSchueler e WHERE e.DurchschnittsnoteFHR = :value")
@NamedQuery(name="DTOSchueler.durchschnittsnotefhr.multiple", query="SELECT e FROM DTOSchueler e WHERE e.DurchschnittsnoteFHR IN :value")
@NamedQuery(name="DTOSchueler.dsn_fhr_text", query="SELECT e FROM DTOSchueler e WHERE e.DSN_FHR_Text = :value")
@NamedQuery(name="DTOSchueler.dsn_fhr_text.multiple", query="SELECT e FROM DTOSchueler e WHERE e.DSN_FHR_Text IN :value")
@NamedQuery(name="DTOSchueler.eigenanteil", query="SELECT e FROM DTOSchueler e WHERE e.Eigenanteil = :value")
@NamedQuery(name="DTOSchueler.eigenanteil.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Eigenanteil IN :value")
@NamedQuery(name="DTOSchueler.bkazvo", query="SELECT e FROM DTOSchueler e WHERE e.BKAZVO = :value")
@NamedQuery(name="DTOSchueler.bkazvo.multiple", query="SELECT e FROM DTOSchueler e WHERE e.BKAZVO IN :value")
@NamedQuery(name="DTOSchueler.hatberufsausbildung", query="SELECT e FROM DTOSchueler e WHERE e.HatBerufsausbildung = :value")
@NamedQuery(name="DTOSchueler.hatberufsausbildung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.HatBerufsausbildung IN :value")
@NamedQuery(name="DTOSchueler.ausweisnummer", query="SELECT e FROM DTOSchueler e WHERE e.Ausweisnummer = :value")
@NamedQuery(name="DTOSchueler.ausweisnummer.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Ausweisnummer IN :value")
@NamedQuery(name="DTOSchueler.epjahre", query="SELECT e FROM DTOSchueler e WHERE e.EPJahre = :value")
@NamedQuery(name="DTOSchueler.epjahre.multiple", query="SELECT e FROM DTOSchueler e WHERE e.EPJahre IN :value")
@NamedQuery(name="DTOSchueler.lsbemerkung", query="SELECT e FROM DTOSchueler e WHERE e.LSBemerkung = :value")
@NamedQuery(name="DTOSchueler.lsbemerkung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.LSBemerkung IN :value")
@NamedQuery(name="DTOSchueler.wechselbestaetigt", query="SELECT e FROM DTOSchueler e WHERE e.WechselBestaetigt = :value")
@NamedQuery(name="DTOSchueler.wechselbestaetigt.multiple", query="SELECT e FROM DTOSchueler e WHERE e.WechselBestaetigt IN :value")
@NamedQuery(name="DTOSchueler.dauerbildungsgang", query="SELECT e FROM DTOSchueler e WHERE e.DauerBildungsgang = :value")
@NamedQuery(name="DTOSchueler.dauerbildungsgang.multiple", query="SELECT e FROM DTOSchueler e WHERE e.DauerBildungsgang IN :value")
@NamedQuery(name="DTOSchueler.anmeldedatum", query="SELECT e FROM DTOSchueler e WHERE e.AnmeldeDatum = :value")
@NamedQuery(name="DTOSchueler.anmeldedatum.multiple", query="SELECT e FROM DTOSchueler e WHERE e.AnmeldeDatum IN :value")
@NamedQuery(name="DTOSchueler.meisterbafoeg", query="SELECT e FROM DTOSchueler e WHERE e.MeisterBafoeg = :value")
@NamedQuery(name="DTOSchueler.meisterbafoeg.multiple", query="SELECT e FROM DTOSchueler e WHERE e.MeisterBafoeg IN :value")
@NamedQuery(name="DTOSchueler.onlineanmeldung", query="SELECT e FROM DTOSchueler e WHERE e.OnlineAnmeldung = :value")
@NamedQuery(name="DTOSchueler.onlineanmeldung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.OnlineAnmeldung IN :value")
@NamedQuery(name="DTOSchueler.dokumentenverzeichnis", query="SELECT e FROM DTOSchueler e WHERE e.Dokumentenverzeichnis = :value")
@NamedQuery(name="DTOSchueler.dokumentenverzeichnis.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Dokumentenverzeichnis IN :value")
@NamedQuery(name="DTOSchueler.berufsqualifikation", query="SELECT e FROM DTOSchueler e WHERE e.Berufsqualifikation = :value")
@NamedQuery(name="DTOSchueler.berufsqualifikation.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Berufsqualifikation IN :value")
@NamedQuery(name="DTOSchueler.zusatznachname", query="SELECT e FROM DTOSchueler e WHERE e.ZusatzNachname = :value")
@NamedQuery(name="DTOSchueler.zusatznachname.multiple", query="SELECT e FROM DTOSchueler e WHERE e.ZusatzNachname IN :value")
@NamedQuery(name="DTOSchueler.endeeingliederung", query="SELECT e FROM DTOSchueler e WHERE e.EndeEingliederung = :value")
@NamedQuery(name="DTOSchueler.endeeingliederung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.EndeEingliederung IN :value")
@NamedQuery(name="DTOSchueler.schulemail", query="SELECT e FROM DTOSchueler e WHERE e.SchulEmail = :value")
@NamedQuery(name="DTOSchueler.schulemail.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SchulEmail IN :value")
@NamedQuery(name="DTOSchueler.endeanschlussfoerderung", query="SELECT e FROM DTOSchueler e WHERE e.EndeAnschlussfoerderung = :value")
@NamedQuery(name="DTOSchueler.endeanschlussfoerderung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.EndeAnschlussfoerderung IN :value")
@NamedQuery(name="DTOSchueler.masernimpfnachweis", query="SELECT e FROM DTOSchueler e WHERE e.MasernImpfnachweis = :value")
@NamedQuery(name="DTOSchueler.masernimpfnachweis.multiple", query="SELECT e FROM DTOSchueler e WHERE e.MasernImpfnachweis IN :value")
@NamedQuery(name="DTOSchueler.lernstandsbericht", query="SELECT e FROM DTOSchueler e WHERE e.Lernstandsbericht = :value")
@NamedQuery(name="DTOSchueler.lernstandsbericht.multiple", query="SELECT e FROM DTOSchueler e WHERE e.Lernstandsbericht IN :value")
@NamedQuery(name="DTOSchueler.sprachfoerderungvon", query="SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungVon = :value")
@NamedQuery(name="DTOSchueler.sprachfoerderungvon.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungVon IN :value")
@NamedQuery(name="DTOSchueler.sprachfoerderungbis", query="SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungBis = :value")
@NamedQuery(name="DTOSchueler.sprachfoerderungbis.multiple", query="SELECT e FROM DTOSchueler e WHERE e.SprachfoerderungBis IN :value")
@NamedQuery(name="DTOSchueler.entlassungbemerkung", query="SELECT e FROM DTOSchueler e WHERE e.EntlassungBemerkung = :value")
@NamedQuery(name="DTOSchueler.entlassungbemerkung.multiple", query="SELECT e FROM DTOSchueler e WHERE e.EntlassungBemerkung IN :value")
@NamedQuery(name="DTOSchueler.credentialid", query="SELECT e FROM DTOSchueler e WHERE e.CredentialID = :value")
@NamedQuery(name="DTOSchueler.credentialid.multiple", query="SELECT e FROM DTOSchueler e WHERE e.CredentialID IN :value")
@NamedQuery(name="DTOSchueler.neuzugewandert", query="SELECT e FROM DTOSchueler e WHERE e.NeuZugewandert = :value")
@NamedQuery(name="DTOSchueler.neuzugewandert.multiple", query="SELECT e FROM DTOSchueler e WHERE e.NeuZugewandert IN :value")
@NamedQuery(name="DTOSchueler.primaryKeyQuery", query="SELECT e FROM DTOSchueler e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchueler.all.migration", query="SELECT e FROM DTOSchueler e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schuljahresabschnitts_ID","GU_ID","SrcID","IDext","Status","Nachname","Vorname","AlleVornamen","Geburtsname","Strassenname","HausNr","HausNrZusatz","Ort_ID","Ortsteil_ID","Telefon","Email","Fax","Geburtsdatum","Geburtsort","Volljaehrig","Geschlecht","StaatKrz","StaatKrz2","Aussiedler","Religion_ID","Religionsabmeldung","Religionsanmeldung","Bafoeg","Sportbefreiung_ID","Fahrschueler_ID","Haltestelle_ID","SchulpflichtErf","Aufnahmedatum","Einschulungsjahr","Einschulungsart_ID","LSSchulNr","LSSchulformSIM","LSJahrgang","LSSchulEntlassDatum","LSVersetzung","LSFachklKennung","LSFachklSIM","LSEntlassgrund","LSEntlassArt","LSKlassenart","LSRefPaed","Entlassjahrgang","Entlassjahrgang_ID","Entlassdatum","Entlassgrund","Entlassart","SchulwechselNr","Schulwechseldatum","Geloescht","Gesperrt","ModifiziertAm","ModifiziertVon","Markiert","FotoVorhanden","JVA","KeineAuskunft","Beruf","AbschlussDatum","Bemerkungen","BeginnBildungsgang","DurchschnittsNote","LSSGL","LSSchulform","KonfDruck","DSN_Text","Berufsabschluss","LSSGL_SIM","BerufsschulpflErf","StatusNSJ","FachklasseNSJ_ID","BuchKonto","VerkehrsspracheFamilie","JahrZuzug","DauerKindergartenbesuch","VerpflichtungSprachfoerderkurs","TeilnahmeSprachfoerderkurs","SchulbuchgeldBefreit","GeburtslandSchueler","GeburtslandVater","GeburtslandMutter","Uebergangsempfehlung_JG5","ErsteSchulform_SI","JahrWechsel_SI","JahrWechsel_SII","Migrationshintergrund","ExterneSchulNr","Kindergarten_ID","LetzterBerufsAbschluss","LetzterAllgAbschluss","Land","Duplikat","EinschulungsartASD","DurchschnittsnoteFHR","DSN_FHR_Text","Eigenanteil","BKAZVO","HatBerufsausbildung","Ausweisnummer","EPJahre","LSBemerkung","WechselBestaetigt","DauerBildungsgang","AnmeldeDatum","MeisterBafoeg","OnlineAnmeldung","Dokumentenverzeichnis","Berufsqualifikation","ZusatzNachname","EndeEingliederung","SchulEmail","EndeAnschlussfoerderung","MasernImpfnachweis","Lernstandsbericht","SprachfoerderungVon","SprachfoerderungBis","EntlassungBemerkung","CredentialID","NeuZugewandert"})
public class DTOSchueler {

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
	@Convert(converter=SchuelerStatusConverter.class)
	@JsonSerialize(using=SchuelerStatusConverterSerializer.class)
	@JsonDeserialize(using=SchuelerStatusConverterDeserializer.class)
	public SchuelerStatus Status;

	/** Name des Schülers PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name")
	@JsonProperty
	public String Nachname;

	/** Vorname des Schülers PAuswG vom 21.6.2019 §5 Abs. 2 */
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
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Geburtsdatum;

	/** Geburtsort des Schülers */
	@Column(name = "Geburtsort")
	@JsonProperty
	public String Geburtsort;

	/** Gibt an ob der Schüler volljährig ist */
	@Column(name = "Volljaehrig")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Volljaehrig;

	/** Geschlecht des Schülers */
	@Column(name = "Geschlecht")
	@JsonProperty
	@Convert(converter=GeschlechtConverter.class)
	@JsonSerialize(using=GeschlechtConverterSerializer.class)
	@JsonDeserialize(using=GeschlechtConverterDeserializer.class)
	public Geschlecht Geschlecht;

	/** Kürzel der 1. Staatsangehörigkeit */
	@Column(name = "StaatKrz")
	@JsonProperty
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
	public Nationalitaeten StaatKrz;

	/** Kürzel der 2. Staatsangehörigkeit */
	@Column(name = "StaatKrz2")
	@JsonProperty
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
	public Nationalitaeten StaatKrz2;

	/** DEPRECATED: Gibt an ob der Schüler ausgesiedelt ist (wird nicht mehr erfasst) */
	@Column(name = "Aussiedler")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Aussiedler;

	/** ID des Religionskatalogeintrags */
	@Column(name = "Religion_ID")
	@JsonProperty
	public Long Religion_ID;

	/** Abmeldetdateum vom Religionsunterricht */
	@Column(name = "Religionsabmeldung")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Religionsabmeldung;

	/** Anmeldedatum zum Religionsunterricht wenn vorher abgemeldet */
	@Column(name = "Religionsanmeldung")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Religionsanmeldung;

	/** Gibt an ob ein Schüler BAFög bezieht */
	@Column(name = "Bafoeg")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchulpflichtErf;

	/** Aufnahmedatum */
	@Column(name = "Aufnahmedatum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
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
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
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
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Schulwechseldatum;

	/** Löschmarkierung Schülerdatensatz */
	@Column(name = "Geloescht")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Geloescht;

	/** Datensatz gesperrt Ja Nein */
	@Column(name = "Gesperrt")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Gesperrt;

	/** zuletzt geändert Datum */
	@Column(name = "ModifiziertAm")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String ModifiziertAm;

	/** zuletzt geändert Benutzer */
	@Column(name = "ModifiziertVon")
	@JsonProperty
	public String ModifiziertVon;

	/** Datensatz ist markiert */
	@Column(name = "Markiert")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Markiert;

	/** DEPRECATED: nicht mehr genutzt Zustimmung Foto */
	@Column(name = "FotoVorhanden")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FotoVorhanden;

	/** Ist Schüler einer Justizvollzugsanstalt */
	@Column(name = "JVA")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean JVA;

	/** Keine Auskunft an Dritte Ja Nein */
	@Column(name = "KeineAuskunft")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean VerpflichtungSprachfoerderkurs;

	/** Teilnahme an einen Sprachförderkurs (Ja/Nein) */
	@Column(name = "TeilnahmeSprachfoerderkurs")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean TeilnahmeSprachfoerderkurs;

	/** Vom Schulbuchgeld befreit (Ja/Nein) */
	@Column(name = "SchulbuchgeldBefreit")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SchulbuchgeldBefreit;

	/** Migrationshintergrund Geburtsland des Schülers */
	@Column(name = "GeburtslandSchueler")
	@JsonProperty
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
	public Nationalitaeten GeburtslandSchueler;

	/** Migrationshintergrund Geburtsland des Vaters */
	@Column(name = "GeburtslandVater")
	@JsonProperty
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
	public Nationalitaeten GeburtslandVater;

	/** Migrationshintergrund Geburtsland der Mutter */
	@Column(name = "GeburtslandMutter")
	@JsonProperty
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean WechselBestaetigt;

	/** Dauer des Bildungsgangs am BK */
	@Column(name = "DauerBildungsgang")
	@JsonProperty
	public Integer DauerBildungsgang;

	/** Anmeldedatum des Schülers */
	@Column(name = "AnmeldeDatum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String AnmeldeDatum;

	/** Gibt an ob ein Schüler MeisterBafög bezieht BK */
	@Column(name = "MeisterBafoeg")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MeisterBafoeg;

	/** Schüler hat sich Online angemeldet (Ja/Nein) */
	@Column(name = "OnlineAnmeldung")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean OnlineAnmeldung;

	/** Pfad zum Dokumentenverzeichnis */
	@Column(name = "Dokumentenverzeichnis")
	@JsonProperty
	public String Dokumentenverzeichnis;

	/** Karteireiter Schulbesuch Berufsausbildung vorhanden (Ja/Nein) */
	@Column(name = "Berufsqualifikation")
	@JsonProperty
	public String Berufsqualifikation;

	/** Gibt ggf. den Zusatz zum Nachnamen an. */
	@Column(name = "ZusatzNachname")
	@JsonProperty
	public String ZusatzNachname;

	/** Ende der Eingliederung bei zugezogenen Schülern (Flüchtlingen) */
	@Column(name = "EndeEingliederung")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String EndeEingliederung;

	/** schulische E-Mail-Adresse des Schülers */
	@Column(name = "SchulEmail")
	@JsonProperty
	public String SchulEmail;

	/** Ende der Anschlussförderung bei zugezogenen Schülern (Flüchtlingen) */
	@Column(name = "EndeAnschlussfoerderung")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String EndeAnschlussfoerderung;

	/** Gibt an, ob ein Nachweis über die Masern-Impfung erbracht wurde */
	@Column(name = "MasernImpfnachweis")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean MasernImpfnachweis;

	/** Gibt an ob ein Schüler Sprachförderung in Deutsch (DAZ) erhält und somit Lernstandsberichte statt Zeugnisse */
	@Column(name = "Lernstandsbericht")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Lernstandsbericht;

	/** Datum des Beginns der Sprachförderung */
	@Column(name = "SprachfoerderungVon")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String SprachfoerderungVon;

	/** Datum des Endes der Sprachförderung */
	@Column(name = "SprachfoerderungBis")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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
	public DTOSchueler(final Long ID, final String GU_ID, final Boolean NeuZugewandert) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchueler other = (DTOSchueler) obj;
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
		return "DTOSchueler(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", GU_ID=" + this.GU_ID + ", SrcID=" + this.SrcID + ", IDext=" + this.IDext + ", Status=" + this.Status + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", AlleVornamen=" + this.AlleVornamen + ", Geburtsname=" + this.Geburtsname + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", Ortsteil_ID=" + this.Ortsteil_ID + ", Telefon=" + this.Telefon + ", Email=" + this.Email + ", Fax=" + this.Fax + ", Geburtsdatum=" + this.Geburtsdatum + ", Geburtsort=" + this.Geburtsort + ", Volljaehrig=" + this.Volljaehrig + ", Geschlecht=" + this.Geschlecht + ", StaatKrz=" + this.StaatKrz + ", StaatKrz2=" + this.StaatKrz2 + ", Aussiedler=" + this.Aussiedler + ", Religion_ID=" + this.Religion_ID + ", Religionsabmeldung=" + this.Religionsabmeldung + ", Religionsanmeldung=" + this.Religionsanmeldung + ", Bafoeg=" + this.Bafoeg + ", Sportbefreiung_ID=" + this.Sportbefreiung_ID + ", Fahrschueler_ID=" + this.Fahrschueler_ID + ", Haltestelle_ID=" + this.Haltestelle_ID + ", SchulpflichtErf=" + this.SchulpflichtErf + ", Aufnahmedatum=" + this.Aufnahmedatum + ", Einschulungsjahr=" + this.Einschulungsjahr + ", Einschulungsart_ID=" + this.Einschulungsart_ID + ", LSSchulNr=" + this.LSSchulNr + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSJahrgang=" + this.LSJahrgang + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", LSEntlassgrund=" + this.LSEntlassgrund + ", LSEntlassArt=" + this.LSEntlassArt + ", LSKlassenart=" + this.LSKlassenart + ", LSRefPaed=" + this.LSRefPaed + ", Entlassjahrgang=" + this.Entlassjahrgang + ", Entlassjahrgang_ID=" + this.Entlassjahrgang_ID + ", Entlassdatum=" + this.Entlassdatum + ", Entlassgrund=" + this.Entlassgrund + ", Entlassart=" + this.Entlassart + ", SchulwechselNr=" + this.SchulwechselNr + ", Schulwechseldatum=" + this.Schulwechseldatum + ", Geloescht=" + this.Geloescht + ", Gesperrt=" + this.Gesperrt + ", ModifiziertAm=" + this.ModifiziertAm + ", ModifiziertVon=" + this.ModifiziertVon + ", Markiert=" + this.Markiert + ", FotoVorhanden=" + this.FotoVorhanden + ", JVA=" + this.JVA + ", KeineAuskunft=" + this.KeineAuskunft + ", Beruf=" + this.Beruf + ", AbschlussDatum=" + this.AbschlussDatum + ", Bemerkungen=" + this.Bemerkungen + ", BeginnBildungsgang=" + this.BeginnBildungsgang + ", DurchschnittsNote=" + this.DurchschnittsNote + ", LSSGL=" + this.LSSGL + ", LSSchulform=" + this.LSSchulform + ", KonfDruck=" + this.KonfDruck + ", DSN_Text=" + this.DSN_Text + ", Berufsabschluss=" + this.Berufsabschluss + ", LSSGL_SIM=" + this.LSSGL_SIM + ", BerufsschulpflErf=" + this.BerufsschulpflErf + ", StatusNSJ=" + this.StatusNSJ + ", FachklasseNSJ_ID=" + this.FachklasseNSJ_ID + ", BuchKonto=" + this.BuchKonto + ", VerkehrsspracheFamilie=" + this.VerkehrsspracheFamilie + ", JahrZuzug=" + this.JahrZuzug + ", DauerKindergartenbesuch=" + this.DauerKindergartenbesuch + ", VerpflichtungSprachfoerderkurs=" + this.VerpflichtungSprachfoerderkurs + ", TeilnahmeSprachfoerderkurs=" + this.TeilnahmeSprachfoerderkurs + ", SchulbuchgeldBefreit=" + this.SchulbuchgeldBefreit + ", GeburtslandSchueler=" + this.GeburtslandSchueler + ", GeburtslandVater=" + this.GeburtslandVater + ", GeburtslandMutter=" + this.GeburtslandMutter + ", Uebergangsempfehlung_JG5=" + this.Uebergangsempfehlung_JG5 + ", ErsteSchulform_SI=" + this.ErsteSchulform_SI + ", JahrWechsel_SI=" + this.JahrWechsel_SI + ", JahrWechsel_SII=" + this.JahrWechsel_SII + ", Migrationshintergrund=" + this.Migrationshintergrund + ", ExterneSchulNr=" + this.ExterneSchulNr + ", Kindergarten_ID=" + this.Kindergarten_ID + ", LetzterBerufsAbschluss=" + this.LetzterBerufsAbschluss + ", LetzterAllgAbschluss=" + this.LetzterAllgAbschluss + ", Land=" + this.Land + ", Duplikat=" + this.Duplikat + ", EinschulungsartASD=" + this.EinschulungsartASD + ", DurchschnittsnoteFHR=" + this.DurchschnittsnoteFHR + ", DSN_FHR_Text=" + this.DSN_FHR_Text + ", Eigenanteil=" + this.Eigenanteil + ", BKAZVO=" + this.BKAZVO + ", HatBerufsausbildung=" + this.HatBerufsausbildung + ", Ausweisnummer=" + this.Ausweisnummer + ", EPJahre=" + this.EPJahre + ", LSBemerkung=" + this.LSBemerkung + ", WechselBestaetigt=" + this.WechselBestaetigt + ", DauerBildungsgang=" + this.DauerBildungsgang + ", AnmeldeDatum=" + this.AnmeldeDatum + ", MeisterBafoeg=" + this.MeisterBafoeg + ", OnlineAnmeldung=" + this.OnlineAnmeldung + ", Dokumentenverzeichnis=" + this.Dokumentenverzeichnis + ", Berufsqualifikation=" + this.Berufsqualifikation + ", ZusatzNachname=" + this.ZusatzNachname + ", EndeEingliederung=" + this.EndeEingliederung + ", SchulEmail=" + this.SchulEmail + ", EndeAnschlussfoerderung=" + this.EndeAnschlussfoerderung + ", MasernImpfnachweis=" + this.MasernImpfnachweis + ", Lernstandsbericht=" + this.Lernstandsbericht + ", SprachfoerderungVon=" + this.SprachfoerderungVon + ", SprachfoerderungBis=" + this.SprachfoerderungBis + ", EntlassungBemerkung=" + this.EntlassungBemerkung + ", CredentialID=" + this.CredentialID + ", NeuZugewandert=" + this.NeuZugewandert + ")";
	}

}