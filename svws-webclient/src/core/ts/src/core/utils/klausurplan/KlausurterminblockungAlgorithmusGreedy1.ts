import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplan/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusAbstract';
import { System } from '../../../java/lang/System';

export class KlausurterminblockungAlgorithmusGreedy1 extends KlausurterminblockungAlgorithmusAbstract {


	/**
	 *
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pDynDaten : KlausurterminblockungDynDaten) {
		super(pRandom, pDynDaten);
	}

	public toString() : string {
		return "Klausurgruppen zufällig, Termine zufällig";
	}

	public berechne(pZeitEnde : number) : void {
		this._dynDaten.aktion_Clear_KlausurgruppenZufaellig_TermineZufaellig();
		this._dynDaten.aktionZustand1Speichern();
		while (System.currentTimeMillis() < pZeitEnde) {
			this._dynDaten.aktion_Clear_KlausurgruppenZufaellig_TermineZufaellig();
			if (this._dynDaten.gibIstBesserAlsZustand1())
				this._dynDaten.aktionZustand1Speichern();
			else
				this._dynDaten.aktionZustand1Laden();
		}
		if (this._dynDaten.gibIstBesserAlsZustand2())
			this._dynDaten.aktionZustand2Speichern();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.KlausurterminblockungAlgorithmusGreedy1', 'de.nrw.schule.svws.core.utils.klausurplan.KlausurterminblockungAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurterminblockungAlgorithmusGreedy1(obj : unknown) : KlausurterminblockungAlgorithmusGreedy1 {
	return obj as KlausurterminblockungAlgorithmusGreedy1;
}
