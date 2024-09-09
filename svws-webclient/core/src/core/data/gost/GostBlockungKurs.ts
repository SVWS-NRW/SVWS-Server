import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBlockungKursLehrer } from '../../../core/data/gost/GostBlockungKursLehrer';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostBlockungKurs extends JavaObject {

	/**
	 * Die ID des Kurses
	 */
	public id : number = -1;

	/**
	 * Die ID des Faches
	 */
	public fach_id : number = -1;

	/**
	 * Die Kursart siehe auch ID von {@link GostKursart}
	 */
	public kursart : number = 0;

	/**
	 * Die Nummer des Kurses (gezählt ab 1)
	 */
	public nummer : number = 0;

	/**
	 * Gibt an, ob es sich um einen Kooperationskurs an einer anderen Schule handelt
	 */
	public istKoopKurs : boolean = false;

	/**
	 * Ein Suffix, welches einer Standard-Kursbezeichnung angehangen wird - z.B. um spezielle Kurse zu markieren.
	 */
	public suffix : string = "";

	/**
	 * Die Anzahl der Wochenstunden, welche dem Kurs zugeordnet
	 */
	public wochenstunden : number = 3;

	/**
	 * Die Anzahl an Schienen, die der Kurs belegt, meistens =1. Falls > 1 ist es ein 'Multikurs'.
	 */
	public anzahlSchienen : number = 1;

	/**
	 * Die Lehrer, die diesem Kurs bereits fest zugeordnet sind.
	 */
	public lehrer : List<GostBlockungKursLehrer> = new ArrayList<GostBlockungKursLehrer>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungKurs';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungKurs'].includes(name);
	}

	public static class = new Class<GostBlockungKurs>('de.svws_nrw.core.data.gost.GostBlockungKurs');

	public static transpilerFromJSON(json : string): GostBlockungKurs {
		const obj = JSON.parse(json) as Partial<GostBlockungKurs>;
		const result = new GostBlockungKurs();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.fach_id === undefined)
			throw new Error('invalid json format, missing attribute fach_id');
		result.fach_id = obj.fach_id;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.nummer === undefined)
			throw new Error('invalid json format, missing attribute nummer');
		result.nummer = obj.nummer;
		if (obj.istKoopKurs === undefined)
			throw new Error('invalid json format, missing attribute istKoopKurs');
		result.istKoopKurs = obj.istKoopKurs;
		if (obj.suffix === undefined)
			throw new Error('invalid json format, missing attribute suffix');
		result.suffix = obj.suffix;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		if (obj.anzahlSchienen === undefined)
			throw new Error('invalid json format, missing attribute anzahlSchienen');
		result.anzahlSchienen = obj.anzahlSchienen;
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(GostBlockungKursLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fach_id" : ' + obj.fach_id.toString() + ',';
		result += '"kursart" : ' + obj.kursart.toString() + ',';
		result += '"nummer" : ' + obj.nummer.toString() + ',';
		result += '"istKoopKurs" : ' + obj.istKoopKurs.toString() + ',';
		result += '"suffix" : ' + JSON.stringify(obj.suffix) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"anzahlSchienen" : ' + obj.anzahlSchienen.toString() + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += GostBlockungKursLehrer.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungKurs>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fach_id !== undefined) {
			result += '"fach_id" : ' + obj.fach_id.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + obj.kursart.toString() + ',';
		}
		if (obj.nummer !== undefined) {
			result += '"nummer" : ' + obj.nummer.toString() + ',';
		}
		if (obj.istKoopKurs !== undefined) {
			result += '"istKoopKurs" : ' + obj.istKoopKurs.toString() + ',';
		}
		if (obj.suffix !== undefined) {
			result += '"suffix" : ' + JSON.stringify(obj.suffix) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.anzahlSchienen !== undefined) {
			result += '"anzahlSchienen" : ' + obj.anzahlSchienen.toString() + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += GostBlockungKursLehrer.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungKurs(obj : unknown) : GostBlockungKurs {
	return obj as GostBlockungKurs;
}
