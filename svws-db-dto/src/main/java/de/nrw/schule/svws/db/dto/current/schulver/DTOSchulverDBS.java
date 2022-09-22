package de.nrw.schule.svws.db.dto.current.schulver;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.Boolean01StringConverter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01StringConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01StringConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulver_DBS.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulver_DBS")
@NamedQuery(name="DTOSchulverDBS.all", query="SELECT e FROM DTOSchulverDBS e")
@NamedQuery(name="DTOSchulverDBS.schulnr", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchulNr = :value")
@NamedQuery(name="DTOSchulverDBS.schulnr.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchulNr IN :value")
@NamedQuery(name="DTOSchulverDBS.regschl", query="SELECT e FROM DTOSchulverDBS e WHERE e.RegSchl = :value")
@NamedQuery(name="DTOSchulverDBS.regschl.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.RegSchl IN :value")
@NamedQuery(name="DTOSchulverDBS.kore", query="SELECT e FROM DTOSchulverDBS e WHERE e.KoRe = :value")
@NamedQuery(name="DTOSchulverDBS.kore.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.KoRe IN :value")
@NamedQuery(name="DTOSchulverDBS.koho", query="SELECT e FROM DTOSchulverDBS e WHERE e.KoHo = :value")
@NamedQuery(name="DTOSchulverDBS.koho.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.KoHo IN :value")
@NamedQuery(name="DTOSchulverDBS.abez1", query="SELECT e FROM DTOSchulverDBS e WHERE e.ABez1 = :value")
@NamedQuery(name="DTOSchulverDBS.abez1.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.ABez1 IN :value")
@NamedQuery(name="DTOSchulverDBS.abez2", query="SELECT e FROM DTOSchulverDBS e WHERE e.ABez2 = :value")
@NamedQuery(name="DTOSchulverDBS.abez2.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.ABez2 IN :value")
@NamedQuery(name="DTOSchulverDBS.abez3", query="SELECT e FROM DTOSchulverDBS e WHERE e.ABez3 = :value")
@NamedQuery(name="DTOSchulverDBS.abez3.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.ABez3 IN :value")
@NamedQuery(name="DTOSchulverDBS.plz", query="SELECT e FROM DTOSchulverDBS e WHERE e.PLZ = :value")
@NamedQuery(name="DTOSchulverDBS.plz.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.PLZ IN :value")
@NamedQuery(name="DTOSchulverDBS.ort", query="SELECT e FROM DTOSchulverDBS e WHERE e.Ort = :value")
@NamedQuery(name="DTOSchulverDBS.ort.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Ort IN :value")
@NamedQuery(name="DTOSchulverDBS.strasse", query="SELECT e FROM DTOSchulverDBS e WHERE e.Strasse = :value")
@NamedQuery(name="DTOSchulverDBS.strasse.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Strasse IN :value")
@NamedQuery(name="DTOSchulverDBS.telvorw", query="SELECT e FROM DTOSchulverDBS e WHERE e.TelVorw = :value")
@NamedQuery(name="DTOSchulverDBS.telvorw.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.TelVorw IN :value")
@NamedQuery(name="DTOSchulverDBS.telefon", query="SELECT e FROM DTOSchulverDBS e WHERE e.Telefon = :value")
@NamedQuery(name="DTOSchulverDBS.telefon.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Telefon IN :value")
@NamedQuery(name="DTOSchulverDBS.faxvorw", query="SELECT e FROM DTOSchulverDBS e WHERE e.FaxVorw = :value")
@NamedQuery(name="DTOSchulverDBS.faxvorw.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.FaxVorw IN :value")
@NamedQuery(name="DTOSchulverDBS.fax", query="SELECT e FROM DTOSchulverDBS e WHERE e.Fax = :value")
@NamedQuery(name="DTOSchulverDBS.fax.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Fax IN :value")
@NamedQuery(name="DTOSchulverDBS.modemvorw", query="SELECT e FROM DTOSchulverDBS e WHERE e.ModemVorw = :value")
@NamedQuery(name="DTOSchulverDBS.modemvorw.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.ModemVorw IN :value")
@NamedQuery(name="DTOSchulverDBS.modem", query="SELECT e FROM DTOSchulverDBS e WHERE e.Modem = :value")
@NamedQuery(name="DTOSchulverDBS.modem.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Modem IN :value")
@NamedQuery(name="DTOSchulverDBS.sf", query="SELECT e FROM DTOSchulverDBS e WHERE e.SF = :value")
@NamedQuery(name="DTOSchulverDBS.sf.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SF IN :value")
@NamedQuery(name="DTOSchulverDBS.oeffpri", query="SELECT e FROM DTOSchulverDBS e WHERE e.OeffPri = :value")
@NamedQuery(name="DTOSchulverDBS.oeffpri.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.OeffPri IN :value")
@NamedQuery(name="DTOSchulverDBS.kurzbez", query="SELECT e FROM DTOSchulverDBS e WHERE e.KurzBez = :value")
@NamedQuery(name="DTOSchulverDBS.kurzbez.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.KurzBez IN :value")
@NamedQuery(name="DTOSchulverDBS.schbetrschl", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchBetrSchl = :value")
@NamedQuery(name="DTOSchulverDBS.schbetrschl.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchBetrSchl IN :value")
@NamedQuery(name="DTOSchulverDBS.schbetrschldatum", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchBetrSchlDatum = :value")
@NamedQuery(name="DTOSchulverDBS.schbetrschldatum.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchBetrSchlDatum IN :value")
@NamedQuery(name="DTOSchulverDBS.artdertraegerschaft", query="SELECT e FROM DTOSchulverDBS e WHERE e.ArtDerTraegerschaft = :value")
@NamedQuery(name="DTOSchulverDBS.artdertraegerschaft.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.ArtDerTraegerschaft IN :value")
@NamedQuery(name="DTOSchulverDBS.schultraegernr", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchultraegerNr = :value")
@NamedQuery(name="DTOSchulverDBS.schultraegernr.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchultraegerNr IN :value")
@NamedQuery(name="DTOSchulverDBS.schulgliederung", query="SELECT e FROM DTOSchulverDBS e WHERE e.Schulgliederung = :value")
@NamedQuery(name="DTOSchulverDBS.schulgliederung.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Schulgliederung IN :value")
@NamedQuery(name="DTOSchulverDBS.schulart", query="SELECT e FROM DTOSchulverDBS e WHERE e.Schulart = :value")
@NamedQuery(name="DTOSchulverDBS.schulart.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Schulart IN :value")
@NamedQuery(name="DTOSchulverDBS.ganztagsbetrieb", query="SELECT e FROM DTOSchulverDBS e WHERE e.Ganztagsbetrieb = :value")
@NamedQuery(name="DTOSchulverDBS.ganztagsbetrieb.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Ganztagsbetrieb IN :value")
@NamedQuery(name="DTOSchulverDBS.fsp", query="SELECT e FROM DTOSchulverDBS e WHERE e.FSP = :value")
@NamedQuery(name="DTOSchulverDBS.fsp.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.FSP IN :value")
@NamedQuery(name="DTOSchulverDBS.verbund", query="SELECT e FROM DTOSchulverDBS e WHERE e.Verbund = :value")
@NamedQuery(name="DTOSchulverDBS.verbund.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Verbund IN :value")
@NamedQuery(name="DTOSchulverDBS.bus", query="SELECT e FROM DTOSchulverDBS e WHERE e.Bus = :value")
@NamedQuery(name="DTOSchulverDBS.bus.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Bus IN :value")
@NamedQuery(name="DTOSchulverDBS.fachberater", query="SELECT e FROM DTOSchulverDBS e WHERE e.Fachberater = :value")
@NamedQuery(name="DTOSchulverDBS.fachberater.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Fachberater IN :value")
@NamedQuery(name="DTOSchulverDBS.fachberhauptamtl", query="SELECT e FROM DTOSchulverDBS e WHERE e.FachberHauptamtl = :value")
@NamedQuery(name="DTOSchulverDBS.fachberhauptamtl.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.FachberHauptamtl IN :value")
@NamedQuery(name="DTOSchulverDBS.telnrdbsalt", query="SELECT e FROM DTOSchulverDBS e WHERE e.TelNrDBSalt = :value")
@NamedQuery(name="DTOSchulverDBS.telnrdbsalt.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.TelNrDBSalt IN :value")
@NamedQuery(name="DTOSchulverDBS.rp", query="SELECT e FROM DTOSchulverDBS e WHERE e.RP = :value")
@NamedQuery(name="DTOSchulverDBS.rp.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.RP IN :value")
@NamedQuery(name="DTOSchulverDBS.email", query="SELECT e FROM DTOSchulverDBS e WHERE e.Email = :value")
@NamedQuery(name="DTOSchulverDBS.email.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Email IN :value")
@NamedQuery(name="DTOSchulverDBS.url", query="SELECT e FROM DTOSchulverDBS e WHERE e.URL = :value")
@NamedQuery(name="DTOSchulverDBS.url.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.URL IN :value")
@NamedQuery(name="DTOSchulverDBS.bemerkung", query="SELECT e FROM DTOSchulverDBS e WHERE e.Bemerkung = :value")
@NamedQuery(name="DTOSchulverDBS.bemerkung.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Bemerkung IN :value")
@NamedQuery(name="DTOSchulverDBS.cd", query="SELECT e FROM DTOSchulverDBS e WHERE e.CD = :value")
@NamedQuery(name="DTOSchulverDBS.cd.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.CD IN :value")
@NamedQuery(name="DTOSchulverDBS.stift", query="SELECT e FROM DTOSchulverDBS e WHERE e.Stift = :value")
@NamedQuery(name="DTOSchulverDBS.stift.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Stift IN :value")
@NamedQuery(name="DTOSchulverDBS.ogts", query="SELECT e FROM DTOSchulverDBS e WHERE e.OGTS = :value")
@NamedQuery(name="DTOSchulverDBS.ogts.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.OGTS IN :value")
@NamedQuery(name="DTOSchulverDBS.selb", query="SELECT e FROM DTOSchulverDBS e WHERE e.SELB = :value")
@NamedQuery(name="DTOSchulverDBS.selb.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SELB IN :value")
@NamedQuery(name="DTOSchulverDBS.internat", query="SELECT e FROM DTOSchulverDBS e WHERE e.Internat = :value")
@NamedQuery(name="DTOSchulverDBS.internat.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Internat IN :value")
@NamedQuery(name="DTOSchulverDBS.internatplaetze", query="SELECT e FROM DTOSchulverDBS e WHERE e.InternatPlaetze = :value")
@NamedQuery(name="DTOSchulverDBS.internatplaetze.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.InternatPlaetze IN :value")
@NamedQuery(name="DTOSchulverDBS.smail", query="SELECT e FROM DTOSchulverDBS e WHERE e.SMail = :value")
@NamedQuery(name="DTOSchulverDBS.smail.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SMail IN :value")
@NamedQuery(name="DTOSchulverDBS.sportimabi", query="SELECT e FROM DTOSchulverDBS e WHERE e.SportImAbi = :value")
@NamedQuery(name="DTOSchulverDBS.sportimabi.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.SportImAbi IN :value")
@NamedQuery(name="DTOSchulverDBS.tal", query="SELECT e FROM DTOSchulverDBS e WHERE e.Tal = :value")
@NamedQuery(name="DTOSchulverDBS.tal.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.Tal IN :value")
@NamedQuery(name="DTOSchulverDBS.konkop", query="SELECT e FROM DTOSchulverDBS e WHERE e.KonKop = :value")
@NamedQuery(name="DTOSchulverDBS.konkop.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.KonKop IN :value")
@NamedQuery(name="DTOSchulverDBS.gueltigvon", query="SELECT e FROM DTOSchulverDBS e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOSchulverDBS.gueltigvon.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOSchulverDBS.gueltigbis", query="SELECT e FROM DTOSchulverDBS e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOSchulverDBS.gueltigbis.multiple", query="SELECT e FROM DTOSchulverDBS e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOSchulverDBS.primaryKeyQuery", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchulNr = ?1")
@NamedQuery(name="DTOSchulverDBS.all.migration", query="SELECT e FROM DTOSchulverDBS e WHERE e.SchulNr IS NOT NULL")
@JsonPropertyOrder({"SchulNr","RegSchl","KoRe","KoHo","ABez1","ABez2","ABez3","PLZ","Ort","Strasse","TelVorw","Telefon","FaxVorw","Fax","ModemVorw","Modem","SF","OeffPri","KurzBez","SchBetrSchl","SchBetrSchlDatum","ArtDerTraegerschaft","SchultraegerNr","Schulgliederung","Schulart","Ganztagsbetrieb","FSP","Verbund","Bus","Fachberater","FachberHauptamtl","TelNrDBSalt","RP","Email","URL","Bemerkung","CD","Stift","OGTS","SELB","Internat","InternatPlaetze","SMail","SportImAbi","Tal","KonKop","gueltigVon","gueltigBis"})
public class DTOSchulverDBS {

	/** Schulver Tabelle IT.NRW: Schulnummer der Schule */
	@Id
	@Column(name = "SchulNr")
	@JsonProperty
	public String SchulNr;

	/** Schulver Tabelle IT.NRW: Regionalschlüssel der Schule */
	@Column(name = "RegSchl")
	@JsonProperty
	public String RegSchl;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "KoRe")
	@JsonProperty
	public Double KoRe;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "KoHo")
	@JsonProperty
	public Double KoHo;

	/** Schulver Tabelle IT.NRW: Bezeichnung 1 der Schule */
	@Column(name = "ABez1")
	@JsonProperty
	public String ABez1;

	/** Schulver Tabelle IT.NRW: Bezeichnung 2 der Schule */
	@Column(name = "ABez2")
	@JsonProperty
	public String ABez2;

	/** Schulver Tabelle IT.NRW: Bezeichnung 3 der Schule */
	@Column(name = "ABez3")
	@JsonProperty
	public String ABez3;

	/** Schulver Tabelle IT.NRW: Postleitzahl der Schule */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Schulver Tabelle IT.NRW: Ort der Schule */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Schulver Tabelle IT.NRW: Straße der Schule */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Schulver Tabelle IT.NRW: Telefonvorwahl der Schule */
	@Column(name = "TelVorw")
	@JsonProperty
	public String TelVorw;

	/** Schulver Tabelle IT.NRW: Telefonnummer der Schule */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Schulver Tabelle IT.NRW: Faxvorwahl der Schule */
	@Column(name = "FaxVorw")
	@JsonProperty
	public String FaxVorw;

	/** Schulver Tabelle IT.NRW: Faxnummer der Schule */
	@Column(name = "Fax")
	@JsonProperty
	public String Fax;

	/** Schulver Tabelle IT.NRW: Modemvorwahl der Schule */
	@Column(name = "ModemVorw")
	@JsonProperty
	public String ModemVorw;

	/** Schulver Tabelle IT.NRW: Modem-Telefonnummer der Schule */
	@Column(name = "Modem")
	@JsonProperty
	public String Modem;

	/** Schulver Tabelle IT.NRW: Schulform der Schule */
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "OeffPri")
	@JsonProperty
	public String OeffPri;

	/** Schulver Tabelle IT.NRW: Kurzbezeichnung der Schule */
	@Column(name = "KurzBez")
	@JsonProperty
	public String KurzBez;

	/** Schulver Tabelle IT.NRW: Schulbetriebsschlüssel der Schule */
	@Column(name = "SchBetrSchl")
	@JsonProperty
	public Integer SchBetrSchl;

	/** Schulver Tabelle IT.NRW: Datum des Schulbetriensschlüssels der Schule */
	@Column(name = "SchBetrSchlDatum")
	@JsonProperty
	public String SchBetrSchlDatum;

	/** Schulver Tabelle IT.NRW: Art der Trägerschaft der Schule */
	@Column(name = "ArtDerTraegerschaft")
	@JsonProperty
	public String ArtDerTraegerschaft;

	/** Schulver Tabelle IT.NRW: Schulträgernummer der Schule */
	@Column(name = "SchultraegerNr")
	@JsonProperty
	public String SchultraegerNr;

	/** Schulver Tabelle IT.NRW: Schulgliederung der Schule ??? */
	@Column(name = "Schulgliederung")
	@JsonProperty
	public String Schulgliederung;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "Schulart")
	@JsonProperty
	public String Schulart;

	/** Schulver Tabelle IT.NRW: Gibt an ob die Schule Ganztagsbetrieb hat */
	@Column(name = "Ganztagsbetrieb")
	@JsonProperty
	public String Ganztagsbetrieb;

	/** Schulver Tabelle IT.NRW: Förderschwerpunkte der Schule */
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "Verbund")
	@JsonProperty
	public String Verbund;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "Bus")
	@JsonProperty
	public String Bus;

	/** Schulver Tabelle IT.NRW: Fachberater der Schule */
	@Column(name = "Fachberater")
	@JsonProperty
	public Integer Fachberater;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "FachberHauptamtl")
	@JsonProperty
	public Integer FachberHauptamtl;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "TelNrDBSalt")
	@JsonProperty
	public String TelNrDBSalt;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "RP")
	@JsonProperty
	public String RP;

	/** Schulver Tabelle IT.NRW: Email-Adresse der Schule */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Schulver Tabelle IT.NRW: Website der Schule */
	@Column(name = "URL")
	@JsonProperty
	public String URL;

	/** Schulver Tabelle IT.NRW: Bemerkung zur Schule */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Schulver Tabelle IT.NRW: Gibt an ob die Schule eine CD für ASDPC32 möchte */
	@Column(name = "CD")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean CD;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "Stift")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean Stift;

	/** Schulver Tabelle IT.NRW: Gibt an ob die Schule offenen Ganztag hat */
	@Column(name = "OGTS")
	@JsonProperty
	public String OGTS;

	/** Schulver Tabelle IT.NRW: ??? */
	@Column(name = "SELB")
	@JsonProperty
	public String SELB;

	/** Schulver Tabelle IT.NRW: Gibt an ob die Schule Internatsplätze hat */
	@Column(name = "Internat")
	@JsonProperty
	public String Internat;

	/** Schulver Tabelle IT.NRW: Anzahl der Internatsplätze */
	@Column(name = "InternatPlaetze")
	@JsonProperty
	public Integer InternatPlaetze;

	/** Schulver Tabelle IT.NRW: Schulmailadresse */
	@Column(name = "SMail")
	@JsonProperty
	public String SMail;

	/** Schulver Tabelle IT.NRW: Hat die Schule Sport im Abitur? */
	@Column(name = "SportImAbi")
	@JsonProperty
	@Convert(converter=Boolean01StringConverter.class)
	@JsonSerialize(using=Boolean01StringConverterSerializer.class)
	@JsonDeserialize(using=Boolean01StringConverterDeserializer.class)
	public Boolean SportImAbi;

	/** Schulver Tabelle IT.NRW: Nimmt die Schule am Projekt Talentschule teil? */
	@Column(name = "Tal")
	@JsonProperty
	@Convert(converter=Boolean01StringConverter.class)
	@JsonSerialize(using=Boolean01StringConverterSerializer.class)
	@JsonDeserialize(using=Boolean01StringConverterDeserializer.class)
	public Boolean Tal;

	/** Schulver Tabelle IT.NRW: Ist die konfessionelle Kooperation an dieser Schule genehmigt? */
	@Column(name = "KonKop")
	@JsonProperty
	@Convert(converter=Boolean01StringConverter.class)
	@JsonSerialize(using=Boolean01StringConverterSerializer.class)
	@JsonDeserialize(using=Boolean01StringConverterDeserializer.class)
	public Boolean KonKop;

	/** Schulver Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schulver Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulverDBS ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulverDBS() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulverDBS ohne eine Initialisierung der Attribute.
	 * @param SchulNr   der Wert für das Attribut SchulNr
	 * @param SportImAbi   der Wert für das Attribut SportImAbi
	 * @param Tal   der Wert für das Attribut Tal
	 * @param KonKop   der Wert für das Attribut KonKop
	 */
	public DTOSchulverDBS(final String SchulNr, final Boolean SportImAbi, final Boolean Tal, final Boolean KonKop) {
		if (SchulNr == null) { 
			throw new NullPointerException("SchulNr must not be null");
		}
		this.SchulNr = SchulNr;
		if (SportImAbi == null) { 
			throw new NullPointerException("SportImAbi must not be null");
		}
		this.SportImAbi = SportImAbi;
		if (Tal == null) { 
			throw new NullPointerException("Tal must not be null");
		}
		this.Tal = Tal;
		if (KonKop == null) { 
			throw new NullPointerException("KonKop must not be null");
		}
		this.KonKop = KonKop;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulverDBS other = (DTOSchulverDBS) obj;
		if (SchulNr == null) {
			if (other.SchulNr != null)
				return false;
		} else if (!SchulNr.equals(other.SchulNr))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SchulNr == null) ? 0 : SchulNr.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchulverDBS(SchulNr=" + this.SchulNr + ", RegSchl=" + this.RegSchl + ", KoRe=" + this.KoRe + ", KoHo=" + this.KoHo + ", ABez1=" + this.ABez1 + ", ABez2=" + this.ABez2 + ", ABez3=" + this.ABez3 + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strasse=" + this.Strasse + ", TelVorw=" + this.TelVorw + ", Telefon=" + this.Telefon + ", FaxVorw=" + this.FaxVorw + ", Fax=" + this.Fax + ", ModemVorw=" + this.ModemVorw + ", Modem=" + this.Modem + ", SF=" + this.SF + ", OeffPri=" + this.OeffPri + ", KurzBez=" + this.KurzBez + ", SchBetrSchl=" + this.SchBetrSchl + ", SchBetrSchlDatum=" + this.SchBetrSchlDatum + ", ArtDerTraegerschaft=" + this.ArtDerTraegerschaft + ", SchultraegerNr=" + this.SchultraegerNr + ", Schulgliederung=" + this.Schulgliederung + ", Schulart=" + this.Schulart + ", Ganztagsbetrieb=" + this.Ganztagsbetrieb + ", FSP=" + this.FSP + ", Verbund=" + this.Verbund + ", Bus=" + this.Bus + ", Fachberater=" + this.Fachberater + ", FachberHauptamtl=" + this.FachberHauptamtl + ", TelNrDBSalt=" + this.TelNrDBSalt + ", RP=" + this.RP + ", Email=" + this.Email + ", URL=" + this.URL + ", Bemerkung=" + this.Bemerkung + ", CD=" + this.CD + ", Stift=" + this.Stift + ", OGTS=" + this.OGTS + ", SELB=" + this.SELB + ", Internat=" + this.Internat + ", InternatPlaetze=" + this.InternatPlaetze + ", SMail=" + this.SMail + ", SportImAbi=" + this.SportImAbi + ", Tal=" + this.Tal + ", KonKop=" + this.KonKop + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}