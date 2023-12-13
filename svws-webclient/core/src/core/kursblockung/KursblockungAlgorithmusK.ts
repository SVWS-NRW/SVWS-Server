import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Logger } from '../../core/logger/Logger';

export abstract class KursblockungAlgorithmusK extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	protected readonly logger : Logger;

	/**
	 * Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	protected readonly dynDaten : KursblockungDynDaten;


	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	protected constructor(pRandom : Random, pLogger : Logger, pDynDaten : KursblockungDynDaten) {
		super();
		this._random = pRandom;
		this.logger = pLogger;
		this.dynDaten = pDynDaten;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Kurse auf die Schienen und
	 * überschreitet dabei nicht die Endzeit (in Millisekunden).
	 *
	 * @param pTimeEndMillis Die Endzeit (in Millisekunden).
	 */
	public abstract berechne(pTimeEndMillis : number) : void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusK';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusK(obj : unknown) : KursblockungAlgorithmusK {
	return obj as KursblockungAlgorithmusK;
}
