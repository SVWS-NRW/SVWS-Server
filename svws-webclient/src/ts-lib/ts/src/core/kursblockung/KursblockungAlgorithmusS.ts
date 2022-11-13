import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';

export abstract class KursblockungAlgorithmusS extends JavaObject {

	protected readonly _random : Random;

	protected readonly logger : Logger;

	protected readonly dynDaten : KursblockungDynDaten;


	/**
	 * Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 * 
	 * @param pRandom  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, logger : Logger, dynDaten : KursblockungDynDaten) {
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusS'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusS(obj : unknown) : KursblockungAlgorithmusS {
	return obj as KursblockungAlgorithmusS;
}
