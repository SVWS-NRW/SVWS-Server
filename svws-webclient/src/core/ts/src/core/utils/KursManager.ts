import { JavaObject } from '../../java/lang/JavaObject';
import { KursListeEintrag } from '../../core/data/kurse/KursListeEintrag';
import { HashMap } from '../../java/util/HashMap';
import { Collection } from '../../java/util/Collection';
import { List, cast_java_util_List } from '../../java/util/List';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Vector } from '../../java/util/Vector';

export class KursManager extends JavaObject {

	/**
	 * Die Kurse, die im Manager vorhanden sind
	 */
	private readonly _kurse : List<KursListeEintrag> = new Vector();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID
	 */
	private readonly _map : HashMap<number, KursListeEintrag> = new HashMap();


	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 */
	public constructor();

	/**
	 * Erstellt einen neuen Manager mit den übergebenen Kursen.
	 *
	 * @param kurse die Liste mit den Kursen
	 */
	public constructor(kurse : List<KursListeEintrag>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : List<KursListeEintrag>) {
		super();
		if ((typeof __param0 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('java.util.List'))) || (__param0 === null))) {
			const kurse : List<KursListeEintrag> = cast_java_util_List(__param0);
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
	private addInternal(kurs : KursListeEintrag) : boolean {
		if (kurs.id < 0)
			throw new DeveloperNotificationException("Die Kurs-ID darf nicht negativ sein!")
		const old : KursListeEintrag | null = this._map.put(kurs.id, kurs);
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
	public add(kurs : KursListeEintrag) : boolean {
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
	public addAll(kurse : Collection<KursListeEintrag>) : boolean {
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
	public get(id : number) : KursListeEintrag | null {
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
	public getOrException(pKursID : number) : KursListeEintrag {
		const kurs : KursListeEintrag | null = this._map.get(pKursID);
		if (kurs === null)
			throw new DeveloperNotificationException("KursListeEintrag mit id=" + pKursID + " gibt es nicht.")
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
	public kurse() : List<KursListeEintrag> {
		return this._kurse;
	}

	/**
	 * Erstellt aus der internen Liste der Kurse ein Array
	 *
	 * @return ein Array mit den Kursen
	 */
	public values() : Array<KursListeEintrag> {
		return this._kurse.toArray(Array(0).fill(null));
	}

	/**
	 * Erstellt aus der internen Liste einen Vector mit den Daten
	 *
	 * @return ein Vector mit den Kursen
	 */
	public toVector() : Vector<KursListeEintrag> {
		const result : Vector<KursListeEintrag> = new Vector();
		for (const kurs of this._kurse)
			result.add(kurs);
		return result;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.KursManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_KursManager(obj : unknown) : KursManager {
	return obj as KursManager;
}
