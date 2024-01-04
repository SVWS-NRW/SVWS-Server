import { KlausurblockungSchienenAlgorithmusAbstract } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenDynDaten } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenDynDaten';
import { Random } from '../../../../java/util/Random';
import { LinkedCollection } from '../../../../core/adt/collection/LinkedCollection';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { System } from '../../../../java/lang/System';

export class KlausurblockungSchienenAlgorithmusGreedy6 extends KlausurblockungSchienenAlgorithmusAbstract {


	/**
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public constructor(pRandom : Random, pDynDaten : KlausurblockungSchienenDynDaten) {
		super(pRandom, pDynDaten);
	}

	public toString() : string {
		return "Recursive Largest First (RLF)";
	}

	public berechne(pZeitEnde : number) : void;

	public berechne() : void;

	/**
	 * Implementation for method overloads of 'berechne'
	 */
	public berechne(__param0? : number) : void {
		if (((typeof __param0 !== "undefined") && typeof __param0 === "number")) {
			const pZeitEnde : number = __param0 as number;
			this.berechne();
			this._dynDaten.aktionZustand1Speichern();
			while (System.currentTimeMillis() < pZeitEnde) {
				this.berechne();
				if (this._dynDaten.gibIstBesserAlsZustand1())
					this._dynDaten.aktionZustand1Speichern();
				else
					this._dynDaten.aktionZustand1Laden();
			}
			if (this._dynDaten.gibIstBesserAlsZustand2())
				this._dynDaten.aktionZustand2Speichern();
		} else if ((typeof __param0 === "undefined")) {
			this._dynDaten.aktionKlausurenAusSchienenEntfernen();
			const setS : LinkedCollection<number> = new LinkedCollection();
			while (this._dynDaten.gibAnzahlNichtverteilterKlausuren() > 0) {
				setS.clear();
				const nr1 : number = this._dynDaten.gibKlausurDieFreiIstMitDenMeistenFreienNachbarn();
				const s : number = this._dynDaten.aktionSetzeKlausurInNeueSchiene(nr1);
				setS.addLast(nr1);
				let nr2 : number = this._dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
				while (nr2 >= 0) {
					setS.addLast(nr2);
					if (!this._dynDaten.aktionSetzeKlausurInSchiene(nr2, s))
						throw new DeveloperNotificationException("Fehler im Algorithmus Greedy6!")
					nr2 = this._dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
				}
			}
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenAlgorithmusGreedy6';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenAlgorithmusGreedy6', 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungSchienenAlgorithmusGreedy6(obj : unknown) : KlausurblockungSchienenAlgorithmusGreedy6 {
	return obj as KlausurblockungSchienenAlgorithmusGreedy6;
}
