import { JavaObject } from '../../../java/lang/JavaObject';

export class GostBlockungListeneintrag extends JavaObject {

	/**
	 * Die ID der Blockung
	 */
	public id : number = -1;

	/**
	 * Der Name der Blockung
	 */
	public name : string = "Neue Blockung";

	/**
	 * Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public gostHalbjahr : number = 0;

	/**
	 * Gibt an, ob diese Blockung als aktiv markiert wurde.
	 */
	public istAktiv : boolean = false;

	/**
	 * Gibt die Anzahl der Ergebnisse an, die bei der Blockung vorliegen
	 */
	public anzahlErgebnisse : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungListeneintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungListeneintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungListeneintrag {
		const obj = JSON.parse(json);
		const result = new GostBlockungListeneintrag();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.name === undefined)
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.gostHalbjahr === undefined)
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (obj.istAktiv === undefined)
			 throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if (obj.anzahlErgebnisse === undefined)
			 throw new Error('invalid json format, missing attribute anzahlErgebnisse');
		result.anzahlErgebnisse = obj.anzahlErgebnisse;
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungListeneintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"name" : ' + JSON.stringify(obj.name!) + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"istAktiv" : ' + obj.istAktiv + ',';
		result += '"anzahlErgebnisse" : ' + obj.anzahlErgebnisse + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungListeneintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
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
		if (obj.anzahlErgebnisse !== undefined) {
			result += '"anzahlErgebnisse" : ' + obj.anzahlErgebnisse + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungListeneintrag(obj : unknown) : GostBlockungListeneintrag {
	return obj as GostBlockungListeneintrag;
}
