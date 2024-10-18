import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

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

	/**
	 * Die ID des vorigen Schuljahresabschnittes
	 */
	public idVorigerAbschnitt : number | null = null;

	/**
	 * Die ID des folgenden Schuljahresabschnittes
	 */
	public idFolgeAbschnitt : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.Schuljahresabschnitt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.Schuljahresabschnitt'].includes(name);
	}

	public static class = new Class<Schuljahresabschnitt>('de.svws_nrw.asd.data.schule.Schuljahresabschnitt');

	public static transpilerFromJSON(json : string): Schuljahresabschnitt {
		const obj = JSON.parse(json) as Partial<Schuljahresabschnitt>;
		const result = new Schuljahresabschnitt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		result.idVorigerAbschnitt = (obj.idVorigerAbschnitt === undefined) ? null : obj.idVorigerAbschnitt === null ? null : obj.idVorigerAbschnitt;
		result.idFolgeAbschnitt = (obj.idFolgeAbschnitt === undefined) ? null : obj.idFolgeAbschnitt === null ? null : obj.idFolgeAbschnitt;
		return result;
	}

	public static transpilerToJSON(obj : Schuljahresabschnitt) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"idVorigerAbschnitt" : ' + ((!obj.idVorigerAbschnitt) ? 'null' : obj.idVorigerAbschnitt.toString()) + ',';
		result += '"idFolgeAbschnitt" : ' + ((!obj.idFolgeAbschnitt) ? 'null' : obj.idFolgeAbschnitt.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schuljahresabschnitt>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.idVorigerAbschnitt !== undefined) {
			result += '"idVorigerAbschnitt" : ' + ((!obj.idVorigerAbschnitt) ? 'null' : obj.idVorigerAbschnitt.toString()) + ',';
		}
		if (obj.idFolgeAbschnitt !== undefined) {
			result += '"idFolgeAbschnitt" : ' + ((!obj.idFolgeAbschnitt) ? 'null' : obj.idFolgeAbschnitt.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_Schuljahresabschnitt(obj : unknown) : Schuljahresabschnitt {
	return obj as Schuljahresabschnitt;
}
