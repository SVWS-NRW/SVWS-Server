import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KursListeEintrag extends JavaObject {

	public id : number = 0;

	public idSchuljahresabschnitt : number = 0;

	public kuerzel : String = "";

	public idJahrgaenge : Vector<Number> = new Vector();

	public idFach : number = 0;

	public lehrer : Number | null = null;

	public sortierung : number = 0;

	public istSichtbar : boolean = false;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kurse.KursListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursListeEintrag {
		const obj = JSON.parse(json);
		const result = new KursListeEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = String(obj.kuerzel);
		if (!!obj.idJahrgaenge) {
			for (let elem of obj.idJahrgaenge) {
				result.idJahrgaenge?.add(Number(elem));
			}
		}
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		result.lehrer = typeof obj.lehrer === "undefined" ? null : obj.lehrer === null ? null : Number(obj.lehrer);
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		return result;
	}

	public static transpilerToJSON(obj : KursListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		if (!obj.idJahrgaenge) {
			result += '"idJahrgaenge" : []';
		} else {
			result += '"idJahrgaenge" : [ ';
			for (let i : number = 0; i < obj.idJahrgaenge.size(); i++) {
				let elem = obj.idJahrgaenge.get(i);
				result += elem;
				if (i < obj.idJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.valueOf()) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursListeEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel.valueOf() + '"' + ',';
		}
		if (typeof obj.idJahrgaenge !== "undefined") {
			if (!obj.idJahrgaenge) {
				result += '"idJahrgaenge" : []';
			} else {
				result += '"idJahrgaenge" : [ ';
				for (let i : number = 0; i < obj.idJahrgaenge.size(); i++) {
					let elem = obj.idJahrgaenge.get(i);
					result += elem;
					if (i < obj.idJahrgaenge.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.lehrer !== "undefined") {
			result += '"lehrer" : ' + ((!obj.lehrer) ? 'null' : obj.lehrer.valueOf()) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_kurse_KursListeEintrag(obj : unknown) : KursListeEintrag {
	return obj as KursListeEintrag;
}
