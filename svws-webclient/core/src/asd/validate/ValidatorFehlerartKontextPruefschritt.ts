import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export class ValidatorFehlerartKontextPruefschritt extends JavaObject {

	/**
	 * Die Nummer des Prüfschrittes, auf welchen sich die Definition bezieht (-1 für einen Default für alle Prüfschritte eines Validators)
	 */
	public nummer : number = -1;

	/**
	 * Liste der Schulformen, in denen bei dem Prüfschritt ein Fehler vorliegt
	 */
	public muss : List<string> = new ArrayList<string>();

	/**
	 * Liste der Schulformen, in denen bei dem Prüfschritt wahrscheinlich ein Fehler vorliegt
	 */
	public kann : List<string> = new ArrayList<string>();

	/**
	 * Liste der Schulformen, in denen bei dem Prüfschritt ein Hinweis auf einen möglichen Fehler erfolgt
	 */
	public hinweis : List<string> = new ArrayList<string>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorFehlerartKontextPruefschritt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorFehlerartKontextPruefschritt'].includes(name);
	}

	public static class = new Class<ValidatorFehlerartKontextPruefschritt>('de.svws_nrw.asd.validate.ValidatorFehlerartKontextPruefschritt');

	public static transpilerFromJSON(json : string): ValidatorFehlerartKontextPruefschritt {
		const obj = JSON.parse(json) as Partial<ValidatorFehlerartKontextPruefschritt>;
		const result = new ValidatorFehlerartKontextPruefschritt();
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
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
		return result;
	}

	public static transpilerToJSON(obj : ValidatorFehlerartKontextPruefschritt) : string {
		let result = '{';
		result += '"nummer" : ' + obj.nummer.toString() + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ValidatorFehlerartKontextPruefschritt>) : string {
		let result = '{';
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer.toString() + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_validate_ValidatorFehlerartKontextPruefschritt(obj : unknown) : ValidatorFehlerartKontextPruefschritt {
	return obj as ValidatorFehlerartKontextPruefschritt;
}
