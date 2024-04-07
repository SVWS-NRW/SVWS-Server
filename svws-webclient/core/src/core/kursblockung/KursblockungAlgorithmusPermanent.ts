import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungAlgorithmusPermanentKOptimiereBest } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKOptimiereBest';
import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungAlgorithmusS } from '../../core/kursblockung/KursblockungAlgorithmusS';
import { KursblockungAlgorithmusSSchnellW } from '../../core/kursblockung/KursblockungAlgorithmusSSchnellW';
import { KursblockungAlgorithmusPermanentKFachwahlmatrix } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKFachwahlmatrix';
import { KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { KursblockungAlgorithmusPermanentKSchuelervorschlag } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKSchuelervorschlag';
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
import { KursblockungAlgorithmusPermanentKMatching } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKMatching';

export class KursblockungAlgorithmusPermanent extends JavaObject {

	private static readonly MILLIS_START : number = 1000;

	private static readonly MILLIS_INCREMENT : number = 1000;

	private static readonly TOP_ERGEBNISSE : number = 10;

	private readonly _random : Random = new Random();

	private readonly _logger : Logger = new Logger();

	/**
	 * Die TOP-Ergebnisse werden als {@link KursblockungDynDaten}-Objekt gespeichert, da diese sortierbar sind.
	 */
	private readonly _top : ArrayList<KursblockungDynDaten>;

	/**
	 * Jeder Algorithmus hat sein eigenes {@link KursblockungDynDaten}-Objekt. Das ist wichtig.
	 */
	private readonly algorithmenK : Array<KursblockungAlgorithmusPermanentK>;

	/**
	 * Die Eingabe-Daten von der GUI.
	 */
	private readonly _input : GostBlockungsdatenManager;

	/**
	 * Die Zeitspanne nachdem alle Algorithmen neu erzeugt werden.
	 */
	private _zeitMax : number = 0;

	/**
	 * Die Zeitspanne reduziert sich schrittweise, da die GUI nur kurze Rechenintervalle dem Algorithmus gibt.
	 */
	private _zeitRest : number = 0;


	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public constructor(pInput : GostBlockungsdatenManager) {
		super();
		const seed : number = this._random.nextLong();
		this._logger.logLn("KursblockungAlgorithmusPermanent: Seed = " + seed);
		this._input = pInput;
		this._zeitMax = KursblockungAlgorithmusPermanent.MILLIS_START;
		this._zeitRest = KursblockungAlgorithmusPermanent.MILLIS_START;
		this._top = new ArrayList();
		this.algorithmenK = [new KursblockungAlgorithmusPermanentKSchnellW(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKFachwahlmatrix(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKMatching(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKSchuelervorschlag(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKOptimiereBest(this._random, this._logger, this._input, null)];
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
		const zeitFuerBerechnung : number = Math.min(zeitProAufruf, this._zeitRest);
		const zeitEnde : number = zeitStart + zeitFuerBerechnung;
		for (let iK : number = 0; (iK < this.algorithmenK.length) && (System.currentTimeMillis() < zeitEnde); iK++)
			this.algorithmenK[iK].next(zeitStart + Math.trunc((zeitFuerBerechnung * (iK + 1)) / this.algorithmenK.length));
		this._zeitRest -= (System.currentTimeMillis() - zeitStart);
		return (this._zeitRest <= 100) ? this._neustart() : false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 *
	 * @return TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 */
	private _neustart() : boolean {
		let verbesserung : number = 0;
		for (let iK : number = 0; iK < this.algorithmenK.length; iK++) {
			this._verteileSuS(this.algorithmenK[iK]);
			if (this._fuegeHinzuFallsBesser(iK))
				verbesserung++;
		}
		this.algorithmenK[0] = new KursblockungAlgorithmusPermanentKSchnellW(this._random, this._logger, this._input);
		this.algorithmenK[1] = new KursblockungAlgorithmusPermanentKFachwahlmatrix(this._random, this._logger, this._input);
		this.algorithmenK[2] = new KursblockungAlgorithmusPermanentKMatching(this._random, this._logger, this._input);
		this.algorithmenK[3] = new KursblockungAlgorithmusPermanentKSchuelervorschlag(this._random, this._logger, this._input);
		this.algorithmenK[4] = new KursblockungAlgorithmusPermanentKOptimiereBest(this._random, this._logger, this._input, this._gibBestOrNull());
		this._zeitMax += KursblockungAlgorithmusPermanent.MILLIS_INCREMENT;
		this._zeitRest = this._zeitMax;
		return verbesserung > 0;
	}

	private _gibBestOrNull() : KursblockungDynDaten | null {
		return this._top.isEmpty() ? null : this._top.get(0);
	}

	private _fuegeHinzuFallsBesser(algNr : number) : boolean {
		const neueDynDaten : KursblockungDynDaten = this.algorithmenK[algNr].gibDynDaten();
		for (let i : number = 0; i < this._top.size(); i++)
			if (neueDynDaten.gibIstBesser_NW_KD_FW_Als(this._top.get(i))) {
				this._top.add(i, neueDynDaten);
				if (this._top.size() > KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE)
					this._top.removeLast();
				return true;
			}
		if (this._top.size() < KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE) {
			this._top.add(neueDynDaten);
			return true;
		}
		return false;
	}

	private _verteileSuS(k : KursblockungAlgorithmusPermanentK) : void {
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

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 * <br> Die ID der Blockungsergebnisse entspricht dem Index in der TOP-Liste.
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
