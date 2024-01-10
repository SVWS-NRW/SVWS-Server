import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnisBewertung } from '../../../core/data/gost/GostBlockungsergebnisBewertung';

export class GostBlockungsergebnisListeneintrag extends JavaObject {

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
	 * Die Informationen zur aktuellen Bewertung des Blockungsergebnisses
	 */
	public bewertung : GostBlockungsergebnisBewertung = new GostBlockungsergebnisBewertung();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsergebnisListeneintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsergebnisListeneintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsergebnisListeneintrag {
		const obj = JSON.parse(json);
		const result = new GostBlockungsergebnisListeneintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.blockungID === "undefined")
			 throw new Error('invalid json format, missing attribute blockungID');
		result.blockungID = obj.blockungID;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.gostHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (typeof obj.istAktiv === "undefined")
			 throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if (typeof obj.bewertung === "undefined")
			 throw new Error('invalid json format, missing attribute bewertung');
		result.bewertung = GostBlockungsergebnisBewertung.transpilerFromJSON(JSON.stringify(obj.bewertung));
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsergebnisListeneintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"blockungID" : ' + obj.blockungID + ',';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"istAktiv" : ' + obj.istAktiv + ',';
		result += '"bewertung" : ' + GostBlockungsergebnisBewertung.transpilerToJSON(obj.bewertung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsergebnisListeneintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.blockungID !== "undefined") {
			result += '"blockungID" : ' + obj.blockungID + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (typeof obj.istAktiv !== "undefined") {
			result += '"istAktiv" : ' + obj.istAktiv + ',';
		}
		if (typeof obj.bewertung !== "undefined") {
			result += '"bewertung" : ' + GostBlockungsergebnisBewertung.transpilerToJSON(obj.bewertung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsergebnisListeneintrag(obj : unknown) : GostBlockungsergebnisListeneintrag {
	return obj as GostBlockungsergebnisListeneintrag;
}
