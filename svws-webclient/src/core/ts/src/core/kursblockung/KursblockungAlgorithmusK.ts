import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';

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
	 *Der Konstruktor stellt einen Logger und die bei der Blockung benötigten dynamischen Daten den Unterklassen zur
	 * Verfügung.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pLogger : Logger, pDynDaten : KursblockungDynDaten) {
		super();
		this._random = pRandom;
		this.logger = pLogger;
		this.dynDaten = pDynDaten;
	}

	/**
	 *Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Kurse auf die Schienen und
	 * überschreitet dabei nicht die Endzeit (in Millisekunden).
	 *
	 * @param pTimeEndMillis Die Endzeit (in Millisekunden).
	 */
	public abstract berechne(pTimeEndMillis : number) : void;

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK(obj : unknown) : KursblockungAlgorithmusK {
	return obj as KursblockungAlgorithmusK;
}
