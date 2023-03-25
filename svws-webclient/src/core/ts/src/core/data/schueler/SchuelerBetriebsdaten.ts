import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerBetriebsdaten extends JavaObject {

	/**
	 * ID des Datensatzes
	 */
	public id : number = 0;

	/**
	 * ID des Schülers
	 */
	public schueler_id : number = 0;

	/**
	 * AdressID des Betriebeeintrags beim Schüler
	 */
	public betrieb_id : number = 0;

	/**
	 * ID der Beschäftigungsart des Schülers
	 */
	public beschaeftigungsart_id : number | null = null;

	/**
	 * Datum Vertragsbeginn des Betriebeeintrags beim Schüler
	 */
	public vertragsbeginn : string | null = null;

	/**
	 * Datum des Vertragsende des Betriebeeintrags beim Schüler
	 */
	public vertragsende : string | null = null;

	/**
	 * Ausbildername des Betriebeeintrags beim Schüler
	 */
	public ausbilder : string | null = null;

	/**
	 * Betrieb erhält Anschreiben Ja/Nein
	 */
	public allgadranschreiben : boolean | null = null;

	/**
	 * Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler
	 */
	public praktikum : boolean | null = null;

	/**
	 * Sortierung des Betriebeeintrags beim Schüler
	 */
	public sortierung : number | null = null;

	/**
	 * AnsprechpartnerID des Betriebeeintrags beim Schüler
	 */
	public ansprechpartner_id : number | null = null;

	/**
	 * BetreuungslehrerID des Betriebeeintrags beim Schüler
	 */
	public betreuungslehrer_id : number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerBetriebsdaten {
		const obj = JSON.parse(json);
		const result = new SchuelerBetriebsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schueler_id === "undefined")
			 throw new Error('invalid json format, missing attribute schueler_id');
		result.schueler_id = obj.schueler_id;
		if (typeof obj.betrieb_id === "undefined")
			 throw new Error('invalid json format, missing attribute betrieb_id');
		result.betrieb_id = obj.betrieb_id;
		result.beschaeftigungsart_id = typeof obj.beschaeftigungsart_id === "undefined" ? null : obj.beschaeftigungsart_id === null ? null : obj.beschaeftigungsart_id;
		result.vertragsbeginn = typeof obj.vertragsbeginn === "undefined" ? null : obj.vertragsbeginn === null ? null : obj.vertragsbeginn;
		result.vertragsende = typeof obj.vertragsende === "undefined" ? null : obj.vertragsende === null ? null : obj.vertragsende;
		result.ausbilder = typeof obj.ausbilder === "undefined" ? null : obj.ausbilder === null ? null : obj.ausbilder;
		result.allgadranschreiben = typeof obj.allgadranschreiben === "undefined" ? null : obj.allgadranschreiben === null ? null : obj.allgadranschreiben;
		result.praktikum = typeof obj.praktikum === "undefined" ? null : obj.praktikum === null ? null : obj.praktikum;
		result.sortierung = typeof obj.sortierung === "undefined" ? null : obj.sortierung === null ? null : obj.sortierung;
		result.ansprechpartner_id = typeof obj.ansprechpartner_id === "undefined" ? null : obj.ansprechpartner_id === null ? null : obj.ansprechpartner_id;
		result.betreuungslehrer_id = typeof obj.betreuungslehrer_id === "undefined" ? null : obj.betreuungslehrer_id === null ? null : obj.betreuungslehrer_id;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerBetriebsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schueler_id" : ' + obj.schueler_id + ',';
		result += '"betrieb_id" : ' + obj.betrieb_id + ',';
		result += '"beschaeftigungsart_id" : ' + ((!obj.beschaeftigungsart_id) ? 'null' : obj.beschaeftigungsart_id) + ',';
		result += '"vertragsbeginn" : ' + ((!obj.vertragsbeginn) ? 'null' : '"' + obj.vertragsbeginn + '"') + ',';
		result += '"vertragsende" : ' + ((!obj.vertragsende) ? 'null' : '"' + obj.vertragsende + '"') + ',';
		result += '"ausbilder" : ' + ((!obj.ausbilder) ? 'null' : '"' + obj.ausbilder + '"') + ',';
		result += '"allgadranschreiben" : ' + ((!obj.allgadranschreiben) ? 'null' : obj.allgadranschreiben) + ',';
		result += '"praktikum" : ' + ((!obj.praktikum) ? 'null' : obj.praktikum) + ',';
		result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung) + ',';
		result += '"ansprechpartner_id" : ' + ((!obj.ansprechpartner_id) ? 'null' : obj.ansprechpartner_id) + ',';
		result += '"betreuungslehrer_id" : ' + ((!obj.betreuungslehrer_id) ? 'null' : obj.betreuungslehrer_id) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerBetriebsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schueler_id !== "undefined") {
			result += '"schueler_id" : ' + obj.schueler_id + ',';
		}
		if (typeof obj.betrieb_id !== "undefined") {
			result += '"betrieb_id" : ' + obj.betrieb_id + ',';
		}
		if (typeof obj.beschaeftigungsart_id !== "undefined") {
			result += '"beschaeftigungsart_id" : ' + ((!obj.beschaeftigungsart_id) ? 'null' : obj.beschaeftigungsart_id) + ',';
		}
		if (typeof obj.vertragsbeginn !== "undefined") {
			result += '"vertragsbeginn" : ' + ((!obj.vertragsbeginn) ? 'null' : '"' + obj.vertragsbeginn + '"') + ',';
		}
		if (typeof obj.vertragsende !== "undefined") {
			result += '"vertragsende" : ' + ((!obj.vertragsende) ? 'null' : '"' + obj.vertragsende + '"') + ',';
		}
		if (typeof obj.ausbilder !== "undefined") {
			result += '"ausbilder" : ' + ((!obj.ausbilder) ? 'null' : '"' + obj.ausbilder + '"') + ',';
		}
		if (typeof obj.allgadranschreiben !== "undefined") {
			result += '"allgadranschreiben" : ' + ((!obj.allgadranschreiben) ? 'null' : obj.allgadranschreiben) + ',';
		}
		if (typeof obj.praktikum !== "undefined") {
			result += '"praktikum" : ' + ((!obj.praktikum) ? 'null' : obj.praktikum) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + ((!obj.sortierung) ? 'null' : obj.sortierung) + ',';
		}
		if (typeof obj.ansprechpartner_id !== "undefined") {
			result += '"ansprechpartner_id" : ' + ((!obj.ansprechpartner_id) ? 'null' : obj.ansprechpartner_id) + ',';
		}
		if (typeof obj.betreuungslehrer_id !== "undefined") {
			result += '"betreuungslehrer_id" : ' + ((!obj.betreuungslehrer_id) ? 'null' : obj.betreuungslehrer_id) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerBetriebsdaten(obj : unknown) : SchuelerBetriebsdaten {
	return obj as SchuelerBetriebsdaten;
}
