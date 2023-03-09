import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchulenKatalogEintrag extends JavaObject {

	/**
	 * Die Schulnummer. 
	 */
	public SchulNr : string = "";

	/**
	 * Regionalschlüssel der Schule 
	 */
	public RegSchl : string | null = null;

	/**
	 * Feld KoRe 
	 */
	public KoRe : number = 0;

	/**
	 * Feld KoHo 
	 */
	public KoHo : number = 0;

	/**
	 * Bezeichnung 1 der Schule 
	 */
	public ABez1 : string | null = null;

	/**
	 * Bezeichnung 2 der Schule 
	 */
	public ABez2 : string | null = null;

	/**
	 * Bezeichnung 3 der Schule 
	 */
	public ABez3 : string | null = null;

	/**
	 * Postleitzahl der Schule 
	 */
	public PLZ : string | null = null;

	/**
	 * Ort der Schule 
	 */
	public Ort : string | null = null;

	/**
	 * Straße der Schule 
	 */
	public Strasse : string | null = null;

	/**
	 * Telefonvorwahl der Schule 
	 */
	public TelVorw : string | null = null;

	/**
	 * Telefonnummer der Schule 
	 */
	public Telefon : string | null = null;

	/**
	 * Faxvorwahl der Schule 
	 */
	public FaxVorw : string | null = null;

	/**
	 * Faxnummer der Schule 
	 */
	public Fax : string | null = null;

	/**
	 * Modemvorwahl der Schule 
	 */
	public ModemVorw : string | null = null;

	/**
	 * Modem-Telefonnummer der Schule 
	 */
	public Modem : string | null = null;

	/**
	 * Schulform der Schule 
	 */
	public SF : string | null = null;

	/**
	 * OeffPri 
	 */
	public OeffPri : string | null = null;

	/**
	 * Kurzbezeichnung der Schule 
	 */
	public KurzBez : string | null = null;

	/**
	 * Schulbetriebsschlüssel der Schule 
	 */
	public SchBetrSchl : number | null = null;

	/**
	 * Datum des Schulbetriensschlüssels der Schule 
	 */
	public SchBetrSchlDatum : string | null = null;

	/**
	 * Art der Trägerschaft der Schule 
	 */
	public ArtDerTraegerschaft : string | null = null;

	/**
	 * Schulträgernummer der Schule 
	 */
	public SchultraegerNr : string | null = null;

	/**
	 * Schulgliederung der Schule 
	 */
	public Schulgliederung : string | null = null;

	/**
	 * Schulart 
	 */
	public Schulart : string | null = null;

	/**
	 * Gibt an ob die Schule Ganztagsbetrieb hat 
	 */
	public Ganztagsbetrieb : string | null = null;

	/**
	 * Förderschwerpunkte der Schule 
	 */
	public FSP : string | null = null;

	/**
	 * Verbund 
	 */
	public Verbund : string | null = null;

	/**
	 * Bus 
	 */
	public Bus : string | null = null;

	/**
	 * Fachberater der Schule 
	 */
	public Fachberater : number | null = null;

	/**
	 * FachberHauptamtl 
	 */
	public FachberHauptamtl : number | null = null;

	/**
	 * TelNrDBSalt 
	 */
	public TelNrDBSalt : string | null = null;

	/**
	 * RP 
	 */
	public RP : string | null = null;

	/**
	 * Email-Adresse der Schule 
	 */
	public Email : string | null = null;

	/**
	 * Website der Schule 
	 */
	public URL : string | null = null;

	/**
	 * Bemerkung zur Schule 
	 */
	public Bemerkung : string | null = null;

	/**
	 * Gibt an ob die Schule eine CD für ASDPC32 möchte 
	 */
	public CD : number | null = null;

	/**
	 * Stift 
	 */
	public Stift : number | null = null;

	/**
	 * Gibt an ob die Schule offenen Ganztag hat 
	 */
	public OGTS : string | null = null;

	/**
	 * SELB 
	 */
	public SELB : string | null = null;

	/**
	 * Gibt an ob die Schule Internatsplätze hat 
	 */
	public Internat : string | null = null;

	/**
	 * Anzahl der Internatsplätze 
	 */
	public InternatPlaetze : number | null = null;

	/**
	 * Schulmailadresse 
	 */
	public SMail : string | null = null;

	/**
	 * Hat die Schule Sport im Abitur? 
	 */
	public SportImAbi : string | null = null;

	/**
	 * Nimmt die Schule am Projekt Talentschule teil? 
	 */
	public Tal : string | null = null;

	/**
	 * Ist die konfessionelle Kooperation an dieser Schule genehmigt? 
	 */
	public KonKop : string | null = null;

	/**
	 * Gibt die Gültigkeit ab welchem Schuljahr an 
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt die Gültigkeit bis zu welchem Schuljahr an 
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchulenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchulenKatalogEintrag();
		if (typeof obj.SchulNr === "undefined")
			 throw new Error('invalid json format, missing attribute SchulNr');
		result.SchulNr = obj.SchulNr;
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl === null ? null : obj.RegSchl;
		if (typeof obj.KoRe === "undefined")
			 throw new Error('invalid json format, missing attribute KoRe');
		result.KoRe = obj.KoRe;
		if (typeof obj.KoHo === "undefined")
			 throw new Error('invalid json format, missing attribute KoHo');
		result.KoHo = obj.KoHo;
		result.ABez1 = typeof obj.ABez1 === "undefined" ? null : obj.ABez1 === null ? null : obj.ABez1;
		result.ABez2 = typeof obj.ABez2 === "undefined" ? null : obj.ABez2 === null ? null : obj.ABez2;
		result.ABez3 = typeof obj.ABez3 === "undefined" ? null : obj.ABez3 === null ? null : obj.ABez3;
		result.PLZ = typeof obj.PLZ === "undefined" ? null : obj.PLZ === null ? null : obj.PLZ;
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort === null ? null : obj.Ort;
		result.Strasse = typeof obj.Strasse === "undefined" ? null : obj.Strasse === null ? null : obj.Strasse;
		result.TelVorw = typeof obj.TelVorw === "undefined" ? null : obj.TelVorw === null ? null : obj.TelVorw;
		result.Telefon = typeof obj.Telefon === "undefined" ? null : obj.Telefon === null ? null : obj.Telefon;
		result.FaxVorw = typeof obj.FaxVorw === "undefined" ? null : obj.FaxVorw === null ? null : obj.FaxVorw;
		result.Fax = typeof obj.Fax === "undefined" ? null : obj.Fax === null ? null : obj.Fax;
		result.ModemVorw = typeof obj.ModemVorw === "undefined" ? null : obj.ModemVorw === null ? null : obj.ModemVorw;
		result.Modem = typeof obj.Modem === "undefined" ? null : obj.Modem === null ? null : obj.Modem;
		result.SF = typeof obj.SF === "undefined" ? null : obj.SF === null ? null : obj.SF;
		result.OeffPri = typeof obj.OeffPri === "undefined" ? null : obj.OeffPri === null ? null : obj.OeffPri;
		result.KurzBez = typeof obj.KurzBez === "undefined" ? null : obj.KurzBez === null ? null : obj.KurzBez;
		result.SchBetrSchl = typeof obj.SchBetrSchl === "undefined" ? null : obj.SchBetrSchl === null ? null : obj.SchBetrSchl;
		result.SchBetrSchlDatum = typeof obj.SchBetrSchlDatum === "undefined" ? null : obj.SchBetrSchlDatum === null ? null : obj.SchBetrSchlDatum;
		result.ArtDerTraegerschaft = typeof obj.ArtDerTraegerschaft === "undefined" ? null : obj.ArtDerTraegerschaft === null ? null : obj.ArtDerTraegerschaft;
		result.SchultraegerNr = typeof obj.SchultraegerNr === "undefined" ? null : obj.SchultraegerNr === null ? null : obj.SchultraegerNr;
		result.Schulgliederung = typeof obj.Schulgliederung === "undefined" ? null : obj.Schulgliederung === null ? null : obj.Schulgliederung;
		result.Schulart = typeof obj.Schulart === "undefined" ? null : obj.Schulart === null ? null : obj.Schulart;
		result.Ganztagsbetrieb = typeof obj.Ganztagsbetrieb === "undefined" ? null : obj.Ganztagsbetrieb === null ? null : obj.Ganztagsbetrieb;
		result.FSP = typeof obj.FSP === "undefined" ? null : obj.FSP === null ? null : obj.FSP;
		result.Verbund = typeof obj.Verbund === "undefined" ? null : obj.Verbund === null ? null : obj.Verbund;
		result.Bus = typeof obj.Bus === "undefined" ? null : obj.Bus === null ? null : obj.Bus;
		result.Fachberater = typeof obj.Fachberater === "undefined" ? null : obj.Fachberater === null ? null : obj.Fachberater;
		result.FachberHauptamtl = typeof obj.FachberHauptamtl === "undefined" ? null : obj.FachberHauptamtl === null ? null : obj.FachberHauptamtl;
		result.TelNrDBSalt = typeof obj.TelNrDBSalt === "undefined" ? null : obj.TelNrDBSalt === null ? null : obj.TelNrDBSalt;
		result.RP = typeof obj.RP === "undefined" ? null : obj.RP === null ? null : obj.RP;
		result.Email = typeof obj.Email === "undefined" ? null : obj.Email === null ? null : obj.Email;
		result.URL = typeof obj.URL === "undefined" ? null : obj.URL === null ? null : obj.URL;
		result.Bemerkung = typeof obj.Bemerkung === "undefined" ? null : obj.Bemerkung === null ? null : obj.Bemerkung;
		result.CD = typeof obj.CD === "undefined" ? null : obj.CD === null ? null : obj.CD;
		result.Stift = typeof obj.Stift === "undefined" ? null : obj.Stift === null ? null : obj.Stift;
		result.OGTS = typeof obj.OGTS === "undefined" ? null : obj.OGTS === null ? null : obj.OGTS;
		result.SELB = typeof obj.SELB === "undefined" ? null : obj.SELB === null ? null : obj.SELB;
		result.Internat = typeof obj.Internat === "undefined" ? null : obj.Internat === null ? null : obj.Internat;
		result.InternatPlaetze = typeof obj.InternatPlaetze === "undefined" ? null : obj.InternatPlaetze === null ? null : obj.InternatPlaetze;
		result.SMail = typeof obj.SMail === "undefined" ? null : obj.SMail === null ? null : obj.SMail;
		result.SportImAbi = typeof obj.SportImAbi === "undefined" ? null : obj.SportImAbi === null ? null : obj.SportImAbi;
		result.Tal = typeof obj.Tal === "undefined" ? null : obj.Tal === null ? null : obj.Tal;
		result.KonKop = typeof obj.KonKop === "undefined" ? null : obj.KonKop === null ? null : obj.KonKop;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : SchulenKatalogEintrag) : string {
		let result = '{';
		result += '"SchulNr" : ' + '"' + obj.SchulNr! + '"' + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl + '"') + ',';
		result += '"KoRe" : ' + obj.KoRe + ',';
		result += '"KoHo" : ' + obj.KoHo + ',';
		result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : '"' + obj.ABez1 + '"') + ',';
		result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : '"' + obj.ABez2 + '"') + ',';
		result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : '"' + obj.ABez3 + '"') + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ + '"') + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort + '"') + ',';
		result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse + '"') + ',';
		result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : '"' + obj.TelVorw + '"') + ',';
		result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : '"' + obj.Telefon + '"') + ',';
		result += '"FaxVorw" : ' + ((!obj.FaxVorw) ? 'null' : '"' + obj.FaxVorw + '"') + ',';
		result += '"Fax" : ' + ((!obj.Fax) ? 'null' : '"' + obj.Fax + '"') + ',';
		result += '"ModemVorw" : ' + ((!obj.ModemVorw) ? 'null' : '"' + obj.ModemVorw + '"') + ',';
		result += '"Modem" : ' + ((!obj.Modem) ? 'null' : '"' + obj.Modem + '"') + ',';
		result += '"SF" : ' + ((!obj.SF) ? 'null' : '"' + obj.SF + '"') + ',';
		result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : '"' + obj.OeffPri + '"') + ',';
		result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez + '"') + ',';
		result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl) + ',';
		result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : '"' + obj.SchBetrSchlDatum + '"') + ',';
		result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : '"' + obj.ArtDerTraegerschaft + '"') + ',';
		result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : '"' + obj.SchultraegerNr + '"') + ',';
		result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : '"' + obj.Schulgliederung + '"') + ',';
		result += '"Schulart" : ' + ((!obj.Schulart) ? 'null' : '"' + obj.Schulart + '"') + ',';
		result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb + '"') + ',';
		result += '"FSP" : ' + ((!obj.FSP) ? 'null' : '"' + obj.FSP + '"') + ',';
		result += '"Verbund" : ' + ((!obj.Verbund) ? 'null' : '"' + obj.Verbund + '"') + ',';
		result += '"Bus" : ' + ((!obj.Bus) ? 'null' : '"' + obj.Bus + '"') + ',';
		result += '"Fachberater" : ' + ((!obj.Fachberater) ? 'null' : obj.Fachberater) + ',';
		result += '"FachberHauptamtl" : ' + ((!obj.FachberHauptamtl) ? 'null' : obj.FachberHauptamtl) + ',';
		result += '"TelNrDBSalt" : ' + ((!obj.TelNrDBSalt) ? 'null' : '"' + obj.TelNrDBSalt + '"') + ',';
		result += '"RP" : ' + ((!obj.RP) ? 'null' : '"' + obj.RP + '"') + ',';
		result += '"Email" : ' + ((!obj.Email) ? 'null' : '"' + obj.Email + '"') + ',';
		result += '"URL" : ' + ((!obj.URL) ? 'null' : '"' + obj.URL + '"') + ',';
		result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : '"' + obj.Bemerkung + '"') + ',';
		result += '"CD" : ' + ((!obj.CD) ? 'null' : obj.CD) + ',';
		result += '"Stift" : ' + ((!obj.Stift) ? 'null' : obj.Stift) + ',';
		result += '"OGTS" : ' + ((!obj.OGTS) ? 'null' : '"' + obj.OGTS + '"') + ',';
		result += '"SELB" : ' + ((!obj.SELB) ? 'null' : '"' + obj.SELB + '"') + ',';
		result += '"Internat" : ' + ((!obj.Internat) ? 'null' : '"' + obj.Internat + '"') + ',';
		result += '"InternatPlaetze" : ' + ((!obj.InternatPlaetze) ? 'null' : obj.InternatPlaetze) + ',';
		result += '"SMail" : ' + ((!obj.SMail) ? 'null' : '"' + obj.SMail + '"') + ',';
		result += '"SportImAbi" : ' + ((!obj.SportImAbi) ? 'null' : '"' + obj.SportImAbi + '"') + ',';
		result += '"Tal" : ' + ((!obj.Tal) ? 'null' : '"' + obj.Tal + '"') + ',';
		result += '"KonKop" : ' + ((!obj.KonKop) ? 'null' : '"' + obj.KonKop + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.SchulNr !== "undefined") {
			result += '"SchulNr" : ' + '"' + obj.SchulNr + '"' + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl + '"') + ',';
		}
		if (typeof obj.KoRe !== "undefined") {
			result += '"KoRe" : ' + obj.KoRe + ',';
		}
		if (typeof obj.KoHo !== "undefined") {
			result += '"KoHo" : ' + obj.KoHo + ',';
		}
		if (typeof obj.ABez1 !== "undefined") {
			result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : '"' + obj.ABez1 + '"') + ',';
		}
		if (typeof obj.ABez2 !== "undefined") {
			result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : '"' + obj.ABez2 + '"') + ',';
		}
		if (typeof obj.ABez3 !== "undefined") {
			result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : '"' + obj.ABez3 + '"') + ',';
		}
		if (typeof obj.PLZ !== "undefined") {
			result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ + '"') + ',';
		}
		if (typeof obj.Ort !== "undefined") {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort + '"') + ',';
		}
		if (typeof obj.Strasse !== "undefined") {
			result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse + '"') + ',';
		}
		if (typeof obj.TelVorw !== "undefined") {
			result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : '"' + obj.TelVorw + '"') + ',';
		}
		if (typeof obj.Telefon !== "undefined") {
			result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : '"' + obj.Telefon + '"') + ',';
		}
		if (typeof obj.FaxVorw !== "undefined") {
			result += '"FaxVorw" : ' + ((!obj.FaxVorw) ? 'null' : '"' + obj.FaxVorw + '"') + ',';
		}
		if (typeof obj.Fax !== "undefined") {
			result += '"Fax" : ' + ((!obj.Fax) ? 'null' : '"' + obj.Fax + '"') + ',';
		}
		if (typeof obj.ModemVorw !== "undefined") {
			result += '"ModemVorw" : ' + ((!obj.ModemVorw) ? 'null' : '"' + obj.ModemVorw + '"') + ',';
		}
		if (typeof obj.Modem !== "undefined") {
			result += '"Modem" : ' + ((!obj.Modem) ? 'null' : '"' + obj.Modem + '"') + ',';
		}
		if (typeof obj.SF !== "undefined") {
			result += '"SF" : ' + ((!obj.SF) ? 'null' : '"' + obj.SF + '"') + ',';
		}
		if (typeof obj.OeffPri !== "undefined") {
			result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : '"' + obj.OeffPri + '"') + ',';
		}
		if (typeof obj.KurzBez !== "undefined") {
			result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez + '"') + ',';
		}
		if (typeof obj.SchBetrSchl !== "undefined") {
			result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl) + ',';
		}
		if (typeof obj.SchBetrSchlDatum !== "undefined") {
			result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : '"' + obj.SchBetrSchlDatum + '"') + ',';
		}
		if (typeof obj.ArtDerTraegerschaft !== "undefined") {
			result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : '"' + obj.ArtDerTraegerschaft + '"') + ',';
		}
		if (typeof obj.SchultraegerNr !== "undefined") {
			result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : '"' + obj.SchultraegerNr + '"') + ',';
		}
		if (typeof obj.Schulgliederung !== "undefined") {
			result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : '"' + obj.Schulgliederung + '"') + ',';
		}
		if (typeof obj.Schulart !== "undefined") {
			result += '"Schulart" : ' + ((!obj.Schulart) ? 'null' : '"' + obj.Schulart + '"') + ',';
		}
		if (typeof obj.Ganztagsbetrieb !== "undefined") {
			result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb + '"') + ',';
		}
		if (typeof obj.FSP !== "undefined") {
			result += '"FSP" : ' + ((!obj.FSP) ? 'null' : '"' + obj.FSP + '"') + ',';
		}
		if (typeof obj.Verbund !== "undefined") {
			result += '"Verbund" : ' + ((!obj.Verbund) ? 'null' : '"' + obj.Verbund + '"') + ',';
		}
		if (typeof obj.Bus !== "undefined") {
			result += '"Bus" : ' + ((!obj.Bus) ? 'null' : '"' + obj.Bus + '"') + ',';
		}
		if (typeof obj.Fachberater !== "undefined") {
			result += '"Fachberater" : ' + ((!obj.Fachberater) ? 'null' : obj.Fachberater) + ',';
		}
		if (typeof obj.FachberHauptamtl !== "undefined") {
			result += '"FachberHauptamtl" : ' + ((!obj.FachberHauptamtl) ? 'null' : obj.FachberHauptamtl) + ',';
		}
		if (typeof obj.TelNrDBSalt !== "undefined") {
			result += '"TelNrDBSalt" : ' + ((!obj.TelNrDBSalt) ? 'null' : '"' + obj.TelNrDBSalt + '"') + ',';
		}
		if (typeof obj.RP !== "undefined") {
			result += '"RP" : ' + ((!obj.RP) ? 'null' : '"' + obj.RP + '"') + ',';
		}
		if (typeof obj.Email !== "undefined") {
			result += '"Email" : ' + ((!obj.Email) ? 'null' : '"' + obj.Email + '"') + ',';
		}
		if (typeof obj.URL !== "undefined") {
			result += '"URL" : ' + ((!obj.URL) ? 'null' : '"' + obj.URL + '"') + ',';
		}
		if (typeof obj.Bemerkung !== "undefined") {
			result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : '"' + obj.Bemerkung + '"') + ',';
		}
		if (typeof obj.CD !== "undefined") {
			result += '"CD" : ' + ((!obj.CD) ? 'null' : obj.CD) + ',';
		}
		if (typeof obj.Stift !== "undefined") {
			result += '"Stift" : ' + ((!obj.Stift) ? 'null' : obj.Stift) + ',';
		}
		if (typeof obj.OGTS !== "undefined") {
			result += '"OGTS" : ' + ((!obj.OGTS) ? 'null' : '"' + obj.OGTS + '"') + ',';
		}
		if (typeof obj.SELB !== "undefined") {
			result += '"SELB" : ' + ((!obj.SELB) ? 'null' : '"' + obj.SELB + '"') + ',';
		}
		if (typeof obj.Internat !== "undefined") {
			result += '"Internat" : ' + ((!obj.Internat) ? 'null' : '"' + obj.Internat + '"') + ',';
		}
		if (typeof obj.InternatPlaetze !== "undefined") {
			result += '"InternatPlaetze" : ' + ((!obj.InternatPlaetze) ? 'null' : obj.InternatPlaetze) + ',';
		}
		if (typeof obj.SMail !== "undefined") {
			result += '"SMail" : ' + ((!obj.SMail) ? 'null' : '"' + obj.SMail + '"') + ',';
		}
		if (typeof obj.SportImAbi !== "undefined") {
			result += '"SportImAbi" : ' + ((!obj.SportImAbi) ? 'null' : '"' + obj.SportImAbi + '"') + ',';
		}
		if (typeof obj.Tal !== "undefined") {
			result += '"Tal" : ' + ((!obj.Tal) ? 'null' : '"' + obj.Tal + '"') + ',';
		}
		if (typeof obj.KonKop !== "undefined") {
			result += '"KonKop" : ' + ((!obj.KonKop) ? 'null' : '"' + obj.KonKop + '"') + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_SchulenKatalogEintrag(obj : unknown) : SchulenKatalogEintrag {
	return obj as SchulenKatalogEintrag;
}
