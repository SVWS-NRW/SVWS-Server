import { JavaObject } from '../../../java/lang/JavaObject';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';

export class GostLaufbahnplanungDatenFachbelegung extends JavaObject {

	/**
	 * Die ID des Faches der Gymnasialen Oberstufe, welches belegt wurde.
	 */
	public fachID : number = -1;

	/**
	 * Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null)
	 */
	public abiturFach : number | null = null;

	/**
	 * Die ID der Gost-Kursart bei den Einzelbelegungen des Faches in den Halbjahren
	 */
	public readonly kursart : Array<string | null> = Array(GostHalbjahr.maxHalbjahre).fill(null);

	/**
	 * Gibt an, ob die Einzelbelegung des Faches in den Halbjahren schriftlich ist oder nicht
	 */
	public readonly schriftlich : Array<boolean> = Array(GostHalbjahr.maxHalbjahre).fill(false);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenFachbelegung'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLaufbahnplanungDatenFachbelegung {
		const obj = JSON.parse(json);
		const result = new GostLaufbahnplanungDatenFachbelegung();
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.abiturFach = typeof obj.abiturFach === "undefined" ? null : obj.abiturFach === null ? null : obj.abiturFach;
		for (let i = 0; i < obj.kursart.length; i++) {
			result.kursart[i] = obj.kursart[i] === null ? null : obj.kursart[i];
		}
		for (let i = 0; i < obj.schriftlich.length; i++) {
			result.schriftlich[i] = obj.schriftlich[i];
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLaufbahnplanungDatenFachbelegung) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach) + ',';
		if (!obj.kursart) {
			result += '"kursart" : []';
		} else {
			result += '"kursart" : [ ';
			for (let i = 0; i < obj.kursart.length; i++) {
				const elem = obj.kursart[i];
				result += (elem == null) ? null : '"' + elem + '"';
				if (i < obj.kursart.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schriftlich) {
			result += '"schriftlich" : []';
		} else {
			result += '"schriftlich" : [ ';
			for (let i = 0; i < obj.schriftlich.length; i++) {
				const elem = obj.schriftlich[i];
				result += JSON.stringify(elem);
				if (i < obj.schriftlich.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLaufbahnplanungDatenFachbelegung>) : string {
		let result = '{';
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.abiturFach !== "undefined") {
			result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach) + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			const a = obj.kursart;
			if (!a) {
				result += '"kursart" : []';
			} else {
				result += '"kursart" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
					result += (elem == null) ? null : '"' + elem + '"';
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schriftlich !== "undefined") {
			const a = obj.schriftlich;
			if (!a) {
				result += '"schriftlich" : []';
			} else {
				result += '"schriftlich" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
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

export function cast_de_svws_nrw_core_data_gost_GostLaufbahnplanungDatenFachbelegung(obj : unknown) : GostLaufbahnplanungDatenFachbelegung {
	return obj as GostLaufbahnplanungDatenFachbelegung;
}
