import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import { KatalogEntlassgrund } from '../../../../core/data/kataloge/KatalogEntlassgrund';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { JavaString } from '../../../../java/lang/JavaString';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../java/util/Comparator';

export class EntlassgruendeListeManager extends AuswahlManager<number, KatalogEntlassgrund, KatalogEntlassgrund> {

	private static readonly _entlassgrundToId : JavaFunction<KatalogEntlassgrund, number> = { apply : (a: KatalogEntlassgrund) => a.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Abteilungen.
	 */
	public static readonly comparator : Comparator<KatalogEntlassgrund> = { compare : (a: KatalogEntlassgrund, b: KatalogEntlassgrund) => {
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
	 * @param entlassgruende     			  die Liste der Entlassgründe
	 */
	public constructor(idSchuljahresabschnitt : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, entlassgruende : List<KatalogEntlassgrund>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, entlassgruende, EntlassgruendeListeManager.comparator, EntlassgruendeListeManager._entlassgrundToId, EntlassgruendeListeManager._entlassgrundToId, ArrayList.of());
	}

	protected checkFilter(eintrag : KatalogEntlassgrund | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : KatalogEntlassgrund, b : KatalogEntlassgrund) : number {
		return EntlassgruendeListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.entlassgruende.EntlassgruendeListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.entlassgruende.EntlassgruendeListeManager'].includes(name);
	}

	public static class = new Class<EntlassgruendeListeManager>('de.svws_nrw.core.utils.kataloge.entlassgruende.EntlassgruendeListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_entlassgruende_EntlassgruendeListeManager(obj : unknown) : EntlassgruendeListeManager {
	return obj as EntlassgruendeListeManager;
}
