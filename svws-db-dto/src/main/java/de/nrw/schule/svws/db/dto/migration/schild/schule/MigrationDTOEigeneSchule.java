package de.nrw.schule.svws.db.dto.migration.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationStringToIntegerConverter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationStringToIntegerConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationStringToIntegerConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule")
@NamedQuery(name="MigrationDTOEigeneSchule.all", query="SELECT e FROM MigrationDTOEigeneSchule e")
@NamedQuery(name="MigrationDTOEigeneSchule.id", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.id.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulformnr", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulformNr = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulformnr.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulformNr IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulform", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Schulform = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulform.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Schulform IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulformbez", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulformBez = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulformbez.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulformBez IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schultraegerart", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchultraegerArt = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schultraegerart.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchultraegerArt IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schultraegernr", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchultraegerNr = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schultraegernr.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchultraegerNr IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulnr", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulNr = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulnr.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulNr IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezeichnung1", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Bezeichnung1 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezeichnung1.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Bezeichnung1 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezeichnung2", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Bezeichnung2 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezeichnung2.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Bezeichnung2 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezeichnung3", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Bezeichnung3 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezeichnung3.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Bezeichnung3 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.strasse", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Strasse = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.strasse.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Strasse IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.strassenname", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Strassenname = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.strassenname.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Strassenname IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.hausnr", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.HausNr = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.hausnr.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.HausNr IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.hausnrzusatz", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.hausnrzusatz.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.plz", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.PLZ = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.plz.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.PLZ IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.ort", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Ort = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.ort.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Ort IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.telefon", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Telefon = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.telefon.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Telefon IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.fax", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Fax = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.fax.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Fax IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.email", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Email = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.email.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Email IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.ganztags", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Ganztags = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.ganztags.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Ganztags IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schuljahresabschnitts_id", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schuljahresabschnitts_id.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.anzahlabschnitte", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AnzahlAbschnitte = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.anzahlabschnitte.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AnzahlAbschnitte IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.abschnittbez", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AbschnittBez = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.abschnittbez.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AbschnittBez IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt1", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt1 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt1.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt1 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt2", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt2 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt2.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt2 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt3", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt3 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt3.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt3 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt4", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt4 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.bezabschnitt4.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.BezAbschnitt4 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.fremdsprachen", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Fremdsprachen = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.fremdsprachen.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Fremdsprachen IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.jvazeigen", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.JVAZeigen = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.jvazeigen.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.JVAZeigen IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.refpaedagogikzeigen", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.RefPaedagogikZeigen = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.refpaedagogikzeigen.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.RefPaedagogikZeigen IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.anzjgs_jahr", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AnzJGS_Jahr = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.anzjgs_jahr.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AnzJGS_Jahr IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.isthauptsitz", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.IstHauptsitz = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.isthauptsitz.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.IstHauptsitz IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.notengesperrt", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.NotenGesperrt = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.notengesperrt.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.NotenGesperrt IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltanzahl", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAnzahl = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltanzahl.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAnzahl IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltweibl", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltWeibl = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltweibl.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltWeibl IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltauslaender", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAuslaender = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltauslaender.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAuslaender IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltauslaenderweibl", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAuslaenderWeibl = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltauslaenderweibl.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAuslaenderWeibl IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltaussiedler", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAussiedler = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltaussiedler.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAussiedler IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltaussiedlerweibl", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAussiedlerWeibl = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.zurueckgestelltaussiedlerweibl.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ZurueckgestelltAussiedlerWeibl IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.teamteaching", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.TeamTeaching = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.teamteaching.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.TeamTeaching IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.abigruppenprozess", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AbiGruppenprozess = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.abigruppenprozess.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.AbiGruppenprozess IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.dauerunterrichtseinheit", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.DauerUnterrichtseinheit = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.dauerunterrichtseinheit.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.DauerUnterrichtseinheit IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.gruppen8bis1", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Gruppen8Bis1 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.gruppen8bis1.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Gruppen8Bis1 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.gruppen13plus", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Gruppen13Plus = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.gruppen13plus.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Gruppen13Plus IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.internatsplaetzem", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.InternatsplaetzeM = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.internatsplaetzem.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.InternatsplaetzeM IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.internatsplaetzew", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.InternatsplaetzeW = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.internatsplaetzew.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.InternatsplaetzeW IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.internatsplaetzeneutral", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.InternatsplaetzeNeutral = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.internatsplaetzeneutral.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.InternatsplaetzeNeutral IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schullogo", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulLogo = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schullogo.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulLogo IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schullogobase64", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulLogoBase64 = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schullogobase64.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulLogoBase64 IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulnreigner", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulnreigner.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleitername", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterName = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleitername.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterName IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleitervorname", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterVorname = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleitervorname.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterVorname IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleiteramtsbez", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterAmtsbez = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleiteramtsbez.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterAmtsbez IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleitergeschlecht", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterGeschlecht = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schulleitergeschlecht.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchulleiterGeschlecht IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleitername", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterName = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleitername.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterName IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleitervorname", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterVorname = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleitervorname.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterVorname IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleiteramtsbez", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterAmtsbez = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleiteramtsbez.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterAmtsbez IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleitergeschlecht", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterGeschlecht = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.stvschulleitergeschlecht.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.StvSchulleiterGeschlecht IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.einstellungen", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Einstellungen = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.einstellungen.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Einstellungen IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.webadresse", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.WebAdresse = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.webadresse.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.WebAdresse IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.land", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Land = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.land.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Land IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schuljahr", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Schuljahr = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schuljahr.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.Schuljahr IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schuljahrabschnitt", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchuljahrAbschnitt = :value")
@NamedQuery(name="MigrationDTOEigeneSchule.schuljahrabschnitt.multiple", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.SchuljahrAbschnitt IN :value")
@NamedQuery(name="MigrationDTOEigeneSchule.primaryKeyQuery", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOEigeneSchule.all.migration", query="SELECT e FROM MigrationDTOEigeneSchule e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SchulformNr","Schulform","SchulformBez","SchultraegerArt","SchultraegerNr","SchulNr","Bezeichnung1","Bezeichnung2","Bezeichnung3","Strasse","Strassenname","HausNr","HausNrZusatz","PLZ","Ort","Telefon","Fax","Email","Ganztags","Schuljahresabschnitts_ID","AnzahlAbschnitte","AbschnittBez","BezAbschnitt1","BezAbschnitt2","BezAbschnitt3","BezAbschnitt4","Fremdsprachen","JVAZeigen","RefPaedagogikZeigen","AnzJGS_Jahr","IstHauptsitz","NotenGesperrt","ZurueckgestelltAnzahl","ZurueckgestelltWeibl","ZurueckgestelltAuslaender","ZurueckgestelltAuslaenderWeibl","ZurueckgestelltAussiedler","ZurueckgestelltAussiedlerWeibl","TeamTeaching","AbiGruppenprozess","DauerUnterrichtseinheit","Gruppen8Bis1","Gruppen13Plus","InternatsplaetzeM","InternatsplaetzeW","InternatsplaetzeNeutral","SchulLogo","SchulLogoBase64","SchulnrEigner","SchulleiterName","SchulleiterVorname","SchulleiterAmtsbez","SchulleiterGeschlecht","StvSchulleiterName","StvSchulleiterVorname","StvSchulleiterAmtsbez","StvSchulleiterGeschlecht","Einstellungen","WebAdresse","Land","Schuljahr","SchuljahrAbschnitt"})
public class MigrationDTOEigeneSchule {

	/** ID des Datensatzes der eigenen Schule */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schulformnummer der eigenen Schule (Statkue IT.NRW) */
	@Column(name = "SchulformNr")
	@JsonProperty
	public String SchulformNr;

	/** Schulformkürzel der eigenen Schule (Statkue IT.NRW) */
	@Column(name = "SchulformKrz")
	@JsonProperty
	public String Schulform;

	/** Bezeichnender Text der Schulform */
	@Column(name = "SchulformBez")
	@JsonProperty
	public String SchulformBez;

	/** Art des Schulträgers */
	@Column(name = "SchultraegerArt")
	@JsonProperty
	public String SchultraegerArt;

	/** Schulträgernummer aus dem Katalog der Schulver IT.NRW */
	@Column(name = "SchultraegerNr")
	@JsonProperty
	public String SchultraegerNr;

	/** Eindeutige Schulnummer der eigenen Schule aus der Schulver IT.NRW */
	@Column(name = "SchulNr")
	@JsonProperty
	@Convert(converter=MigrationStringToIntegerConverter.class)
	@JsonSerialize(using=MigrationStringToIntegerConverterSerializer.class)
	@JsonDeserialize(using=MigrationStringToIntegerConverterDeserializer.class)
	public Integer SchulNr;

	/** 1. Text für die Bezeichnung der Schule */
	@Column(name = "Bezeichnung1")
	@JsonProperty
	public String Bezeichnung1;

	/** 2. Text für die Bezeichnung der Schule */
	@Column(name = "Bezeichnung2")
	@JsonProperty
	public String Bezeichnung2;

	/** 3. Text für die Bezeichnung der Schule */
	@Column(name = "Bezeichnung3")
	@JsonProperty
	public String Bezeichnung3;

	/** Straße der eigenen Schule */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Straßenname der Schule */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnummerzusatz wenn getrennt gespeichert */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** PLZ der eigenen Schule */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ortsangabe der eigenen Schule */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Telefonnummer der eigenen Schule */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Faxnummer der eigenen Schule */
	@Column(name = "Fax")
	@JsonProperty
	public String Fax;

	/** Email-Adresse der eigenen Schule */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Gibt an, ob die Schule Ganztagsbetrieb hat */
	@Column(name = "Ganztags")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Ganztags;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Anzahl der verfügbaren Abschnitte (meist Halbjahre oder Quartale) */
	@Column(name = "AnzahlAbschnitte")
	@JsonProperty
	public Integer AnzahlAbschnitte;

	/** Bezeichnung der Abschnitte */
	@Column(name = "AbschnittBez")
	@JsonProperty
	public String AbschnittBez;

	/** Bezeichnung des ersten Abschnitts */
	@Column(name = "BezAbschnitt1")
	@JsonProperty
	public String BezAbschnitt1;

	/** Bezeichnung des zweiten Abschnitts */
	@Column(name = "BezAbschnitt2")
	@JsonProperty
	public String BezAbschnitt2;

	/** Bezeichnung des dritten Abschnitts */
	@Column(name = "BezAbschnitt3")
	@JsonProperty
	public String BezAbschnitt3;

	/** Bezeichnung des vierten Abschnitts */
	@Column(name = "BezAbschnitt4")
	@JsonProperty
	public String BezAbschnitt4;

	/** Welche Fremdsprachen werden unterrichtet */
	@Column(name = "Fremdsprachen")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Fremdsprachen;

	/** Schule unterrichtet in der Justizvollzugsanstalt */
	@Column(name = "JVAZeigen")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean JVAZeigen;

	/** Schule hat Reformpädagogischen-Zweig */
	@Column(name = "RefPaedagogikZeigen")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RefPaedagogikZeigen;

	/** Anzahl der Jahrgangstufen pro Schuljahr (Semesterbetrieb WBK) */
	@Column(name = "AnzJGS_Jahr")
	@JsonProperty
	public Integer AnzJGS_Jahr;

	/** Gibt an, ob die Datenbank am Hauptsitzder Schule ist (Dependancebetrieb) */
	@Column(name = "IstHauptsitz")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstHauptsitz;

	/** Sperrt die Noteneingabe */
	@Column(name = "NotenGesperrt")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NotenGesperrt;

	/** Anzahl de Zurückgestellten Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAnzahl")
	@JsonProperty
	public Integer ZurueckgestelltAnzahl;

	/** Anzahl de Zurückgestellten weiblichen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltWeibl")
	@JsonProperty
	public Integer ZurueckgestelltWeibl;

	/** Anzahl de Zurückgestellten ausländischen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAuslaender")
	@JsonProperty
	public Integer ZurueckgestelltAuslaender;

	/** Anzahl de Zurückgestellten ausländischen weiblichen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAuslaenderWeibl")
	@JsonProperty
	public Integer ZurueckgestelltAuslaenderWeibl;

	/** DEPRECATED: Anzahl de Zurückgestellten ausgesiedelten Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAussiedler")
	@JsonProperty
	public Integer ZurueckgestelltAussiedler;

	/** DEPRECATED: Anzahl de Zurückgestellten ausgesiedelten weiblichen Kinder in der Grundschule */
	@Column(name = "ZurueckgestelltAussiedlerWeibl")
	@JsonProperty
	public Integer ZurueckgestelltAussiedlerWeibl;

	/** Aktiviert das Teamteaching */
	@Column(name = "TeamTeaching")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean TeamTeaching;

	/** Sperrt oder erlaubt die Gruppenprozesse für das Abitur */
	@Column(name = "AbiGruppenprozess")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean AbiGruppenprozess;

	/** Dauer der Unterrichtseinheit (bei NULL = 45 Minuten) */
	@Column(name = "DauerUnterrichtseinheit")
	@JsonProperty
	public Integer DauerUnterrichtseinheit;

	/** Schule hat Betreuung 8 bis 13 */
	@Column(name = "Gruppen8Bis1")
	@JsonProperty
	public Integer Gruppen8Bis1;

	/** Gruppen in der 13Plus-Betreuung */
	@Column(name = "Gruppen13Plus")
	@JsonProperty
	public Integer Gruppen13Plus;

	/** Internatsplätze männlilch */
	@Column(name = "InternatsplaetzeM")
	@JsonProperty
	public Integer InternatsplaetzeM;

	/** Internatsplätze weiblich */
	@Column(name = "InternatsplaetzeW")
	@JsonProperty
	public Integer InternatsplaetzeW;

	/** Internatsplätze divers ohne Angabe */
	@Column(name = "InternatsplaetzeNeutral")
	@JsonProperty
	public Integer InternatsplaetzeNeutral;

	/** Schullogo als Bild im Binärformat */
	@Column(name = "SchulLogo")
	@JsonProperty
	public byte[] SchulLogo;

	/** Schullogo als Bild im Base64-Format */
	@Column(name = "SchulLogoBase64")
	@JsonProperty
	public String SchulLogoBase64;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Nachname der Schulleitung */
	@Column(name = "SchulleiterName")
	@JsonProperty
	public String SchulleiterName;

	/** Vorname der Schulleitung */
	@Column(name = "SchulleiterVorname")
	@JsonProperty
	public String SchulleiterVorname;

	/** Amtsbezeichnung der Schulleitung */
	@Column(name = "SchulleiterAmtsbez")
	@JsonProperty
	public String SchulleiterAmtsbez;

	/** Geschlecht der Schulleitung */
	@Column(name = "SchulleiterGeschlecht")
	@JsonProperty
	public Integer SchulleiterGeschlecht;

	/** Name der stellvertretenden Schulleitung */
	@Column(name = "StvSchulleiterName")
	@JsonProperty
	public String StvSchulleiterName;

	/** Vorname der stellvertretenden Schulleitung */
	@Column(name = "StvSchulleiterVorname")
	@JsonProperty
	public String StvSchulleiterVorname;

	/** Amtsbezeichnung der stellvertretenden Schulleitung */
	@Column(name = "StvSchulleiterAmtsbez")
	@JsonProperty
	public String StvSchulleiterAmtsbez;

	/** Geschlecht der stellvertretenden Schulleitung */
	@Column(name = "StvSchulleiterGeschlecht")
	@JsonProperty
	public Integer StvSchulleiterGeschlecht;

	/** DEPRECATED: Schild2 - Einstellungen zur Schule im INI-Format (kann in einem Texteditor gelesen werden). Wird in Schild 3 ausgelagert. */
	@Column(name = "Einstellungen")
	@JsonProperty
	public String Einstellungen;

	/** Enthält die Homepage-Adresse URL der Schule */
	@Column(name = "WebAdresse")
	@JsonProperty
	public String WebAdresse;

	/** Land der Schule ??? Wird wahrscheinlich für Bundeswehr verwendet? */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/** Aktuelle Schuljahr der Datenbank */
	@Column(name = "Schuljahr")
	@JsonProperty
	public Integer Schuljahr;

	/** Aktueller Abschnitt der Datenbank */
	@Column(name = "SchuljahrAbschnitt")
	@JsonProperty
	public Integer SchuljahrAbschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneSchule ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOEigeneSchule() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOEigeneSchule ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOEigeneSchule(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOEigeneSchule other = (MigrationDTOEigeneSchule) obj;
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
		return "MigrationDTOEigeneSchule(ID=" + this.ID + ", SchulformNr=" + this.SchulformNr + ", Schulform=" + this.Schulform + ", SchulformBez=" + this.SchulformBez + ", SchultraegerArt=" + this.SchultraegerArt + ", SchultraegerNr=" + this.SchultraegerNr + ", SchulNr=" + this.SchulNr + ", Bezeichnung1=" + this.Bezeichnung1 + ", Bezeichnung2=" + this.Bezeichnung2 + ", Bezeichnung3=" + this.Bezeichnung3 + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Telefon=" + this.Telefon + ", Fax=" + this.Fax + ", Email=" + this.Email + ", Ganztags=" + this.Ganztags + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", AnzahlAbschnitte=" + this.AnzahlAbschnitte + ", AbschnittBez=" + this.AbschnittBez + ", BezAbschnitt1=" + this.BezAbschnitt1 + ", BezAbschnitt2=" + this.BezAbschnitt2 + ", BezAbschnitt3=" + this.BezAbschnitt3 + ", BezAbschnitt4=" + this.BezAbschnitt4 + ", Fremdsprachen=" + this.Fremdsprachen + ", JVAZeigen=" + this.JVAZeigen + ", RefPaedagogikZeigen=" + this.RefPaedagogikZeigen + ", AnzJGS_Jahr=" + this.AnzJGS_Jahr + ", IstHauptsitz=" + this.IstHauptsitz + ", NotenGesperrt=" + this.NotenGesperrt + ", ZurueckgestelltAnzahl=" + this.ZurueckgestelltAnzahl + ", ZurueckgestelltWeibl=" + this.ZurueckgestelltWeibl + ", ZurueckgestelltAuslaender=" + this.ZurueckgestelltAuslaender + ", ZurueckgestelltAuslaenderWeibl=" + this.ZurueckgestelltAuslaenderWeibl + ", ZurueckgestelltAussiedler=" + this.ZurueckgestelltAussiedler + ", ZurueckgestelltAussiedlerWeibl=" + this.ZurueckgestelltAussiedlerWeibl + ", TeamTeaching=" + this.TeamTeaching + ", AbiGruppenprozess=" + this.AbiGruppenprozess + ", DauerUnterrichtseinheit=" + this.DauerUnterrichtseinheit + ", Gruppen8Bis1=" + this.Gruppen8Bis1 + ", Gruppen13Plus=" + this.Gruppen13Plus + ", InternatsplaetzeM=" + this.InternatsplaetzeM + ", InternatsplaetzeW=" + this.InternatsplaetzeW + ", InternatsplaetzeNeutral=" + this.InternatsplaetzeNeutral + ", SchulLogo=" + this.SchulLogo + ", SchulLogoBase64=" + this.SchulLogoBase64 + ", SchulnrEigner=" + this.SchulnrEigner + ", SchulleiterName=" + this.SchulleiterName + ", SchulleiterVorname=" + this.SchulleiterVorname + ", SchulleiterAmtsbez=" + this.SchulleiterAmtsbez + ", SchulleiterGeschlecht=" + this.SchulleiterGeschlecht + ", StvSchulleiterName=" + this.StvSchulleiterName + ", StvSchulleiterVorname=" + this.StvSchulleiterVorname + ", StvSchulleiterAmtsbez=" + this.StvSchulleiterAmtsbez + ", StvSchulleiterGeschlecht=" + this.StvSchulleiterGeschlecht + ", Einstellungen=" + this.Einstellungen + ", WebAdresse=" + this.WebAdresse + ", Land=" + this.Land + ", Schuljahr=" + this.Schuljahr + ", SchuljahrAbschnitt=" + this.SchuljahrAbschnitt + ")";
	}

}