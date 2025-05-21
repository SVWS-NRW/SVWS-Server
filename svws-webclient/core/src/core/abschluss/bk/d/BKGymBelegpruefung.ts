import { JavaObject } from '../../../../java/lang/JavaObject';
import { BKGymBelegungsfehler } from '../../../../core/abschluss/bk/d/BKGymBelegungsfehler';
import { BKGymAbiturdatenManager } from '../../../../core/abschluss/bk/d/BKGymAbiturdatenManager';
import { ArrayList } from '../../../../java/util/ArrayList';
import { Schulgliederung } from '../../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { BKGymBelegpruefungD01_10600 } from '../../../../core/abschluss/bk/d/BKGymBelegpruefungD01_10600';

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

	/**
	 * Erstellt eine Belegprüfung zu einer Fachklasse in der Schulgliederung D01.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	private static createPruefungD01(manager : BKGymAbiturdatenManager) : BKGymBelegpruefung {
		const fks : string = manager.getFachklassenschluessel();
		let _sevar_1372805868 : any;
		const _seexpr_1372805868 = (fks);
		if (_seexpr_1372805868 === "10600") {
			_sevar_1372805868 = new BKGymBelegpruefungD01_10600(manager);
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + manager.getGliederung().name() + " und den Fachklassenschlüssel " + manager.getFachklassenschluessel() + " wird noch nicht unterstützt.");
		}
		return _sevar_1372805868;
	}

	/**
	 * Erstellt die zugehörige Belegprüfung mit den Abiturdaten anhand des übergebenen Bildungsganges.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 *
	 * @return der Belegprüfungsalgorithmus
	 */
	public static getPruefung(manager : BKGymAbiturdatenManager) : BKGymBelegpruefung {
		const sgl : Schulgliederung = manager.getGliederung();
		let pruefung : BKGymBelegpruefung;
		const _seexpr_2062124187 = (sgl);
		if (_seexpr_2062124187 === Schulgliederung.D01) {
			pruefung = BKGymBelegpruefung.createPruefungD01(manager);
		} else {
			throw new DeveloperNotificationException("Die Belegprüfung für die Schulgliederung " + manager.getGliederung().name() + " wird noch nicht unterstützt.");
		}
		;
		return pruefung;
	}

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
