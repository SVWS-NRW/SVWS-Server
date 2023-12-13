import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
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
	public readonly belegungen : List<GostLeistungenFachbelegung> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLeistungenFachwahl';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLeistungenFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLeistungenFachwahl {
		const obj = JSON.parse(json);
		const result = new GostLeistungenFachwahl();
		result.fach = ((typeof obj.fach === "undefined") || (obj.fach === null)) ? null : GostFach.transpilerFromJSON(JSON.stringify(obj.fach));
		result.abiturfach = typeof obj.abiturfach === "undefined" ? null : obj.abiturfach === null ? null : obj.abiturfach;
		if (typeof obj.istFSNeu === "undefined")
			 throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		if ((obj.belegungen !== undefined) && (obj.belegungen !== null)) {
			for (const elem of obj.belegungen) {
				result.belegungen?.add(GostLeistungenFachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLeistungenFachwahl) : string {
		let result = '{';
		result += '"fach" : ' + ((!obj.fach) ? 'null' : GostFach.transpilerToJSON(obj.fach)) + ',';
		result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach) + ',';
		result += '"istFSNeu" : ' + obj.istFSNeu + ',';
		if (!obj.belegungen) {
			result += '"belegungen" : []';
		} else {
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

	public static transpilerToJSONPatch(obj : Partial<GostLeistungenFachwahl>) : string {
		let result = '{';
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + ((!obj.fach) ? 'null' : GostFach.transpilerToJSON(obj.fach)) + ',';
		}
		if (typeof obj.abiturfach !== "undefined") {
			result += '"abiturfach" : ' + ((!obj.abiturfach) ? 'null' : obj.abiturfach) + ',';
		}
		if (typeof obj.istFSNeu !== "undefined") {
			result += '"istFSNeu" : ' + obj.istFSNeu + ',';
		}
		if (typeof obj.belegungen !== "undefined") {
			if (!obj.belegungen) {
				result += '"belegungen" : []';
			} else {
				result += '"belegungen" : [ ';
				for (let i = 0; i < obj.belegungen.size(); i++) {
					const elem = obj.belegungen.get(i);
					result += GostLeistungenFachbelegung.transpilerToJSON(elem);
					if (i < obj.belegungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostLeistungenFachwahl(obj : unknown) : GostLeistungenFachwahl {
	return obj as GostLeistungenFachwahl;
}
