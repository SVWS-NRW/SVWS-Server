import { JavaObject } from '../../../java/lang/JavaObject';

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchulenKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchulenKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchulenKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchulenKatalogEintrag();
		if (obj.SchulNr === undefined)
			 throw new Error('invalid json format, missing attribute SchulNr');
		result.SchulNr = obj.SchulNr;
		result.RegSchl = (obj.RegSchl === undefined) ? null : obj.RegSchl === null ? null : obj.RegSchl;
		if (obj.KoRe === undefined)
			 throw new Error('invalid json format, missing attribute KoRe');
		result.KoRe = obj.KoRe;
		if (obj.KoHo === undefined)
			 throw new Error('invalid json format, missing attribute KoHo');
		result.KoHo = obj.KoHo;
		result.ABez1 = (obj.ABez1 === undefined) ? null : obj.ABez1 === null ? null : obj.ABez1;
		result.ABez2 = (obj.ABez2 === undefined) ? null : obj.ABez2 === null ? null : obj.ABez2;
		result.ABez3 = (obj.ABez3 === undefined) ? null : obj.ABez3 === null ? null : obj.ABez3;
		result.PLZ = (obj.PLZ === undefined) ? null : obj.PLZ === null ? null : obj.PLZ;
		result.Ort = (obj.Ort === undefined) ? null : obj.Ort === null ? null : obj.Ort;
		result.Strasse = (obj.Strasse === undefined) ? null : obj.Strasse === null ? null : obj.Strasse;
		result.TelVorw = (obj.TelVorw === undefined) ? null : obj.TelVorw === null ? null : obj.TelVorw;
		result.Telefon = (obj.Telefon === undefined) ? null : obj.Telefon === null ? null : obj.Telefon;
		result.FaxVorw = (obj.FaxVorw === undefined) ? null : obj.FaxVorw === null ? null : obj.FaxVorw;
		result.Fax = (obj.Fax === undefined) ? null : obj.Fax === null ? null : obj.Fax;
		result.ModemVorw = (obj.ModemVorw === undefined) ? null : obj.ModemVorw === null ? null : obj.ModemVorw;
		result.Modem = (obj.Modem === undefined) ? null : obj.Modem === null ? null : obj.Modem;
		result.SF = (obj.SF === undefined) ? null : obj.SF === null ? null : obj.SF;
		result.OeffPri = (obj.OeffPri === undefined) ? null : obj.OeffPri === null ? null : obj.OeffPri;
		result.KurzBez = (obj.KurzBez === undefined) ? null : obj.KurzBez === null ? null : obj.KurzBez;
		result.SchBetrSchl = (obj.SchBetrSchl === undefined) ? null : obj.SchBetrSchl === null ? null : obj.SchBetrSchl;
		result.SchBetrSchlDatum = (obj.SchBetrSchlDatum === undefined) ? null : obj.SchBetrSchlDatum === null ? null : obj.SchBetrSchlDatum;
		result.ArtDerTraegerschaft = (obj.ArtDerTraegerschaft === undefined) ? null : obj.ArtDerTraegerschaft === null ? null : obj.ArtDerTraegerschaft;
		result.SchultraegerNr = (obj.SchultraegerNr === undefined) ? null : obj.SchultraegerNr === null ? null : obj.SchultraegerNr;
		result.Schulgliederung = (obj.Schulgliederung === undefined) ? null : obj.Schulgliederung === null ? null : obj.Schulgliederung;
		result.Schulart = (obj.Schulart === undefined) ? null : obj.Schulart === null ? null : obj.Schulart;
		result.Ganztagsbetrieb = (obj.Ganztagsbetrieb === undefined) ? null : obj.Ganztagsbetrieb === null ? null : obj.Ganztagsbetrieb;
		result.FSP = (obj.FSP === undefined) ? null : obj.FSP === null ? null : obj.FSP;
		result.Verbund = (obj.Verbund === undefined) ? null : obj.Verbund === null ? null : obj.Verbund;
		result.Bus = (obj.Bus === undefined) ? null : obj.Bus === null ? null : obj.Bus;
		result.Fachberater = (obj.Fachberater === undefined) ? null : obj.Fachberater === null ? null : obj.Fachberater;
		result.FachberHauptamtl = (obj.FachberHauptamtl === undefined) ? null : obj.FachberHauptamtl === null ? null : obj.FachberHauptamtl;
		result.TelNrDBSalt = (obj.TelNrDBSalt === undefined) ? null : obj.TelNrDBSalt === null ? null : obj.TelNrDBSalt;
		result.RP = (obj.RP === undefined) ? null : obj.RP === null ? null : obj.RP;
		result.Email = (obj.Email === undefined) ? null : obj.Email === null ? null : obj.Email;
		result.URL = (obj.URL === undefined) ? null : obj.URL === null ? null : obj.URL;
		result.Bemerkung = (obj.Bemerkung === undefined) ? null : obj.Bemerkung === null ? null : obj.Bemerkung;
		result.CD = (obj.CD === undefined) ? null : obj.CD === null ? null : obj.CD;
		result.Stift = (obj.Stift === undefined) ? null : obj.Stift === null ? null : obj.Stift;
		result.OGTS = (obj.OGTS === undefined) ? null : obj.OGTS === null ? null : obj.OGTS;
		result.SELB = (obj.SELB === undefined) ? null : obj.SELB === null ? null : obj.SELB;
		result.Internat = (obj.Internat === undefined) ? null : obj.Internat === null ? null : obj.Internat;
		result.InternatPlaetze = (obj.InternatPlaetze === undefined) ? null : obj.InternatPlaetze === null ? null : obj.InternatPlaetze;
		result.SMail = (obj.SMail === undefined) ? null : obj.SMail === null ? null : obj.SMail;
		result.SportImAbi = (obj.SportImAbi === undefined) ? null : obj.SportImAbi === null ? null : obj.SportImAbi;
		result.Tal = (obj.Tal === undefined) ? null : obj.Tal === null ? null : obj.Tal;
		result.KonKop = (obj.KonKop === undefined) ? null : obj.KonKop === null ? null : obj.KonKop;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : SchulenKatalogEintrag) : string {
		let result = '{';
		result += '"SchulNr" : ' + JSON.stringify(obj.SchulNr!) + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		result += '"KoRe" : ' + obj.KoRe + ',';
		result += '"KoHo" : ' + obj.KoHo + ',';
		result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : JSON.stringify(obj.ABez1)) + ',';
		result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : JSON.stringify(obj.ABez2)) + ',';
		result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : JSON.stringify(obj.ABez3)) + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : JSON.stringify(obj.PLZ)) + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : JSON.stringify(obj.Strasse)) + ',';
		result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : JSON.stringify(obj.TelVorw)) + ',';
		result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : JSON.stringify(obj.Telefon)) + ',';
		result += '"FaxVorw" : ' + ((!obj.FaxVorw) ? 'null' : JSON.stringify(obj.FaxVorw)) + ',';
		result += '"Fax" : ' + ((!obj.Fax) ? 'null' : JSON.stringify(obj.Fax)) + ',';
		result += '"ModemVorw" : ' + ((!obj.ModemVorw) ? 'null' : JSON.stringify(obj.ModemVorw)) + ',';
		result += '"Modem" : ' + ((!obj.Modem) ? 'null' : JSON.stringify(obj.Modem)) + ',';
		result += '"SF" : ' + ((!obj.SF) ? 'null' : JSON.stringify(obj.SF)) + ',';
		result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : JSON.stringify(obj.OeffPri)) + ',';
		result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : JSON.stringify(obj.KurzBez)) + ',';
		result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl) + ',';
		result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : JSON.stringify(obj.SchBetrSchlDatum)) + ',';
		result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : JSON.stringify(obj.ArtDerTraegerschaft)) + ',';
		result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : JSON.stringify(obj.SchultraegerNr)) + ',';
		result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : JSON.stringify(obj.Schulgliederung)) + ',';
		result += '"Schulart" : ' + ((!obj.Schulart) ? 'null' : JSON.stringify(obj.Schulart)) + ',';
		result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : JSON.stringify(obj.Ganztagsbetrieb)) + ',';
		result += '"FSP" : ' + ((!obj.FSP) ? 'null' : JSON.stringify(obj.FSP)) + ',';
		result += '"Verbund" : ' + ((!obj.Verbund) ? 'null' : JSON.stringify(obj.Verbund)) + ',';
		result += '"Bus" : ' + ((!obj.Bus) ? 'null' : JSON.stringify(obj.Bus)) + ',';
		result += '"Fachberater" : ' + ((!obj.Fachberater) ? 'null' : obj.Fachberater) + ',';
		result += '"FachberHauptamtl" : ' + ((!obj.FachberHauptamtl) ? 'null' : obj.FachberHauptamtl) + ',';
		result += '"TelNrDBSalt" : ' + ((!obj.TelNrDBSalt) ? 'null' : JSON.stringify(obj.TelNrDBSalt)) + ',';
		result += '"RP" : ' + ((!obj.RP) ? 'null' : JSON.stringify(obj.RP)) + ',';
		result += '"Email" : ' + ((!obj.Email) ? 'null' : JSON.stringify(obj.Email)) + ',';
		result += '"URL" : ' + ((!obj.URL) ? 'null' : JSON.stringify(obj.URL)) + ',';
		result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : JSON.stringify(obj.Bemerkung)) + ',';
		result += '"CD" : ' + ((!obj.CD) ? 'null' : obj.CD) + ',';
		result += '"Stift" : ' + ((!obj.Stift) ? 'null' : obj.Stift) + ',';
		result += '"OGTS" : ' + ((!obj.OGTS) ? 'null' : JSON.stringify(obj.OGTS)) + ',';
		result += '"SELB" : ' + ((!obj.SELB) ? 'null' : JSON.stringify(obj.SELB)) + ',';
		result += '"Internat" : ' + ((!obj.Internat) ? 'null' : JSON.stringify(obj.Internat)) + ',';
		result += '"InternatPlaetze" : ' + ((!obj.InternatPlaetze) ? 'null' : obj.InternatPlaetze) + ',';
		result += '"SMail" : ' + ((!obj.SMail) ? 'null' : JSON.stringify(obj.SMail)) + ',';
		result += '"SportImAbi" : ' + ((!obj.SportImAbi) ? 'null' : JSON.stringify(obj.SportImAbi)) + ',';
		result += '"Tal" : ' + ((!obj.Tal) ? 'null' : JSON.stringify(obj.Tal)) + ',';
		result += '"KonKop" : ' + ((!obj.KonKop) ? 'null' : JSON.stringify(obj.KonKop)) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulenKatalogEintrag>) : string {
		let result = '{';
		if (obj.SchulNr !== undefined) {
			result += '"SchulNr" : ' + JSON.stringify(obj.SchulNr!) + ',';
		}
		if (obj.RegSchl !== undefined) {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		}
		if (obj.KoRe !== undefined) {
			result += '"KoRe" : ' + obj.KoRe + ',';
		}
		if (obj.KoHo !== undefined) {
			result += '"KoHo" : ' + obj.KoHo + ',';
		}
		if (obj.ABez1 !== undefined) {
			result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : JSON.stringify(obj.ABez1)) + ',';
		}
		if (obj.ABez2 !== undefined) {
			result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : JSON.stringify(obj.ABez2)) + ',';
		}
		if (obj.ABez3 !== undefined) {
			result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : JSON.stringify(obj.ABez3)) + ',';
		}
		if (obj.PLZ !== undefined) {
			result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : JSON.stringify(obj.PLZ)) + ',';
		}
		if (obj.Ort !== undefined) {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		}
		if (obj.Strasse !== undefined) {
			result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : JSON.stringify(obj.Strasse)) + ',';
		}
		if (obj.TelVorw !== undefined) {
			result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : JSON.stringify(obj.TelVorw)) + ',';
		}
		if (obj.Telefon !== undefined) {
			result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : JSON.stringify(obj.Telefon)) + ',';
		}
		if (obj.FaxVorw !== undefined) {
			result += '"FaxVorw" : ' + ((!obj.FaxVorw) ? 'null' : JSON.stringify(obj.FaxVorw)) + ',';
		}
		if (obj.Fax !== undefined) {
			result += '"Fax" : ' + ((!obj.Fax) ? 'null' : JSON.stringify(obj.Fax)) + ',';
		}
		if (obj.ModemVorw !== undefined) {
			result += '"ModemVorw" : ' + ((!obj.ModemVorw) ? 'null' : JSON.stringify(obj.ModemVorw)) + ',';
		}
		if (obj.Modem !== undefined) {
			result += '"Modem" : ' + ((!obj.Modem) ? 'null' : JSON.stringify(obj.Modem)) + ',';
		}
		if (obj.SF !== undefined) {
			result += '"SF" : ' + ((!obj.SF) ? 'null' : JSON.stringify(obj.SF)) + ',';
		}
		if (obj.OeffPri !== undefined) {
			result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : JSON.stringify(obj.OeffPri)) + ',';
		}
		if (obj.KurzBez !== undefined) {
			result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : JSON.stringify(obj.KurzBez)) + ',';
		}
		if (obj.SchBetrSchl !== undefined) {
			result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl) + ',';
		}
		if (obj.SchBetrSchlDatum !== undefined) {
			result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : JSON.stringify(obj.SchBetrSchlDatum)) + ',';
		}
		if (obj.ArtDerTraegerschaft !== undefined) {
			result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : JSON.stringify(obj.ArtDerTraegerschaft)) + ',';
		}
		if (obj.SchultraegerNr !== undefined) {
			result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : JSON.stringify(obj.SchultraegerNr)) + ',';
		}
		if (obj.Schulgliederung !== undefined) {
			result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : JSON.stringify(obj.Schulgliederung)) + ',';
		}
		if (obj.Schulart !== undefined) {
			result += '"Schulart" : ' + ((!obj.Schulart) ? 'null' : JSON.stringify(obj.Schulart)) + ',';
		}
		if (obj.Ganztagsbetrieb !== undefined) {
			result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : JSON.stringify(obj.Ganztagsbetrieb)) + ',';
		}
		if (obj.FSP !== undefined) {
			result += '"FSP" : ' + ((!obj.FSP) ? 'null' : JSON.stringify(obj.FSP)) + ',';
		}
		if (obj.Verbund !== undefined) {
			result += '"Verbund" : ' + ((!obj.Verbund) ? 'null' : JSON.stringify(obj.Verbund)) + ',';
		}
		if (obj.Bus !== undefined) {
			result += '"Bus" : ' + ((!obj.Bus) ? 'null' : JSON.stringify(obj.Bus)) + ',';
		}
		if (obj.Fachberater !== undefined) {
			result += '"Fachberater" : ' + ((!obj.Fachberater) ? 'null' : obj.Fachberater) + ',';
		}
		if (obj.FachberHauptamtl !== undefined) {
			result += '"FachberHauptamtl" : ' + ((!obj.FachberHauptamtl) ? 'null' : obj.FachberHauptamtl) + ',';
		}
		if (obj.TelNrDBSalt !== undefined) {
			result += '"TelNrDBSalt" : ' + ((!obj.TelNrDBSalt) ? 'null' : JSON.stringify(obj.TelNrDBSalt)) + ',';
		}
		if (obj.RP !== undefined) {
			result += '"RP" : ' + ((!obj.RP) ? 'null' : JSON.stringify(obj.RP)) + ',';
		}
		if (obj.Email !== undefined) {
			result += '"Email" : ' + ((!obj.Email) ? 'null' : JSON.stringify(obj.Email)) + ',';
		}
		if (obj.URL !== undefined) {
			result += '"URL" : ' + ((!obj.URL) ? 'null' : JSON.stringify(obj.URL)) + ',';
		}
		if (obj.Bemerkung !== undefined) {
			result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : JSON.stringify(obj.Bemerkung)) + ',';
		}
		if (obj.CD !== undefined) {
			result += '"CD" : ' + ((!obj.CD) ? 'null' : obj.CD) + ',';
		}
		if (obj.Stift !== undefined) {
			result += '"Stift" : ' + ((!obj.Stift) ? 'null' : obj.Stift) + ',';
		}
		if (obj.OGTS !== undefined) {
			result += '"OGTS" : ' + ((!obj.OGTS) ? 'null' : JSON.stringify(obj.OGTS)) + ',';
		}
		if (obj.SELB !== undefined) {
			result += '"SELB" : ' + ((!obj.SELB) ? 'null' : JSON.stringify(obj.SELB)) + ',';
		}
		if (obj.Internat !== undefined) {
			result += '"Internat" : ' + ((!obj.Internat) ? 'null' : JSON.stringify(obj.Internat)) + ',';
		}
		if (obj.InternatPlaetze !== undefined) {
			result += '"InternatPlaetze" : ' + ((!obj.InternatPlaetze) ? 'null' : obj.InternatPlaetze) + ',';
		}
		if (obj.SMail !== undefined) {
			result += '"SMail" : ' + ((!obj.SMail) ? 'null' : JSON.stringify(obj.SMail)) + ',';
		}
		if (obj.SportImAbi !== undefined) {
			result += '"SportImAbi" : ' + ((!obj.SportImAbi) ? 'null' : JSON.stringify(obj.SportImAbi)) + ',';
		}
		if (obj.Tal !== undefined) {
			result += '"Tal" : ' + ((!obj.Tal) ? 'null' : JSON.stringify(obj.Tal)) + ',';
		}
		if (obj.KonKop !== undefined) {
			result += '"KonKop" : ' + ((!obj.KonKop) ? 'null' : JSON.stringify(obj.KonKop)) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchulenKatalogEintrag(obj : unknown) : SchulenKatalogEintrag {
	return obj as SchulenKatalogEintrag;
}
