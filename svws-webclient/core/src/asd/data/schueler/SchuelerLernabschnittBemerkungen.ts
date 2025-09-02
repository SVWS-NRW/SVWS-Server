import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerLernabschnittBemerkungen extends JavaObject {

	/**
	 * Der Text für allgemeine Zeugnisbemerkungen.
	 */
	public zeugnisAllgemein : string | null = "";

	/**
	 * Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 */
	public zeugnisASV : string | null = "";

	/**
	 * Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 */
	public zeugnisLELS : string | null = "";

	/**
	 * Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement.
	 */
	public zeugnisAUE : string | null = "";

	/**
	 * Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 */
	public uebergangESF : string | null = "";

	/**
	 * Eine Bemerkung zum Förderschwerpunkt.
	 */
	public foerderschwerpunkt : string | null = "";

	/**
	 * Eine Bemerkung zur Versetzungsentscheidung.
	 */
	public versetzungsentscheidung : string | null = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen'].includes(name);
	}

	public static class = new Class<SchuelerLernabschnittBemerkungen>('de.svws_nrw.asd.data.schueler.SchuelerLernabschnittBemerkungen');

	public static transpilerFromJSON(json : string): SchuelerLernabschnittBemerkungen {
		const obj = JSON.parse(json) as Partial<SchuelerLernabschnittBemerkungen>;
		const result = new SchuelerLernabschnittBemerkungen();
		result.zeugnisAllgemein = (obj.zeugnisAllgemein === undefined) ? null : obj.zeugnisAllgemein === null ? null : obj.zeugnisAllgemein;
		result.zeugnisASV = (obj.zeugnisASV === undefined) ? null : obj.zeugnisASV === null ? null : obj.zeugnisASV;
		result.zeugnisLELS = (obj.zeugnisLELS === undefined) ? null : obj.zeugnisLELS === null ? null : obj.zeugnisLELS;
		result.zeugnisAUE = (obj.zeugnisAUE === undefined) ? null : obj.zeugnisAUE === null ? null : obj.zeugnisAUE;
		result.uebergangESF = (obj.uebergangESF === undefined) ? null : obj.uebergangESF === null ? null : obj.uebergangESF;
		result.foerderschwerpunkt = (obj.foerderschwerpunkt === undefined) ? null : obj.foerderschwerpunkt === null ? null : obj.foerderschwerpunkt;
		result.versetzungsentscheidung = (obj.versetzungsentscheidung === undefined) ? null : obj.versetzungsentscheidung === null ? null : obj.versetzungsentscheidung;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittBemerkungen) : string {
		let result = '{';
		result += '"zeugnisAllgemein" : ' + ((obj.zeugnisAllgemein === null) ? 'null' : JSON.stringify(obj.zeugnisAllgemein)) + ',';
		result += '"zeugnisASV" : ' + ((obj.zeugnisASV === null) ? 'null' : JSON.stringify(obj.zeugnisASV)) + ',';
		result += '"zeugnisLELS" : ' + ((obj.zeugnisLELS === null) ? 'null' : JSON.stringify(obj.zeugnisLELS)) + ',';
		result += '"zeugnisAUE" : ' + ((obj.zeugnisAUE === null) ? 'null' : JSON.stringify(obj.zeugnisAUE)) + ',';
		result += '"uebergangESF" : ' + ((obj.uebergangESF === null) ? 'null' : JSON.stringify(obj.uebergangESF)) + ',';
		result += '"foerderschwerpunkt" : ' + ((obj.foerderschwerpunkt === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
		result += '"versetzungsentscheidung" : ' + ((obj.versetzungsentscheidung === null) ? 'null' : JSON.stringify(obj.versetzungsentscheidung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittBemerkungen>) : string {
		let result = '{';
		if (obj.zeugnisAllgemein !== undefined) {
			result += '"zeugnisAllgemein" : ' + ((obj.zeugnisAllgemein === null) ? 'null' : JSON.stringify(obj.zeugnisAllgemein)) + ',';
		}
		if (obj.zeugnisASV !== undefined) {
			result += '"zeugnisASV" : ' + ((obj.zeugnisASV === null) ? 'null' : JSON.stringify(obj.zeugnisASV)) + ',';
		}
		if (obj.zeugnisLELS !== undefined) {
			result += '"zeugnisLELS" : ' + ((obj.zeugnisLELS === null) ? 'null' : JSON.stringify(obj.zeugnisLELS)) + ',';
		}
		if (obj.zeugnisAUE !== undefined) {
			result += '"zeugnisAUE" : ' + ((obj.zeugnisAUE === null) ? 'null' : JSON.stringify(obj.zeugnisAUE)) + ',';
		}
		if (obj.uebergangESF !== undefined) {
			result += '"uebergangESF" : ' + ((obj.uebergangESF === null) ? 'null' : JSON.stringify(obj.uebergangESF)) + ',';
		}
		if (obj.foerderschwerpunkt !== undefined) {
			result += '"foerderschwerpunkt" : ' + ((obj.foerderschwerpunkt === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
		}
		if (obj.versetzungsentscheidung !== undefined) {
			result += '"versetzungsentscheidung" : ' + ((obj.versetzungsentscheidung === null) ? 'null' : JSON.stringify(obj.versetzungsentscheidung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schueler_SchuelerLernabschnittBemerkungen(obj : unknown) : SchuelerLernabschnittBemerkungen {
	return obj as SchuelerLernabschnittBemerkungen;
}
