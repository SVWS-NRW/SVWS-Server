import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Logger } from '../../core/logger/Logger';

export abstract class KursblockungAlgorithmusS extends JavaObject {

	/**
	 *  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 *  Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	protected readonly logger : Logger;

	/**
	 *  Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	protected readonly dynDaten : KursblockungDynDaten;


	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 *
	 * @param pRandom  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDaten Die aktuellen Blockungsdaten.
	 */
	protected constructor(pRandom : Random, logger : Logger, dynDaten : KursblockungDynDaten) {
		super();
		this._random = pRandom;
		this.logger = logger;
		this.dynDaten = dynDaten;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der SuS auf die Kurse. Es gibt keine
	 * festgelegte Zeitgrenze, aber die Methode sollte sehr schnell sein.
	 */
	public abstract berechne() : void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusS';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusS'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusS(obj : unknown) : KursblockungAlgorithmusS {
	return obj as KursblockungAlgorithmusS;
}
