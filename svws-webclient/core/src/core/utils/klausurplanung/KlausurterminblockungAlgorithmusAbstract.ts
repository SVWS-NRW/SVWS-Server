import { JavaObject } from '../../../java/lang/JavaObject';
import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplanung/KlausurterminblockungDynDaten';

export abstract class KlausurterminblockungAlgorithmusAbstract extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 * Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	protected readonly _dynDaten : KlausurterminblockungDynDaten;


	/**
	 * Der Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	protected constructor(pRandom : Random, pDynDaten : KlausurterminblockungDynDaten) {
		super();
		this._random = pRandom;
		this._dynDaten = pDynDaten;
	}

	/**
	 * Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Klausuren auf Termine,
	 * beachtet dabei potentielle Regeln und überschreitet nicht die Endzeit (in Millisekunden).
	 *
	 * @param pZeitEnde Die Endzeit (in Millisekunden).
	 */
	public abstract berechne(pZeitEnde : number) : void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmusAbstract';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_KlausurterminblockungAlgorithmusAbstract(obj : unknown) : KlausurterminblockungAlgorithmusAbstract {
	return obj as KlausurterminblockungAlgorithmusAbstract;
}
