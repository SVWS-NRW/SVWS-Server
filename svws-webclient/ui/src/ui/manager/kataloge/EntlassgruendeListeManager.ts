import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { KatalogEntlassgrund } from '../../../../../core/src/core/data/kataloge/KatalogEntlassgrund';
import type { List } from '../../../../../core/src/java/util/List';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { HashSet } from "../../../../../core/src/java/util/HashSet";
import type { JavaSet } from "../../../../../core/src/java/util/JavaSet";

export class EntlassgruendeListeManager extends AuswahlManager<number, KatalogEntlassgrund, KatalogEntlassgrund> {

	private static readonly _entlassgrundToId: JavaFunction<KatalogEntlassgrund, number> = { apply: (a: KatalogEntlassgrund) => a.id };

	private readonly idsReferencedEntlassgruende: HashSet<number> = new HashSet<number>();

	private _filterNurSichtbar: boolean = true;
	/**
	 * Ein Default-Comparator für den Vergleich von Entlassgründen.
	 */
	public static readonly comparator: Comparator<KatalogEntlassgrund> = { compare: (a: KatalogEntlassgrund, b: KatalogEntlassgrund) => {
		let cmp: number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp !== 0) {
			return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Entlassgrundauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param entlassgruende     			  die Liste der Entlassgründe
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, entlassgruende: List<KatalogEntlassgrund>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, entlassgruende, EntlassgruendeListeManager.comparator,
			EntlassgruendeListeManager._entlassgrundToId, EntlassgruendeListeManager._entlassgrundToId, ArrayList.of());
	}

	/**
	 *Gibt das Set mit den Ids der Entlassgruende zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Entlassgruende, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedEntlassgruende(): JavaSet<number> {
		return this.idsReferencedEntlassgruende;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsReferencedEntlassgruende.clear();
		for (const e of this.liste.auswahl()) {
			if ((e.referenziertInAnderenTabellen !== null) && e.referenziertInAnderenTabellen) {
				this.idsReferencedEntlassgruende.add(e.id);
			}
		}
	}

	protected checkFilter(eintrag: KatalogEntlassgrund): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return true;
	}

	protected compareAuswahl(a: KatalogEntlassgrund, b: KatalogEntlassgrund): number {
		return EntlassgruendeListeManager.comparator.compare(a, b);
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Fächer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Fächer zurück.
	 *
	 * @return true, wenn nur sichtbare Fächer angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

}
