import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplanung/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusAbstract';
import { System } from '../../../java/lang/System';

export class KlausurterminblockungAlgorithmusGreedy1b extends KlausurterminblockungAlgorithmusAbstract {


	/**
	 *Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pDynDaten : KlausurterminblockungDynDaten) {
		super(pRandom, pDynDaten);
	}

	public toString() : string {
		return "Schienen nacheinander, Klausurgruppen zufällig";
	}

	public berechne(pZeitEnde : number) : void {
		this._dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		this._dynDaten.aktionZustand1Speichern();
		while (System.currentTimeMillis() < pZeitEnde) {
			this._dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
			if (this._dynDaten.gibIstBesserAlsZustand1())
				this._dynDaten.aktionZustand1Speichern();
			else
				this._dynDaten.aktionZustand1Laden();
		}
		if (this._dynDaten.gibIstBesserAlsZustand2())
			this._dynDaten.aktionZustand2Speichern();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmusGreedy1b', 'de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_KlausurterminblockungAlgorithmusGreedy1b(obj : unknown) : KlausurterminblockungAlgorithmusGreedy1b {
	return obj as KlausurterminblockungAlgorithmusGreedy1b;
}
