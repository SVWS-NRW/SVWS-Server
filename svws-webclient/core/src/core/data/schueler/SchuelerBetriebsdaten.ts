import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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
	public allgadranschreiben : boolean = false;

	/**
	 * Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler
	 */
	public praktikum : boolean = false;

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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten'].includes(name);
	}

	public static class = new Class<SchuelerBetriebsdaten>('de.svws_nrw.core.data.schueler.SchuelerBetriebsdaten');

	public static transpilerFromJSON(json : string): SchuelerBetriebsdaten {
		const obj = JSON.parse(json) as Partial<SchuelerBetriebsdaten>;
		const result = new SchuelerBetriebsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schueler_id === undefined)
			throw new Error('invalid json format, missing attribute schueler_id');
		result.schueler_id = obj.schueler_id;
		if (obj.betrieb_id === undefined)
			throw new Error('invalid json format, missing attribute betrieb_id');
		result.betrieb_id = obj.betrieb_id;
		result.beschaeftigungsart_id = (obj.beschaeftigungsart_id === undefined) ? null : obj.beschaeftigungsart_id === null ? null : obj.beschaeftigungsart_id;
		result.vertragsbeginn = (obj.vertragsbeginn === undefined) ? null : obj.vertragsbeginn === null ? null : obj.vertragsbeginn;
		result.vertragsende = (obj.vertragsende === undefined) ? null : obj.vertragsende === null ? null : obj.vertragsende;
		result.ausbilder = (obj.ausbilder === undefined) ? null : obj.ausbilder === null ? null : obj.ausbilder;
		if (obj.allgadranschreiben === undefined)
			throw new Error('invalid json format, missing attribute allgadranschreiben');
		result.allgadranschreiben = obj.allgadranschreiben;
		if (obj.praktikum === undefined)
			throw new Error('invalid json format, missing attribute praktikum');
		result.praktikum = obj.praktikum;
		result.sortierung = (obj.sortierung === undefined) ? null : obj.sortierung === null ? null : obj.sortierung;
		result.ansprechpartner_id = (obj.ansprechpartner_id === undefined) ? null : obj.ansprechpartner_id === null ? null : obj.ansprechpartner_id;
		result.betreuungslehrer_id = (obj.betreuungslehrer_id === undefined) ? null : obj.betreuungslehrer_id === null ? null : obj.betreuungslehrer_id;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerBetriebsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schueler_id" : ' + obj.schueler_id.toString() + ',';
		result += '"betrieb_id" : ' + obj.betrieb_id.toString() + ',';
		result += '"beschaeftigungsart_id" : ' + ((obj.beschaeftigungsart_id === null) ? 'null' : obj.beschaeftigungsart_id.toString()) + ',';
		result += '"vertragsbeginn" : ' + ((obj.vertragsbeginn === null) ? 'null' : JSON.stringify(obj.vertragsbeginn)) + ',';
		result += '"vertragsende" : ' + ((obj.vertragsende === null) ? 'null' : JSON.stringify(obj.vertragsende)) + ',';
		result += '"ausbilder" : ' + ((obj.ausbilder === null) ? 'null' : JSON.stringify(obj.ausbilder)) + ',';
		result += '"allgadranschreiben" : ' + obj.allgadranschreiben + ',';
		result += '"praktikum" : ' + obj.praktikum + ',';
		result += '"sortierung" : ' + ((obj.sortierung === null) ? 'null' : obj.sortierung.toString()) + ',';
		result += '"ansprechpartner_id" : ' + ((obj.ansprechpartner_id === null) ? 'null' : obj.ansprechpartner_id.toString()) + ',';
		result += '"betreuungslehrer_id" : ' + ((obj.betreuungslehrer_id === null) ? 'null' : obj.betreuungslehrer_id.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerBetriebsdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schueler_id !== undefined) {
			result += '"schueler_id" : ' + obj.schueler_id.toString() + ',';
		}
		if (obj.betrieb_id !== undefined) {
			result += '"betrieb_id" : ' + obj.betrieb_id.toString() + ',';
		}
		if (obj.beschaeftigungsart_id !== undefined) {
			result += '"beschaeftigungsart_id" : ' + ((obj.beschaeftigungsart_id === null) ? 'null' : obj.beschaeftigungsart_id.toString()) + ',';
		}
		if (obj.vertragsbeginn !== undefined) {
			result += '"vertragsbeginn" : ' + ((obj.vertragsbeginn === null) ? 'null' : JSON.stringify(obj.vertragsbeginn)) + ',';
		}
		if (obj.vertragsende !== undefined) {
			result += '"vertragsende" : ' + ((obj.vertragsende === null) ? 'null' : JSON.stringify(obj.vertragsende)) + ',';
		}
		if (obj.ausbilder !== undefined) {
			result += '"ausbilder" : ' + ((obj.ausbilder === null) ? 'null' : JSON.stringify(obj.ausbilder)) + ',';
		}
		if (obj.allgadranschreiben !== undefined) {
			result += '"allgadranschreiben" : ' + obj.allgadranschreiben + ',';
		}
		if (obj.praktikum !== undefined) {
			result += '"praktikum" : ' + obj.praktikum + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + ((obj.sortierung === null) ? 'null' : obj.sortierung.toString()) + ',';
		}
		if (obj.ansprechpartner_id !== undefined) {
			result += '"ansprechpartner_id" : ' + ((obj.ansprechpartner_id === null) ? 'null' : obj.ansprechpartner_id.toString()) + ',';
		}
		if (obj.betreuungslehrer_id !== undefined) {
			result += '"betreuungslehrer_id" : ' + ((obj.betreuungslehrer_id === null) ? 'null' : obj.betreuungslehrer_id.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerBetriebsdaten(obj : unknown) : SchuelerBetriebsdaten {
	return obj as SchuelerBetriebsdaten;
}
