import { JavaObject } from '../../java/lang/JavaObject';
import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungAlgorithmusS } from '../../core/kursblockung/KursblockungAlgorithmusS';
import { KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { KursblockungAlgorithmusPermanentKFachwahlmatrix } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKFachwahlmatrix';
import { KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { ArrayList } from '../../java/util/ArrayList';
import { KursblockungAlgorithmusSMatching } from '../../core/kursblockung/KursblockungAlgorithmusSMatching';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';
import { KursblockungAlgorithmusSMatchingW } from '../../core/kursblockung/KursblockungAlgorithmusSMatchingW';
import { Random } from '../../java/util/Random';
import { KursblockungAlgorithmusPermanentKSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKSchnellW';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusSZufaellig } from '../../core/kursblockung/KursblockungAlgorithmusSZufaellig';
import type { List } from '../../java/util/List';

export class KursblockungAlgorithmusPermanent extends JavaObject {

	private static readonly TOP_ERGEBNISSE : number = 10;

	private readonly _random : Random = new Random();

	private readonly _logger : Logger;

	private readonly _top : ArrayList<KursblockungDynDaten>;

	private readonly _input : GostBlockungsdatenManager;

	private readonly algorithmenK : Array<KursblockungAlgorithmusPermanentK>;

	private _zeitBisNeustart : number = 0;

	private _zeitRest : number = 0;


	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public constructor(pInput : GostBlockungsdatenManager) {
		super();
		const seed : number = this._random.nextLong();
		console.log(JSON.stringify("KursblockungAlgorithmusPermanent: seed = " + seed));
		this._input = pInput;
		this._zeitBisNeustart = 100;
		this._zeitRest = this._zeitBisNeustart;
		this._top = new ArrayList();
		this._logger = new Logger();
		this.algorithmenK = [new KursblockungAlgorithmusPermanentKSchnellW(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKFachwahlmatrix(this._random, this._logger, this._input)];
	}

	/**
	 * Liefert TRUE, falls der Blockungsalgorithmus innerhalb der erlaubten Zeit seine Ergebnisse verbessern konnte.
	 *
	 * @param zeitProAufruf  Die zur Verfügung stehende Zeit (in Millisekunden), um die ehemaligen Ergebnisse zu optimieren.
	 *
	 * @return TRUE, falls der Blockungsalgorithmus innerhalb der erlaubten Zeit seine Ergebnisse verbessern konnte.
	 */
	public next(zeitProAufruf : number) : boolean {
		const zeitStart : number = System.currentTimeMillis();
		const zeitEnde : number = zeitStart + zeitProAufruf;
		for (let iK : number = 0; (iK < this.algorithmenK.length) && (System.currentTimeMillis() < zeitEnde); iK++)
			this.algorithmenK[iK].next(zeitStart + Math.trunc((zeitProAufruf * (iK + 1)) / this.algorithmenK.length));
		this._zeitRest -= (System.currentTimeMillis() - zeitStart);
		return (this._zeitRest <= 0) ? this._neustart() : false;
	}

	private _neustart() : boolean {
		let verbesserung : boolean = false;
		for (let iK : number = 0; iK < this.algorithmenK.length; iK++) {
			this.verteileSuS(this.algorithmenK[iK]);
			verbesserung = verbesserung || this._fuegeHinzuFallsBesser(this.algorithmenK[iK].gibDynDaten());
		}
		console.log(JSON.stringify("    Ende der Berechnungszeit. Verbesserung: " + verbesserung));
		this.algorithmenK[0] = new KursblockungAlgorithmusPermanentKSchnellW(this._random, this._logger, this._input);
		this.algorithmenK[1] = new KursblockungAlgorithmusPermanentKFachwahlmatrix(this._random, this._logger, this._input);
		this._zeitBisNeustart += 100;
		this._zeitRest = this._zeitBisNeustart;
		console.log(JSON.stringify("    Berechnungszeit erhöht sich auf: " + this._zeitBisNeustart + " Millisekunden."));
		return verbesserung;
	}

	private verteileSuS(k : KursblockungAlgorithmusPermanentK) : void {
		const dynDaten : KursblockungDynDaten = k.gibDynDaten();
		const algorithmenS : Array<KursblockungAlgorithmusS> = [new KursblockungAlgorithmusSSchnellW(this._random, this._logger, dynDaten), new KursblockungAlgorithmusSZufaellig(this._random, this._logger, dynDaten), new KursblockungAlgorithmusSMatching(this._random, this._logger, dynDaten), new KursblockungAlgorithmusSMatchingW(this._random, this._logger, dynDaten)];
		dynDaten.aktionZustandSpeichernK();
		for (let iS : number = 0; iS < algorithmenS.length; iS++) {
			algorithmenS[iS].berechne();
			if (dynDaten.gibCompareZustandK_NW_KD_FW() > 0)
				dynDaten.aktionZustandSpeichernK();
		}
		dynDaten.aktionZustandLadenK();
		if (dynDaten.gibCompareZustandG_NW_KD_FW() > 0)
			dynDaten.aktionZustandSpeichernG();
	}

	private _fuegeHinzuFallsBesser(neueDynDaten : KursblockungDynDaten) : boolean {
		for (let i : number = 0; i < this._top.size(); i++)
			if (neueDynDaten.gibIstBesser_NW_KD_FW_Als(this._top.get(i))) {
				this._top.add(i, neueDynDaten);
				console.log(JSON.stringify("    Index " + i + " verbessert!"));
				if (this._top.size() > KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE)
					this._top.remove(KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE);
				return true;
			}
		if (this._top.size() < KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE) {
			console.log(JSON.stringify("    Index " + this._top.size() + " verbessert (hinten)!"));
			this._top.add(neueDynDaten);
			return true;
		}
		return false;
	}

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 *
	 * @return die Liste der aktuellen Top-Blockungsergebnisse.
	 */
	public getBlockungsergebnisse() : List<GostBlockungsergebnisManager> {
		const list : List<GostBlockungsergebnisManager> = new ArrayList();
		for (let i : number = 0; i < this._top.size(); i++)
			list.add(this._top.get(i).gibErzeugtesKursblockungOutput(this._input, i));
		return list;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanent';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanent'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanent(obj : unknown) : KursblockungAlgorithmusPermanent {
	return obj as KursblockungAlgorithmusPermanent;
}
