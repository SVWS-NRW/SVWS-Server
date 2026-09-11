import { JavaObject } from '../../../java/lang/JavaObject';
import { Random } from '../../../java/util/Random';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvKlasse } from '../../../core/data/uv/UvKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../../core/logger/Logger';

export class UvAlgorithmusDynDatenKlasse extends JavaObject {

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	readonly log: Logger;

	/**
	 * Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung.
	 */
	readonly rnd: Random;

	/**
	 * Der Index im internen Array.
	 */
	readonly interneID: number;

	/**
	 * Die externe ID der DB/GUI.
	 */
	readonly uvID: number;

	/**
	 * Menge aller potentiellen Lehrkräfte, die als Klassenleitung (Leitung 1) in Frage kommen.
	 */
	readonly leitung1Potentiell: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Menge aller potentiellen Lehrkräfte, die als stellv. Klassenleitung (Leitung 2) in Frage kommen.
	 */
	readonly leitung2Potentiell: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Lehrkräfte, die aktuell Klassenleitung (Leitung 1) sind.
	 */
	readonly leitung1Zugeordnet: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Lehrkräfte, die aktuell stellv. Klassenleitung (Leitung 2) sind.
	 */
	readonly leitung2Zugeordnet: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Lehrkräfte, die aktuell Klassenleitung (Leitung 1) und fixiert sind.
	 */
	readonly leitung1ZugeordnetFixiert: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Lehrkräfte, die aktuell stellv. Klassenleitung (Leitung 2) und fixiert sind.
	 */
	readonly leitung2ZugeordnetFixiert: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Menge aller Regel-Objekte die sich auf diese Klasse beziehen.
	 */
	readonly regeln: List<UvAlgorithmusDynDatenRegel>;


	/**
	 * Der Konstruktor.
	 *
	 * @param log           Ein Logger für Debug-Zwecke.
	 * @param rnd           Ein Random-Objekt für zufällige Entscheidungen.
	 * @param index         Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param uvKlasse      Die UV-Klasse deren Daten teils kopiert werden.
	 * @param lehrerMenge   Alle {@link UvAlgorithmusDynDatenLehrkraft}-Objekte, die potentiell die Klasse Leiten.
	 */
	public constructor(log: Logger, rnd: Random, index: number, uvKlasse: UvKlasse, lehrerMenge: Array<UvAlgorithmusDynDatenLehrkraft>) {
		super();
		this.log = log;
		this.rnd = rnd;
		this.interneID = index;
		this.uvID = uvKlasse.id;
		this.leitung1Potentiell = new ArrayList();
		this.leitung2Potentiell = new ArrayList();
		for (const lehrkraft of lehrerMenge) {
			this.leitung1Potentiell.add(lehrkraft);
			this.leitung2Potentiell.add(lehrkraft);
		}
		this.leitung1Zugeordnet = new ArrayList();
		this.leitung1ZugeordnetFixiert = new ArrayList();
		this.leitung2Zugeordnet = new ArrayList();
		this.leitung2ZugeordnetFixiert = new ArrayList();
		this.regeln = new ArrayList();
	}

