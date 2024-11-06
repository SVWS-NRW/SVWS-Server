import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanListeEintragMinimal extends JavaObject {

	/**
	 * Die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public bezeichnung : string = "";

	/**
	 * Das Datum, ab dem der Stundenpland gültig ist.
	 */
	public gueltigAb : string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal'].includes(name);
	}

	public static class = new Class<StundenplanListeEintragMinimal>('de.svws_nrw.core.data.stundenplan.StundenplanListeEintragMinimal');

	public static transpilerFromJSON(json : string): StundenplanListeEintragMinimal {
		const obj = JSON.parse(json) as Partial<StundenplanListeEintragMinimal>;
		const result = new StundenplanListeEintragMinimal();
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.gueltigAb === undefined)
			throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = obj.gueltigAb;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanListeEintragMinimal) : string {
		let result = '{';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"gueltigAb" : ' + JSON.stringify(obj.gueltigAb) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanListeEintragMinimal>) : string {
		let result = '{';
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.gueltigAb !== undefined) {
			result += '"gueltigAb" : ' + JSON.stringify(obj.gueltigAb) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanListeEintragMinimal(obj : unknown) : StundenplanListeEintragMinimal {
	return obj as StundenplanListeEintragMinimal;
}
