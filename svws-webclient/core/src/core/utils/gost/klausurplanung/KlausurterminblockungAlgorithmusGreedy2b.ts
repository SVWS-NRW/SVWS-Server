import { Random } from '../../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../../core/utils/gost/klausurplanung/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../../core/utils/gost/klausurplanung/KlausurterminblockungAlgorithmusAbstract';
import { Class } from '../../../../java/lang/Class';
import { System } from '../../../../java/lang/System';

export class KlausurterminblockungAlgorithmusGreedy2b extends KlausurterminblockungAlgorithmusAbstract {


	/**
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pDynDaten : KlausurterminblockungDynDaten) {
		super(pRandom, pDynDaten);
	}

	public toString() : string {
		return "Schienen nacheinander, Klausurgruppen nach Knotengrad";
	}

	public berechne(pZeitEnde : number) : void {
		this._dynDaten.aktion_Clear_TermineNacheinander_GruppeNachGrad();
		this._dynDaten.aktionZustand1Speichern();
		while (System.currentTimeMillis() < pZeitEnde) {
			this._dynDaten.aktion_Clear_TermineNacheinander_GruppeNachGrad();
			if (this._dynDaten.gibIstBesserAlsZustand1())
				this._dynDaten.aktionZustand1Speichern();
			else
				this._dynDaten.aktionZustand1Laden();
		}
		if (this._dynDaten.gibIstBesserAlsZustand2())
			this._dynDaten.aktionZustand2Speichern();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusGreedy2b';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusGreedy2b', 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusAbstract'].includes(name);
	}

	public static class = new Class<KlausurterminblockungAlgorithmusGreedy2b>('de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusGreedy2b');

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurterminblockungAlgorithmusGreedy2b(obj : unknown) : KlausurterminblockungAlgorithmusGreedy2b {
	return obj as KlausurterminblockungAlgorithmusGreedy2b;
}
