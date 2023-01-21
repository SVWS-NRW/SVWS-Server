package de.nrw.schule.svws.db.dto.dev.schild.schueler;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.converter.current.GeschlechtConverter;
import de.nrw.schule.svws.db.converter.current.NationalitaetenConverter;
import de.nrw.schule.svws.db.converter.current.SchuelerStatusConverter;
import de.nrw.schule.svws.db.converter.current.VerkehrssprachenConverter;

import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.core.types.schule.Verkehrssprache;


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
import de.nrw.schule.svws.csv.converter.current.VerkehrssprachenConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.VerkehrssprachenConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schueler")
@NamedQuery(name="DevDTOSchueler.all", query="SELECT e FROM DevDTOSchueler e")
@NamedQuery(name="DevDTOSchueler.id", query="SELECT e FROM DevDTOSchueler e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchueler.id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchueler.schuljahresabschnitts_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DevDTOSchueler.schuljahresabschnitts_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DevDTOSchueler.gu_id", query="SELECT e FROM DevDTOSchueler e WHERE e.GU_ID = :value")
@NamedQuery(name="DevDTOSchueler.gu_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.GU_ID IN :value")
@NamedQuery(name="DevDTOSchueler.srcid", query="SELECT e FROM DevDTOSchueler e WHERE e.SrcID = :value")
@NamedQuery(name="DevDTOSchueler.srcid.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SrcID IN :value")
@NamedQuery(name="DevDTOSchueler.idext", query="SELECT e FROM DevDTOSchueler e WHERE e.IDext = :value")
@NamedQuery(name="DevDTOSchueler.idext.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.IDext IN :value")
@NamedQuery(name="DevDTOSchueler.status", query="SELECT e FROM DevDTOSchueler e WHERE e.Status = :value")
@NamedQuery(name="DevDTOSchueler.status.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Status IN :value")
@NamedQuery(name="DevDTOSchueler.nachname", query="SELECT e FROM DevDTOSchueler e WHERE e.Nachname = :value")
@NamedQuery(name="DevDTOSchueler.nachname.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Nachname IN :value")
@NamedQuery(name="DevDTOSchueler.vorname", query="SELECT e FROM DevDTOSchueler e WHERE e.Vorname = :value")
@NamedQuery(name="DevDTOSchueler.vorname.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Vorname IN :value")
@NamedQuery(name="DevDTOSchueler.allevornamen", query="SELECT e FROM DevDTOSchueler e WHERE e.AlleVornamen = :value")
@NamedQuery(name="DevDTOSchueler.allevornamen.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.AlleVornamen IN :value")
@NamedQuery(name="DevDTOSchueler.geburtsname", query="SELECT e FROM DevDTOSchueler e WHERE e.Geburtsname = :value")
@NamedQuery(name="DevDTOSchueler.geburtsname.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Geburtsname IN :value")
@NamedQuery(name="DevDTOSchueler.strassenname", query="SELECT e FROM DevDTOSchueler e WHERE e.Strassenname = :value")
@NamedQuery(name="DevDTOSchueler.strassenname.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Strassenname IN :value")
@NamedQuery(name="DevDTOSchueler.hausnr", query="SELECT e FROM DevDTOSchueler e WHERE e.HausNr = :value")
@NamedQuery(name="DevDTOSchueler.hausnr.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.HausNr IN :value")
@NamedQuery(name="DevDTOSchueler.hausnrzusatz", query="SELECT e FROM DevDTOSchueler e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="DevDTOSchueler.hausnrzusatz.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="DevDTOSchueler.ort_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Ort_ID = :value")
@NamedQuery(name="DevDTOSchueler.ort_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Ort_ID IN :value")
@NamedQuery(name="DevDTOSchueler.ortsteil_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Ortsteil_ID = :value")
@NamedQuery(name="DevDTOSchueler.ortsteil_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Ortsteil_ID IN :value")
@NamedQuery(name="DevDTOSchueler.telefon", query="SELECT e FROM DevDTOSchueler e WHERE e.Telefon = :value")
@NamedQuery(name="DevDTOSchueler.telefon.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Telefon IN :value")
@NamedQuery(name="DevDTOSchueler.email", query="SELECT e FROM DevDTOSchueler e WHERE e.Email = :value")
@NamedQuery(name="DevDTOSchueler.email.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Email IN :value")
@NamedQuery(name="DevDTOSchueler.fax", query="SELECT e FROM DevDTOSchueler e WHERE e.Fax = :value")
@NamedQuery(name="DevDTOSchueler.fax.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Fax IN :value")
@NamedQuery(name="DevDTOSchueler.geburtsdatum", query="SELECT e FROM DevDTOSchueler e WHERE e.Geburtsdatum = :value")
@NamedQuery(name="DevDTOSchueler.geburtsdatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Geburtsdatum IN :value")
@NamedQuery(name="DevDTOSchueler.geburtsort", query="SELECT e FROM DevDTOSchueler e WHERE e.Geburtsort = :value")
@NamedQuery(name="DevDTOSchueler.geburtsort.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Geburtsort IN :value")
@NamedQuery(name="DevDTOSchueler.volljaehrig", query="SELECT e FROM DevDTOSchueler e WHERE e.Volljaehrig = :value")
@NamedQuery(name="DevDTOSchueler.volljaehrig.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Volljaehrig IN :value")
@NamedQuery(name="DevDTOSchueler.geschlecht", query="SELECT e FROM DevDTOSchueler e WHERE e.Geschlecht = :value")
@NamedQuery(name="DevDTOSchueler.geschlecht.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Geschlecht IN :value")
@NamedQuery(name="DevDTOSchueler.staatkrz", query="SELECT e FROM DevDTOSchueler e WHERE e.StaatKrz = :value")
@NamedQuery(name="DevDTOSchueler.staatkrz.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.StaatKrz IN :value")
@NamedQuery(name="DevDTOSchueler.staatkrz2", query="SELECT e FROM DevDTOSchueler e WHERE e.StaatKrz2 = :value")
@NamedQuery(name="DevDTOSchueler.staatkrz2.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.StaatKrz2 IN :value")
@NamedQuery(name="DevDTOSchueler.aussiedler", query="SELECT e FROM DevDTOSchueler e WHERE e.Aussiedler = :value")
@NamedQuery(name="DevDTOSchueler.aussiedler.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Aussiedler IN :value")
@NamedQuery(name="DevDTOSchueler.religion_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Religion_ID = :value")
@NamedQuery(name="DevDTOSchueler.religion_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Religion_ID IN :value")
@NamedQuery(name="DevDTOSchueler.religionsabmeldung", query="SELECT e FROM DevDTOSchueler e WHERE e.Religionsabmeldung = :value")
@NamedQuery(name="DevDTOSchueler.religionsabmeldung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Religionsabmeldung IN :value")
@NamedQuery(name="DevDTOSchueler.religionsanmeldung", query="SELECT e FROM DevDTOSchueler e WHERE e.Religionsanmeldung = :value")
@NamedQuery(name="DevDTOSchueler.religionsanmeldung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Religionsanmeldung IN :value")
@NamedQuery(name="DevDTOSchueler.bafoeg", query="SELECT e FROM DevDTOSchueler e WHERE e.Bafoeg = :value")
@NamedQuery(name="DevDTOSchueler.bafoeg.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Bafoeg IN :value")
@NamedQuery(name="DevDTOSchueler.sportbefreiung_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Sportbefreiung_ID = :value")
@NamedQuery(name="DevDTOSchueler.sportbefreiung_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Sportbefreiung_ID IN :value")
@NamedQuery(name="DevDTOSchueler.fahrschueler_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Fahrschueler_ID = :value")
@NamedQuery(name="DevDTOSchueler.fahrschueler_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Fahrschueler_ID IN :value")
@NamedQuery(name="DevDTOSchueler.haltestelle_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Haltestelle_ID = :value")
@NamedQuery(name="DevDTOSchueler.haltestelle_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Haltestelle_ID IN :value")
@NamedQuery(name="DevDTOSchueler.schulpflichterf", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulpflichtErf = :value")
@NamedQuery(name="DevDTOSchueler.schulpflichterf.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulpflichtErf IN :value")
@NamedQuery(name="DevDTOSchueler.aufnahmedatum", query="SELECT e FROM DevDTOSchueler e WHERE e.Aufnahmedatum = :value")
@NamedQuery(name="DevDTOSchueler.aufnahmedatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Aufnahmedatum IN :value")
@NamedQuery(name="DevDTOSchueler.einschulungsjahr", query="SELECT e FROM DevDTOSchueler e WHERE e.Einschulungsjahr = :value")
@NamedQuery(name="DevDTOSchueler.einschulungsjahr.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Einschulungsjahr IN :value")
@NamedQuery(name="DevDTOSchueler.einschulungsart_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Einschulungsart_ID = :value")
@NamedQuery(name="DevDTOSchueler.einschulungsart_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Einschulungsart_ID IN :value")
@NamedQuery(name="DevDTOSchueler.lsschulnr", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulNr = :value")
@NamedQuery(name="DevDTOSchueler.lsschulnr.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulNr IN :value")
@NamedQuery(name="DevDTOSchueler.lsschulformsim", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulformSIM = :value")
@NamedQuery(name="DevDTOSchueler.lsschulformsim.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulformSIM IN :value")
@NamedQuery(name="DevDTOSchueler.lsjahrgang", query="SELECT e FROM DevDTOSchueler e WHERE e.LSJahrgang = :value")
@NamedQuery(name="DevDTOSchueler.lsjahrgang.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSJahrgang IN :value")
@NamedQuery(name="DevDTOSchueler.lsschulentlassdatum", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulEntlassDatum = :value")
@NamedQuery(name="DevDTOSchueler.lsschulentlassdatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulEntlassDatum IN :value")
@NamedQuery(name="DevDTOSchueler.lsversetzung", query="SELECT e FROM DevDTOSchueler e WHERE e.LSVersetzung = :value")
@NamedQuery(name="DevDTOSchueler.lsversetzung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSVersetzung IN :value")
@NamedQuery(name="DevDTOSchueler.lsfachklkennung", query="SELECT e FROM DevDTOSchueler e WHERE e.LSFachklKennung = :value")
@NamedQuery(name="DevDTOSchueler.lsfachklkennung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSFachklKennung IN :value")
@NamedQuery(name="DevDTOSchueler.lsfachklsim", query="SELECT e FROM DevDTOSchueler e WHERE e.LSFachklSIM = :value")
@NamedQuery(name="DevDTOSchueler.lsfachklsim.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSFachklSIM IN :value")
@NamedQuery(name="DevDTOSchueler.lsentlassgrund", query="SELECT e FROM DevDTOSchueler e WHERE e.LSEntlassgrund = :value")
@NamedQuery(name="DevDTOSchueler.lsentlassgrund.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSEntlassgrund IN :value")
@NamedQuery(name="DevDTOSchueler.lsentlassart", query="SELECT e FROM DevDTOSchueler e WHERE e.LSEntlassArt = :value")
@NamedQuery(name="DevDTOSchueler.lsentlassart.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSEntlassArt IN :value")
@NamedQuery(name="DevDTOSchueler.lsklassenart", query="SELECT e FROM DevDTOSchueler e WHERE e.LSKlassenart = :value")
@NamedQuery(name="DevDTOSchueler.lsklassenart.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSKlassenart IN :value")
@NamedQuery(name="DevDTOSchueler.lsrefpaed", query="SELECT e FROM DevDTOSchueler e WHERE e.LSRefPaed = :value")
@NamedQuery(name="DevDTOSchueler.lsrefpaed.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSRefPaed IN :value")
@NamedQuery(name="DevDTOSchueler.entlassjahrgang", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassjahrgang = :value")
@NamedQuery(name="DevDTOSchueler.entlassjahrgang.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassjahrgang IN :value")
@NamedQuery(name="DevDTOSchueler.entlassjahrgang_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassjahrgang_ID = :value")
@NamedQuery(name="DevDTOSchueler.entlassjahrgang_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassjahrgang_ID IN :value")
@NamedQuery(name="DevDTOSchueler.entlassdatum", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassdatum = :value")
@NamedQuery(name="DevDTOSchueler.entlassdatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassdatum IN :value")
@NamedQuery(name="DevDTOSchueler.entlassgrund", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassgrund = :value")
@NamedQuery(name="DevDTOSchueler.entlassgrund.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassgrund IN :value")
@NamedQuery(name="DevDTOSchueler.entlassart", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassart = :value")
@NamedQuery(name="DevDTOSchueler.entlassart.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Entlassart IN :value")
@NamedQuery(name="DevDTOSchueler.schulwechselnr", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulwechselNr = :value")
@NamedQuery(name="DevDTOSchueler.schulwechselnr.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulwechselNr IN :value")
@NamedQuery(name="DevDTOSchueler.schulwechseldatum", query="SELECT e FROM DevDTOSchueler e WHERE e.Schulwechseldatum = :value")
@NamedQuery(name="DevDTOSchueler.schulwechseldatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Schulwechseldatum IN :value")
@NamedQuery(name="DevDTOSchueler.geloescht", query="SELECT e FROM DevDTOSchueler e WHERE e.Geloescht = :value")
@NamedQuery(name="DevDTOSchueler.geloescht.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Geloescht IN :value")
@NamedQuery(name="DevDTOSchueler.gesperrt", query="SELECT e FROM DevDTOSchueler e WHERE e.Gesperrt = :value")
@NamedQuery(name="DevDTOSchueler.gesperrt.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Gesperrt IN :value")
@NamedQuery(name="DevDTOSchueler.modifiziertam", query="SELECT e FROM DevDTOSchueler e WHERE e.ModifiziertAm = :value")
@NamedQuery(name="DevDTOSchueler.modifiziertam.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.ModifiziertAm IN :value")
@NamedQuery(name="DevDTOSchueler.modifiziertvon", query="SELECT e FROM DevDTOSchueler e WHERE e.ModifiziertVon = :value")
@NamedQuery(name="DevDTOSchueler.modifiziertvon.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.ModifiziertVon IN :value")
@NamedQuery(name="DevDTOSchueler.markiert", query="SELECT e FROM DevDTOSchueler e WHERE e.Markiert = :value")
@NamedQuery(name="DevDTOSchueler.markiert.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Markiert IN :value")
@NamedQuery(name="DevDTOSchueler.fotovorhanden", query="SELECT e FROM DevDTOSchueler e WHERE e.FotoVorhanden = :value")
@NamedQuery(name="DevDTOSchueler.fotovorhanden.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.FotoVorhanden IN :value")
@NamedQuery(name="DevDTOSchueler.jva", query="SELECT e FROM DevDTOSchueler e WHERE e.JVA = :value")
@NamedQuery(name="DevDTOSchueler.jva.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.JVA IN :value")
@NamedQuery(name="DevDTOSchueler.keineauskunft", query="SELECT e FROM DevDTOSchueler e WHERE e.KeineAuskunft = :value")
@NamedQuery(name="DevDTOSchueler.keineauskunft.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.KeineAuskunft IN :value")
@NamedQuery(name="DevDTOSchueler.beruf", query="SELECT e FROM DevDTOSchueler e WHERE e.Beruf = :value")
@NamedQuery(name="DevDTOSchueler.beruf.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Beruf IN :value")
@NamedQuery(name="DevDTOSchueler.abschlussdatum", query="SELECT e FROM DevDTOSchueler e WHERE e.AbschlussDatum = :value")
@NamedQuery(name="DevDTOSchueler.abschlussdatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.AbschlussDatum IN :value")
@NamedQuery(name="DevDTOSchueler.bemerkungen", query="SELECT e FROM DevDTOSchueler e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DevDTOSchueler.bemerkungen.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DevDTOSchueler.beginnbildungsgang", query="SELECT e FROM DevDTOSchueler e WHERE e.BeginnBildungsgang = :value")
@NamedQuery(name="DevDTOSchueler.beginnbildungsgang.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.BeginnBildungsgang IN :value")
@NamedQuery(name="DevDTOSchueler.durchschnittsnote", query="SELECT e FROM DevDTOSchueler e WHERE e.DurchschnittsNote = :value")
@NamedQuery(name="DevDTOSchueler.durchschnittsnote.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.DurchschnittsNote IN :value")
@NamedQuery(name="DevDTOSchueler.lssgl", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSGL = :value")
@NamedQuery(name="DevDTOSchueler.lssgl.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSGL IN :value")
@NamedQuery(name="DevDTOSchueler.lsschulform", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulform = :value")
@NamedQuery(name="DevDTOSchueler.lsschulform.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSchulform IN :value")
@NamedQuery(name="DevDTOSchueler.konfdruck", query="SELECT e FROM DevDTOSchueler e WHERE e.KonfDruck = :value")
@NamedQuery(name="DevDTOSchueler.konfdruck.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.KonfDruck IN :value")
@NamedQuery(name="DevDTOSchueler.dsn_text", query="SELECT e FROM DevDTOSchueler e WHERE e.DSN_Text = :value")
@NamedQuery(name="DevDTOSchueler.dsn_text.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.DSN_Text IN :value")
@NamedQuery(name="DevDTOSchueler.berufsabschluss", query="SELECT e FROM DevDTOSchueler e WHERE e.Berufsabschluss = :value")
@NamedQuery(name="DevDTOSchueler.berufsabschluss.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Berufsabschluss IN :value")
@NamedQuery(name="DevDTOSchueler.lssgl_sim", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSGL_SIM = :value")
@NamedQuery(name="DevDTOSchueler.lssgl_sim.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSSGL_SIM IN :value")
@NamedQuery(name="DevDTOSchueler.berufsschulpflerf", query="SELECT e FROM DevDTOSchueler e WHERE e.BerufsschulpflErf = :value")
@NamedQuery(name="DevDTOSchueler.berufsschulpflerf.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.BerufsschulpflErf IN :value")
@NamedQuery(name="DevDTOSchueler.statusnsj", query="SELECT e FROM DevDTOSchueler e WHERE e.StatusNSJ = :value")
@NamedQuery(name="DevDTOSchueler.statusnsj.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.StatusNSJ IN :value")
@NamedQuery(name="DevDTOSchueler.fachklassensj_id", query="SELECT e FROM DevDTOSchueler e WHERE e.FachklasseNSJ_ID = :value")
@NamedQuery(name="DevDTOSchueler.fachklassensj_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.FachklasseNSJ_ID IN :value")
@NamedQuery(name="DevDTOSchueler.buchkonto", query="SELECT e FROM DevDTOSchueler e WHERE e.BuchKonto = :value")
@NamedQuery(name="DevDTOSchueler.buchkonto.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.BuchKonto IN :value")
@NamedQuery(name="DevDTOSchueler.verkehrssprachefamilie", query="SELECT e FROM DevDTOSchueler e WHERE e.VerkehrsspracheFamilie = :value")
@NamedQuery(name="DevDTOSchueler.verkehrssprachefamilie.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.VerkehrsspracheFamilie IN :value")
@NamedQuery(name="DevDTOSchueler.jahrzuzug", query="SELECT e FROM DevDTOSchueler e WHERE e.JahrZuzug = :value")
@NamedQuery(name="DevDTOSchueler.jahrzuzug.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.JahrZuzug IN :value")
@NamedQuery(name="DevDTOSchueler.dauerkindergartenbesuch", query="SELECT e FROM DevDTOSchueler e WHERE e.DauerKindergartenbesuch = :value")
@NamedQuery(name="DevDTOSchueler.dauerkindergartenbesuch.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.DauerKindergartenbesuch IN :value")
@NamedQuery(name="DevDTOSchueler.verpflichtungsprachfoerderkurs", query="SELECT e FROM DevDTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs = :value")
@NamedQuery(name="DevDTOSchueler.verpflichtungsprachfoerderkurs.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.VerpflichtungSprachfoerderkurs IN :value")
@NamedQuery(name="DevDTOSchueler.teilnahmesprachfoerderkurs", query="SELECT e FROM DevDTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs = :value")
@NamedQuery(name="DevDTOSchueler.teilnahmesprachfoerderkurs.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.TeilnahmeSprachfoerderkurs IN :value")
@NamedQuery(name="DevDTOSchueler.schulbuchgeldbefreit", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulbuchgeldBefreit = :value")
@NamedQuery(name="DevDTOSchueler.schulbuchgeldbefreit.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulbuchgeldBefreit IN :value")
@NamedQuery(name="DevDTOSchueler.geburtslandschueler", query="SELECT e FROM DevDTOSchueler e WHERE e.GeburtslandSchueler = :value")
@NamedQuery(name="DevDTOSchueler.geburtslandschueler.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.GeburtslandSchueler IN :value")
@NamedQuery(name="DevDTOSchueler.geburtslandvater", query="SELECT e FROM DevDTOSchueler e WHERE e.GeburtslandVater = :value")
@NamedQuery(name="DevDTOSchueler.geburtslandvater.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.GeburtslandVater IN :value")
@NamedQuery(name="DevDTOSchueler.geburtslandmutter", query="SELECT e FROM DevDTOSchueler e WHERE e.GeburtslandMutter = :value")
@NamedQuery(name="DevDTOSchueler.geburtslandmutter.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.GeburtslandMutter IN :value")
@NamedQuery(name="DevDTOSchueler.uebergangsempfehlung_jg5", query="SELECT e FROM DevDTOSchueler e WHERE e.Uebergangsempfehlung_JG5 = :value")
@NamedQuery(name="DevDTOSchueler.uebergangsempfehlung_jg5.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Uebergangsempfehlung_JG5 IN :value")
@NamedQuery(name="DevDTOSchueler.ersteschulform_si", query="SELECT e FROM DevDTOSchueler e WHERE e.ErsteSchulform_SI = :value")
@NamedQuery(name="DevDTOSchueler.ersteschulform_si.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.ErsteSchulform_SI IN :value")
@NamedQuery(name="DevDTOSchueler.jahrwechsel_si", query="SELECT e FROM DevDTOSchueler e WHERE e.JahrWechsel_SI = :value")
@NamedQuery(name="DevDTOSchueler.jahrwechsel_si.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.JahrWechsel_SI IN :value")
@NamedQuery(name="DevDTOSchueler.jahrwechsel_sii", query="SELECT e FROM DevDTOSchueler e WHERE e.JahrWechsel_SII = :value")
@NamedQuery(name="DevDTOSchueler.jahrwechsel_sii.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.JahrWechsel_SII IN :value")
@NamedQuery(name="DevDTOSchueler.migrationshintergrund", query="SELECT e FROM DevDTOSchueler e WHERE e.Migrationshintergrund = :value")
@NamedQuery(name="DevDTOSchueler.migrationshintergrund.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Migrationshintergrund IN :value")
@NamedQuery(name="DevDTOSchueler.externeschulnr", query="SELECT e FROM DevDTOSchueler e WHERE e.ExterneSchulNr = :value")
@NamedQuery(name="DevDTOSchueler.externeschulnr.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.ExterneSchulNr IN :value")
@NamedQuery(name="DevDTOSchueler.kindergarten_id", query="SELECT e FROM DevDTOSchueler e WHERE e.Kindergarten_ID = :value")
@NamedQuery(name="DevDTOSchueler.kindergarten_id.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Kindergarten_ID IN :value")
@NamedQuery(name="DevDTOSchueler.letzterberufsabschluss", query="SELECT e FROM DevDTOSchueler e WHERE e.LetzterBerufsAbschluss = :value")
@NamedQuery(name="DevDTOSchueler.letzterberufsabschluss.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LetzterBerufsAbschluss IN :value")
@NamedQuery(name="DevDTOSchueler.letzterallgabschluss", query="SELECT e FROM DevDTOSchueler e WHERE e.LetzterAllgAbschluss = :value")
@NamedQuery(name="DevDTOSchueler.letzterallgabschluss.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LetzterAllgAbschluss IN :value")
@NamedQuery(name="DevDTOSchueler.land", query="SELECT e FROM DevDTOSchueler e WHERE e.Land = :value")
@NamedQuery(name="DevDTOSchueler.land.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Land IN :value")
@NamedQuery(name="DevDTOSchueler.duplikat", query="SELECT e FROM DevDTOSchueler e WHERE e.Duplikat = :value")
@NamedQuery(name="DevDTOSchueler.duplikat.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Duplikat IN :value")
@NamedQuery(name="DevDTOSchueler.einschulungsartasd", query="SELECT e FROM DevDTOSchueler e WHERE e.EinschulungsartASD = :value")
@NamedQuery(name="DevDTOSchueler.einschulungsartasd.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.EinschulungsartASD IN :value")
@NamedQuery(name="DevDTOSchueler.durchschnittsnotefhr", query="SELECT e FROM DevDTOSchueler e WHERE e.DurchschnittsnoteFHR = :value")
@NamedQuery(name="DevDTOSchueler.durchschnittsnotefhr.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.DurchschnittsnoteFHR IN :value")
@NamedQuery(name="DevDTOSchueler.dsn_fhr_text", query="SELECT e FROM DevDTOSchueler e WHERE e.DSN_FHR_Text = :value")
@NamedQuery(name="DevDTOSchueler.dsn_fhr_text.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.DSN_FHR_Text IN :value")
@NamedQuery(name="DevDTOSchueler.eigenanteil", query="SELECT e FROM DevDTOSchueler e WHERE e.Eigenanteil = :value")
@NamedQuery(name="DevDTOSchueler.eigenanteil.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Eigenanteil IN :value")
@NamedQuery(name="DevDTOSchueler.bkazvo", query="SELECT e FROM DevDTOSchueler e WHERE e.BKAZVO = :value")
@NamedQuery(name="DevDTOSchueler.bkazvo.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.BKAZVO IN :value")
@NamedQuery(name="DevDTOSchueler.hatberufsausbildung", query="SELECT e FROM DevDTOSchueler e WHERE e.HatBerufsausbildung = :value")
@NamedQuery(name="DevDTOSchueler.hatberufsausbildung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.HatBerufsausbildung IN :value")
@NamedQuery(name="DevDTOSchueler.ausweisnummer", query="SELECT e FROM DevDTOSchueler e WHERE e.Ausweisnummer = :value")
@NamedQuery(name="DevDTOSchueler.ausweisnummer.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Ausweisnummer IN :value")
@NamedQuery(name="DevDTOSchueler.epjahre", query="SELECT e FROM DevDTOSchueler e WHERE e.EPJahre = :value")
@NamedQuery(name="DevDTOSchueler.epjahre.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.EPJahre IN :value")
@NamedQuery(name="DevDTOSchueler.lsbemerkung", query="SELECT e FROM DevDTOSchueler e WHERE e.LSBemerkung = :value")
@NamedQuery(name="DevDTOSchueler.lsbemerkung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.LSBemerkung IN :value")
@NamedQuery(name="DevDTOSchueler.wechselbestaetigt", query="SELECT e FROM DevDTOSchueler e WHERE e.WechselBestaetigt = :value")
@NamedQuery(name="DevDTOSchueler.wechselbestaetigt.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.WechselBestaetigt IN :value")
@NamedQuery(name="DevDTOSchueler.dauerbildungsgang", query="SELECT e FROM DevDTOSchueler e WHERE e.DauerBildungsgang = :value")
@NamedQuery(name="DevDTOSchueler.dauerbildungsgang.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.DauerBildungsgang IN :value")
@NamedQuery(name="DevDTOSchueler.anmeldedatum", query="SELECT e FROM DevDTOSchueler e WHERE e.AnmeldeDatum = :value")
@NamedQuery(name="DevDTOSchueler.anmeldedatum.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.AnmeldeDatum IN :value")
@NamedQuery(name="DevDTOSchueler.meisterbafoeg", query="SELECT e FROM DevDTOSchueler e WHERE e.MeisterBafoeg = :value")
@NamedQuery(name="DevDTOSchueler.meisterbafoeg.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.MeisterBafoeg IN :value")
@NamedQuery(name="DevDTOSchueler.onlineanmeldung", query="SELECT e FROM DevDTOSchueler e WHERE e.OnlineAnmeldung = :value")
@NamedQuery(name="DevDTOSchueler.onlineanmeldung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.OnlineAnmeldung IN :value")
@NamedQuery(name="DevDTOSchueler.dokumentenverzeichnis", query="SELECT e FROM DevDTOSchueler e WHERE e.Dokumentenverzeichnis = :value")
@NamedQuery(name="DevDTOSchueler.dokumentenverzeichnis.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Dokumentenverzeichnis IN :value")
@NamedQuery(name="DevDTOSchueler.berufsqualifikation", query="SELECT e FROM DevDTOSchueler e WHERE e.Berufsqualifikation = :value")
@NamedQuery(name="DevDTOSchueler.berufsqualifikation.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Berufsqualifikation IN :value")
@NamedQuery(name="DevDTOSchueler.zusatznachname", query="SELECT e FROM DevDTOSchueler e WHERE e.ZusatzNachname = :value")
@NamedQuery(name="DevDTOSchueler.zusatznachname.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.ZusatzNachname IN :value")
@NamedQuery(name="DevDTOSchueler.endeeingliederung", query="SELECT e FROM DevDTOSchueler e WHERE e.EndeEingliederung = :value")
@NamedQuery(name="DevDTOSchueler.endeeingliederung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.EndeEingliederung IN :value")
@NamedQuery(name="DevDTOSchueler.schulemail", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulEmail = :value")
@NamedQuery(name="DevDTOSchueler.schulemail.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SchulEmail IN :value")
@NamedQuery(name="DevDTOSchueler.endeanschlussfoerderung", query="SELECT e FROM DevDTOSchueler e WHERE e.EndeAnschlussfoerderung = :value")
@NamedQuery(name="DevDTOSchueler.endeanschlussfoerderung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.EndeAnschlussfoerderung IN :value")
@NamedQuery(name="DevDTOSchueler.masernimpfnachweis", query="SELECT e FROM DevDTOSchueler e WHERE e.MasernImpfnachweis = :value")
@NamedQuery(name="DevDTOSchueler.masernimpfnachweis.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.MasernImpfnachweis IN :value")
@NamedQuery(name="DevDTOSchueler.lernstandsbericht", query="SELECT e FROM DevDTOSchueler e WHERE e.Lernstandsbericht = :value")
@NamedQuery(name="DevDTOSchueler.lernstandsbericht.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.Lernstandsbericht IN :value")
@NamedQuery(name="DevDTOSchueler.sprachfoerderungvon", query="SELECT e FROM DevDTOSchueler e WHERE e.SprachfoerderungVon = :value")
@NamedQuery(name="DevDTOSchueler.sprachfoerderungvon.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SprachfoerderungVon IN :value")
@NamedQuery(name="DevDTOSchueler.sprachfoerderungbis", query="SELECT e FROM DevDTOSchueler e WHERE e.SprachfoerderungBis = :value")
@NamedQuery(name="DevDTOSchueler.sprachfoerderungbis.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.SprachfoerderungBis IN :value")
@NamedQuery(name="DevDTOSchueler.entlassungbemerkung", query="SELECT e FROM DevDTOSchueler e WHERE e.EntlassungBemerkung = :value")
@NamedQuery(name="DevDTOSchueler.entlassungbemerkung.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.EntlassungBemerkung IN :value")
@NamedQuery(name="DevDTOSchueler.credentialid", query="SELECT e FROM DevDTOSchueler e WHERE e.CredentialID = :value")
@NamedQuery(name="DevDTOSchueler.credentialid.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.CredentialID IN :value")
@NamedQuery(name="DevDTOSchueler.neuzugewandert", query="SELECT e FROM DevDTOSchueler e WHERE e.NeuZugewandert = :value")
@NamedQuery(name="DevDTOSchueler.neuzugewandert.multiple", query="SELECT e FROM DevDTOSchueler e WHERE e.NeuZugewandert IN :value")
@NamedQuery(name="DevDTOSchueler.primaryKeyQuery", query="SELECT e FROM DevDTOSchueler e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchueler.all.migration", query="SELECT e FROM DevDTOSchueler e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schuljahresabschnitts_ID","GU_ID","SrcID","IDext","Status","Nachname","Vorname","AlleVornamen","Geburtsname","Strassenname","HausNr","HausNrZusatz","Ort_ID","Ortsteil_ID","Telefon","Email","Fax","Geburtsdatum","Geburtsort","Volljaehrig","Geschlecht","StaatKrz","StaatKrz2","Aussiedler","Religion_ID","Religionsabmeldung","Religionsanmeldung","Bafoeg","Sportbefreiung_ID","Fahrschueler_ID","Haltestelle_ID","SchulpflichtErf","Aufnahmedatum","Einschulungsjahr","Einschulungsart_ID","LSSchulNr","LSSchulformSIM","LSJahrgang","LSSchulEntlassDatum","LSVersetzung","LSFachklKennung","LSFachklSIM","LSEntlassgrund","LSEntlassArt","LSKlassenart","LSRefPaed","Entlassjahrgang","Entlassjahrgang_ID","Entlassdatum","Entlassgrund","Entlassart","SchulwechselNr","Schulwechseldatum","Geloescht","Gesperrt","ModifiziertAm","ModifiziertVon","Markiert","FotoVorhanden","JVA","KeineAuskunft","Beruf","AbschlussDatum","Bemerkungen","BeginnBildungsgang","DurchschnittsNote","LSSGL","LSSchulform","KonfDruck","DSN_Text","Berufsabschluss","LSSGL_SIM","BerufsschulpflErf","StatusNSJ","FachklasseNSJ_ID","BuchKonto","VerkehrsspracheFamilie","JahrZuzug","DauerKindergartenbesuch","VerpflichtungSprachfoerderkurs","TeilnahmeSprachfoerderkurs","SchulbuchgeldBefreit","GeburtslandSchueler","GeburtslandVater","GeburtslandMutter","Uebergangsempfehlung_JG5","ErsteSchulform_SI","JahrWechsel_SI","JahrWechsel_SII","Migrationshintergrund","ExterneSchulNr","Kindergarten_ID","LetzterBerufsAbschluss","LetzterAllgAbschluss","Land","Duplikat","EinschulungsartASD","DurchschnittsnoteFHR","DSN_FHR_Text","Eigenanteil","BKAZVO","HatBerufsausbildung","Ausweisnummer","EPJahre","LSBemerkung","WechselBestaetigt","DauerBildungsgang","AnmeldeDatum","MeisterBafoeg","OnlineAnmeldung","Dokumentenverzeichnis","Berufsqualifikation","ZusatzNachname","EndeEingliederung","SchulEmail","EndeAnschlussfoerderung","MasernImpfnachweis","Lernstandsbericht","SprachfoerderungVon","SprachfoerderungBis","EntlassungBemerkung","CredentialID","NeuZugewandert"})
public class DevDTOSchueler {

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
	@Convert(converter=VerkehrssprachenConverter.class)
	@JsonSerialize(using=VerkehrssprachenConverterSerializer.class)
	@JsonDeserialize(using=VerkehrssprachenConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DevDTOSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchueler ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param GU_ID   der Wert für das Attribut GU_ID
	 * @param NeuZugewandert   der Wert für das Attribut NeuZugewandert
	 */
	public DevDTOSchueler(final Long ID, final String GU_ID, final Boolean NeuZugewandert) {
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
		DevDTOSchueler other = (DevDTOSchueler) obj;
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
		return "DevDTOSchueler(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", GU_ID=" + this.GU_ID + ", SrcID=" + this.SrcID + ", IDext=" + this.IDext + ", Status=" + this.Status + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", AlleVornamen=" + this.AlleVornamen + ", Geburtsname=" + this.Geburtsname + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", Ortsteil_ID=" + this.Ortsteil_ID + ", Telefon=" + this.Telefon + ", Email=" + this.Email + ", Fax=" + this.Fax + ", Geburtsdatum=" + this.Geburtsdatum + ", Geburtsort=" + this.Geburtsort + ", Volljaehrig=" + this.Volljaehrig + ", Geschlecht=" + this.Geschlecht + ", StaatKrz=" + this.StaatKrz + ", StaatKrz2=" + this.StaatKrz2 + ", Aussiedler=" + this.Aussiedler + ", Religion_ID=" + this.Religion_ID + ", Religionsabmeldung=" + this.Religionsabmeldung + ", Religionsanmeldung=" + this.Religionsanmeldung + ", Bafoeg=" + this.Bafoeg + ", Sportbefreiung_ID=" + this.Sportbefreiung_ID + ", Fahrschueler_ID=" + this.Fahrschueler_ID + ", Haltestelle_ID=" + this.Haltestelle_ID + ", SchulpflichtErf=" + this.SchulpflichtErf + ", Aufnahmedatum=" + this.Aufnahmedatum + ", Einschulungsjahr=" + this.Einschulungsjahr + ", Einschulungsart_ID=" + this.Einschulungsart_ID + ", LSSchulNr=" + this.LSSchulNr + ", LSSchulformSIM=" + this.LSSchulformSIM + ", LSJahrgang=" + this.LSJahrgang + ", LSSchulEntlassDatum=" + this.LSSchulEntlassDatum + ", LSVersetzung=" + this.LSVersetzung + ", LSFachklKennung=" + this.LSFachklKennung + ", LSFachklSIM=" + this.LSFachklSIM + ", LSEntlassgrund=" + this.LSEntlassgrund + ", LSEntlassArt=" + this.LSEntlassArt + ", LSKlassenart=" + this.LSKlassenart + ", LSRefPaed=" + this.LSRefPaed + ", Entlassjahrgang=" + this.Entlassjahrgang + ", Entlassjahrgang_ID=" + this.Entlassjahrgang_ID + ", Entlassdatum=" + this.Entlassdatum + ", Entlassgrund=" + this.Entlassgrund + ", Entlassart=" + this.Entlassart + ", SchulwechselNr=" + this.SchulwechselNr + ", Schulwechseldatum=" + this.Schulwechseldatum + ", Geloescht=" + this.Geloescht + ", Gesperrt=" + this.Gesperrt + ", ModifiziertAm=" + this.ModifiziertAm + ", ModifiziertVon=" + this.ModifiziertVon + ", Markiert=" + this.Markiert + ", FotoVorhanden=" + this.FotoVorhanden + ", JVA=" + this.JVA + ", KeineAuskunft=" + this.KeineAuskunft + ", Beruf=" + this.Beruf + ", AbschlussDatum=" + this.AbschlussDatum + ", Bemerkungen=" + this.Bemerkungen + ", BeginnBildungsgang=" + this.BeginnBildungsgang + ", DurchschnittsNote=" + this.DurchschnittsNote + ", LSSGL=" + this.LSSGL + ", LSSchulform=" + this.LSSchulform + ", KonfDruck=" + this.KonfDruck + ", DSN_Text=" + this.DSN_Text + ", Berufsabschluss=" + this.Berufsabschluss + ", LSSGL_SIM=" + this.LSSGL_SIM + ", BerufsschulpflErf=" + this.BerufsschulpflErf + ", StatusNSJ=" + this.StatusNSJ + ", FachklasseNSJ_ID=" + this.FachklasseNSJ_ID + ", BuchKonto=" + this.BuchKonto + ", VerkehrsspracheFamilie=" + this.VerkehrsspracheFamilie + ", JahrZuzug=" + this.JahrZuzug + ", DauerKindergartenbesuch=" + this.DauerKindergartenbesuch + ", VerpflichtungSprachfoerderkurs=" + this.VerpflichtungSprachfoerderkurs + ", TeilnahmeSprachfoerderkurs=" + this.TeilnahmeSprachfoerderkurs + ", SchulbuchgeldBefreit=" + this.SchulbuchgeldBefreit + ", GeburtslandSchueler=" + this.GeburtslandSchueler + ", GeburtslandVater=" + this.GeburtslandVater + ", GeburtslandMutter=" + this.GeburtslandMutter + ", Uebergangsempfehlung_JG5=" + this.Uebergangsempfehlung_JG5 + ", ErsteSchulform_SI=" + this.ErsteSchulform_SI + ", JahrWechsel_SI=" + this.JahrWechsel_SI + ", JahrWechsel_SII=" + this.JahrWechsel_SII + ", Migrationshintergrund=" + this.Migrationshintergrund + ", ExterneSchulNr=" + this.ExterneSchulNr + ", Kindergarten_ID=" + this.Kindergarten_ID + ", LetzterBerufsAbschluss=" + this.LetzterBerufsAbschluss + ", LetzterAllgAbschluss=" + this.LetzterAllgAbschluss + ", Land=" + this.Land + ", Duplikat=" + this.Duplikat + ", EinschulungsartASD=" + this.EinschulungsartASD + ", DurchschnittsnoteFHR=" + this.DurchschnittsnoteFHR + ", DSN_FHR_Text=" + this.DSN_FHR_Text + ", Eigenanteil=" + this.Eigenanteil + ", BKAZVO=" + this.BKAZVO + ", HatBerufsausbildung=" + this.HatBerufsausbildung + ", Ausweisnummer=" + this.Ausweisnummer + ", EPJahre=" + this.EPJahre + ", LSBemerkung=" + this.LSBemerkung + ", WechselBestaetigt=" + this.WechselBestaetigt + ", DauerBildungsgang=" + this.DauerBildungsgang + ", AnmeldeDatum=" + this.AnmeldeDatum + ", MeisterBafoeg=" + this.MeisterBafoeg + ", OnlineAnmeldung=" + this.OnlineAnmeldung + ", Dokumentenverzeichnis=" + this.Dokumentenverzeichnis + ", Berufsqualifikation=" + this.Berufsqualifikation + ", ZusatzNachname=" + this.ZusatzNachname + ", EndeEingliederung=" + this.EndeEingliederung + ", SchulEmail=" + this.SchulEmail + ", EndeAnschlussfoerderung=" + this.EndeAnschlussfoerderung + ", MasernImpfnachweis=" + this.MasernImpfnachweis + ", Lernstandsbericht=" + this.Lernstandsbericht + ", SprachfoerderungVon=" + this.SprachfoerderungVon + ", SprachfoerderungBis=" + this.SprachfoerderungBis + ", EntlassungBemerkung=" + this.EntlassungBemerkung + ", CredentialID=" + this.CredentialID + ", NeuZugewandert=" + this.NeuZugewandert + ")";
	}

}