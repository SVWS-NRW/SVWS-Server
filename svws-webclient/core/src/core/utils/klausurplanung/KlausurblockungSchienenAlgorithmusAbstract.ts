import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurblockungSchienenDynDaten } from '../../../core/utils/klausurplanung/KlausurblockungSchienenDynDaten';
import { Random } from '../../../java/util/Random';

export abstract class KlausurblockungSchienenAlgorithmusAbstract extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 * Die aktuellen Blockungsdaten. Dieses Objekt dient zur Manipulation der Daten während des Blockungsvorganges.
	 */
	protected readonly _dynDaten : KlausurblockungSchienenDynDaten;


	/**
	 *Der Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	protected constructor(pRandom : Random, pDynDaten : KlausurblockungSchienenDynDaten) {
		super();
		this._random = pRandom;
		this._dynDaten = pDynDaten;
	}

	/**
	 *Eine Unterklasse, die diese Methode implementiert, berechnet eine Verteilung der Klausuren auf die Schienen und
	 * überschreitet dabei nicht die Endzeit (in Millisekunden).
	 *
	 * @param pZeitEnde Die Endzeit (in Millisekunden).
	 */
	public abstract berechne(pZeitEnde : number) : void;

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.klausurplanung.KlausurblockungSchienenAlgorithmusAbstract';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.KlausurblockungSchienenAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_KlausurblockungSchienenAlgorithmusAbstract(obj : unknown) : KlausurblockungSchienenAlgorithmusAbstract {
	return obj as KlausurblockungSchienenAlgorithmusAbstract;
}
