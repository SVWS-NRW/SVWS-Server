import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { Merkmal } from '../../../../core/data/schule/Merkmal';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { JavaString } from '../../../../java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../java/util/Comparator';

export class MerkmaleListeManager extends AuswahlManager<number, Merkmal, Merkmal> {

	private static readonly _merkmalToId : JavaFunction<Merkmal, number> = { apply : (m: Merkmal) => m.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Merkmalen.
	 */
	public static readonly comparator : Comparator<Merkmal> = { compare : (a: Merkmal, b: Merkmal) => {
		if ((a.bezeichnung !== null) && (b.bezeichnung !== null)) {
			let cmp : number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp === 0)
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
	 * @param merkmale     			          die Liste der Merkmale
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, abschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, merkmale : List<Merkmal>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, abschnitte, schulform, merkmale, MerkmaleListeManager.comparator, MerkmaleListeManager._merkmalToId, MerkmaleListeManager._merkmalToId, ArrayList.of());
	}

	protected checkFilter(merkmal : Merkmal | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Merkmal, b : Merkmal) : number {
		return MerkmaleListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.merkmale.MerkmaleListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.merkmale.MerkmaleListeManager'].includes(name);
	}

	public static class = new Class<MerkmaleListeManager>('de.svws_nrw.core.utils.kataloge.merkmale.MerkmaleListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_merkmale_MerkmaleListeManager(obj : unknown) : MerkmaleListeManager {
	return obj as MerkmaleListeManager;
}
