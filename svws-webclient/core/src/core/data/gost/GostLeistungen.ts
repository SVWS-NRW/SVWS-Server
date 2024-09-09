import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostLeistungenFachwahl } from '../../../core/data/gost/GostLeistungenFachwahl';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Sprachendaten } from '../../../core/data/schueler/Sprachendaten';

export class GostLeistungen extends JavaObject {

	/**
	 * Die ID des Schülers, dessen Leistungen in diesem Objekt gespeichert sind.
	 */
	public id : number = 0;

	/**
	 * Das aktuelle Schuljahr, welches dem Schüler zugeordnet ist.
	 */
	public aktuellesSchuljahr : number | null = null;

	/**
	 * Der Jahrgang, in dem sich der Schüler in dem aktuellen Schuljahr befindet.
	 */
	public aktuellerJahrgang : string | null = null;

	/**
	 * Die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen des Schülers
	 */
	public sprachendaten : Sprachendaten | null = null;

	/**
	 * Das einstellige Kürzel der bilingualen Sprache, sofern der Schüler einem bilingualen Bildungsgang angehört.
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Das Thema des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte.
	 */
	public projektkursThema : string | null = null;

	/**
	 * Das Kürzel des ersten Leitfaches des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte.
	 */
	public projektkursLeitfach1Kuerzel : string | null = null;

	/**
	 * Das Kürzel eines zweiten Leitfaches des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte und der Projektkurs ein zweites Leitfach hat.
	 */
	public projektkursLeitfach2Kuerzel : string | null = null;

	/**
	 * Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind.
	 */
	public readonly bewertetesHalbjahr : Array<boolean> = Array(6).fill(false);

	/**
	 * Die einzelnen Fachwahlen des Schülers.
	 */
	public readonly faecher : List<GostLeistungenFachwahl> = new ArrayList<GostLeistungenFachwahl>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLeistungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLeistungen'].includes(name);
	}

	public static class = new Class<GostLeistungen>('de.svws_nrw.core.data.gost.GostLeistungen');

	public static transpilerFromJSON(json : string): GostLeistungen {
		const obj = JSON.parse(json) as Partial<GostLeistungen>;
		const result = new GostLeistungen();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.aktuellesSchuljahr = (obj.aktuellesSchuljahr === undefined) ? null : obj.aktuellesSchuljahr === null ? null : obj.aktuellesSchuljahr;
		result.aktuellerJahrgang = (obj.aktuellerJahrgang === undefined) ? null : obj.aktuellerJahrgang === null ? null : obj.aktuellerJahrgang;
		result.sprachendaten = ((obj.sprachendaten === undefined) || (obj.sprachendaten === null)) ? null : Sprachendaten.transpilerFromJSON(JSON.stringify(obj.sprachendaten));
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		result.projektkursThema = (obj.projektkursThema === undefined) ? null : obj.projektkursThema === null ? null : obj.projektkursThema;
		result.projektkursLeitfach1Kuerzel = (obj.projektkursLeitfach1Kuerzel === undefined) ? null : obj.projektkursLeitfach1Kuerzel === null ? null : obj.projektkursLeitfach1Kuerzel;
		result.projektkursLeitfach2Kuerzel = (obj.projektkursLeitfach2Kuerzel === undefined) ? null : obj.projektkursLeitfach2Kuerzel === null ? null : obj.projektkursLeitfach2Kuerzel;
		if (obj.bewertetesHalbjahr !== undefined) {
			for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
				result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(GostLeistungenFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostLeistungen) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"aktuellesSchuljahr" : ' + ((!obj.aktuellesSchuljahr) ? 'null' : obj.aktuellesSchuljahr.toString()) + ',';
		result += '"aktuellerJahrgang" : ' + ((!obj.aktuellerJahrgang) ? 'null' : JSON.stringify(obj.aktuellerJahrgang)) + ',';
		result += '"sprachendaten" : ' + ((!obj.sprachendaten) ? 'null' : Sprachendaten.transpilerToJSON(obj.sprachendaten)) + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"projektkursThema" : ' + ((!obj.projektkursThema) ? 'null' : JSON.stringify(obj.projektkursThema)) + ',';
		result += '"projektkursLeitfach1Kuerzel" : ' + ((!obj.projektkursLeitfach1Kuerzel) ? 'null' : JSON.stringify(obj.projektkursLeitfach1Kuerzel)) + ',';
		result += '"projektkursLeitfach2Kuerzel" : ' + ((!obj.projektkursLeitfach2Kuerzel) ? 'null' : JSON.stringify(obj.projektkursLeitfach2Kuerzel)) + ',';
		result += '"bewertetesHalbjahr" : [ ';
		for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
			const elem = obj.bewertetesHalbjahr[i];
			result += JSON.stringify(elem);
			if (i < obj.bewertetesHalbjahr.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += GostLeistungenFachwahl.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLeistungen>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.aktuellesSchuljahr !== undefined) {
			result += '"aktuellesSchuljahr" : ' + ((!obj.aktuellesSchuljahr) ? 'null' : obj.aktuellesSchuljahr.toString()) + ',';
		}
		if (obj.aktuellerJahrgang !== undefined) {
			result += '"aktuellerJahrgang" : ' + ((!obj.aktuellerJahrgang) ? 'null' : JSON.stringify(obj.aktuellerJahrgang)) + ',';
		}
		if (obj.sprachendaten !== undefined) {
			result += '"sprachendaten" : ' + ((!obj.sprachendaten) ? 'null' : Sprachendaten.transpilerToJSON(obj.sprachendaten)) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (obj.projektkursThema !== undefined) {
			result += '"projektkursThema" : ' + ((!obj.projektkursThema) ? 'null' : JSON.stringify(obj.projektkursThema)) + ',';
		}
		if (obj.projektkursLeitfach1Kuerzel !== undefined) {
			result += '"projektkursLeitfach1Kuerzel" : ' + ((!obj.projektkursLeitfach1Kuerzel) ? 'null' : JSON.stringify(obj.projektkursLeitfach1Kuerzel)) + ',';
		}
		if (obj.projektkursLeitfach2Kuerzel !== undefined) {
			result += '"projektkursLeitfach2Kuerzel" : ' + ((!obj.projektkursLeitfach2Kuerzel) ? 'null' : JSON.stringify(obj.projektkursLeitfach2Kuerzel)) + ',';
		}
		if (obj.bewertetesHalbjahr !== undefined) {
			const a = obj.bewertetesHalbjahr;
			result += '"bewertetesHalbjahr" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
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

}

export function cast_de_svws_nrw_core_data_gost_GostLeistungen(obj : unknown) : GostLeistungen {
	return obj as GostLeistungen;
}
