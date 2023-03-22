import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { KlausurblockungSchienenAlgorithmusAbstract, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenDynDaten, cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenDynDaten } from '../../../core/utils/klausurplan/KlausurblockungSchienenDynDaten';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { DeveloperNotificationException, cast_de_nrw_schule_svws_core_exceptions_DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { System, cast_java_lang_System } from '../../../java/lang/System';

export class KlausurblockungSchienenAlgorithmusGreedy6 extends KlausurblockungSchienenAlgorithmusAbstract {


	/**
	 *
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
			let pZeitEnde : number = __param0 as number;
			this.berechne();
			this._dynDaten.aktionZustand1Speichern();
			while (System.currentTimeMillis() < pZeitEnde) {
				this.berechne();
				if (this._dynDaten.gibIstBesserAlsZustand1() === true)
					this._dynDaten.aktionZustand1Speichern();
				else
					this._dynDaten.aktionZustand1Laden();
			}
			if (this._dynDaten.gibIstBesserAlsZustand2() === true)
				this._dynDaten.aktionZustand2Speichern();
		} else if ((typeof __param0 === "undefined")) {
			this._dynDaten.aktionKlausurenAusSchienenEntfernen();
			let setS : LinkedCollection<number> = new LinkedCollection();
			while (this._dynDaten.gibAnzahlNichtverteilterKlausuren() > 0) {
				setS.clear();
				let nr1 : number = this._dynDaten.gibKlausurDieFreiIstMitDenMeistenFreienNachbarn();
				let s : number = this._dynDaten.aktionSetzeKlausurInNeueSchiene(nr1);
				setS.addLast(nr1);
				let nr2 : number = this._dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
				while (nr2 >= 0) {
					setS.addLast(nr2);
					if (this._dynDaten.aktionSetzeKlausurInSchiene(nr2, s) === false)
						throw new DeveloperNotificationException("Fehler im Algorithmus Greedy6!")
					nr2 = this._dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
				}
			}
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.klausurplan.KlausurblockungSchienenAlgorithmusGreedy6', 'de.nrw.schule.svws.core.utils.klausurplan.KlausurblockungSchienenAlgorithmusAbstract'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_klausurplan_KlausurblockungSchienenAlgorithmusGreedy6(obj : unknown) : KlausurblockungSchienenAlgorithmusGreedy6 {
	return obj as KlausurblockungSchienenAlgorithmusGreedy6;
}
