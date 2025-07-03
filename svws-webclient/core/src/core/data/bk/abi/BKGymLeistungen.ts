import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import { BKGymLeistungenFach } from '../../../../core/data/bk/abi/BKGymLeistungenFach';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { Sprachendaten } from '../../../../asd/data/schueler/Sprachendaten';

export class BKGymLeistungen extends JavaObject {

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
	 * Gibt für die einzelnen Halbjahre an, ob gewertete Leistungsdaten vorhanden sind.
	 */
	public readonly bewertetesHalbjahr : Array<boolean> = Array(6).fill(false);

	/**
	 * Die einzelnen Fächer des Schülers mit Leistungen.
	 */
	public readonly faecher : List<BKGymLeistungenFach> = new ArrayList<BKGymLeistungenFach>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.bk.abi.BKGymLeistungen';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.bk.abi.BKGymLeistungen'].includes(name);
	}

	public static class = new Class<BKGymLeistungen>('de.svws_nrw.core.data.bk.abi.BKGymLeistungen');

	public static transpilerFromJSON(json : string): BKGymLeistungen {
		const obj = JSON.parse(json) as Partial<BKGymLeistungen>;
		const result = new BKGymLeistungen();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.aktuellesSchuljahr = (obj.aktuellesSchuljahr === undefined) ? null : obj.aktuellesSchuljahr === null ? null : obj.aktuellesSchuljahr;
		result.aktuellerJahrgang = (obj.aktuellerJahrgang === undefined) ? null : obj.aktuellerJahrgang === null ? null : obj.aktuellerJahrgang;
		result.sprachendaten = ((obj.sprachendaten === undefined) || (obj.sprachendaten === null)) ? null : Sprachendaten.transpilerFromJSON(JSON.stringify(obj.sprachendaten));
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.bewertetesHalbjahr !== undefined) {
			for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
				result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
			}
		}
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(BKGymLeistungenFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKGymLeistungen) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"aktuellesSchuljahr" : ' + ((obj.aktuellesSchuljahr === null) ? 'null' : obj.aktuellesSchuljahr.toString()) + ',';
		result += '"aktuellerJahrgang" : ' + ((obj.aktuellerJahrgang === null) ? 'null' : JSON.stringify(obj.aktuellerJahrgang)) + ',';
		result += '"sprachendaten" : ' + ((obj.sprachendaten === null) ? 'null' : Sprachendaten.transpilerToJSON(obj.sprachendaten)) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
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
			result += BKGymLeistungenFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKGymLeistungen>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.aktuellesSchuljahr !== undefined) {
			result += '"aktuellesSchuljahr" : ' + ((obj.aktuellesSchuljahr === null) ? 'null' : obj.aktuellesSchuljahr.toString()) + ',';
		}
		if (obj.aktuellerJahrgang !== undefined) {
			result += '"aktuellerJahrgang" : ' + ((obj.aktuellerJahrgang === null) ? 'null' : JSON.stringify(obj.aktuellerJahrgang)) + ',';
		}
		if (obj.sprachendaten !== undefined) {
			result += '"sprachendaten" : ' + ((obj.sprachendaten === null) ? 'null' : Sprachendaten.transpilerToJSON(obj.sprachendaten)) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
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
				result += BKGymLeistungenFach.transpilerToJSON(elem);
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

export function cast_de_svws_nrw_core_data_bk_abi_BKGymLeistungen(obj : unknown) : BKGymLeistungen {
	return obj as BKGymLeistungen;
}
