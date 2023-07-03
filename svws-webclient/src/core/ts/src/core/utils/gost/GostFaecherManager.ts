import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { GostFach } from '../../../core/data/gost/GostFach';
import { Fachgruppe } from '../../../core/types/fach/Fachgruppe';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import type { Collection } from '../../../java/util/Collection';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../java/util/Comparator';

export class GostFaecherManager extends JavaObject {

	/**
	 * Sortiert die Fächer anhand ihrer konfigurierten Sortierung
	 */
	public static readonly comp : Comparator<GostFach> = { compare : (a: GostFach | null, b: GostFach | null) => {
		const va : number = (a === null) ? JavaInteger.MIN_VALUE : a.sortierung;
		const vb : number = (b === null) ? JavaInteger.MIN_VALUE : b.sortierung;
		return JavaInteger.compare(va, vb);
	} };

	/**
	 * Die Liste der Fächer, die im Manager vorhanden sind.
	 */
	private readonly _faecher : List<GostFach> = new ArrayList();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID
	 */
	private readonly _map : HashMap<number, GostFach> = new HashMap();

	/**
	 * Eine Map für den schnellen Zugriff auf die Leitfächer
	 */
	private readonly _leitfaecher : List<GostFach> = new ArrayList();


	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 */
	public constructor();

	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param faecher   die Liste mit den Fächern
	 */
	public constructor(faecher : List<GostFach>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : List<GostFach>) {
		super();
		if ((typeof __param0 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null))) {
			const faecher : List<GostFach> = cast_java_util_List(__param0);
			this.addAll(faecher);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu.
	 * Die interne Sortierung wird nicht korrigiert
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 * @throws DeveloperNotificationException Falls die ID des Faches nagativ ist.
	 */
	private addInternal(fach : GostFach) : boolean {
		if (fach.id < 0)
			throw new DeveloperNotificationException("Die Fach-ID darf nicht negativ sein!")
		const old : GostFach | null = this._map.put(fach.id, fach);
		if (old !== null)
			return false;
		const added : boolean = this._faecher.add(fach);
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
			return added;
		const fg : Fachgruppe | null = ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getFachgruppe();
		if ((fg as unknown === Fachgruppe.FG_VX as unknown) || (fg as unknown === Fachgruppe.FG_PX as unknown))
			return added;
		this._leitfaecher.add(fach);
		return added;
	}

	/**
	 * Führt eine Sortierung der Fächer anhand des Sortierungsfeldes durch.
	 */
	private sort() : void {
		this._faecher.sort(GostFaecherManager.comp);
		this._leitfaecher.sort(GostFaecherManager.comp);
	}

	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu und
	 * passt intern die Sortierung der Fächer an.
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 */
	public add(fach : GostFach) : boolean {
		const result : boolean = this.addInternal(fach);
		this.sort();
		return result;
	}

	/**
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die einzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	public addAll(faecher : Collection<GostFach>) : boolean {
		let result : boolean = true;
		for (const fach of faecher)
			if (!this.addInternal(fach))
				result = false;
		this.sort();
		return result;
	}

	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public get(id : number) : GostFach | null {
		return this._map.get(id);
	}

	/**
	 * Liefert das Fach mit der angegebenen ID zurück.
	 *
	 * @param pFachID die ID des gesuchten Faches.
	 * @return Das Fach mit der angegebenen ID zurück.
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public getOrException(pFachID : number) : GostFach {
		const fach : GostFach | null = this._map.get(pFachID);
		if (fach === null)
			throw new DeveloperNotificationException("GostFach mit id=" + pFachID + " gibt es nicht.")
		return fach;
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
	public faecher() : List<GostFach> {
		return this._faecher;
	}

	/**
	 * Liefert die interne Liste mit den Leitfächern zurück.
	 *
	 * @return die interne Liste mit den Leitfächern
	 */
	public getLeitfaecher() : List<GostFach> {
		return this._leitfaecher;
	}

	/**
	 * Erstellt aus der internen Liste der Fächer ein Array
	 *
	 * @return ein Array mit den Fächern
	 */
	public values() : Array<GostFach> {
		return this._faecher.toArray(Array(0).fill(null));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostFaecherManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostFaecherManager(obj : unknown) : GostFaecherManager {
	return obj as GostFaecherManager;
}
