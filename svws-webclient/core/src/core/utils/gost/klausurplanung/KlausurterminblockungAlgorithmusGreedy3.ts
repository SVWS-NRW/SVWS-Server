import { Random } from '../../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../../core/utils/gost/klausurplanung/KlausurterminblockungDynDaten';
import { ArrayList } from '../../../../java/util/ArrayList';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../../core/utils/gost/klausurplanung/KlausurterminblockungAlgorithmusAbstract';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { System } from '../../../../java/lang/System';

export class KlausurterminblockungAlgorithmusGreedy3 extends KlausurterminblockungAlgorithmusAbstract {

	/**
	 * Die kleinste Schienenanzahl, die bisher gefunden wurde.
	 */
	private _minTermine : number = 0;

	/**
	 * Bis zu welchem Zeitpunkt die Rekursion laufen darf.
	 */
	private _zeitEnde : number = 0;


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
		return "Termine nacheinander, Klausurgruppen mit Backtracking";
	}

	public berechne(pZeitEnde : number) : void {
		this._zeitEnde = pZeitEnde;
		this._dynDaten.aktion_Clear_TermineNacheinander_GruppeNachGrad();
		this._minTermine = this._dynDaten.gibKlausurgruppenAnzahl();
		this._dynDaten.aktionZustand1Speichern();
		this._dynDaten.aktionClear();
		this.berechneRekursiv();
		this._dynDaten.aktionZustand1Laden();
		if (this._dynDaten.gibIstBesserAlsZustand2())
			this._dynDaten.aktionZustand2Speichern();
	}

	private berechneRekursiv() : void {
		if (this._dynDaten.gibTerminAnzahl() > this._minTermine)
			return;
		if (System.currentTimeMillis() > this._zeitEnde)
			return;
		if (!this._dynDaten.gibExistierenNichtverteilteKlausuren()) {
			if (this._dynDaten.gibIstBesserAlsZustand1()) {
				this._minTermine = this._dynDaten.gibTerminAnzahl();
				this._dynDaten.aktionZustand1Speichern();
			}
			return;
		}
		const gruppe : ArrayList<number> = this._dynDaten.gibKlausurgruppeMitMinimalenTerminmoeglichkeiten();
		for (let terminNr : number = 0; terminNr < this._dynDaten.gibTerminAnzahl(); terminNr++) {
			if (this._dynDaten.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr)) {
				this.berechneRekursiv();
				this._dynDaten.aktionEntferneKlausurgruppeAusTermin(gruppe, terminNr);
			}
		}
		const terminNr : number = this._dynDaten.gibErzeugeNeuenTermin();
		if (!this._dynDaten.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr))
			throw new DeveloperNotificationException("Ein Setzen muss hier möglich sein!")
		this.berechneRekursiv();
		this._dynDaten.aktionEntferneKlausurgruppeAusTermin(gruppe, terminNr);
		this._dynDaten.entferneLetztenTermin();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusGreedy3';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusGreedy3', 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurterminblockungAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurterminblockungAlgorithmusGreedy3(obj : unknown) : KlausurterminblockungAlgorithmusGreedy3 {
	return obj as KlausurterminblockungAlgorithmusGreedy3;
}
