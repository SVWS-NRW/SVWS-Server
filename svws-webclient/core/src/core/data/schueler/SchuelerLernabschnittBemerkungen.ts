import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerLernabschnittBemerkungen extends JavaObject {

	/**
	 * Der Text für allgemeine Zeugnisbemerkungen.
	 */
	public zeugnisAllgemein : string = "";

	/**
	 * Der Text für Zeugnisbemerkungen zum Arbeits- und Sozialverhalten.
	 */
	public zeugnisASV : string = "";

	/**
	 * Der Text für Zeugnisbemerkungen zur Lernentwicklung in Grundschulen.
	 */
	public zeugnisLELS : string = "";

	/**
	 * Der Text für Zeugnisbemerkungen zum Außerunterrichtlichen Engagement.
	 */
	public zeugnisAUE : string = "";

	/**
	 * Der Text für Empfehlung der Schulform beim Übergang von der Primarstufe in die Sekundarstufe I.
	 */
	public uebergangESF : string = "";

	/**
	 * Eine Bemerkung zum Förderschwerpunkt.
	 */
	public foerderschwerpunkt : string = "";

	/**
	 * Eine Bemerkung zur Versetzungsentscheidung.
	 */
	public versetzungsentscheidung : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerLernabschnittBemerkungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerLernabschnittBemerkungen'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerLernabschnittBemerkungen {
		const obj = JSON.parse(json) as Partial<SchuelerLernabschnittBemerkungen>;
		const result = new SchuelerLernabschnittBemerkungen();
		if (obj.zeugnisAllgemein === undefined)
			throw new Error('invalid json format, missing attribute zeugnisAllgemein');
		result.zeugnisAllgemein = obj.zeugnisAllgemein;
		if (obj.zeugnisASV === undefined)
			throw new Error('invalid json format, missing attribute zeugnisASV');
		result.zeugnisASV = obj.zeugnisASV;
		if (obj.zeugnisLELS === undefined)
			throw new Error('invalid json format, missing attribute zeugnisLELS');
		result.zeugnisLELS = obj.zeugnisLELS;
		if (obj.zeugnisAUE === undefined)
			throw new Error('invalid json format, missing attribute zeugnisAUE');
		result.zeugnisAUE = obj.zeugnisAUE;
		if (obj.uebergangESF === undefined)
			throw new Error('invalid json format, missing attribute uebergangESF');
		result.uebergangESF = obj.uebergangESF;
		if (obj.foerderschwerpunkt === undefined)
			throw new Error('invalid json format, missing attribute foerderschwerpunkt');
		result.foerderschwerpunkt = obj.foerderschwerpunkt;
		if (obj.versetzungsentscheidung === undefined)
			throw new Error('invalid json format, missing attribute versetzungsentscheidung');
		result.versetzungsentscheidung = obj.versetzungsentscheidung;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerLernabschnittBemerkungen) : string {
		let result = '{';
		result += '"zeugnisAllgemein" : ' + JSON.stringify(obj.zeugnisAllgemein) + ',';
		result += '"zeugnisASV" : ' + JSON.stringify(obj.zeugnisASV) + ',';
		result += '"zeugnisLELS" : ' + JSON.stringify(obj.zeugnisLELS) + ',';
		result += '"zeugnisAUE" : ' + JSON.stringify(obj.zeugnisAUE) + ',';
		result += '"uebergangESF" : ' + JSON.stringify(obj.uebergangESF) + ',';
		result += '"foerderschwerpunkt" : ' + JSON.stringify(obj.foerderschwerpunkt) + ',';
		result += '"versetzungsentscheidung" : ' + JSON.stringify(obj.versetzungsentscheidung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerLernabschnittBemerkungen>) : string {
		let result = '{';
		if (obj.zeugnisAllgemein !== undefined) {
			result += '"zeugnisAllgemein" : ' + JSON.stringify(obj.zeugnisAllgemein) + ',';
		}
		if (obj.zeugnisASV !== undefined) {
			result += '"zeugnisASV" : ' + JSON.stringify(obj.zeugnisASV) + ',';
		}
		if (obj.zeugnisLELS !== undefined) {
			result += '"zeugnisLELS" : ' + JSON.stringify(obj.zeugnisLELS) + ',';
		}
		if (obj.zeugnisAUE !== undefined) {
			result += '"zeugnisAUE" : ' + JSON.stringify(obj.zeugnisAUE) + ',';
		}
		if (obj.uebergangESF !== undefined) {
			result += '"uebergangESF" : ' + JSON.stringify(obj.uebergangESF) + ',';
		}
		if (obj.foerderschwerpunkt !== undefined) {
			result += '"foerderschwerpunkt" : ' + JSON.stringify(obj.foerderschwerpunkt) + ',';
		}
		if (obj.versetzungsentscheidung !== undefined) {
			result += '"versetzungsentscheidung" : ' + JSON.stringify(obj.versetzungsentscheidung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerLernabschnittBemerkungen(obj : unknown) : SchuelerLernabschnittBemerkungen {
	return obj as SchuelerLernabschnittBemerkungen;
}
