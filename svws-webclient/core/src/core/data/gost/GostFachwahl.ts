import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostFachwahl extends JavaObject {

	/**
	 * Die ID des Faches
	 */
	public fachID : number = -1;

	/**
	 * Die ID des Schülers
	 */
	public schuelerID : number = -1;

	/**
	 * Die ID der Kursart
	 */
	public kursartID : number = -1;

	/**
	 * Gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht
	 */
	public istSchriftlich : boolean = false;

	/**
	 * Gibt an, ob die Fachwahl als ein Abiturfach geplant ist oder nicht
	 */
	public abiturfach : number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostFachwahl';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostFachwahl'].includes(name);
	}

	public static class = new Class<GostFachwahl>('de.svws_nrw.core.data.gost.GostFachwahl');

	public static transpilerFromJSON(json : string): GostFachwahl {
		const obj = JSON.parse(json) as Partial<GostFachwahl>;
		const result = new GostFachwahl();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (obj.schuelerID === undefined)
			throw new Error('invalid json format, missing attribute schuelerID');
		result.schuelerID = obj.schuelerID;
		if (obj.kursartID === undefined)
			throw new Error('invalid json format, missing attribute kursartID');
		result.kursartID = obj.kursartID;
		if (obj.istSchriftlich === undefined)
			throw new Error('invalid json format, missing attribute istSchriftlich');
		result.istSchriftlich = obj.istSchriftlich;
		result.abiturfach = (obj.abiturfach === undefined) ? null : obj.abiturfach === null ? null : obj.abiturfach;
		return result;
	}

	public static transpilerToJSON(obj : GostFachwahl) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		result += '"kursartID" : ' + obj.kursartID.toString() + ',';
		result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		result += '"abiturfach" : ' + ((obj.abiturfach === null) ? 'null' : obj.abiturfach.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostFachwahl>) : string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.schuelerID !== undefined) {
			result += '"schuelerID" : ' + obj.schuelerID.toString() + ',';
		}
		if (obj.kursartID !== undefined) {
			result += '"kursartID" : ' + obj.kursartID.toString() + ',';
		}
		if (obj.istSchriftlich !== undefined) {
			result += '"istSchriftlich" : ' + obj.istSchriftlich.toString() + ',';
		}
		if (obj.abiturfach !== undefined) {
			result += '"abiturfach" : ' + ((obj.abiturfach === null) ? 'null' : obj.abiturfach.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostFachwahl(obj : unknown) : GostFachwahl {
	return obj as GostFachwahl;
}
