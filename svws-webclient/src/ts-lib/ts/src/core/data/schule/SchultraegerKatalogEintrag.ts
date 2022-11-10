import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class SchultraegerKatalogEintrag extends JavaObject {

	public SchulNr : String = "";

	public RegSchl : String | null = null;

	public KoRe : String | null = null;

	public KoHo : String | null = null;

	public ABez1 : String | null = null;

	public ABez2 : String | null = null;

	public ABez3 : String | null = null;

	public PLZ : String | null = null;

	public Ort : String | null = null;

	public Strasse : String | null = null;

	public TelVorw : String | null = null;

	public Telefon : String | null = null;

	public SF : String | null = null;

	public OeffPri : String | null = null;

	public KurzBez : String | null = null;

	public SchBetrSchl : Number | null = null;

	public SchBetrSchlDatum : String | null = null;

	public SchuelerZahlASD : Number | null = null;

	public SchuelerZahlVS : Number | null = null;

	public ArtDerTraegerschaft : String | null = null;

	public SchultraegerNr : String | null = null;

	public Schulgliederung : String | null = null;

	public Ganztagsbetrieb : String | null = null;

	public Aktiv : Number | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.SchultraegerKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchultraegerKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchultraegerKatalogEintrag();
		if (typeof obj.SchulNr === "undefined")
			 throw new Error('invalid json format, missing attribute SchulNr');
		result.SchulNr = String(obj.SchulNr);
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl === null ? null : String(obj.RegSchl);
		result.KoRe = typeof obj.KoRe === "undefined" ? null : obj.KoRe === null ? null : String(obj.KoRe);
		result.KoHo = typeof obj.KoHo === "undefined" ? null : obj.KoHo === null ? null : String(obj.KoHo);
		result.ABez1 = typeof obj.ABez1 === "undefined" ? null : obj.ABez1 === null ? null : String(obj.ABez1);
		result.ABez2 = typeof obj.ABez2 === "undefined" ? null : obj.ABez2 === null ? null : String(obj.ABez2);
		result.ABez3 = typeof obj.ABez3 === "undefined" ? null : obj.ABez3 === null ? null : String(obj.ABez3);
		result.PLZ = typeof obj.PLZ === "undefined" ? null : obj.PLZ === null ? null : String(obj.PLZ);
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort === null ? null : String(obj.Ort);
		result.Strasse = typeof obj.Strasse === "undefined" ? null : obj.Strasse === null ? null : String(obj.Strasse);
		result.TelVorw = typeof obj.TelVorw === "undefined" ? null : obj.TelVorw === null ? null : String(obj.TelVorw);
		result.Telefon = typeof obj.Telefon === "undefined" ? null : obj.Telefon === null ? null : String(obj.Telefon);
		result.SF = typeof obj.SF === "undefined" ? null : obj.SF === null ? null : String(obj.SF);
		result.OeffPri = typeof obj.OeffPri === "undefined" ? null : obj.OeffPri === null ? null : String(obj.OeffPri);
		result.KurzBez = typeof obj.KurzBez === "undefined" ? null : obj.KurzBez === null ? null : String(obj.KurzBez);
		result.SchBetrSchl = typeof obj.SchBetrSchl === "undefined" ? null : obj.SchBetrSchl === null ? null : Number(obj.SchBetrSchl);
		result.SchBetrSchlDatum = typeof obj.SchBetrSchlDatum === "undefined" ? null : obj.SchBetrSchlDatum === null ? null : String(obj.SchBetrSchlDatum);
		result.SchuelerZahlASD = typeof obj.SchuelerZahlASD === "undefined" ? null : obj.SchuelerZahlASD === null ? null : Number(obj.SchuelerZahlASD);
		result.SchuelerZahlVS = typeof obj.SchuelerZahlVS === "undefined" ? null : obj.SchuelerZahlVS === null ? null : Number(obj.SchuelerZahlVS);
		result.ArtDerTraegerschaft = typeof obj.ArtDerTraegerschaft === "undefined" ? null : obj.ArtDerTraegerschaft === null ? null : String(obj.ArtDerTraegerschaft);
		result.SchultraegerNr = typeof obj.SchultraegerNr === "undefined" ? null : obj.SchultraegerNr === null ? null : String(obj.SchultraegerNr);
		result.Schulgliederung = typeof obj.Schulgliederung === "undefined" ? null : obj.Schulgliederung === null ? null : String(obj.Schulgliederung);
		result.Ganztagsbetrieb = typeof obj.Ganztagsbetrieb === "undefined" ? null : obj.Ganztagsbetrieb === null ? null : String(obj.Ganztagsbetrieb);
		result.Aktiv = typeof obj.Aktiv === "undefined" ? null : obj.Aktiv === null ? null : Number(obj.Aktiv);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : SchultraegerKatalogEintrag) : string {
		let result = '{';
		result += '"SchulNr" : ' + '"' + obj.SchulNr.valueOf() + '"' + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		result += '"KoRe" : ' + ((!obj.KoRe) ? 'null' : '"' + obj.KoRe.valueOf() + '"') + ',';
		result += '"KoHo" : ' + ((!obj.KoHo) ? 'null' : '"' + obj.KoHo.valueOf() + '"') + ',';
		result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : '"' + obj.ABez1.valueOf() + '"') + ',';
		result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : '"' + obj.ABez2.valueOf() + '"') + ',';
		result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : '"' + obj.ABez3.valueOf() + '"') + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ.valueOf() + '"') + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort.valueOf() + '"') + ',';
		result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse.valueOf() + '"') + ',';
		result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : '"' + obj.TelVorw.valueOf() + '"') + ',';
		result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : '"' + obj.Telefon.valueOf() + '"') + ',';
		result += '"SF" : ' + ((!obj.SF) ? 'null' : '"' + obj.SF.valueOf() + '"') + ',';
		result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : '"' + obj.OeffPri.valueOf() + '"') + ',';
		result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez.valueOf() + '"') + ',';
		result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl.valueOf()) + ',';
		result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : '"' + obj.SchBetrSchlDatum.valueOf() + '"') + ',';
		result += '"SchuelerZahlASD" : ' + ((!obj.SchuelerZahlASD) ? 'null' : obj.SchuelerZahlASD.valueOf()) + ',';
		result += '"SchuelerZahlVS" : ' + ((!obj.SchuelerZahlVS) ? 'null' : obj.SchuelerZahlVS.valueOf()) + ',';
		result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : '"' + obj.ArtDerTraegerschaft.valueOf() + '"') + ',';
		result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : '"' + obj.SchultraegerNr.valueOf() + '"') + ',';
		result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : '"' + obj.Schulgliederung.valueOf() + '"') + ',';
		result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb.valueOf() + '"') + ',';
		result += '"Aktiv" : ' + ((!obj.Aktiv) ? 'null' : obj.Aktiv.valueOf()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchultraegerKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.SchulNr !== "undefined") {
			result += '"SchulNr" : ' + '"' + obj.SchulNr.valueOf() + '"' + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl.valueOf() + '"') + ',';
		}
		if (typeof obj.KoRe !== "undefined") {
			result += '"KoRe" : ' + ((!obj.KoRe) ? 'null' : '"' + obj.KoRe.valueOf() + '"') + ',';
		}
		if (typeof obj.KoHo !== "undefined") {
			result += '"KoHo" : ' + ((!obj.KoHo) ? 'null' : '"' + obj.KoHo.valueOf() + '"') + ',';
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
		if (typeof obj.SchuelerZahlASD !== "undefined") {
			result += '"SchuelerZahlASD" : ' + ((!obj.SchuelerZahlASD) ? 'null' : obj.SchuelerZahlASD.valueOf()) + ',';
		}
		if (typeof obj.SchuelerZahlVS !== "undefined") {
			result += '"SchuelerZahlVS" : ' + ((!obj.SchuelerZahlVS) ? 'null' : obj.SchuelerZahlVS.valueOf()) + ',';
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
		if (typeof obj.Ganztagsbetrieb !== "undefined") {
			result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb.valueOf() + '"') + ',';
		}
		if (typeof obj.Aktiv !== "undefined") {
			result += '"Aktiv" : ' + ((!obj.Aktiv) ? 'null' : obj.Aktiv.valueOf()) + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_SchultraegerKatalogEintrag(obj : unknown) : SchultraegerKatalogEintrag {
	return obj as SchultraegerKatalogEintrag;
}
