import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymFach } from '../../../../core/data/bk/abi/BKGymFach';
import { BKGymLeistungenFachHalbjahr } from '../../../../core/data/bk/abi/BKGymLeistungenFachHalbjahr';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class BKGymLeistungenFach extends JavaObject {

	/**
	 * Das Fach des beruflichen Gymnasiums, welches dieser Leistung zugeordnet ist.
	 */
	public fach : BKGymFach | null = new BKGymFach();

	/**
	 * Die Nummer des Abiturfaches, sofern es sich um ein Abiturfach handelt - ansonsten null
	 */
	public abiturfach : number | null = null;

	/**
	 * Gibt an, ob es sich um eine neu einsetzende Fremdsprache handelt oder nicht.
	 */
	public istFSNeu : boolean = false;

	/**
	 * Die einzelnen Belegungen dieses Faches
	 */
	public readonly belegungen : List<BKGymLeistungenFachHalbjahr> = new ArrayList<BKGymLeistungenFachHalbjahr>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.abi.BKGymLeistungenFach';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.abi.BKGymLeistungenFach'].includes(name);
	}

	public static class = new Class<BKGymLeistungenFach>('de.svws_nrw.core.data.bk.abi.BKGymLeistungenFach');

	public static transpilerFromJSON(json : string): BKGymLeistungenFach {
		const obj = JSON.parse(json) as Partial<BKGymLeistungenFach>;
		const result = new BKGymLeistungenFach();
		result.fach = ((obj.fach === undefined) || (obj.fach === null)) ? null : BKGymFach.transpilerFromJSON(JSON.stringify(obj.fach));
		result.abiturfach = (obj.abiturfach === undefined) ? null : obj.abiturfach === null ? null : obj.abiturfach;
		if (obj.istFSNeu === undefined)
			throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		if (obj.belegungen !== undefined) {
			for (const elem of obj.belegungen) {
				result.belegungen.add(BKGymLeistungenFachHalbjahr.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKGymLeistungenFach) : string {
		let result = '{';
		result += '"fach" : ' + ((obj.fach === null) ? 'null' : BKGymFach.transpilerToJSON(obj.fach)) + ',';
		result += '"abiturfach" : ' + ((obj.abiturfach === null) ? 'null' : obj.abiturfach.toString()) + ',';
		result += '"istFSNeu" : ' + obj.istFSNeu.toString() + ',';
		result += '"belegungen" : [ ';
		for (let i = 0; i < obj.belegungen.size(); i++) {
			const elem = obj.belegungen.get(i);
			result += BKGymLeistungenFachHalbjahr.transpilerToJSON(elem);
			if (i < obj.belegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKGymLeistungenFach>) : string {
		let result = '{';
		if (obj.fach !== undefined) {
			result += '"fach" : ' + ((obj.fach === null) ? 'null' : BKGymFach.transpilerToJSON(obj.fach)) + ',';
		}
		if (obj.abiturfach !== undefined) {
			result += '"abiturfach" : ' + ((obj.abiturfach === null) ? 'null' : obj.abiturfach.toString()) + ',';
		}
		if (obj.istFSNeu !== undefined) {
			result += '"istFSNeu" : ' + obj.istFSNeu.toString() + ',';
		}
		if (obj.belegungen !== undefined) {
			result += '"belegungen" : [ ';
			for (let i = 0; i < obj.belegungen.size(); i++) {
				const elem = obj.belegungen.get(i);
				result += BKGymLeistungenFachHalbjahr.transpilerToJSON(elem);
				if (i < obj.belegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_bk_abi_BKGymLeistungenFach(obj : unknown) : BKGymLeistungenFach {
	return obj as BKGymLeistungenFach;
}
