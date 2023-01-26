import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMLernabschnitt extends JavaObject {

	/**
	 * 
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
	public fehlstundenUnentschuldigt : number | null = null;

	/**
	 * Gibt den Zeitstempel der letzten Änderung für die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. 
	 */
	public tsFehlstundenUnentschuldigt : string | null = null;

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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.enm.ENMLernabschnitt'].includes(name);
	}

	public static transpilerFromJSON(json : string): ENMLernabschnitt {
		const obj = JSON.parse(json);
		const result = new ENMLernabschnitt();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.fehlstundenGesamt = typeof obj.fehlstundenGesamt === "undefined" ? null : obj.fehlstundenGesamt === null ? null : obj.fehlstundenGesamt;
		result.tsFehlstundenGesamt = typeof obj.tsFehlstundenGesamt === "undefined" ? null : obj.tsFehlstundenGesamt === null ? null : obj.tsFehlstundenGesamt;
		result.fehlstundenUnentschuldigt = typeof obj.fehlstundenUnentschuldigt === "undefined" ? null : obj.fehlstundenUnentschuldigt === null ? null : obj.fehlstundenUnentschuldigt;
		result.tsFehlstundenUnentschuldigt = typeof obj.tsFehlstundenUnentschuldigt === "undefined" ? null : obj.tsFehlstundenUnentschuldigt === null ? null : obj.tsFehlstundenUnentschuldigt;
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
		result += '"tsFehlstundenGesamt" : ' + ((!obj.tsFehlstundenGesamt) ? 'null' : '"' + obj.tsFehlstundenGesamt + '"') + ',';
		result += '"fehlstundenUnentschuldigt" : ' + ((!obj.fehlstundenUnentschuldigt) ? 'null' : obj.fehlstundenUnentschuldigt) + ',';
		result += '"tsFehlstundenUnentschuldigt" : ' + ((!obj.tsFehlstundenUnentschuldigt) ? 'null' : '"' + obj.tsFehlstundenUnentschuldigt + '"') + ',';
		result += '"pruefungsordnung" : ' + ((!obj.pruefungsordnung) ? 'null' : '"' + obj.pruefungsordnung + '"') + ',';
		result += '"lernbereich1note" : ' + ((!obj.lernbereich1note) ? 'null' : '"' + obj.lernbereich1note + '"') + ',';
		result += '"lernbereich2note" : ' + ((!obj.lernbereich2note) ? 'null' : '"' + obj.lernbereich2note + '"') + ',';
		result += '"foerderschwerpunkt1" : ' + ((!obj.foerderschwerpunkt1) ? 'null' : '"' + obj.foerderschwerpunkt1 + '"') + ',';
		result += '"foerderschwerpunkt2" : ' + ((!obj.foerderschwerpunkt2) ? 'null' : '"' + obj.foerderschwerpunkt2 + '"') + ',';
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
			result += '"tsFehlstundenGesamt" : ' + ((!obj.tsFehlstundenGesamt) ? 'null' : '"' + obj.tsFehlstundenGesamt + '"') + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + ((!obj.fehlstundenUnentschuldigt) ? 'null' : obj.fehlstundenUnentschuldigt) + ',';
		}
		if (typeof obj.tsFehlstundenUnentschuldigt !== "undefined") {
			result += '"tsFehlstundenUnentschuldigt" : ' + ((!obj.tsFehlstundenUnentschuldigt) ? 'null' : '"' + obj.tsFehlstundenUnentschuldigt + '"') + ',';
		}
		if (typeof obj.pruefungsordnung !== "undefined") {
			result += '"pruefungsordnung" : ' + ((!obj.pruefungsordnung) ? 'null' : '"' + obj.pruefungsordnung + '"') + ',';
		}
		if (typeof obj.lernbereich1note !== "undefined") {
			result += '"lernbereich1note" : ' + ((!obj.lernbereich1note) ? 'null' : '"' + obj.lernbereich1note + '"') + ',';
		}
		if (typeof obj.lernbereich2note !== "undefined") {
			result += '"lernbereich2note" : ' + ((!obj.lernbereich2note) ? 'null' : '"' + obj.lernbereich2note + '"') + ',';
		}
		if (typeof obj.foerderschwerpunkt1 !== "undefined") {
			result += '"foerderschwerpunkt1" : ' + ((!obj.foerderschwerpunkt1) ? 'null' : '"' + obj.foerderschwerpunkt1 + '"') + ',';
		}
		if (typeof obj.foerderschwerpunkt2 !== "undefined") {
			result += '"foerderschwerpunkt2" : ' + ((!obj.foerderschwerpunkt2) ? 'null' : '"' + obj.foerderschwerpunkt2 + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMLernabschnitt(obj : unknown) : ENMLernabschnitt {
	return obj as ENMLernabschnitt;
}
