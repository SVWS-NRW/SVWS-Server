package de.nrw.schule.svws.db.dto.rev9.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.StringToIntegerConverter;
import de.nrw.schule.svws.db.converter.current.statkue.SchulformKuerzelConverter;

import de.nrw.schule.svws.core.types.schule.Schulform;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.StringToIntegerConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.StringToIntegerConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.statkue.SchulformKuerzelConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.statkue.SchulformKuerzelConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule")
@NamedQuery(name="Rev9DTOEigeneSchule.all", query="SELECT e FROM Rev9DTOEigeneSchule e")
@NamedQuery(name="Rev9DTOEigeneSchule.id", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.id.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulformnr", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulformNr = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulformnr.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulformNr IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulform", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Schulform = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulform.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Schulform IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulformbez", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulformBez = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulformbez.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulformBez IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schultraegerart", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchultraegerArt = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schultraegerart.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchultraegerArt IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schultraegernr", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchultraegerNr = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schultraegernr.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchultraegerNr IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulnr", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulNr = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schulnr.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulNr IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezeichnung1", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Bezeichnung1 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezeichnung1.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Bezeichnung1 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezeichnung2", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Bezeichnung2 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezeichnung2.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Bezeichnung2 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezeichnung3", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Bezeichnung3 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezeichnung3.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Bezeichnung3 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.strassenname", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Strassenname = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.strassenname.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Strassenname IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.hausnr", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.HausNr = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.hausnr.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.HausNr IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.hausnrzusatz", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.hausnrzusatz.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.plz", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.PLZ = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.plz.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.PLZ IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.ort", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Ort = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.ort.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Ort IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.telefon", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Telefon = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.telefon.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Telefon IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.fax", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Fax = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.fax.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Fax IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.email", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Email = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.email.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Email IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.ganztags", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Ganztags = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.ganztags.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Ganztags IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schuljahresabschnitts_id", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schuljahresabschnitts_id.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.anzahlabschnitte", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AnzahlAbschnitte = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.anzahlabschnitte.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AnzahlAbschnitte IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.fremdsprachen", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Fremdsprachen = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.fremdsprachen.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Fremdsprachen IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.jvazeigen", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.JVAZeigen = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.jvazeigen.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.JVAZeigen IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.refpaedagogikzeigen", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.RefPaedagogikZeigen = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.refpaedagogikzeigen.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.RefPaedagogikZeigen IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.anzjgs_jahr", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AnzJGS_Jahr = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.anzjgs_jahr.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AnzJGS_Jahr IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.abschnittbez", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AbschnittBez = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.abschnittbez.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AbschnittBez IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt1", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt1 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt1.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt1 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt2", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt2 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt2.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt2 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.isthauptsitz", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.IstHauptsitz = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.isthauptsitz.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.IstHauptsitz IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.notengesperrt", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.NotenGesperrt = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.notengesperrt.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.NotenGesperrt IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt3", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt3 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt3.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt3 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt4", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt4 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.bezabschnitt4.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.BezAbschnitt4 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltanzahl", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAnzahl = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltanzahl.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAnzahl IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltweibl", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltWeibl = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltweibl.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltWeibl IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltauslaender", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaender = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltauslaender.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaender IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltauslaenderweibl", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaenderWeibl = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltauslaenderweibl.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAuslaenderWeibl IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltaussiedler", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedler = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltaussiedler.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedler IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltaussiedlerweibl", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedlerWeibl = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.zurueckgestelltaussiedlerweibl.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ZurueckgestelltAussiedlerWeibl IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.teamteaching", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.TeamTeaching = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.teamteaching.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.TeamTeaching IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.abigruppenprozess", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AbiGruppenprozess = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.abigruppenprozess.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.AbiGruppenprozess IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.dauerunterrichtseinheit", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.DauerUnterrichtseinheit = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.dauerunterrichtseinheit.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.DauerUnterrichtseinheit IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.gruppen8bis1", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Gruppen8Bis1 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.gruppen8bis1.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Gruppen8Bis1 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.gruppen13plus", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Gruppen13Plus = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.gruppen13plus.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Gruppen13Plus IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.internatsplaetzem", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.InternatsplaetzeM = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.internatsplaetzem.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.InternatsplaetzeM IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.internatsplaetzew", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.InternatsplaetzeW = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.internatsplaetzew.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.InternatsplaetzeW IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.internatsplaetzeneutral", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.InternatsplaetzeNeutral = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.internatsplaetzeneutral.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.InternatsplaetzeNeutral IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schullogobase64", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulLogoBase64 = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.schullogobase64.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.SchulLogoBase64 IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.einstellungen", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Einstellungen = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.einstellungen.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Einstellungen IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.webadresse", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.WebAdresse = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.webadresse.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.WebAdresse IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.land", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Land = :value")
@NamedQuery(name="Rev9DTOEigeneSchule.land.multiple", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.Land IN :value")
@NamedQuery(name="Rev9DTOEigeneSchule.primaryKeyQuery", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOEigeneSchule.all.migration", query="SELECT e FROM Rev9DTOEigeneSchule e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SchulformNr","Schulform","SchulformBez","SchultraegerArt","SchultraegerNr","SchulNr","Bezeichnung1","Bezeichnung2","Bezeichnung3","Strassenname","HausNr","HausNrZusatz","PLZ","Ort","Telefon","Fax","Email","Ganztags","Schuljahresabschnitts_ID","AnzahlAbschnitte","Fremdsprachen","JVAZeigen","RefPaedagogikZeigen","AnzJGS_Jahr","AbschnittBez","BezAbschnitt1","BezAbschnitt2","IstHauptsitz","NotenGesperrt","BezAbschnitt3","BezAbschnitt4","ZurueckgestelltAnzahl","ZurueckgestelltWeibl","ZurueckgestelltAuslaender","ZurueckgestelltAuslaenderWeibl","ZurueckgestelltAussiedler","ZurueckgestelltAussiedlerWeibl","TeamTeaching","AbiGruppenprozess","DauerUnterrichtseinheit","Gruppen8Bis1","Gruppen13Plus","InternatsplaetzeM","InternatsplaetzeW","InternatsplaetzeNeutral","SchulLogoBase64","Einstellungen","WebAdresse","Land"})
public class Rev9DTOEigeneSchule {

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
	@Convert(converter=SchulformKuerzelConverter.class)
	@JsonSerialize(using=SchulformKuerzelConverterSerializer.class)
	@JsonDeserialize(using=SchulformKuerzelConverterDeserializer.class)
	public Schulform Schulform;

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
	@Convert(converter=StringToIntegerConverter.class)
	@JsonSerialize(using=StringToIntegerConverterSerializer.class)
	@JsonDeserialize(using=StringToIntegerConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Ganztags;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Anzahl der verfügbaren Abschnitte (meist Halbjahre oder Quartale) */
	@Column(name = "AnzahlAbschnitte")
	@JsonProperty
	public Integer AnzahlAbschnitte;

	/** Welche Fremdsprachen werden unterrichtet */
	@Column(name = "Fremdsprachen")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Fremdsprachen;

	/** Schule unterrichtet in der Justizvollzugsanstalt */
	@Column(name = "JVAZeigen")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean JVAZeigen;

	/** Schule hat Reformpädagogischen-Zweig */
	@Column(name = "RefPaedagogikZeigen")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean RefPaedagogikZeigen;

	/** Anzahl der Jahrgangstufen pro Schuljahr (Semesterbetrieb WBK) */
	@Column(name = "AnzJGS_Jahr")
	@JsonProperty
	public Integer AnzJGS_Jahr;

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

	/** Gibt an, ob die Datenbank am Hauptsitzder Schule ist (Dependancebetrieb) */
	@Column(name = "IstHauptsitz")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstHauptsitz;

	/** Sperrt die Noteneingabe */
	@Column(name = "NotenGesperrt")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NotenGesperrt;

	/** Bezeichnung des dritten Abschnitts */
	@Column(name = "BezAbschnitt3")
	@JsonProperty
	public String BezAbschnitt3;

	/** Bezeichnung des vierten Abschnitts */
	@Column(name = "BezAbschnitt4")
	@JsonProperty
	public String BezAbschnitt4;

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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean TeamTeaching;

	/** Sperrt oder erlaubt die Gruppenprozesse für das Abitur */
	@Column(name = "AbiGruppenprozess")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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

	/** Schullogo als Bild im Base64-Format */
	@Column(name = "SchulLogoBase64")
	@JsonProperty
	public String SchulLogoBase64;

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

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOEigeneSchule ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOEigeneSchule() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOEigeneSchule ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev9DTOEigeneSchule(final Long ID) {
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
		Rev9DTOEigeneSchule other = (Rev9DTOEigeneSchule) obj;
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
		return "Rev9DTOEigeneSchule(ID=" + this.ID + ", SchulformNr=" + this.SchulformNr + ", Schulform=" + this.Schulform + ", SchulformBez=" + this.SchulformBez + ", SchultraegerArt=" + this.SchultraegerArt + ", SchultraegerNr=" + this.SchultraegerNr + ", SchulNr=" + this.SchulNr + ", Bezeichnung1=" + this.Bezeichnung1 + ", Bezeichnung2=" + this.Bezeichnung2 + ", Bezeichnung3=" + this.Bezeichnung3 + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Telefon=" + this.Telefon + ", Fax=" + this.Fax + ", Email=" + this.Email + ", Ganztags=" + this.Ganztags + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", AnzahlAbschnitte=" + this.AnzahlAbschnitte + ", Fremdsprachen=" + this.Fremdsprachen + ", JVAZeigen=" + this.JVAZeigen + ", RefPaedagogikZeigen=" + this.RefPaedagogikZeigen + ", AnzJGS_Jahr=" + this.AnzJGS_Jahr + ", AbschnittBez=" + this.AbschnittBez + ", BezAbschnitt1=" + this.BezAbschnitt1 + ", BezAbschnitt2=" + this.BezAbschnitt2 + ", IstHauptsitz=" + this.IstHauptsitz + ", NotenGesperrt=" + this.NotenGesperrt + ", BezAbschnitt3=" + this.BezAbschnitt3 + ", BezAbschnitt4=" + this.BezAbschnitt4 + ", ZurueckgestelltAnzahl=" + this.ZurueckgestelltAnzahl + ", ZurueckgestelltWeibl=" + this.ZurueckgestelltWeibl + ", ZurueckgestelltAuslaender=" + this.ZurueckgestelltAuslaender + ", ZurueckgestelltAuslaenderWeibl=" + this.ZurueckgestelltAuslaenderWeibl + ", ZurueckgestelltAussiedler=" + this.ZurueckgestelltAussiedler + ", ZurueckgestelltAussiedlerWeibl=" + this.ZurueckgestelltAussiedlerWeibl + ", TeamTeaching=" + this.TeamTeaching + ", AbiGruppenprozess=" + this.AbiGruppenprozess + ", DauerUnterrichtseinheit=" + this.DauerUnterrichtseinheit + ", Gruppen8Bis1=" + this.Gruppen8Bis1 + ", Gruppen13Plus=" + this.Gruppen13Plus + ", InternatsplaetzeM=" + this.InternatsplaetzeM + ", InternatsplaetzeW=" + this.InternatsplaetzeW + ", InternatsplaetzeNeutral=" + this.InternatsplaetzeNeutral + ", SchulLogoBase64=" + this.SchulLogoBase64 + ", Einstellungen=" + this.Einstellungen + ", WebAdresse=" + this.WebAdresse + ", Land=" + this.Land + ")";
	}

}