	public toString(): string {
		return JavaString.format(("UvAlgorithmusDynDatenKlasse { interneID=%d, uvID=%d, sollLeitung1=%d, prioLeitung1=%d, sollLeitung2=%d, prioLeitung2=%d}"), this.interneID, this.uvID, this.leitung1Zugeordnet.size(), this.leitung1ZugeordnetFixiert.size(), this.leitung2Zugeordnet.size(), this.leitung2ZugeordnetFixiert.size());
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft aktuell als Leitung 1 fixiert ist.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft aktuell als Leitung 1 fixiert ist.
	 */
	gibIstLeitung1Fixiert(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.leitung1ZugeordnetFixiert.contains(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft aktuell als Leitung 2 fixiert ist.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft aktuell als Leitung 2 fixiert ist.
	 */
	gibIstLeitung2Fixiert(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.leitung2ZugeordnetFixiert.contains(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft als Leitung 1 eingesetzt werden darf.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft als Leitung 1 eingesetzt werden darf.
	 */
	gibIstLeitung1Potentiell(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.leitung1Potentiell.contains(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft als Leitung 2 eingesetzt werden darf.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft als Leitung 2 eingesetzt werden darf.
	 */
	gibIstLeitung2Potentiell(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.leitung2Potentiell.contains(lehrkraft);
	}

	/**
	 * Liefert die Menge aller momentan zugeordneten zuerst Klassenlehrer (Leitung 1), gefolgt von stellv. Klassenlehrer (Leitung 2).
	 *
	 * @return die Menge aller momentan zugeordneten zuerst Klassenlehrer (Leitung 1), gefolgt von stellv. Klassenlehrer (Leitung 2).
	 */
	gibAktuelleMengeLeitung1und2(): List<UvAlgorithmusDynDatenLehrkraft> {
		const list: List<UvAlgorithmusDynDatenLehrkraft> = new ArrayList<UvAlgorithmusDynDatenLehrkraft>();
		list.addAll(this.leitung1ZugeordnetFixiert);
		list.addAll(this.leitung1Zugeordnet);
		list.addAll(this.leitung2ZugeordnetFixiert);
		list.addAll(this.leitung2Zugeordnet);
		return list;
	}

	/**
	 * Liefert die Menge aller momentan zugeordneten stellv. Klassenlehrer (Leitung 2).
	 *
	 * @return die Menge aller momentan zugeordneten stellv. Klassenlehrer (Leitung 2).
	 */
	gibAktuelleMengeLeitung2(): List<UvAlgorithmusDynDatenLehrkraft> {
		const list: List<UvAlgorithmusDynDatenLehrkraft> = new ArrayList<UvAlgorithmusDynDatenLehrkraft>();
		list.addAll(this.leitung2ZugeordnetFixiert);
		list.addAll(this.leitung2Zugeordnet);
		return list;
	}

	/**
	 * Liefert eine potentielle Lehrkraft, welche der Klassenleitung (Leitung 1) noch nicht zugeordnet ist.
	 *
	 * <br>Hinweis: Eine Optimierung lohnt sich hier nicht,
	 *              denn die zugeordneten Lehrkräfte sind fast immer 0 oder 1.
	 *
	 * @return eine potentielle Lehrkraft, welche der Klassenleitung (Leitung 1) noch nicht zugeordnet ist.
	 */
	gibLeitung1PotentiellZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		const size: number = this.leitung1Potentiell.size();
		if (size === 0) {
			return null;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.leitung1Potentiell.get(this.rnd.nextInt(size));
		if (this.leitung1Zugeordnet.contains(lehrkraft) || this.leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		if (this.leitung2Zugeordnet.contains(lehrkraft) || this.leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		return lehrkraft;
	}

	/**
	 * Liefert eine potentielle Lehrkraft, welche der stellv. Klassenleitung (Leitung 2) noch nicht zugeordnet ist.
	 *
	 * <br>Hinweis: Eine Optimierung lohnt sich hier nicht,
	 *              denn die zugeordneten Lehrkräfte sind fast immer 0 oder 1.
	 *
	 * @return eine potentielle Lehrkraft, welche der stellv. Klassenleitung (Leitung 2) noch nicht zugeordnet ist.
	 */
	gibLeitung2PotentiellZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		const size: number = this.leitung2Potentiell.size();
		if (size === 0) {
			return null;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.leitung2Potentiell.get(this.rnd.nextInt(size));
		if (this.leitung1Zugeordnet.contains(lehrkraft) || this.leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		if (this.leitung2Zugeordnet.contains(lehrkraft) || this.leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		return lehrkraft;
	}

	/**
	 * Liefert eine zufällige derzeit zugeordnete Klassenleitung (Leitung 1) die nicht fixiert ist, oder null.
	 *
	 * @return eine zufällige derzeit zugeordnete Klassenleitung (Leitung 1) die nicht fixiert ist, oder null.
	 */
	gibLeitung1ZugeordnetAberNichtFixiertZufaellig(): UvAlgorithmusDynDatenLehrkraft | null {
		const size: number = this.leitung1Zugeordnet.size();
		if (size === 0) {
			return null;
		}
		return this.leitung1Zugeordnet.get(this.rnd.nextInt(size));
	}

	/**
	 * Liefert eine zufällige derzeit zugeordnete stellv. Klassenleitung (Leitung 2) die nicht fixiert ist, oder null.
	 *
	 * @return eine zufällige derzeit zugeordnete stellv. Klassenleitung (Leitung 2) die nicht fixiert ist, oder null.
	 */
	gibLeitung2ZugeordnetAberNichtFixiertZufaellig(): UvAlgorithmusDynDatenLehrkraft | null {
		const size: number = this.leitung2Zugeordnet.size();
		if (size === 0) {
			return null;
		}
		return this.leitung2Zugeordnet.get(this.rnd.nextInt(size));
	}

	/**
	 * Entfernt die Lehrkraft als potentielle Klassenleitung (Leitung 1).
	 * <br>Diese Methode darf nur einmalig aufgerufen werden.
	 *
	 * @param lehrkraft   Die zu entfernende potentielle Lehrkraft.
	 */
	entferneLeitung1(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (this.leitung1Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Leitung 1 %s soll verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
		}
		if (this.leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Leitung 1 %s soll verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
		}
		this.leitung1Potentiell.remove(lehrkraft);
	}

	/**
	 * Entfernt die Lehrkraft als potentielle stellv. Klassenleitung (Leitung 2).
	 * <br>Diese Methode darf nur einmalig aufgerufen werden.
	 *
	 * @param lehrkraft   Die zu entfernende potentielle Lehrkraft.
	 */
	entferneLeitung2(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (this.leitung2Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Leitung 2 %s soll verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
		}
		if (this.leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Leitung 2 %s soll verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
		}
		this.leitung2Potentiell.remove(lehrkraft);
	}

	/**
	 * Fügt dieser Klasse eine Klassenleitung (Leitung 1) hinzu und aktualisiert den Malus.
	 *
	 * <br>Die Lehrkraft darf dabei weder bereits als Leitung 1 noch als Leitung 2 zugeordnet oder fixiert sein.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}, die als Leitung 1 zugeordnet werden soll.
	 * @param fixiert     Falls true, wird die Lehrkraft in die fixierte Menge aufgenommen,
	 *                    sonst in die normale Menge.
	 */
	stateLeitung1ZugeordnetAdd(lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (this.leitung1Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung1 hat Lehrkraft bereits (normal)!");
		}
		if (this.leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung1 hat Lehrkraft bereits (fixiert)!");
		}
		if (this.leitung2Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung2 hat Lehrkraft bereits (normal)!");
		}
		if (this.leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung2 hat Lehrkraft bereits (fixiert)!");
		}
		if (fixiert) {
			this.leitung1ZugeordnetFixiert.add(lehrkraft);
		} else {
			this.leitung1Zugeordnet.add(lehrkraft);
		}
	}

	/**
	 * Entfernt eine zuvor zugeordnete Klassenleitung (Leitung 1) und aktualisiert den Malus.
	 *
	 * <br>Die Lehrkraft muss aktuell als normale Leitung 1 zugeordnet sein.
	 *
	 * @param lehrkraft   Die aktuell zugeordnete Lehrkraft.
	 */
	stateLeitung1ZugeordnetDel(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (!this.leitung1Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung 1 kann nicht entfernt werden, da sie gar nicht zugeordnet ist!");
		}
		this.leitung1Zugeordnet.remove(lehrkraft);
	}

	/**
	 * Fügt dieser Klasse eine stellv. Klassenleitung (Leitung 2) hinzu und aktualisiert den Malus.
	 *
	 * <br>Die Lehrkraft darf dabei weder bereits als Leitung 1 noch als Leitung 2
	 * zugeordnet oder fixiert sein.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}, die als Leitung 2 zugeordnet werden soll.
	 * @param fixiert     Falls true, wird die Lehrkraft in die fixierte Menge aufgenommen,
	 *                    sonst in die normale Menge.
	 */
	stateLeitung2ZugeordnetAdd(lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (this.leitung1Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung1 hat Lehrkraft bereits (normal)!");
		}
		if (this.leitung1ZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung1 hat Lehrkraft bereits (fixiert)!");
		}
		if (this.leitung2Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung2 hat Lehrkraft bereits (normal)!");
		}
		if (this.leitung2ZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung2 hat Lehrkraft bereits (fixiert)!");
		}
		if (fixiert) {
			this.leitung2ZugeordnetFixiert.add(lehrkraft);
		} else {
			this.leitung2Zugeordnet.add(lehrkraft);
		}
	}

	/**
	 * Entfernt eine zuvor zugeordnete stellv. Klassenleitung (Leitung 2) und aktualisiert den Malus.
	 *
	 * <br>Die Lehrkraft muss aktuell als normale Leitung 2 zugeordnet sein.
	 *
	 * @param lehrkraft   Die aktuell zugeordnete Lehrkraft.
	 */
	stateLeitung2ZugeordnetDel(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (!this.leitung2Zugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException("Klassenleitung 2 kann nicht entfernt werden, da sie gar nicht zugeordnet ist!");
		}
		this.leitung2Zugeordnet.remove(lehrkraft);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenKlasse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenKlasse'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenKlasse>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenKlasse');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenKlasse(obj: unknown): UvAlgorithmusDynDatenKlasse {
	return obj as UvAlgorithmusDynDatenKlasse;
}
