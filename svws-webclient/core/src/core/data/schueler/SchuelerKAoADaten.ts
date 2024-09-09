import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerKAoADaten extends JavaObject {

	/**
	 * Die ID der KAOA Daten in der Datenbank.
	 */
	public id : number = -1;

	/**
	 * Der Lernabschnitts des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public abschnitt : number = -1;

	/**
	 * Der Jahrgaeng des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public jahrgang : string = "";

	/**
	 * Der Kategorie des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public kategorie : number = -1;

	/**
	 * Das Merkmal des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public merkmal : number = -1;

	/**
	 * Das Zusatzmerkmal des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public zusatzmerkmal : number | null = null;

	/**
	 * Die Anschlussoption des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public anschlussoption : number | null = null;

	/**
	 * Das Berufsfeld des Schülers, zu dem diese KAOA Daten gehören.
	 */
	public berufsfeld : number | null = null;

	/**
	 * Ebene4 dieser KAOA Daten
	 */
	public ebene4 : number | null = null;

	/**
	 * Die Bemerkung zu diesen KAOA Daten.
	 */
	public bemerkung : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schueler.SchuelerKAoADaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schueler.SchuelerKAoADaten'].includes(name);
	}

	public static class = new Class<SchuelerKAoADaten>('de.svws_nrw.core.data.schueler.SchuelerKAoADaten');

	public static transpilerFromJSON(json : string): SchuelerKAoADaten {
		const obj = JSON.parse(json) as Partial<SchuelerKAoADaten>;
		const result = new SchuelerKAoADaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.jahrgang === undefined)
			throw new Error('invalid json format, missing attribute jahrgang');
		result.jahrgang = obj.jahrgang;
		if (obj.kategorie === undefined)
			throw new Error('invalid json format, missing attribute kategorie');
		result.kategorie = obj.kategorie;
		if (obj.merkmal === undefined)
			throw new Error('invalid json format, missing attribute merkmal');
		result.merkmal = obj.merkmal;
		result.zusatzmerkmal = (obj.zusatzmerkmal === undefined) ? null : obj.zusatzmerkmal === null ? null : obj.zusatzmerkmal;
		result.anschlussoption = (obj.anschlussoption === undefined) ? null : obj.anschlussoption === null ? null : obj.anschlussoption;
		result.berufsfeld = (obj.berufsfeld === undefined) ? null : obj.berufsfeld === null ? null : obj.berufsfeld;
		result.ebene4 = (obj.ebene4 === undefined) ? null : obj.ebene4 === null ? null : obj.ebene4;
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerKAoADaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		result += '"kategorie" : ' + obj.kategorie.toString() + ',';
		result += '"merkmal" : ' + obj.merkmal.toString() + ',';
		result += '"zusatzmerkmal" : ' + ((!obj.zusatzmerkmal) ? 'null' : obj.zusatzmerkmal.toString()) + ',';
		result += '"anschlussoption" : ' + ((!obj.anschlussoption) ? 'null' : obj.anschlussoption.toString()) + ',';
		result += '"berufsfeld" : ' + ((!obj.berufsfeld) ? 'null' : obj.berufsfeld.toString()) + ',';
		result += '"ebene4" : ' + ((!obj.ebene4) ? 'null' : obj.ebene4.toString()) + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerKAoADaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + JSON.stringify(obj.jahrgang) + ',';
		}
		if (obj.kategorie !== undefined) {
			result += '"kategorie" : ' + obj.kategorie.toString() + ',';
		}
		if (obj.merkmal !== undefined) {
			result += '"merkmal" : ' + obj.merkmal.toString() + ',';
		}
		if (obj.zusatzmerkmal !== undefined) {
			result += '"zusatzmerkmal" : ' + ((!obj.zusatzmerkmal) ? 'null' : obj.zusatzmerkmal.toString()) + ',';
		}
		if (obj.anschlussoption !== undefined) {
			result += '"anschlussoption" : ' + ((!obj.anschlussoption) ? 'null' : obj.anschlussoption.toString()) + ',';
		}
		if (obj.berufsfeld !== undefined) {
			result += '"berufsfeld" : ' + ((!obj.berufsfeld) ? 'null' : obj.berufsfeld.toString()) + ',';
		}
		if (obj.ebene4 !== undefined) {
			result += '"ebene4" : ' + ((!obj.ebene4) ? 'null' : obj.ebene4.toString()) + ',';
		}
		if (obj.bemerkung !== undefined) {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schueler_SchuelerKAoADaten(obj : unknown) : SchuelerKAoADaten {
	return obj as SchuelerKAoADaten;
}
