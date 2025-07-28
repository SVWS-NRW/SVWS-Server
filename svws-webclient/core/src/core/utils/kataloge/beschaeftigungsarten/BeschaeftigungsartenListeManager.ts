import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { JavaString } from '../../../../java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import { Beschaeftigungsart } from '../../../../core/data/betrieb/Beschaeftigungsart';
import type { Comparator } from '../../../../java/util/Comparator';

export class BeschaeftigungsartenListeManager extends AuswahlManager<number, Beschaeftigungsart, Beschaeftigungsart> {

	private static readonly _beschaeftigungsartToId : JavaFunction<Beschaeftigungsart, number> = { apply : (a: Beschaeftigungsart) => a.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Beschäftigungsarten.
	 */
	public static readonly comparator : Comparator<Beschaeftigungsart> = { compare : (a: Beschaeftigungsart, b: Beschaeftigungsart) => {
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
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param beschaeftigungsarten				die Liste der Beschäftigungsarten
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, beschaeftigungsarten : List<Beschaeftigungsart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, beschaeftigungsarten, BeschaeftigungsartenListeManager.comparator, BeschaeftigungsartenListeManager._beschaeftigungsartToId, BeschaeftigungsartenListeManager._beschaeftigungsartToId, ArrayList.of());
	}

	protected checkFilter(eintrag : Beschaeftigungsart | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Beschaeftigungsart, b : Beschaeftigungsart) : number {
		return BeschaeftigungsartenListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.beschaeftigungsarten.BeschaeftigungsartenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.beschaeftigungsarten.BeschaeftigungsartenListeManager'].includes(name);
	}

	public static class = new Class<BeschaeftigungsartenListeManager>('de.svws_nrw.core.utils.kataloge.beschaeftigungsarten.BeschaeftigungsartenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_beschaeftigungsarten_BeschaeftigungsartenListeManager(obj : unknown) : BeschaeftigungsartenListeManager {
	return obj as BeschaeftigungsartenListeManager;
}
