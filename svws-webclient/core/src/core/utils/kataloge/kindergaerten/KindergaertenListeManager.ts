import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import { Kindergarten } from '../../../../core/data/schule/Kindergarten';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { JavaString } from '../../../../java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../java/util/Comparator';

export class KindergaertenListeManager extends AuswahlManager<number, Kindergarten, Kindergarten> {

	private static readonly _kindergartenToId : JavaFunction<Kindergarten, number> = { apply : (k: Kindergarten) => k.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Kindergaärten
	 */
	public static readonly comparator : Comparator<Kindergarten> = { compare : (a: Kindergarten, b: Kindergarten) => {
		let cmp : number = JavaInteger.compare(a.sortierung, b.sortierung);
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
	 * @param kindergaerten     			  die Liste der Kindergärten
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, kindergaerten : List<Kindergarten>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, kindergaerten, KindergaertenListeManager.comparator, KindergaertenListeManager._kindergartenToId, KindergaertenListeManager._kindergartenToId, ArrayList.of());
	}

	protected checkFilter(eintrag : Kindergarten | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Kindergarten, b : Kindergarten) : number {
		return KindergaertenListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.kindergaerten.KindergaertenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.kindergaerten.KindergaertenListeManager'].includes(name);
	}

	public static class = new Class<KindergaertenListeManager>('de.svws_nrw.core.utils.kataloge.kindergaerten.KindergaertenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_kindergaerten_KindergaertenListeManager(obj : unknown) : KindergaertenListeManager {
	return obj as KindergaertenListeManager;
}
