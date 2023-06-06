import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurterminblockungAlgorithmusConfig } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusConfig';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { ArrayList } from '../../../java/util/ArrayList';
import { KlausurterminblockungAlgorithmusGreedy1 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1';
import { KlausurterminblockungAlgorithmusGreedy2 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KlausurterminblockungAlgorithmusGreedy3 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy3';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../core/logger/Logger';
import { System } from '../../../java/lang/System';
import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplan/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusGreedy1b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1b';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusAbstract';
import { KlausurterminblockungAlgorithmusGreedy2b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2b';
import type { List } from '../../../java/util/List';

export class KlausurterminblockungAlgorithmus extends JavaObject {

	private static readonly _random : Random = new Random();

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	private readonly _logger : Logger;


	/**
	 * Der Konstruktor.
	 */
	public constructor();

	/**
	 * Der Konstruktor.
	 *
	 * @param pLogger  Ein Logger für Debug-Zwecke.
	 */
	public constructor(pLogger : Logger);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : Logger) {
		super();
		if ((typeof __param0 === "undefined")) {
			this._logger = new Logger();
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.logger.Logger'))))) {
			const pLogger : Logger = cast_de_svws_nrw_core_logger_Logger(__param0);
			this._logger = pLogger;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Liefert eine Liste (Termine, Ebene 1) von Listen (KlausurIDs, Ebene 2).
	 *
	 * @param pInput   Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pConfig  Das Konfigurationsobjekt für den Algorithmus.
	 *
	 * @return eine Liste (Termine, Ebene 1) von Listen (KlausurIDs, Ebene 2).
	 */
	public berechne(pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig) : List<List<number>> {
		const out : List<List<number>> = new ArrayList();
		switch (pConfig.get_lk_gk_modus()) {
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_BEIDE: {
				this.berechne_helper(pInput, pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_LK: {
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, true), pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_NUR_GK: {
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, false), pConfig, out);
				break;
			}
			case KlausurterminblockungAlgorithmusConfig.LK_GK_MODUS_GETRENNT: {
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, true), pConfig, out);
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(pInput, false), pConfig, out);
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!")
			}
		}
		return out;
	}

	/**
	 * Liefert die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 *
	 * @param pInput Die Liste, die gefiltert werden soll.
	 * @param pLK    Falls TRUE, werden die LK-Klausuren herausgefiltert, andernfalls das Gegenteil.
	 *
	 * @return die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 */
	private static filter(pInput : List<GostKursklausur>, pLK : boolean) : List<GostKursklausur> {
		const temp : List<GostKursklausur> = new ArrayList();
		for (const gostKursklausur of pInput)
			if (JavaObject.equalsTranspiler(gostKursklausur.kursart, ("LK")) === pLK)
				temp.add(gostKursklausur);
		return temp;
	}

	/**
	 * Berechnet eine Klausurblockung unter Verwendung verschiedener internen Algorithmen.
	 *
	 * @param pInput  Die Menge der Klausuren (Eingabe).
	 * @param pConfig Die Konfiguration der Blockung.
	 * @param pOut    Die Termin-Klausur-Zuordnung (Ausgabe).
	 */
	private berechne_helper(pInput : List<GostKursklausur>, pConfig : KlausurterminblockungAlgorithmusConfig, pOut : List<List<number>>) : void {
		this._logger.log("KlausurterminblockungAlgorithmus");
		this._logger.modifyIndent(+4);
		const zeitEndeGesamt : number = System.currentTimeMillis() + pConfig.get_max_time_millis();
		const seed : number = KlausurterminblockungAlgorithmus._random.nextLong();
		const random : Random = new Random(seed);
		const dynDaten : KlausurterminblockungDynDaten | null = new KlausurterminblockungDynDaten(this._logger, random, pInput, pConfig);
		const algorithmen : Array<KlausurterminblockungAlgorithmusAbstract> = [new KlausurterminblockungAlgorithmusGreedy1(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy1b(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy2(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy2b(random, dynDaten), new KlausurterminblockungAlgorithmusGreedy3(random, dynDaten)];
		dynDaten.aktion_Clear_TermineNacheinander_GruppeZufaellig();
		dynDaten.aktionZustand2Speichern();
		let zeitProAlgorithmus : number = 10;
		do {
			this._logger.log("zeitProAlgorithmus --> " + zeitProAlgorithmus);
			for (let iAlgo : number = 0; iAlgo < algorithmen.length; iAlgo++) {
				const zeitEndeRunde : number = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmen[iAlgo].berechne(zeitEndeRunde);
				this._logger.log(algorithmen[iAlgo].toString()! + " --> " + dynDaten.gibTerminAnzahl());
			}
			zeitProAlgorithmus *= 2;
		} while (System.currentTimeMillis() + algorithmen.length * zeitProAlgorithmus <= zeitEndeGesamt);
		dynDaten.aktionZustand2Laden();
		pOut.addAll(dynDaten.gibErzeugeOutput());
		this._logger.modifyIndent(-4);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplan.KlausurterminblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplan_KlausurterminblockungAlgorithmus(obj : unknown) : KlausurterminblockungAlgorithmus {
	return obj as KlausurterminblockungAlgorithmus;
}
