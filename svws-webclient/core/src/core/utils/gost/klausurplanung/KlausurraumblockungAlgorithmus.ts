import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';
import { GostKlausurraumblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurraumblockungKonfiguration';
import { Pair } from '../../../../core/adt/Pair';

export class KlausurraumblockungAlgorithmus extends JavaObject {


	public constructor() {
		super();
	}

	/**
	 * @param config   		  Die Konfiguration
	 *
	 * @return Eine Liste von Paaren: 1. Element = GostKlausurraumRich (Nachschreiber), 2. Element = Liste von GostSchuelerklausurTerminRich-Objekten
	 *
	 * Harte Parameter sind:
	 * - Die Raumkapazität darf nicht überschritten werden
	 * - Es dürfen nur Klausuren in einen Raum geblockt werden, die dieselbe Startzeit haben.
	 */
	public berechne(config : GostKlausurraumblockungKonfiguration) : List<Pair<GostKlausurraumRich, List<GostSchuelerklausurTerminRich | null>>> {
		return new ArrayList<Pair<GostKlausurraumRich, List<GostSchuelerklausurTerminRich | null>>>();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurraumblockungAlgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurraumblockungAlgorithmus'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurraumblockungAlgorithmus(obj : unknown) : KlausurraumblockungAlgorithmus {
	return obj as KlausurraumblockungAlgorithmus;
}
