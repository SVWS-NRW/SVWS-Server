import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisSchiene } from '../../../core/data/gost/GostBlockungsergebnisSchiene';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';

export class GostBlockungsergebnis extends JavaObject {

	/**
	 * Die ID des Zwischenergebnisses der Blockung
	 */
	public id : number = -1;

	/**
	 * Die ID der Blockung
	 */
	public blockungID : number = -1;

	/**
	 * Der Name der Blockung
	 */
	public name : string = "Blockung";

	/**
	 * Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public gostHalbjahr : number = 0;

	/**
	 * Gibt an, ob dieses Ergebnis als aktiv markiert wurde.
	 */
	public istAktiv : boolean = false;

	/**
	 * Eine Liste der Schienen, welche zugeordnete Kurse beinhalten.
	 */
	public readonly schienen : List<GostBlockungsergebnisSchiene> = new ArrayList<GostBlockungsergebnisSchiene>();

	/**
	 * Die Informationen zur aktuellen Bewertung des Blockungsergebnisses
	 */
	public bewertung : GostBlockungsergebnisBewertung = new GostBlockungsergebnisBewertung();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnis';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnis'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnis {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnis();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.blockungID === undefined)
			 throw new Error('invalid json format, missing attribute blockungID');
		result.blockungID = obj.blockungID;
		if (obj.name === undefined)
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.gostHalbjahr === undefined)
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (obj.istAktiv === undefined)
			 throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if ((obj.schienen !== undefined) && (obj.schienen !== null)) {
			for (const elem of obj.schienen) {
				result.schienen?.add(GostBlockungsergebnisSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.bewertung === undefined)
			 throw new Error('invalid json format, missing attribute bewertung');
		result.bewertung = GostBlockungsergebnisBewertung.transpilerFromJSON(JSON.stringify(obj.bewertung));
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnis) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"blockungID" : ' + obj.blockungID + ',';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"istAktiv" : ' + obj.istAktiv + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += GostBlockungsergebnisSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"bewertung" : ' + GostBlockungsergebnisBewertung.transpilerToJSON(obj.bewertung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnis>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.blockungID !== undefined) {
			result += '"blockungID" : ' + obj.blockungID + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (obj.gostHalbjahr !== undefined) {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (obj.istAktiv !== undefined) {
			result += '"istAktiv" : ' + obj.istAktiv + ',';
		}
		if (obj.schienen !== undefined) {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i = 0; i < obj.schienen.size(); i++) {
					const elem = obj.schienen.get(i);
					result += GostBlockungsergebnisSchiene.transpilerToJSON(elem);
					if (i < obj.schienen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (obj.bewertung !== undefined) {
			result += '"bewertung" : ' + GostBlockungsergebnisBewertung.transpilerToJSON(obj.bewertung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnis(obj : unknown) : GostBlockungsergebnis {
	return obj as GostBlockungsergebnis;
}
