import { JavaObject } from '../../../java/lang/JavaObject';
import { BKGymFach, cast_de_svws_nrw_core_data_bk_abi_BKGymFach } from '../../../core/data/bk/abi/BKGymFach';
import { Fach } from '../../../asd/types/fach/Fach';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../java/util/Comparator';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { Collection } from '../../../java/util/Collection';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { FachKatalogEintrag } from '../../../asd/data/fach/FachKatalogEintrag';

export class BKGymFaecherManager extends JavaObject {

	/**
	 * Die Menge aller Fremdsprachen, welche am beruflichen Gymnasium ggf. vorkommen können
	 */
	public static readonly alleFremdsprachen : JavaSet<Fach> = java_util_Set_of(Fach.E, Fach.C, Fach.C0, Fach.C5, Fach.C6, Fach.C7, Fach.C8, Fach.C9, Fach.F, Fach.F0, Fach.F5, Fach.F6, Fach.F7, Fach.F8, Fach.F9, Fach.G, Fach.G0, Fach.G5, Fach.G6, Fach.G7, Fach.G8, Fach.G9, Fach.H, Fach.H0, Fach.H5, Fach.H6, Fach.H7, Fach.H8, Fach.H9, Fach.I, Fach.I0, Fach.I5, Fach.I6, Fach.I7, Fach.I8, Fach.I9, Fach.K, Fach.K0, Fach.K5, Fach.K6, Fach.K7, Fach.K8, Fach.K9, Fach.L, Fach.L0, Fach.L5, Fach.L6, Fach.L7, Fach.L8, Fach.L9, Fach.N, Fach.N0, Fach.N5, Fach.N6, Fach.N7, Fach.N8, Fach.N9, Fach.O, Fach.O0, Fach.O5, Fach.O6, Fach.O7, Fach.O8, Fach.O9, Fach.R, Fach.R0, Fach.R5, Fach.R6, Fach.R7, Fach.R8, Fach.R9, Fach.S, Fach.S0, Fach.S5, Fach.S6, Fach.S7, Fach.S8, Fach.S9, Fach.T, Fach.T0, Fach.T5, Fach.T6, Fach.T7, Fach.T8, Fach.T9, Fach.Z, Fach.Z0, Fach.Z5, Fach.Z6, Fach.Z7, Fach.Z8, Fach.Z9);

	/**
	 * Sortiert die Fächer anhand ihrer konfigurierten Sortierung
	 */
	public static readonly comp : Comparator<BKGymFach | null> = { compare : (a: BKGymFach | null, b: BKGymFach | null) => {
		return -1;
	} };

	/**
	 * Die Liste der Fächer, die im Manager vorhanden sind.
	 */
	private readonly _faecher : List<BKGymFach> = new ArrayList<BKGymFach>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID
	 */
	private readonly _map : HashMap<number, BKGymFach> = new HashMap<number, BKGymFach>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fächer anhand der Bezeichnung des Faches
	 */
	private readonly _mapByBezeichnung : HashMap<string, List<BKGymFach>> = new HashMap<string, List<BKGymFach>>();

	/**
	 * das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet - relevant wg. der Fächergültigkeit laut ASD
	 */
	private readonly schuljahr : number;


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param schuljahr         das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher           die Liste mit den Fächern
	 * @param schulgliederung   die Schulgliederung
	 * @param fks               der fünfstellige Fachklassen-Schlüssen (mit laufender Nummer in den letzen beiden Stellen)
	 */
	public constructor(schuljahr : number, faecher : List<BKGymFach>, schulgliederung : Schulgliederung, fks : string) {
		super();
		this.schuljahr = schuljahr;
		this.addAll(faecher);
	}

	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu. Die interne Sortierung wird nicht korrigiert.
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die ID des Faches negativ ist.
	 */
	private addFachInternal(fach : BKGymFach) : boolean {
		DeveloperNotificationException.ifSmaller("fach.id", fach.id, 0);
		if (this._map.containsKey(fach.id))
			return false;
		if (fach.bezeichnung === null)
			return false;
		const zf : Fach | null = Fach.getBySchluesselOrDefault(fach.kuerzel);
		const fke : FachKatalogEintrag | null = zf.daten(this.schuljahr);
		if (fke === null)
			return false;
		this._map.put(fach.id, fach);
		let listForBezeichnung : List<BKGymFach> | null = this._mapByBezeichnung.get(fach.bezeichnung);
		if (listForBezeichnung === null) {
			listForBezeichnung = new ArrayList();
			this._mapByBezeichnung.put(fach.bezeichnung, listForBezeichnung);
		}
		listForBezeichnung.add(fach);
		return this._faecher.add(fach);
	}

