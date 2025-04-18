import { JavaObject } from '../../../java/lang/JavaObject';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../java/lang/Class';

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


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenFachbelegung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenFachbelegung'].includes(name);
	}

	public static class = new Class<GostLaufbahnplanungDatenFachbelegung>('de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenFachbelegung');

	public static transpilerFromJSON(json : string): GostLaufbahnplanungDatenFachbelegung {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungDatenFachbelegung>;
		const result = new GostLaufbahnplanungDatenFachbelegung();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.abiturFach = (obj.abiturFach === undefined) ? null : obj.abiturFach === null ? null : obj.abiturFach;
		if (obj.kursart !== undefined) {
			for (let i = 0; i < obj.kursart.length; i++) {
				result.kursart[i] = obj.kursart[i] === null ? null : obj.kursart[i];
			}
		}
		if (obj.schriftlich !== undefined) {
			for (let i = 0; i < obj.schriftlich.length; i++) {
				result.schriftlich[i] = obj.schriftlich[i];
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLaufbahnplanungDatenFachbelegung) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"abiturFach" : ' + ((obj.abiturFach === null) ? 'null' : obj.abiturFach.toString()) + ',';
		result += '"kursart" : [ ';
		for (let i = 0; i < obj.kursart.length; i++) {
			const elem = obj.kursart[i];
			result += (elem === null) ? null : '"' + elem + '"';
			if (i < obj.kursart.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schriftlich" : [ ';
		for (let i = 0; i < obj.schriftlich.length; i++) {
			const elem = obj.schriftlich[i];
			result += JSON.stringify(elem);
			if (i < obj.schriftlich.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLaufbahnplanungDatenFachbelegung>) : string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.abiturFach !== undefined) {
			result += '"abiturFach" : ' + ((obj.abiturFach === null) ? 'null' : obj.abiturFach.toString()) + ',';
		}
		if (obj.kursart !== undefined) {
			const a = obj.kursart;
			result += '"kursart" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += (elem === null) ? null : '"' + elem + '"';
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schriftlich !== undefined) {
			const a = obj.schriftlich;
			result += '"schriftlich" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostLaufbahnplanungDatenFachbelegung(obj : unknown) : GostLaufbahnplanungDatenFachbelegung {
	return obj as GostLaufbahnplanungDatenFachbelegung;
}
