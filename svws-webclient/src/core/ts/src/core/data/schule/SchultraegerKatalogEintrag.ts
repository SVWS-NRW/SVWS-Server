import { JavaObject } from '../../../java/lang/JavaObject';

export class SchultraegerKatalogEintrag extends JavaObject {

	/**
	 * Schulträgernummer des Schulträgers.
	 */
	public SchulNr : string = "";

	/**
	 * Regionalschlüssel des Schulträgers
	 */
	public RegSchl : string | null = null;

	/**
	 * KoRe
	 */
	public KoRe : string | null = null;

	/**
	 * KoHo
	 */
	public KoHo : string | null = null;

	/**
	 * Bezeichnung 1 des Schulträgers
	 */
	public ABez1 : string | null = null;

	/**
	 * Bezeichnung 2 des Schulträgers
	 */
	public ABez2 : string | null = null;

	/**
	 * Bezeichnung 3 des Schulträgers
	 */
	public ABez3 : string | null = null;

	/**
	 * PLZ des Schulträgers
	 */
	public PLZ : string | null = null;

	/**
	 * Ort des Schulträgers
	 */
	public Ort : string | null = null;

	/**
	 * Straße des Schulträgers
	 */
	public Strasse : string | null = null;

	/**
	 * Vorwahl des Schulträgers
	 */
	public TelVorw : string | null = null;

	/**
	 * Telefonnummer des Schulträgers
	 */
	public Telefon : string | null = null;

	/**
	 * Ist immer 00 ???
	 */
	public SF : string | null = null;

	/**
	 * Öffentlicher oder privater Schulträger
	 */
	public OeffPri : string | null = null;

	/**
	 * Kurzbezeichnung des Schulträgers
	 */
	public KurzBez : string | null = null;

	/**
	 * Schulbetriebsschlüssel des Schulträgers
	 */
	public SchBetrSchl : number | null = null;

	/**
	 * Datum des Schulbetriebsschlüssels
	 */
	public SchBetrSchlDatum : string | null = null;

	/**
	 * Schülerzahl laut ASD
	 */
	public SchuelerZahlASD : number | null = null;

	/**
	 * Schülerzahl laut VS
	 */
	public SchuelerZahlVS : number | null = null;

	/**
	 * Art der Trägerschaft des Schulträgers
	 */
	public ArtDerTraegerschaft : string | null = null;

	/**
	 * leer siehe SchulNr
	 */
	public SchultraegerNr : string | null = null;

	/**
	 * leer Gliederung
	 */
	public Schulgliederung : string | null = null;

	/**
	 * Leer Ganztagsbetrieb
	 */
	public Ganztagsbetrieb : string | null = null;