	/**
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die hinzuzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	private addAll(faecher : Collection<BKGymFach>) : boolean {
		let result : boolean = true;
		for (const fach of faecher)
			if (!this.addFachInternal(fach))
				result = false;
		this.sort();
		return result;
	}

	/**
	 * Führt eine Sortierung der Fächer anhand des Sortierungsfeldes durch.
	 */
	private sort() : void {
		this._faecher.sort(BKGymFaecherManager.comp);
	}

	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public isEmpty() : boolean {
		return this._faecher.isEmpty();
	}

	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public faecher() : List<BKGymFach> {
		return new ArrayList<BKGymFach>(this._faecher);
	}

	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 *
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public get(id : number) : BKGymFach | null {
		return this._map.get(id);
	}

	/**
	 * Liefert das Fach mit der angegebenen ID zurück.
	 *
	 * @param idFach   die ID des gesuchten Faches.
	 *
	 * @return Das Fach mit der angegebenen ID zurück.
	 *
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public getOrException(idFach : number) : BKGymFach {
		return DeveloperNotificationException.ifMapGetIsNull(this._map, idFach);
	}

	/**
	 * Prüft, ob es auch bei dem Fach mit dem angegeben Statistik-Kürzel
	 * um eine Fremdsprache handelt oder nicht
	 *
	 * @param kuerzel   das zu überprüfende Statistik-Kürzel
	 *
	 * @return true, falls es sich um eine Fremdsprache handelt und ansonsten null
	 */
	public static istFremdsprache(kuerzel : string) : boolean;

	/**
	 * Prüft, ob es auch bei dem Fach um eine Fremdsprache handelt oder nicht
	 *
	 * @param fach   das zu überprüfende Fach
	 *
	 * @return true, falls es sich um eine Fremdsprache handelt und ansonsten null
	 */
	public static istFremdsprache(fach : BKGymFach) : boolean;

	/**
	 * Implementation for method overloads of 'istFremdsprache'
	 */
	public static istFremdsprache(__param0 : BKGymFach | string) : boolean {
		if (((__param0 !== undefined) && (typeof __param0 === "string"))) {
			const kuerzel : string = __param0;
			return BKGymFaecherManager.alleFremdsprachen.contains(Fach.getBySchluesselOrDefault(kuerzel));
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.bk.abi.BKGymFach'))))) {
			const fach : BKGymFach = cast_de_svws_nrw_core_data_bk_abi_BKGymFach(__param0);
			return BKGymFaecherManager.alleFremdsprachen.contains(Fach.getBySchluesselOrDefault(fach.kuerzel));
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert das Kürzel der Sprache (ohne Jahrgang) zurück, falls es sich um eine Sprache handelt.
	 *
	 * @param fach   das Fach des beruflichen Gymnasiums
	 *
	 * @return das einstellige Sprach-Kürzel oder null
	 */
	public static getFremdsprache(fach : BKGymFach) : string | null {
		if ((fach.kuerzel === null) || (JavaObject.equalsTranspiler("", (fach.kuerzel))) || !BKGymFaecherManager.istFremdsprache(fach))
			return null;
		return fach.kuerzel.substring(0, 1).toUpperCase();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.bk.BKGymFaecherManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.bk.BKGymFaecherManager'].includes(name);
	}

	public static class = new Class<BKGymFaecherManager>('de.svws_nrw.core.utils.bk.BKGymFaecherManager');

}

export function cast_de_svws_nrw_core_utils_bk_BKGymFaecherManager(obj : unknown) : BKGymFaecherManager {
	return obj as BKGymFaecherManager;
}
