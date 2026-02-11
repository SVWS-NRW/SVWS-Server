import { JavaObject } from '../../../java/lang/JavaObject';
import { BKGymFach } from '../../../core/data/bk/abi/BKGymFach';
import { Fach } from '../../../asd/types/fach/Fach';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { Collection } from '../../../java/util/Collection';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { FachKatalogEintrag } from '../../../asd/data/fach/FachKatalogEintrag';
import { HashSet } from '../../../java/util/HashSet';

export class BKGymFaecherManager extends JavaObject {

	/**
	 * Die Menge aller Fremdsprachen, welche am beruflichen Gymnasium ggf. vorkommen können
	 */
	public static readonly alleFremdsprachen: JavaSet<Fach> = java_util_Set_of(Fach.E, Fach.C, Fach.F, Fach.G, Fach.H, Fach.I, Fach.K, Fach.L, Fach.N, Fach.O, Fach.R, Fach.S, Fach.T, Fach.Z);

	/**
	 * Die Liste der Fächer, die im Manager vorhanden sind.
	 */
	private readonly faecher: List<BKGymFach> = new ArrayList<BKGymFach>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID
	 */
	private readonly map: HashMap<number, BKGymFach> = new HashMap<number, BKGymFach>();

	/**
	 * das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet - relevant wg. der Fächergültigkeit laut ASD
	 */
	private readonly schuljahr: number;

	/**
	 * Fachbezeichnungen, die doppelt in der Fächerliste eingetragen sind
	 */
	private readonly doppelteFaecher: JavaSet<string> = new HashSet<string>();


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param schuljahr         das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher           die Liste mit den Fächern
	 */
	public constructor(schuljahr: number, faecher: List<BKGymFach>) {
		super();
		this.schuljahr = schuljahr;
		this.addAll(faecher);
	}

	/**
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die hinzuzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	private addAll(faecher: Collection<BKGymFach>): boolean {
		const setOfBezeichnung: JavaSet<string> | null = new HashSet<string>();
		let result: boolean = true;
		for (const fach of faecher) {
			if (!this.addFachInternal(fach))
				result = false;
			if (setOfBezeichnung.contains(fach.bezeichnung))
				this.doppelteFaecher.add(fach.bezeichnung);
			else
				setOfBezeichnung.add(fach.bezeichnung);
		}
		return result;
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
	private addFachInternal(fach: BKGymFach): boolean {
		DeveloperNotificationException.ifSmaller("fach.id", fach.id, 0);
		if (this.map.containsKey(fach.id))
			return false;
		if (fach.bezeichnung === null)
			return false;
		const zf: Fach | null = Fach.getBySchluesselOrDefault(fach.kuerzel);
		const fke: FachKatalogEintrag | null = zf.daten(this.schuljahr);
		if (fke === null)
			return false;
		this.map.put(fach.id, fach);
		return true;
	}

	/**
	 * Getter für die doppelten Fächer als List
	 *
	 * @return die Liste der doppelten Fächer
	 */
	public getDoppelteFaecher(): List<string> {
		return new ArrayList<string>(this.doppelteFaecher);
	}

	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public isEmpty(): boolean {
		return this.faecher.isEmpty();
	}

	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public getFaecher(): List<BKGymFach> {
		return new ArrayList<BKGymFach>(this.faecher);
	}

	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 *
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public get(id: number): BKGymFach | null {
		return this.map.get(id);
	}

	/**
	 * liefert zu einer fachID die Fachbezeichnung
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die Fachbezeichnung
	 */
	public getBezeichnungByFachID(id: number): string {
		const fach: BKGymFach | null = this.map.get(id);
		if (fach === null)
			return "";
		if (fach.bezeichnung === null)
			return "";
		return fach.bezeichnung;
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
	public getOrException(idFach: number): BKGymFach {
		return DeveloperNotificationException.ifMapGetIsNull(this.map, idFach);
	}

	/**
	 * Prüft, ob es auch bei dem Fach um eine Fremdsprache handelt oder nicht
	 *
	 * @param fach   das zu überprüfende Fach
	 *
	 * @return true, falls es sich um eine Fremdsprache handelt und ansonsten null
	 */
	public static istFremdsprache(fach: BKGymFach): boolean {
		return BKGymFaecherManager.alleFremdsprachen.contains(Fach.getBySchluesselOrDefault(fach.kuerzel));
	}

	/**
	 * Liefert das Kürzel der Sprache (ohne Jahrgang) zurück, falls es sich um eine Sprache handelt.
	 *
	 * @param fach   das Fach des beruflichen Gymnasiums
	 *
	 * @return das einstellige Sprach-Kürzel oder null
	 */
	public static getFremdsprache(fach: BKGymFach): string | null {
		if ((JavaObject.equalsTranspiler("", (fach.kuerzel))) || !BKGymFaecherManager.istFremdsprache(fach))
			return null;
		return fach.kuerzel.substring(0, 1).toUpperCase();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.bk.BKGymFaecherManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.bk.BKGymFaecherManager'].includes(name);
	}

	public static readonly class = new Class<BKGymFaecherManager>('de.svws_nrw.core.utils.bk.BKGymFaecherManager');

}

export function cast_de_svws_nrw_core_utils_bk_BKGymFaecherManager(obj: unknown): BKGymFaecherManager {
	return obj as BKGymFaecherManager;
}
