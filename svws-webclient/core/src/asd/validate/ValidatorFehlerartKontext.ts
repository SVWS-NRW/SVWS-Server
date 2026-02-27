import { JavaObject } from '../../java/lang/JavaObject';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';

export class ValidatorFehlerartKontext extends JavaObject {

	/**
	 * Gibt an, ob der Validator im zebras ausgeführt werden soll.
	 */
	public zebras: boolean = false;

	/**
	 * Gibt an, ob der Validator im client ausgeführt werden soll.
	 */
	public svws: boolean = false;

	/**
	 * der Präfix-Teil des ASD-Fehlercodes
	 */
	public praefix: string = "";

	/**
	 * Liste der Schulformen, in denen bei dem Prüfschritt ein Fehler vorliegt
	 */
	public muss: List<string> = new ArrayList<string>();

	/**
	 * Liste der Schulformen, in denen bei dem Prüfschritt wahrscheinlich ein Fehler vorliegt
	 */
	public kann: List<string> = new ArrayList<string>();

	/**
	 * Liste der Schulformen, in denen bei dem Prüfschritt ein Hinweis auf einen möglichen Fehler erfolgt
	 */
	public hinweis: List<string> = new ArrayList<string>();

	/**
	 * Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt.
	 */
	public gueltigVon: number | null = null;

	/**
	 * Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt.
	 */
	public gueltigBis: number | null = null;

	/**
	 * Das alte Kürzel des Prüfschritts
	 */
	public altes_kuerzel: string | null = "";

	/**
	 * Der Zweig/Bereich der Prüfung
	 */
	public zweig: string | null = "";

	/**
	 * Die beteiligten DTOs
	 */
	public dtos: string | null = "";

	/**
	 * UI-Bereich für die Ausführung
	 */
	public ausfuehrungsbereich_ui: string | null = "";

	/**
	 * UI-Bereich für die Anzeige
	 */
	public anzeigebereich_ui: string | null = "";

	/**
	 * Der Fehlertext
	 */
	public text: string | null = "";

	/**
	 * Zusätzliche Erläuterungen
	 */
	public erlaeuterung: string | null = "";

	/**
	 * Die fachliche Bedingung als String
	 */
	public bedingung: string | null = "";

	/**
	 * Vorbedingungen für die Prüfung
	 */
	public vorbedingung: List<string> = new ArrayList<string>();


