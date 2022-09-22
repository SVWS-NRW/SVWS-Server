import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchuelerStundenplanUnterricht, cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplanUnterricht } from '../../../core/data/stundenplan/SchuelerStundenplanUnterricht';
import { JavaSet, cast_java_util_Set } from '../../../java/util/JavaSet';
import { RuntimeException, cast_java_lang_RuntimeException } from '../../../java/lang/RuntimeException';
import { HashMap, cast_java_util_HashMap } from '../../../java/util/HashMap';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaMap, cast_java_util_Map } from '../../../java/util/JavaMap';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { SchuelerStundenplan, cast_de_nrw_schule_svws_core_data_stundenplan_SchuelerStundenplan } from '../../../core/data/stundenplan/SchuelerStundenplan';

export class SchuelerStundenplanManager extends JavaObject {

	private readonly _daten : SchuelerStundenplan;

	private readonly _mapWochenTypUnterricht : JavaMap<Number, List<SchuelerStundenplanUnterricht>>;

	private readonly _woche0Unterricht : List<SchuelerStundenplanUnterricht> | null;


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Stundenplandaten
	 * 
	 * @param daten die Stundenplandaten
	 */
	public constructor(daten : SchuelerStundenplan) {
		super();
		this._daten = daten;
		this._mapWochenTypUnterricht = new HashMap();
		for (let ssu of this._daten.unterricht) {
			let ssul : List<SchuelerStundenplanUnterricht> | null = this._mapWochenTypUnterricht.get(ssu.wochentyp);
			if (ssul === null) {
				ssul = new Vector();
				this._mapWochenTypUnterricht.put(ssu.wochentyp, ssul);
			}
			ssul.add(ssu);
		}
		this._woche0Unterricht = this._mapWochenTypUnterricht.get(0);
		this._mapWochenTypUnterricht.remove(0);
		if (this._woche0Unterricht !== null) {
			for (let l of this._mapWochenTypUnterricht.values()) {
				l.addAll(this._woche0Unterricht);
			}
		}
	}

	/**
	 * Gibt zurück, ob es unterschiedliche Wochentypen gibt.
	 * 
	 * @return {@code true}, falls es sich um unterschiedliche Wochentypen handelt,
	 *         {@code false}, falls es nur einen Typen gibt
	 */
	public isAbWochen() : boolean {
		return this._mapWochenTypUnterricht.size() > 0;
	}

	/**
	 * Gibt die Anzahl der Wochentypen zurück.
	 * 
	 * @return die Anzahl der Wochentypen
	 */
	public getAnzahlWochentypen() : number {
		return this.isAbWochen() ? this._mapWochenTypUnterricht.size() : 1;
	}

	/**
	 * Gibt die Wochentypen zurück.
	 * 
	 * @return die Wochentypen als Set von Integern
	 */
	public getWochentypen() : JavaSet<Number> {
		if (!this.isAbWochen()) {
		}
		return this._mapWochenTypUnterricht.keySet();
	}

	/**
	 * Gibt den Unterricht ohne A/B-Wochen zurück.
	 * 
	 * @return die Liste, die den Unterricht enthält
	 */
	public getUnterricht() : List<SchuelerStundenplanUnterricht>;

	/**
	 * Gibt den Unterricht in einem bestimmten Wochentyp zurück.
	 * 
	 * @param woche der Wochentyp
	 * 
	 * @return die Liste, die den Unterricht im angegebenen Wochentyp enthält
	 */
	public getUnterricht(woche : number) : List<SchuelerStundenplanUnterricht> | null;

	/**
	 * Implementation for method overloads of 'getUnterricht'
	 */
	public getUnterricht(__param0? : number) : List<SchuelerStundenplanUnterricht> | null {
		if ((typeof __param0 === "undefined")) {
			if (this.isAbWochen() || (this._woche0Unterricht === null)) 
				throw new RuntimeException("Der Unterricht ist für den Schüler in Wochentypen organisiert.")
			return this._woche0Unterricht;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			let woche : number = __param0 as number;
			if (!this.isAbWochen() || !this._mapWochenTypUnterricht.containsKey(woche)) {
			}
			return this._mapWochenTypUnterricht.get(woche);
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.stundenplan.SchuelerStundenplanManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_stundenplan_SchuelerStundenplanManager(obj : unknown) : SchuelerStundenplanManager {
	return obj as SchuelerStundenplanManager;
}
