import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class AnkreuzkompetenzKonfiguration extends JavaObject {

	/**
	 * Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen die zu verwendenden Texte an (hier mit einer Verschiebung von 1 zum Array-Index).
	 */
	public textStufen: Array<string | null> = Array(5).fill(null);

	/**
	 * Der für die frei definierbare Zeugnisrubrik "Sonstiges" zu verwendenden Text.
	 */
	public textSonstiges: string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration'].includes(name);
	}

	public static readonly class = new Class<AnkreuzkompetenzKonfiguration>('de.svws_nrw.core.data.kataloge.AnkreuzkompetenzKonfiguration');

	public static transpilerFromJSON(json: string): AnkreuzkompetenzKonfiguration {
		const obj = JSON.parse(json) as Partial<AnkreuzkompetenzKonfiguration>;
		const result = new AnkreuzkompetenzKonfiguration();
		if (obj.textStufen !== undefined) {
			for (let i = 0; i < obj.textStufen.length; i++) {
				result.textStufen[i] = obj.textStufen[i] === null ? null : obj.textStufen[i];
			}
		}
		result.textSonstiges = (obj.textSonstiges === undefined) ? null : obj.textSonstiges === null ? null : obj.textSonstiges;
		return result;
	}

	public static transpilerToJSON(obj: AnkreuzkompetenzKonfiguration): string {
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AnkreuzkompetenzKonfiguration>): string {
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kataloge_AnkreuzkompetenzKonfiguration(obj: unknown): AnkreuzkompetenzKonfiguration {
	return obj as AnkreuzkompetenzKonfiguration;
}
