import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsalgorithmusErgebnis extends JavaObject {

	/**
	 * gibt an, ob der Algorithmus erfolgreich durchgeführt wurde
	 */
	public erfolgreich: boolean = false;

	/**
	 * eine Liste der vorgenommenen Markierungen von Halbjahres-Belegungen in der Qualifikationsphase
	 */
	public markierungen: List<BKGymAbiturMarkierungsalgorithmusMarkierung> = new ArrayList<BKGymAbiturMarkierungsalgorithmusMarkierung>();

	/**
	 * Ein Log, der den Ablauf des Markierungsalgorithmus verdeutlicht
	 */
	public log: List<string> = new ArrayList<string>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsalgorithmusErgebnis>('de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis');

	public static transpilerFromJSON(json: string): BKGymAbiturMarkierungsalgorithmusErgebnis {
		const obj = JSON.parse(json) as Partial<BKGymAbiturMarkierungsalgorithmusErgebnis>;
		const result = new BKGymAbiturMarkierungsalgorithmusErgebnis();
		if (obj.erfolgreich === undefined)
			throw new Error('invalid json format, missing attribute erfolgreich');
		result.erfolgreich = obj.erfolgreich;
		if (obj.markierungen !== undefined) {
			for (const elem of obj.markierungen) {
				result.markierungen.add(BKGymAbiturMarkierungsalgorithmusMarkierung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.log !== undefined) {
			for (const elem of obj.log) {
				result.log.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: BKGymAbiturMarkierungsalgorithmusErgebnis): string {
		let result = '{';
		result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
		result += '"markierungen" : [ ';
		for (let i = 0; i < obj.markierungen.size(); i++) {
			const elem = obj.markierungen.get(i);
			result += BKGymAbiturMarkierungsalgorithmusMarkierung.transpilerToJSON(elem);
			if (i < obj.markierungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"log" : [ ';
		for (let i = 0; i < obj.log.size(); i++) {
			const elem = obj.log.get(i);
			result += '"' + elem + '"';
			if (i < obj.log.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<BKGymAbiturMarkierungsalgorithmusErgebnis>): string {
		let result = '{';
		if (obj.erfolgreich !== undefined) {
			result += '"erfolgreich" : ' + obj.erfolgreich.toString() + ',';
		}
		if (obj.markierungen !== undefined) {
			result += '"markierungen" : [ ';
			for (let i = 0; i < obj.markierungen.size(); i++) {
				const elem = obj.markierungen.get(i);
				result += BKGymAbiturMarkierungsalgorithmusMarkierung.transpilerToJSON(elem);
				if (i < obj.markierungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.log !== undefined) {
			result += '"log" : [ ';
			for (let i = 0; i < obj.log.size(); i++) {
				const elem = obj.log.get(i);
				result += '"' + elem + '"';
				if (i < obj.log.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_abi_BKGymAbiturMarkierungsalgorithmusErgebnis(obj: unknown): BKGymAbiturMarkierungsalgorithmusErgebnis {
	return obj as BKGymAbiturMarkierungsalgorithmusErgebnis;
}
