import { JavaObject } from '../../../java/lang/JavaObject';

export class ENMLernabschnitt extends JavaObject {

	/**
	 *  Die ID des Lernabschnittes in der SVWS-DB - kann zum Prüfen verwendet werden, ob der
	 *  zuvor exportierte Lernabschnitt in der DB noch gültig ist
	 */
	public id : number = 0;

	/**
	 * Gibt die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public fehlstundenGesamt : number | null = null;

	/**
	 * Gibt den Zeitstempel der letzten Änderung für die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public tsFehlstundenGesamt : string | null = null;

	/**
	 * Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public fehlstundenGesamtUnentschuldigt : number | null = null;

	/**
	 * Gibt den Zeitstempel der letzten Änderung für die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public tsFehlstundenGesamtUnentschuldigt : string | null = null;

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt verwendet werden muss
	 */
	public pruefungsordnung : string | null = null;

	/**
	 * Das Kürzel der Note für den Lernbereich 1, die vergeben wurde.
	 */
	public lernbereich1note : string | null = null;

	/**
	 * Das Kürzel der Note für den Lernbereich 2, die vergeben wurde.
	 */
	public lernbereich2note : string | null = null;

	/**
	 * Das Kürzel des Haupförderschwerpunktes oder null bei keinem Haupförderschwerpunkt
	 */
	public foerderschwerpunkt1 : string | null = null;

	/**
	 * Das Kürzel des weiteren Förderschwerpunktes oder null bei keinem weiteren Förderschwerpunkt
	 */
	public foerderschwerpunkt2 : string | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.ENMLernabschnitt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.enm.ENMLernabschnitt'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLernabschnitt {
		const obj = JSON.parse(json);
		const result = new ENMLernabschnitt();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.fehlstundenGesamt = typeof obj.fehlstundenGesamt === "undefined" ? null : obj.fehlstundenGesamt === null ? null : obj.fehlstundenGesamt;
		result.tsFehlstundenGesamt = typeof obj.tsFehlstundenGesamt === "undefined" ? null : obj.tsFehlstundenGesamt === null ? null : obj.tsFehlstundenGesamt;
		result.fehlstundenGesamtUnentschuldigt = typeof obj.fehlstundenGesamtUnentschuldigt === "undefined" ? null : obj.fehlstundenGesamtUnentschuldigt === null ? null : obj.fehlstundenGesamtUnentschuldigt;
		result.tsFehlstundenGesamtUnentschuldigt = typeof obj.tsFehlstundenGesamtUnentschuldigt === "undefined" ? null : obj.tsFehlstundenGesamtUnentschuldigt === null ? null : obj.tsFehlstundenGesamtUnentschuldigt;
		result.pruefungsordnung = typeof obj.pruefungsordnung === "undefined" ? null : obj.pruefungsordnung === null ? null : obj.pruefungsordnung;
		result.lernbereich1note = typeof obj.lernbereich1note === "undefined" ? null : obj.lernbereich1note === null ? null : obj.lernbereich1note;
		result.lernbereich2note = typeof obj.lernbereich2note === "undefined" ? null : obj.lernbereich2note === null ? null : obj.lernbereich2note;
		result.foerderschwerpunkt1 = typeof obj.foerderschwerpunkt1 === "undefined" ? null : obj.foerderschwerpunkt1 === null ? null : obj.foerderschwerpunkt1;
		result.foerderschwerpunkt2 = typeof obj.foerderschwerpunkt2 === "undefined" ? null : obj.foerderschwerpunkt2 === null ? null : obj.foerderschwerpunkt2;
		return result;
	}

	public static transpilerToJSON(obj : ENMLernabschnitt) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fehlstundenGesamt" : ' + ((!obj.fehlstundenGesamt) ? 'null' : obj.fehlstundenGesamt) + ',';
		result += '"tsFehlstundenGesamt" : ' + ((!obj.tsFehlstundenGesamt) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamt)) + ',';
		result += '"fehlstundenGesamtUnentschuldigt" : ' + ((!obj.fehlstundenGesamtUnentschuldigt) ? 'null' : obj.fehlstundenGesamtUnentschuldigt) + ',';
		result += '"tsFehlstundenGesamtUnentschuldigt" : ' + ((!obj.tsFehlstundenGesamtUnentschuldigt) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamtUnentschuldigt)) + ',';
		result += '"pruefungsordnung" : ' + ((!obj.pruefungsordnung) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		result += '"lernbereich1note" : ' + ((!obj.lernbereich1note) ? 'null' : JSON.stringify(obj.lernbereich1note)) + ',';
		result += '"lernbereich2note" : ' + ((!obj.lernbereich2note) ? 'null' : JSON.stringify(obj.lernbereich2note)) + ',';
		result += '"foerderschwerpunkt1" : ' + ((!obj.foerderschwerpunkt1) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		result += '"foerderschwerpunkt2" : ' + ((!obj.foerderschwerpunkt2) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<ENMLernabschnitt>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.fehlstundenGesamt !== "undefined") {
			result += '"fehlstundenGesamt" : ' + ((!obj.fehlstundenGesamt) ? 'null' : obj.fehlstundenGesamt) + ',';
		}
		if (typeof obj.tsFehlstundenGesamt !== "undefined") {
			result += '"tsFehlstundenGesamt" : ' + ((!obj.tsFehlstundenGesamt) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamt)) + ',';
		}
		if (typeof obj.fehlstundenGesamtUnentschuldigt !== "undefined") {
			result += '"fehlstundenGesamtUnentschuldigt" : ' + ((!obj.fehlstundenGesamtUnentschuldigt) ? 'null' : obj.fehlstundenGesamtUnentschuldigt) + ',';
		}
		if (typeof obj.tsFehlstundenGesamtUnentschuldigt !== "undefined") {
			result += '"tsFehlstundenGesamtUnentschuldigt" : ' + ((!obj.tsFehlstundenGesamtUnentschuldigt) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamtUnentschuldigt)) + ',';
		}
		if (typeof obj.pruefungsordnung !== "undefined") {
			result += '"pruefungsordnung" : ' + ((!obj.pruefungsordnung) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		}
		if (typeof obj.lernbereich1note !== "undefined") {
			result += '"lernbereich1note" : ' + ((!obj.lernbereich1note) ? 'null' : JSON.stringify(obj.lernbereich1note)) + ',';
		}
		if (typeof obj.lernbereich2note !== "undefined") {
			result += '"lernbereich2note" : ' + ((!obj.lernbereich2note) ? 'null' : JSON.stringify(obj.lernbereich2note)) + ',';
		}
		if (typeof obj.foerderschwerpunkt1 !== "undefined") {
			result += '"foerderschwerpunkt1" : ' + ((!obj.foerderschwerpunkt1) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		}
		if (typeof obj.foerderschwerpunkt2 !== "undefined") {
			result += '"foerderschwerpunkt2" : ' + ((!obj.foerderschwerpunkt2) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_ENMLernabschnitt(obj : unknown) : ENMLernabschnitt {
	return obj as ENMLernabschnitt;
}
