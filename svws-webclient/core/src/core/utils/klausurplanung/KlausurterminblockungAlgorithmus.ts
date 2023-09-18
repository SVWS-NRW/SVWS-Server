import { JavaObject } from '../../../java/lang/JavaObject';
import { GostKlausurterminblockungErgebnis } from '../../../core/data/gost/klausurplanung/GostKlausurterminblockungErgebnis';
import { KlausurterminblockungModusKursarten } from '../../../core/types/gost/klausurplanung/KlausurterminblockungModusKursarten';
import { GostKlausurterminblockungDaten } from '../../../core/data/gost/klausurplanung/GostKlausurterminblockungDaten';
import { GostKursklausur } from '../../../core/data/gost/klausurplanung/GostKursklausur';
import { GostKlausurterminblockungKonfiguration } from '../../../core/data/gost/klausurplanung/GostKlausurterminblockungKonfiguration';
import { ArrayList } from '../../../java/util/ArrayList';
import { KlausurterminblockungAlgorithmusGreedy1 } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusGreedy1';
import { KlausurterminblockungAlgorithmusGreedy2 } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusGreedy2';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KlausurterminblockungAlgorithmusGreedy3 } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusGreedy3';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../core/logger/Logger';
import { System } from '../../../java/lang/System';
import type { Comparator } from '../../../java/util/Comparator';
import { Random } from '../../../java/util/Random';
import { KlausurterminblockungDynDaten } from '../../../core/utils/klausurplanung/KlausurterminblockungDynDaten';
import { KlausurterminblockungAlgorithmusGreedy1b } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusGreedy1b';
import { KlausurterminblockungModusQuartale } from '../../../core/types/gost/klausurplanung/KlausurterminblockungModusQuartale';
import { KlausurterminblockungAlgorithmusAbstract } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusAbstract';
import { KlausurterminblockungAlgorithmusGreedy2b } from '../../../core/utils/klausurplanung/KlausurterminblockungAlgorithmusGreedy2b';
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
	 * Berechnet eine Liste von Terminen, denen die IDs der übergebenen Kurs-Klausuren zugeordnetet sind.
	 *
	 * @param daten   die Kurs-Klausuren mit Schülern und die Konfiguration als Eingabe für den Blockungs-Algorithmus
	 *
	 * @return die Liste der Termine
	 */
	public apply(daten : GostKlausurterminblockungDaten) : GostKlausurterminblockungErgebnis {
		const out : GostKlausurterminblockungErgebnis = new GostKlausurterminblockungErgebnis();
		this.berechneRekursivQuartalsModus(daten.klausuren, daten.konfiguration, out);
		return out;
	}

	private berechneRekursivQuartalsModus(input : List<GostKursklausur>, config : GostKlausurterminblockungKonfiguration, out : GostKlausurterminblockungErgebnis) : void {
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

	private berechneRekursivLkGkModus(input : List<GostKursklausur>, config : GostKlausurterminblockungKonfiguration, out : GostKlausurterminblockungErgebnis) : void {
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
	 * @param input   die Menge der Klausuren (Eingabe).
	 * @param config  die Konfiguration der Blockung.
	 * @param out     die Termin-Klausur-Zuordnung (Ausgabe).
	 */
	private berechne_helper(input : List<GostKursklausur>, config : GostKlausurterminblockungKonfiguration, out : GostKlausurterminblockungErgebnis) : void {
		this._logger.log("KlausurterminblockungAlgorithmus");
		this._logger.modifyIndent(+4);
		const zeitEndeGesamt : number = System.currentTimeMillis() + config.maxTimeMillis;
		const seed : number = KlausurterminblockungAlgorithmus._random.nextLong();
		const random : Random = new Random(seed);
		const dynDaten : KlausurterminblockungDynDaten | null = new KlausurterminblockungDynDaten(this._logger, random, input, config);
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
		out.termine.addAll(dynDaten.gibErzeugeOutput().termine);
		this._logger.modifyIndent(-4);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klausurplanung_KlausurterminblockungAlgorithmus(obj : unknown) : KlausurterminblockungAlgorithmus {
	return obj as KlausurterminblockungAlgorithmus;
}
