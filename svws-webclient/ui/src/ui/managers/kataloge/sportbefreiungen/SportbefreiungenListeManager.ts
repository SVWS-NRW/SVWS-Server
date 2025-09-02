import { AuswahlManager } from '../../../AuswahlManager';
import { JavaInteger } from '../../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../../core/src/java/util/function/JavaFunction';
import { Schulform } from '../../../../../../core/src/asd/types/schule/Schulform';
import { JavaLong } from '../../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../../core/src/java/util/ArrayList';
import { Sportbefreiung } from '../../../../../../core/src/core/data/schule/Sportbefreiung';
import type { List } from '../../../../../../core/src/java/util/List';
import { Class } from '../../../../../../core/src/java/lang/Class';
import { JavaString } from '../../../../../../core/src/java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../../../core/src/java/util/Comparator';

export class SportbefreiungenListeManager extends AuswahlManager<number, Sportbefreiung, Sportbefreiung> {

	private static readonly _sportbefreiungToId : JavaFunction<Sportbefreiung, number> = { apply : (h: Sportbefreiung) => h.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Sportbefreiungen.
	 */
	public static readonly comparator : Comparator<Sportbefreiung> = { compare : (a: Sportbefreiung, b: Sportbefreiung) => {
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
	 * @param sportbefreiungen				  die Liste der Sportbefreiungen
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, sportbefreiungen : List<Sportbefreiung>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, sportbefreiungen, SportbefreiungenListeManager.comparator, SportbefreiungenListeManager._sportbefreiungToId, SportbefreiungenListeManager._sportbefreiungToId, ArrayList.of());
	}

	protected checkFilter(sportbefreiung : Sportbefreiung | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Sportbefreiung, b : Sportbefreiung) : number {
		return SportbefreiungenListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.sportbefreiungen.SportbefreiungenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.sportbefreiungen.SportbefreiungenListeManager'].includes(name);
	}

	public static class = new Class<SportbefreiungenListeManager>('de.svws_nrw.core.utils.kataloge.sportbefreiungen.SportbefreiungenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_sportbefreiungen_SportbefreiungenListeManager(obj : unknown) : SportbefreiungenListeManager {
	return obj as SportbefreiungenListeManager;
}
