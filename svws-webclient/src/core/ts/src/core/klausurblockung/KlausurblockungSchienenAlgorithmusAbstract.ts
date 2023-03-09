import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KlausurblockungSchienenDynDaten, cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenDynDaten } from './KlausurblockungSchienenDynDaten';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../logger/Logger';

export abstract class KlausurblockungSchienenAlgorithmusAbstract extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed. 
	 */
	protected readonly _random : Random;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler. 
	 */
	protected readonly _logger : Logger;

	/**
	 * Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges. 
	 */
	protected readonly _dynDaten : KlausurblockungSchienenDynDaten;


	/**
	 *Der Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public constructor(pRandom : Random, pLogger : Logger, pDynDaten : KlausurblockungSchienenDynDaten) {
		super();
		this._random = pRandom;
		this._logger = pLogger;
		this._dynDaten = pDynDaten;
	}

	/**
	 *Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Klausuren auf die Schienen und
	 * überschreitet dabei nicht die Endzeit (in Millisekunden).
	 * 
	 * @param pZeitEnde Die Endzeit (in Millisekunden). 
	 */
	public abstract berechne(pZeitEnde : number) : void;

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.klausurblockung.KlausurblockungSchienenAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_klausurblockung_KlausurblockungSchienenAlgorithmusAbstract(obj : unknown) : KlausurblockungSchienenAlgorithmusAbstract {
	return obj as KlausurblockungSchienenAlgorithmusAbstract;
}
