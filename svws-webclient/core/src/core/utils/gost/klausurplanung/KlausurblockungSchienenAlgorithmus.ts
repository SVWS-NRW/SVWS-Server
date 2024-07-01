import { JavaObject } from '../../../../java/lang/JavaObject';
import { KlausurblockungSchienenAlgorithmusGreedy3 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy3';
import { KlausurblockungSchienenDynDaten } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenDynDaten';
import { KlausurblockungSchienenAlgorithmusGreedy2 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy2';
import { KlausurblockungSchienenAlgorithmusGreedy5 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy5';
import { KlausurblockungSchienenAlgorithmusGreedy4 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy4';
import { KlausurblockungSchienenAlgorithmusGreedy1 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy1';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../../core/logger/Logger';
import { KlausurblockungSchienenAlgorithmusGreedy7 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy7';
import { KlausurblockungSchienenAlgorithmusGreedy6 } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy6';
import { System } from '../../../../java/lang/System';
import { KlausurblockungSchienenAlgorithmusGreedy1b } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy1b';
import { KlausurblockungSchienenAlgorithmusAbstract } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusAbstract';
import { KlausurblockungSchienenAlgorithmusGreedy2b } from '../../../../core/utils/gost/klausurplanung/KlausurblockungSchienenAlgorithmusGreedy2b';
import { Random } from '../../../../java/util/Random';
import type { List } from '../../../../java/util/List';
import { GostKursklausurRich } from '../../../../core/data/gost/klausurplanung/GostKursklausurRich';

export class KlausurblockungSchienenAlgorithmus extends JavaObject {

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
		if ((__param0 === undefined)) {
			this._logger = new Logger();
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.logger.Logger'))))) {
			const pLogger : Logger = cast_de_svws_nrw_core_logger_Logger(__param0);
			this._logger = pLogger;
		} else throw new Error('invalid method overload');
	}

	/**
	 * @param pInput          Die Eingabe beinhaltet alle Klausuren, welche die SuS beinhalten.
	 * @param pMaxTimeMillis  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @return Eine Liste von Listen: 1. Ebene = Schienen, 2. Ebene = KlausurIDs
	 */
	public berechne(pInput : List<GostKursklausurRich>, pMaxTimeMillis : number) : List<List<number>> {
		const zeitEndeGesamt : number = System.currentTimeMillis() + pMaxTimeMillis;
		const seed : number = KlausurblockungSchienenAlgorithmus._random.nextLong();
		const random : Random = new Random(seed);
		const dynDaten : KlausurblockungSchienenDynDaten | null = new KlausurblockungSchienenDynDaten(this._logger, random, pInput);
		const algorithmen : Array<KlausurblockungSchienenAlgorithmusAbstract> = [new KlausurblockungSchienenAlgorithmusGreedy3(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy4(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy1b(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy2b(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy5(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy6(random, dynDaten), new KlausurblockungSchienenAlgorithmusGreedy7(random, dynDaten)];
		dynDaten.aktion_EntferneAlles_SchienenNacheinander_KlausurenZufaellig();
		dynDaten.aktionZustand2Speichern();
		let zeitProAlgorithmus : number = 10;
		do {
			for (const algorithmus of algorithmen) {
				const zeitEndeRunde : number = System.currentTimeMillis() + zeitProAlgorithmus;
				algorithmus.berechne(zeitEndeRunde);
			}
			zeitProAlgorithmus *= 2;
		} while ((System.currentTimeMillis() + (algorithmen.length * zeitProAlgorithmus)) <= zeitEndeGesamt);
		dynDaten.aktionZustand2Laden();
		return dynDaten.gibErzeugeOutput();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungSchienenAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungSchienenAlgorithmus(obj : unknown) : KlausurblockungSchienenAlgorithmus {
	return obj as KlausurblockungSchienenAlgorithmus;
}
