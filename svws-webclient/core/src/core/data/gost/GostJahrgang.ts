import { JavaObject } from '../../../java/lang/JavaObject';

export class GostJahrgang extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird. -1 bei der Vorlage für neue Abiturjahrgänge.
	 */
	public abiturjahr : number = -1;

	/**
	 * Die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist.
	 */
	public jahrgang : string | null = null;

	/**
	 * Die textuelle Bezeichnung für den Abiturjahrgang
	 */
	public bezeichnung : string | null = "Allgemein / Vorlage";

	/**
	 * Gibt an, ob das Abitur für diesen Jahrgang berets abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet.
	 */
	public istAbgeschlossen : boolean = false;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostJahrgang';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostJahrgang'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostJahrgang {
		const obj = JSON.parse(json);
		const result = new GostJahrgang();
		if (typeof obj.abiturjahr === "undefined")
			 throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		result.jahrgang = typeof obj.jahrgang === "undefined" ? null : obj.jahrgang === null ? null : obj.jahrgang;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (typeof obj.istAbgeschlossen === "undefined")
			 throw new Error('invalid json format, missing attribute istAbgeschlossen');
		result.istAbgeschlossen = obj.istAbgeschlossen;
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgang) : string {
		let result = '{';
		result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgang>) : string {
		let result = '{';
		if (typeof obj.abiturjahr !== "undefined") {
			result += '"abiturjahr" : ' + obj.abiturjahr + ',';
		}
		if (typeof obj.jahrgang !== "undefined") {
			result += '"jahrgang" : ' + ((!obj.jahrgang) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (typeof obj.istAbgeschlossen !== "undefined") {
			result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostJahrgang(obj : unknown) : GostJahrgang {
	return obj as GostJahrgang;
}
