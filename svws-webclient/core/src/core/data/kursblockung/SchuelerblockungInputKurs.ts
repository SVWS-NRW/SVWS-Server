import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuelerblockungInputKurs extends JavaObject {

	/**
	 * Die ID des Kurses.
	 */
	public id : number = -1;

	/**
	 * Die ID des zugeordneten Faches. Beispielsweise gehört der Kurs 'D-LK1' zum Fach 'D'.
	 */
	public fach : number = -1;

	/**
	 * Die ID der zugeordneten Kursart. Beispielsweise gehört der Kurs 'D-LK1' zur Kursart 'LK'.
	 */
	public kursart : number = -1;

	/**
	 * Falls TRUE, dann darf der Schüler diesen Kurs nicht erhalten.
	 */
	public istGesperrt : boolean = false;

	/**
	 * Falls TRUE, dann muss der Schüler diesen Kurs erhalten.
	 */
	public istFixiert : boolean = false;

	/**
	 * Die Anzahl an SuS, die derzeit in diesem Kurs sind, ohne diesen Schüler.
	 */
	public anzahlSuS : number = -1;

	/**
	 * Die Schienen, die dieser Kurs belegt. In der Regel steht im Array eine Zahl (Schiene). Die Schienen sind 1-indiziert.
	 */
	public schienen : Array<number> = Array(0).fill(0);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kursblockung.SchuelerblockungInputKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuelerblockungInputKurs {
		const obj = JSON.parse(json);
		const result = new SchuelerblockungInputKurs();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.fach === "undefined")
			 throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.istGesperrt === "undefined")
			 throw new Error('invalid json format, missing attribute istGesperrt');
		result.istGesperrt = obj.istGesperrt;
		if (typeof obj.istFixiert === "undefined")
			 throw new Error('invalid json format, missing attribute istFixiert');
		result.istFixiert = obj.istFixiert;
		if (typeof obj.anzahlSuS === "undefined")
			 throw new Error('invalid json format, missing attribute anzahlSuS');
		result.anzahlSuS = obj.anzahlSuS;
		for (let i = 0; i < obj.schienen.length; i++) {
			result.schienen[i] = obj.schienen[i];
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungInputKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fach" : ' + obj.fach + ',';
		result += '"kursart" : ' + obj.kursart + ',';
		result += '"istGesperrt" : ' + obj.istGesperrt + ',';
		result += '"istFixiert" : ' + obj.istFixiert + ',';
		result += '"anzahlSuS" : ' + obj.anzahlSuS + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.length; i++) {
				const elem = obj.schienen[i];
				result += JSON.stringify(elem);
				if (i < obj.schienen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungInputKurs>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + obj.fach + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + obj.kursart + ',';
		}
		if (typeof obj.istGesperrt !== "undefined") {
			result += '"istGesperrt" : ' + obj.istGesperrt + ',';
		}
		if (typeof obj.istFixiert !== "undefined") {
			result += '"istFixiert" : ' + obj.istFixiert + ',';
		}
		if (typeof obj.anzahlSuS !== "undefined") {
			result += '"anzahlSuS" : ' + obj.anzahlSuS + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			const a = obj.schienen;
			if (!a) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
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

export function cast_de_svws_nrw_core_data_kursblockung_SchuelerblockungInputKurs(obj : unknown) : SchuelerblockungInputKurs {
	return obj as SchuelerblockungInputKurs;
}
