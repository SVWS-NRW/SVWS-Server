import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';

export class SchuelerBetriebsdaten extends JavaObject {

	public id : Number | null = null;

	public schueler_id : Number | null = null;

	public betrieb_id : Number | null = null;

	public beschaeftigungsart_id : Number | null = null;

	public vertragsbeginn : String | null = null;

	public vertragsende : String | null = null;

	public ausbilder : String | null = null;

	public allgadranschreiben : Boolean | null = null;

	public praktikum : Boolean | null = null;

	public sortierung : Number | null = null;

	public ansprechpartner_id : Number | null = null;

	public betreuungslehrer_id : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schueler.SchuelerBetriebsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerBetriebsdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerBetriebsdaten();
		result.id = typeof obj.id === "undefined" ? null : obj.id === null ? null : Number(obj.id);
		result.schueler_id = typeof obj.schueler_id === "undefined" ? null : obj.schueler_id === null ? null : Number(obj.schueler_id);
		result.betrieb_id = typeof obj.betrieb_id === "undefined" ? null : obj.betrieb_id === null ? null : Number(obj.betrieb_id);
		result.beschaeftigungsart_id = typeof obj.beschaeftigungsart_id === "undefined" ? null : obj.beschaeftigungsart_id === null ? null : Number(obj.beschaeftigungsart_id);
		result.vertragsbeginn = typeof obj.vertragsbeginn === "undefined" ? null : obj.vertragsbeginn === null ? null : String(obj.vertragsbeginn);
		result.vertragsende = typeof obj.vertragsende === "undefined" ? null : obj.vertragsende === null ? null : String(obj.vertragsende);
		result.ausbilder = typeof obj.ausbilder === "undefined" ? null : obj.ausbilder === null ? null : String(obj.ausbilder);
		result.allgadranschreiben = typeof obj.allgadranschreiben === "undefined" ? null : obj.allgadranschreiben === null ? null : Boolean(obj.allgadranschreiben);
		result.praktikum = typeof obj.praktikum === "undefined" ? null : obj.praktikum === null ? null : Boolean(obj.praktikum);
		result.sortierung = typeof obj.sortierung === "undefined" ? null : obj.sortierung === null ? null : Number(obj.sortierung);
		result.ansprechpartner_id = typeof obj.ansprechpartner_id === "undefined" ? null : obj.ansprechpartner_id === null ? null : Number(obj.ansprechpartner_id);
		result.betreuungslehrer_id = typeof obj.betreuungslehrer_id === "undefined" ? null : obj.betreuungslehrer_id === null ? null : Number(obj.betreuungslehrer_id);
		return result;
	}

	public static transpilerToJSON(obj : SchuelerBetriebsdaten) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		result += '"schueler_id" : ' + ((!obj.schueler_id) ? 'null' : obj.schueler_id.valueOf()) + ',';
		result += '"betrieb_id" : ' + ((!obj.betrieb_id) ? 'null' : obj.betrieb_id.valueOf()) + ',';
		result += '"beschaeftigungsart_id" : ' + ((!obj.beschaeftigungsart_id) ? 'null' : obj.beschaeftigungsart_id.valueOf()) + ',';
		result += '"vertragsbeginn" : ' + ((!obj.vertragsbeginn) ? 'null' : '"' + obj.vertragsbeginn.valueOf() + '"') + ',';
		result += '"vertragsende" : ' + ((!obj.vertragsende) ? 'null' : '"' + obj.vertragsende.valueOf() + '"') + ',';
		result += '"ausbilder" : ' + ((!obj.ausbilder) ? 'null' : '"' + obj.ausbilder.valueOf() + '"') + ',';
		result += '"allgadranschreiben" : ' + ((!obj.allgadranschreiben) ? 'null' : obj.allgadranschreiben.valueOf()) + ',';
		result += '"praktikum" : ' + ((!obj.praktikum) ? 'null' : obj.praktikum.valueOf()) + ',';
		result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung.valueOf()) + ',';
		result += '"ansprechpartner_id" : ' + ((!obj.ansprechpartner_id) ? 'null' : obj.ansprechpartner_id.valueOf()) + ',';
		result += '"betreuungslehrer_id" : ' + ((!obj.betreuungslehrer_id) ? 'null' : obj.betreuungslehrer_id.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerBetriebsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.valueOf()) + ',';
		}
		if (typeof obj.schueler_id !== "undefined") {
			result += '"schueler_id" : ' + ((!obj.schueler_id) ? 'null' : obj.schueler_id.valueOf()) + ',';
		}
		if (typeof obj.betrieb_id !== "undefined") {
			result += '"betrieb_id" : ' + ((!obj.betrieb_id) ? 'null' : obj.betrieb_id.valueOf()) + ',';
		}
		if (typeof obj.beschaeftigungsart_id !== "undefined") {
			result += '"beschaeftigungsart_id" : ' + ((!obj.beschaeftigungsart_id) ? 'null' : obj.beschaeftigungsart_id.valueOf()) + ',';
		}
		if (typeof obj.vertragsbeginn !== "undefined") {
			result += '"vertragsbeginn" : ' + ((!obj.vertragsbeginn) ? 'null' : '"' + obj.vertragsbeginn.valueOf() + '"') + ',';
		}
		if (typeof obj.vertragsende !== "undefined") {
			result += '"vertragsende" : ' + ((!obj.vertragsende) ? 'null' : '"' + obj.vertragsende.valueOf() + '"') + ',';
		}
		if (typeof obj.ausbilder !== "undefined") {
			result += '"ausbilder" : ' + ((!obj.ausbilder) ? 'null' : '"' + obj.ausbilder.valueOf() + '"') + ',';
		}
		if (typeof obj.allgadranschreiben !== "undefined") {
			result += '"allgadranschreiben" : ' + ((!obj.allgadranschreiben) ? 'null' : obj.allgadranschreiben.valueOf()) + ',';
		}
		if (typeof obj.praktikum !== "undefined") {
			result += '"praktikum" : ' + ((!obj.praktikum) ? 'null' : obj.praktikum.valueOf()) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung.valueOf()) + ',';
		}
		if (typeof obj.ansprechpartner_id !== "undefined") {
			result += '"ansprechpartner_id" : ' + ((!obj.ansprechpartner_id) ? 'null' : obj.ansprechpartner_id.valueOf()) + ',';
		}
		if (typeof obj.betreuungslehrer_id !== "undefined") {
			result += '"betreuungslehrer_id" : ' + ((!obj.betreuungslehrer_id) ? 'null' : obj.betreuungslehrer_id.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schueler_SchuelerBetriebsdaten(obj : unknown) : SchuelerBetriebsdaten {
	return obj as SchuelerBetriebsdaten;
}
