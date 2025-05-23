import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerSchulbesuchSchule extends JavaObject {

	/**
	 * Die ID der Informationen zum vorigen Schulbesuch in der Datenbank.
	 */
	public id : number = 0;

	/**
	 * Die ID der Schule.
	 */
	public idSchule : number | null = null;

	/**
	 * Der Schlüssel des Bildungsganges/Schulgliederung an der Schule.
	 */
	public schulgliederung : string | null = null;

	/**
	 * Die ID des Grundes für die Entlassung von der Schule.
	 */
	public entlassgrundID : number | null = null;

	/**
	 * Die ID des Abschlusses, welcher an der Schule erworben wurde.
	 */
	public abschlussartID : string | null = null;

	/**
	 * Die ID der Organisationsform der Schule (z.B. für Halbtagsunterricht).
	 */
	public organisationsFormID : string | null = null;

	/**
	 * Das Datum, ab dem die Schule besucht wurde.
	 */
	public datumVon : string | null = null;

	/**
	 * Das Datum, bis wann die Schule besucht wurde.
	 */
	public datumBis : string | null = null;

	/**
	 * Der Jahrgang, ab dem die Schule besucht wurde.
	 */
	public jahrgangVon : string | null = null;

	/**
	 * Der Jahrgang, bis zu dem die Schule besucht wurde.
	 */
	public jahrgangBis : string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule'].includes(name);
	}

	public static class = new Class<SchuelerSchulbesuchSchule>('de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule');

	public static transpilerFromJSON(json : string): SchuelerSchulbesuchSchule {
		const obj = JSON.parse(json) as Partial<SchuelerSchulbesuchSchule>;
		const result = new SchuelerSchulbesuchSchule();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.idSchule = (obj.idSchule === undefined) ? null : obj.idSchule === null ? null : obj.idSchule;
		result.schulgliederung = (obj.schulgliederung === undefined) ? null : obj.schulgliederung === null ? null : obj.schulgliederung;
		result.entlassgrundID = (obj.entlassgrundID === undefined) ? null : obj.entlassgrundID === null ? null : obj.entlassgrundID;
		result.abschlussartID = (obj.abschlussartID === undefined) ? null : obj.abschlussartID === null ? null : obj.abschlussartID;
		result.organisationsFormID = (obj.organisationsFormID === undefined) ? null : obj.organisationsFormID === null ? null : obj.organisationsFormID;
		result.datumVon = (obj.datumVon === undefined) ? null : obj.datumVon === null ? null : obj.datumVon;
		result.datumBis = (obj.datumBis === undefined) ? null : obj.datumBis === null ? null : obj.datumBis;
		result.jahrgangVon = (obj.jahrgangVon === undefined) ? null : obj.jahrgangVon === null ? null : obj.jahrgangVon;
		result.jahrgangBis = (obj.jahrgangBis === undefined) ? null : obj.jahrgangBis === null ? null : obj.jahrgangBis;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerSchulbesuchSchule) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idSchule" : ' + ((obj.idSchule === null) ? 'null' : obj.idSchule.toString()) + ',';
		result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		result += '"entlassgrundID" : ' + ((obj.entlassgrundID === null) ? 'null' : obj.entlassgrundID.toString()) + ',';
		result += '"abschlussartID" : ' + ((obj.abschlussartID === null) ? 'null' : JSON.stringify(obj.abschlussartID)) + ',';
		result += '"organisationsFormID" : ' + ((obj.organisationsFormID === null) ? 'null' : JSON.stringify(obj.organisationsFormID)) + ',';
		result += '"datumVon" : ' + ((obj.datumVon === null) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		result += '"datumBis" : ' + ((obj.datumBis === null) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		result += '"jahrgangVon" : ' + ((obj.jahrgangVon === null) ? 'null' : JSON.stringify(obj.jahrgangVon)) + ',';
		result += '"jahrgangBis" : ' + ((obj.jahrgangBis === null) ? 'null' : JSON.stringify(obj.jahrgangBis)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerSchulbesuchSchule>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idSchule !== undefined) {
			result += '"idSchule" : ' + ((obj.idSchule === null) ? 'null' : obj.idSchule.toString()) + ',';
		}
		if (obj.schulgliederung !== undefined) {
			result += '"schulgliederung" : ' + ((obj.schulgliederung === null) ? 'null' : JSON.stringify(obj.schulgliederung)) + ',';
		}
		if (obj.entlassgrundID !== undefined) {
			result += '"entlassgrundID" : ' + ((obj.entlassgrundID === null) ? 'null' : obj.entlassgrundID.toString()) + ',';
		}
		if (obj.abschlussartID !== undefined) {
			result += '"abschlussartID" : ' + ((obj.abschlussartID === null) ? 'null' : JSON.stringify(obj.abschlussartID)) + ',';
		}
		if (obj.organisationsFormID !== undefined) {
			result += '"organisationsFormID" : ' + ((obj.organisationsFormID === null) ? 'null' : JSON.stringify(obj.organisationsFormID)) + ',';
		}
		if (obj.datumVon !== undefined) {
			result += '"datumVon" : ' + ((obj.datumVon === null) ? 'null' : JSON.stringify(obj.datumVon)) + ',';
		}
		if (obj.datumBis !== undefined) {
			result += '"datumBis" : ' + ((obj.datumBis === null) ? 'null' : JSON.stringify(obj.datumBis)) + ',';
		}
		if (obj.jahrgangVon !== undefined) {
			result += '"jahrgangVon" : ' + ((obj.jahrgangVon === null) ? 'null' : JSON.stringify(obj.jahrgangVon)) + ',';
		}
		if (obj.jahrgangBis !== undefined) {
			result += '"jahrgangBis" : ' + ((obj.jahrgangBis === null) ? 'null' : JSON.stringify(obj.jahrgangBis)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerSchulbesuchSchule(obj : unknown) : SchuelerSchulbesuchSchule {
	return obj as SchuelerSchulbesuchSchule;
}
