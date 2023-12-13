import { KlausurblockungSchienenAlgorithmusAbstract } from '../../../core/utils/klausurplanung/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenDynDaten } from '../../../core/utils/klausurplanung/KlausurblockungSchienenDynDaten';
import { Random } from '../../../java/util/Random';
import { System } from '../../../java/lang/System';

export class KlausurblockungSchienenAlgorithmusGreedy2b extends KlausurblockungSchienenAlgorithmusAbstract {


	/**
	 *Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pDynDaten : KlausurblockungSchienenDynDaten) {
		super(pRandom, pDynDaten);
	}

	public toString() : string {
		return "Klausuren nach Knotengrad & Schienen nacheinander";
	}

	public berechne(pZeitEnde : number) : void {
		this._dynDaten.aktion_EntferneAlles_SchienenNacheinande_KlausurenHoherGradZuerst();
		this._dynDaten.aktionZustand1Speichern();
		while (System.currentTimeMillis() < pZeitEnde) {
			this._dynDaten.aktion_EntferneAlles_SchienenNacheinande_KlausurenHoherGradZuerst();
			if (this._dynDaten.gibIstBesserAlsZustand1())
				this._dynDaten.aktionZustand1Speichern();
			else
				this._dynDaten.aktionZustand1Laden();
		}
		if (this._dynDaten.gibIstBesserAlsZustand2())
			this._dynDaten.aktionZustand2Speichern();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.klausurplanung.KlausurblockungSchienenAlgorithmusGreedy2b';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.KlausurblockungSchienenAlgorithmusGreedy2b', 'de.svws_nrw.core.utils.klausurplanung.KlausurblockungSchienenAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_KlausurblockungSchienenAlgorithmusGreedy2b(obj : unknown) : KlausurblockungSchienenAlgorithmusGreedy2b {
	return obj as KlausurblockungSchienenAlgorithmusGreedy2b;
}
