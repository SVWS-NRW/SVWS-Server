import { JavaObject } from '../../../java/lang/JavaObject';
import { GostStatistikFachwahlHalbjahr } from '../../../core/data/gost/GostStatistikFachwahlHalbjahr';

export class GostStatistikFachwahl extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird.
	 */
	public abiturjahr : number = 0;

	/**
	 * Die ID des Faches, für welches die Fachwahldaten ermittelt wurden.
	 */
	public id : number = 0;

	/**
	 * Das eindeutige Kürzel des Faches
	 */
	public kuerzel : string | null = null;

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string | null = null;

	/**
	 * Das Statistik-Kürzel des Faches
	 */
	public kuerzelStatistik : string | null = null;

	/**
	 * Die Anzahl der Wahlen als drittes Abiturfach.
	 */
	public wahlenAB3 : number = 0;

	/**
	 * Die Anzahl der Wahlen als viertes Abiturfach.
	 */
	public wahlenAB4 : number = 0;

	/**
	 * Ein Array mit den Fachwahlen der 6 Halbjahre der gymnasialen Oberstufe
	 */
	public fachwahlen : Array<GostStatistikFachwahlHalbjahr> = Array(6).fill(null);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostStatistikFachwahl'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostStatistikFachwahl {
		const obj = JSON.parse(json);
		const result = new GostStatistikFachwahl();
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		result.kuerzelStatistik = typeof obj.kuerzelStatistik === "undefined" ? null : obj.kuerzelStatistik === null ? null : obj.kuerzelStatistik;
		if (typeof obj.wahlenAB3 === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenAB3');
		result.wahlenAB3 = obj.wahlenAB3;
		if (typeof obj.wahlenAB4 === "undefined")
			 throw new Error('invalid json format, missing attribute wahlenAB4');
		result.wahlenAB4 = obj.wahlenAB4;
		for (let i = 0; i < obj.fachwahlen.length; i++) {
			result.fachwahlen[i] = (GostStatistikFachwahlHalbjahr.transpilerFromJSON(JSON.stringify(obj.fachwahlen[i])));
		}
		return result;
	}

	public static transpilerToJSON(obj : GostStatistikFachwahl) : string {
		let result = '{';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik + '"') + ',';
		result += '"wahlenAB3" : ' + obj.wahlenAB3 + ',';
		result += '"wahlenAB4" : ' + obj.wahlenAB4 + ',';
		if (!obj.fachwahlen) {
			result += '"fachwahlen" : []';
		} else {
			result += '"fachwahlen" : [ ';
			for (let i = 0; i < obj.fachwahlen.length; i++) {
				const elem = obj.fachwahlen[i];
				result += GostStatistikFachwahlHalbjahr.transpilerToJSON(elem);
				if (i < obj.fachwahlen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostStatistikFachwahl>) : string {
		let result = '{';
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : '"' + obj.kuerzel + '"') + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : '"' + obj.bezeichnung + '"') + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + ((!obj.kuerzelStatistik) ? 'null' : '"' + obj.kuerzelStatistik + '"') + ',';
		}
		if (typeof obj.wahlenAB3 !== "undefined") {
			result += '"wahlenAB3" : ' + obj.wahlenAB3 + ',';
		}
		if (typeof obj.wahlenAB4 !== "undefined") {
			result += '"wahlenAB4" : ' + obj.wahlenAB4 + ',';
		}
		if (typeof obj.fachwahlen !== "undefined") {
			const a = obj.fachwahlen;
			if (!a) {
				result += '"fachwahlen" : []';
			} else {
				result += '"fachwahlen" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
					result += GostStatistikFachwahlHalbjahr.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_gost_GostStatistikFachwahl(obj : unknown) : GostStatistikFachwahl {
	return obj as GostStatistikFachwahl;
}
