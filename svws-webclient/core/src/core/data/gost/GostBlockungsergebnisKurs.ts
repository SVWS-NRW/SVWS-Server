import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostBlockungsergebnisKurs extends JavaObject {

	/**
	 * Die ID des Kurses
	 */
	public id : number = -1;

	/**
	 * Die ID des Kurs-Faches
	 */
	public fachID : number = -1;

	/**
	 * Die Kursart des Kurses
	 */
	public kursart : number = -1;

	/**
	 * Die Anzahl an Schienen die der Kurs belegen soll. Falls > 1 handelt es sich um einen Multikurs.
	 */
	public anzahlSchienen : number = -1;

	/**
	 * Eine Liste Schüler-IDs, welche diesem Kurs zugeordnet sind.
	 */
	public readonly schueler : List<number> = new ArrayList<number>();

	/**
	 * Die Schienen-IDs, denen der Kurs zugeordnet ist.
	 */
	public readonly schienen : List<number> = new ArrayList<number>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs'].includes(name);
	}

	public static class = new Class<GostBlockungsergebnisKurs>('de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs');

	public static transpilerFromJSON(json : string): GostBlockungsergebnisKurs {
		const obj = JSON.parse(json) as Partial<GostBlockungsergebnisKurs>;
		const result = new GostBlockungsergebnisKurs();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.anzahlSchienen === undefined)
			throw new Error('invalid json format, missing attribute anzahlSchienen');
		result.anzahlSchienen = obj.anzahlSchienen;
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(elem);
			}
		}
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisKurs) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"kursart" : ' + obj.kursart.toString() + ',';
		result += '"anzahlSchienen" : ' + obj.anzahlSchienen.toString() + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += elem.toString();
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += elem.toString();
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisKurs>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + obj.kursart.toString() + ',';
		}
		if (obj.anzahlSchienen !== undefined) {
			result += '"anzahlSchienen" : ' + obj.anzahlSchienen.toString() + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += elem.toString();
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += elem.toString();
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisKurs(obj : unknown) : GostBlockungsergebnisKurs {
	return obj as GostBlockungsergebnisKurs;
}
