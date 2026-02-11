import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungAlgorithmusPermanentKOptimiereBest } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKOptimiereBest';
import { GostBlockungsergebnisManager } from '../../core/utils/gost/GostBlockungsergebnisManager';
import { KursblockungAlgorithmusPermanentK } from '../../core/kursblockung/KursblockungAlgorithmusPermanentK';
import { GostBlockungsdatenManager } from '../../core/utils/gost/GostBlockungsdatenManager';
import { KursblockungAlgorithmusPermanentKSchuelervorschlag } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKSchuelervorschlag';
import { ArrayList } from '../../java/util/ArrayList';
import { KursblockungAlgorithmusPermanentKSchuelervorschlagSingle } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKSchuelervorschlagSingle';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';
import { Random } from '../../java/util/Random';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import type { List } from '../../java/util/List';
import { Class } from '../../java/lang/Class';
import { KursblockungAlgorithmusPermanentKMatching } from '../../core/kursblockung/KursblockungAlgorithmusPermanentKMatching';

export class KursblockungAlgorithmusPermanent extends JavaObject {

	private static readonly MILLIS_START: number = 4000;

	private static readonly TOP_ERGEBNISSE: number = 3;

	private readonly _random: Random = new Random();

	private readonly _logger: Logger = new Logger();

	/**
	 * Die TOP-Ergebnisse werden als {@link KursblockungDynDaten}-Objekt gespeichert, da diese sortierbar sind.
	 */
	private readonly _top: ArrayList<KursblockungDynDaten>;

	/**
	 * Jeder Algorithmus hat sein eigenes {@link KursblockungDynDaten}-Objekt. Das ist wichtig.
	 */
	private readonly algorithmenK: Array<KursblockungAlgorithmusPermanentK>;

	/**
	 * Die Eingabe-Daten von der GUI.
	 */
	private readonly _input: GostBlockungsdatenManager;

	/**
	 * Die Zeitspanne nachdem alle Algorithmen neu erzeugt werden.
	 */
	private _zeitMax: number = 0;

	/**
	 * Die Zeitspanne reduziert sich schrittweise, da die GUI nur kurze Rechenintervalle dem Algorithmus gibt.
	 */
	private _zeitRest: number = 0;

	/**
	 * Der Index des aktuellen Algorithmus der als nächstes ausgeführt wird.
	 */
	private _currentIndex: number = 0;


	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public constructor(pInput: GostBlockungsdatenManager) {
		super();
		const seed: number = this._random.nextLong();
		this._logger.logLn("KursblockungAlgorithmusPermanent: Seed = " + seed);
		this._input = pInput;
		this._zeitMax = KursblockungAlgorithmusPermanent.MILLIS_START;
		this._zeitRest = KursblockungAlgorithmusPermanent.MILLIS_START;
		this._currentIndex = 0;
		this._top = new ArrayList();
		this.algorithmenK = [new KursblockungAlgorithmusPermanentKMatching(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKSchuelervorschlag(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKSchuelervorschlagSingle(this._random, this._logger, this._input), new KursblockungAlgorithmusPermanentKOptimiereBest(this._random, this._logger, this._input, null), new KursblockungAlgorithmusPermanentKOptimiereBest(this._random, this._logger, this._input, null)];
	}

	/**
	 * Liefert TRUE, falls die GUI die TOP-Liste aktualisieren soll.
	 *
	 * @param zeitProAufruf  Die zur Verfügung stehende Zeit (in Millisekunden), um die ehemaligen Ergebnisse zu optimieren.
	 * @return TRUE, falls die GUI die TOP-Liste aktualisieren soll.
	 */
	public next(zeitProAufruf: number): boolean {
		const zeitStart: number = System.currentTimeMillis();
		const zeitEnde: number = zeitStart + zeitProAufruf;
		this.algorithmenK[this._currentIndex].next(zeitEnde);
		this._currentIndex = (this._currentIndex + 1) % this.algorithmenK.length;
		this._zeitRest -= (System.currentTimeMillis() - zeitStart);
		if (this._zeitRest < 100) {
			this._neustart();
			return true;
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 *
	 * @return TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 */
	private _neustart(): number {
		let verbesserungen: number = 0;
		for (let iK: number = 0; iK < this.algorithmenK.length; iK++) {
			this.algorithmenK[iK].ladeBestMitSchuelerverteilung();
			let eingefuegt: boolean = false;
			for (let i: number = 0; (i < this._top.size()) && (!eingefuegt); i++)
				if (this.algorithmenK[iK].dynDaten.gibIstBesser_NW_KD_FW_Als(this._top.get(i))) {
					this._top.add(i, this.algorithmenK[iK].dynDaten);
					eingefuegt = true;
				}
			if (eingefuegt) {
				verbesserungen++;
				if (this._top.size() > KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE)
					this._top.removeLast();
			} else {
				if (this._top.size() < KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE) {
					this._top.addLast(this.algorithmenK[iK].dynDaten);
					verbesserungen++;
				}
			}
		}
		this.algorithmenK[0] = new KursblockungAlgorithmusPermanentKMatching(this._random, this._logger, this._input);
		this.algorithmenK[1] = new KursblockungAlgorithmusPermanentKSchuelervorschlag(this._random, this._logger, this._input);
		this.algorithmenK[2] = new KursblockungAlgorithmusPermanentKSchuelervorschlagSingle(this._random, this._logger, this._input);
		this.algorithmenK[3] = new KursblockungAlgorithmusPermanentKOptimiereBest(this._random, this._logger, this._input, this._gibTopElementOrNull());
		this.algorithmenK[4] = new KursblockungAlgorithmusPermanentKOptimiereBest(this._random, this._logger, this._input, this._gibTopElementOrNull());
		this._zeitMax = (this._zeitMax * 1.5) as number;
		this._zeitRest = this._zeitMax;
		return verbesserungen;
	}

	/**
	 * Liefert ein zufälliges Element aus der TOP-Liste (oder NULL);
	 *
	 * @return ein zufälliges Element aus der TOP-Liste (oder NULL);
	 */
	private _gibTopElementOrNull(): KursblockungDynDaten | null {
		if (this._top.isEmpty())
			return null;
		let index: number = this._random.nextInt(this._top.size());
		return this._top.get(index);
	}

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 * <br> Die ID der Blockungsergebnisse entspricht dem Index in der TOP-Liste.
	 *
	 * @return die Liste der aktuellen Top-Blockungsergebnisse.
	 */
	public getBlockungsergebnisse(): List<GostBlockungsergebnisManager> {
		const list: List<GostBlockungsergebnisManager> = new ArrayList<GostBlockungsergebnisManager>();
		for (let i: number = 0; i < this._top.size(); i++)
			list.add(this._top.get(i).gibErzeugtesKursblockungOutput(this._input, i));
		return list;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanent';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanent'].includes(name);
	}

	public static readonly class = new Class<KursblockungAlgorithmusPermanent>('de.svws_nrw.core.kursblockung.KursblockungAlgorithmusPermanent');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusPermanent(obj: unknown): KursblockungAlgorithmusPermanent {
	return obj as KursblockungAlgorithmusPermanent;
}
