import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';

export abstract class KursblockungAlgorithmusS extends JavaObject {

	/**
	 *  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly rnd: Random;

	/**
	 *  Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	protected readonly log: Logger;

	/**
	 *  Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	protected readonly dynDaten: KursblockungDynDaten;


	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 *
	 * @param random     Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger     Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDaten   Die aktuellen Blockungsdaten.
	 */
	protected constructor(random: Random, logger: Logger, dynDaten: KursblockungDynDaten) {
		super();
		this.rnd = random;
		this.log = logger;
		this.dynDaten = dynDaten;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der SuS auf die Kurse. Es gibt keine
	 * festgelegte Zeitgrenze, aber die Methode sollte sehr schnell sein.
	 */
	public abstract berechne(): void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusS';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusS'].includes(name);
	}

	public static readonly class = new Class<KursblockungAlgorithmusS>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusS');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusS(obj: unknown): KursblockungAlgorithmusS {
	return obj as KursblockungAlgorithmusS;
}