	/**
	 * Erstellt einen ValidatorFehlerartKontext mit Standardwerten
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorFehlerartKontext';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorFehlerartKontext'].includes(name);
	}

	public static readonly class = new Class<ValidatorFehlerartKontext>('de.svws_nrw.asd.validate.ValidatorFehlerartKontext');

	public static transpilerFromJSON(json: string): ValidatorFehlerartKontext {
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
		result.altes_kuerzel = (obj.altes_kuerzel === undefined) ? null : obj.altes_kuerzel === null ? null : obj.altes_kuerzel;
		result.zweig = (obj.zweig === undefined) ? null : obj.zweig === null ? null : obj.zweig;
		result.dtos = (obj.dtos === undefined) ? null : obj.dtos === null ? null : obj.dtos;
		result.ausfuehrungsbereich_ui = (obj.ausfuehrungsbereich_ui === undefined) ? null : obj.ausfuehrungsbereich_ui === null ? null : obj.ausfuehrungsbereich_ui;
		result.anzeigebereich_ui = (obj.anzeigebereich_ui === undefined) ? null : obj.anzeigebereich_ui === null ? null : obj.anzeigebereich_ui;
		result.text = (obj.text === undefined) ? null : obj.text === null ? null : obj.text;
		result.erlaeuterung = (obj.erlaeuterung === undefined) ? null : obj.erlaeuterung === null ? null : obj.erlaeuterung;
		result.bedingung = (obj.bedingung === undefined) ? null : obj.bedingung === null ? null : obj.bedingung;
		if (obj.vorbedingung !== undefined) {
			for (const elem of obj.vorbedingung) {
				result.vorbedingung.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ValidatorFehlerartKontext): string {
		let result = '{';
		result += '"zebras" : ' + obj.zebras.toString() + ',';
		result += '"svws" : ' + obj.svws.toString() + ',';
		result += '"praefix" : ' + JSON.stringify(obj.praefix) + ',';
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
		result += '"altes_kuerzel" : ' + ((obj.altes_kuerzel === null) ? 'null' : JSON.stringify(obj.altes_kuerzel)) + ',';
		result += '"zweig" : ' + ((obj.zweig === null) ? 'null' : JSON.stringify(obj.zweig)) + ',';
		result += '"dtos" : ' + ((obj.dtos === null) ? 'null' : JSON.stringify(obj.dtos)) + ',';
		result += '"ausfuehrungsbereich_ui" : ' + ((obj.ausfuehrungsbereich_ui === null) ? 'null' : JSON.stringify(obj.ausfuehrungsbereich_ui)) + ',';
		result += '"anzeigebereich_ui" : ' + ((obj.anzeigebereich_ui === null) ? 'null' : JSON.stringify(obj.anzeigebereich_ui)) + ',';
		result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		result += '"erlaeuterung" : ' + ((obj.erlaeuterung === null) ? 'null' : JSON.stringify(obj.erlaeuterung)) + ',';
		result += '"bedingung" : ' + ((obj.bedingung === null) ? 'null' : JSON.stringify(obj.bedingung)) + ',';
		result += '"vorbedingung" : [ ';
		for (let i = 0; i < obj.vorbedingung.size(); i++) {
			const elem = obj.vorbedingung.get(i);
			result += '"' + elem + '"';
			if (i < obj.vorbedingung.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ValidatorFehlerartKontext>): string {
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
		if (obj.altes_kuerzel !== undefined) {
			result += '"altes_kuerzel" : ' + ((obj.altes_kuerzel === null) ? 'null' : JSON.stringify(obj.altes_kuerzel)) + ',';
		}
		if (obj.zweig !== undefined) {
			result += '"zweig" : ' + ((obj.zweig === null) ? 'null' : JSON.stringify(obj.zweig)) + ',';
		}
		if (obj.dtos !== undefined) {
			result += '"dtos" : ' + ((obj.dtos === null) ? 'null' : JSON.stringify(obj.dtos)) + ',';
		}
		if (obj.ausfuehrungsbereich_ui !== undefined) {
			result += '"ausfuehrungsbereich_ui" : ' + ((obj.ausfuehrungsbereich_ui === null) ? 'null' : JSON.stringify(obj.ausfuehrungsbereich_ui)) + ',';
		}
		if (obj.anzeigebereich_ui !== undefined) {
			result += '"anzeigebereich_ui" : ' + ((obj.anzeigebereich_ui === null) ? 'null' : JSON.stringify(obj.anzeigebereich_ui)) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + ((obj.text === null) ? 'null' : JSON.stringify(obj.text)) + ',';
		}
		if (obj.erlaeuterung !== undefined) {
			result += '"erlaeuterung" : ' + ((obj.erlaeuterung === null) ? 'null' : JSON.stringify(obj.erlaeuterung)) + ',';
		}
		if (obj.bedingung !== undefined) {
			result += '"bedingung" : ' + ((obj.bedingung === null) ? 'null' : JSON.stringify(obj.bedingung)) + ',';
		}
		if (obj.vorbedingung !== undefined) {
			result += '"vorbedingung" : [ ';
			for (let i = 0; i < obj.vorbedingung.size(); i++) {
				const elem = obj.vorbedingung.get(i);
				result += '"' + elem + '"';
				if (i < obj.vorbedingung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_validate_ValidatorFehlerartKontext(obj: unknown): ValidatorFehlerartKontext {
	return obj as ValidatorFehlerartKontext;
}
