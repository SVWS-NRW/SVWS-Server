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

	private readonly rnd: Random = new Random();

	private readonly log: Logger = new Logger();

	/**
	 * Die TOP-Ergebnisse werden als {@link KursblockungDynDaten}-Objekt gespeichert, da diese sortierbar sind.
	 */
	private readonly topErgebnisse: ArrayList<KursblockungDynDaten>;

	/**
	 * Jeder Algorithmus hat sein eigenes {@link KursblockungDynDaten}-Objekt. Das ist wichtig.
	 */
	private algorithmenK: Array<KursblockungAlgorithmusPermanentK>;

	/**
	 * Die Eingabe-Daten von der GUI.
	 */
	private readonly input: GostBlockungsdatenManager;

	/**
	 * Die Zeitspanne nachdem alle Algorithmen neu erzeugt werden.
	 */
	private zeitMax: number = 0;

	/**
	 * Die Zeitspanne reduziert sich schrittweise, da die GUI nur kurze Rechenintervalle dem Algorithmus gibt.
	 */
	private zeitRest: number = 0;

	/**
	 * Der Index des aktuellen Algorithmus der als nächstes ausgeführt wird.
	 */
	private currentIndex: number = 0;


	/**
	 * Initialisiert den Blockungsalgorithmus für eine vom Clienten initiierte dauerhafte Berechnung.
	 *
	 * @param pInput  Das Eingabe-Objekt (der Daten-Manager).
	 */
	public constructor(pInput: GostBlockungsdatenManager) {
		super();
		const seed: number = this.rnd.nextLong();
		this.log.logLn("KursblockungAlgorithmusPermanent: Seed = " + seed);
		this.input = pInput;
		this.zeitMax = KursblockungAlgorithmusPermanent.MILLIS_START;
		this.zeitRest = KursblockungAlgorithmusPermanent.MILLIS_START;
		this.currentIndex = 0;
		this.topErgebnisse = new ArrayList();
		this.algorithmenK = this.erzeugeAlgorithmenNeu();
	}

	private erzeugeAlgorithmenNeu(): Array<KursblockungAlgorithmusPermanentK> {
		return [new KursblockungAlgorithmusPermanentKMatching(this.rnd, this.log, this.input), new KursblockungAlgorithmusPermanentKSchuelervorschlag(this.rnd, this.log, this.input), new KursblockungAlgorithmusPermanentKSchuelervorschlagSingle(this.rnd, this.log, this.input), new KursblockungAlgorithmusPermanentKOptimiereBest(this.rnd, this.log, this.input, this.gibTopElementOrNull()), new KursblockungAlgorithmusPermanentKOptimiereBest(this.rnd, this.log, this.input, this.gibTopElementOrNull())];
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
		this.algorithmenK[this.currentIndex].next(zeitEnde);
		this.currentIndex = (this.currentIndex + 1) % this.algorithmenK.length;
		this.zeitRest -= (System.currentTimeMillis() - zeitStart);
		if (this.zeitRest < 100) {
			this.neustart();
			return true;
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 *
	 * @return TRUE, falls mindestens ein Algorithmus ein besseres Ergebnis gefunden hat.
	 */
	private neustart(): number {
		let verbesserungen: number = 0;
		for (let iK: number = 0; iK < this.algorithmenK.length; iK++) {
			this.algorithmenK[iK].ladeBestMitSchuelerverteilung();
			let eingefuegt: boolean = false;
			for (let i: number = 0; (i < this.topErgebnisse.size()) && (!eingefuegt); i++) {
				if (this.algorithmenK[iK].dynDaten.gibIstBesserAls1NW2KD3FW(this.topErgebnisse.get(i))) {
					this.topErgebnisse.add(i, this.algorithmenK[iK].dynDaten);
					eingefuegt = true;
				}
			}
			if (eingefuegt) {
				verbesserungen++;
				if (this.topErgebnisse.size() > KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE) {
					this.topErgebnisse.removeLast();
				}
			} else
				if (this.topErgebnisse.size() < KursblockungAlgorithmusPermanent.TOP_ERGEBNISSE) {
					this.topErgebnisse.addLast(this.algorithmenK[iK].dynDaten);
					verbesserungen++;
				}
		}
		this.algorithmenK = this.erzeugeAlgorithmenNeu();
		this.zeitMax = (this.zeitMax * 1.5) as number;
		this.zeitRest = this.zeitMax;
		return verbesserungen;
	}

	/**
	 * Liefert ein zufälliges Element aus der TOP-Liste (oder NULL);
	 *
	 * @return ein zufälliges Element aus der TOP-Liste (oder NULL);
	 */
	private gibTopElementOrNull(): KursblockungDynDaten | null {
		if (this.topErgebnisse.isEmpty()) {
			return null;
		}
		const index: number = this.rnd.nextInt(this.topErgebnisse.size());
		return this.topErgebnisse.get(index);
	}

	/**
	 * Liefert die Liste der aktuellen Top-Blockungsergebnisse.
	 * <br> Die ID der Blockungsergebnisse entspricht dem Index in der TOP-Liste.
	 *
	 * @return die Liste der aktuellen Top-Blockungsergebnisse.
	 */
	public getBlockungsergebnisse(): List<GostBlockungsergebnisManager> {
		const list: List<GostBlockungsergebnisManager> = new ArrayList<GostBlockungsergebnisManager>();
		for (let i: number = 0; i < this.topErgebnisse.size(); i++) {
			list.add(this.topErgebnisse.get(i).gibErzeugtesKursblockungOutput(this.input, i));
		}
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
