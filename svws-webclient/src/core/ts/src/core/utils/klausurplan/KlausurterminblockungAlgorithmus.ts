import { JavaObject } from '../../../java/lang/JavaObject';
import { KlausurterminblockungModusKursarten } from '../../../core/types/gost/klausurplanung/KlausurterminblockungModusKursarten';
import { KlausurterminblockungAlgorithmusConfig } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusConfig';
import { GostKursklausur } from '../../../core/data/gost/klausuren/GostKursklausur';
import { ArrayList } from '../../../java/util/ArrayList';
import { KlausurterminblockungAlgorithmusGreedy1 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1';
import { KlausurterminblockungAlgorithmusGreedy2 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KlausurterminblockungAlgorithmusGreedy3 } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy3';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../core/logger/Logger';
import { System } from '../../../java/lang/System';
import type { Comparator } from '../../../java/util/Comparator';
import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplan/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusGreedy1b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy1b';
import { KlausurterminblockungModusQuartale } from '../../../core/types/gost/klausurplanung/KlausurterminblockungModusQuartale';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusAbstract';
import { KlausurterminblockungAlgorithmusGreedy2b } from '../../../core/utils/klausurplan/KlausurterminblockungAlgorithmusGreedy2b';
import type { List } from '../../../java/util/List';

export class KlausurterminblockungAlgorithmus extends JavaObject {

	private static readonly _random : Random = new Random();

	private static readonly _compGostKursklausur : Comparator<GostKursklausur> = { compare : (a: GostKursklausur, b: GostKursklausur) => {
		if (a.halbjahr < b.halbjahr)
			return -1;
		if (a.halbjahr > b.halbjahr)
			return +1;
		if (a.quartal < b.quartal)
			return -1;
		if (a.quartal > b.quartal)
			return +1;
		return 0;
	} };

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
	 * @param input   Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param config  Das Konfigurationsobjekt für den Algorithmus.
	 *
	 * @return eine Liste (Termine, Ebene 1) von Listen (KlausurIDs, Ebene 2).
	 */
	public berechne(input : List<GostKursklausur>, config : KlausurterminblockungAlgorithmusConfig) : List<List<number>> {
		const out : List<List<number>> = new ArrayList();
		this.berechneRekursivQuartalsModus(input, config, out);
		return out;
	}

	private berechneRekursivQuartalsModus(input : List<GostKursklausur>, config : KlausurterminblockungAlgorithmusConfig, out : List<List<number>>) : void {
		if (input.isEmpty())
			return;
		if (config.modusQuartale === KlausurterminblockungModusQuartale.ZUSAMMEN.id) {
			this.berechneRekursivLkGkModus(input, config, out);
			return;
		}
		input.sort(KlausurterminblockungAlgorithmus._compGostKursklausur);
		const temp : List<GostKursklausur> = new ArrayList();
		for (const klausur of input) {
			if (temp.isEmpty()) {
				temp.add(klausur);
				continue;
			}
			if (KlausurterminblockungAlgorithmus._compGostKursklausur.compare(klausur, temp.get(0)) === 0) {
				temp.add(klausur);
				continue;
			}
			this.berechneRekursivLkGkModus(temp, config, out);
			temp.clear();
			temp.add(klausur);
		}
		if (!temp.isEmpty()) {
			this.berechneRekursivLkGkModus(temp, config, out);
			temp.clear();
		}
	}

	private berechneRekursivLkGkModus(input : List<GostKursklausur>, config : KlausurterminblockungAlgorithmusConfig, out : List<List<number>>) : void {
		const modus : KlausurterminblockungModusKursarten = KlausurterminblockungModusKursarten.getOrException(config.modusKursarten);
		switch (modus) {
			case KlausurterminblockungModusKursarten.BEIDE: {
				this.berechne_helper(input, config, out);
				break;
			}
			case KlausurterminblockungModusKursarten.NUR_LK: {
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(input, true), config, out);
				break;
			}
			case KlausurterminblockungModusKursarten.NUR_GK: {
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(input, false), config, out);
				break;
			}
			case KlausurterminblockungModusKursarten.GETRENNT: {
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(input, true), config, out);
				this.berechne_helper(KlausurterminblockungAlgorithmus.filter(input, false), config, out);
				break;
			}
			default: {
				throw new DeveloperNotificationException("Der LK-GK-Modus ist unbekannt!")
			}
		}
	}

	/**
	 * Liefert die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 *
	 * @param input   Die Liste, die gefiltert werden soll.
	 * @param istLK   Falls TRUE, werden die LK-Klausuren herausgefiltert, andernfalls das Gegenteil.
	 *
	 * @return die Liste pInput nach LK-Klausuren (oder dem Gegenteil) gefiltert heraus.
	 */
	private static filter(input : List<GostKursklausur>, istLK : boolean) : List<GostKursklausur> {
		const temp : List<GostKursklausur> = new ArrayList();
		for (const gostKursklausur of input)
			if (JavaObject.equalsTranspiler(gostKursklausur.kursart, ("LK")) === istLK)
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
		const zeitEndeGesamt : number = System.currentTimeMillis() + pConfig.maxTimeMillis;
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
