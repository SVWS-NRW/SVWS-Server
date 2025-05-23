import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { GostLeistungenFachbelegung } from '../../../core/data/gost/GostLeistungenFachbelegung';

export class GostLeistungenFachwahl extends JavaObject {

	/**
	 * Das Fach der Gymnasialen Oberstufe, welches dieser Fachwahl zugeordnet ist.
	 */
	public fach : GostFach | null = new GostFach();

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
	public readonly belegungen : List<GostLeistungenFachbelegung> = new ArrayList<GostLeistungenFachbelegung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLeistungenFachwahl';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLeistungenFachwahl'].includes(name);
	}

	public static class = new Class<GostLeistungenFachwahl>('de.svws_nrw.core.data.gost.GostLeistungenFachwahl');

	public static transpilerFromJSON(json : string): GostLeistungenFachwahl {
		const obj = JSON.parse(json) as Partial<GostLeistungenFachwahl>;
		const result = new GostLeistungenFachwahl();
		result.fach = ((obj.fach === undefined) || (obj.fach === null)) ? null : GostFach.transpilerFromJSON(JSON.stringify(obj.fach));
		result.abiturfach = (obj.abiturfach === undefined) ? null : obj.abiturfach === null ? null : obj.abiturfach;
		if (obj.istFSNeu === undefined)
			throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		if (obj.belegungen !== undefined) {
			for (const elem of obj.belegungen) {
				result.belegungen.add(GostLeistungenFachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLeistungenFachwahl) : string {
		let result = '{';
		result += '"fach" : ' + ((obj.fach === null) ? 'null' : GostFach.transpilerToJSON(obj.fach)) + ',';
		result += '"abiturfach" : ' + ((obj.abiturfach === null) ? 'null' : obj.abiturfach.toString()) + ',';
		result += '"istFSNeu" : ' + obj.istFSNeu.toString() + ',';
		result += '"belegungen" : [ ';
		for (let i = 0; i < obj.belegungen.size(); i++) {
			const elem = obj.belegungen.get(i);
			result += GostLeistungenFachbelegung.transpilerToJSON(elem);
			if (i < obj.belegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLeistungenFachwahl>) : string {
		let result = '{';
		if (obj.fach !== undefined) {
			result += '"fach" : ' + ((obj.fach === null) ? 'null' : GostFach.transpilerToJSON(obj.fach)) + ',';
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
				result += GostLeistungenFachbelegung.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_gost_GostLeistungenFachwahl(obj : unknown) : GostLeistungenFachwahl {
	return obj as GostLeistungenFachwahl;
}
