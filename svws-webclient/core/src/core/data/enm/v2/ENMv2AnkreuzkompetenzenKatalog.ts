import { JavaObject } from '../../../../java/lang/JavaObject';
import { ENMv2Ankreuzkompetenz, cast_de_svws_nrw_core_data_enm_v2_ENMv2Ankreuzkompetenz } from '../../../../core/data/enm/v2/ENMv2Ankreuzkompetenz';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ENMv2AnkreuzkompetenzenKatalog extends JavaObject {

	/**
	 * Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen die zu verwendenden Texte an (hier mit einer Verschiebung von 1 zum Array-Index).
	 */
	public textStufen: Array<string | null> = Array(5).fill(null);

	/**
	 * Der für die frei definierbare Zeugnisrubrik "Sonstiges" zu verwendenden Text.
	 */
	public textSonstiges: string | null = null;

	/**
	 * Die Katalog-Einträge für die Ankreuzkompetenzen, die in der Notendatei enthalten sind.
	 */
	public kompetenzen: List<ENMv2Ankreuzkompetenz> = new ArrayList<ENMv2Ankreuzkompetenz>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2AnkreuzkompetenzenKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2AnkreuzkompetenzenKatalog'].includes(name);
	}

	public static readonly class = new Class<ENMv2AnkreuzkompetenzenKatalog>('de.svws_nrw.core.data.enm.v2.ENMv2AnkreuzkompetenzenKatalog');

	public static transpilerFromJSON(json: string): ENMv2AnkreuzkompetenzenKatalog {
		const obj = JSON.parse(json) as Partial<ENMv2AnkreuzkompetenzenKatalog>;
		const result = new ENMv2AnkreuzkompetenzenKatalog();
		if (obj.textStufen !== undefined) {
			for (let i = 0; i < obj.textStufen.length; i++) {
				result.textStufen[i] = obj.textStufen[i] === null ? null : obj.textStufen[i];
			}
		}
		result.textSonstiges = (obj.textSonstiges === undefined) ? null : obj.textSonstiges === null ? null : obj.textSonstiges;
		if (obj.kompetenzen !== undefined) {
			for (const elem of obj.kompetenzen) {
				result.kompetenzen.add(ENMv2Ankreuzkompetenz.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: ENMv2AnkreuzkompetenzenKatalog): string {
		let result = '{';
		result += '"textStufen" : [ ';
		for (let i = 0; i < obj.textStufen.length; i++) {
			const elem = obj.textStufen[i];
			result += (elem === null) ? null : '"' + elem + '"';
			if (i < obj.textStufen.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"textSonstiges" : ' + ((obj.textSonstiges === null) ? 'null' : JSON.stringify(obj.textSonstiges)) + ',';
		result += '"kompetenzen" : [ ';
		for (let i = 0; i < obj.kompetenzen.size(); i++) {
			const elem = obj.kompetenzen.get(i);
			result += ENMv2Ankreuzkompetenz.transpilerToJSON(elem);
			if (i < obj.kompetenzen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2AnkreuzkompetenzenKatalog>): string {
		let result = '{';
		if (obj.textStufen !== undefined) {
			const a = obj.textStufen;
			result += '"textStufen" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += (elem === null) ? null : '"' + elem + '"';
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.textSonstiges !== undefined) {
			result += '"textSonstiges" : ' + ((obj.textSonstiges === null) ? 'null' : JSON.stringify(obj.textSonstiges)) + ',';
		}
		if (obj.kompetenzen !== undefined) {
			result += '"kompetenzen" : [ ';
			for (let i = 0; i < obj.kompetenzen.size(); i++) {
				const elem = obj.kompetenzen.get(i);
				result += ENMv2Ankreuzkompetenz.transpilerToJSON(elem);
				if (i < obj.kompetenzen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2AnkreuzkompetenzenKatalog(obj: unknown): ENMv2AnkreuzkompetenzenKatalog {
	return obj as ENMv2AnkreuzkompetenzenKatalog;
}