	/**
	 * Aktiv ja nein des Schulträgers
	 */
	public Aktiv : number | null = null;

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
		return ['de.nrw.schule.svws.core.data.schule.SchultraegerKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchultraegerKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new SchultraegerKatalogEintrag();
		if (typeof obj.SchulNr === "undefined")
			 throw new Error('invalid json format, missing attribute SchulNr');
		result.SchulNr = obj.SchulNr;
		result.RegSchl = typeof obj.RegSchl === "undefined" ? null : obj.RegSchl === null ? null : obj.RegSchl;
		result.KoRe = typeof obj.KoRe === "undefined" ? null : obj.KoRe === null ? null : obj.KoRe;
		result.KoHo = typeof obj.KoHo === "undefined" ? null : obj.KoHo === null ? null : obj.KoHo;
		result.ABez1 = typeof obj.ABez1 === "undefined" ? null : obj.ABez1 === null ? null : obj.ABez1;
		result.ABez2 = typeof obj.ABez2 === "undefined" ? null : obj.ABez2 === null ? null : obj.ABez2;
		result.ABez3 = typeof obj.ABez3 === "undefined" ? null : obj.ABez3 === null ? null : obj.ABez3;
		result.PLZ = typeof obj.PLZ === "undefined" ? null : obj.PLZ === null ? null : obj.PLZ;
		result.Ort = typeof obj.Ort === "undefined" ? null : obj.Ort === null ? null : obj.Ort;
		result.Strasse = typeof obj.Strasse === "undefined" ? null : obj.Strasse === null ? null : obj.Strasse;
		result.TelVorw = typeof obj.TelVorw === "undefined" ? null : obj.TelVorw === null ? null : obj.TelVorw;
		result.Telefon = typeof obj.Telefon === "undefined" ? null : obj.Telefon === null ? null : obj.Telefon;
		result.SF = typeof obj.SF === "undefined" ? null : obj.SF === null ? null : obj.SF;
		result.OeffPri = typeof obj.OeffPri === "undefined" ? null : obj.OeffPri === null ? null : obj.OeffPri;
		result.KurzBez = typeof obj.KurzBez === "undefined" ? null : obj.KurzBez === null ? null : obj.KurzBez;
		result.SchBetrSchl = typeof obj.SchBetrSchl === "undefined" ? null : obj.SchBetrSchl === null ? null : obj.SchBetrSchl;
		result.SchBetrSchlDatum = typeof obj.SchBetrSchlDatum === "undefined" ? null : obj.SchBetrSchlDatum === null ? null : obj.SchBetrSchlDatum;
		result.SchuelerZahlASD = typeof obj.SchuelerZahlASD === "undefined" ? null : obj.SchuelerZahlASD === null ? null : obj.SchuelerZahlASD;
		result.SchuelerZahlVS = typeof obj.SchuelerZahlVS === "undefined" ? null : obj.SchuelerZahlVS === null ? null : obj.SchuelerZahlVS;
		result.ArtDerTraegerschaft = typeof obj.ArtDerTraegerschaft === "undefined" ? null : obj.ArtDerTraegerschaft === null ? null : obj.ArtDerTraegerschaft;
		result.SchultraegerNr = typeof obj.SchultraegerNr === "undefined" ? null : obj.SchultraegerNr === null ? null : obj.SchultraegerNr;
		result.Schulgliederung = typeof obj.Schulgliederung === "undefined" ? null : obj.Schulgliederung === null ? null : obj.Schulgliederung;
		result.Ganztagsbetrieb = typeof obj.Ganztagsbetrieb === "undefined" ? null : obj.Ganztagsbetrieb === null ? null : obj.Ganztagsbetrieb;
		result.Aktiv = typeof obj.Aktiv === "undefined" ? null : obj.Aktiv === null ? null : obj.Aktiv;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : SchultraegerKatalogEintrag) : string {
		let result = '{';
		result += '"SchulNr" : ' + '"' + obj.SchulNr! + '"' + ',';
		result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl + '"') + ',';
		result += '"KoRe" : ' + ((!obj.KoRe) ? 'null' : '"' + obj.KoRe + '"') + ',';
		result += '"KoHo" : ' + ((!obj.KoHo) ? 'null' : '"' + obj.KoHo + '"') + ',';
		result += '"ABez1" : ' + ((!obj.ABez1) ? 'null' : '"' + obj.ABez1 + '"') + ',';
		result += '"ABez2" : ' + ((!obj.ABez2) ? 'null' : '"' + obj.ABez2 + '"') + ',';
		result += '"ABez3" : ' + ((!obj.ABez3) ? 'null' : '"' + obj.ABez3 + '"') + ',';
		result += '"PLZ" : ' + ((!obj.PLZ) ? 'null' : '"' + obj.PLZ + '"') + ',';
		result += '"Ort" : ' + ((!obj.Ort) ? 'null' : '"' + obj.Ort + '"') + ',';
		result += '"Strasse" : ' + ((!obj.Strasse) ? 'null' : '"' + obj.Strasse + '"') + ',';
		result += '"TelVorw" : ' + ((!obj.TelVorw) ? 'null' : '"' + obj.TelVorw + '"') + ',';
		result += '"Telefon" : ' + ((!obj.Telefon) ? 'null' : '"' + obj.Telefon + '"') + ',';
		result += '"SF" : ' + ((!obj.SF) ? 'null' : '"' + obj.SF + '"') + ',';
		result += '"OeffPri" : ' + ((!obj.OeffPri) ? 'null' : '"' + obj.OeffPri + '"') + ',';
		result += '"KurzBez" : ' + ((!obj.KurzBez) ? 'null' : '"' + obj.KurzBez + '"') + ',';
		result += '"SchBetrSchl" : ' + ((!obj.SchBetrSchl) ? 'null' : obj.SchBetrSchl) + ',';
		result += '"SchBetrSchlDatum" : ' + ((!obj.SchBetrSchlDatum) ? 'null' : '"' + obj.SchBetrSchlDatum + '"') + ',';
		result += '"SchuelerZahlASD" : ' + ((!obj.SchuelerZahlASD) ? 'null' : obj.SchuelerZahlASD) + ',';
		result += '"SchuelerZahlVS" : ' + ((!obj.SchuelerZahlVS) ? 'null' : obj.SchuelerZahlVS) + ',';
		result += '"ArtDerTraegerschaft" : ' + ((!obj.ArtDerTraegerschaft) ? 'null' : '"' + obj.ArtDerTraegerschaft + '"') + ',';
		result += '"SchultraegerNr" : ' + ((!obj.SchultraegerNr) ? 'null' : '"' + obj.SchultraegerNr + '"') + ',';
		result += '"Schulgliederung" : ' + ((!obj.Schulgliederung) ? 'null' : '"' + obj.Schulgliederung + '"') + ',';
		result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb + '"') + ',';
		result += '"Aktiv" : ' + ((!obj.Aktiv) ? 'null' : obj.Aktiv) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchultraegerKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.SchulNr !== "undefined") {
			result += '"SchulNr" : ' + '"' + obj.SchulNr + '"' + ',';
		}
		if (typeof obj.RegSchl !== "undefined") {
			result += '"RegSchl" : ' + ((!obj.RegSchl) ? 'null' : '"' + obj.RegSchl + '"') + ',';
		}
		if (typeof obj.KoRe !== "undefined") {
			result += '"KoRe" : ' + ((!obj.KoRe) ? 'null' : '"' + obj.KoRe + '"') + ',';
		}
		if (typeof obj.KoHo !== "undefined") {
			result += '"KoHo" : ' + ((!obj.KoHo) ? 'null' : '"' + obj.KoHo + '"') + ',';
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
		if (typeof obj.SchuelerZahlASD !== "undefined") {
			result += '"SchuelerZahlASD" : ' + ((!obj.SchuelerZahlASD) ? 'null' : obj.SchuelerZahlASD) + ',';
		}
		if (typeof obj.SchuelerZahlVS !== "undefined") {
			result += '"SchuelerZahlVS" : ' + ((!obj.SchuelerZahlVS) ? 'null' : obj.SchuelerZahlVS) + ',';
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
		if (typeof obj.Ganztagsbetrieb !== "undefined") {
			result += '"Ganztagsbetrieb" : ' + ((!obj.Ganztagsbetrieb) ? 'null' : '"' + obj.Ganztagsbetrieb + '"') + ',';
		}
		if (typeof obj.Aktiv !== "undefined") {
			result += '"Aktiv" : ' + ((!obj.Aktiv) ? 'null' : obj.Aktiv) + ',';
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

export function cast_de_nrw_schule_svws_core_data_schule_SchultraegerKatalogEintrag(obj : unknown) : SchultraegerKatalogEintrag {
	return obj as SchultraegerKatalogEintrag;
}
