import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { Fahrschuelerart } from '../../../../core/data/schule/Fahrschuelerart';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { JavaString } from '../../../../java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../java/util/Comparator';

export class FahrschuelerartenListeManager extends AuswahlManager<number, Fahrschuelerart, Fahrschuelerart> {

	private static readonly _fahrschuelerartToId : JavaFunction<Fahrschuelerart, number> = { apply : (m: Fahrschuelerart) => m.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Fahrschülerarten.
	 */
	public static readonly comparator : Comparator<Fahrschuelerart> = { compare : (a: Fahrschuelerart, b: Fahrschuelerart) => {
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
	 * @param abschnitte           			  die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param fahrschuelerarten		          die Liste der Fahrschülerarten
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, abschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, fahrschuelerarten : List<Fahrschuelerart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, abschnitte, schulform, fahrschuelerarten, FahrschuelerartenListeManager.comparator, FahrschuelerartenListeManager._fahrschuelerartToId, FahrschuelerartenListeManager._fahrschuelerartToId, ArrayList.of());
	}

	protected checkFilter(fahrschuelerart : Fahrschuelerart | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Fahrschuelerart, b : Fahrschuelerart) : number {
		return FahrschuelerartenListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.fahrschuelerarten.FahrschuelerartenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.fahrschuelerarten.FahrschuelerartenListeManager'].includes(name);
	}

	public static class = new Class<FahrschuelerartenListeManager>('de.svws_nrw.core.utils.kataloge.fahrschuelerarten.FahrschuelerartenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_fahrschuelerarten_FahrschuelerartenListeManager(obj : unknown) : FahrschuelerartenListeManager {
	return obj as FahrschuelerartenListeManager;
}
