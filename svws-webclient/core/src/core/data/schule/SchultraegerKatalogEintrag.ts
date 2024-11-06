import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.SchultraegerKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.SchultraegerKatalogEintrag'].includes(name);
	}

	public static class = new Class<SchultraegerKatalogEintrag>('de.svws_nrw.core.data.schule.SchultraegerKatalogEintrag');

	public static transpilerFromJSON(json : string): SchultraegerKatalogEintrag {
		const obj = JSON.parse(json) as Partial<SchultraegerKatalogEintrag>;
		const result = new SchultraegerKatalogEintrag();
		if (obj.SchulNr === undefined)
			throw new Error('invalid json format, missing attribute SchulNr');
		result.SchulNr = obj.SchulNr;
		result.RegSchl = (obj.RegSchl === undefined) ? null : obj.RegSchl === null ? null : obj.RegSchl;
		result.KoRe = (obj.KoRe === undefined) ? null : obj.KoRe === null ? null : obj.KoRe;
		result.KoHo = (obj.KoHo === undefined) ? null : obj.KoHo === null ? null : obj.KoHo;
		result.ABez1 = (obj.ABez1 === undefined) ? null : obj.ABez1 === null ? null : obj.ABez1;
		result.ABez2 = (obj.ABez2 === undefined) ? null : obj.ABez2 === null ? null : obj.ABez2;
		result.ABez3 = (obj.ABez3 === undefined) ? null : obj.ABez3 === null ? null : obj.ABez3;
		result.PLZ = (obj.PLZ === undefined) ? null : obj.PLZ === null ? null : obj.PLZ;
		result.Ort = (obj.Ort === undefined) ? null : obj.Ort === null ? null : obj.Ort;
		result.Strasse = (obj.Strasse === undefined) ? null : obj.Strasse === null ? null : obj.Strasse;
		result.TelVorw = (obj.TelVorw === undefined) ? null : obj.TelVorw === null ? null : obj.TelVorw;
		result.Telefon = (obj.Telefon === undefined) ? null : obj.Telefon === null ? null : obj.Telefon;
		result.SF = (obj.SF === undefined) ? null : obj.SF === null ? null : obj.SF;
		result.OeffPri = (obj.OeffPri === undefined) ? null : obj.OeffPri === null ? null : obj.OeffPri;
		result.KurzBez = (obj.KurzBez === undefined) ? null : obj.KurzBez === null ? null : obj.KurzBez;
		result.SchBetrSchl = (obj.SchBetrSchl === undefined) ? null : obj.SchBetrSchl === null ? null : obj.SchBetrSchl;
		result.SchBetrSchlDatum = (obj.SchBetrSchlDatum === undefined) ? null : obj.SchBetrSchlDatum === null ? null : obj.SchBetrSchlDatum;
		result.SchuelerZahlASD = (obj.SchuelerZahlASD === undefined) ? null : obj.SchuelerZahlASD === null ? null : obj.SchuelerZahlASD;
		result.SchuelerZahlVS = (obj.SchuelerZahlVS === undefined) ? null : obj.SchuelerZahlVS === null ? null : obj.SchuelerZahlVS;
		result.ArtDerTraegerschaft = (obj.ArtDerTraegerschaft === undefined) ? null : obj.ArtDerTraegerschaft === null ? null : obj.ArtDerTraegerschaft;
		result.SchultraegerNr = (obj.SchultraegerNr === undefined) ? null : obj.SchultraegerNr === null ? null : obj.SchultraegerNr;
		result.Schulgliederung = (obj.Schulgliederung === undefined) ? null : obj.Schulgliederung === null ? null : obj.Schulgliederung;
		result.Ganztagsbetrieb = (obj.Ganztagsbetrieb === undefined) ? null : obj.Ganztagsbetrieb === null ? null : obj.Ganztagsbetrieb;
		result.Aktiv = (obj.Aktiv === undefined) ? null : obj.Aktiv === null ? null : obj.Aktiv;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : SchultraegerKatalogEintrag) : string {
		let result = '{';
		result += '"SchulNr" : ' + JSON.stringify(obj.SchulNr) + ',';
		result += '"RegSchl" : ' + ((obj.RegSchl === null) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		result += '"KoRe" : ' + ((obj.KoRe === null) ? 'null' : JSON.stringify(obj.KoRe)) + ',';
		result += '"KoHo" : ' + ((obj.KoHo === null) ? 'null' : JSON.stringify(obj.KoHo)) + ',';
		result += '"ABez1" : ' + ((obj.ABez1 === null) ? 'null' : JSON.stringify(obj.ABez1)) + ',';
		result += '"ABez2" : ' + ((obj.ABez2 === null) ? 'null' : JSON.stringify(obj.ABez2)) + ',';
		result += '"ABez3" : ' + ((obj.ABez3 === null) ? 'null' : JSON.stringify(obj.ABez3)) + ',';
		result += '"PLZ" : ' + ((obj.PLZ === null) ? 'null' : JSON.stringify(obj.PLZ)) + ',';
		result += '"Ort" : ' + ((obj.Ort === null) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		result += '"Strasse" : ' + ((obj.Strasse === null) ? 'null' : JSON.stringify(obj.Strasse)) + ',';
		result += '"TelVorw" : ' + ((obj.TelVorw === null) ? 'null' : JSON.stringify(obj.TelVorw)) + ',';
		result += '"Telefon" : ' + ((obj.Telefon === null) ? 'null' : JSON.stringify(obj.Telefon)) + ',';
		result += '"SF" : ' + ((obj.SF === null) ? 'null' : JSON.stringify(obj.SF)) + ',';
		result += '"OeffPri" : ' + ((obj.OeffPri === null) ? 'null' : JSON.stringify(obj.OeffPri)) + ',';
		result += '"KurzBez" : ' + ((obj.KurzBez === null) ? 'null' : JSON.stringify(obj.KurzBez)) + ',';
		result += '"SchBetrSchl" : ' + ((obj.SchBetrSchl === null) ? 'null' : obj.SchBetrSchl.toString()) + ',';
		result += '"SchBetrSchlDatum" : ' + ((obj.SchBetrSchlDatum === null) ? 'null' : JSON.stringify(obj.SchBetrSchlDatum)) + ',';
		result += '"SchuelerZahlASD" : ' + ((obj.SchuelerZahlASD === null) ? 'null' : obj.SchuelerZahlASD.toString()) + ',';
		result += '"SchuelerZahlVS" : ' + ((obj.SchuelerZahlVS === null) ? 'null' : obj.SchuelerZahlVS.toString()) + ',';
		result += '"ArtDerTraegerschaft" : ' + ((obj.ArtDerTraegerschaft === null) ? 'null' : JSON.stringify(obj.ArtDerTraegerschaft)) + ',';
		result += '"SchultraegerNr" : ' + ((obj.SchultraegerNr === null) ? 'null' : JSON.stringify(obj.SchultraegerNr)) + ',';
		result += '"Schulgliederung" : ' + ((obj.Schulgliederung === null) ? 'null' : JSON.stringify(obj.Schulgliederung)) + ',';
		result += '"Ganztagsbetrieb" : ' + ((obj.Ganztagsbetrieb === null) ? 'null' : JSON.stringify(obj.Ganztagsbetrieb)) + ',';
		result += '"Aktiv" : ' + ((obj.Aktiv === null) ? 'null' : obj.Aktiv.toString()) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchultraegerKatalogEintrag>) : string {
		let result = '{';
		if (obj.SchulNr !== undefined) {
			result += '"SchulNr" : ' + JSON.stringify(obj.SchulNr) + ',';
		}
		if (obj.RegSchl !== undefined) {
			result += '"RegSchl" : ' + ((obj.RegSchl === null) ? 'null' : JSON.stringify(obj.RegSchl)) + ',';
		}
		if (obj.KoRe !== undefined) {
			result += '"KoRe" : ' + ((obj.KoRe === null) ? 'null' : JSON.stringify(obj.KoRe)) + ',';
		}
		if (obj.KoHo !== undefined) {
			result += '"KoHo" : ' + ((obj.KoHo === null) ? 'null' : JSON.stringify(obj.KoHo)) + ',';
		}
		if (obj.ABez1 !== undefined) {
			result += '"ABez1" : ' + ((obj.ABez1 === null) ? 'null' : JSON.stringify(obj.ABez1)) + ',';
		}
		if (obj.ABez2 !== undefined) {
			result += '"ABez2" : ' + ((obj.ABez2 === null) ? 'null' : JSON.stringify(obj.ABez2)) + ',';
		}
		if (obj.ABez3 !== undefined) {
			result += '"ABez3" : ' + ((obj.ABez3 === null) ? 'null' : JSON.stringify(obj.ABez3)) + ',';
		}
		if (obj.PLZ !== undefined) {
			result += '"PLZ" : ' + ((obj.PLZ === null) ? 'null' : JSON.stringify(obj.PLZ)) + ',';
		}
		if (obj.Ort !== undefined) {
			result += '"Ort" : ' + ((obj.Ort === null) ? 'null' : JSON.stringify(obj.Ort)) + ',';
		}
		if (obj.Strasse !== undefined) {
			result += '"Strasse" : ' + ((obj.Strasse === null) ? 'null' : JSON.stringify(obj.Strasse)) + ',';
		}
		if (obj.TelVorw !== undefined) {
			result += '"TelVorw" : ' + ((obj.TelVorw === null) ? 'null' : JSON.stringify(obj.TelVorw)) + ',';
		}
		if (obj.Telefon !== undefined) {
			result += '"Telefon" : ' + ((obj.Telefon === null) ? 'null' : JSON.stringify(obj.Telefon)) + ',';
		}
		if (obj.SF !== undefined) {
			result += '"SF" : ' + ((obj.SF === null) ? 'null' : JSON.stringify(obj.SF)) + ',';
		}
		if (obj.OeffPri !== undefined) {
			result += '"OeffPri" : ' + ((obj.OeffPri === null) ? 'null' : JSON.stringify(obj.OeffPri)) + ',';
		}
		if (obj.KurzBez !== undefined) {
			result += '"KurzBez" : ' + ((obj.KurzBez === null) ? 'null' : JSON.stringify(obj.KurzBez)) + ',';
		}
		if (obj.SchBetrSchl !== undefined) {
			result += '"SchBetrSchl" : ' + ((obj.SchBetrSchl === null) ? 'null' : obj.SchBetrSchl.toString()) + ',';
		}
		if (obj.SchBetrSchlDatum !== undefined) {
			result += '"SchBetrSchlDatum" : ' + ((obj.SchBetrSchlDatum === null) ? 'null' : JSON.stringify(obj.SchBetrSchlDatum)) + ',';
		}
		if (obj.SchuelerZahlASD !== undefined) {
			result += '"SchuelerZahlASD" : ' + ((obj.SchuelerZahlASD === null) ? 'null' : obj.SchuelerZahlASD.toString()) + ',';
		}
		if (obj.SchuelerZahlVS !== undefined) {
			result += '"SchuelerZahlVS" : ' + ((obj.SchuelerZahlVS === null) ? 'null' : obj.SchuelerZahlVS.toString()) + ',';
		}
		if (obj.ArtDerTraegerschaft !== undefined) {
			result += '"ArtDerTraegerschaft" : ' + ((obj.ArtDerTraegerschaft === null) ? 'null' : JSON.stringify(obj.ArtDerTraegerschaft)) + ',';
		}
		if (obj.SchultraegerNr !== undefined) {
			result += '"SchultraegerNr" : ' + ((obj.SchultraegerNr === null) ? 'null' : JSON.stringify(obj.SchultraegerNr)) + ',';
		}
		if (obj.Schulgliederung !== undefined) {
			result += '"Schulgliederung" : ' + ((obj.Schulgliederung === null) ? 'null' : JSON.stringify(obj.Schulgliederung)) + ',';
		}
		if (obj.Ganztagsbetrieb !== undefined) {
			result += '"Ganztagsbetrieb" : ' + ((obj.Ganztagsbetrieb === null) ? 'null' : JSON.stringify(obj.Ganztagsbetrieb)) + ',';
		}
		if (obj.Aktiv !== undefined) {
			result += '"Aktiv" : ' + ((obj.Aktiv === null) ? 'null' : obj.Aktiv.toString()) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_SchultraegerKatalogEintrag(obj : unknown) : SchultraegerKatalogEintrag {
	return obj as SchultraegerKatalogEintrag;
}
