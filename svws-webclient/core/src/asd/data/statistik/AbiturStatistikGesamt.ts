import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class AbiturStatistikGesamt extends JavaObject {

	/**
	 * Die Liste der Statistikkuerzel der Abiturfächer.
	 */
	public abifach: List<string> = new ArrayList<string>();

	/**
	 * Die Abiturnote.
	 */
	public note: string | null = null;

	/**
	 * Gibt an, ob der Schüler zum Abitur zugelassen wurde.
	 */
	public istZugelassen: boolean = false;

	/**
	 * Gibt an, ob das Abitur bestanden wurde.
	 */
	public hatBestanden: boolean = false;

	/**
	 * Gibt an, ob der Schüler freiwillig von der Abiturprüfung zurückgetreten ist.
	 */
	public istZurueckgetreten: boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.AbiturStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.AbiturStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<AbiturStatistikGesamt>('de.svws_nrw.asd.data.statistik.AbiturStatistikGesamt');

	public static transpilerFromJSON(json: string): AbiturStatistikGesamt {
		const obj = JSON.parse(json) as Partial<AbiturStatistikGesamt>;
		const result = new AbiturStatistikGesamt();
		if (obj.abifach !== undefined) {
			for (const elem of obj.abifach) {
				result.abifach.add(elem);
			}
		}
		result.note = (obj.note === undefined) ? null : obj.note === null ? null : obj.note;
		if (obj.istZugelassen === undefined)
			throw new Error('invalid json format, missing attribute istZugelassen');
		result.istZugelassen = obj.istZugelassen;
		if (obj.hatBestanden === undefined)
			throw new Error('invalid json format, missing attribute hatBestanden');
		result.hatBestanden = obj.hatBestanden;
		if (obj.istZurueckgetreten === undefined)
			throw new Error('invalid json format, missing attribute istZurueckgetreten');
		result.istZurueckgetreten = obj.istZurueckgetreten;
		return result;
	}

	public static transpilerToJSON(obj: AbiturStatistikGesamt): string {
		let result = '{';
		result += '"abifach" : [ ';
		for (let i = 0; i < obj.abifach.size(); i++) {
			const elem = obj.abifach.get(i);
			result += '"' + elem + '"';
			if (i < obj.abifach.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"note" : ' + ((obj.note === null) ? 'null' : JSON.stringify(obj.note)) + ',';
		result += '"istZugelassen" : ' + obj.istZugelassen.toString() + ',';
		result += '"hatBestanden" : ' + obj.hatBestanden.toString() + ',';
		result += '"istZurueckgetreten" : ' + obj.istZurueckgetreten.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AbiturStatistikGesamt>): string {
		let result = '{';
		if (obj.abifach !== undefined) {
			result += '"abifach" : [ ';
			for (let i = 0; i < obj.abifach.size(); i++) {
				const elem = obj.abifach.get(i);
				result += '"' + elem + '"';
				if (i < obj.abifach.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.note !== undefined) {
			result += '"note" : ' + ((obj.note === null) ? 'null' : JSON.stringify(obj.note)) + ',';
		}
		if (obj.istZugelassen !== undefined) {
			result += '"istZugelassen" : ' + obj.istZugelassen.toString() + ',';
		}
		if (obj.hatBestanden !== undefined) {
			result += '"hatBestanden" : ' + obj.hatBestanden.toString() + ',';
		}
		if (obj.istZurueckgetreten !== undefined) {
			result += '"istZurueckgetreten" : ' + obj.istZurueckgetreten.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_AbiturStatistikGesamt(obj: unknown): AbiturStatistikGesamt {
	return obj as AbiturStatistikGesamt;
}
