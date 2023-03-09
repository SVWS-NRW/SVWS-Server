import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';

export class Schuljahresabschnitt extends JavaObject {

	/**
	 * Die ID des Schuljahresabschnittes 
	 */
	public id : number = 0;

	/**
	 * Das Schuljahr, in welchem der Schuljahresabschnitt liegt 
	 */
	public schuljahr : number = 0;

	/**
	 * Die Nummer des Abschnitts im Schuljahr 
	 */
	public abschnitt : number = 0;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schule.Schuljahresabschnitt'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schuljahresabschnitt {
		const obj = JSON.parse(json);
		const result = new Schuljahresabschnitt();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.schuljahr === "undefined")
			 throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (typeof obj.abschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		return result;
	}

	public static transpilerToJSON(obj : Schuljahresabschnitt) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"schuljahr" : ' + obj.schuljahr + ',';
		result += '"abschnitt" : ' + obj.abschnitt + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schuljahresabschnitt>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.schuljahr !== "undefined") {
			result += '"schuljahr" : ' + obj.schuljahr + ',';
		}
		if (typeof obj.abschnitt !== "undefined") {
			result += '"abschnitt" : ' + obj.abschnitt + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schule_Schuljahresabschnitt(obj : unknown) : Schuljahresabschnitt {
	return obj as Schuljahresabschnitt;
}
