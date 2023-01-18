import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { GostLeistungenFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostLeistungenFachwahl } from '../../../core/data/gost/GostLeistungenFachwahl';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { Sprachendaten, cast_de_nrw_schule_svws_core_data_schueler_Sprachendaten } from '../../../core/data/schueler/Sprachendaten';

export class GostLeistungen extends JavaObject {

	/**
	 * Die ID des Schülers, dessen Leistungen in diesem Objekt gespeichert sind. 
	 */
	public id : number = 0;

	/**
	 * Das aktuelle Schuljahr, welches dem Schüler zugeordnet ist. 
	 */
	public aktuellesSchuljahr : Number | null = null;

	/**
	 * Der Jahrgang, in dem sich der Schüler in dem aktuellen Schuljahr befindet. 
	 */
	public aktuellerJahrgang : String | null = null;

	/**
	 * Die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen des Schülers 
	 */
	public sprachendaten : Sprachendaten | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern der Schüler einem bilingualen Bildungsgang angehört. 
	 */
	public bilingualeSprache : String | null = null;

	/**
	 * Das Thema des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte. 
	 */
	public projektkursThema : String | null = null;

	/**
	 * Das Kürzel des ersten Leitfaches des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte. 
	 */
	public projektkursLeitfach1Kuerzel : String | null = null;

	/**
	 * Das Kürzel eines zweiten Leitfaches des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte und der Projektkurs ein zweites Leitfach hat. 
	 */
	public projektkursLeitfach2Kuerzel : String | null = null;

	/**
	 * Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind. 
	 */
	public readonly bewertetesHalbjahr : Array<boolean> = Array(6).fill(false);

	/**
	 * Die einzelnen Fachwahlen des Schülers. 
	 */
	public readonly faecher : Vector<GostLeistungenFachwahl> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostLeistungen'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLeistungen {
		const obj = JSON.parse(json);
		const result = new GostLeistungen();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.aktuellesSchuljahr = typeof obj.aktuellesSchuljahr === "undefined" ? null : obj.aktuellesSchuljahr === null ? null : Number(obj.aktuellesSchuljahr);
		result.aktuellerJahrgang = typeof obj.aktuellerJahrgang === "undefined" ? null : obj.aktuellerJahrgang === null ? null : String(obj.aktuellerJahrgang);
		result.sprachendaten = ((typeof obj.sprachendaten === "undefined") || (obj.sprachendaten === null)) ? null : Sprachendaten.transpilerFromJSON(JSON.stringify(obj.sprachendaten));
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : String(obj.bilingualeSprache);
		result.projektkursThema = typeof obj.projektkursThema === "undefined" ? null : obj.projektkursThema === null ? null : String(obj.projektkursThema);
		result.projektkursLeitfach1Kuerzel = typeof obj.projektkursLeitfach1Kuerzel === "undefined" ? null : obj.projektkursLeitfach1Kuerzel === null ? null : String(obj.projektkursLeitfach1Kuerzel);
		result.projektkursLeitfach2Kuerzel = typeof obj.projektkursLeitfach2Kuerzel === "undefined" ? null : obj.projektkursLeitfach2Kuerzel === null ? null : String(obj.projektkursLeitfach2Kuerzel);
		for (let i : number = 0; i < obj.bewertetesHalbjahr.length; i++) {
			result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
		}
		if (!!obj.faecher) {
			for (let elem of obj.faecher) {
				result.faecher?.add(GostLeistungenFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLeistungen) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"aktuellesSchuljahr" : ' + ((!obj.aktuellesSchuljahr) ? 'null' : obj.aktuellesSchuljahr.valueOf()) + ',';
		result += '"aktuellerJahrgang" : ' + ((!obj.aktuellerJahrgang) ? 'null' : '"' + obj.aktuellerJahrgang.valueOf() + '"') + ',';
		result += '"sprachendaten" : ' + ((!obj.sprachendaten) ? 'null' : Sprachendaten.transpilerToJSON(obj.sprachendaten)) + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		result += '"projektkursThema" : ' + ((!obj.projektkursThema) ? 'null' : '"' + obj.projektkursThema.valueOf() + '"') + ',';
		result += '"projektkursLeitfach1Kuerzel" : ' + ((!obj.projektkursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach1Kuerzel.valueOf() + '"') + ',';
		result += '"projektkursLeitfach2Kuerzel" : ' + ((!obj.projektkursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach2Kuerzel.valueOf() + '"') + ',';
		if (!obj.bewertetesHalbjahr) {
			result += '"bewertetesHalbjahr" : []';
		} else {
			result += '"bewertetesHalbjahr" : [ ';
			for (let i : number = 0; i < obj.bewertetesHalbjahr.length; i++) {
				let elem = obj.bewertetesHalbjahr[i];
				result += JSON.stringify(elem);
				if (i < obj.bewertetesHalbjahr.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i : number = 0; i < obj.faecher.size(); i++) {
				let elem = obj.faecher.get(i);
				result += GostLeistungenFachwahl.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLeistungen>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.aktuellesSchuljahr !== "undefined") {
			result += '"aktuellesSchuljahr" : ' + ((!obj.aktuellesSchuljahr) ? 'null' : obj.aktuellesSchuljahr.valueOf()) + ',';
		}
		if (typeof obj.aktuellerJahrgang !== "undefined") {
			result += '"aktuellerJahrgang" : ' + ((!obj.aktuellerJahrgang) ? 'null' : '"' + obj.aktuellerJahrgang.valueOf() + '"') + ',';
		}
		if (typeof obj.sprachendaten !== "undefined") {
			result += '"sprachendaten" : ' + ((!obj.sprachendaten) ? 'null' : Sprachendaten.transpilerToJSON(obj.sprachendaten)) + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : '"' + obj.bilingualeSprache.valueOf() + '"') + ',';
		}
		if (typeof obj.projektkursThema !== "undefined") {
			result += '"projektkursThema" : ' + ((!obj.projektkursThema) ? 'null' : '"' + obj.projektkursThema.valueOf() + '"') + ',';
		}
		if (typeof obj.projektkursLeitfach1Kuerzel !== "undefined") {
			result += '"projektkursLeitfach1Kuerzel" : ' + ((!obj.projektkursLeitfach1Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach1Kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.projektkursLeitfach2Kuerzel !== "undefined") {
			result += '"projektkursLeitfach2Kuerzel" : ' + ((!obj.projektkursLeitfach2Kuerzel) ? 'null' : '"' + obj.projektkursLeitfach2Kuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.bewertetesHalbjahr !== "undefined") {
			let a = obj.bewertetesHalbjahr;
			if (!a) {
				result += '"bewertetesHalbjahr" : []';
			} else {
				result += '"bewertetesHalbjahr" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i : number = 0; i < obj.faecher.size(); i++) {
					let elem = obj.faecher.get(i);
					result += GostLeistungenFachwahl.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_gost_GostLeistungen(obj : unknown) : GostLeistungen {
	return obj as GostLeistungen;
}
