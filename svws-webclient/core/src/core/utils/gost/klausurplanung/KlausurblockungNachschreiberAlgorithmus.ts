import { JavaObject } from '../../../../java/lang/JavaObject';
import { Random } from '../../../../java/util/Random';
import { GostKursklausurManager } from '../../../../core/utils/gost/klausurplanung/GostKursklausurManager';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Logger, cast_de_svws_nrw_core_logger_Logger } from '../../../../core/logger/Logger';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { Pair } from '../../../../core/adt/Pair';

export class KlausurblockungNachschreiberAlgorithmus extends JavaObject {

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
	 * @param pNachschreiber        Die Eingabe beinhaltet alle Schülerklausurtermine, die auf die Klausurtermine in der Liste pTermine verteilt werden müssen.
	 * @param pTermine				Die Liste der GostKlausurtermine, auf die die Nachschreiber verteilt werden sollen
	 * @param pManager				Der Kursklausur-Manager
	 * @param pMaxTimeMillis  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @return Eine Liste von Paaren: 1. Element = GostSchuelerklausurtermin (Nachschreiber), 2. Element = ID des Termins / der Schiene
	 */
	public berechne(pNachschreiber : List<GostSchuelerklausurTermin>, pTermine : List<GostKlausurtermin>, pManager : GostKursklausurManager, pMaxTimeMillis : number) : List<Pair<GostSchuelerklausurTermin, number>> {
		for (let skt of pNachschreiber) {
			let sk : GostSchuelerklausur | null = pManager.schuelerklausurBySchuelerklausurtermin(skt);
			let idSchueler : number = sk.idSchueler;
			let idKurs : number = sk.idKursklausur;
			let schiene1 : GostKlausurtermin | null = pTermine.getFirst();
			if (schiene1 !== null) {
				let schuelerklausuren : List<GostSchuelerklausur | null> | null = pManager.schuelerklausurGetMengeByTerminid(schiene1.id);
			}
		}
		return new ArrayList();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungNachschreiberAlgorithmus(obj : unknown) : KlausurblockungNachschreiberAlgorithmus {
	return obj as KlausurblockungNachschreiberAlgorithmus;
}
