import { JavaObject } from '../../../java/lang/JavaObject';

export class GostJahrgangFachkombination extends JavaObject {

	/**
	 * Die ID der Fachkombination
	 */
	public id : number = 0;

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird oder -1, falls es sich um die Vorlage für neue Jahrgänge handelt
	 */
	public abiturjahr : number = 0;

	/**
	 * Die ID des Faches (Fach 1), dessen Wahl die Kombination mit einem anderen Fach verlangt oder ausschließt
	 */
	public fachID1 : number = 0;

	/**
	 * Die Kursart der Fachwahl von Fach 1, falls die Fachkombination auf diese Kursart bei Fach 1 eingeschränkt ist
	 */
	public kursart1 : string | null = null;

	/**
	 * Die ID des Faches (Fach 2), welches in der Kombination verlangt oder ausgeschlossen wird
	 */
	public fachID2 : number = 0;

	/**
	 * Die Kursart der Fachwahl von Fach 2, falls die Fachkombination auf diese Kursart bei Fach 2 eingeschränkt ist
	 */
	public kursart2 : string | null = null;

	/**
	 * Gibt an, ob für die jeweilige Halbjahre der Oberstufe die Fachkombination gilt (0 = EF.1, 1=EF.2, ...)
	 */
	public gueltigInHalbjahr : Array<boolean> = Array(6).fill(false);

	/**
	 * Der Typ der Fachkombination (0: Wahl von Fach 2 ist in Kombination mit Fach 1 unzulässig, 1: Wahl von Fach 2 ist bei Wahl von Fach 1 nötig)
	 */
	public typ : number = 0;

	/**
	 * Der erläuternde Hinweistext zu der Fachkombination
	 */
	public hinweistext : string = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostJahrgangFachkombination'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostJahrgangFachkombination {
		const obj = JSON.parse(json);
		const result = new GostJahrgangFachkombination();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		if (typeof obj.fachID1 === "undefined")
			 throw new Error('invalid json format, missing attribute fachID1');
		result.fachID1 = obj.fachID1;
		result.kursart1 = typeof obj.kursart1 === "undefined" ? null : obj.kursart1 === null ? null : obj.kursart1;
		if (typeof obj.fachID2 === "undefined")
			 throw new Error('invalid json format, missing attribute fachID2');
		result.fachID2 = obj.fachID2;
		result.kursart2 = typeof obj.kursart2 === "undefined" ? null : obj.kursart2 === null ? null : obj.kursart2;
		for (let i = 0; i < obj.gueltigInHalbjahr.length; i++) {
			result.gueltigInHalbjahr[i] = obj.gueltigInHalbjahr[i];
		}
		if (typeof obj.typ === "undefined")
			 throw new Error('invalid json format, missing attribute typ');
		result.typ = obj.typ;
		if (typeof obj.hinweistext === "undefined")
			 throw new Error('invalid json format, missing attribute hinweistext');
		result.hinweistext = obj.hinweistext;
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgangFachkombination) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"fachID1" : ' + obj.fachID1 + ',';
		result += '"kursart1" : ' + ((!obj.kursart1) ? 'null' : JSON.stringify(obj.kursart1)) + ',';
		result += '"fachID2" : ' + obj.fachID2 + ',';
		result += '"kursart2" : ' + ((!obj.kursart2) ? 'null' : JSON.stringify(obj.kursart2)) + ',';
		if (!obj.gueltigInHalbjahr) {
			result += '"gueltigInHalbjahr" : []';
		} else {
			result += '"gueltigInHalbjahr" : [ ';
			for (let i = 0; i < obj.gueltigInHalbjahr.length; i++) {
				const elem = obj.gueltigInHalbjahr[i];
				result += JSON.stringify(elem);
				if (i < obj.gueltigInHalbjahr.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"typ" : ' + obj.typ + ',';
		result += '"hinweistext" : ' + JSON.stringify(obj.hinweistext!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgangFachkombination>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.fachID1 !== "undefined") {
			result += '"fachID1" : ' + obj.fachID1 + ',';
		}
		if (typeof obj.kursart1 !== "undefined") {
			result += '"kursart1" : ' + ((!obj.kursart1) ? 'null' : JSON.stringify(obj.kursart1)) + ',';
		}
		if (typeof obj.fachID2 !== "undefined") {
			result += '"fachID2" : ' + obj.fachID2 + ',';
		}
		if (typeof obj.kursart2 !== "undefined") {
			result += '"kursart2" : ' + ((!obj.kursart2) ? 'null' : JSON.stringify(obj.kursart2)) + ',';
		}
		if (typeof obj.gueltigInHalbjahr !== "undefined") {
			const a = obj.gueltigInHalbjahr;
			if (!a) {
				result += '"gueltigInHalbjahr" : []';
			} else {
				result += '"gueltigInHalbjahr" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.typ !== "undefined") {
			result += '"typ" : ' + obj.typ + ',';
		}
		if (typeof obj.hinweistext !== "undefined") {
			result += '"hinweistext" : ' + JSON.stringify(obj.hinweistext!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostJahrgangFachkombination(obj : unknown) : GostJahrgangFachkombination {
	return obj as GostJahrgangFachkombination;
}
