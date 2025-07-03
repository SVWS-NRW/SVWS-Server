import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanListeEintrag extends JavaObject {

	/**
	 * Die ID des Stundenplans.
	 */
	public id : number = -1;

	/**
	 * Die textuelle Beschreibung des Stundenplans.
	 */
	public bezeichnung : string = "";

	/**
	 * Die ID des Schuljahresabschnitts des Stundenplans.
	 */
	public idSchuljahresabschnitt : number = -1;

	/**
	 * Das Schuljahr, in welchem der Stundenplan gültig ist.
	 */
	public schuljahr : number = -1;

	/**
	 * Der Abschnitt, in welchem der Stundenplan gültig ist (z.B. 2. Halbjahr oder 3. Quartal).
	 */
	public abschnitt : number = -1;

	/**
	 * Das Datum, ab dem der Stundenpland gültig ist.
	 */
	public gueltigAb : string = "";

	/**
	 * Das Datum, bis wann der Stundenplan gültig ist.
	 */
	public gueltigBis : string = "";

	/**
	 * Gibt an, ob der Stundenplan aktiv ist. An einem Datum kann immer nur ein Stundenplan aktiv sein.
	 */
	public aktiv : boolean = false;

	/**
	 * Das Modell für die Wochen des Stundenplans, d.h. ob es sich um einen Stundenplan für jede Woche handelt (0) oder ob es sich um einen unterschiedliche Stundenpläne in Abhängigkeit des Wochentyps handelt - z.B. A-/B-Wochen (2) handelt. Hier wird dann die maximale Anzahl der unterschiedlichen Wochentypen festgelegt. Der Wert 1 ist ungültig!
	 */
	public wochenTypModell : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag'].includes(name);
	}

	public static class = new Class<StundenplanListeEintrag>('de.svws_nrw.core.data.stundenplan.StundenplanListeEintrag');

	public static transpilerFromJSON(json : string): StundenplanListeEintrag {
		const obj = JSON.parse(json) as Partial<StundenplanListeEintrag>;
		const result = new StundenplanListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		if (obj.gueltigAb === undefined)
			throw new Error('invalid json format, missing attribute gueltigAb');
		result.gueltigAb = obj.gueltigAb;
		if (obj.gueltigBis === undefined)
			throw new Error('invalid json format, missing attribute gueltigBis');
		result.gueltigBis = obj.gueltigBis;
		if (obj.aktiv === undefined)
			throw new Error('invalid json format, missing attribute aktiv');
		result.aktiv = obj.aktiv;
		if (obj.wochenTypModell === undefined)
			throw new Error('invalid json format, missing attribute wochenTypModell');
		result.wochenTypModell = obj.wochenTypModell;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result += '"gueltigAb" : ' + JSON.stringify(obj.gueltigAb) + ',';
		result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		result += '"wochenTypModell" : ' + obj.wochenTypModell.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		if (obj.gueltigAb !== undefined) {
			result += '"gueltigAb" : ' + JSON.stringify(obj.gueltigAb) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + JSON.stringify(obj.gueltigBis) + ',';
		}
		if (obj.aktiv !== undefined) {
			result += '"aktiv" : ' + obj.aktiv.toString() + ',';
		}
		if (obj.wochenTypModell !== undefined) {
			result += '"wochenTypModell" : ' + obj.wochenTypModell.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanListeEintrag(obj : unknown) : StundenplanListeEintrag {
	return obj as StundenplanListeEintrag;
}
