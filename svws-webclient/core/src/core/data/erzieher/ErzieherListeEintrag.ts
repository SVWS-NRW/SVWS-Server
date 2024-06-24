import { JavaObject } from '../../../java/lang/JavaObject';

export class ErzieherListeEintrag extends JavaObject {

	/**
	 * Die "Partner"-ID des Erziehers mit der Nummer des Erziehers in der DB angehangen (1 oder 2), welche diesem Erzieher im gleichen DB-Datensatz zugeordnet ist und die gleichen Addressdaten, etc. sich teil.
	 */
	public id : number = 0;

	/**
	 * Die ID des Schülers, welchem der Erzieher zugeordnet ist.
	 */
	public idSchueler : number = 0;

	/**
	 * Die ID der Art des Erziehereintrages
	 */
	public idErzieherArt : number | null = null;

	/**
	 * Die Anrede des Erziehers.
	 */
	public anrede : string | null = null;

	/**
	 * Der Name des Erziehers.
	 */
	public name : string | null = null;

	/**
	 * Der Vorname des Erziehers.
	 */
	public vorname : string | null = null;

	/**
	 * Die E-Mailadresse des  Erziehers.
	 */
	public email : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.erzieher.ErzieherListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.erzieher.ErzieherListeEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): ErzieherListeEintrag {
		const obj = JSON.parse(json);
		const result = new ErzieherListeEintrag();
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idSchueler === undefined)
			 throw new Error('invalid json format, missing attribute idSchueler');
		result.idSchueler = obj.idSchueler;
		result.idErzieherArt = (obj.idErzieherArt === undefined) ? null : obj.idErzieherArt === null ? null : obj.idErzieherArt;
		result.anrede = (obj.anrede === undefined) ? null : obj.anrede === null ? null : obj.anrede;
		result.name = (obj.name === undefined) ? null : obj.name === null ? null : obj.name;
		result.vorname = (obj.vorname === undefined) ? null : obj.vorname === null ? null : obj.vorname;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		return result;
	}

	public static transpilerToJSON(obj : ErzieherListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idSchueler" : ' + obj.idSchueler + ',';
		result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt) + ',';
		result += '"anrede" : ' + ((!obj.anrede) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		result += '"name" : ' + ((!obj.name) ? 'null' : JSON.stringify(obj.name)) + ',';
		result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ErzieherListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.idSchueler !== undefined) {
			result += '"idSchueler" : ' + obj.idSchueler + ',';
		}
		if (obj.idErzieherArt !== undefined) {
			result += '"idErzieherArt" : ' + ((!obj.idErzieherArt) ? 'null' : obj.idErzieherArt) + ',';
		}
		if (obj.anrede !== undefined) {
			result += '"anrede" : ' + ((!obj.anrede) ? 'null' : JSON.stringify(obj.anrede)) + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + ((!obj.name) ? 'null' : JSON.stringify(obj.name)) + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + ((!obj.vorname) ? 'null' : JSON.stringify(obj.vorname)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((!obj.email) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_erzieher_ErzieherListeEintrag(obj : unknown) : ErzieherListeEintrag {
	return obj as ErzieherListeEintrag;
}
