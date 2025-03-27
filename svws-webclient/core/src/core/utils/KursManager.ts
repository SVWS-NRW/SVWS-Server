import { JavaObject } from '../../java/lang/JavaObject';
import { KursDaten } from '../../asd/data/kurse/KursDaten';
import { HashMap } from '../../java/util/HashMap';
import { ArrayList } from '../../java/util/ArrayList';
import type { Collection } from '../../java/util/Collection';
import type { List } from '../../java/util/List';
import { cast_java_util_List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';

export class KursManager extends JavaObject {

	/**
	 * Die Kurse, die im Manager vorhanden sind
	 */
	private readonly _kurse : List<KursDaten> = new ArrayList<KursDaten>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID
	 */
	private readonly _map : HashMap<number, KursDaten> = new HashMap<number, KursDaten>();


	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 */
	public constructor();

	/**
	 * Erstellt einen neuen Manager mit den übergebenen Kursen.
	 *
	 * @param kurse die Liste mit den Kursen
	 */
	public constructor(kurse : List<KursDaten>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : List<KursDaten>) {
		super();
		if ((__param0 === undefined)) {
			// empty method body
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('java.util.List'))) || (__param0 === null))) {
			const kurse : List<KursDaten> = cast_java_util_List(__param0);
			this.addAll(kurse);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt den übergebenen Kurs zu diesem Manager hinzu. Die interne Sortierung
	 * wird nicht korrigiert
	 *
	 * @param kurs der hinzuzufügende Kurs
	 *
	 * @return true, falls der Kurs hinzugefügt wurde
	 * @throws DeveloperNotificationException Falls die ID des Kurses nagativ ist.
	 */
	private addInternal(kurs : KursDaten) : boolean {
		if (kurs.id < 0)
			throw new DeveloperNotificationException("Die Kurs-ID darf nicht negativ sein!")
		const old : KursDaten | null = this._map.put(kurs.id, kurs);
		if (old !== null)
			return false;
		return this._kurse.add(kurs);
	}

	/**
	 * Führt eine Sortierung der Kurse anhand des Sortierungsfeldes durch.
	 */
	private sort() : void {
		// empty block
	}

	/**
	 * Fügt den übergebenen Kurs zu diesem Manager hinzu und passt intern die
	 * Sortierung der Kurse an.
	 *
	 * @param kurs der hinzuzufügende Kurs
	 *
	 * @return true, falls der Kurs hinzugefügt wurde
	 */
	public add(kurs : KursDaten) : boolean {
		const result : boolean = this.addInternal(kurs);
		this.sort();
		return result;
	}

	/**
	 * Fügt die Kurse in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param kurse die einzufügenden Kurse
	 *
	 * @return true, falls <i>alle</i> Kurse eingefügt wurden, sonst false
	 */
	public addAll(kurse : Collection<KursDaten>) : boolean {
		let result : boolean = true;
		for (const kurs of kurse)
			if (!this.addInternal(kurs))
				result = false;
		this.sort();
		return result;
	}

	/**
	 * Gibt den Kurs mit der angegebenen ID zurück oder null, falls es den Kurs
	 * nicht gibt.
	 *
	 * @param id die ID des gesuchten Kurses
	 * @return Der Kurs mit der angegebenen ID oder null, falls es den Kurs nicht
	 *         gibt.
	 */
	public get(id : number) : KursDaten | null {
		return this._map.get(id);
	}

	/**
	 * Liefert den Kurs mit der angegebenen ID zurück.
	 *
	 * @param pKursID die ID des gesuchten Kurses
	 * @return den Kurs mit der angegebenen ID
	 * @throws DeveloperNotificationException falls ein Kurs mit der ID nicht
	 *                                        bekannt ist
	 */
	public getOrException(pKursID : number) : KursDaten {
		const kurs : KursDaten | null = this._map.get(pKursID);
		if (kurs === null)
			throw new DeveloperNotificationException("KursDaten mit id=" + pKursID + " gibt es nicht.")
		return kurs;
	}

	/**
	 * Gibt zurück, ob die Liste der Kurse leer ist
	 *
	 * @return true, wenn die Liste der Kurse leer ist.
	 */
	public isEmpty() : boolean {
		return this._kurse.isEmpty();
	}

	/**
	 * Liefert die interne Liste der Kurse. Diese sollte nicht verändert werden.
	 *
	 * @return die interne Liste der Kurse
	 */
	public kurse() : List<KursDaten> {
		return this._kurse;
	}

	/**
	 * Erstellt aus der internen Liste der Kurse ein Array
	 *
	 * @return ein Array mit den Kursen
	 */
	public values() : Array<KursDaten> {
		return this._kurse.toArray(Array(0).fill(null));
	}

	/**
	 * Erstellt aus der internen Liste einen Vector mit den Daten
	 *
	 * @return ein Vector mit den Kursen
	 */
	public toList() : List<KursDaten> {
		const result : List<KursDaten> = new ArrayList<KursDaten>();
		for (const kurs of this._kurse)
			result.add(kurs);
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.KursManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.KursManager'].includes(name);
	}

	public static class = new Class<KursManager>('de.svws_nrw.core.utils.KursManager');

}

export function cast_de_svws_nrw_core_utils_KursManager(obj : unknown) : KursManager {
	return obj as KursManager;
}
