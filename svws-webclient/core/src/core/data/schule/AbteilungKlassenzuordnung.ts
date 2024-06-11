import { JavaObject } from '../../../java/lang/JavaObject';

export class AbteilungKlassenzuordnung extends JavaObject {

	/**
	 * Die ID der Zuordnung
	 */
	public id : number = -1;

	/**
	 * Die ID der Abteilung
	 */
	public idAbleitung : number = -1;

	/**
	 * Die ID der Klasse
	 */
	public idKlasse : number = -1;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbteilungKlassenzuordnung {
		const obj = JSON.parse(json);
		const result = new AbteilungKlassenzuordnung();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idAbleitung === "undefined")
			 throw new Error('invalid json format, missing attribute idAbleitung');
		result.idAbleitung = obj.idAbleitung;
		if (typeof obj.idKlasse === "undefined")
			 throw new Error('invalid json format, missing attribute idKlasse');
		result.idKlasse = obj.idKlasse;
		return result;
	}

	public static transpilerToJSON(obj : AbteilungKlassenzuordnung) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idAbleitung" : ' + obj.idAbleitung + ',';
		result += '"idKlasse" : ' + obj.idKlasse + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbteilungKlassenzuordnung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idAbleitung !== "undefined") {
			result += '"idAbleitung" : ' + obj.idAbleitung + ',';
		}
		if (typeof obj.idKlasse !== "undefined") {
			result += '"idKlasse" : ' + obj.idKlasse + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_AbteilungKlassenzuordnung(obj : unknown) : AbteilungKlassenzuordnung {
	return obj as AbteilungKlassenzuordnung;
}
