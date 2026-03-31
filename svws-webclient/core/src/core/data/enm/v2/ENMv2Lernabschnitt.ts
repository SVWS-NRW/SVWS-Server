import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class ENMv2Lernabschnitt extends JavaObject {

	/**
	 *  Die ID des Lernabschnittes in der SVWS-DB - kann zum Prüfen verwendet werden, ob der
	 *  zuvor exportierte Lernabschnitt in der DB noch gültig ist
	 */
	public id: number = 0;

	/**
	 * Gibt die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public fehlstundenGesamt: number | null = null;

	/**
	 * Gibt den Zeitstempel der letzten Änderung für die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public tsFehlstundenGesamt: string | null = null;

	/**
	 * Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public fehlstundenGesamtUnentschuldigt: number | null = null;

	/**
	 * Gibt den Zeitstempel der letzten Änderung für die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.
	 */
	public tsFehlstundenGesamtUnentschuldigt: string | null = null;

	/**
	 * Die Prüfungsordnung, die in dem Lernabschnitt verwendet werden muss
	 */
	public pruefungsordnung: string | null = null;

	/**
	 * Das Kürzel der Note für den Lernbereich 1, die vergeben wurde.
	 */
	public lernbereich1note: string | null = null;

	/**
	 * Das Kürzel der Note für den Lernbereich 2, die vergeben wurde.
	 */
	public lernbereich2note: string | null = null;

	/**
	 * Das Kürzel des Hauptförderschwerpunktes oder null bei keinem Hauptförderschwerpunkt
	 */
	public foerderschwerpunkt1: string | null = null;

	/**
	 * Das Kürzel des weiteren Förderschwerpunktes oder null bei keinem weiteren Förderschwerpunkt
	 */
	public foerderschwerpunkt2: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.enm.v2.ENMv2Lernabschnitt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.enm.v2.ENMv2Lernabschnitt'].includes(name);
	}

	public static readonly class = new Class<ENMv2Lernabschnitt>('de.svws_nrw.core.data.enm.v2.ENMv2Lernabschnitt');

	public static transpilerFromJSON(json: string): ENMv2Lernabschnitt {
		const obj = JSON.parse(json) as Partial<ENMv2Lernabschnitt>;
		const result = new ENMv2Lernabschnitt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.fehlstundenGesamt = (obj.fehlstundenGesamt === undefined) ? null : obj.fehlstundenGesamt === null ? null : obj.fehlstundenGesamt;
		result.tsFehlstundenGesamt = (obj.tsFehlstundenGesamt === undefined) ? null : obj.tsFehlstundenGesamt === null ? null : obj.tsFehlstundenGesamt;
		result.fehlstundenGesamtUnentschuldigt = (obj.fehlstundenGesamtUnentschuldigt === undefined) ? null : obj.fehlstundenGesamtUnentschuldigt === null ? null : obj.fehlstundenGesamtUnentschuldigt;
		result.tsFehlstundenGesamtUnentschuldigt = (obj.tsFehlstundenGesamtUnentschuldigt === undefined) ? null : obj.tsFehlstundenGesamtUnentschuldigt === null ? null : obj.tsFehlstundenGesamtUnentschuldigt;
		result.pruefungsordnung = (obj.pruefungsordnung === undefined) ? null : obj.pruefungsordnung === null ? null : obj.pruefungsordnung;
		result.lernbereich1note = (obj.lernbereich1note === undefined) ? null : obj.lernbereich1note === null ? null : obj.lernbereich1note;
		result.lernbereich2note = (obj.lernbereich2note === undefined) ? null : obj.lernbereich2note === null ? null : obj.lernbereich2note;
		result.foerderschwerpunkt1 = (obj.foerderschwerpunkt1 === undefined) ? null : obj.foerderschwerpunkt1 === null ? null : obj.foerderschwerpunkt1;
		result.foerderschwerpunkt2 = (obj.foerderschwerpunkt2 === undefined) ? null : obj.foerderschwerpunkt2 === null ? null : obj.foerderschwerpunkt2;
		return result;
	}

	public static transpilerToJSON(obj: ENMv2Lernabschnitt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fehlstundenGesamt" : ' + ((obj.fehlstundenGesamt === null) ? 'null' : obj.fehlstundenGesamt.toString()) + ',';
		result += '"tsFehlstundenGesamt" : ' + ((obj.tsFehlstundenGesamt === null) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamt)) + ',';
		result += '"fehlstundenGesamtUnentschuldigt" : ' + ((obj.fehlstundenGesamtUnentschuldigt === null) ? 'null' : obj.fehlstundenGesamtUnentschuldigt.toString()) + ',';
		result += '"tsFehlstundenGesamtUnentschuldigt" : ' + ((obj.tsFehlstundenGesamtUnentschuldigt === null) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamtUnentschuldigt)) + ',';
		result += '"pruefungsordnung" : ' + ((obj.pruefungsordnung === null) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		result += '"lernbereich1note" : ' + ((obj.lernbereich1note === null) ? 'null' : JSON.stringify(obj.lernbereich1note)) + ',';
		result += '"lernbereich2note" : ' + ((obj.lernbereich2note === null) ? 'null' : JSON.stringify(obj.lernbereich2note)) + ',';
		result += '"foerderschwerpunkt1" : ' + ((obj.foerderschwerpunkt1 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		result += '"foerderschwerpunkt2" : ' + ((obj.foerderschwerpunkt2 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<ENMv2Lernabschnitt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fehlstundenGesamt !== undefined) {
			result += '"fehlstundenGesamt" : ' + ((obj.fehlstundenGesamt === null) ? 'null' : obj.fehlstundenGesamt.toString()) + ',';
		}
		if (obj.tsFehlstundenGesamt !== undefined) {
			result += '"tsFehlstundenGesamt" : ' + ((obj.tsFehlstundenGesamt === null) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamt)) + ',';
		}
		if (obj.fehlstundenGesamtUnentschuldigt !== undefined) {
			result += '"fehlstundenGesamtUnentschuldigt" : ' + ((obj.fehlstundenGesamtUnentschuldigt === null) ? 'null' : obj.fehlstundenGesamtUnentschuldigt.toString()) + ',';
		}
		if (obj.tsFehlstundenGesamtUnentschuldigt !== undefined) {
			result += '"tsFehlstundenGesamtUnentschuldigt" : ' + ((obj.tsFehlstundenGesamtUnentschuldigt === null) ? 'null' : JSON.stringify(obj.tsFehlstundenGesamtUnentschuldigt)) + ',';
		}
		if (obj.pruefungsordnung !== undefined) {
			result += '"pruefungsordnung" : ' + ((obj.pruefungsordnung === null) ? 'null' : JSON.stringify(obj.pruefungsordnung)) + ',';
		}
		if (obj.lernbereich1note !== undefined) {
			result += '"lernbereich1note" : ' + ((obj.lernbereich1note === null) ? 'null' : JSON.stringify(obj.lernbereich1note)) + ',';
		}
		if (obj.lernbereich2note !== undefined) {
			result += '"lernbereich2note" : ' + ((obj.lernbereich2note === null) ? 'null' : JSON.stringify(obj.lernbereich2note)) + ',';
		}
		if (obj.foerderschwerpunkt1 !== undefined) {
			result += '"foerderschwerpunkt1" : ' + ((obj.foerderschwerpunkt1 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt1)) + ',';
		}
		if (obj.foerderschwerpunkt2 !== undefined) {
			result += '"foerderschwerpunkt2" : ' + ((obj.foerderschwerpunkt2 === null) ? 'null' : JSON.stringify(obj.foerderschwerpunkt2)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_enm_v2_ENMv2Lernabschnitt(obj: unknown): ENMv2Lernabschnitt {
	return obj as ENMv2Lernabschnitt;
}
