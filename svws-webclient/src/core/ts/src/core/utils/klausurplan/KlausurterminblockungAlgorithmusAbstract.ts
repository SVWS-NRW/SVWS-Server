import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungDynDaten } from '../../../core/utils/klausurplan/KlausurterminblockungDynDaten';

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
	 *Der Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pDynDaten : KlausurterminblockungDynDaten) {
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.KlausurterminblockungAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusAbstract(obj : unknown) : KlausurterminblockungAlgorithmusAbstract {
	return obj as KlausurterminblockungAlgorithmusAbstract;
}
