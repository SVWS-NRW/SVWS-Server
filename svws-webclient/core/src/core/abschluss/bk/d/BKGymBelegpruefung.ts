import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export abstract class BKGymBelegpruefung extends JavaObject {

	/**
	 * Die Abiturdaten-Manager
	 */
	protected readonly manager : BKGymAbiturdatenManager;

	/**
	 * Die Belegungsfehler, die bei der Prüfung entstanden sind.
	 */
	private readonly belegungsfehler : List<BKGymBelegungsfehler> = new ArrayList<BKGymBelegungsfehler>();


	/**
	 * Erzeugt eine neue Belegprüfung mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public constructor(manager : BKGymAbiturdatenManager) {
		super();
		this.manager = manager;
	}

	/**
	 * Fügt einen Belegungsfehler zu der Belegprüfung hinzu. Diese Methode wird von den Sub-Klassen
	 * aufgerufen, wenn dort ein Belegungsfehler erkannt wird.
	 *
	 * @param fehler   der hinzuzufügende Belegungsfehler
	 */
	protected addFehler(fehler : BKGymBelegungsfehler) : void {
		if (!this.belegungsfehler.contains(fehler))
			this.belegungsfehler.add(fehler);
	}

	/**
	 * Gibt die Belegungsfehler zurück, welche bei der Prüfung aufgetreten sind.
	 *
	 * @return die Belegungsfehler
	 */
	public getBelegungsfehler() : List<BKGymBelegungsfehler> {
		return this.belegungsfehler;
	}

	/**
	 * Gibt zurück, ob ein "echter" Belegungsfehler vorliegt und nicht nur eine Warnung oder ein Hinweis.
	 *
	 * @return true, wenn kein "echter" Belegungsfehler vorliegt, und ansonsten false.
	 */
	public istErfolgreich() : boolean {
		for (const fehler of this.belegungsfehler)
			if (!fehler.istInfo())
				return false;
		return true;
	}

	/**
	 * Führt die Belegprüfung durch.
	 */
	public abstract pruefe() : void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung'].includes(name);
	}

	public static class = new Class<BKGymBelegpruefung>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegpruefung');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegpruefung(obj : unknown) : BKGymBelegpruefung {
	return obj as BKGymBelegpruefung;
}
