import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { ValidatorFehlerartKontextPruefschritt } from '../../asd/validate/ValidatorFehlerartKontextPruefschritt';

export class ValidatorFehlerartKontext extends JavaObject {

	/**
	 * Gibt an, ob der Validator im zebras ausgeführt werden soll.
	 */
	public zebras : boolean = false;

	/**
	 * Gibt an, ob der Validator im client ausgeführt werden soll.
	 */
	public svws : boolean = false;

	/**
	 * der Präfix-Teil des ASD-Fehlercodes
	 */
	public praefix : string = "";

	/**
	 * Die Liste mit den Zuordnungen der Fehlerarten für die einzelnen Prüfschritte eines Validators
	 */
	public pruefschritte : List<ValidatorFehlerartKontextPruefschritt> = new ArrayList<ValidatorFehlerartKontextPruefschritt>();

	/**
	 * Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen ValidatorFehlerartKontext mit Standardwerten
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorFehlerartKontext';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorFehlerartKontext'].includes(name);
	}

	public static class = new Class<ValidatorFehlerartKontext>('de.svws_nrw.asd.validate.ValidatorFehlerartKontext');

	public static transpilerFromJSON(json : string): ValidatorFehlerartKontext {
		const obj = JSON.parse(json) as Partial<ValidatorFehlerartKontext>;
		const result = new ValidatorFehlerartKontext();
		if (obj.zebras === undefined)
			throw new Error('invalid json format, missing attribute zebras');
		result.zebras = obj.zebras;
		if (obj.svws === undefined)
			throw new Error('invalid json format, missing attribute svws');
		result.svws = obj.svws;
		if (obj.praefix === undefined)
			throw new Error('invalid json format, missing attribute praefix');
		result.praefix = obj.praefix;
		if (obj.pruefschritte !== undefined) {
			for (const elem of obj.pruefschritte) {
				result.pruefschritte.add(ValidatorFehlerartKontextPruefschritt.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : ValidatorFehlerartKontext) : string {
		let result = '{';
		result += '"zebras" : ' + obj.zebras.toString() + ',';
		result += '"svws" : ' + obj.svws.toString() + ',';
		result += '"praefix" : ' + JSON.stringify(obj.praefix) + ',';
		result += '"pruefschritte" : [ ';
		for (let i = 0; i < obj.pruefschritte.size(); i++) {
			const elem = obj.pruefschritte.get(i);
			result += ValidatorFehlerartKontextPruefschritt.transpilerToJSON(elem);
			if (i < obj.pruefschritte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ValidatorFehlerartKontext>) : string {
		let result = '{';
		if (obj.zebras !== undefined) {
			result += '"zebras" : ' + obj.zebras.toString() + ',';
		}
		if (obj.svws !== undefined) {
			result += '"svws" : ' + obj.svws.toString() + ',';
		}
		if (obj.praefix !== undefined) {
			result += '"praefix" : ' + JSON.stringify(obj.praefix) + ',';
		}
		if (obj.pruefschritte !== undefined) {
			result += '"pruefschritte" : [ ';
			for (let i = 0; i < obj.pruefschritte.size(); i++) {
				const elem = obj.pruefschritte.get(i);
				result += ValidatorFehlerartKontextPruefschritt.transpilerToJSON(elem);
				if (i < obj.pruefschritte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
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

export function cast_de_svws_nrw_asd_validate_ValidatorFehlerartKontext(obj : unknown) : ValidatorFehlerartKontext {
	return obj as ValidatorFehlerartKontext;
}
