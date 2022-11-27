import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class ENMLernabschnitt extends JavaObject {

	public id : number = 0;

	public fehlstundenGesamt : Number | null = null;

	public tsFehlstundenGesamt : String | null = null;

	public fehlstundenUnentschuldigt : Number | null = null;

	public tsFehlstundenUnentschuldigt : String | null = null;

	public pruefungsordnung : String | null = null;

	public lernbereich1note : String | null = null;

	public lernbereich2note : String | null = null;

	public foerderschwerpunkt1 : String | null = null;

	public foerderschwerpunkt2 : String | null = null;


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
		result.fehlstundenGesamt = typeof obj.fehlstundenGesamt === "undefined" ? null : obj.fehlstundenGesamt === null ? null : Number(obj.fehlstundenGesamt);
		result.tsFehlstundenGesamt = typeof obj.tsFehlstundenGesamt === "undefined" ? null : obj.tsFehlstundenGesamt === null ? null : String(obj.tsFehlstundenGesamt);
		result.fehlstundenUnentschuldigt = typeof obj.fehlstundenUnentschuldigt === "undefined" ? null : obj.fehlstundenUnentschuldigt === null ? null : Number(obj.fehlstundenUnentschuldigt);
		result.tsFehlstundenUnentschuldigt = typeof obj.tsFehlstundenUnentschuldigt === "undefined" ? null : obj.tsFehlstundenUnentschuldigt === null ? null : String(obj.tsFehlstundenUnentschuldigt);
		result.pruefungsordnung = typeof obj.pruefungsordnung === "undefined" ? null : obj.pruefungsordnung === null ? null : String(obj.pruefungsordnung);
		result.lernbereich1note = typeof obj.lernbereich1note === "undefined" ? null : obj.lernbereich1note === null ? null : String(obj.lernbereich1note);
		result.lernbereich2note = typeof obj.lernbereich2note === "undefined" ? null : obj.lernbereich2note === null ? null : String(obj.lernbereich2note);
		result.foerderschwerpunkt1 = typeof obj.foerderschwerpunkt1 === "undefined" ? null : obj.foerderschwerpunkt1 === null ? null : String(obj.foerderschwerpunkt1);
		result.foerderschwerpunkt2 = typeof obj.foerderschwerpunkt2 === "undefined" ? null : obj.foerderschwerpunkt2 === null ? null : String(obj.foerderschwerpunkt2);
		return result;
	}

	public static transpilerToJSON(obj : ENMLernabschnitt) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"fehlstundenGesamt" : ' + ((!obj.fehlstundenGesamt) ? 'null' : obj.fehlstundenGesamt.valueOf()) + ',';
		result += '"tsFehlstundenGesamt" : ' + ((!obj.tsFehlstundenGesamt) ? 'null' : '"' + obj.tsFehlstundenGesamt.valueOf() + '"') + ',';
		result += '"fehlstundenUnentschuldigt" : ' + ((!obj.fehlstundenUnentschuldigt) ? 'null' : obj.fehlstundenUnentschuldigt.valueOf()) + ',';
		result += '"tsFehlstundenUnentschuldigt" : ' + ((!obj.tsFehlstundenUnentschuldigt) ? 'null' : '"' + obj.tsFehlstundenUnentschuldigt.valueOf() + '"') + ',';
		result += '"pruefungsordnung" : ' + ((!obj.pruefungsordnung) ? 'null' : '"' + obj.pruefungsordnung.valueOf() + '"') + ',';
		result += '"lernbereich1note" : ' + ((!obj.lernbereich1note) ? 'null' : '"' + obj.lernbereich1note.valueOf() + '"') + ',';
		result += '"lernbereich2note" : ' + ((!obj.lernbereich2note) ? 'null' : '"' + obj.lernbereich2note.valueOf() + '"') + ',';
		result += '"foerderschwerpunkt1" : ' + ((!obj.foerderschwerpunkt1) ? 'null' : '"' + obj.foerderschwerpunkt1.valueOf() + '"') + ',';
		result += '"foerderschwerpunkt2" : ' + ((!obj.foerderschwerpunkt2) ? 'null' : '"' + obj.foerderschwerpunkt2.valueOf() + '"') + ',';
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
			result += '"fehlstundenGesamt" : ' + ((!obj.fehlstundenGesamt) ? 'null' : obj.fehlstundenGesamt.valueOf()) + ',';
		}
		if (typeof obj.tsFehlstundenGesamt !== "undefined") {
			result += '"tsFehlstundenGesamt" : ' + ((!obj.tsFehlstundenGesamt) ? 'null' : '"' + obj.tsFehlstundenGesamt.valueOf() + '"') + ',';
		}
		if (typeof obj.fehlstundenUnentschuldigt !== "undefined") {
			result += '"fehlstundenUnentschuldigt" : ' + ((!obj.fehlstundenUnentschuldigt) ? 'null' : obj.fehlstundenUnentschuldigt.valueOf()) + ',';
		}
		if (typeof obj.tsFehlstundenUnentschuldigt !== "undefined") {
			result += '"tsFehlstundenUnentschuldigt" : ' + ((!obj.tsFehlstundenUnentschuldigt) ? 'null' : '"' + obj.tsFehlstundenUnentschuldigt.valueOf() + '"') + ',';
		}
		if (typeof obj.pruefungsordnung !== "undefined") {
			result += '"pruefungsordnung" : ' + ((!obj.pruefungsordnung) ? 'null' : '"' + obj.pruefungsordnung.valueOf() + '"') + ',';
		}
		if (typeof obj.lernbereich1note !== "undefined") {
			result += '"lernbereich1note" : ' + ((!obj.lernbereich1note) ? 'null' : '"' + obj.lernbereich1note.valueOf() + '"') + ',';
		}
		if (typeof obj.lernbereich2note !== "undefined") {
			result += '"lernbereich2note" : ' + ((!obj.lernbereich2note) ? 'null' : '"' + obj.lernbereich2note.valueOf() + '"') + ',';
		}
		if (typeof obj.foerderschwerpunkt1 !== "undefined") {
			result += '"foerderschwerpunkt1" : ' + ((!obj.foerderschwerpunkt1) ? 'null' : '"' + obj.foerderschwerpunkt1.valueOf() + '"') + ',';
		}
		if (typeof obj.foerderschwerpunkt2 !== "undefined") {
			result += '"foerderschwerpunkt2" : ' + ((!obj.foerderschwerpunkt2) ? 'null' : '"' + obj.foerderschwerpunkt2.valueOf() + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_enm_ENMLernabschnitt(obj : unknown) : ENMLernabschnitt {
	return obj as ENMLernabschnitt;
}
