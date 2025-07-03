import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { Haltestelle } from '../../../../core/data/schule/Haltestelle';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { JavaString } from '../../../../java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../java/util/Comparator';

export class HaltestellenListeManager extends AuswahlManager<number, Haltestelle, Haltestelle> {

	private static readonly _haltestelleToId : JavaFunction<Haltestelle, number> = { apply : (h: Haltestelle) => h.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Haltestellen.
	 */
	public static readonly comparator : Comparator<Haltestelle> = { compare : (a: Haltestelle, b: Haltestelle) => {
		let cmp : number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0)
			return cmp;
		if ((a.bezeichnung !== null) && (b.bezeichnung !== null)) {
			cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0)
				return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param haltestellen	     			  die Liste der Haltestellen
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, haltestellen : List<Haltestelle>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, haltestellen, HaltestellenListeManager.comparator, HaltestellenListeManager._haltestelleToId, HaltestellenListeManager._haltestelleToId, ArrayList.of());
	}

	protected checkFilter(haltestelle : Haltestelle | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Haltestelle, b : Haltestelle) : number {
		return HaltestellenListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.haltestellen.HaltestellenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.haltestellen.HaltestellenListeManager'].includes(name);
	}

	public static class = new Class<HaltestellenListeManager>('de.svws_nrw.core.utils.kataloge.haltestellen.HaltestellenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_haltestellen_HaltestellenListeManager(obj : unknown) : HaltestellenListeManager {
	return obj as HaltestellenListeManager;
}
