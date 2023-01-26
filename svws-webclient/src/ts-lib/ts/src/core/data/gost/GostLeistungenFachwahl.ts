import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { GostLeistungenFachbelegung, cast_de_nrw_schule_svws_core_data_gost_GostLeistungenFachbelegung } from '../../../core/data/gost/GostLeistungenFachbelegung';

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
	public readonly belegungen : Vector<GostLeistungenFachbelegung> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostLeistungenFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLeistungenFachwahl {
		const obj = JSON.parse(json);
		const result = new GostLeistungenFachwahl();
		result.fach = ((typeof obj.fach === "undefined") || (obj.fach === null)) ? null : GostFach.transpilerFromJSON(JSON.stringify(obj.fach));
		result.abiturfach = typeof obj.abiturfach === "undefined" ? null : obj.abiturfach === null ? null : obj.abiturfach;
		if (typeof obj.istFSNeu === "undefined")
			 throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		if (!!obj.belegungen) {
			for (let elem of obj.belegungen) {
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
			for (let i : number = 0; i < obj.belegungen.size(); i++) {
				let elem = obj.belegungen.get(i);
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
				for (let i : number = 0; i < obj.belegungen.size(); i++) {
					let elem = obj.belegungen.get(i);
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

export function cast_de_nrw_schule_svws_core_data_gost_GostLeistungenFachwahl(obj : unknown) : GostLeistungenFachwahl {
	return obj as GostLeistungenFachwahl;
}
