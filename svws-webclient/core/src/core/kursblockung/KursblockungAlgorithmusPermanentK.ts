import { JavaObject } from '../../java/lang/JavaObject';
import { Random } from '../../java/util/Random';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';

export abstract class KursblockungAlgorithmusPermanentK extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	readonly _random : Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	readonly logger : Logger;

	/**
	 * Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	readonly dynDaten : KursblockungDynDaten;


	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param input     Die Eingabedaten.
	 */
	protected constructor(pRandom : Random, pLogger : Logger, input : GostBlockungsdatenManager) {
		super();
		this._random = pRandom;
		this.logger = pLogger;
		this.dynDaten = new KursblockungDynDaten(pRandom, pLogger, input);
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, optimiert die Blockung weiter.
	 *
	 * @param zeitEnde  Der Zeitpunkt (in Millisekunden), bis zu dem der Algorithmus weiter optimieren darf.
	 */
	public abstract next(zeitEnde : number) : void;

	/**
	 * Liefert das KursblockungDynDaten-Objekt des Algorithmus.
	 *
	 * @return das KursblockungDynDaten-Objekt des Algorithmus.
	 */
	public gibDynDaten() : KursblockungDynDaten {
		return this.dynDaten;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK'].includes(name);
	}

	public static class = new Class<KursblockungAlgorithmusPermanentK>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanentK');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanentK(obj : unknown) : KursblockungAlgorithmusPermanentK {
	return obj as KursblockungAlgorithmusPermanentK;
}
