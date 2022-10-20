import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchulenKatalogEintrag extends JavaObject {

	public SchulNr : String = "";

	public RegSchl : String | null = null;

	public KoRe : number = 0;

	public KoHo : number = 0;

	public ABez1 : String | null = null;

	public ABez2 : String | null = null;

	public ABez3 : String | null = null;

	public PLZ : String | null = null;

	public Ort : String | null = null;

	public Strasse : String | null = null;

	public TelVorw : String | null = null;

	public Telefon : String | null = null;

	public FaxVorw : String | null = null;

	public Fax : String | null = null;

	public ModemVorw : String | null = null;

	public Modem : String | null = null;

	public SF : String | null = null;

	public OeffPri : String | null = null;

	public KurzBez : String | null = null;

	public SchBetrSchl : Number | null = null;

	public SchBetrSchlDatum : String | null = null;

	public ArtDerTraegerschaft : String | null = null;

	public SchultraegerNr : String | null = null;

	public Schulgliederung : String | null = null;

	public Schulart : String | null = null;

	public Ganztagsbetrieb : String | null = null;

	public FSP : String | null = null;

	public Verbund : String | null = null;

	public Bus : String | null = null;

	public Fachberater : Number | null = null;

	public FachberHauptamtl : Number | null = null;

	public TelNrDBSalt : String | null = null;

	public RP : String | null = null;

	public Email : String | null = null;

	public URL : String | null = null;

	public Bemerkung : String | null = null;

	public CD : Number | null = null;

	public Stift : Number | null = null;

	public OGTS : String | null = null;

	public SELB : String | null = null;

	public Internat : String | null = null;

	public InternatPlaetze : Number | null = null;

	public SMail : String | null = null;

	public SportImAbi : String | null = null;

	public Tal : String | null = null;

	public KonKop : String | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


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
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl;
		if (typeof obj.KoRe === "undefined")
			 throw new Error('invalid json format, missing attribute KoRe');
		result.KoRe = obj.KoRe;
		if (typeof obj.KoHo === "undefined")
			 throw new Error('invalid json format, missing attribute KoHo');
		result.KoHo = obj.KoHo;
		result.ABez1 = typeof obj.ABez1 === "undefined" ? null : obj.ABez1;
		result.ABez2 = typeof obj.ABez2 === "undefined" ? null : obj.ABez2;
		result.ABez3 = typeof obj.ABez3 === "undefined" ? null : obj.ABez3;
		result.PLZ = typeof obj.PLZ === "undefined" ? null : obj.PLZ;
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort;
		result.Strasse = typeof obj.Strasse === "undefined" ? null : obj.Strasse;
		result.TelVorw = typeof obj.TelVorw === "undefined" ? null : obj.TelVorw;
		result.Telefon = typeof obj.Telefon === "undefined" ? null : obj.Telefon;
		result.FaxVorw = typeof obj.FaxVorw === "undefined" ? null : obj.FaxVorw;
		result.Fax = typeof obj.Fax === "undefined" ? null : obj.Fax;
		result.ModemVorw = typeof obj.ModemVorw === "undefined" ? null : obj.ModemVorw;
		result.Modem = typeof obj.Modem === "undefined" ? null : obj.Modem;
		result.SF = typeof obj.SF === "undefined" ? null : obj.SF;
		result.OeffPri = typeof obj.OeffPri === "undefined" ? null : obj.OeffPri;
		result.KurzBez = typeof obj.KurzBez === "undefined" ? null : obj.KurzBez;
		result.SchBetrSchl = typeof obj.SchBetrSchl === "undefined" ? null : obj.SchBetrSchl;
		result.SchBetrSchlDatum = typeof obj.SchBetrSchlDatum === "undefined" ? null : obj.SchBetrSchlDatum;
		result.ArtDerTraegerschaft = typeof obj.ArtDerTraegerschaft === "undefined" ? null : obj.ArtDerTraegerschaft;
		result.SchultraegerNr = typeof obj.SchultraegerNr === "undefined" ? null : obj.SchultraegerNr;
		result.Schulgliederung = typeof obj.Schulgliederung === "undefined" ? null : obj.Schulgliederung;
		result.Schulart = typeof obj.Schulart === "undefined" ? null : obj.Schulart;
		result.Ganztagsbetrieb = typeof obj.Ganztagsbetrieb === "undefined" ? null : obj.Ganztagsbetrieb;
		result.FSP = typeof obj.FSP === "undefined" ? null : obj.FSP;
		result.Verbund = typeof obj.Verbund === "undefined" ? null : obj.Verbund;
		result.Bus = typeof obj.Bus === "undefined" ? null : obj.Bus;
		result.Fachberater = typeof obj.Fachberater === "undefined" ? null : obj.Fachberater;
		result.FachberHauptamtl = typeof obj.FachberHauptamtl === "undefined" ? null : obj.FachberHauptamtl;
		result.TelNrDBSalt = typeof obj.TelNrDBSalt === "undefined" ? null : obj.TelNrDBSalt;
		result.RP = typeof obj.RP === "undefined" ? null : obj.RP;
		result.Email = typeof obj.Email === "undefined" ? null : obj.Email;
		result.URL = typeof obj.URL === "undefined" ? null : obj.URL;
		result.Bemerkung = typeof obj.Bemerkung === "undefined" ? null : obj.Bemerkung;
		result.CD = typeof obj.CD === "undefined" ? null : obj.CD;
		result.Stift = typeof obj.Stift === "undefined" ? null : obj.Stift;
		result.OGTS = typeof obj.OGTS === "undefined" ? null : obj.OGTS;
		result.SELB = typeof obj.SELB === "undefined" ? null : obj.SELB;
		result.Internat = typeof obj.Internat === "undefined" ? null : obj.Internat;
		result.InternatPlaetze = typeof obj.InternatPlaetze === "undefined" ? null : obj.InternatPlaetze;
		result.SMail = typeof obj.SMail === "undefined" ? null : obj.SMail;
		result.SportImAbi = typeof obj.SportImAbi === "undefined" ? null : obj.SportImAbi;
		result.Tal = typeof obj.Tal === "undefined" ? null : obj.Tal;
		result.KonKop = typeof obj.KonKop === "undefined" ? null : obj.KonKop;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : SchulenKatalogEintrag) : string {
		let result = '{';
		result += '"SchulNr" : ' + '"' + obj.SchulNr.valueOf() + '"' + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		result += '"KoRe" : ' + obj.KoRe + ',';
		result += '"KoHo" : ' + obj.KoHo + ',';
		result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : '"' + obj.ABez1.valueOf() + '"') + ',';
		result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : '"' + obj.ABez2.valueOf() + '"') + ',';
		result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : '"' + obj.ABez3.valueOf() + '"') + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ.valueOf() + '"') + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse.valueOf() + '"') + ',';
		result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : '"' + obj.TelVorw.valueOf() + '"') + ',';
		result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : '"' + obj.Telefon.valueOf() + '"') + ',';
		result += '"FaxVorw" : ' + ((!obj.FaxVorw) ? 'null' : '"' + obj.FaxVorw.valueOf() + '"') + ',';
		result += '"Fax" : ' + ((!obj.Fax) ? 'null' : '"' + obj.Fax.valueOf() + '"') + ',';
		result += '"ModemVorw" : ' + ((!obj.ModemVorw) ? 'null' : '"' + obj.ModemVorw.valueOf() + '"') + ',';
		result += '"Modem" : ' + ((!obj.Modem) ? 'null' : '"' + obj.Modem.valueOf() + '"') + ',';
		result += '"SF" : ' + ((!obj.SF) ? 'null' : '"' + obj.SF.valueOf() + '"') + ',';
		result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : '"' + obj.OeffPri.valueOf() + '"') + ',';
		result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez.valueOf() + '"') + ',';
		result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl.valueOf()) + ',';
		result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : '"' + obj.SchBetrSchlDatum.valueOf() + '"') + ',';
		result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : '"' + obj.ArtDerTraegerschaft.valueOf() + '"') + ',';
		result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : '"' + obj.SchultraegerNr.valueOf() + '"') + ',';
		result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : '"' + obj.Schulgliederung.valueOf() + '"') + ',';
		result += '"Schulart" : ' + ((!obj.Schulart) ? 'null' : '"' + obj.Schulart.valueOf() + '"') + ',';
		result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb.valueOf() + '"') + ',';
		result += '"FSP" : ' + ((!obj.FSP) ? 'null' : '"' + obj.FSP.valueOf() + '"') + ',';
		result += '"Verbund" : ' + ((!obj.Verbund) ? 'null' : '"' + obj.Verbund.valueOf() + '"') + ',';
		result += '"Bus" : ' + ((!obj.Bus) ? 'null' : '"' + obj.Bus.valueOf() + '"') + ',';
		result += '"Fachberater" : ' + ((!obj.Fachberater) ? 'null' : obj.Fachberater.valueOf()) + ',';
		result += '"FachberHauptamtl" : ' + ((!obj.FachberHauptamtl) ? 'null' : obj.FachberHauptamtl.valueOf()) + ',';
		result += '"TelNrDBSalt" : ' + ((!obj.TelNrDBSalt) ? 'null' : '"' + obj.TelNrDBSalt.valueOf() + '"') + ',';
		result += '"RP" : ' + ((!obj.RP) ? 'null' : '"' + obj.RP.valueOf() + '"') + ',';
		result += '"Email" : ' + ((!obj.Email) ? 'null' : '"' + obj.Email.valueOf() + '"') + ',';
		result += '"URL" : ' + ((!obj.URL) ? 'null' : '"' + obj.URL.valueOf() + '"') + ',';
		result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : '"' + obj.Bemerkung.valueOf() + '"') + ',';
		result += '"CD" : ' + ((!obj.CD) ? 'null' : obj.CD.valueOf()) + ',';
		result += '"Stift" : ' + ((!obj.Stift) ? 'null' : obj.Stift.valueOf()) + ',';
		result += '"OGTS" : ' + ((!obj.OGTS) ? 'null' : '"' + obj.OGTS.valueOf() + '"') + ',';
		result += '"SELB" : ' + ((!obj.SELB) ? 'null' : '"' + obj.SELB.valueOf() + '"') + ',';
		result += '"Internat" : ' + ((!obj.Internat) ? 'null' : '"' + obj.Internat.valueOf() + '"') + ',';
		result += '"InternatPlaetze" : ' + ((!obj.InternatPlaetze) ? 'null' : obj.InternatPlaetze.valueOf()) + ',';
		result += '"SMail" : ' + ((!obj.SMail) ? 'null' : '"' + obj.SMail.valueOf() + '"') + ',';
		result += '"SportImAbi" : ' + ((!obj.SportImAbi) ? 'null' : '"' + obj.SportImAbi.valueOf() + '"') + ',';
		result += '"Tal" : ' + ((!obj.Tal) ? 'null' : '"' + obj.Tal.valueOf() + '"') + ',';
		result += '"KonKop" : ' + ((!obj.KonKop) ? 'null' : '"' + obj.KonKop.valueOf() + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchulenKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.SchulNr !== "undefined") {
			result += '"SchulNr" : ' + '"' + obj.SchulNr.valueOf() + '"' + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		}
		if (typeof obj.KoRe !== "undefined") {
			result += '"KoRe" : ' + obj.KoRe + ',';
		}
		if (typeof obj.KoHo !== "undefined") {
			result += '"KoHo" : ' + obj.KoHo + ',';
		}
		if (typeof obj.ABez1 !== "undefined") {
			result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : '"' + obj.ABez1.valueOf() + '"') + ',';
		}
		if (typeof obj.ABez2 !== "undefined") {
			result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : '"' + obj.ABez2.valueOf() + '"') + ',';
		}
		if (typeof obj.ABez3 !== "undefined") {
			result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : '"' + obj.ABez3.valueOf() + '"') + ',';
		}
		if (typeof obj.PLZ !== "undefined") {
			result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ.valueOf() + '"') + ',';
		}
		if (typeof obj.Ort !== "undefined") {
			result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		}
		if (typeof obj.Strasse !== "undefined") {
			result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse.valueOf() + '"') + ',';
		}
		if (typeof obj.TelVorw !== "undefined") {
			result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : '"' + obj.TelVorw.valueOf() + '"') + ',';
		}
		if (typeof obj.Telefon !== "undefined") {
			result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : '"' + obj.Telefon.valueOf() + '"') + ',';
		}
		if (typeof obj.FaxVorw !== "undefined") {
			result += '"FaxVorw" : ' + ((!obj.FaxVorw) ? 'null' : '"' + obj.FaxVorw.valueOf() + '"') + ',';
		}
		if (typeof obj.Fax !== "undefined") {
			result += '"Fax" : ' + ((!obj.Fax) ? 'null' : '"' + obj.Fax.valueOf() + '"') + ',';
		}
		if (typeof obj.ModemVorw !== "undefined") {
			result += '"ModemVorw" : ' + ((!obj.ModemVorw) ? 'null' : '"' + obj.ModemVorw.valueOf() + '"') + ',';
		}
		if (typeof obj.Modem !== "undefined") {
			result += '"Modem" : ' + ((!obj.Modem) ? 'null' : '"' + obj.Modem.valueOf() + '"') + ',';
		}
		if (typeof obj.SF !== "undefined") {
			result += '"SF" : ' + ((!obj.SF) ? 'null' : '"' + obj.SF.valueOf() + '"') + ',';
		}
		if (typeof obj.OeffPri !== "undefined") {
			result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : '"' + obj.OeffPri.valueOf() + '"') + ',';
		}
		if (typeof obj.KurzBez !== "undefined") {
			result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez.valueOf() + '"') + ',';
		}
		if (typeof obj.SchBetrSchl !== "undefined") {
			result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl.valueOf()) + ',';
		}
		if (typeof obj.SchBetrSchlDatum !== "undefined") {
			result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : '"' + obj.SchBetrSchlDatum.valueOf() + '"') + ',';
		}
		if (typeof obj.ArtDerTraegerschaft !== "undefined") {
			result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : '"' + obj.ArtDerTraegerschaft.valueOf() + '"') + ',';
		}
		if (typeof obj.SchultraegerNr !== "undefined") {
			result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : '"' + obj.SchultraegerNr.valueOf() + '"') + ',';
		}
		if (typeof obj.Schulgliederung !== "undefined") {
			result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : '"' + obj.Schulgliederung.valueOf() + '"') + ',';
		}
		if (typeof obj.Schulart !== "undefined") {
			result += '"Schulart" : ' + ((!obj.Schulart) ? 'null' : '"' + obj.Schulart.valueOf() + '"') + ',';
		}
		if (typeof obj.Ganztagsbetrieb !== "undefined") {
			result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb.valueOf() + '"') + ',';
		}
		if (typeof obj.FSP !== "undefined") {
			result += '"FSP" : ' + ((!obj.FSP) ? 'null' : '"' + obj.FSP.valueOf() + '"') + ',';
		}
		if (typeof obj.Verbund !== "undefined") {
			result += '"Verbund" : ' + ((!obj.Verbund) ? 'null' : '"' + obj.Verbund.valueOf() + '"') + ',';
		}
		if (typeof obj.Bus !== "undefined") {
			result += '"Bus" : ' + ((!obj.Bus) ? 'null' : '"' + obj.Bus.valueOf() + '"') + ',';
		}
		if (typeof obj.Fachberater !== "undefined") {
			result += '"Fachberater" : ' + ((!obj.Fachberater) ? 'null' : obj.Fachberater.valueOf()) + ',';
		}
		if (typeof obj.FachberHauptamtl !== "undefined") {
			result += '"FachberHauptamtl" : ' + ((!obj.FachberHauptamtl) ? 'null' : obj.FachberHauptamtl.valueOf()) + ',';
		}
		if (typeof obj.TelNrDBSalt !== "undefined") {
			result += '"TelNrDBSalt" : ' + ((!obj.TelNrDBSalt) ? 'null' : '"' + obj.TelNrDBSalt.valueOf() + '"') + ',';
		}
		if (typeof obj.RP !== "undefined") {
			result += '"RP" : ' + ((!obj.RP) ? 'null' : '"' + obj.RP.valueOf() + '"') + ',';
		}
		if (typeof obj.Email !== "undefined") {
			result += '"Email" : ' + ((!obj.Email) ? 'null' : '"' + obj.Email.valueOf() + '"') + ',';
		}
		if (typeof obj.URL !== "undefined") {
			result += '"URL" : ' + ((!obj.URL) ? 'null' : '"' + obj.URL.valueOf() + '"') + ',';
		}
		if (typeof obj.Bemerkung !== "undefined") {
			result += '"Bemerkung" : ' + ((!obj.Bemerkung) ? 'null' : '"' + obj.Bemerkung.valueOf() + '"') + ',';
		}
		if (typeof obj.CD !== "undefined") {
			result += '"CD" : ' + ((!obj.CD) ? 'null' : obj.CD.valueOf()) + ',';
		}
		if (typeof obj.Stift !== "undefined") {
			result += '"Stift" : ' + ((!obj.Stift) ? 'null' : obj.Stift.valueOf()) + ',';
		}
		if (typeof obj.OGTS !== "undefined") {
			result += '"OGTS" : ' + ((!obj.OGTS) ? 'null' : '"' + obj.OGTS.valueOf() + '"') + ',';
		}
		if (typeof obj.SELB !== "undefined") {
			result += '"SELB" : ' + ((!obj.SELB) ? 'null' : '"' + obj.SELB.valueOf() + '"') + ',';
		}
		if (typeof obj.Internat !== "undefined") {
			result += '"Internat" : ' + ((!obj.Internat) ? 'null' : '"' + obj.Internat.valueOf() + '"') + ',';
		}
		if (typeof obj.InternatPlaetze !== "undefined") {
			result += '"InternatPlaetze" : ' + ((!obj.InternatPlaetze) ? 'null' : obj.InternatPlaetze.valueOf()) + ',';
		}
		if (typeof obj.SMail !== "undefined") {
			result += '"SMail" : ' + ((!obj.SMail) ? 'null' : '"' + obj.SMail.valueOf() + '"') + ',';
		}
		if (typeof obj.SportImAbi !== "undefined") {
			result += '"SportImAbi" : ' + ((!obj.SportImAbi) ? 'null' : '"' + obj.SportImAbi.valueOf() + '"') + ',';
		}
		if (typeof obj.Tal !== "undefined") {
			result += '"Tal" : ' + ((!obj.Tal) ? 'null' : '"' + obj.Tal.valueOf() + '"') + ',';
		}
		if (typeof obj.KonKop !== "undefined") {
			result += '"KonKop" : ' + ((!obj.KonKop) ? 'null' : '"' + obj.KonKop.valueOf() + '"') + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_SchulenKatalogEintrag(obj : unknown) : SchulenKatalogEintrag {
	return obj as SchulenKatalogEintrag;
}
