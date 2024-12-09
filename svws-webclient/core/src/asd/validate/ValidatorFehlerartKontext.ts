import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export class ValidatorFehlerartKontext extends JavaObject {

	/**
	 * ob der Validator im zebras ausgeführt werden soll.
	 */
	public zebras : boolean = false;

	/**
	 * ob der Validator im client ausgeführt werden soll.
	 */
	public svws : boolean = false;

	/**
	 * Liste der Schulformen, in denen ein Fehler vorliegt
	 */
	public muss : List<string> = new ArrayList<string>();

	/**
	 * Liste der Schulformen, in denen wahrscheinlich ein Fehler vorliegt
	 */
	public kann : List<string> = new ArrayList<string>();

	/**
	 * Liste der Schulformen, in denen ein Hinweise auf einen möglichen Fehler erfolgt
	 */
	public hinweis : List<string> = new ArrayList<string>();

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
		if (obj.muss !== undefined) {
			for (const elem of obj.muss) {
				result.muss.add(elem);
			}
		}
		if (obj.kann !== undefined) {
			for (const elem of obj.kann) {
				result.kann.add(elem);
			}
		}
		if (obj.hinweis !== undefined) {
			for (const elem of obj.hinweis) {
				result.hinweis.add(elem);
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
		result += '"muss" : [ ';
		for (let i = 0; i < obj.muss.size(); i++) {
			const elem = obj.muss.get(i);
			result += '"' + elem + '"';
			if (i < obj.muss.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kann" : [ ';
		for (let i = 0; i < obj.kann.size(); i++) {
			const elem = obj.kann.get(i);
			result += '"' + elem + '"';
			if (i < obj.kann.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"hinweis" : [ ';
		for (let i = 0; i < obj.hinweis.size(); i++) {
			const elem = obj.hinweis.get(i);
			result += '"' + elem + '"';
			if (i < obj.hinweis.size() - 1)
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
		if (obj.muss !== undefined) {
			result += '"muss" : [ ';
			for (let i = 0; i < obj.muss.size(); i++) {
				const elem = obj.muss.get(i);
				result += '"' + elem + '"';
				if (i < obj.muss.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kann !== undefined) {
			result += '"kann" : [ ';
			for (let i = 0; i < obj.kann.size(); i++) {
				const elem = obj.kann.get(i);
				result += '"' + elem + '"';
				if (i < obj.kann.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.hinweis !== undefined) {
			result += '"hinweis" : [ ';
			for (let i = 0; i < obj.hinweis.size(); i++) {
				const elem = obj.hinweis.get(i);
				result += '"' + elem + '"';
				if (i < obj.hinweis.size() - 1)
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
